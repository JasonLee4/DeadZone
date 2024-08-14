package provided.owlMaps.general;

import java.util.List;

import provided.owlMaps.general.impl.PathList;

/**
 * A Standard Visitor pattern host that represents a list of lists of element of type E,
 * i.e. a List&lt;IPath&lt;E&gt;&gt;.
 * this is the inductive case of IPaths.
 * Note: There is an execute() method inherited from IPaths.
 * @author swong
 *
 * @param <E> The type of elements held in the contained lists.
 */
public interface IPathList<E> extends IPaths<E>, List<IPath<E>> {

	/**
	 * Convenience factory for constructing an IPathList instance from a 
	 * varargs of IPath instances.   
	 * Note that the concrete implementation of IPathList has the same constructors as
	 * ArrayList for use in other situations.
	 * @param <T>   The type of elements in the IPath objects used to fill the path list with.
	 * @param elements  Varargs of the IPath elements of the IPathList 
	 * @return A new IPathList object
	 */
	@SafeVarargs
	public static <T> IPathList<T> make( IPath<T>... elements) {
		return new PathList<T>(elements);
	}
	
}
