package provided.owlMaps.general;

import java.io.Serializable;

/**
 * Represents a rectangular (i.e. parallel top latitude and longitude) bounds on the Earth in terms of ILatLngs of its southwest and northeast corners.
 * @author swong
 *
 */
public interface ILatLngBounds extends Serializable {
	
	/**
	 * Accessor for the northeast corner ILatLng
	 * @return The northeast corner ILatLng
	 */
	public ILatLng getNorthEast();

	/**
	 * Accessor for the southwest corner ILatLng
	 * @return The southwest corner ILatLng
	 */
	public ILatLng getSouthWest();
	
	/**
	 * Accessor for the center ILatLng of the bounds
	 * @return The ILatLng of the center of the bounds
	 */
	public ILatLng getCenter();
	
	
	/**
	 * Make an ILatLngBounds from the given values
	 * @param swLatLng  The ILatLng of the southwest corner of the bounds
	 * @param neLatLng The ILatLng of the northeast corner of the bounds
	 * @return A new ILatLngBounds instance
	 */
	public static ILatLngBounds make(final ILatLng swLatLng, final ILatLng neLatLng) {
		return new ILatLngBounds() {

			/**
			 * For Serializable
			 */
			private static final long serialVersionUID = 6211804242179940662L;

			@Override
			public ILatLng getNorthEast() {
				return neLatLng;
			}

			@Override
			public ILatLng getSouthWest() {
				return swLatLng;
			}

			@Override
			public ILatLng getCenter() {
				return ILatLng.make(neLatLng.getLat()-swLatLng.getLat(), neLatLng.getLng()-swLatLng.getLng());
			}
			
			@Override
			public String toString() {
				return "ILatLngBounds("+swLatLng+", "+neLatLng+")";
			}
			
		};
	}
}
