package provided.owlMaps.cefUtils;

import java.util.UUID;
import provided.owlMaps.cefUtils.impl.CefObjectFactory;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS INTERFACE DIRECTLY! ***<br/>
 * An abstract factory for instantiating new ICefObjects
 * @author swong
 *
 */
public interface ICefObjectFactory {

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Instantiate a new ICefObject given a JavaScript classname and constructor parameters
	 * @param jsClassname The JavaScript classname for the new object. Use empty string for generic Javascript object
	 * @param params The constructor parameters for the class
	 * @return A new ICefObject instance
	 */
	public ICefObject make(String jsClassname, Object... params);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Instantiate a new ICefObject with a known ID value
	 * @param id The ID for the new ICefObject
	 * @return A new ICefObject instance
	 */
	public ICefObject make(final UUID id);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Service the the returned CEF object ID associated with the given request ID.
	 * @param requestId  The request ID for the object creation process
	 * @param cefObjId The ID of the created CEF object. 
	 */
	public void serviceObjectReturn(UUID requestId, UUID cefObjId);
	
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Make an instance of this factory
	 * @param cefUtils The CEF utilities to use
	 * @return An ICefObjectFactory instance
	 */
	public static ICefObjectFactory makeFac(ICefUtils cefUtils) {
		return new CefObjectFactory(cefUtils);
	}
	
}