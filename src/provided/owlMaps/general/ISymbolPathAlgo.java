package provided.owlMaps.general;

/**
 * Standard visitor for ISymbolPath processing
 * @author swong
 *
 * @param <R> The return type of the algorithm
 * @param <P> The parameter type of the algorithm
 */
public interface ISymbolPathAlgo<R, P> {

	/**
	 * The case for SymbolPathConstants enums
	 * @param host The symbolPathConstants enums to process
	 * @param params The input parameter for the algorithm.
	 * @return The return value of the algorithm.
	 */
	public R caseSymbolPathConstants(SymbolPathConstants host, @SuppressWarnings("unchecked") P... params);

	/**
	 * The case for SVGSymbolPath objects
	 * @param host The SVGSymbolPath object to process
	 * @param params The input parameter for the algorithm.
	 * @return The return value of the algorithm.
	 */
	public R caseSVGSymbolPath(SVGSymbolPath host, @SuppressWarnings("unchecked") P... params);

}
