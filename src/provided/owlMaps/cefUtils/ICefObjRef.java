package provided.owlMaps.cefUtils;

import java.util.UUID;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS INTERFACE DIRECTLY! ***<br/>
 * This interface represents an entity that exists both on the Java and JavaScript sides
 * of the system.
 * * @author swong
 *
 */
public interface ICefObjRef {
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Get the unique identifier that identifies this object on both the Java and JavaScript sides of the system.
	 * @return The ID value for this object
	 */
	public UUID getCefId();
	
}
