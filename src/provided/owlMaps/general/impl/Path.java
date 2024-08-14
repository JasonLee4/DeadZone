/**
 * 
 */
package provided.owlMaps.general.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import provided.owlMaps.general.IPath;
import provided.owlMaps.general.IPathsAlgo;

/**
 * A concrete implementation of IPath based on an ArrayList.  
 * This implementation may not be suitable for all applications, particularly those 
 * that require concurrent modification and thread safety.  
 * @author swong
 * @param <E> The type of element in the path
 *
 */
public class Path<E> extends ArrayList<E> implements IPath<E> {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 1562337031121912196L;

	/**
	 * Construct an empty path with the default capacity.
	 */
	public Path() {
		super();
	}
	
	/**
	 * Construct a path from a varargs of element values.
	 * @param elements Vararg of the elements to fill the path
	 */
	@SafeVarargs
	public Path( E... elements) {
		super(List.of(elements));
	}

	/**
	 * Construct an empty path with the given initial capacity
	 * @param initialCapacity  the initial capacity of the list
	 */
	public Path(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Construct a path from the given Collection of elements
	 * @param c A collection of elements to use 
	 */
	public Path(Collection<? extends E> c) {
		super(c);
	}

	@Override
	public <R, P> R execute(IPathsAlgo<R, E, P> algo, @SuppressWarnings("unchecked") P... params) {
		return algo.pathCase(this, params);
	}

}
