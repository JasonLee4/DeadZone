package provided.owlMaps.components.shapes;

import java.util.UUID;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.ILatLngBounds;
import provided.owlMaps.general.IOptionsKeys;


/**
 * Options for rectangles
 * @author swong
 *
 */
public interface IRectangleOptions extends IOptionsKeys {

	/**
	 * Unique identifier for this class of options. Always refer to this field by
	 * name and not by its value to ensure compatibility when the codebase is
	 * updated as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("93e2b58d-710b-4f44-aa67-56a02267d135");

	/**
	 * The bounds of the rectangle
	 */
	public static MixedDataKey<ILatLngBounds> BOUNDS = new MixedDataKey<ILatLngBounds>(OPTIONS_ID, "bounds", ILatLngBounds.class);

	/**
	 * Indicates whether this Rectangle handles mouse events. Defaults to true.
	 */
	public static MixedDataKey<Boolean> CLICKABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "clickable", Boolean.class); 

	/**
	 * If set to true, the user can drag this rectangle over the map. Defaults to false.
	 */
	public static MixedDataKey<Boolean> DRAGGABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "draggable", Boolean.class); 

	/**
	 * If set to true, the user can edit this rectangle by dragging the control points shown at the corners and on each edge. Defaults to false.
	 */
	public static MixedDataKey<Boolean> EDITABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "editable", Boolean.class); 

	/**
	 * The fill color. All CSS3 colors are supported except for extended named colors.
	 */
	public static MixedDataKey<String> FILL_COLOR = new MixedDataKey<String>(OPTIONS_ID, "fillColor", String.class); 

	/**
	 * The fill opacity between 0.0 and 1.0
	 */
	public static MixedDataKey<Double> FILL_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "fillOpacity", Double.class); 

	/**
	 * The stroke color. All CSS3 colors are supported except for extended named colors.
	 */
	public static MixedDataKey<String> STROKE_COLOR = new MixedDataKey<String>(OPTIONS_ID, "strokeColor", String.class); 

	/**
	 * The stroke opacity between 0.0 and 1.0
	 */
	public static MixedDataKey<Double> STROKE_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "strokeOpacity", Double.class); 

	/**
	 * The stroke position. Defaults to StrokePosition.CENTER. This property is not supported on Internet Explorer 8 and earlier.
	 */
	public static MixedDataKey<StrokePosition> STROKE_POSITION = new MixedDataKey<StrokePosition>(OPTIONS_ID, "strokePosition", StrokePosition.class);	

	/**
	 * The stroke width in pixels.
	 */
	public static MixedDataKey<Integer> STROKE_WEIGHT = new MixedDataKey<Integer>(OPTIONS_ID, "strokeWeight", Integer.class);

	/**
	 * Whether this rectangle is visible on the map. Defaults to true.
	 */
	public static MixedDataKey<Boolean> VISIBLE = new MixedDataKey<Boolean>(OPTIONS_ID, "visible", Boolean.class);;

	/**
	 * The zIndex compared to other polys.
	 */
	public static MixedDataKey<Double> ZINDEX = new MixedDataKey<Double>(OPTIONS_ID, "zIndex", Double.class);

	/**
	 * Make a dictionary of the default options for the component.
	 * 
	 * @return A dictionary with any default options
	 */
	public static IMixedDataDictionary makeDefault() {
		IMixedDataDictionary result = IOptionsKeys.makeDefault();

//				result.put(FILL_COLOR,  "Black");
//				result.put(FILL_OPACITY,  1.0);
		result.put(DRAGGABLE, false);
		result.put(EDITABLE, false);
		result.put(VISIBLE, false);
		return result;
	}

	/**
	 * *FOR INTERNAL USE ONLY!!* OWLMAPS DEVELOPER CODE SHOULD *NOT* USE THIS
	 * METHOD! Factory to create the IOptionsFiller that converts the component
	 * options into their corresponding Javascript Google Map component options.
	 * 
	 * @param cefUtils The IJsUtils created by the system
	 * @return An IOptionsFiller for this component's options.
	 */
	public static IOptionsFiller makeOptionsFiller(ICefUtils cefUtils) {

		return new AOptionsFiller<IRectangleOptions>(cefUtils, IRectangleOptions.class) {

			{
				put(BOUNDS, (key, value, jsonWriter) -> {
//					jsOptions.putProperty(key.getDesc(), cefUtils.toJsLatLngBounds(value));
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, ILatLngBounds.class));
					
					
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
