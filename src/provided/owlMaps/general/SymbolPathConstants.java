package provided.owlMaps.general;

/**
 * Predefined symbols for use with markers, polylines, etc.
 * 
 * @author swong
 *
 */
public enum SymbolPathConstants implements ISymbolPath {

	/**
	 * A backward-pointing closed arrow.
	 */
	BACKWARD_CLOSED_ARROW("BACKWARD_CLOSED_ARROW"),

	/**
	 * A backward-pointing open arrow.
	 */
	BACKWARD_OPEN_ARROW("BACKWARD_OPEN_ARROW"),

	/**
	 * A circle.
	 */
	CIRCLE("CIRCLE"),

	/**
	 * A forward-pointing closed arrow.
	 */
	FORWARD_CLOSED_ARROW("FORWARD_CLOSED_ARROW"),

	/**
	 * A forward-pointing open arrow.
	 */
	FORWARD_OPEN_ARROW("FORWARD_OPEN_ARROW");

	/**
	 * The Google Maps event name
	 */
	private final String name;

	/**
	 * Construct an enum instance
	 * 
	 * @param name The associated Google Maps event name
	 */
	SymbolPathConstants(String name) {
		this.name = name;
	}

	/**
	 * Accessor for the associated Google Maps SymbolPath name
	 * 
	 * @return The associated Google Maps event name
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public <R, P> R execute(ISymbolPathAlgo<R, P> algo, @SuppressWarnings("unchecked") P... params) {
		return algo.caseSymbolPathConstants(this, params);
	}

}
