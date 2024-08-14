package provided.owlMaps.general;

import java.util.List;

import provided.owlMaps.general.impl.Path;

/**
 * A List&lt;E&gt; that is a Standard Visitor pattern visitor host
 * and in such, is also abstractly equivalent to IPaths.
 * This is the base case of IPaths.
 * Some parts of the OwlMaps system use IPath alone for 
 * compatibility and extensibility with IPaths usage.
 * Note:  There is an execute() method inherited from IPaths.
 * @author swong
 *
 * @param <E>  The type of element held in the list
 */
public interface IPath<E> extends IPaths<E>, List<E> {

	
	/**
	 * Convenience factory for constructing an IPath instance from a 
	 * varargs of elements.   
	 * Note that the concrete implementation of IPath has the same constructors as
	 * ArrayList for use in other situations.
	 * @param <T>   The type of elements to fill the path with.
	 * @param elements Vararg of the elements to fill the path
	 * @return A new IPath instance
	 */
	@SafeVarargs
	public static <T> IPath<T> make( T... elements) {
		return new Path<T>(elements);
	}
}
