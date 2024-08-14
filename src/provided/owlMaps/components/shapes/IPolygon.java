package provided.owlMaps.components.shapes;

import provided.mixedData.MixedDataDictionary;
import provided.owlMaps.general.IEditableObject;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IOptionedMapObject;
import provided.owlMaps.general.IPath;
import provided.owlMaps.general.IPathList;
import provided.owlMaps.general.IPaths;
import provided.owlMaps.general.IVisibleMapObject;
import provided.owlMaps.mouse.IComponentMouseDragEventObject;



/**
 * Represents a polygon drawn on the map
 * Corresponds to the Google Maps Javascript API google.maps.Polygon
 * A polygon is defined by one or more series of connected coordinates in an ordered sequence,
 * i.e. an IPath or IPathList of ILatLngs. 
 * Additionally, polygons form a closed loop and define a filled region. 
 * A polygons can be defined using multiple separate paths of points to create shapes 
 * with holes for instance. 
 * @author swong
 *
 */
public interface IPolygon extends  IVisibleMapObject, IEditableObject, IPolyMouseEventObject, IComponentMouseDragEventObject, IOptionedMapObject<MixedDataDictionary> {

	/**
	 * Retrieves the path of ILatLngs if only an IPath was used or the first IPath is an IPathList was used.
	 * @return An IPath of ILatLngs
	 */
	public IPath<ILatLng> getPath();
	
	/**
	 * Returns the paths for this polygon.  
	 * If only one IPath was used, then an IPathList of only one IPath element is returned.
	 * @return an IPaths of ILatLngs
	 */
	public IPathList<ILatLng> getPaths();
	
	/**
	 * Set the path or the first path if an IPathList was used.
	 * @param path an IPath of ILatLngs
	 */
	public void setPath(IPath<ILatLng> path);
	
	/**
	 * Sets the path(s) of this polygon
	 * @param paths An IPaths object of ILatLngs which could be either an IPath or an IPathList.
	 */
	public void setPaths(IPaths<ILatLng> paths);


}
