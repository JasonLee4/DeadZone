package provided.owlMaps.general;

import java.awt.geom.Point2D;

/**
 * Represents a map component that can be used to position ("anchor") another component on the map.
 * As of Google Maps API v3.39, the only anchor object available is a Marker.   The typical component to
 * be anchored is an InfoWindow.
 * @author swong
 *
 */
public interface IAnchor {

	/**
	 * The LatLng position of this anchor point.
	 * @return  The ILatLng of the anchor's location
	 */
	ILatLng getPosition();

	/**
	 * A pixel offset from the LatLng position that the anchored object, e.g. an IInfoWindow is located.   
	 * This corresponds to the anchorPoint property of a Google Maps anchor object.
	 * @return  The pixel offset of an entity with respect to the anchor position.
	 */
	Point2D.Double getPixelOffset();

}
