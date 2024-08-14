package provided.owlMaps.utils;

import java.util.Map;

import provided.owlMaps.control.MapLengthUnits;

/**
 * A collection of OwlMaps-related definitions and constants
 * @author swong
 *
 */
public interface IOwlMapsDefs {

		/**
		 * The license key for the JxBrowser subsystem
		 */
		public static final String JxBrowserLicenseKey = "1BNDJDKIKHTB98D69PDN6BU5KUC2R86Y9DNK7FQ097B7RQ6CXB10HA45S627WMQRRXSF0N";   // Expires 5/20/2020  

		/**
		 * Conversion from degrees to radians
		 */
		public static final double RADIANS_PER_DEGREE = Math.PI/180.0;

		
		/**
		 * Conversion from meters to kilometers
		 */
		public static final double KILOMETERS_PER_METER = 0.001;
		
		
		/**
		 * Conversion from meters to feet
		 */
		public static final double FEET_PER_METER = 3.2808399;
		
		/**
		 * Conversion from meters to feet
		 */
		public static final double MILES_PER_FEET = 1.0/5280.0;		

		/**
		 * Mapping of map length measurement units to conversion factor of unitX/meter, 
		 * i.e. the multiplicative factor to convert meters into the desired units.
		 */
		public static final Map<MapLengthUnits, Double> LENGTH_CONVERSIONS = Map.ofEntries(
			Map.entry(MapLengthUnits.METERS, 1.0),
			Map.entry(MapLengthUnits.KILOMETERS, KILOMETERS_PER_METER),
			Map.entry(MapLengthUnits.FEET, FEET_PER_METER),
			Map.entry(MapLengthUnits.MILES, FEET_PER_METER*MILES_PER_FEET)
		);
		
		
		/**
		 * The "mean" radius of an ellipsoidal model of the Earth in meters.  
		 * r_mean = (3*a+b)/3 where a = equatorial radius and b = polar radius.  
		 * See <a href="https://en.m.wikipedia.org/wiki/Earth_radius#Mean_radii" target="_blank">https://en.m.wikipedia.org/wiki/Earth_radius#Mean_radii</a>
		 * 
		 */
		public static final double EARTH_MEAN_RADIUS_METERS = 6371008.8;

		/**
		 * Inverse of the "flattening" ratio of an ellipsoidal model of the Earth.  
		 * f = (a-b)/a where a = equatorial radius and b = polar radius.  
		 * See <a href= "https://en.m.wikipedia.org/wiki/World_Geodetic_System#WGS84" target="_blank">https://en.m.wikipedia.org/wiki/World_Geodetic_System#WGS84</a>
		 */
		public static final double EARTH_INVERSE_FLATTENING = 298.257222100882711;

		/**
		 * Mapping of map length measurement units to the mean radius of the Earth in those units.
		 */
		public static final Map<MapLengthUnits, Double> EARTH_MEAN_RADII = Map.ofEntries(
			Map.entry(MapLengthUnits.METERS, EARTH_MEAN_RADIUS_METERS),
			Map.entry(MapLengthUnits.KILOMETERS, EARTH_MEAN_RADIUS_METERS*KILOMETERS_PER_METER),
			Map.entry(MapLengthUnits.FEET, EARTH_MEAN_RADIUS_METERS*FEET_PER_METER),
			Map.entry(MapLengthUnits.MILES, EARTH_MEAN_RADIUS_METERS*FEET_PER_METER*MILES_PER_FEET)
		);


}
