package provided.owlMaps.general;


import java.io.Serializable;


/**
 * Represents a named location on a map with an associated default zoom level when viewing it.
 * Implementation note:  Since the LatLng class is not Serializable, then a concrete IPlace
 * must store the latitude and longitude of the place, not a LatLng object.
 * @author swong
 *
 */
public interface IPlace extends Serializable {
	
	/**
	 * Get the name of the place
	 * @return The name of the place
	 */
	public String getName();
	/**
	 * Get the latitude of the place
	 * @return The latitude of the place
	 */
	double getLat();
	/**
	 * Get the longitude of the place
	 * @return The longitude of the place
	 */
	double getLng();
	
	/**
	 * Convenience method to get the latitude and longitude of the place 
	 * in the form of a LatLng object.   Note that a new LatLng instance is
	 * returned with a default noWrap setting.
	 * @return A new LatLng instance with this place's latitude and longitude.
	 */
	public ILatLng getLatLng();
	
	/**
	 * Get the default zoom when viewing the place
	 * @return The default zoom level for this place
	 */
	public int getZoom();
	
	
	/**
	 * Make an IPlace instance from the given values
	 * @param name the name of the place
	 * @param lat The latitude of the place
	 * @param lng The longitude of the place
	 * @param zoom The preferred zoom level to use when viewing the place
	 * @return An IPlace instance
	 */
	public static IPlace make(final String name, final double lat, final double lng, final int zoom) {
		return new IPlace() {

			private static final long serialVersionUID = 7574126169609496267L;

			@Override
			public String getName() {
				return name;
			}

			@Override
			public double getLat() {
				return lat;
			}
			
			@Override
			public double getLng() {
				return lng;
			}
			
			
			@Override
			public ILatLng getLatLng() {
				return ILatLng.make(lat, lng);
			}

			@Override
			public int getZoom() {
				return zoom;
			}
			
			@Override
			public String toString() {
				return getName();
			}
			
			
		};
	}
}

