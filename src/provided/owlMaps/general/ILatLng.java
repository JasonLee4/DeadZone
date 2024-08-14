package provided.owlMaps.general;

import java.io.Serializable;

/**
 * The latitude and longitude of a location on Earth.
 * @author swong
 *
 */
public interface ILatLng extends Serializable {

	/**
	 * Accessor for the latitude
	 * @return The latitude value
	 */
	public double getLat();

	/**
	 * Accessor for the longitude
	 * @return The longitude value
	 */
	double getLng();
	
	/**
	 * The distance between this and a given ILatLng, assuming a spherical Earth
	 * @param otherLatLng The other ILatLng location
	 * @param earthRadius  The Earth's radius to use in any units
	 * @return The distance between the two locations in the same units as the given Earth's radius.
	 */
	double distanceBetween(ILatLng otherLatLng, double earthRadius);

	
	/**
	 * Convert the given value from degrees to radians
	 * @param degrees A value in degrees
	 * @return The equivalent value in radians.
	 */
	public static double degreesToRadians(double degrees) {
		return Math.PI*degrees/180;
	}

	/**
	 * Make a new ILatLng object
	 * @param lat Latitude value in the range [-90, +90]
	 * @param lng Longitude value in the range [-180, +180]
	 * @return An instance of ILatLng
	 */
	public static ILatLng make(final double lat, final double lng) {
		if (Math.abs(lat) > 90.0 || Math.abs(lng) > 180.0) {
			throw new IllegalArgumentException("[ILatLng.make("+lat+", "+lng+")] Lat or Lng is out of bounds!");
		}
		return new ILatLng() {

			private static final long serialVersionUID = 2199482862600242903L;

			@Override
			public double getLat() {
				return lat;
			}
			
			@Override
			public double getLng() {
				return lng;
			}
			
			@Override 
			public boolean equals(Object other) {
				if(other instanceof ILatLng) {
					return lat==((ILatLng)other).getLat() && lng==((ILatLng)other).getLng();
				}
				else {
					return false;
				}
			}
			
			@Override
			public int hashCode() {
				return Double.hashCode(lat) + Double.hashCode(lng);
			}
			
			@Override
			public String toString() {
				return "ILatLng("+lat+", "+lng+")";
			}
			

			
			/**
			 * Calculate the great circle distance between two LatLng's using the 
			 * "haversine formula" for greater accuracy.
			 * The units of the results depend on the units of the given earth's radius.
			 * Reference: http://www.movable-type.co.uk/scripts/latlong.html 
			 * @param otherLatLng	The other LatLng location
			 * @param earthRadius The mean earth radius in the desired units.
			 * @return  The great circle distance in the units of the given earthRadius for a sphere of that radius.
			 */
			@Override
			public double distanceBetween(ILatLng otherLatLng, double earthRadius) {

				// Do all calculations in radians
				double latA = degreesToRadians(this.getLat());
				double lngA = degreesToRadians(this.getLng());

				double latB = degreesToRadians(otherLatLng.getLat());
				double lngB = degreesToRadians(otherLatLng.getLng());

				// The square of the chord length
				double chord2 = Math.pow(Math.sin((latB-latA)/2), 2) 
						+ Math.cos(latA)*Math.cos(latB) * Math.pow(Math.sin((lngB-lngA)/2), 2);

				// the angular distance in radians
				double unitCircleDistance = 2*Math.atan2(Math.sqrt(chord2), Math.sqrt(1.0-chord2));

				return earthRadius*unitCircleDistance;

			}

			
		};
	}
}