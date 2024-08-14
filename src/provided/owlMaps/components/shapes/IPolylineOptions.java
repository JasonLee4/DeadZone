package provided.owlMaps.components.shapes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.IIconSequence;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IOptionsKeys;
import provided.owlMaps.general.IPath;
import provided.owlMaps.general.IPathLatLng;


/**
 * The options for a IPolyLine
 * @author swong
 *
 */
public interface IPolylineOptions extends IOptionsKeys {
	
	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("5ec7543a-90ba-46fa-b8e1-0303deaa1b25");

	/**
	 * 	Indicates whether this Polyline handles mouse events. Defaults to true.
	 */
	public static MixedDataKey<Boolean> CLICKABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "clickable", Boolean.class); // defaults to true
	
	/**
	 * If set to true, the user can drag this shape over the map. The geodesic property defines the mode of dragging. Defaults to false.
	 */
	public static MixedDataKey<Boolean> DRAGGABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "draggable", Boolean.class); // defaults to false
	
	/**
	 * If set to true, the user can edit this shape by dragging the control points shown at the vertices and on each segment. Defaults to false.
	 */
	public static MixedDataKey<Boolean> EDITABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "editable", Boolean.class); // defaults to false
	
	/**
	 * When true, edges of the polygon are interpreted as geodesic and will follow the curvature of the Earth. 
	 * When false, edges of the polygon are rendered as straight lines in screen space. 
	 * Note that the shape of a geodesic polygon may appear to change when dragged, 
	 * as the dimensions are maintained relative to the surface of the earth. Defaults to false.
	 */
	public static MixedDataKey<Boolean> GEODESIC = new MixedDataKey<Boolean>(OPTIONS_ID, "geodesic", Boolean.class); // defaults to false
	
	/**
	 * The icons to be rendered along the polyline as represented by a List of options dictionaries of IIconSequenceOptions
	 */
	@SuppressWarnings("unchecked")
	public static MixedDataKey<List<IMixedDataDictionary>> ICONS = new MixedDataKey<List<IMixedDataDictionary>>(OPTIONS_ID, "icons", (Class<List<IMixedDataDictionary>>) ((Class<?>)List.class)); 

	/**
	 * The ordered sequence (IPath) of ILatLng coordinates of the Polyline. 
	 */
	@SuppressWarnings("unchecked")
	public static MixedDataKey<IPath<ILatLng>> PATH = new MixedDataKey<IPath<ILatLng>>(OPTIONS_ID, "path", (Class<IPath<ILatLng>>)((Class<?>)IPath.class)); 
	
	/**
	 * The stroke color. All CSS3 colors are supported except for extended named colors.
	 */
	public static MixedDataKey<String> STROKE_COLOR = new MixedDataKey<String>(OPTIONS_ID, "strokeColor", String.class);  // standard (not extended) CSS3 color name or number as "#XXXXXX" string, defaults to "Black"
	
	/**
	 * The stroke opacity between 0.0 and 1.0.
	 */
	public static MixedDataKey<Double> STROKE_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "strokeOpacity", Double.class);	// [0.0, 1.0]   default = 1.0
	
	/**
	 * The stroke width in pixels.
	 */
	public static MixedDataKey<Integer> STROKE_WEIGHT = new MixedDataKey<Integer>(OPTIONS_ID, "strokeWeight", Integer.class); // defaults to 2
	
	/**
	 * Whether this polyline is visible on the map. Defaults to false.
	 */
	public static MixedDataKey<Boolean> VISIBLE = new MixedDataKey<Boolean>(OPTIONS_ID, "visible", Boolean.class);;
	
	/**
	 * The zIndex compared to other polys.
	 */
	public static MixedDataKey<Double> ZINDEX = new MixedDataKey<Double>(OPTIONS_ID, "zIndex", Double.class);


	
	/**
	 * Make a dictionary of the default options for the component.
	 * @return  A dictionary with any default options 
	 */
	public static IMixedDataDictionary makeDefault() {
		IMixedDataDictionary result = IOptionsKeys.makeDefault();
		result.put(STROKE_COLOR, "Black");
		result.put(STROKE_OPACITY,  1.0);
		result.put(STROKE_WEIGHT,  2);
		result.put(DRAGGABLE, false);
		result.put(EDITABLE, false);
		result.put(GEODESIC, true);
		result.put(VISIBLE, false);
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
			
		return new AOptionsFiller<IPolylineOptions>(cefUtils, IPolylineOptions.class) {

			{
				put(PATH, (key, value, jsonWriter)->{
//					jsOptions.putProperty(key.getDesc(), jsUtils.toJsArray(value) );
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, IPathLatLng.class));
				});
				put(ICONS, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.beginArray();  // value is a List IIconSequence options
					System.out.println("[IPolyline.makeOptionsFiller()]  Processing ICONS array = "+Arrays.toString(value.toArray()));
					value.forEach((iconSequence)->{
						try {
							System.out.println("[IPolyline.makeOptionsFiller()] Processing ICONS array, iconSequence = "+iconSequence); 
							jsonWriter.jsonValue(toJson(iconSequence, IIconSequence.class)); // Process as IIconSequence class in order to wrap the options
						} catch (IOException e) {
							System.err.println("[IPolyline.makeOptionsFiller()] Exception while processing ICONS option list value = "+value+": "+e);
							e.printStackTrace();
						}
					});
					
					jsonWriter.endArray();

				});
			}
		};
	}
}


