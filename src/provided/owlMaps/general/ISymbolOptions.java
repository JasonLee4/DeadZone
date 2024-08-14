package provided.owlMaps.general;

import java.awt.geom.Point2D;
import java.util.UUID;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefObject;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.IProcessOption;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;




/**
 * Options for a Google Maps Symbol object.   
 * There is no corresponding OwlMaps symbol class as the options dictionary is all that is needed to define a symbol..
 * @author swong
 *
 */
public interface ISymbolOptions extends IOptionsKeys {

	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("0a7ff02a-a114-4ca4-b9ee-894749dcdcc9");
	
	/**
	 * The position of the symbol relative to the marker or polyline. 
	 * The coordinates of the symbol's path are translated left and up by the anchor's x and y coordinates respectively. 
	 * By default, a symbol is anchored at (0, 0). The position is expressed in the same coordinate system as the symbol's path.
	 */
	public static MixedDataKey<Point2D> ANCHOR = new MixedDataKey<Point2D>(OPTIONS_ID, "point", Point2D.class);
	
	/**
	 * The symbol's fill color. All CSS3 colors are supported except for extended named colors.
	 *  For symbol markers, this defaults to 'black'. 
	 *  For symbols on polylines, this defaults to the stroke color of the corresponding polyline.
	 */
	public static MixedDataKey<String> FILL_COLOR = new MixedDataKey<String>(OPTIONS_ID, "fillColor", String.class);  // standard (not extended) CSS3 color name
	
	/**
	 * The symbol's fill opacity. 
	 * Defaults to 0.
	 */
	public static MixedDataKey<Double> FILL_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "fillOpacity", Double.class);  // [0.0, 1.0]
	
	/**
	 * The origin of the label relative to the origin of the path, if label is supplied by the marker. 
	 * By default, the origin is located at (0, 0). 
	 * The origin is expressed in the same coordinate system as the symbol's path. 
	 * This property is unused for symbols on polylines.
	 */
	public static MixedDataKey<Point2D> LABEL_ORIGIN = new MixedDataKey<Point2D>(OPTIONS_ID, "labelOrigin", Point2D.class);
	
	/**
	 * The symbol's path, which is a built-in symbol path, or 
	 * a custom path expressed using <a href="http://www.w3.org/TR/SVG/paths.html#PathData" target="_blank">SVG path notation</a>. 
	 * Required.
	 */
	public static MixedDataKey<ISymbolPath> PATH = new MixedDataKey<ISymbolPath>(OPTIONS_ID, "path", ISymbolPath.class); 
	
	/**
	 * The angle by which to rotate the symbol, expressed clockwise in degrees. Defaults to 0. 
	 * A symbol in an IconSequence where fixedRotation is false is rotated relative to the angle of the edge on which it lies.
	 */
	public static MixedDataKey<Double> ROTATION = new MixedDataKey<Double>(OPTIONS_ID, "rotation", Double.class);
	
	/**
	 * The amount by which the symbol is scaled in size. 
	 * For symbol markers, this defaults to 1; after scaling, the symbol may be of any size. 
	 * For symbols on a polyline, this defaults to the stroke weight of the polyline; after scaling, 
	 * the symbol must lie inside a square 22 pixels in size centered at the symbol's anchor.
	 */
	public static MixedDataKey<Double> SCALE = new MixedDataKey<Double>(OPTIONS_ID, "scale", Double.class);
	
	/**
	 * The symbol's stroke color. All CSS3 colors are supported except for extended named colors. 
	 * For symbol markers, this defaults to 'black'. 
	 * For symbols on a polyline, this defaults to the stroke color of the polyline
	 */
	public static MixedDataKey<String> STROKE_COLOR = new MixedDataKey<String>(OPTIONS_ID, "strokeColor", String.class);  // standard (not extended) CSS3 color name or number as "#XXXXXX" string, defaults to "Black"

	/**
	 * The symbol's stroke opacity. For symbol markers, this defaults to 1. 
	 * For symbols on a polyline, this defaults to the stroke opacity of the polyline.
	 */
	public static MixedDataKey<Double> STROKE_OPACITY = new MixedDataKey<Double>(OPTIONS_ID, "strokeOpacity", Double.class);	// [0.0, 1.0]  
	
	/**
	 * The symbol's stroke weight. Defaults to the scale of the symbol.
	 */
	public static MixedDataKey<Double> STROKE_WEIGHT = new MixedDataKey<Double>(OPTIONS_ID, "strokeWeight", Double.class); 

	
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
		
		return new AOptionsFiller<ISymbolOptions>(cefUtils, ISymbolOptions.class) {
			{
				put(ANCHOR, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, Point2D.class));
				});
				put(LABEL_ORIGIN, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, Point2D.class));
				});
				put(PATH, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, ISymbolPath.class));
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
