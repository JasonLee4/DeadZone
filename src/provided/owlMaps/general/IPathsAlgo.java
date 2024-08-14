package provided.owlMaps.general;

import java.io.Serializable;

/**
 * Standard Visitor pattern visitors to use with IPaths objects.   
 * @author swong
 *
 * @param <R> The return type of the visitor algorithm
 * @param <E> The type of element held by the host
 * @param <P> The parameter type of the visitor algorithm.
 */
public interface IPathsAlgo<R, E, P> extends Serializable {
	
	/**
	 * Visitor base case
	 * @param host The IPath host
	 * @param params  The vararg parameters for the base case
	 * @return The return value of the processing 
	 */
	public R pathCase(IPath<E> host, @SuppressWarnings("unchecked") P...params);

	/**
	 * Visitor inductive case
	 * @param host The IPathList host
	 * @param params  The vararg parameters for the base case
	 * @return The return value of the processing 
	 */
	public R pathListCase(IPathList<E> host, @SuppressWarnings("unchecked") P...params);

}
