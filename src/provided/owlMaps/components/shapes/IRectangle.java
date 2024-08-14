package provided.owlMaps.components.shapes;

import provided.mixedData.MixedDataDictionary;
import provided.owlMaps.general.IEditableObject;
import provided.owlMaps.general.ILatLngBounds;
import provided.owlMaps.general.IOptionedMapObject;
import provided.owlMaps.general.IVisibleMapObject;
import provided.owlMaps.mouse.IComponentMouseDragEventObject;
import provided.owlMaps.mouse.IComponentMouseEventObject;



/**
 * Represents an rectangle drawn on the map oriented along the latitude and longitude lines 
 * This corresponds to a Javascript Google Maps google.maps.Rectangle object
 * @author swong
 *
 */
public interface IRectangle extends  IVisibleMapObject, IEditableObject, IComponentMouseEventObject, IComponentMouseDragEventObject, IOptionedMapObject<MixedDataDictionary> {

	/**
	 * Get a COPY of the rectangle's bounds
	 * @return An ILatLngBounds object
	 */
	public ILatLngBounds getBounds();
	
	/**
	 * Set the rectangle's bounds
	 * @param bounds The new bounds as an ILatLngBounds object
	 */
	public void setBounds(ILatLngBounds bounds);
	
	
	/**
	 * Set a RectangleEventType event 
	 * @param eventType The type of event being set
	 * @param eventFn The callback to use when the event is invoked.
	 */
	void setRectangleEvent(RectangleEventType eventType, Runnable eventFn);

}
