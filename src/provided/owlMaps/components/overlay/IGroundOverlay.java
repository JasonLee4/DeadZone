package provided.owlMaps.components.overlay;

import java.util.function.Consumer;

import provided.mixedData.MixedDataDictionary;
import provided.owlMaps.general.ILatLngBounds;
import provided.owlMaps.general.IOptionedMapObject;
import provided.owlMaps.general.IVisibleMapObject;
import provided.owlMaps.mouse.IMouseEvent;


/**
 * A rectangular image overlaid onto the map.
 * Note: A ground overlay may only display when the map is oriented with north upward (bug in Google API?)
 * IGroundOverlays only accept IGroupOverlayMouseEventType events.
 * @author swong
 *
 */
public interface IGroundOverlay  extends  IVisibleMapObject, IOptionedMapObject<MixedDataDictionary> {

	/**
	 * Get a COPY of the overlay's bounds
	 * @return An ILatLngBounds object
	 */
	public ILatLngBounds getBounds();
	
	/**
	 * Set the overlay's bounds
	 * @param bounds The new bounds as an ILatLngBounds object
	 */
	public void setBounds(ILatLngBounds bounds);	
	
	/**
	 * Get the URL of the  displayed image
	 * @return A URL string
	 */
	public String getUrl();
	
	
	/**
	 * Get the current opacity level of the overlay
	 * @return The opacity level [0.0-1.0]
	 */
	public double getOpacity();
	
	
	/**
	 * Set the overlay's opacity 
	 * @param opacity The new opacity level [0.0-1.0]
	 */
	public void setOpacity(double opacity);
	
	
	/**
	 * Set the event 
	 * @param eventType The type of event being set
	 * @param eventFn The callback to use when the event is invoked.
	 */
	void setGroundOverlayMouseEvent(GroundOverlayMouseEventType eventType, Consumer<IMouseEvent> eventFn);

}
