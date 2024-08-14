package provided.owlMaps.general;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.UUID;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;




/**
 * Options for creating icons
 * @author swong
 *
 */
public interface IIconOptions extends IOptionsKeys {

	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("33ccfb5b-0105-4a3e-9d0b-fe25c2435fe7");
	
	/**
	 * The position at which to anchor an image in correspondence to the location of the marker on the map. 
	 * By default, the anchor is located along the center point of the bottom of the image.
	 */
	public static MixedDataKey<Point2D> ANCHOR = new MixedDataKey<Point2D>(OPTIONS_ID, "point", Point2D.class);
	
	/**
	 * The origin of the label relative to the top-left corner of the icon image, if a label is supplied by the marker. 
	 * By default, the origin is located in the center point of the image.
	 */
	public static MixedDataKey<Point2D> LABEL_ORIGIN = new MixedDataKey<Point2D>(OPTIONS_ID, "labelOrigin", Point2D.class);
	
	/**
	 * The position of the image within a sprite, if any. 
	 * By default, the origin is located at the top left corner of the image (0, 0).
	 */
	public static MixedDataKey<Point2D> ORIGIN = new MixedDataKey<Point2D>(OPTIONS_ID, "origin", Point2D.class);
	
	/**
	 * The size of the entire image after scaling, if any. 
	 * Use this property to stretch/shrink an image or a sprite.
	 */
	public static MixedDataKey<Dimension2D> SCALED_SIZE = new MixedDataKey<Dimension2D>(OPTIONS_ID, "scaledSize", Dimension2D.class);
	
	/**
	 * The display size of the sprite or image. When using sprites, you must specify the sprite size. 
	 * If the size is not provided, it will be set when the image loads.
	 */
	public static MixedDataKey<Dimension2D> SIZE = new MixedDataKey<Dimension2D>(OPTIONS_ID, "size", Dimension2D.class);
	
	/**
	 * The URL of the image or sprite sheet.
	 */
	public static MixedDataKey<String> URL = new MixedDataKey<String>(OPTIONS_ID, "url", String.class);

	
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
		
		return new AOptionsFiller<IIconOptions>(cefUtils, IIconOptions.class) {
			{
				put(ANCHOR, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, Point2D.class));
				});
				put(LABEL_ORIGIN, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, Point2D.class));
				});
				put(ORIGIN, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, Point2D.class));;
				});
				put(SCALED_SIZE, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, Dimension2D.class));
				});		
				put(SIZE, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, Dimension2D.class));
				});		
			}
		};
	}			
			
}
