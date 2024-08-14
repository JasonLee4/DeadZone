package provided.owlMaps.demo.model;

import java.util.function.Supplier;

import javax.swing.JComponent;

import provided.owlMaps.general.ILatLng;

/**
 * The adapter from the demo model to its view
 * @author swong
 *
 */
public interface IModel2ViewAdapter {

	/**
	 * Add the map component to the view
	 * @param mapCompFac  The map component to add
	 * @param label A label to associate with the generated component
	 */
	void addMapComp(Supplier<JComponent> mapCompFac, String label);

	/**
	 * Display the given latitude and longitude on the view
	 * @param latLng The latitude and longitude to display
	 */
	void setLatLng(ILatLng latLng);
	
	/**
	 * Display the given text on the view
	 * @param text The text to display
	 */
	void displayText(String text);

}
