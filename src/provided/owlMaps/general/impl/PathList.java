/**
 * 
 */
package provided.owlMaps.general.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import provided.owlMaps.general.IPath;
import provided.owlMaps.general.IPathList;
import provided.owlMaps.general.IPathsAlgo;

/**
 * A concrete implementation of IPathList based on an ArrayList.  
 * This implementation may not be suitable for all applications, particularly those 
 * that require concurrent modification and thread safety.  
 * @author swong
 * @param <E> The type of element in the paths in the path list
 *
 */
public class PathList<E> extends ArrayList<IPath<E>> implements IPathList<E> {
	
	/**
	 * For serialization
	 */
	private static final long serialVersionUID = -1323139493243768467L;


	/**
	 * Construct an empty path list with the default initial capacity
	 */
	public PathList() {
		super();
	}

	/**
	 * Construct a path list from a varargs of IPath objects.
	 * @param elements Vararg of the IPath objects to fill the path list
	 */
	@SafeVarargs
	public PathList( IPath<E>... elements) {
		super(List.of(elements));
	}
	
	/**
	 * Construct an empty path list with the given initial capacity
	 * @param initialCapacity The initial capacity of the list
	 */
	public PathList(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Construct a path list from the given Collection of IPath elements
	 * @param c A collection of IPath elements to use 
	 */
	public PathList(Collection<? extends IPath<E>> c) {
		super(c);
	}
			

	@Override
	public <R, P> R execute(IPathsAlgo<R, E, P> algo, @SuppressWarnings("unchecked") P... params) {
		return algo.pathListCase(this, params);
	}

}
