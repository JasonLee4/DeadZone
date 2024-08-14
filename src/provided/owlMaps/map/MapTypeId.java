package provided.owlMaps.map;

import java.util.Map;

/**
 * The allowable types of maps to display. 
 * Corresponds Google Maps Javascript API google.maps.MapTypeId
 * @author swong
 *
 */
public enum MapTypeId {
	/**
	 * This map type displays a transparent layer of major streets on satellite images.
	 */
	HYBRID("hybrid"),
	
	/**
	 * This map type displays a normal street map.
	 */
	ROADMAP("roadmap"),
	
	/**
	 * This map type displays satellite images.
	 */
	SATELLITE("satellite"),
	
	/**
	 * This map type displays maps with physical features such as terrain and vegetation.

	 */
	TERRAIN("terrain");
	
	/**
	 * The Google Maps map type name
	 */
	private final String name;

	/**
	 * Construct an enum instance 
	 * @param name The associated Google Maps event name
	 */
	MapTypeId(String name) {
		this.name = name;
	}
	
	/**
	 * Accessor for the associated Google Maps map type name
	 * @return The associated Google Maps map type name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Mapping of Google Maps API MapTypeID string values to the corresponding enum value
	 */
	private static final Map<String, MapTypeId> STR2ID = Map.ofEntries(
			Map.entry(HYBRID.getName(),HYBRID),
			Map.entry(ROADMAP.getName(),ROADMAP),
			Map.entry(SATELLITE.getName(),SATELLITE),
			Map.entry(TERRAIN.getName(),TERRAIN)
	);
	
	/**
	 * FOR INTERNAL USE ONLY!!  DO NOT USE THIS METHOD IN DEVELOPER CODE!
	 * Converts the string representation of a map type ID into its associated enum value.
	 * @param mapTypeIdStr The string representation of the map type ID.
	 * @return A MapTypeID enum value
	 */
	public static MapTypeId fromString(String mapTypeIdStr) {
		MapTypeId id = STR2ID.get(mapTypeIdStr);
		if(null==id) {
			throw new IllegalArgumentException("[MapTypeId.fromString()] Unknown map type ID string: "+mapTypeIdStr) ;
		}
		else {
			return id;
		}
	}
	
}
