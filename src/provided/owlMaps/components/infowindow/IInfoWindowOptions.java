package provided.owlMaps.components.infowindow;

import java.awt.geom.Dimension2D;
import java.util.UUID;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.IProcessOption;
import provided.owlMaps.cefUtils.impl.AOptionsFiller;
import provided.owlMaps.general.IAnchor;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IOptionsKeys;



/**
 * Options for creating info windows.
 * @author swong
 *
 */
public interface IInfoWindowOptions extends IOptionsKeys {
	
	/**
	 * Unique identifier for this class of options.  
	 * Always refer to this field by name and not by its value to ensure compatibility when the codebase is updated
	 * as the value of the field may change!
	 */
	public static UUID OPTIONS_ID = UUID.fromString("4ec56bad-760f-4470-b65d-58e07c5df829");
	
	/**
	 * The entity used that anchors this info window.
	 * Not part of the Google Maps Javascript API!
	 */
	public static MixedDataKey<IAnchor> ANCHOR = new MixedDataKey<IAnchor>(OPTIONS_ID, "anchor", IAnchor.class); 
	
	/**
	 * Content to display in the InfoWindow. This can be an HTML element, a plain-text string, or a string containing HTML. 
	 * The InfoWindow will be sized according to the content. To set an explicit size for the content, 
	 * set content to be a HTML element with that size
	 */
	public static MixedDataKey<String> CONTENT = new MixedDataKey<String>(OPTIONS_ID, "content", String.class);
	
	/**
	 * Disable auto-pan on open. By default, the info window will pan the map so that it is fully visible when it opens.
	 */
	public static MixedDataKey<Boolean> DISABLE_AUTOPAN = new MixedDataKey<Boolean>(OPTIONS_ID, "disableAutoPan", Boolean.class); // defaults to false
	
	/**
	 * Maximum width of the infowindow, regardless of content's width. This value is only considered if it is set before a call to open. 
	 * To change the maximum width when changing content, call close, setOptions, and then open.
	 */
	public static MixedDataKey<Integer> MAX_WIDTH = new MixedDataKey<Integer>(OPTIONS_ID, "maxWidth", Integer.class); // defaults to false
	
	/**
	 * The offset, in pixels, of the tip of the info window from the point on the map at whose geographical coordinates the info window is anchored. 
	 * If an InfoWindow is opened with an anchor, the pixelOffset will be calculated from the anchor's anchorPoint property.
	 */
	public static MixedDataKey<Dimension2D> PIXEL_OFFSET = new MixedDataKey<Dimension2D>(OPTIONS_ID, "pixelOffset", Dimension2D.class);

	/**
	 * The LatLng at which to display this InfoWindow. If the InfoWindow is opened with an anchor, the anchor's position will be used instead.
	 */
	public static MixedDataKey<ILatLng> POSITION = new MixedDataKey<ILatLng>(OPTIONS_ID, "position", ILatLng.class); 
	
	/**
	 * All InfoWindows are displayed on the map in order of their zIndex, with higher values displaying in front of InfoWindows with lower values. 
	 * By default, InfoWindows are displayed according to their latitude, with InfoWindows of lower latitudes appearing in front of InfoWindows 
	 * at higher latitudes. InfoWindows are always displayed in front of markers.
	 */
	public static MixedDataKey<Double> ZINDEX = new MixedDataKey<Double>(OPTIONS_ID, "zIndex", Double.class);

	/**
	 * If true, the component will be visible upon creation.  Defaults to false.
	 * Description of VISIBLE key is "map" b/c/ visibility tied to connection to map
	 * Not part of the Google Maps Javascript API!
	 */
	public static MixedDataKey<Boolean> VISIBLE = new MixedDataKey<Boolean>(OPTIONS_ID, "visible", Boolean.class);  

	
	/**
	 * Make a dictionary of the default options for the component.
	 * @return  A dictionary with any default options 
	 */
	public static IMixedDataDictionary makeDefault() {
		IMixedDataDictionary result = IOptionsKeys.makeDefault();
		result.put(VISIBLE, false);
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
		
		
		return new AOptionsFiller<IInfoWindowOptions>(cefUtils, IInfoWindowOptions.class) {
			{
//				put(ANCHOR,  (key, value, jsonWriter)->{
//					jsonWriter.name(key.getDesc());
//					jsonWriter.jsonValue(toJson(value, ICefObjRef.class));
//				});  
				
				put(ANCHOR, IProcessOption.makeNoOp());  // ignore the ANCHOR key
				put(PIXEL_OFFSET, (key, value, jsonWriter)->{
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, Dimension2D.class));
				});	
				put(POSITION, (key, value, jsonWriter)->{
//					System.out.println("POSITION: "+ key.getDesc()+" = "+value);
					jsonWriter.name(key.getDesc());
					jsonWriter.jsonValue(toJson(value, ILatLng.class));
				});
				put(VISIBLE, IProcessOption.makeNoOp());  // ignore the VISIBLE key
			
//				put(VISIBLE, (key, value, jsonWriter)->{
//					if(value.equals(true)) {
//						jsonWriter.name(key.getDesc());  // Description of VISIBLE key is "map" b/c/ visibility tied to connection to map
//						jsonWriter.jsonValue(toJson(cefUtils.getCefMapObj(), ICefObjRef.class));						
//					}
//
//				});  
			}
		};
	}
}


