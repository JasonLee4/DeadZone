package provided.owlMaps.components.shapes;

import java.util.UUID;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IOptionsKeys;



/**
 * Options for making circles
 * @author swong
 *
 */
public interface ICircleOptions extends IOptionsKeys {
	
	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("db17c767-89f5-4924-b98c-b09b7636b8c2");

	/**
	 * The center of the Circle.
	 */
	public static MixedDataKey<ILatLng> CENTER = new MixedDataKey<ILatLng>(OPTIONS_ID, "center", ILatLng.class); 		

	/**
	 * Indicates whether this Circle handles mouse events. Defaults to true.
	 */
	public static MixedDataKey<Boolean> CLICKABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "clickable", Boolean.class); // defaults to true

	/**
	 * If set to true, the user can drag this circle over the map. Defaults to false.
	 */
	public static MixedDataKey<Boolean> DRAGGABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "draggable", Boolean.class); // defaults to false

	/**
	 * If set to true, the user can edit this circle by dragging the control points shown at the center and around the circumference of the circle. 
	 * Defaults to false.
	 */
	public static MixedDataKey<Boolean> EDITABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "editable", Boolean.class); // defaults to false

	/**
	 * The fill color. All CSS3 colors are supported except for extended named colors.
	 */
	public static MixedDataKey<String> FILL_COLOR = new MixedDataKey<String>(OPTIONS_ID, "fillColor", String.class);  

	/**
	 * The fill opacity between 0.0 and 1.0.
	 */
	public static MixedDataKey<Double> FILL_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "fillOpacity", Double.class); 

	/**
	 * The radius of the circle on the Earth's surface in the current system units.
	 * Note: underlying Google maps uses meters.   The conversion is automatic.
	 */
	public static MixedDataKey<Double> RADIUS = new MixedDataKey<Double>(OPTIONS_ID, "radius", Double.class);	
	
	/**
	 * The stroke color. All CSS3 colors are supported except for extended named colors.
	 */
	public static MixedDataKey<String> STROKE_COLOR = new MixedDataKey<String>(OPTIONS_ID, "strokeColor", String.class);  // standard (not extended) CSS3 color name

	/**
	 * The stroke opacity between 0.0 and 1.0.
	 */
	public static MixedDataKey<Double> STROKE_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "strokeOpacity", Double.class);	// [0.0, 1.0]

	/**
	 * The stroke position. Defaults to StrokePosition.CENTER. This property is not supported on Internet Explorer 8 and earlier.
	 */
	public static MixedDataKey<StrokePosition> STROKE_POSITION = new MixedDataKey<StrokePosition>(OPTIONS_ID, "strokePosition", StrokePosition.class);	// Defaults to CENTER
	
	/**
	 * The stroke width in pixels.
	 */
	public static MixedDataKey<Integer> STROKE_WEIGHT = new MixedDataKey<Integer>(OPTIONS_ID, "strokeWeight", Integer.class);

	/**
	 * Whether this circle is visible on the map. Defaults to true.
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
//				this.put(FILL_COLOR,  "Black");
//				this.put(FILL_OPACITY,  1.0);
		result.put(DRAGGABLE, false);
		result.put(EDITABLE, false);
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
		
		
		return new AOptionsFiller<ICircleOptions>(cefUtils, ICircleOptions.class) {

			{
				put(CENTER, (key, value, jsonWriter)->{
//					jsOptions.putProperty(key.getDesc(), jsUtils.toJsLatLng(value));
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, ILatLng.class));
				});
				
				put(RADIUS, (key, value, jsonWriter)->{
//					jsOptions.putProperty(key.getDesc(), value/IOwlMapsDefs.LENGTH_CONVERSIONS.get(mapUtils.getSystemLengthUnit()));
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value/cefUtils.getSysLengthPerMeter(), Double.class));   // Need to convert to meters
				});
				
				put(STROKE_POSITION, (key, value, jsonWriter)->{
//					jsOptions.putProperty(key.getDesc(), jsUtils.toJsStrokePosition(value));
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, StrokePosition.class));
				});
			}
		};
	}
}


