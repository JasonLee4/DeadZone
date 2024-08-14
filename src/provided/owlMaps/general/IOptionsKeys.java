package provided.owlMaps.general;

import java.io.Serializable;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataDictionary;


/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS INTERFACE! ***<br/>
 * Parent interface of IXXXOptions interfaces
 * Provides base level services.
 * @author swong
 *
 */
public interface IOptionsKeys extends Serializable {
	
	
	/**
	 * Returns an empty options dictionary (IMixedDataDictionary).   
	 * Use this method in sub-interfaces rather than directly instantiating the IMixedDataDictionary
	 * to achieve a consistent base dictionary. 
	 * @return an empty IMixedDataDictionary
	 */
	public static IMixedDataDictionary makeDefault() {
		return new MixedDataDictionary();
	}

}
