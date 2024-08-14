package provided.owlMaps.mouse;

/**
 * NOTE:  enums are not allowed to extend anything so the code to store the name has to be replicated in each enum definition.
 */


/**
 * The allowable map mouse drag event types
 * Map mouse drag events are not passed any parameters when invoked.
 * @author swong
 *
 */
public enum MapMouseDragEventType {
	

	/**
	 * Event while the map is dragged
	 */
	DRAG("drag"), 
	
	/**
	 * Event when the map's dragging ends
	 */
	DRAG_END("dragend"),
	
	/**
	 * Event when the map's dragging starts
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
	MapMouseDragEventType(String name) {
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


