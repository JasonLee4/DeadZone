package provided.owlMaps.components;

import provided.mixedData.IMixedDataDictionary;

import provided.owlMaps.components.infowindow.IInfoWindow;
import provided.owlMaps.components.marker.IMarker;
import provided.owlMaps.components.overlay.IGroundOverlay;
import provided.owlMaps.components.shapes.ICircle;
import provided.owlMaps.components.shapes.IPolygon;
import provided.owlMaps.components.shapes.IPolyline;
import provided.owlMaps.components.shapes.IRectangle;

/**
 * Factory for components tied to a specific map.
 * In general, unless otherwise specified, components will be constructed NOT visible 
 * as this enables "pre-construction" of components before they are used. 
 * Map components are NOT Serializable and MUST be created on the target machine! 
 * THIS FACTORY *MUST* BE MADE BY AN OwlMapControl INSTANCE *AFTER* THE MAP IS CREATED!
 * DO *NOT* ATTEMPT TO INSTANTIATE THIS FACTORY DIRECTLY!
 * @author swong
 *
 */
public interface IMapComponentFactory {

	/**
	 * Create a new IMarker with the given options
	 * @param options  Options dictionary for the component with IMarkerOptions entries
	 * @return An instance of the component
	 * @throws ClassNotFoundException if an option is requesting an unknown option type
	 */
	public IMarker makeMarker(IMixedDataDictionary options) throws ClassNotFoundException;

	/**
	 * Create a new IInfoWindow with the given options
	 * @param options  Options dictionary for the component with IInfoWindowOptions entries
	 * @return An instance of the component
	 * @throws ClassNotFoundException if an option is requesting an unknown option type
	 */
	public IInfoWindow makeInfoWindow(IMixedDataDictionary options) throws ClassNotFoundException;

	/**
	 * Create a new IPolygon with the given options
	 * @param options  Options dictionary for the component with IPolygonOptions entries
	 * @return An instance of the component
	 * @throws ClassNotFoundException if an option is requesting an unknown option type
	 */
	public IPolygon makePolygon(IMixedDataDictionary options ) throws ClassNotFoundException;

	/**
	 * Create a new IPolyline with the given options
	 * @param options  Options dictionary for the component with IPolylineOptions entries
	 * @return An instance of the component
	 * @throws ClassNotFoundException if an option is requesting an unknown option type
	 */
	public IPolyline makePolyline(IMixedDataDictionary options) throws ClassNotFoundException;

	/**
	 * Create a new IRectangle with the given options
	 * @param options  Options dictionary for the component with IRectangleOptions entries
	 * @return An instance of the component
	 * @throws ClassNotFoundException if an option is requesting an unknown option type
	 */
	public IRectangle makeRectangle(IMixedDataDictionary options) throws ClassNotFoundException;

	/**
	 * Create a new ICircle with the given options
	 * @param options  Options dictionary for the component with ICircleOptions entries
	 * @return An instance of the component
	 * @throws ClassNotFoundException if an option is requesting an unknown option type
	 */
	public ICircle makeCircle(IMixedDataDictionary options) throws ClassNotFoundException;

	/**
	 * Create a new IGroundOverlay with the given options
	 * @param options  Options dictionary for the component with IGroundOverlayOptions entries
	 * @return An instance of the component
	 * @throws ClassNotFoundException if an option is requesting an unknown option type
	 */
	public IGroundOverlay makeGroundOverlay(IMixedDataDictionary options) throws ClassNotFoundException;
	
}
