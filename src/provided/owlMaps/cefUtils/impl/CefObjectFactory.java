package provided.owlMaps.cefUtils.impl;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

import provided.owlMaps.cefUtils.ICefObject;
import provided.owlMaps.cefUtils.ICefObjectFactory;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.gson.GsonTypeWrap;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS CLASS DIRECTLY! ***<br/>
 * An concrete factory for instantiating new ICefObjects
 * @author swong
 *
 */
public class CefObjectFactory implements ICefObjectFactory {

	/**
	 * The name of the Javascript method to make a CEF accessible object
	 */
	public static final String MAKE_CEF_OBJECT = "makeCefObject";

	/**
	 * The name of the Javascript method to make a property settor call on CEF accessible object 
	 */
	public static final String CEF_OBJECT_SET = "setObjProp";

	/**
	 * The name of the Javascript method to make a property gettor call on CEF accessible object 
	 */
	public static final String CEF_OBJECT_GET = "getObjProp";

	/**
	 * The name of the Javascript method to make a method call on CEF accessible object that has a return value
	 */
	public static final String CEF_OBJECT_CALL = "callObj";

	/**
	 * The name of the Javascript method to make a method call on CEF accessible object that has a void return
	 */
	public static final String CEF_OBJECT_CALL_VOID = "callObjVoid";

	/**
	 * A map of IDs to processing functions for objects pending instantiation on the Javascript side.   
	 * Entries MUST be removed from the queue once they have been processed!
	 */
	private Map<UUID, Consumer<UUID>> newObjQueue = new ConcurrentHashMap<UUID, Consumer<UUID>>();

	/**
	 * CEF utilities for internal use.
	 */
	private ICefUtils cefUtils;

	/**
	 * Construct a new factory instance
	 * @param cefUtils The CEF utilties for internal use.  Be careful about circular calls between this factory and the utilities!
	 */
	public CefObjectFactory(ICefUtils cefUtils) {
		this.cefUtils = cefUtils;
	}

	/**
	 * Instantiate a new ICefObject instance given a Supplier of its ID value. 
	 * The use of a Supplier here decouples this process from whether or not the ID 
	 * value is available synchronously or asynchronously.
	 * @param getIdFn A Supplier of the ID value
	 * @return A new ICefObject instance
	 */
	private ICefObject make(Supplier<UUID> getIdFn) {
		return new ICefObject() {

			@Override
			public UUID getCefId() {
				System.out.println("[ICefObject.getId()] called. ");
				return getIdFn.get();
			}

			@Override
			public <T> Future<T> getProperty(String propName, Type resultType) {

				CompletableFuture<T> result = new CompletableFuture<T>();

				cefUtils.call(CEF_OBJECT_GET, resultType, new GsonTypeWrap(this, ICefObject.class),
						propName, new GsonTypeWrap((Consumer<T>) (x) -> {

							T resultVal = cefUtils.convertJsonMap(x, resultType);
							System.out.println("[ICefObject.getProperty()] x = " + x);
							result.complete(resultVal);
						}, Consumer.class));

				return result;
			}

			//			@Override
			//			public boolean hasProperty(String propName) {
			//				UUID id = getId();
			//				// TODO Auto-generated method stub
			//				
			//				return false;
			//			}

			@Override
			public void setProperty(String propName, Object value) {
				cefUtils.callVoid(CEF_OBJECT_SET, new GsonTypeWrap(this, ICefObject.class), propName, value);
			}

			@Override
			public void setProperty(String propName, Object value, Type propType) {
				this.setProperty(propName, new GsonTypeWrap(value, propType));
			}

			@Override
			public <T> Future<T> call(String methodName, Type resultType, Object... params) {
//				CompletableFuture<T> result = new CompletableFuture<T>();

				return  cefUtils.call(CEF_OBJECT_CALL, resultType, new GsonTypeWrap(this, ICefObject.class), methodName, params);
//						params, new GsonTypeWrap((Consumer<T>) (rawResult2) -> {
//							System.out.println("[ICefObject.call()] raw result = " + rawResult2);
//							T resultVal = cefUtils.convertJsonMap(rawResult2, resultType);
//
////							result.complete(resultVal);
//						}, Consumer.class));
				


//				return futureResult;
			}

			@Override
			public void callVoid(String methodName, Object... params) {
				System.out.println("[ICefObject.callVoid()] methodName = "+methodName+", params = "+Arrays.deepToString(params));

				cefUtils.callVoid(CEF_OBJECT_CALL_VOID, new GsonTypeWrap(this, ICefObject.class), methodName,
						params);
			}

			@Override
			public void addEvent(String eventName, Runnable runnableCallback) {
				System.out.println("[ICefObject.addEvent("+eventName+")] Adding Runnable event.");
				cefUtils.callVoid("addEventTo", new GsonTypeWrap(this, ICefObject.class), eventName,
						new GsonTypeWrap((Runnable) () -> {
							System.out.println("[ICefObject.addEvent("+eventName+") runnableCallback] Executing!");
							runnableCallback.run();
							cefUtils.runPostOpCleanup(); // Add post-op cleanup since this event is triggered asynchronously on the Javascript side.
						}, Runnable.class));
			}

			@SuppressWarnings("unchecked")
			@Override
			public <T> void addEvent(String eventName, Consumer<T> consumerCallback, Type paramType) {
				System.out.println("[ICefObject.addEvent("+eventName+")] Adding Consumer event.");
				cefUtils.callVoid("addEventTo", new GsonTypeWrap(this, ICefObject.class), eventName,
						new GsonTypeWrap((Consumer<?>) (param) -> {
							System.out.println("[ICefObject.addEvent() consumerCallback] param = " + param);
							consumerCallback.accept((T) cefUtils.convertJsonMap(param, paramType));
							cefUtils.runPostOpCleanup(); // Add post-op cleanup since this event is triggered asynchronously on the Javascript side.
						}, Consumer.class));

			}
			
			@Override
			public String toString() {
				return "ICefObject("+this.getCefId()+")";
			}
		};

	}


	@Override
	public ICefObject make(String jsClassname, Object... params) {
		// Note: Two separate UUIDs are being used here because the requestId and cefObjId are semantically distinct.
		// The system could theoretically be streamlined by using the same UUID for both at the expense of the semantic differentiation.
		UUID requestId = UUID.randomUUID();
		UUID cefObjId = UUID.randomUUID();

		CompletableFuture<UUID> idFuture = new CompletableFuture<UUID>();

		//		newObjQueue.put(uuid, idFuture::complete);  // all that is technically needed
		newObjQueue.put(requestId, (objId) -> { // extra layer here for debugging purposes
			System.out.println("[ICefObjectFactory.make()] Servicing object creation request ID = " + requestId	+ ", CEF object ID = " + objId);
			idFuture.complete(objId);
		});

		Object[] requiredArgs = new Object[] { requestId.toString(), jsClassname, cefObjId.toString() }; // Using an array here decouples the process from the number of required params.

		Object[] args = new Object[requiredArgs.length + params.length];

		System.arraycopy(requiredArgs, 0, args, 0, requiredArgs.length);
		System.arraycopy(params, 0, args, requiredArgs.length, params.length);

		// call to make new JS object using requestId and cefObjId.   Callback processing should remove consumer from newObjQueue and run it.  
		// See serviceObjectReturn() below.

		cefUtils.callVoid(MAKE_CEF_OBJECT, args);

		return make(() -> {
			try {
				// Block access to the  ID until the ID is ready from the Javascript side
				return idFuture.get();
			} catch (InterruptedException | ExecutionException e) {
				System.err.println("[]ICefObjectFactory.make()] Exception while retrieving CEF object ID: " + e);
				e.printStackTrace();
				return null;
			}
		});

	}

	@Override
	public ICefObject make(final UUID id) {
		return make(() -> {
			return id;
		});
	}

	@Override
	public void serviceObjectReturn(UUID requestId, UUID cefObjId) {

		Consumer<UUID> consumer = newObjQueue.remove(requestId);
		if (null == consumer) {
			System.err.println("[ICefObjectFactory.serviceObjectReturn()] No request processing associated with ID =  "
					+ requestId);
		} else {
			consumer.accept(cefObjId);
		}
	}
}
