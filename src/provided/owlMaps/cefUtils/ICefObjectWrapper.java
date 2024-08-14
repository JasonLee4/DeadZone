package provided.owlMaps.cefUtils;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS INTERFACE DIRECTLY! ***<br/>
 * Interface that represents a wrapper around a ICefObject 
 * providing convenience methods to operate directly on that wrapped object.  
 * The use of a wrapper helps shield and decouple the underlying implementation from the developer code.
 * @author swong
 *
 */
public interface ICefObjectWrapper extends ICefObjRef {
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Return a reference to the wrapped ICefObject
	 * @return The wrapped CEF object
	 */
	public ICefObject getWrappedCefObject();
	
	
	@Override
	default public UUID getCefId() {
		return getWrappedCefObject().getCefId();
	}
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Call a method on the wrapped ICefObject that returns a values.
	 * Will return null if the method name does not exist or
	 * an error occurs during execution.
	 * @param <T> The expected return type of the returned value.
	 * @param methodName The name of the method to call.
	 * @param resultType The type of the expected result
	 * @param args Any arguments to give to the method call
	 * @return The return value of the method call.
	 */
	@SuppressWarnings("unchecked")
	default public <T> T call(String methodName,  Class<? extends T> resultType, Object...args)  {
		try {
			return (T) getWrappedCefObject().call(methodName, resultType, args).get(); // TODO is this correct?   Is there a better way to get the type safety?
		}
		catch (Exception e) {
			System.err.println("["+this.getClass().getName()+ "] RETURNING NULL VALUE!  Exception while calling the method: "+methodName+": "+e);
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Call a method on the wrapped ICefObject that has a void return.
	 * Will print an exception if the method name does not exist or
	 * an error occurs during execution.
	 * @param methodName The name of the method to call.
	 * @param args Any arguments to give to the method call
	 */
	default public void callVoid(String methodName,  Object...args)  {
		try {
			getWrappedCefObject().callVoid(methodName, args);
		}
		catch (Exception e) {
			System.err.println("["+this.getClass().getName()+ "] Exception while calling the method: "+methodName+": "+e);
			e.printStackTrace();
		}
	}
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Get the value of a property of the wrapped JsObject.
	 * Will return null if the property does not exist.
	 * @param <T> The expected type of the requested property value
	 * @param name The name of the property
	 * @param resultType The type of the requested property
	 * @return The value of the property.
	 */
	@SuppressWarnings("unchecked")
	default public <T> T  getCefProperty(String name,  Type resultType)  {
		try {
			return (T) getWrappedCefObject().getProperty(name, resultType).get(); // TODO is this correct?   Is there a better way to get the type safety?

		} 
		catch (Exception e) {
			System.err.println("["+this.getClass().getName()+ "] RETURNING NULL VALUE!  Exception while retrieving the property: "+name+": "+e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Set the value of a property to the given value.  The property is created 
	 * if it doesn't already exist.
	 * @param name The name of the property
	 * @param value The value for the property
	 * @return True if successful, False otherwise.
	 */
	default public boolean setCefProperty(String name, Object value) {
		try {
			// TODO more error checking and value conversion if necessary?
			getWrappedCefObject().setProperty(name, value);
			return true;			
		}
		catch (Exception e) {
			System.err.println("["+this.getClass().getName()+ "] RETURNING FALSE!  Exception while setting the property: "+name+": "+e);
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Add an event to the wrapped object that utilizes a Runnable callback
	 * @param eventName The name of the event type
	 * @param runnableCallback The callback to associate with that event type
	 */
	default public void addEvent(String eventName, Runnable runnableCallback) {
		getWrappedCefObject().addEvent(eventName, runnableCallback);
	}

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Add an event to the wrapped object that utilizes a Consumer callback
	 * @param <T> The type of the input to the callback Consumer.
	 * @param eventName The name of the event type
	 * @param consumerCallback The callback to associate with event type
	 * @param paramType The type of the input parameter to the callback.
	 */
	default public <T> void addEvent(String eventName, Consumer<T> consumerCallback, Type paramType) {
		getWrappedCefObject().addEvent(eventName, consumerCallback, paramType);
	}


}
