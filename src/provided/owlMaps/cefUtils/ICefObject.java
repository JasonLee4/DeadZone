package provided.owlMaps.cefUtils;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS INTERFACE DIRECTLY! ***<br/>
 * Represents an object which has a counterpart instance on the JavaScript side.
 * ICefObjects only provide the basic communications services for communicating with the associated JavaScript-side object  
 * Java-side representations of JavaScript-side classes are implemented as wrappers around ICefObjects.
 * @author swong
 *
 */
public interface ICefObject extends ICefObjRef {



	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Get the value of the given property as a Future
	 * @param <T> The return type of the Future
	 * @param propName The name of the desired property
	 * @param propType The type of the desired Properly
	 * @return A Future that will supply the value of the property when it is ready.
	 */
	public <T> Future<T> getProperty(String propName, Type propType);

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Set the property value where the correct type of the value can be retrieved from the value object itself.
	 * This method cannot be used for anonymous inner class implementations because those objects do not 
	 * report the correct superclass type. 
	 * @param propName The name of the desired property
	 * @param value The value for the desired property
	 */
	public void setProperty(String propName, Object value);

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Set the property value with an explicit type for the value.
	 * This method is useful for anonymous inner class implementations.
	 * Note that the system must already be configured to process the supplied type.  
	 * @param propName The name of the desired property
	 * @param value The value for the desired property
	 * @param propType The type of the desired property
	 */
	public void setProperty(String propName, Object value, Type propType);

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Call a method on the associated JavaScript side object that returns a value.
	 * @param <T> The expected return type of the returned value.
	 * @param methodName The name of the method to call.
	 * @param resultType The type of the expected result
	 * @param params Vararg of any arguments to give to the method call
	 * @return The return value of the method call as a Future.
	 */
	public <T> Future<T> call(String methodName, Type resultType, Object... params);

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Call a method on the associated JavaScript side object that has a void return.
	 * @param methodName The name of the method to call.
	 * @param params Vararg of any arguments to give to the method call
	 */
	public void callVoid(String methodName, Object... params);

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Add an event to the associated JavaScript-side object that utilizes a Runnable callback
	 * @param eventName The name of the event type
	 * @param runnableCallback The callback to associate with that event type
	 */
	public void addEvent(String eventName, Runnable runnableCallback);

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Add an event to the associated JavaScript-side object that utilizes a Consumer callback
	 * @param <T> The type of the input to the callback Consumer.
	 * @param eventName The name of the event type
	 * @param consumerCallback The callback to associate with event type
	 * @param paramType The type of the input parameter to the callback.
	 */
	public <T> void addEvent(String eventName, Consumer<T> consumerCallback, Type paramType);

	/**
	 * *** FOR INTERNAL USE ONLY!  DEVELOPER CODE SHOULD NEVER ACCESS THIS OBJECT! ***<br/>
	 * Singleton null object
	 */
	public static final ICefObject NULL = new ICefObject() {

		private UUID id = UUID.randomUUID();

		@Override
		public UUID getCefId() {
			return this.id;
		}

		@Override
		public <T> Future<T> getProperty(String propName, Type propType) {
			throw new IllegalArgumentException("[ICefObject.getProperty()] ICefObject.NULL has no properties.");
		}

		@Override
		public void setProperty(String propName, Object value) {
			throw new IllegalArgumentException("[ICefObject.setProperty()] ICefObject.NULL has no properties.");
		}

		@Override
		public <T> Future<T> call(String methodName, Type resultType, Object... params) {
			throw new IllegalArgumentException("[ICefObject.call()] ICefObject.NULL has no methods.");
		}

		@Override
		public void callVoid(String methodName, Object... params) {
			throw new IllegalArgumentException("[ICefObject.callVoid()] ICefObject.NULL has no methods.");
		}

		//		@Override
		//		public boolean hasProperty(String propName) {
		//			return false;
		//		}

		@Override
		public void setProperty(String propName, Object value, Type propType) {
			throw new IllegalArgumentException("[ICefObject.setProperty()] ICefObject.NULL has no properties.");
		}

		@Override
		public void addEvent(String eventName, Runnable runnableCallback) {
			throw new IllegalArgumentException("[ICefObject.addEvent()] ICefObject.NULL has no Runnable events.");
		}

		@Override
		public <T> void addEvent(String eventNAme, Consumer<T> consumerCallback, Type paramType) {
			throw new IllegalArgumentException("[ICefObject.addEvent()] ICefObject.NULL has no Consumer events.");
		}

	};

}
