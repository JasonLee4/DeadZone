package provided.owlMaps.utils;

import java.awt.geom.Point2D;

import provided.owlMaps.control.MapLengthUnits;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IPath;
import provided.owlMaps.general.IPaths;

/**
 * Utilities for working with an IOwlMap map, including information about the Earth and 
 * working with latitude/longitude values and relative distances. 
 * @author swong
 *
 */
public interface IOwlMapUtils {
	
	/**
	 * Get the currently set system length unit
	 * @return  the current length unit
	 */
	public MapLengthUnits getSystemLengthUnit();
	
	/**
	 * Get the small angle approximation for the length per degree of factor 
	 * for distances along a fixed longitude line at the given latitude.  
	 * The calculation assumes that the Earth 
	 * is a sphere equal to the Earth's radius at the given latitude.  
	 * This could generate errors if the distances are very large since the Earth
	 * is an ellipsoid.
	 * @param lat The latitude to calculate the conversion factor at.
	 * @return length per degree of latitude in the current units used by the system.
	 */
	double getLngFactor(double lat);

	/**
	 * Get the length per degree factor for length per degree in a fixed latitude line  
	 * at the given latitude.  The calculation accounts for the dependence of the Earth's 
	 * radius on latitude.   However, caution should be observed for calculations done 
	 * near the poles as out-of-bounds longitude values could easily occur.
	 * @param lat The latitude to calculate the conversion factor at.
	 * @return length per degree of longitude in the current units used by the system.
	 */
	double getLatFactor(double lat);

	/**
	 * Get the mean Earth radius in the units currently used by the system.
	 * @return The mean Earth radius in the system's current length measurement units.
	 */
	public double getEarthMeanRadius();

	/**
	 * Get the Earth's radius at the equator in the units currently used by the system.
	 * @return The equatorial radius of the Earth
	 */
	double getEarthEquatorialRadius();

	/**
	 * Get the Earth's radius at the poles in the units currently used by the system.
	 * @return The polar radius of the Earth
	 */
	double getEarthPolarRadius();	

	/**
	 * Convert a path of Point2D's to a path of ILatLng where the points are relative to the given origin location.
	 * Assumes that the distances involved are small relative to the size of the Earth or errors will occur 
	 * due to the ellipsoidal nature of the globe. 
	 * Units are in the currently used system length units.
	 * @param originLatLng The ILatLng designating the origin 
	 * @param pts path of points relative to the originLatLng 
	 * @return An IPath of ILatLngs
	 */
	public IPath<ILatLng> convertPointsToLatLng(ILatLng originLatLng, IPath<Point2D> pts);
	
	/**
	 * Convert paths of Point2D's to paths of ILatLng where the points are relative to the given origin location.
	 * Paths could be a simple IPath or a compound IPaths (path of paths) of points.
	 * Assumes that the distances involved are small relative to the size of the Earth or errors will occur 
	 * due to the ellipsoidal nature of the globe.
	 * Units are in the currently used system length units.
	 * @param originLatLng The LatLng designating the origin
	 * @param pts paths of points relative to the originLatLng 
	 * @return An IPaths of ILatLngs
	 */
	public IPaths<ILatLng> convertPointsToLatLng(ILatLng originLatLng, IPaths<Point2D> pts);
	
	/**
	 * Get the ILatLng that is the vector distance from the given origin ILatLng
	 * Assumes that the distances involved are small relative to the size of the Earth or errors will occur 
	 * due to the ellipsoidal nature of the globe.
	 * @param originLatLng  The origin coordinate
	 * @param pt A vector from the origin in feet
	 * @return an ILatLng corresponding to the point relative to the origin.
	 */
	public ILatLng getLatLngRelativeTo(ILatLng originLatLng, Point2D pt);
	
	
	/**
	 * Get the Earth's radius at the given latitude.  
	 * The Earth's radius depends on latitude because it is an ellipsoid.
	 * The units are in the currently used system units. 
	 * @param lat The latitude to measure the radius
	 * @return The Earth's radius at the given latitude
	 */
	public double getEarthRadius(double lat);
	
	

}
