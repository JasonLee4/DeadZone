package provided.owlMaps.components.marker;

import java.util.UUID;

import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.IOptionsKeys;

/**
 * The options to specify a Google Maps MarkerLabel value.  
 * There is no associated marker label class, all that is needed to specify 
 * a marker label is a dictionary filled with IMarkerLabelOption key-values
 * @author swong
 *
 */
public interface IMarkerLabelOptions extends IOptionsKeys {
	
	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("7592e7b9-e433-4bd7-9d3f-6489335c4f44");

	/**
	 * The color of the label text. Default color is black.
	 */
	public static MixedDataKey<String> COLOR = new MixedDataKey<String>(OPTIONS_ID, "color", String.class); 

	/**
	 * The font family of the label text (equivalent to the CSS font-family property).
	 */
	public static MixedDataKey<String> FONT_FAMILY = new MixedDataKey<String>(OPTIONS_ID, "fontFamily", String.class); 

	/**
	 * The font size of the label text (equivalent to the CSS font-size property). Default size is 14px.
	 */
	public static MixedDataKey<String> FONT_SIZE = new MixedDataKey<String>(OPTIONS_ID, "fontSize", String.class); 

	/**
	 * The font weight of the label text (equivalent to the CSS font-weight property).
	 */
	public static MixedDataKey<String> FONT_WEIGHT = new MixedDataKey<String>(OPTIONS_ID, "fontWeight", String.class); 

	/**
	 * The text to be displayed in the label.
	 */
	public static MixedDataKey<String> TEXT = new MixedDataKey<String>(OPTIONS_ID, "text", String.class); 
	
	
	/**
	 * Make a dictionary of the default options for the component.
	 * @return  A dictionary with any default options 
	 */
	public static IMixedDataDictionary makeDefault() {
		IMixedDataDictionary result = IOptionsKeys.makeDefault();
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
		
		return new AOptionsFiller<IMarkerLabelOptions>(cefUtils, IMarkerLabelOptions.class) {
			{
			}
		};
	}
}	


