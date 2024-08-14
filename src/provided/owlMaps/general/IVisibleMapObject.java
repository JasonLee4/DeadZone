package provided.owlMaps.general;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY INSTANTIATE THIS INTERFACE!  *** <br/>
 * A MapObject that has a notion of visibility
 * @author swong
 *
 */
public interface IVisibleMapObject  {

	/**
	 * Set this object's visibility
	 * @param isVisible True if visible, false otherwise
	 */
	public void setVisible(boolean isVisible);
	
	/**
	 * Gets the object's current visibility 
	 * @return true if the object is visible, false otherwise.
	 */
	public boolean getVisible();
	
}
