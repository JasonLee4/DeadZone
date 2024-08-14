package provided.owlMaps.map.data;

import java.util.UUID;

import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefObject;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.IProcessOption;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.IOptionsKeys;


/**
 * 
 * These options specify the way a Feature should appear when displayed on a map.
 * @author swong
 *
 */
public interface IMapDataStyleOptions extends IOptionsKeys {
	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("b1763b49-cd55-4817-8280-1bab4c0dcbe0");
	
	/**
	 * If true, the marker receives mouse and touch events. Default value is true.
	 */
	public static MixedDataKey<Boolean> CLICKABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "clickable", Boolean.class); // TODO: defaults to false until mouse events are operational 

	/**
	 * Mouse cursor to show on hover. Only applies to point geometries.
	 */
	public static MixedDataKey<String> CURSOR = new MixedDataKey<String>(OPTIONS_ID, "cursor", String.class); 

	/**
	 * If true, the object can be dragged across the map and the underlying feature will have its geometry updated. 
	 * Default value is false.
	 */
	public static MixedDataKey<Boolean> DRAGGABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "draggable", Boolean.class); // defaults to false	

	/**
	 * If true, the object can be edited by dragging control points and the underlying feature will have its geometry updated. 
	 * Only applies to LineString and Polygon geometries. Default value is false.
	 */
	public static MixedDataKey<Boolean> EDITABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "editable", Boolean.class); 

	/**
	 * The fill color. All CSS3 colors are supported except for extended named colors.
	 * Only applies to polygon geometries.
	 */
	public static MixedDataKey<String> FILL_COLOR = new MixedDataKey<String>(OPTIONS_ID, "fillColor", String.class); 
	
	/**
	 * The fill opacity between 0.0 and 1.0. Only applies to polygon geometries.
	 */
	public static MixedDataKey<Double> FILL_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "fillOpacity", Double.class);
	
	/**
	 * Icon for the foreground. URL for the icon image
	 */
	public static MixedDataKey<String> ICON = new MixedDataKey<String>(OPTIONS_ID, "icon", String.class); // use URL to icon image

// TODO
//	/**
//	 * Shape of the marker image
//	 */
//	public static MixedDataKey<IMarkerShape> SHAPE = new MixedDataKey<IMarkerShape>(OPTIONS_ID, "shape", IMarkerShape.class); 

	/**
	 * The stroke color. All CSS3 colors are supported except for extended named colors.
	 * Only applies to line and polygon geometries.
	 */
	public static MixedDataKey<String> STROKE_COLOR = new MixedDataKey<String>(OPTIONS_ID, "strokeColor", String.class); 

	/**
	 * The stroke opacity between 0.0 and 1.0.  Only applies to line and polygon geometries.
	 */
	public static MixedDataKey<Double> STROKE_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "strokeOpacity", Double.class); 

	/**
	 * The stroke width in pixels.  Only applies to line and polygon geometries.
	 */
	public static MixedDataKey<Integer> STROKE_WEIGHT = new MixedDataKey<Integer>(OPTIONS_ID, "strokeWeight", Integer.class);


	/**
	 * Rollover text. Only applies to line and polygon geometries.
	 */
	public static MixedDataKey<String> TITLE = new MixedDataKey<String>(OPTIONS_ID, "title", String.class);

	/**
	 * If true, the feature is visible. Defaults to false
	 */
	public static MixedDataKey<Boolean> VISIBLE = new MixedDataKey<Boolean>(OPTIONS_ID, "visible", Boolean.class);

	/**
	 * All features are displayed on the map in order of their zIndex, with higher values displaying in front of features with lower values. 
	 * Markers are always displayed in front of line-strings and polygons.
	 */
	public static MixedDataKey<Double> ZINDEX = new MixedDataKey<Double>(OPTIONS_ID, "zIndex", Double.class);

	
	

	/**
	 * Style for all features in the collection. For more details, see the setStyle() method above.
	 */
	public static MixedDataKey<IMixedDataDictionary> STYLE = new MixedDataKey<IMixedDataDictionary>(OPTIONS_ID, "style", IMixedDataDictionary.class);

	
	
	/**
	 * Make a dictionary of the default options for the component.
	 * @return  A dictionary with any default options 
	 */
	public static IMixedDataDictionary makeDefault() {
		IMixedDataDictionary result = IOptionsKeys.makeDefault();
		result.put(VISIBLE, false);
		result.put(CLICKABLE, false); // TODO:   Change this once mouse events become operational
		return result;

		// TODO override put() to check for valid option -- do this in superclass?

	}
	
	/**
	 * *FOR INTERNAL USE ONLY!!* OWLMAPS  DEVELOPER CODE SHOULD *NOT* USE THIS METHOD! 
	 * Factory to create the IOptionsFiller that converts the component options into 
	 * their corresponding Javascript Google Map component options.
	 * 
	 * @param cefUtils   The ICefUtils created by the system
	 * @return An IOptionsFiller for this component's options.
	 */
	public static IOptionsFiller makeOptionsFiller(ICefUtils cefUtils) {
		
		return new AOptionsFiller<IMapDataStyleOptions>(cefUtils, IMapDataStyleOptions.class) {
			{
											
			}
			
			/**
			 * Make IProcessOption that excludes the usual map field
			 */
			@Override
			protected IProcessOption<ICefObject> makeDefaultMapOptionProcess() {
				return IProcessOption.makeNoOp();
			}
		};
	}
}


