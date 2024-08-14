package provided.owlMaps.components.shapes;

/**
 * NOTE:  enums are not allowed to extend anything so the code to store the name has to be replicated in each enum definition.
 */

/**
 * An extension of ComponentMouseEventType used in Polygons and Polylines that adds some events plus is paired with an event 
 * handler that is given an IlolyMouseEvent.
 * Marker events are not passed any parameters when invoked.
 * @author swong
 *
 */
public enum PolyMouseEventType {
	

	/**
	 * Vent when the component is clicked
	 */
	CLICK("click"),
	
	/**
	 * Event when the component is double-clicked
	 */
	DBL_CLICK("dblclick"),
	
	/**
	 * Event when the mouse button is pressed.
	 */
	MOUSE_DOWN("mousedown"),

	/**
	 * Event when the mouse is moved over the component  
	 */
	MOUSE_MOVE("mousemove"),
	
	/**
	 * Event when the mouse leaves the area of the component
	 */
	MOUSE_OUT("mouseout"),
	
	/**
	 * Event when the mouse enters the area of the component
	 */
	MOUSE_OVER("mouseover"),
	
	/**
	 * Event when the mouse button is released
	 */
	MOUSE_UP("mouseup"),
	
	/**
	 * Event when the right mouse button is clicked on the component
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
	PolyMouseEventType(String name) {
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

