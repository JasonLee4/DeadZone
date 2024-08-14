package provided.owlMaps.mouse;

/**
 * NOTE:  enums are not allowed to extend anything so the code to store the name has to be replicated in each enum definition.
 */


/**
 * The allowable map component mouse drag event types
 * Map component mouse drag events are passed IMouseEvent parameters when invoked, 
 * unlike MapMouseDragEventTypes which are not passed any input parameters.
 * @author swong
 *
 */
public enum ComponentMouseDragEventType {
	

	/**
	 * Event while the component is dragged
	 */
	DRAG("drag"), 
	
	/**
	 * Event when the component's dragging ends
	 */
	DRAG_END("dragend"),
	
	/**
	 * Event when the component's dragging starts
	 */
	DRAG_START("dragstart");

	/**
	 * The Google Maps event name
	 */
	private final String name;

	/**
	 * Construct an enum instance 
	 * @param name The associated Google Maps event name
	 */
	ComponentMouseDragEventType(String name) {
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


