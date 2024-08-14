package provided.owlMaps.general;

import provided.owlMaps.cefUtils.ICefObjectWrapper;

/**
 * Represents an editable map object corresponding to an actual underlying
 * Javascript object on the map.
 * @author swong
 *
 */
public interface IEditableObject extends ICefObjectWrapper {
	
	/**
	 * Get the editable status of the underlying object
	 * @return True if editable, false otherwise
	 */
	public default boolean getEditable() {
		try {
			return this.call("getEditable", Boolean.class);
		} catch (Exception e) {
			System.err.println("[IEditableObject.getEditable()] Exception while retrieving value: "+e);
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Set the editable status of the underlying object
	 * @param isEditable True if editable, false otherwise
	 */
	public default void setEditable(boolean isEditable) {
		this.callVoid("setEditable", isEditable);
	}

}
