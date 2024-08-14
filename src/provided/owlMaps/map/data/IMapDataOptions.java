package provided.owlMaps.map.data;

import java.util.UUID;

import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.IOptionsKeys;


/**
 * Options that can be set of a map's data.
 * @author swong
 *
 */
public interface IMapDataOptions extends IOptionsKeys {
	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("090732c0-501e-40de-ac54-2fb4c1ef3714");

	
// TODO
//	/**
//	 * The position of the drawing controls on the map. The default position is TOP_LEFT.
//	 */
//	public static MixedDataKey<ControlPosition> CONTROL_POSITION = new MixedDataKey<ControlPosition>(OPTIONS_ID, "controlPosition", ControlPosition.class);// is an xy point not latlng
	
// TODO
//	/**
//	 * Describes which drawing modes are available for the user to select, in the order they are displayed. This should not include the null drawing mode, which is added by default. If null, drawing controls are disabled and not displayed. Defaults to null. Possible drawing modes are "Point", "LineString" or "Polygon".
//	drawingMode
//	 */
//	public static MixedDataKey<List<DrawingMode>> CONTROLS = new MixedDataKey<DrawingMode>(OPTIONS_ID, "controls", List<DrawingMode>.class);  
	
// TODO
//		/**
//		 * The current drawing mode of the given Data layer. A drawing mode of null means that the user can interact with the map as normal, and clicks do not draw anything. Defaults to null. Possible drawing modes are null, "Point", "LineString" or "Polygon".
//		 */
//		public static MixedDataKey<DrawingMode> DRAWING_MODE = new MixedDataKey<DrawingMode>(OPTIONS_ID, "drawingMode", DrawingMode.class);  


// TODO
//	/**
//	 * The current drawing mode of the given Data layer. A drawing mode of null means that the user can interact with the map as normal, and clicks do not draw anything. Defaults to null. Possible drawing modes are null, "Point", "LineString" or "Polygon".
//	 */
//	public static MixedDataKey<IFeatureFactory> FEATURE_FACTORY = new MixedDataKey<IFeatureFactory>(OPTIONS_ID, "featureFactory", IFeatureFactory.class);  


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
		return result;

		// TODO override put() to check for valid option -- do this in superclass?

	}
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Factory to create the IOptionsFiller that converts the component options into 
	 * their corresponding Javascript Google Map component options.
	 * 
	 * @param cefUtils   The ICefUtils created by the system
	 * @return An IOptionsFiller for this component's options.
	 */
	public static IOptionsFiller makeOptionsFiller(ICefUtils cefUtils) {
		
		return new AOptionsFiller<IMapDataOptions>(cefUtils, IMapDataOptions.class) {

			{
				put(STYLE, (key, value, jsonWriter)->{			
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson( value, IMapDataStyleOptions.class));
				});				
				
			}
		};
	}
}


