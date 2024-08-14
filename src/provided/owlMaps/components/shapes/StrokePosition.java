package provided.owlMaps.components.shapes;

/**
 * Enums corresponding to the Google Maps StrokePosition constants
 * @author swong
 *
 */
public enum StrokePosition {
	
	
	/**
	 * The stroke is centered on the shape's path, with half the stroke inside the shape and half the stroke outside the shape.
	 */
	CENTER("Center"),
	
	/**
	 * The stroke lies inside the shape
	 */
	INSIDE("INSIDE"),
	
	/**
	 * The stroke lies outside the shape.
	 */
	OUTSIDE("OUTSIDE");
	
	/**
	 * The Google Maps StrokePosition constant name
	 */
	private final String name;

	/**
	 * Construct an enum instance 
	 * @param name The associated Google Maps event name
	 */
	StrokePosition(String name) {
		this.name = name;
	}
	
	/**
	 * Accessor for the associated Google Maps event name
	 * @return The associated Google Maps StrokePosition constant name
	 */
	public String getName() {
		return this.name;
	}

}
