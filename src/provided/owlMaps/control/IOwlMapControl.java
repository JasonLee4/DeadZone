package provided.owlMaps.control;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import provided.logger.ILogger;
import provided.logger.LogLevel;
import provided.mixedData.IMixedDataDictionary;
import provided.owlMaps.components.IMapComponentFactory;
import provided.owlMaps.control.impl.OwlMapControl;
import provided.owlMaps.map.IOwlMap;
import provided.owlMaps.utils.IOwlMapUtils;

/**
 * Main top-level OwlMaps entity that starts/stops the OwlMaps system and instantiates the map, map component factory, map utilities, etc.
 * 
 * ONLY instantiate an instance of this interface by using its static make() method!   
 * Do NOT attempt to directly instantiate any implementation of this interface! 
 * @author swong
 *
 */
public interface IOwlMapControl {

	/**
	 * Get the IOwlMapUtils configured for this system.
	 * @return an IOwlMapUtils instance
	 */
	public IOwlMapUtils getMapUtils();

//	/**
//	 * Make the map JComponent and start the mapping system.
//	 * THIS METHOD MUST BE RUN FROM THE GUI THREAD!!
//	 * 
//	 * @param mapOptions A dictionary of IMapOptions to use when creating the map
//	 * @param onMapLoad A Runnable to run when the map has finished loading.
//	 * @return A JComponent that will display the map.
//	 * @throws ClassNotFoundException If an unknown map option is encountered
//	 */
//	public JComponent start(IMixedDataDictionary mapOptions, Runnable onMapLoad) throws ClassNotFoundException;


	/**
	 * Get the map object
	 * 
	 * @return The map object
	 */
	public IOwlMap getMap();

	/**
	 * Stop the map engine. Must be called when app exits or engine will keep
	 * running.
	 */
	public void stop();

	/**
	 * Get the current IMapComponentFactory object 
	 * @return The current IMapComponentFactory object
	 */
	public IMapComponentFactory getMapComponentFactory();
	
	
	/**
	 * Enables the display of the browser dev console window
	 * @param makeDevTools If true, show the dev console window
	 */
	public void setMakeDevTools(boolean makeDevTools);


//	/**
//	 * Instantiate an IOwlMapControl instance
//	 * @param googleMapsApiKey  The Google Maps API key to use
//	 * @param logger The logger to use.  
//	 * @param systemLengthUnit The length unit to use by the system.
//	 * @return An instance of IOwlMapControl
//	 */
//	public static IOwlMapControl make(String googleMapsApiKey, MapLengthUnits systemLengthUnit, ILogger logger) { 
//		return new OwlMapControl(googleMapsApiKey, systemLengthUnit, logger);
//	}

	/**
	 * Instantiate a JComponent factory that will instantiate and start the IOwlMapsControl and then return a map component.
	 * 	 * THE RETURNED FACTORY *MUST* BE RUN ON THE GUI THREAD!!!
	 * This is a convenience method that uses MapLengthUnits.FEET as the map length units and does NOT display the Chromium browser dev console, 
	 * which is only needed for low-level debugging. 
	 * Typical developer code would use this method.
	 * @param gMapsApiKey The Google Maps API key to use
	 * @param mapOptionsDict  The options dictionary used to set the initial map options.
	 * @param onMapLoadFn A function that is run once the map has been created.  The given input parameter is the new IOwlMapControl object that still needs to be saved somewhere.  The map not available until this function runs!   This function is NOT run on the GUI thread!
	 * @param logger The logger to use for messages
	 * @return A factory that will will instantiate and start the IOwlMapsControl and then return a map component.   This factory MUST be run on the GUI thread!
	 */
	public static Supplier<JComponent> makeMapFactory(String gMapsApiKey, IMixedDataDictionary mapOptionsDict, Consumer<IOwlMapControl> onMapLoadFn, ILogger logger) {
		return makeMapFactory(gMapsApiKey, mapOptionsDict, onMapLoadFn, logger, MapLengthUnits.FEET, false);
	}
	
	/**
	 * Instantiate a JComponent factory that will instantiate and start the IOwlMapsControl and then return a map component.
	 * THE RETURNED FACTORY *MUST* BE RUN ON THE GUI THREAD!!!
	 * This method enables control over whether or not the Chromium browser dev console is shown, which is used only for low-level debugging.
	 * Typical developer code would NOT use this method.
	 * @param gMapsApiKey The Google Maps API key to use
	 * @param mapOptionsDict  The options dictionary used to set the initial map options.
	 * @param onMapLoadFn A function that is run once the map has been created.  The given input parameter is the new IOwlMapControl object that still needs to be saved somewhere.  The map not available until this function runs!   This function is NOT run on the GUI thread!
	 * @param logger The logger to use for messages
	 * @param mapLengthUnits The enum specifying the length units to use in the map
	 * @param makeDevTools  If true, the Chromium browser dev tools console will be shown.  This is only needed for low level debugging.
	 * @return A factory that will will instantiate and start the IOwlMapsControl and then return a map component.   This factory MUST be run on the GUI thread!
	 */
	public static Supplier<JComponent> makeMapFactory(String gMapsApiKey, IMixedDataDictionary mapOptionsDict, Consumer<IOwlMapControl> onMapLoadFn, ILogger logger, MapLengthUnits mapLengthUnits, boolean makeDevTools) {
		if(null==gMapsApiKey || gMapsApiKey.equals("")) {
			throw new IllegalArgumentException("[IOwlMapControl.makeMapFactory()] The supplied Google Maps API key String cannot be null or empty!");
		}
		
		return ()->{ 
			try {
				// The following is just a safety check to help developers detect if this factory is being run on the wrong thread. 
				if(SwingUtilities.isEventDispatchThread()) {
					logger.log(LogLevel.DEBUG, "[makeMapFactory().run()] Running on the the GUI thread as it should.");
				}
				else {
					String errMsg = "[makeMapFactory().run()] The OwlMap component factory is NOT running on the GUI thread!";
					logger.log(LogLevel.ERROR, errMsg);
					throw new IllegalStateException(errMsg);
				}
				
				OwlMapControl owlMapControl =  new OwlMapControl(
						gMapsApiKey, 
						mapLengthUnits,
						logger
//						(msg) -> {
//							logger.log(LogLevel.INFO, "[makeMapFactory().run()] "+msg);
//						}
					);
				
				owlMapControl.setMakeDevTools(makeDevTools);   // set makeDevTools to true to see the Chromium browser dev tools console

				
				return owlMapControl.start(mapOptionsDict, ()->{
					logger.log(LogLevel.INFO, "[makeMapFactory().run()] Calling onMapLoadFn...");
					onMapLoadFn.accept(owlMapControl);
				});
			}
			catch(Exception e) {
				String errorMsg = "[makeMapFactory().run()] Exception while creating the map: "+e;
				logger.log(LogLevel.ERROR, errorMsg);
				e.printStackTrace();
				return new JLabel(errorMsg); // Return a displayable label with the error message so the user knows something went wrong.				
			}
		};
	}

}