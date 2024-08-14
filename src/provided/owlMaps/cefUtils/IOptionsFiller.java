package provided.owlMaps.cefUtils;


import java.util.UUID;

import com.google.gson.TypeAdapterFactory;

import provided.mixedData.MixedDataKey;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS INTERFACE OR ANY IMPLEMENTATION OF IT! ***<br/>
 * Utility to add options from an IMixedDataDictionary to Javascript options object
 * These processing objects are made by the IXXXOptions interface for the XXX component type and
 * thus individual instances of this interface are associated with a specific kind component.
 * This interface is not normally used by developers working only with the Java side of the system.  
 * @author swong
 *
 */
public interface IOptionsFiller {
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS FIELD DIRECTLY! ***<br/>
	 * The name of the Javascript option key for the host map.
	 */
	public static final String MAP_KEY = "map";
	
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Utility factory for the "map" option key needed by many map components. 
	 * @param optionsId  The ID of the options type
	 * @return An options key for the map option
	 */
	public static MixedDataKey<ICefObject> makeMapKey(UUID optionsId) {
		return new MixedDataKey<ICefObject>(optionsId, MAP_KEY, ICefObject.class);
	}
	
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Factory method to make the TypeAdapterFactory for use by GSON to process a dictionary of a particular type of options.
	 * @return A TypeAdapterFactory instance
	 */
	public TypeAdapterFactory makeTypeAdapterFactory();
	


}
