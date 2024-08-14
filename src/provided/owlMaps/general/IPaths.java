package provided.owlMaps.general;

import java.io.Serializable;

/**
 * An abstraction that represents either a list of elements of type E (IPath&lt;E&gt;) or
 * a list of lists of element of type E (IPathList&lt;E&gt;).
 * This is a Standard Visitor pattern host.
 * @author swong
 *
 * @param <E>  The type of element held in the list
 */
public interface IPaths<E> extends Serializable {

	/**
	 * The "accept" method of the visitor hosts.
	 * @param <R> The return type of the visitor algorithm
	 * @param <P> The parameter type of the visitor algorithm.
	 * @param algo   The visitor to accept
	 * @param params The parameters to pass to the visitor execution
	 * @return The return value from the visitor execution.
	 */
	public <R, P> R execute(IPathsAlgo<R, E, P> algo,  @SuppressWarnings("unchecked") P...params);

}
