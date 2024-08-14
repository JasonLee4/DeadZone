package provided.owlMaps.components.shapes;

import provided.mixedData.MixedDataDictionary;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.ILatLngBounds;
import provided.owlMaps.general.IOptionedMapObject;
import provided.owlMaps.general.IVisibleMapObject;
import provided.owlMaps.mouse.IComponentMouseDragEventObject;
import provided.owlMaps.mouse.IComponentMouseEventObject;


/**
 * Represents an circle drawn on the map 
 * This corresponds to a Javascript Google Maps google.maps.Circle object
 * @author swong
 *
 */
public interface ICircle extends  IVisibleMapObject, IComponentMouseEventObject, IComponentMouseDragEventObject, IOptionedMapObject<MixedDataDictionary> {

	/**
	 * Get a COPY of the circle's bounds
	 * @return An ILatLngBounds object
	 */
	public ILatLngBounds getBounds();
	
	/**
	 * Returns the center of this circle. 
	 * @return The ILatLng of the center of the circle
	 */
	public ILatLng getCenter();
	
	/**
	 * Set the center of this circle. 
	 * @param center The ILatLng of the new center of the circle
	 */
	public void setCenter(ILatLng center);
	
	/**
	 * Get the radius of the circle in the system's current units.
	 * @return The radius of the circle
	 */
	public double getRadius();
	
	/**
	 * Set the radius of the circle in the system's current units.
	 * @param radius The new radius of the circle.
	 */
	public void setRadius(double radius);
	
	
	/**
	 * Set a CircleEventType event 
	 * @param eventType The type of event being set
	 * @param eventFn The callback to use when the event is invoked.
	 */
	void setCircleEvent(CircleEventType eventType, Runnable eventFn);	
}
