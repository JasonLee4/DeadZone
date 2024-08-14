package provided.owlMaps.components.overlay;

/**
 * NOTE:  enums are not allowed to extend anything so the code to store the name has to be replicated in each enum definition.
 */


/**
 * The allowable ground overlay component mouse event types.  
 * Similar to map mouse event types but only contains click and double-click.
 * These events are passed a IMouseEvent object when invoked.
 * @author swong
 *
 */
public enum GroundOverlayMouseEventType {
	
	/**
	 * Event when the component is clicked
	 */
	CLICK("click"),
	
	/**
	 * Event when the component is double-clicked
	 */
	DBL_CLICK("dblclick");
	
	
	
	/**
	 * The Google Maps event name
	 */
	private final String name;

	/**
	 * Construct an enum instance 
	 * @param name The associated Google Maps event name
	 */
	GroundOverlayMouseEventType(String name) {
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



