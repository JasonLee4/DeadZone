package provided.owlMaps.general;

import java.util.UUID;

import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefObject;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.IProcessOption;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;





/**
 * Options for a Google Maps IconSequence object.  
 * There is no OwlMaps IconSequence class as all that is needed is the options dictionary.
 * @author swong
 *
 */
public interface IIconSequenceOptions extends IOptionsKeys {

	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("d9b8afe1-a146-4afd-87aa-80e210922e3b");
	
	/**
	 * If true, each icon in the sequence has the same fixed rotation regardless of the angle of the edge on which it lies. 
	 * Defaults to false, in which case each icon in the sequence is rotated to align with its edge.
	 */
	public static MixedDataKey<Boolean> FIXED_ROTATION = new MixedDataKey<Boolean>(OPTIONS_ID, "fixedRotation", Boolean.class);
	
	/**
	 * The icon to render on the line, as represented by an options dictionary of ISymbolOptions 
	 */
	public static MixedDataKey<IMixedDataDictionary> ICON = new MixedDataKey<IMixedDataDictionary>(OPTIONS_ID, "icon", IMixedDataDictionary.class);
	
	/**
	 * The distance from the start of the line at which an icon is to be rendered. 
	 * This distance may be expressed as a percentage of line's length (e.g. '50%') or in pixels (e.g. '50px'). Defaults to '100%'.
	 */
	public static MixedDataKey<String> OFFSET = new MixedDataKey<String>(OPTIONS_ID, "offset", String.class);
	
	/**
	 * The distance between consecutive icons on the line. 
	 * This distance may be expressed as a percentage of the line's length (e.g. '50%') or in pixels (e.g. '50px'). 
	 * To disable repeating of the icon, specify '0'. Defaults to '0'.
	 */
	public static MixedDataKey<String> REPEAT = new MixedDataKey<String>(OPTIONS_ID, "repeat", String.class);

	
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
	 * @param cefUtils   The IJsUtils created by the system
	 * @return An IOptionsFiller for this component's options.
	 */
	public static IOptionsFiller makeOptionsFiller(ICefUtils cefUtils) {
		
		return new AOptionsFiller<IIconSequenceOptions>(cefUtils, IIconSequenceOptions.class) {
			{
				put(ICON, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, ISymbol.class));
				});

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
