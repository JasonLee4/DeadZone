package afv2_jml25.api;

import provided.mixedData.IMixedDataDictionary;

/**
 * Data pertaining to the Google Maps instance including configurations.
 * 
 * @author Andres Villada
 * @author Jason Lee
 */
public class MapData implements IMapData {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 606887440466805206L;

	/**
	 * Configurations for this map instance
	 */
	private IMixedDataDictionary mapOptions;

	/**
	 * Constructor for MapData
	 * 
	 * @param options configurations for this map
	 */
	public MapData(IMixedDataDictionary options) {
		this.mapOptions = options;
	}

	@Override
	public IMixedDataDictionary getMapOptions() {
		return mapOptions;
	}
}
