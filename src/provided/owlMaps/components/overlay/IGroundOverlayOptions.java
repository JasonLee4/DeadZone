package provided.owlMaps.components.overlay;

import java.util.UUID;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.IProcessOption;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.ILatLngBounds;
import provided.owlMaps.general.IOptionsKeys;



/**
 * Options for making ground overlays.
 * Note: the opacity and clickable options must always be explicitly set because of 
 * a bug in the underlying Google API Javascript code that will leave those fields 
 * undefined if omitted.
 * @author swong
 *
 */
public interface IGroundOverlayOptions extends IOptionsKeys {
	
	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("79a881f8-be2f-43c5-a6b9-1cd694b7b501");

	/**
	 * The bounds of the overlay
	 */
	public static MixedDataKey<ILatLngBounds> BOUNDS = new MixedDataKey<ILatLngBounds>(OPTIONS_ID, "bounds", ILatLngBounds.class); // NOT part of the Google API!

	/**
	 * Indicates whether this Circle handles mouse events. Defaults to true.
	 */
	public static MixedDataKey<Boolean> CLICKABLE = new MixedDataKey<Boolean>(OPTIONS_ID, "clickable", Boolean.class); // defaults to true

	/**
	 * The opacity of the overlay between 0.0 and 1.0.   Default = 1.0
	 */
	public static MixedDataKey<Double> OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "opacity", Double.class); 

	/**
	 * The URL of the image to display
	 */
	public static MixedDataKey<String> URL = new MixedDataKey<String>(OPTIONS_ID, "url", String.class);  // NOT part of Google API! 		

	
	/**
	 * Whether this circle is visible on the map. Defaults to true.
	 */
	public static MixedDataKey<Boolean> VISIBLE = new MixedDataKey<Boolean>(OPTIONS_ID, "visible", Boolean.class);  // NOT part of the Google API!


	
	/**
	 * Make a dictionary of the default options for the component, 
	 * including explicitly setting the opacity and clickable options to 
	 * their default values.
	 * @return  A dictionary with any default options 
	 */
	public static IMixedDataDictionary makeDefault() {
		IMixedDataDictionary result = IOptionsKeys.makeDefault();
		result.put(VISIBLE, false);
		result.put(CLICKABLE, true);  // Must explicitly set this to avoid undefined error in Javascript
		result.put(OPACITY, 1.0);  // Must explicitly set this to avoid undefined error in Javascript
		return result;
	}


	

	/**
	 * *FOR INTERNAL USE ONLY!!* OWLMAPS  DEVELOPER CODE SHOULD *NOT* USE THIS METHOD! 
	 * Factory to create the IOptionsFiller that converts the component options into 
	 * their corresponding Javascript Google Map component options.
	 * 
	 * @param jsUtils   The IJsUtils created by the system
	 * @return An IOptionsFiller for this component's options.
	 */
	public static IOptionsFiller makeOptionsFiller(ICefUtils jsUtils) {
		
		
		return new AOptionsFiller<IGroundOverlayOptions>(jsUtils, IGroundOverlayOptions.class) {

			{
				// Don't process any non-Google API options!
				put(URL, IProcessOption.makeNoOp());				
				put(BOUNDS, IProcessOption.makeNoOp());
				
				// Visibility is controlled by the map field
				put(VISIBLE, (key, value, jsonWriter)->{
					if(!value) {
//						jsOptions.putProperty(IOptionsFiller.MAP_KEY, null);

						jsonWriter.name(IOptionsFiller.MAP_KEY);
						jsonWriter.jsonValue(toJson(null, String.class)); // Set map key to null if not visible.
					}
				});
			}
		};
	}
}


