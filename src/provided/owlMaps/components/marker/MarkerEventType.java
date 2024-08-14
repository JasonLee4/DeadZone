package provided.owlMaps.components.marker;

/**
 * NOTE:  enums are not allowed to extend anything so the code to store the name has to be replicated in each enum definition.
 */

/**
 * The allowable marker event types
 * Marker events are not passed any parameters when invoked.
 * @author swong
 *
 */
public enum MarkerEventType {
	
	/**
	 * This event is fired when the marker's animation property changes.
	 */
	ANIMATION_CHANGED("animation_changed"), 
	
	/**
	 * This event is fired when the marker's clickable property changes.
	 */
	CLICKABLE_CHANGED("clickable_changed"),
	
	/**
	 * This event is fired when the marker's cursor property changes.
	 */
	CURSOR_CHANGED("cursor_changed"),
	
	/**
	 * This event is fired when the marker's draggable property changes.
	 */
	DRAGGABLE_CHANGED("draggable_changed"),
	
	/**
	 * This event is fired when the marker's flat property changes.
	 */
	FLAT_CHANGED("flat_changed"),
	
	/**
	 * This event is fired when the marker icon property changes.
	 */
	ICON_CHANGED("icon_changed"),
	
	/**
	 * This event is fired when the marker position property changes.
	 */
	POSITION_CHANGED("position_changed"),
	
	/**
	 * This event is fired when the marker's shape property changes.
	 */
	SHAPE_CHANGED("shape_changed"),
	
	/**
	 * This event is fired when the marker title property changes.
	 */
	TITLE_CHANGED("title_changed"),
	
	/**
	 * This event is fired when the marker's visible property changes.
	 */
	VISIBLE_CHANGED("visible_changed"),
	
	/**
	 * This event is fired when the marker's zIndex property changes.
	 */
	ZINDEX_CHANGED("zindex_changed");

	/**
	 * The Google Maps event name
	 */
	private final String name;

	/**
	 * Construct an enum instance 
	 * @param name The associated Google Maps event name
	 */
	MarkerEventType(String name) {
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

