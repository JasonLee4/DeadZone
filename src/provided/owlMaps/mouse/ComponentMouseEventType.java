package provided.owlMaps.mouse;

/**
 * NOTE:  enums are not allowed to extend anything so the code to store the name has to be replicated in each enum definition.
 */


/**
 * The allowable map component mouse event types.  
 * Similar to map mouse event types but adds mouse up and mouse down event types.
 * These events are passed a IMouseEvent object when invoked.
 * @author swong
 *
 */
public enum ComponentMouseEventType {
	
	/**
	 * Event when the component is clicked
	 */
	CLICK("click"),
	
	/**
	 * Event when the component is double-clicked
	 */
	DBL_CLICK("dblclick"),
	
	/**
	 * Event when the mouse leaves the area of the component
	 */
	MOUSE_OUT("mouseout"),
	
	/**
	 * Event when the mouse enters the area of the component
	 */
	MOUSE_OVER("mouseover"),
	
	/**
	 * Event when the right mouse button is clicked on the component
	 */
	RIGHT_CLICK("rightclick"),
	
	/**
	 * Event when the mouse button is released
	 */
	MOUSE_UP("mouseup"),
	
	/**
	 * Event when the mouse button is pressed.
	 */
	MOUSE_DOWN("mousedown");
	
	
	/**
	 * The Google Maps event name
	 */
	private final String name;

	/**
	 * Construct an enum instance 
	 * @param name The associated Google Maps event name
	 */
	ComponentMouseEventType(String name) {
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



