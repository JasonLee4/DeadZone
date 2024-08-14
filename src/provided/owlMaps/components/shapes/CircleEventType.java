package provided.owlMaps.components.shapes;

/**
 * NOTE:  enums are not allowed to extend anything so the code to store the name has to be replicated in each enum definition.
 */

/**
 * The allowable marker event types
 * Marker events are not passed any parameters when invoked.
 * @author swong
 *
 */
public enum CircleEventType {
	

	/**
	 * This event is fired when the circle's center is changed.
	 */
	CENTER_CHANGED("center_changed"),
	
	
	/**
	 * This event is fired when the circle's radius is changed.
	 */
	RADIUS_CHANGED("radius_changed");

	/**
	 * The Google Maps event name
	 */
	private final String name;

	/**
	 * Construct an enum instance 
	 * @param name The associated Google Maps event name
	 */
	CircleEventType(String name) {
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

