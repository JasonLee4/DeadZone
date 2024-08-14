package provided.owlMaps.mouse;

/**
 * NOTE:  enums are not allowed to extend anything so the code to store the name has to be replicated in each enum definition.
 */


/**
 * The allowable map mouse event types
 * These events are passed a IMouseEvent object when invoked.
 * @author swong
 *
 */
public enum MapMouseEventType {
	
	
	/**
	 * Event when the map is clicked
	 */
	CLICK("click"),
	
	/**
	 * Event when the map is double-clicked
	 */
	DBL_CLICK("dblclick"),

	/**
	 * Event when the mouse moves over the map container
	 */
	MOUSE_MOVE("mousemove"),
	
	/**
	 * Event when the mouse leaves the map container
	 */
	MOUSE_OUT("mouseout"),
	
	/**
	 * Event when the mouse enters the map container
	 */
	MOUSE_OVER("mouseover"),

	/**
	 * Event when the right mouse button is clicked on the map
	 */
	RIGHT_CLICK("rightclick");
	
		
	
	/**
	 * The Google Maps event name
	 */
	private final String name;

	/**
	 * Construct an enum instance 
	 * @param name The associated Google Maps event name
	 */
	MapMouseEventType(String name) {
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



