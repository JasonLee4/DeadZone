package provided.owlMaps.components.infowindow;

/**
 * NOTE:  enums are not allowed to extend anything so the code to store the name has to be replicated in each enum definition.
 */

/**
 * The allowable marker event types
 * Marker events are not passed any parameters when invoked.
 * @author swong
 *
 */
public enum InfoWindowEventType {
	

	/**
	 * Event when the 'x' close icon is clicked to close the info window.
	 * Programmatically closing the info window, e.g. setVisible(false), will NOT invoke the CLOSE_CLICK event!
	 */
	CLOSE_CLICK("closeclick"),  
	/**
	 * Event when the content of the info window is changed.
	 */
	CONTENT_CHANGED("content_changed"),
	
	// Deliberately omitted: domready
	
	/**
	 * Event when the position of the info window is changed.
	 */
	POSITION_CHANGED("position_changed"),
	
	/**
	 * Event when the z-index display order is changed.
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
	InfoWindowEventType(String name) {
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

