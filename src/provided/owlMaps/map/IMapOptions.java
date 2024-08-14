package provided.owlMaps.map;

import java.util.UUID;

import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.IProcessOption;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IOptionsKeys;
import provided.owlMaps.general.IPlace;
import provided.owlMaps.utils.IPlace_Samples;


/**
 * Available options for configuring a map
 * @author swong
 *
 */
public interface IMapOptions extends IOptionsKeys {
	
	
	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("5607567c-21d2-44db-be13-a83a1aa113c1");

	/**
	 * Color used for the background of the Map div. This color will be visible when tiles have not yet loaded as the user pans. 
	 * This option can only be set when the map is initialized.
	 */
	public static MixedDataKey<String> BACKGROUND_COLOR = new MixedDataKey<String>(OPTIONS_ID, "backgroundColor", String.class);

	/**
	 * The initial Map center. Required.
	 */
	public static MixedDataKey<ILatLng> CENTER = new MixedDataKey<ILatLng>(OPTIONS_ID, "center", ILatLng.class);

	/**
	 * When false, map icons are not clickable. A map icon represents a point of interest, also known as a POI. By default map icons are clickable.
	 */
	public static MixedDataKey<Boolean> CLICKABLE_ICONS = new MixedDataKey<Boolean>(OPTIONS_ID, "clickableIcons", Boolean.class);

	/**
	 * Size in pixels of the controls appearing on the map. This value must be supplied directly when creating the Map, 
	 * updating this value later may bring the controls into an undefined state. 
	 * Only governs the controls made by the Maps API itself. 
	 * Does not scale developer created custom controls.
	 */
	public static MixedDataKey<Double> CONTROL_SIZE = new MixedDataKey<Double>(OPTIONS_ID, "controlSize", Double.class);

	/**
	 * Enables/disables all default UI. May be overridden individually.
	 */
	public static MixedDataKey<Boolean> DISABLE_DEFAULT_UI = new MixedDataKey<Boolean>(OPTIONS_ID, "disableDefaultUI", Boolean.class);
	// Deliberately omitted: disableDoubleClickZoom
	// Deliberately omitted: draggable

	/**
	 * The name or url of the cursor to display when mousing over a draggable map. This property uses the css cursor attribute to change the icon. 
	 * As with the css property, you must specify at least one fallback cursor that is not a URL. 
	 * For example: draggableCursor: 'url(http://www.example.com/icon.png), auto;'.
	 */
	public static MixedDataKey<String> DRAGGABLE_CURSOR = new MixedDataKey<String>(OPTIONS_ID, "draggableCursor", String.class);

	/**
	 * The name or url of the cursor to display when the map is being dragged. This property uses the css cursor attribute to change the icon. 
	 * As with the css property, you must specify at least one fallback cursor that is not a URL. 
	 * For example: draggingCursor: 'url(http://www.example.com/icon.png), auto;'.
	 */
	public static MixedDataKey<String> DRAGGING_CURSOR = new MixedDataKey<String>(OPTIONS_ID, "draggingCursor", String.class);

	/**
	 * The enabled/disabled state of the Fullscreen control.
	 */
	public static MixedDataKey<Boolean> FULL_SCREEN_CONTROL = new MixedDataKey<Boolean>(OPTIONS_ID, "fullscreenControl", Boolean.class);
	//TODO fullscreenControlOptions

	/**
	 * This setting controls how the API handles gestures on the map. 
	 * Allowed values: 
	 * <ul><li>"cooperative": Scroll events and one-finger touch gestures scroll the page, and do not zoom or pan the map. 
	 * Two-finger touch gestures pan and zoom the map. Scroll events with a ctrl key or &#8984; key pressed zoom the map.
	 * In this mode the map cooperates with the page. </li>
	 * <li>"greedy": All touch gestures and scroll events pan or zoom the map. </li>
	 * <li>"none": The map cannot be panned or zoomed by user gestures. </li>
	 * <li>"auto": (default) Gesture handling is either cooperative or greedy, depending on whether the page is scrollable or in an iframe. </li>
	 * </ul>
	 */
	public static MixedDataKey<String> GESTURE_HANDLING = new MixedDataKey<String>(OPTIONS_ID, "gestureHandling", String.class);

	/**
	 * The heading for aerial imagery in degrees measured clockwise from cardinal direction North. 
	 * Headings are snapped to the nearest available angle for which imagery is available.
	 */
	public static MixedDataKey<Double> HEADING = new MixedDataKey<Double>(OPTIONS_ID, "heading", Double.class);

	/**
	 * If false, prevents the map from being controlled by the keyboard. Keyboard shortcuts are enabled by default.
	 */
	public static MixedDataKey<Boolean> KEYBOARD_SHORTCUTS = new MixedDataKey<Boolean>(OPTIONS_ID, "keyboardShortcuts", Boolean.class);

	/**
	 * The initial enabled/disabled state of the Map type control
	 */
	public static MixedDataKey<Boolean> MAP_TYPE_CONTROL = new MixedDataKey<Boolean>(OPTIONS_ID, "mapTypeControl", Boolean.class);
	//TODO mapTypeControlOptions

	/**
	 * The initial Map mapTypeId. Defaults to "roadmap".  Options are "hybrid", "roadmap", "satellite" and "terrain".
	 */
	public static MixedDataKey<MapTypeId> MAP_TYPE_ID = new MixedDataKey<MapTypeId>(OPTIONS_ID, "mapTypeId", MapTypeId.class); 
	
	/**
	 * The maximum zoom level which will be displayed on the map. If omitted, or set to null, the maximum zoom from the current map type is used instead. 
	 * Valid values: Integers between zero, and up to the supported maximum zoom level.
	 */
	public static MixedDataKey<Integer> MAX_ZOOM = new MixedDataKey<Integer>(OPTIONS_ID, "maxZoom", Integer.class);

	/**
	 * The minimum zoom level which will be displayed on the map. If omitted, or set to null, the minimum zoom from the current map type is used instead. 
	 * Valid values: Integers between zero, and up to the supported maximum zoom level.
	 */
	public static MixedDataKey<Integer> MIN_ZOOM = new MixedDataKey<Integer>(OPTIONS_ID, "minZoom", Integer.class);
	// Deliberately omitted: noClear
	// Deliberately omitted: panControl
	// Deliberately omitted: panControlOptions
	//TODO restriction

	/**
	 * The enabled/disabled state of the Rotate control.
	 */
	public static MixedDataKey<Boolean> ROTATE_CONTROL = new MixedDataKey<Boolean>(OPTIONS_ID, "rotateControl", Boolean.class);
	//TODO rotateControlOptions

	/**
	 * The initial enabled/disabled state of the Scale control.
	 */
	public static MixedDataKey<Boolean> SCALE_CONTROL = new MixedDataKey<Boolean>(OPTIONS_ID, "scaleControl", Boolean.class);
	//TODO scaleControlOptions
	// Deliberately omitted: scrollWheel
	//TODO streetView

	/**
	 * The initial enabled/disabled state of the Street View Pegman control. 
	 * This control is part of the default UI, and should be set to false when displaying a map type on which the Street View road overlay 
	 * should not appear (e.g. a non-Earth map type).
	 */
	public static MixedDataKey<Boolean> STREET_VIEW_CONTROL = new MixedDataKey<Boolean>(OPTIONS_ID, "streetViewControl", Boolean.class);
	//TODO streetViewControlOptions
	//TODO styles

	/**
	 * Controls the automatic switching behavior for the angle of incidence of the map. 
	 * The only allowed values are 0 and 45. The value 0 causes the map to always use a 0 degree overhead view regardless of the zoom level and viewport. 
	 * The value 45 causes the tilt angle to automatically switch to 45 whenever 45 degree imagery is available for the current zoom level and viewport, 
	 * and switch back to 0 whenever 45 degree imagery is not available (this is the default behavior). 45 degree imagery is only available for 
	 * satellite and hybrid map types, within some locations, and at some zoom levels. Note: getTilt returns the current tilt angle, 
	 * not the value specified by this option. Because getTilt and this option refer to different things, do not bind() the tilt property; 
	 * doing so may yield unpredictable effects.
	 */
	public static MixedDataKey<Double> TILT = new MixedDataKey<Double>(OPTIONS_ID, "tilt", Double.class);

	/**
	 * The initial Map zoom level. Required. Valid values: Integers between zero, and up to the supported maximum zoom level.
	 */
	public static MixedDataKey<Integer> ZOOM = new MixedDataKey<Integer>(OPTIONS_ID, "zoom", Integer.class);
	
	/**
	 * The enabled/disabled state of the Zoom control.
	 */
	public static MixedDataKey<Boolean> ZOOM_CONTROL = new MixedDataKey<Boolean>(OPTIONS_ID, "zoomControl", Boolean.class);
	//TODO zoomControlOptions
	
	/**
	 * Make a dictionary of the default options for the map.
	 * @return  A dictionary with any default options 
	 */
	public static IMixedDataDictionary makeDefault() {
		IPlace defaultPlace = IPlace_Samples.SAMPLE_PLACES_DICT.get("WILLY");
		
		IMixedDataDictionary result = IOptionsKeys.makeDefault();

		result.put(CENTER, defaultPlace.getLatLng());
		result.put(ZOOM, defaultPlace.getZoom());
		result.put(FULL_SCREEN_CONTROL, false);
		result.put(ROTATE_CONTROL, true);
		result.put(MAP_TYPE_ID, MapTypeId.HYBRID);
		
		// TODO override put() to check for valid option -- do this in superclass?
		
		return result;

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
		
		return new AOptionsFiller<IMapOptions>(cefUtils, IMapOptions.class) {
			{
				put(IOptionsFiller.makeMapKey(OPTIONS_ID), IProcessOption.makeNoOp()); // Override default behavior b/c Maps have no map option
				
				put(CENTER, (key, value, jsonWriter)->{
						jsonWriter.name(key.getDesc());
						jsonWriter.jsonValue(toJson(value, ILatLng.class));
					
				});
				
				put(MAP_TYPE_ID, (key, value, jsonWriter)->{
						jsonWriter.name(key.getDesc());
						jsonWriter.jsonValue(toJson(value.getName()));
				});
			}
		};
	}
}
