package provided.owlMaps.map;

/**
 * NOTE:  enums are not allowed to extend anything so the code to store the name has to be replicated in each enum definition.
 */


/**
 * The allowable map event types.
 * Map events are not passed any parameters when invoked.
 * @author swong
 *
 */
public enum MapEventType {
	
	/**
	 * This event is fired when the viewport bounds have changed.
	 */
	BOUNDS_CHANGED("bounds_changed"),
	
	/**
	 * This event is fired when the map center property changes.
	 */
	CENTER_CHANGED("center_changed"),
	
	/**
	 * This event is fired when the map heading property changes.
	 */
	HEADING_CHANGED("heading_changed"),
	
	/**
	 * This event is fired when the map becomes idle after panning or zooming.
	 * This event is useful when waiting for the map to stabilize before attempting any other map operations.
	 */
	IDLE("idle"),
	
	/**
	 * This event is fired when the mapTypeId property changes.
	 */
	MAPTYPEID_CHANGED("maptypeid_changed"),
	
	/**
	 * This event is fired when the projection has changed.
	 */
	PROJECTION_CHANGED("projection_changed"),
	
	/**
	 * This event is fired when the visible tiles have finished loading.
	 */
	TILES_LOADED("tilesloaded"),
	
	/**
	 * This event is fired when the map tilt property changes.
	 */
	TILT_CHANGED("tilt_changed"),
	
	/**
	 * This event is fired when the map zoom property changes.
	 */
	ZOOM_CHANGED("zoom_changed");

	
	/**
	 * The Google Maps event name
	 */
	private final String name;

	/**
	 * Construct an enum instance 
	 * @param name The associated Google Maps event name
	 */
	MapEventType(String name) {
		this.name = name;
	}
	
	/**
	 * Accessor for the associated Google Maps event name
	 * @return The associated Google Maps event name
	 */
	public String getName() {
		return this.name;
	}
	
}

