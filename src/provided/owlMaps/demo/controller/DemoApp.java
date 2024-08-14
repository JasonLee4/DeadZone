package provided.owlMaps.demo.controller;

import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import provided.config.impl.AppConfigChooser;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.owlMaps.demo.AppConfigGMaps;
import provided.owlMaps.demo.model.DemoModel;
import provided.owlMaps.demo.model.IModel2ViewAdapter;
import provided.owlMaps.demo.view.DemoFrame;
import provided.owlMaps.demo.view.IView2ModelAdapter;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.IPlace;
import provided.owlMaps.utils.IPlace_Samples;


/**
 * Controller to build the OwlMaps demo application
 * @author swong
 *
 */
public class DemoApp {
	
	/**
	 * System-wide logger to use
	 */
	ILogger sysLogger = ILoggerControl.getSharedLogger();
	
	/**
	 * The demo's model
	 */
	private DemoModel model;
	
	/**
	 * The demo's view
	 */
	private DemoFrame<IPlace> view;
	
	private AppConfigChooser<AppConfigGMaps> appConfigChooser= new AppConfigChooser<>(
		new AppConfigGMaps("No Dev", "AIzaSyBI7z-Q6Sj8mteRVfeLzvNfTDXY6LD8NGs" , false),
		new AppConfigGMaps("With Dev", "AIzaSyBI7z-Q6Sj8mteRVfeLzvNfTDXY6LD8NGs" , true)
	);
	
	AppConfigGMaps currentConfig;
	
	/**
	 * Constructs that controller
	 * @param googleMapsAPIKey  The Google Maps API key to use 
	 */
	public DemoApp(String googleMapsAPIKey) {
		currentConfig = appConfigChooser.choose();
		if("" != googleMapsAPIKey) {
			currentConfig.apiKey = googleMapsAPIKey; // Override the selected config's API key
		}
		sysLogger.log(LogLevel.INFO, "Current Configuration: " + currentConfig.name+" = API key: "+currentConfig.apiKey+", show Chromium Dev console: "+currentConfig.useDev);
		model = new DemoModel(sysLogger, new IModel2ViewAdapter() {

			@Override
			public void addMapComp(Supplier<JComponent> mapCompFac, String label) {
				view.addComponentToCenter(mapCompFac, label);
			}

			@Override
			public void setLatLng(ILatLng latLng) {
				view.setLatLng(latLng.getLat(), latLng.getLng());
				
			}

			@Override
			public void displayText(String text) {
				view.displayText(text);
			}
			
		}, currentConfig);
		
		view = new DemoFrame<IPlace>(new IView2ModelAdapter<IPlace>() {

			@Override
			public void exit() {
				model.stop();
				System.exit(0);
			}

			@Override
			public void goToLatLng(String latStr, String lngStr) {
				model.goToLatLng(ILatLng.make(Double.parseDouble(latStr), Double.parseDouble(lngStr)));
			}

			@Override
			public void makeInfoWin(String latStr, String lngStr) {
				model.makeInfoWin(ILatLng.make(Double.parseDouble(latStr), Double.parseDouble(lngStr)));
			}

			@Override
			public void makePolygon(String latStr, String lngStr) {
				model.makePolygon(ILatLng.make(Double.parseDouble(latStr), Double.parseDouble(lngStr)));
			}

			@Override
			public void makePolyline(String latStr, String lngStr) {
				model.makePolyline(ILatLng.make(Double.parseDouble(latStr), Double.parseDouble(lngStr)));
			}

			@Override
			public void makeRectangle(String latStr, String lngStr) {
				model.makeRectangle(ILatLng.make(Double.parseDouble(latStr), Double.parseDouble(lngStr)));	
			}

			@Override
			public void makeCircle(String latStr, String lngStr) {
				model.makeCircle(ILatLng.make(Double.parseDouble(latStr), Double.parseDouble(lngStr)));	
			}

			
			@Override
			public void makeOverlay(String latStr, String lngStr) {
				model.makeOverlay(ILatLng.make(Double.parseDouble(latStr), Double.parseDouble(lngStr)));	
			}
			
			
			@Override
			public void resetMap() {
				model.resetMap();
			}

			@Override
			public void loadGeoJson() {
				model.loadGeoJson();
				
			}

			@Override
			public void addGeoJson() {
				model.addGeoJson();
			}

			@Override
			public void goToPlace(IPlace place) {
				model.goToPlace(place);
				
			}
			
		});
	}
	
	/**
	 * Start the system
	 */
	public void start() {
		model.start();
		view.start();
		// Load the places drop list with the sample places.  
		// Note that the IPlace_Samples.getSamplePlacesDict() returns a mutable copy of the immutable IPlace_Samples.SAMPLE_PLACES_DICT mapping.
		IPlace_Samples.getSamplePlacesDict().values().forEach((place)->{
			view.addPlace(place);
		});
	}
	
	/**
	 * The main method to run the app.
	 * @param args  args[0] = Google Maps API key
	 */
	public static void main(String[] args) {
		System.out.println("[provided.owlMaps.demo.controller.DemoApp.main()] Java version: "+System.getProperty("java.version"));
		SwingUtilities.invokeLater(()->{
			(new DemoApp( 0 < args.length ? args[0]: "")).start();
		});
	}

}
