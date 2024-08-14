package provided.owlMaps.components.infowindow;

import provided.mixedData.MixedDataDictionary;
import provided.owlMaps.cefUtils.ICefObjRef;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IOptionedMapObject;
import provided.owlMaps.general.IVisibleMapObject;


/**
 * Represents a pop-up information window on the map that can contain text, images or any other HTML component.
 * @author swong
 *
 */
public interface IInfoWindow extends  ICefObjRef, IVisibleMapObject, IOptionedMapObject<MixedDataDictionary> {

	/**
	 * Get the HTML  contents of the info window
	 * @return  The contents of the info window
	 */
	public String getContent();
	
	/**
	 * Set the HTML contents of the info window
	 * @param str The new contents of the window
	 */
	public void setContent(String str);
	
	/**
	 * Get the ILatLng position of the window if set directly on the map, i.e. not anchored.
	 * @return The ILatLng position
	 */
	public ILatLng getPosition();
	
	/**
	 * Set the ILatLng position of the window if set directly on the map, i.e. not anchored.
	 * @param latLng The ILatLng position
	 */
	public void setPosition(ILatLng latLng);

	/**
	 * Get the z-index layer position of the component
	 * @return The current z-index value
	 */
	public double getZIndex();

	/**
	 * set the z-index layer position of the component
	 * @param zIndex The new z-index value
	 */
	public void setZIndex(double zIndex);
	
	/**
	 * Set the given event type on the info window
	 * @param eventType The type of the event
	 * @param eventFn The callback function to run when the event occurs.
	 */
	void setInfoWindowEvent(InfoWindowEventType eventType, Runnable eventFn);

}
