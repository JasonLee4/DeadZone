package provided.owlMaps.components.marker;

import java.awt.geom.Point2D;
import java.util.UUID;

import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IOptionsKeys;



/**
 * Options for making markers
 * @author swong
 *
 */
public interface IMarkerOptions extends IOptionsKeys {
	
	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("a4695f1e-7d9e-42d3-bebd-d526d0801652");

	
	/**
	 * The offset from the marker's position to the tip of an InfoWindow that has been opened with the marker as anchor.
	 */
	public static MixedDataKey<Point2D> ANCHOR_POINT = new MixedDataKey<Point2D>(OPTIONS_ID, "anchorPoint", Point2D.class);// is an xy point not latlng
	
	/**
	 * Which animation to play when marker is added to a map.
	 */
	//TODO public static MixedDataKey<String> ANIMATION = new MixedDataKey<String>(OPTIONS_ID, "animation", String.class);  change to enum
	

	/**
	 * If true, the marker receives mouse and touch events. Default value is true.
	 */
	public static MixedDataKey<Boolean> CLICKABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "clickable", Boolean.class); // defaults to true

	/**
	 * If false, disables cross that appears beneath the marker when dragging. This option is true by default.
	 */
	public static MixedDataKey<Boolean> CROSS_ON_DRAG = new MixedDataKey<Boolean>(OPTIONS_ID, "crossOnDrag", Boolean.class);

	/**
	 * Mouse cursor to show on hover
	 */
	public static MixedDataKey<String> CURSOR = new MixedDataKey<String>(OPTIONS_ID, "cursor", String.class); 

	/**
	 * If true, the marker can be dragged. Default value is false.
	 */
	public static MixedDataKey<Boolean> DRAGGABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "draggable", Boolean.class); // defaults to false	

	/**
	 * Icon for the foreground. URL for the icon image
	 */
	public static MixedDataKey<String> ICON = new MixedDataKey<String>(OPTIONS_ID, "icon", String.class); // use URL to icon image



	/**
	 * Adds a label to the marker specified as a dictionary of IMarkerLLabelOptions
	 */
	public static MixedDataKey<IMarkerLabel> LABEL = new MixedDataKey<IMarkerLabel>(OPTIONS_ID, "label", IMarkerLabel.class); 

	/**
	 * The marker's opacity between 0.0 and 1.0.
	 */
	public static MixedDataKey<Double> OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "opacity", Double.class);

	/**
	 * Optimization renders many markers as a single static element. Optimized rendering is enabled by default. 
	 * Disable optimized rendering for animated GIFs or PNGs, or when each marker must be rendered as a separate DOM element (advanced usage only).
	 */
	public static MixedDataKey<Boolean> OPTIMIZED = new MixedDataKey<Boolean>(OPTIONS_ID, "optimized", Boolean.class);

	/**
	 * Marker position. Required.
	 */
	public static MixedDataKey<ILatLng> POSITION = new MixedDataKey<ILatLng>(OPTIONS_ID, "position", ILatLng.class); 
	
	//TODO  shape	

	/**
	 * Rollover text
	 */
	public static MixedDataKey<String> TITLE = new MixedDataKey<String>(OPTIONS_ID, "title", String.class);

	/**
	 * If true, the marker is visible. Defaults to false
	 */
	public static MixedDataKey<Boolean> VISIBLE = new MixedDataKey<Boolean>(OPTIONS_ID, "visible", Boolean.class);

	/**
	 * All markers are displayed on the map in order of their zIndex, with higher values displaying in front of markers with lower values. 
	 * By default, markers are displayed according to their vertical position on screen, with lower markers appearing in front of markers 
	 * further up the screen.
	 */
	public static MixedDataKey<Double> ZINDEX = new MixedDataKey<Double>(OPTIONS_ID, "zIndex", Double.class);
	
	
	
	/**
	 * Make a dictionary of the default options for the component.
	 * @return  A dictionary with any default options 
	 */
	public static IMixedDataDictionary makeDefault() {
		IMixedDataDictionary result = IOptionsKeys.makeDefault();
		result.put(VISIBLE, false);
		result.put(TITLE, "Marker");
		return result;

		// TODO override put() to check for valid option -- do this in superclass?

	}
	
	/**
	 * *FOR INTERNAL USE ONLY!!* OWLMAPS  DEVELOPER CODE SHOULD *NOT* USE THIS METHOD! 
	 * Factory to create the IOptionsFiller that converts the component options into 
	 * their corresponding Javascript Google Map component options.
	 * 
	 * @param cefUtils   The IJsUtils created by the system
	 * @return An IOptionsFiller for this component's options.
	 */
	public static IOptionsFiller makeOptionsFiller(ICefUtils cefUtils) {
		
		return new AOptionsFiller<IMarkerOptions>(cefUtils, IMarkerOptions.class) {
			{
				put(ANCHOR_POINT, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, Point2D.class));
				});
				put(POSITION, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, ILatLng.class));
				});
				
				put(LABEL, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, IMarkerLabel.class));
				});				
				
			}
		};
	}
}


