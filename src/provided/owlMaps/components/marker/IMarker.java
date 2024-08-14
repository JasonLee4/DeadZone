package provided.owlMaps.components.marker;

import provided.mixedData.MixedDataDictionary;
import provided.owlMaps.cefUtils.ICefObjRef;
import provided.owlMaps.general.IAnchor;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IOptionedMapObject;
import provided.owlMaps.general.IVisibleMapObject;
import provided.owlMaps.mouse.IComponentMouseDragEventObject;
import provided.owlMaps.mouse.IComponentMouseEventObject;


/**
 * Represents a marker placed on the map.   Markers can be used as anchor points for info windows
 * @author swong
 *
 */
public interface IMarker extends  ICefObjRef, IVisibleMapObject, IComponentMouseEventObject, IComponentMouseDragEventObject, IAnchor, IOptionedMapObject<MixedDataDictionary> {


	@Override
	public ILatLng getPosition();
	
	/**
	 * Set the position of this marker
	 * @param latLng The new position
	 */
	public void setPosition(ILatLng latLng);

	/**
	 * Get the label of the marker
	 * @return The label value
	 */
	public IMarkerLabel getLabel();
	
	/**
	 * Set the label of the marker
	 * @param label The new label for the marker
	 */
	public void setLabel(IMarkerLabel label);
	
	/**
	 * Get the title of the marker
	 * @return The title of the marker
	 */
	public String getTitle();
	
	/**
	 * Set the title of the marker.  
	 * Note that this may appear to override the marker's label.
	 * @param title The new title of the marker
	 */
	public void setTitle(String title);	
	
	/**
	 * Returns whether or not the marker is clickable
	 * @return true if clickable, false otherwise
	 */
	public boolean getClickable();
	
	/**
	 * Sets whether or not the marker is clickable
	 * @param isClickable true if clickable, false otherwise.
	 */
	public void setClickable(boolean isClickable);	
	
	/**
	 * Returns whether or not the marker is draggable
	 * @return true if draggable, false otherwise
	 */
	public boolean getDraggable();
	
	/**
	 * Sets whether or not the marker is draggable
	 * @param isDraggable true if draggable, false otherwise
	 */	
	public void setDraggable(boolean isDraggable);	
	
	//TODO enable shape
	
//	public IMarkerShape getShape();
//	public void setShape(IMarkerShape shape);
	
	/**
	 * Get the opacity of the marker
	 * @return The opacity value [0.0, 1.0]
	 */
	public double getOpacity();
	
	/**
	 * Set the opacity of the marker
	 * @param opacity The new opacity value [0.0, 1.0]
	 */
	public void setOpacity(double opacity);
	
	/**
	 * Get the URL of the icon for the marker
	 * @return The URL string of the icon
	 */
	public String getIcon();
	
	/**
	 * Set the URL string of the marker's icon
	 * @param url A URL string
	 */
	public void setIcon(String url);
	
	/**
	 * Get the CSS cursor type shown when hovering over the marker
	 * @return The CSS cursor type for the marker
	 */
	public String getCursor();
	
	/**
	 * Set the CSS cursor type shown when hovering over the marker
	 * @param cursor The CSS cursor type for the marker
	 */
	public void setCursor(String cursor);

	//TODO enable animation
	
//	public String getAnimation();
//	public void setAnimation(String animation);
	/**
	 * Gets the z-index of the marker on the map
	 * @return the z-index
	 */
	public double getZIndex();
	
	/**
	 * Sets the z-index of the marker on the map
	 * @param zIndex The new z-index 
	 */
	public void setZIndex(double zIndex);
	
	
	/**
	 * Set an MarkerEventType event on the marker
	 * @param eventType The event type
	 * @param eventFn The callback to use when the event occurs
	 */
	void setMarkerEvent(MarkerEventType eventType, Runnable eventFn);

}
