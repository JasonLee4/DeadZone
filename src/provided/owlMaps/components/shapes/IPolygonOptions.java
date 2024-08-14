package provided.owlMaps.components.shapes;

import java.util.UUID;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IOptionsKeys;
import provided.owlMaps.general.IPaths;
import provided.owlMaps.general.IPathsLatLng;


/**
 * Options for creating polygons
 * @author swong
 *
 */
public interface IPolygonOptions extends IOptionsKeys {
	
	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("7200b64d-6e41-4481-b03c-31303fbfa80b");

	/**
	 * 	Indicates whether this Polygon handles mouse events. Defaults to true.
	 */
	public static MixedDataKey<Boolean> CLICKABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "clickable", Boolean.class); 
	
	/**
	 * 	If set to true, the user can drag this shape over the map. The geodesic property defines the mode of dragging. Defaults to false.
	 */
	public static MixedDataKey<Boolean> DRAGGABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "draggable", Boolean.class); 
	
	/**
	 * 	If set to true, the user can edit this shape by dragging the control points shown at the vertices and on each segment. Defaults to false.
	 */
	public static MixedDataKey<Boolean> EDITABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "editable", Boolean.class); // defaults to false
	
	/**
	 * 	The fill color. All CSS3 colors are supported except for extended named colors.
	 */
	public static MixedDataKey<String> FILL_COLOR = new MixedDataKey<String>(OPTIONS_ID, "fillColor", String.class);  // standard (not extended) CSS3 color name
	
	/**
	 * 	The fill opacity between 0.0 and 1.0
	 */
	public static MixedDataKey<Double> FILL_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "fillOpacity", Double.class);  // [0.0, 1.0]
	
	/**
	 * 	When true, edges of the polygon are interpreted as geodesic and will follow the curvature of the Earth. 
	 * When false, edges of the polygon are rendered as straight lines in screen space. 
	 * Note that the shape of a geodesic polygon may appear to change when dragged, as the dimensions are maintained relative 
	 * to the surface of the earth. Defaults to false.
	 */
	public static MixedDataKey<Boolean> GEODESIC = new MixedDataKey<Boolean>(OPTIONS_ID, "geodesic", Boolean.class); // defaults to false
	
	/**
	 * 	The ordered sequence of LatLng coordinates that designates a closed loop. Unlike polylines, a polygon may consist of one or more paths. 
	 * As a result, the paths property may specify one or more arrays of LatLng coordinates. 
	 * Paths are closed automatically; do not repeat the first vertex of the path as the last vertex. 
	 * Simple polygons may be defined using an IPath&lt;ILatLng&gt; (List of ILatLngs). More complex polygons may specify an IPathList&lt;ILatLng&gt; (A list of IPaths). 
	 */
	@SuppressWarnings("unchecked")
	public static MixedDataKey<IPaths<ILatLng>> PATHS = new MixedDataKey<IPaths<ILatLng>>(OPTIONS_ID, "paths", (Class<IPaths<ILatLng>>)((Class<?>)IPaths.class)); 	
	
	/**
	 * 	The stroke color. All CSS3 colors are supported except for extended named colors.
	 */
	public static MixedDataKey<String> STROKE_COLOR = new MixedDataKey<String>(OPTIONS_ID, "strokeColor", String.class);  // standard (not extended) CSS3 color name

	/**
	 * 	The stroke opacity between 0.0 and 1.0
	 */
	public static MixedDataKey<Double> STROKE_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "strokeOpacity", Double.class);	// [0.0, 1.0]
	
	/**
	 * The stroke position. Defaults to StrokePosition.CENTER. This property is not supported on Internet Explorer 8 and earlier.	
	 */
	public static MixedDataKey<StrokePosition> STROKE_POSITION = new MixedDataKey<StrokePosition>(OPTIONS_ID, "strokePosition", StrokePosition.class);	// Defaults to CENTER

	/**
	 * 	The stroke width in pixels.
	 */
	public static MixedDataKey<Integer> STROKE_WEIGHT = new MixedDataKey<Integer>(OPTIONS_ID, "strokeWeight", Integer.class);

	/**
	 * 	Whether this polygon is visible on the map. Defaults to true.
	 */
	public static MixedDataKey<Boolean> VISIBLE = new MixedDataKey<Boolean>(OPTIONS_ID, "visible", Boolean.class);;

	/**
	 * 	The zIndex compared to other polys.
	 */
	public static MixedDataKey<Double> ZINDEX = new MixedDataKey<Double>(OPTIONS_ID, "zIndex", Double.class);


	
	/**
	 * Make a dictionary of the default options for the component.
	 * @return  A dictionary with any default options 
	 */
	public static IMixedDataDictionary makeDefault() {
		IMixedDataDictionary result = IOptionsKeys.makeDefault();
//				this.put(FILL_COLOR,  "Black");
//				this.put(FILL_OPACITY,  1.0);
		result.put(DRAGGABLE, false);
		result.put(EDITABLE, false);
		result.put(GEODESIC, false);
		result.put(VISIBLE, false);
			
			// TODO override put() to check for valid option -- do this in superclass?
		return result;
	}


	

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Factory to create the IOptionsFiller that converts the component options into 
	 * their corresponding Javascript Google Map component options.
	 * 
	 * @param cefUtils   The IJsUtils created by the system
	 * @return An IOptionsFiller for this component's options.
	 */
	public static IOptionsFiller makeOptionsFiller(ICefUtils cefUtils) {
		
		
		return new AOptionsFiller<IPolygonOptions>(cefUtils, IPolygonOptions.class) {

			{
				put(PATHS, (key, value, jsonWriter)->{
//					jsOptions.putProperty(key.getDesc(), cefUtils.toJsArray(value));
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, IPathsLatLng.class));
				});
				
				put(STROKE_POSITION, (key, value, jsonWriter)->{
//					jsOptions.putProperty(key.getDesc(), cefUtils.toJsStrokePosition(value));
					
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, StrokePosition.class));
				});
				
			}
		};
	}
}


