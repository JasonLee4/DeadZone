package provided.owlMaps.map;

import java.util.Map;
import java.util.function.Consumer;
import provided.mixedData.IMixedDataDictionary;
import provided.owlMaps.cefUtils.ICefObjRef;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IPlace;
import provided.owlMaps.map.data.IMapDataFeature;
import provided.owlMaps.mouse.IMouseEvent;
import provided.owlMaps.mouse.MapMouseDragEventType;
import provided.owlMaps.mouse.MapMouseEventType;

/**
 * Represents a displayed map.
 * Corresponds to Google Map Javascript API google.maps.Map 
 * @author swong
 *
 */
public interface IOwlMap extends ICefObjRef {

	/**
	 * Get the ILatLng of the center of the map 
	 * @return An ILatLng object
	 */
	public ILatLng getCenter();

	/**
	 * Set the ILatLng of the center of the map 
	 * @param latLng An ILatLng object
	 */
	public void setCenter(ILatLng latLng);

	/**
	 * Get the type of Map being displayed.
	 * See MapTypeId for valid values
	 * @return The map type
	 */
	public MapTypeId getMapTypeId();

	/**
	 * Sets the displayed map type.  
	 * See MapTypeId for the valid values
	 * @param mapTypeID The type of map to display
	 */
	public void setMapTypeId(MapTypeId mapTypeID);

	/**
	 * Get the current zoom level
	 * @return The current zoom level
	 */
	public int getZoom();

	/**
	 * Set the current zoom level
	 * @param zoom The new zoom level
	 */
	public void setZoom(int zoom);

	/**
	 * Pan the center of the map to the location and set the zoom level to that specified by the given IPlace
	 * @param place The IPlace to move the map to.
	 */
	public void goTo(IPlace place);

	/**
	 * Pan the map to the given location.  The zoom level is unchanged.
	 * @param latLng The new location
	 */
	public void panTo(ILatLng latLng);

	/**
	 * Set the map to the given IMapOptions options in the given dictionary
	 * @param options A dictionary containing IMapOptions option key-values.
	 * @throws ClassNotFoundException If a aupplied IMapOptions option is unknown
	 */
	public void setOptions(IMixedDataDictionary options) throws ClassNotFoundException;

	/**
	 * Set the given MapEventType event to run the given event handler function
	 * @param eventType The desired MapEventType
	 * @param eventFn The handler function to run when the given event type occurs.
	 */
	public void setMapEvent(MapEventType eventType, Runnable eventFn);

	/**
	 * Set the given MapMouseEventType event to invoke the given event handler function
	 * @param eventType The desired event type
	 * @param eventFn The event handler function to invoke when the event occurs.
	 */
	public void setMapMouseEvent(MapMouseEventType eventType, Consumer<IMouseEvent> eventFn);

	/**
	 * Set the given MapMouseDragEventType event to invoke the given event handler function
	 * @param eventType The desired event type
	 * @param eventFn The event handler function to invoke when the event occurs.
	 */
	public void setMapMouseDragEvent(MapMouseDragEventType eventType, Runnable eventFn);

	/**
	 * Load GeoJSON data into the map from the given URL.
	 * If  a feature is not defined in the GeoJSON data with an "id" field, the feature ID will be returned as "undefined_X" where X = [0, 1, ...]. 
	 * Since this operation can take a considerable amount of time to execute, a Consumer function needs to be 
	 * provided that will run asynchronously when the results are ready. The IMapDataFeatures created by the GeoJSON data, mapped to their respective IDs, is
	 * supplied to the callback function.
	 * WARNING: Do not manipulate the map or map components in the callback due to potential deadlocking problems 
	 * in the underlying Java-to-Javascript system. 
	 * @param url The url of a GeoJSON file 
	 * @param callback The function that is called once the GeoJSON file is loaded and ready. Accepts a mapping of feature IDs to their respective features.
	 */
	public void loadGeoJson(String url,  Consumer<Map<String, IMapDataFeature>> callback);

	/**
	 * Load GeoJSON data into the map from the given JSON string. 
	 * If  a feature is not defined in the GeoJSON data with an "id" field, the feature ID will be returned as "undefined_X" where X = [0, 1, ...]. 
	 * Since this operation can take a considerable amount of time to execute, a Consumer function needs to be 
	 * provided that will run asynchronously when the results are ready.   The IMapDataFeatures created by the GeoJSON data, mapped to their respective IDs, is
	 * supplied to the callback function.
	 * @param geoJsonStr A GeoJSON encoded JSON string
	 * @param callback The function that is called once the GeoJSON data is loaded and ready. Accepts a mapping of feature IDs to their respective features.
	 */
	public void addGeoJson(String geoJsonStr, Consumer<Map<String, IMapDataFeature>> callback);

	/**
	 * Set the default appearance styling for the maps displayed data
	 * @param mapDataStyleOptions  An IMixedDataDictionary containing IMapDataStyleOption key-values
	 */
	public void setDataStyle(IMixedDataDictionary mapDataStyleOptions);

	/**
	 * Override the styling of a feature with the given options
	 * @param featureId The desired feature 
	 * @param mapDataStyleOptions An IMixedDataDictionary containing IMapDataStyleOption key-values
	 */
	public void overrideDataStyleById(String featureId, IMixedDataDictionary mapDataStyleOptions);

	/**
	 * Revert the styling of a feature back to the default styling 
	 * @param featureId The ID of the desired feature
	 */
	public void revertDataStyleById(String featureId);

	/**
	 * Revert the styling of all features back to the default styling.
	 */
	public void revertDataStyleAll();
}
