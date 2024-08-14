package provided.owlMaps.demo.view;

/**
 * The adapter from the demo view to its model
 * @author swong
 * @param <TPlacesDropListItem>  The type of item held by the view's combobox of places
 *
 */
public interface IView2ModelAdapter<TPlacesDropListItem> {

	/**
	 * Quit the demo application
	 */
	void exit();

	/**
	 * Move the map to the given latitude and longitude
	 * @param latStr The desired latitude as a string 
	 * @param lngStr The desired longitude as a string 
	 */
	void goToLatLng(String latStr, String lngStr);

	/**
	 * Make an info window at the the given latitude and longitude 
	 * @param latStr The desired latitude as a string 
	 * @param lngStr The desired longitude as a string 
	 */	
	void makeInfoWin(String latStr, String lngStr);

	/**
	 * Make a polygon at the the given latitude and longitude 
	 * @param latStr The desired latitude as a string 
	 * @param lngStr The desired longitude as a string 
	 */	
	void makePolygon(String latStr, String lngStr);

	/**
	 * Make a polyline at the the given latitude and longitude 
	 * @param latStr The desired latitude as a string 
	 * @param lngStr The desired longitude as a string 
	 */	
	void makePolyline(String latStr, String lngStr);

	/**
	 * Make a rectangle at the the given latitude and longitude 
	 * @param latStr The desired latitude as a string 
	 * @param lngStr The desired longitude as a string 
	 */	
	void makeRectangle(String latStr, String lngStr);

	/**
	 * Make a circle at the the given latitude and longitude 
	 * @param latStr The desired latitude as a string 
	 * @param lngStr The desired longitude as a string 
	 */	
	void makeCircle(String latStr, String lngStr);

	/**
	 * Test method -- not fully implemented yet.
	 */
	void resetMap();

	/**
	 * Make a ground overlay at the the given latitude and longitude 
	 * @param latStr The desired latitude as a string 
	 * @param lngStr The desired longitude as a string 
	 */
	void makeOverlay(String latStr, String lngStr);

	/**
	 * Load GeoJSON data from a URL
	 */
	void loadGeoJson();

	/**
	 * Load GeoJSON data from a String
	 */
	void addGeoJson();

	/**
	 * Go to the given place selected from the places combobox.
	 * @param place  The place to go to
	 */
	void goToPlace(TPlacesDropListItem place);

}
