package provided.owlMaps.general;

/**
 * Representation of an SVG vector path defining a symbol shape.   
 * See <a href="https://www.w3.org/TR/SVG/paths.html#PathData" target="_blank">https://www.w3.org/TR/SVG/paths.html#PathData</a>
 * @author swong
 *
 */
public class SVGSymbolPath implements ISymbolPath {

	/**
	 * The SVG path as a string
	 */
	private String path;

	/**
	 * Construct a new instance
	 * @param path An SVG path string
	 */
	public SVGSymbolPath(String path) {
		this.path = path;
	}
	
	/**
	 * Accessor for the stored SVG path string
	 * @return The stored SVG path string
	 */
	public String getPath() {
		return this.path;
	}

	@Override
	public <R, P> R execute(ISymbolPathAlgo<R, P> algo, @SuppressWarnings("unchecked") P... params) {
		return algo.caseSVGSymbolPath(this, params);
	}

}
