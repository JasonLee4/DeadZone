package provided.owlMaps.mouse;

import provided.owlMaps.general.ILatLng;

/**
 * The parameter passed by the system to a map mouse event.
 * Contains the lat and lng of where the mouse event occurred on the map. 
 * @author swong
 *
 */
public interface IMouseEvent {	
	/**
	 * The ILatLng location where the mouse event occurred 
	 * @return an ILatLng object
	 */
	public ILatLng getLatLng();
	
	
	/**
	 * *** For internal use ONLY!  Developer code should never use this method! ***<br/> 
	 * Factory method to construct an IMouseEvent instance from an ILatLng.
	 * @param latLng The ILatLng where the mouse event occurred on the map
	 * @return A new IMouseEvent instance
	 */
	public static IMouseEvent make(final ILatLng latLng) {
		return new IMouseEvent() {

			@Override
			public ILatLng getLatLng() {
				return latLng;
			}
			@Override
			public String toString() {
				return "IMouseEvent(latLng="+latLng+")";
			}
			
		};
	}
}
