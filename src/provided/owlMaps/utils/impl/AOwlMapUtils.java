package provided.owlMaps.utils.impl;

import java.awt.geom.Point2D;
import java.util.stream.Collectors;

import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IPath;
import provided.owlMaps.general.IPathList;
import provided.owlMaps.general.IPaths;
import provided.owlMaps.general.IPathsAlgo;
import provided.owlMaps.general.impl.Path;
import provided.owlMaps.general.impl.PathList;
import provided.owlMaps.utils.IOwlMapUtils;
import provided.owlMaps.utils.IOwlMapsDefs;

/**
 * Abstract implementation of IOwlMapUtils
 * @author swong
 *
 */
public abstract class AOwlMapUtils implements IOwlMapUtils {

	@Override
	public IPath<ILatLng> convertPointsToLatLng(ILatLng originLatLng, IPath<Point2D> pts){
		return new Path<ILatLng>(pts.stream().map((pt)->{ return getLatLngRelativeTo(originLatLng, pt);}).collect(Collectors.toList()));	
	}
	
	
	@Override
	public IPaths<ILatLng> convertPointsToLatLng(ILatLng originLatLng, IPaths<Point2D> pts) {
		System.out.println("convert IPaths");
		return pts.execute(new IPathsAlgo<IPaths<ILatLng>, Point2D, Void>(){

			/**
			 * For Serializable
			 */
			private static final long serialVersionUID = 1285129706760351178L;

			@Override
			public IPaths<ILatLng> pathCase(IPath<Point2D> host, Void... params) {
				return  convertPointsToLatLng(originLatLng, host); // toPath(host);
			}

			@Override
			public IPaths<ILatLng> pathListCase(IPathList<Point2D> host, Void... params) {
				// Can't do recursive call here because constructor of PathList explicitly wants a Collection of IPath objects, not an IPaths.
				return  new PathList<ILatLng>(host.stream().map((path)->{ return convertPointsToLatLng(originLatLng, path);}).collect(Collectors.toList()));
			}
			
		});
	}


	@Override
	public ILatLng getLatLngRelativeTo(ILatLng originLatLng, Point2D pt) {
		
		double newLat = originLatLng.getLat()+pt.getX()/getLatFactor(originLatLng.getLat());
		
		double newLng = originLatLng.getLng()+pt.getY()/getLngFactor(newLat);

		return ILatLng.make(newLat, newLng); 
	}
	
	@Override 
	public double getEarthMeanRadius() {
		return IOwlMapsDefs.EARTH_MEAN_RADII.get(getSystemLengthUnit());
	}
	
	@Override
	public double getEarthEquatorialRadius() {
		return (3.0*getEarthMeanRadius()*IOwlMapsDefs.EARTH_INVERSE_FLATTENING)/(3.0*IOwlMapsDefs.EARTH_INVERSE_FLATTENING-1.0);
	}
	
	@Override
	public double getEarthPolarRadius() {
		return (3.0*getEarthMeanRadius()*(IOwlMapsDefs.EARTH_INVERSE_FLATTENING-1))/(3.0*IOwlMapsDefs.EARTH_INVERSE_FLATTENING-1.0);
	}			
	
	@Override
	public double getEarthRadius(double lat) {
//		double a = 6378137.0;   // major axis, meters  
//		double b = 6356752.314245;   // minor axis, meters
//		
//		double f_inv = 298.257223563;  // inverse flattening factor f = (a-b)/a
		
//		double rMean = getEarthMeanRadius() ; //(2.0*a+b)/3.0;  // mean radius (2*a+b)/3 =  6371008.8 meters  https://en.m.wikipedia.org/wiki/Earth_radius#Mean_radii
		
		double latRadians = lat*IOwlMapsDefs.RADIANS_PER_DEGREE; //Math.PI*lat/180.0;
		
		double a = getEarthEquatorialRadius(); //(3.0*rMean*f_inv)/(3*f_inv-1.0);
		
		double b = getEarthPolarRadius(); //((3.0*rMean*(f_inv-1.0))/(3*f_inv-1.0);
		
		double aCosLat = a*Math.cos(latRadians);
		double bSinLat = b*Math.sin(latRadians);							
		
		double radius = Math.sqrt((Math.pow(a*aCosLat, 2.0)+Math.pow(b*bSinLat, 2.0))/(Math.pow(aCosLat, 2.0)+Math.pow(bSinLat, 2.0)));
		System.out.println("[getEarthRadius()] radius = "+radius+" @ lat = "+lat);
		return radius;
	}
	
	@Override
	public double getLatFactor(double lat) {
		double r = getEarthRadius(lat);   // = length per radian
		return r*IOwlMapsDefs.RADIANS_PER_DEGREE;	
	}
	
	@Override
	public double getLngFactor(double lat) {
		double r = getEarthRadius(lat);   // = length per radian
		
		double latRadians = lat*IOwlMapsDefs.RADIANS_PER_DEGREE; 
		
		double r_lat = r*Math.cos(latRadians);
		
		return r_lat*IOwlMapsDefs.RADIANS_PER_DEGREE;
		
	}


}
