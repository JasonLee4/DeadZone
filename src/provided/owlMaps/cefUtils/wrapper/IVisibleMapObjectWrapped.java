package provided.owlMaps.cefUtils.wrapper;

import provided.owlMaps.cefUtils.ICefObjectWrapper;
import provided.owlMaps.general.IVisibleMapObject;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS INTERFACE! ***<br/>
 * A wrapper for an ICefObject that has a notion of visibility
 * @author swong
 *
 */
public interface IVisibleMapObjectWrapped extends IVisibleMapObject, ICefObjectWrapper{

	/**
	 * Set this object's visibility
	 * @param isVisible True if visible, false otherwise
	 */
	@Override
	public default void setVisible(boolean isVisible) {
		callVoid("setVisible", isVisible);
	};
	
	/**
	 * Gets the object's current visibility 
	 * @return true if the object is visible, false otherwise.
	 */
	@Override
	public default boolean getVisible() {
		return call("getVisible", Boolean.class);
	};
	
}
