package provided.owlMaps.demo.model;


import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JComponent;
import provided.logger.ILogEntry;
import provided.logger.ILogEntryFormatter;
import provided.logger.ILogEntryProcessor;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.mixedData.IMixedDataDictionary;
import provided.owlMaps.components.infowindow.IInfoWindow;
import provided.owlMaps.components.infowindow.IInfoWindowOptions;
import provided.owlMaps.components.infowindow.InfoWindowEventType;
import provided.owlMaps.components.marker.IMarker;
import provided.owlMaps.components.marker.IMarkerLabel;
import provided.owlMaps.components.marker.IMarkerOptions;
import provided.owlMaps.components.marker.MarkerEventType;
import provided.owlMaps.components.overlay.GroundOverlayMouseEventType;
import provided.owlMaps.components.overlay.IGroundOverlay;
import provided.owlMaps.components.overlay.IGroundOverlayOptions;
import provided.owlMaps.components.shapes.ICircle;
import provided.owlMaps.components.shapes.ICircleOptions;
import provided.owlMaps.components.shapes.IPolygon;
import provided.owlMaps.components.shapes.IPolygonOptions;
import provided.owlMaps.components.shapes.IPolyline;
import provided.owlMaps.components.shapes.IPolylineOptions;
import provided.owlMaps.components.shapes.IRectangle;
import provided.owlMaps.components.shapes.IRectangleOptions;
import provided.owlMaps.control.IOwlMapControl;
import provided.owlMaps.control.MapLengthUnits;
import provided.owlMaps.demo.AppConfigGMaps;
import provided.owlMaps.general.IIconSequenceOptions;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.ILatLngBounds;
import provided.owlMaps.general.IPath;
import provided.owlMaps.general.IPathList;
import provided.owlMaps.general.IPaths;
import provided.owlMaps.general.IPlace;
import provided.owlMaps.general.ISymbolOptions;
import provided.owlMaps.general.SymbolPathConstants;
import provided.owlMaps.map.IMapOptions;
import provided.owlMaps.map.IOwlMap;
import provided.owlMaps.map.data.IMapDataStyleOptions;
import provided.owlMaps.mouse.ComponentMouseEventType;
import provided.owlMaps.mouse.MapMouseEventType;
import provided.owlMaps.utils.IOwlMapUtils;



/**
 * The model for the OwlMaps demo app
 * @author swong
 *
 */
public class DemoModel {
	
	/**
	 * The adapter to the view 
	 */
	private IModel2ViewAdapter model2ViewAdpt;
	
	/**
	 * Logger that outputs to the view
	 */
	private ILogger viewLogger = ILoggerControl.makeLogger(new ILogEntryProcessor() {

		ILogEntryFormatter formatter = ILogEntryFormatter.MakeFormatter("[%1$s] %2$s");
		@Override
		public void accept(ILogEntry logEntry) {
			model2ViewAdpt.displayText(formatter.apply(logEntry));
		}
		
	});

	/**
	 * The OwlMapControl object that runs everything about the map.
	 */
	private IOwlMapControl owlMapControl;
	
	
	/**
	 * A local reference to the map itself.   This could have always been retrieved directly from the OwlMapControl object.
	 */
	private IOwlMap owlMap;
	
	/**
	 * The map-dependent map utilities 
	 */
	private IOwlMapUtils owlMapUtils;  //= owlMapControl.getMapUtils(); //IOwlMapUtils.make(MapLengthUnits.FEET);
	


	
//	/**
//	 * The Google Maps API key to use
//	 */
//	private String googleMapsApiKey;

	/**
	 * The system-wide logger to use
	 */
	private ILogger sysLogger;

	/**
	 * Latch used to ensure that the map is not accessed before it is ready.
	 */
	private CountDownLatch mapReadyCountDownLatch;

	/**
	 * The app config in use
	 */
	private AppConfigGMaps currentConfig;

	/**
	 * Construct the demo model
	 * @param sysLogger 
	 * @param model2ViewAdpt The adapter to the view 
	 * @param currentConfig.apiKey The Google Maps API key to use
	 */
	public DemoModel(ILogger sysLogger, IModel2ViewAdapter model2ViewAdpt, AppConfigGMaps currentConfig) {
		
		this.sysLogger = sysLogger;
		sysLogger.setLogLevel(LogLevel.DEBUG);
		this.model2ViewAdpt = model2ViewAdpt;
		this.currentConfig = currentConfig;
//		this.googleMapsApiKey = currentConfig.apiKey;
		
		viewLogger.append(this.sysLogger);	
		
	}
	

	/**
	 * Start the model. 
	 */
	public void start() {
		viewLogger.log(LogLevel.INFO, "user.dir = "+System.getProperty("user.dir"));
		viewLogger.log(LogLevel.INFO, "[DemoModel.start()] System starting..."); // logger that displays messages on both the view and console
		try {
			this.mapReadyCountDownLatch = new CountDownLatch(1);   // Initialize the latch which which will pause threads as they wait for the map to be ready

			IMixedDataDictionary mapOptionsDict = IMapOptions.makeDefault();
			// Add additional map options here.
			
			// Make the factory but DO NOT run it yet! -- the IOwlMapsControl and map is not constructed until the mapFactory runs!
			Supplier<JComponent> mapFactory =  IOwlMapControl.makeMapFactory(
				//this.googleMapsApiKey,
				currentConfig.apiKey,
				mapOptionsDict, 
				(owlMapControl)->{
					// The mapControl and its map are NOT available until this function runs!  
					// Any initial processing of the map must be done here.
				
					this.owlMapControl = owlMapControl;  // The IOwlMapControl instance has not been saved anywhere yet, so assign it to a field in the model!
					this.owlMap = owlMapControl.getMap();   // Get the map -- map is not available until now!
					this.owlMapUtils = owlMapControl.getMapUtils();  // Save the map utils
					
					// The IMapComponentFactory can be obtained from the IOwlMapControl and used now if desired.
					
					// Add a right-click mouse handler to the map, for example
					this.owlMap.setMapMouseEvent(MapMouseEventType.RIGHT_CLICK, (mouseEvent)->{
						viewLogger.log(LogLevel.DEBUG, "[MapMouseEvent: "+MapMouseEventType.RIGHT_CLICK.getName()+"] mouseEvent = "+mouseEvent);
						model2ViewAdpt.setLatLng(mouseEvent.getLatLng());
					});
					viewLogger.log(LogLevel.INFO, "[onMapLoad] completed, releasing countdown latch!");
					mapReadyCountDownLatch.countDown();  // release threads waiting for map to be ready
				}, 
				viewLogger,
				// For convenience, these last two parameters can be omitted and will default to MapLengthUnits.FEET and false respectively.
				MapLengthUnits.FEET,  // The length units to use in the map
				currentConfig.useDev // if true, the Chrominum dev tools console will be shown
			);
			
			// Do the equivalent of an ICmd2ModelAdapter.displayComponent(componentFactory, title) call:
			model2ViewAdpt.addMapComp(mapFactory, "Demo Map"); // Give the factory to the view to be run on the GUI thread. See view.DemoFrame.addMainComponent()
			
		} catch (Exception e) {
			viewLogger.log(LogLevel.ERROR, "[DemoModel.start()] Exception = " + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Reset the map.  Currently, only sets the options back to their default settings.  
	 */
	public void resetMap() {
		try {
			owlMap.setOptions(IMapOptions.makeDefault());
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		owlMapControl.resetMap(IMapOptions.makeDefault(),
//			()->{
//				System.out.println("[DemoModel.resetMap()] Map is reset.");
//			});
	}	
	
	/**
	 * Stop the model. Must call this when exiting in order to stop the JxBrowser
	 * engine.
	 */
	public void stop() {
		owlMapControl.stop();

	}

	/**
	 * Pan the map to the given ILatLng location
	 * @param latLng The location to pan to 
	 */
	public void goToLatLng(ILatLng latLng) {
		// TODO is there a better way to make this more tranparent?
		new Thread(()->{
			try {
				mapReadyCountDownLatch.await(); // Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.
				owlMap.panTo(latLng);
				makeMarker(latLng);
			} catch (InterruptedException e) {
				viewLogger.log(LogLevel.ERROR, "[goToLatLng()] Exception while waiting for countdownLatch: "+e);
				e.printStackTrace();
			}

		}).start();
	}

	/**
	 * Move the map to the given IPlace location and zoom level
	 * @param place The IPlace to move to 
	 */
	public void goToPlace(IPlace place) {
		// TODO is there a better way to make this more transparent?
		new Thread(()->{
			try {
				mapReadyCountDownLatch.await(); // Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.
				owlMap.goTo(place);
			} catch (InterruptedException e) {
				viewLogger.log(LogLevel.ERROR, "[goToPlace()] Exception while waiting for countdownLatch: "+e);
				e.printStackTrace();
			}
		}).start();
	}
	
	/**
	 * Make an IMarker object at the given location
	 * @param latLng The desired location of the marker
	 */
	public void makeMarker(ILatLng latLng) {
		(new Thread(()->{
			try {
				mapReadyCountDownLatch.await(); // Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.
	
				try {
					
					IMixedDataDictionary markerOptions = IMarkerOptions.makeDefault();
					// -- start debug code ---
		//			markerOptions.put(IMarkerOptions.VISIBLE, true);
		//			markerOptions.put(IMarkerOptions.POSITION, latLng);
					// -- end debug code ---
					
					IMarker marker = owlMapControl.getMapComponentFactory().makeMarker(markerOptions);
					marker.setPosition(latLng);
					marker.setIcon("https://www.clear.rice.edu/comp310/course/samples/images/Rice_OwlBlue_50x58.png");
					marker.setLabel(IMarkerLabel.make("Red", "Brush Script MT, Brush Script Std, cursive", "20px", "bold", "Huh?"));
					marker.setDraggable(true);
					marker.setComponentMouseEvent(ComponentMouseEventType.DBL_CLICK, (mouseEvt)->{
						viewLogger.log(LogLevel.DEBUG, "[Marker Double-Click] ILatLng = "+mouseEvt.getLatLng());
						marker.setTitle(mouseEvt.getLatLng().toString());
						
					});
					
					marker.setMarkerEvent(MarkerEventType.POSITION_CHANGED, ()->{
						ILatLng markerLatLng = marker.getPosition();
						viewLogger.log(LogLevel.DEBUG, "[Marker Position Changed] position = "+markerLatLng);
						marker.setTitle(String.format("(%3.5f, %3.5f)", markerLatLng.getLat(), markerLatLng.getLng()));
		//				marker.setTitle(.toString());
					});
					
					viewLogger.log(LogLevel.DEBUG, "marker.getPixelOffset() = "+marker.getPixelOffset());
		//			marker.setTitle("!!");
					marker.setCursor("help");
					
					marker.setVisible(true);
					
					IMixedDataDictionary infoWinOptions = IInfoWindowOptions.makeDefault();
					infoWinOptions.put(IInfoWindowOptions.ANCHOR, marker);
					
					IInfoWindow infoWin = owlMapControl.getMapComponentFactory().makeInfoWindow(infoWinOptions);
					infoWin.setContent("<img src='https://www.clear.rice.edu/comp310/course/samples/images/Rice_Owl_flying_75x51.png'><br/>Right-click the marker to re-open info window.<br/>Marker will show a formatted lat/lng when dragged.");
					
					marker.setComponentMouseEvent(ComponentMouseEventType.RIGHT_CLICK, (mouseEvt)->{
						viewLogger.log(LogLevel.DEBUG, "[Marker right-click] ILatLng = "+mouseEvt.getLatLng());
						infoWin.setVisible(!infoWin.getVisible());
					});
					
					infoWin.setInfoWindowEvent(InfoWindowEventType.CLOSE_CLICK, ()->{
						viewLogger.log(LogLevel.DEBUG, "Additional info window close operation!");
					});
					
		//			IInfoWindow infoWin = owlMapControl.getMapComponentFactory().makeInfoWindow(IInfoWindowOptions.makeDefault(latLng, true), marker);
					infoWin.setVisible(true);
					
				} catch (ClassNotFoundException e) {
					viewLogger.log(LogLevel.ERROR, "[makeMarker()] Exception = "+e);
					e.printStackTrace();
				}
			} catch (InterruptedException e1) {
				viewLogger.log(LogLevel.ERROR, "[makeMarker()] Exception while waiting for countdownLatch: "+e1);
				e1.printStackTrace();
			}
		})).start();
	}

	/**
	 * Make an IInfoWindow object at the given location
	 * @param latLng The desired location of the info window
	 */
	public void makeInfoWin(ILatLng latLng) {
		String htmlContent = "Because...<br/><img src='https://www.clear.rice.edu/comp310/course/samples/images/humbird_150x102.gif'>";
		(new Thread(()->{
				try {
					mapReadyCountDownLatch.await();// Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.
					try {
						IMixedDataDictionary infoWinOptions = IInfoWindowOptions.makeDefault();
						infoWinOptions.put(IInfoWindowOptions.POSITION, latLng);
						IInfoWindow infoWin = owlMapControl.getMapComponentFactory().makeInfoWindow(infoWinOptions);
						infoWin.setContent(htmlContent);
						infoWin.setVisible(true);
						
					} catch (ClassNotFoundException e) {
						viewLogger.log(LogLevel.ERROR, "[makeInfoWin()] Exception = "+e);
						e.printStackTrace();
					}
				} catch (InterruptedException e1) {
					viewLogger.log(LogLevel.ERROR, "[makeInfoWin()] Exception while waiting for countdownLatch: "+e1);
					e1.printStackTrace();
				} 

		})).start();
		
	}

	/**
	 * Make an IPolygon object at the given location
	 * @param originLatLng The desired location of the origin point of the polygon
	 */
	public void makePolygon(ILatLng originLatLng ) {
		(new Thread(()->{
			try {
				mapReadyCountDownLatch.await(); // Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.

					try {		
						IMixedDataDictionary options = IPolygonOptions.makeDefault();
					
		
						IPath<Point2D> pts1  = IPath.make(new Point(100,0), new Point(-50,100), new Point(-80, -50), new Point(50, -50));
						IPath<Point2D> pts2  = IPath.make(new Point(20,0), new Point(-10,20), new Point(-16, -10), new Point(10, -10));
						IPaths<Point2D> pts3  = IPathList.make(pts1, pts2);
						
						options.put(IPolygonOptions.PATHS, owlMapUtils.convertPointsToLatLng(originLatLng, pts3));
						viewLogger.log(LogLevel.DEBUG, "[makePolygon()] options[PATHS] = "+options.get(IPolygonOptions.PATHS));
						
						
						IPolygon polygon = owlMapControl.getMapComponentFactory().makePolygon(options);
						polygon.setVisible(true);
						
						viewLogger.log(LogLevel.DEBUG, "path = "+polygon.getPath());			
						viewLogger.log(LogLevel.DEBUG, "paths = "+polygon.getPaths());
		//				
		//				IPath<Point2D> pts4  = IPath.make(new Point(25,0), new Point(0, 25), new Point(-25, 0), new Point(0, -25));
		//				IPaths<Point2D> pts5  = IPathList.make(pts1, pts2, pts4);
		//				
		//				polygon.setPaths(owlMapUtils.convertPointsToLatLng(centerLatLng, pts5));
						
					} catch (ClassNotFoundException e) {
						viewLogger.log(LogLevel.ERROR, "[makePolygon()] Exception = "+e);
						e.printStackTrace();
					}
			} catch (InterruptedException e1) {
				viewLogger.log(LogLevel.ERROR, "[makePolygon()] Exception while waiting for countdownLatch: "+e1);
				e1.printStackTrace();
			}

			
		})).start();
	}

	/**
	 * Make an IPolyline object at the given location
	 * @param originLatLng The desired location of the origin point of the polyline
	 */
	public void makePolyline(ILatLng originLatLng ) {
		(new Thread(()->{
			try {
				mapReadyCountDownLatch.await(); // Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.
				try {
					
					IMixedDataDictionary options = IPolylineOptions.makeDefault();
					
					IPath<Point2D> pts1  = IPath.make(new Point(100,0), new Point(-50,100), new Point(-80, -50), new Point(50, -50));
		
					options.put(IPolylineOptions.PATH, owlMapUtils.convertPointsToLatLng(originLatLng, pts1));
					viewLogger.log(LogLevel.DEBUG, "[makePolyline()] options[PATH] = "+options.get(IPolylineOptions.PATH));			
		
					// The IPolylineOptions.ICONS is a List of IIconSequenceOptions dictionaries.
					options.put(IPolylineOptions.ICONS, new ArrayList<IMixedDataDictionary>() {
						/**
						 * For Serializable
						 */
						private static final long serialVersionUID = 2228077614002321364L;
		
						{
							IMixedDataDictionary symbolOptions = ISymbolOptions.makeDefault();
							symbolOptions.put(ISymbolOptions.PATH, SymbolPathConstants.FORWARD_CLOSED_ARROW);
		//					symbolOptions.put(ISymbolOptions.PATH, SymbolPathConstants.CIRCLE);
							symbolOptions.put(ISymbolOptions.STROKE_COLOR, "Red"); // Polyline symbol colors are controlled by stroke color not fill color
							
							IMixedDataDictionary iconSequenceOptions =  IIconSequenceOptions.makeDefault();
							iconSequenceOptions.put(IIconSequenceOptions.ICON, symbolOptions); //IIconSequenceOptions.ICON is an ISymbolOptions dictionary
							iconSequenceOptions.put(IIconSequenceOptions.REPEAT, "25%");
							add(iconSequenceOptions);
							
							// Adding more symbols on the polyline, reusing the variables for convenience...
							symbolOptions = ISymbolOptions.makeDefault();
							symbolOptions.put(ISymbolOptions.PATH, SymbolPathConstants.CIRCLE);
							symbolOptions.put(ISymbolOptions.SCALE, 5.0);
							
							iconSequenceOptions =  IIconSequenceOptions.makeDefault();
							iconSequenceOptions.put(IIconSequenceOptions.ICON, symbolOptions);
							iconSequenceOptions.put(IIconSequenceOptions.REPEAT, "10%");
							add(iconSequenceOptions);
							
						}
						
					});
					
					IPolyline polyline = owlMapControl.getMapComponentFactory().makePolyline(options);
					polyline.setVisible(true);
					
				} catch (ClassNotFoundException e) {
					viewLogger.log(LogLevel.ERROR, "[DemoModel.makePolyline()] Exception = "+e);
					e.printStackTrace();
				}
			} catch (InterruptedException e1) {
				viewLogger.log(LogLevel.ERROR, "[makePolyline()] Exception while waiting for countdownLatch: "+e1);
				e1.printStackTrace();
			}
		})).start();
	}	
	
	/**
	 * Make an IRectangle object at the given location
	 * @param originLatLng The desired location of the origin point of the rectangle
	 */
	public void makeRectangle(ILatLng originLatLng ) {
		(new Thread(()->{
			try {
				mapReadyCountDownLatch.await(); // Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.
	
				try {
					
					IMixedDataDictionary options = IRectangleOptions.makeDefault();
					
					Point2D.Double swPt = new Point2D.Double(-30, -10);
					Point2D.Double nePt = new Point2D.Double(30, 10);
					
					ILatLng swLatLng = owlMapUtils.getLatLngRelativeTo(originLatLng, swPt);
					ILatLng neLatLng = owlMapUtils.getLatLngRelativeTo(originLatLng, nePt);	
		
					options.put(IRectangleOptions.BOUNDS, ILatLngBounds.make(swLatLng, neLatLng));
					viewLogger.log(LogLevel.DEBUG, "[makeRectangle()] options[BOUNDS] = "+options.get(IRectangleOptions.BOUNDS));
					
					IRectangle rectangle = owlMapControl.getMapComponentFactory().makeRectangle(options);
					rectangle.setVisible(true);
					
				} catch (ClassNotFoundException e) {
					viewLogger.log(LogLevel.ERROR, "[makeRectangle()] Exception = "+e);
					e.printStackTrace();
				}
			} catch (InterruptedException e1) {
				viewLogger.log(LogLevel.ERROR, "[makeRectangle()] Exception while waiting for countdownLatch: "+e1);
				e1.printStackTrace();
			}
		})).start();
	}	
	
	/**
	 * Make an ICircle object at the given location
	 * @param originLatLng The desired location of the center the circle
	 */
	public void makeCircle(ILatLng originLatLng ) {
		(new Thread(()->{
			try {
				mapReadyCountDownLatch.await(); // Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.
	
				try {
					
					IMixedDataDictionary options = ICircleOptions.makeDefault();
					
					double radius = 50;
		
					options.put(ICircleOptions.CENTER, originLatLng);
					options.put(ICircleOptions.RADIUS, radius);
					options.put(ICircleOptions.FILL_COLOR, "Green");
					options.put(ICircleOptions.STROKE_COLOR, "Blue");
					
					ICircle circle = owlMapControl.getMapComponentFactory().makeCircle(options);
					circle.setVisible(true);
					
				} catch (ClassNotFoundException e) {
					viewLogger.log(LogLevel.ERROR, "[makeCircle()] Exception = "+e);
					e.printStackTrace();
				}
			} catch (InterruptedException e1) {
				viewLogger.log(LogLevel.ERROR, "[makeCircle()] Exception while waiting for countdownLatch: "+e1);
				e1.printStackTrace();
			}
		})).start();
	}

	/**
	 * Make an overlay centered at the given lat/lng
	 * @param originLatLng The center of the image
	 *
	 */
	public void makeOverlay(ILatLng originLatLng) {
		(new Thread(()->{
			try {
				mapReadyCountDownLatch.await(); // Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.
	
				try {
					String url = "https://scholarship.rice.edu/bitstream/handle/1911/63949/wrc00681.jpg";   // Willy facing backwards
					
					double halfWidth = 50.0; // in system length units 
					
					Point2D.Double swPt = new Point2D.Double(-halfWidth, -halfWidth);
					Point2D.Double nePt = new Point2D.Double(halfWidth, halfWidth);
					
					ILatLng swLatLng = owlMapUtils.getLatLngRelativeTo(originLatLng, swPt);
					ILatLng neLatLng = owlMapUtils.getLatLngRelativeTo(originLatLng, nePt);	
					
					
					ILatLngBounds bounds = ILatLngBounds.make(swLatLng, neLatLng);
					
					IMixedDataDictionary options = IGroundOverlayOptions.makeDefault();
					
					options.put(IGroundOverlayOptions.URL, url);
					options.put(IGroundOverlayOptions.BOUNDS, bounds);
					
					options.put(IGroundOverlayOptions.OPACITY, 0.75);
					
		//			options.put(IGroundOverlayOptions.VISIBLE, true);
					
					IGroundOverlay overlay = owlMapControl.getMapComponentFactory().makeGroundOverlay(options);
					
					overlay.setGroundOverlayMouseEvent(GroundOverlayMouseEventType.CLICK, (evt)->{
						overlay.setVisible(false);
					});
					
					overlay.setVisible(true);
					
				} catch (ClassNotFoundException e) {
					viewLogger.log(LogLevel.ERROR, "[makeOverlay()] Exception = "+e);
					e.printStackTrace();
				}	
			} catch (InterruptedException e1) {
				viewLogger.log(LogLevel.ERROR, "[makeOverlay()] Exception while waiting for countdownLatch: "+e1);
				e1.printStackTrace();
			}

		})).start();
	}

	/**
	 * Load outlines of world countries with a specific default styling and an overridden styling for USA.  
	 * The new default styling may change the appearance of an existing feature that uses the default styling.
	 */
	public void loadGeoJson() {
		(new Thread(()->{
			try {
				mapReadyCountDownLatch.await(); // Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.

				IMixedDataDictionary mapDataStyleOptions = IMapDataStyleOptions.makeDefault();
				
				mapDataStyleOptions.put(IMapDataStyleOptions.VISIBLE, true);
				mapDataStyleOptions.put(IMapDataStyleOptions.STROKE_COLOR, "Green");
				mapDataStyleOptions.put(IMapDataStyleOptions.FILL_COLOR, "Blue");
				mapDataStyleOptions.put(IMapDataStyleOptions.FILL_OPACITY, 0.15);
				
				owlMap.setDataStyle(mapDataStyleOptions);
		
				//owlMap.loadGeoJson("https://storage.googleapis.com/mapsdevsite/json/google.json",  (features)->{// fill this in}); // draws to word "Google" on top of Australia
				
				// Load outlines of world countries
				owlMap.loadGeoJson("https://raw.githubusercontent.com/johan/world.geo.json/master/countries.geo.json", (featureMap)->{
					
					// featureMap is a Map<feature_id_string, IMapDataFeature> 
					
					viewLogger.log(LogLevel.INFO, "[addGeoJson()] Found featureMap = "+featureMap);
					// Print out the features that were found.
					featureMap.forEach( (featureId, feature) -> {
						viewLogger.log(LogLevel.INFO, "[addGeoJson()] Found feature (ID = "+featureId+") properties: "+feature.getProperties());
						viewLogger.log(LogLevel.INFO, "[addGeoJson()] Found feature.getGeometry()  = "+feature.getGeometry());
						viewLogger.log(LogLevel.INFO, "[addGeoJson()] Found feature.getGeometry().getLatLngs().size() = "+feature.getGeometry().getLatLngs().size());
						// Too many features to print out all their geometries!
						
		//				System.out.println("[DemoModel.addGeoJson()] Found feature.getGeometry().getLatLngs()  = ");
		//				
		//				// Print the list of ILatLngs in chunks because println can't properly printout a single string with all 2500+ elements.
		//				// This could also have been done using List.subList()
		//				feature.getGeometry().getLatLngs().forEach( (Consumer<? super ILatLng>) new Consumer<ILatLng>() {
		//					// Explicit declaration using anonymous inner class rather than lambda expression so that the function can hold state. 
		//					int count = 0; // counter for elements per line
		//					
		//					public void accept(ILatLng latLng){
		//						
		//						if(10<=++count) {  // max 10 elements per line
		//							count = 0;
		//							System.out.println("(%3.5f, %3.5f)".formatted(latLng.getLat(), latLng.getLng())); // print with newline at end
		//						}
		//						else {
		//							System.out.print("(%3.5f, %3.5f), ".formatted(latLng.getLat(), latLng.getLng())); // print with no newline at end
		//						}
		//					}
		//				});
					});
			
					
					IMixedDataDictionary featureDataStyleOptions = IMapDataStyleOptions.makeDefault();
					
					featureDataStyleOptions.put(IMapDataStyleOptions.VISIBLE, true);
					featureDataStyleOptions.put(IMapDataStyleOptions.STROKE_COLOR, "Red");
					featureDataStyleOptions.put(IMapDataStyleOptions.FILL_COLOR, "Yellow");
					featureDataStyleOptions.put(IMapDataStyleOptions.FILL_OPACITY, 0.25);
					String featureId = "USA";   
					if(featureMap.containsKey(featureId)) { // make sure that the associated feature actually exists first!
						owlMap.overrideDataStyleById(featureId, featureDataStyleOptions);	// Override the styling for the "USA" feature.							
					}
					else {
						viewLogger.log(LogLevel.INFO, "[loadGeoJson()] Result does not include featureId = "+featureId);
					}
		
					owlMap.setZoom(3);
				});
			} catch (InterruptedException e) {
				viewLogger.log(LogLevel.ERROR, "[loadGeoJson()] Exception while waiting for countdownLatch: "+e);
				e.printStackTrace();
			}

		})).start();
	}

	/**
	 * Add GeoJSON data using the currently set data styling.  Note that the styling will change if the default styling is changed.
	 */
	public void addGeoJson() {
		(new Thread(()->{
			try {
				mapReadyCountDownLatch.await(); // Wait for the map to be ready! Optionally, can terminate wait after a specified time. See CountDownLatch docs.

				// from https://github.com/TNRIS/tx.geojson
				
				// outline of Harris County -- Note: as retrieved from the web site above, the feature has no feature ID defined (missing "id" field)!
				String geoJsonStr = "{\r\n" + 
						"\"type\": \"FeatureCollection\",\r\n" + 
						"\"features\": [ {\r\n" + 
						 //"\"id\": \"Harris\","+      // Uncomment this line to add the missing feature ID
						"\"type\": \"Feature\", \"properties\": { \"STATE\": \"TX\", \"COUNTY\": \"Harris County\", \"FIPS\": \"48201\", \"STATE_FIPS\": \"48\", \"SQUARE_MIL\": 1754.39 }, \"geometry\": { \"type\": \"MultiPolygon\", \"coordinates\": [ [ [ [ -94.971225164999908, 29.680874776000053 ], [ -94.975571692999893, 29.678532236000081 ], [ -94.977227585999856, 29.680096136000145 ], [ -94.978391647999899, 29.681759081000166 ], [ -94.978906711999912, 29.684082171000171 ], [ -94.976679335999904, 29.685237808000128 ], [ -94.974438479999947, 29.68617680500012 ], [ -94.972278192999909, 29.684528305000125 ], [ -94.969860629999857, 29.682514095000098 ], [ -94.970171194999921, 29.681442807999989 ], [ -94.971225164999908, 29.680874776000053 ] ] ], [ [ [ -94.975748061999923, 29.688503541000042 ], [ -94.979300096999907, 29.687784067000052 ], [ -94.982174689999908, 29.689709382999983 ], [ -94.985192659999953, 29.692295863000137 ], [ -94.988171407999857, 29.695015800000192 ], [ -94.991210126999931, 29.696883552000202 ], [ -94.98836879299995, 29.698116405000178 ], [ -94.985166745999962, 29.698295688000144 ], [ -94.982507785999928, 29.697581198000197 ], [ -94.979908727999941, 29.696187805000079 ], [ -94.976549363999879, 29.694514211000183 ], [ -94.973947153999859, 29.690990759000158 ], [ -94.975748061999923, 29.688503541000042 ] ] ], [ [ [ -95.033774846999847, 29.708756302000044 ], [ -95.036798512999951, 29.708328471000076 ], [ -95.040080679999903, 29.709197733000053 ], [ -95.041653920999863, 29.711686547000109 ], [ -95.043261378999887, 29.713708799000134 ], [ -95.043932374999883, 29.716008103000092 ], [ -95.043160370999885, 29.718319199000177 ], [ -95.041820925999914, 29.720388377000177 ], [ -95.043284678999896, 29.722600133000185 ], [ -95.04348117, 29.725555568 ], [ -95.041069776999848, 29.726840800000161 ], [ -95.0383375749999, 29.727226796000085 ], [ -95.036289672999942, 29.727150303000087 ], [ -95.033375541999931, 29.727073986000054 ], [ -95.02933326599981, 29.726445798000157 ], [ -95.02580417799993, 29.724888696000104 ], [ -95.024706608999921, 29.722343942000069 ], [ -95.024809383999866, 29.719907002000184 ], [ -95.024856009999908, 29.716923130000058 ], [ -95.024851977999901, 29.714302401 ], [ -95.02530639399987, 29.710731437000167 ], [ -95.027500451999856, 29.709153984000071 ], [ -95.03015530499988, 29.708879553000202 ], [ -95.033774846999847, 29.708756302000044 ] ] ], [ [ [ -95.065858320999951, 29.766295478000075 ], [ -95.070466389999922, 29.764687002000191 ], [ -95.071038110999893, 29.765538297000035 ], [ -95.072987786999931, 29.764556585000154 ], [ -95.075030987999867, 29.763556721000043 ], [ -95.077335021999886, 29.764295750000148 ], [ -95.077335021999886, 29.765465273000189 ], [ -95.077335021999886, 29.766469369000163 ], [ -95.075422236999941, 29.767121455000112 ], [ -95.073465983999881, 29.767164926000024 ], [ -95.072286228999872, 29.767628010000184 ], [ -95.072916649999911, 29.768683518000106 ], [ -95.07052577099995, 29.768950048000139 ], [ -95.069029135999926, 29.769019264000178 ], [ -95.064945400999932, 29.769208127000066 ], [ -95.063249978999863, 29.768425625000191 ], [ -95.065858320999951, 29.766295478000075 ] ] ], [ [ [ -95.087939664999908, 29.781600197999975 ], [ -95.080073778999918, 29.781336914000065 ], [ -95.076378628999919, 29.781380387000183 ], [ -95.078160995999838, 29.777293986000018 ], [ -95.078378357999895, 29.77120785600005 ], [ -95.080769338999914, 29.76681714800003 ], [ -95.087507551999863, 29.765773811000145 ], [ -95.093072013999915, 29.769077711000136 ], [ -95.095984659999942, 29.768077846000093 ], [ -95.098027861999924, 29.766512842000111 ], [ -95.099723283999936, 29.77420744900019 ], [ -95.097462720999943, 29.778641629000106 ], [ -95.090883053999903, 29.780804571000143 ], [ -95.087939664999908, 29.781600197999975 ] ] ], [ [ [ -95.540654026999903, 30.169892720000121 ], [ -95.539679026999863, 30.169136723000179 ], [ -95.538862024999844, 30.168448720000185 ], [ -95.537043025999878, 30.16897472300008 ], [ -95.536068025999896, 30.168080722000127 ], [ -95.534908026999915, 30.168263723000052 ], [ -95.534118029999831, 30.167300723000153 ], [ -95.533486025999935, 30.166109722000101 ], [ -95.532485025999904, 30.16535372400006 ], [ -95.531800030999875, 30.164596722000109 ], [ -95.530693029999838, 30.165145723000194 ], [ -95.52961202899985, 30.165259724000126 ], [ -95.528479028999925, 30.164732722 ], [ -95.528154131999941, 30.164443021000125 ], [ -95.527399031999892, 30.163769723000204 ], [ -95.526239029999942, 30.163425721000124 ], [ -95.526688032999857, 30.162348725000015 ], [ -95.525581030999945, 30.162348725000015 ], [ -95.524501027999918, 30.161751727000194 ], [ -95.524078438999936, 30.161218920000067 ], [ -95.523738028999844, 30.160789726000075 ], [ -95.524266028999932, 30.159690727 ], [ -95.524398029999929, 30.15868272400019 ], [ -95.523001029999875, 30.158727727000102 ], [ -95.523450033999893, 30.157811727000137 ], [ -95.522371028999885, 30.156573724000111 ], [ -95.522214034999877, 30.155520725000144 ], [ -95.521444821999921, 30.154930790000094 ], [ -95.521107032999865, 30.154671728000153 ], [ -95.52031803499996, 30.153894724000168 ], [ -95.519580030999919, 30.152998726000074 ], [ -95.518711030999839, 30.15228872400013 ], [ -95.518439961999889, 30.152902771000072 ], [ -95.51823603299988, 30.15336472700011 ], [ -95.517312032999826, 30.15398172800008 ], [ -95.516470031999859, 30.153133727000355 ], [ -95.515812034999897, 30.151896724000043 ], [ -95.514469035999866, 30.151116726000165 ], [ -95.513441032999879, 30.150566726000136 ], [ -95.512678032999929, 30.149489724000034 ], [ -95.510843036999859, 30.148623727000025 ], [ -95.509754036999936, 30.148318724000148 ], [ -95.509201034999933, 30.147379727000331 ], [ -95.508114035999938, 30.146822725000053 ], [ -95.507226038999931, 30.146300728000138 ], [ -95.505857035999895, 30.145177726000099 ], [ -95.50419603499995, 30.145084725000171 ], [ -95.503274037999859, 30.144579728 ], [ -95.502273038999931, 30.14439572900017 ], [ -95.500903035999897, 30.143730730000126 ], [ -95.499792040999921, 30.1433227290002 ], [ -95.499842035999848, 30.141581728000119 ], [ -95.499340035999808, 30.140437731000073 ], [ -95.498574040999927, 30.139200727 ], [ -95.498783040999925, 30.13787273100013 ], [ -95.497886037999933, 30.137117726000099 ], [ -95.497410035999906, 30.136041731000148 ], [ -95.497514039999885, 30.134781729000171 ], [ -95.496906038999839, 30.133842732000179 ], [ -95.49716904099995, 30.132834729000194 ], [ -95.496140037999908, 30.13210272800006 ], [ -95.496455037999908, 30.131002730000034 ], [ -95.496401042999878, 30.129857729000037 ], [ -95.497111037999844, 30.128963728000087 ], [ -95.496530040999914, 30.127956730000051 ], [ -95.496317040999884, 30.126788731000151 ], [ -95.497423037999909, 30.126351731000057 ], [ -95.497211041999947, 30.125344732000144 ], [ -95.497078041999956, 30.124061733000133 ], [ -95.497524038999927, 30.122709729000178 ], [ -95.497128037999914, 30.12177173200007 ], [ -95.495993040999906, 30.12069573500014 ], [ -95.496440041999847, 30.119687732000163 ], [ -95.495517039999925, 30.119207730000202 ], [ -95.494699039999944, 30.118268732000161 ], [ -95.493724039999904, 30.117834732000119 ], [ -95.492932038999925, 30.11694273400019 ], [ -95.492798041999947, 30.115567733000034 ], [ -95.491823041999908, 30.114904731000081 ], [ -95.492375044999847, 30.113918734000094 ], [ -95.492294044999895, 30.112888732000162 ], [ -95.491845041999795, 30.111491733000094 ], [ -95.490922044999877, 30.110942732000069 ], [ -95.491237038999941, 30.109980737000175 ], [ -95.489839040999925, 30.109019734000125 ], [ -95.488547042999926, 30.108104733000175 ], [ -95.487335045999941, 30.107761736000153 ], [ -95.486255042999915, 30.107785733000071 ], [ -95.48470104199987, 30.107833737000139 ], [ -95.483356046999859, 30.107307733000141 ], [ -95.482329047999883, 30.107125731000167 ], [ -95.481353042999785, 30.106392732000188 ], [ -95.480720045999817, 30.105614736000121 ], [ -95.479588043999854, 30.106279735000154 ], [ -95.479350042999897, 30.105249734000328 ], [ -95.478217043999905, 30.10508973200001 ], [ -95.477215045999799, 30.104357736000054 ], [ -95.476213045999941, 30.104244733000204 ], [ -95.475422043999856, 30.103603736000196 ], [ -95.473842043999881, 30.103627732000064 ], [ -95.473158047999846, 30.104521734000112 ], [ -95.472184045999825, 30.105140731000059 ], [ -95.471655046999842, 30.103927736000173 ], [ -95.470575050999912, 30.103813734000138 ], [ -95.469654044999928, 30.104386731000201 ], [ -95.468389044999867, 30.103883731000167 ], [ -95.466702045999909, 30.10356473600018 ], [ -95.466308048999906, 30.104641732000122 ], [ -95.465097049999883, 30.105214735000175 ], [ -95.464226046999784, 30.104299734000051 ], [ -95.463121049999927, 30.105010734000075 ], [ -95.461883047999891, 30.105469736000373 ], [ -95.461302049999858, 30.104507734000038 ], [ -95.460645051999848, 30.10540173600009 ], [ -95.459485046999816, 30.105379736000028 ], [ -95.457772047999924, 30.104487733000045 ], [ -95.4566660509999, 30.105106731000092 ], [ -95.455717050999922, 30.104351732000055 ], [ -95.454584050999927, 30.104351732000055 ], [ -95.453663052999843, 30.105406734000152 ], [ -95.454085051999925, 30.106322733000184 ], [ -95.452952051999887, 30.106070735000117 ], [ -95.452822048999906, 30.107284735000178 ], [ -95.452006051999888, 30.107881734000106 ], [ -95.450741052999945, 30.108477735000125 ], [ -95.449872052999865, 30.109050732000018 ], [ -95.448186050999936, 30.108845732000134 ], [ -95.446974054999828, 30.109258732000171 ], [ -95.445473051999898, 30.109351733000157 ], [ -95.444261053999867, 30.109649733000136 ], [ -95.443391053999903, 30.108619732000079 ], [ -95.441600051999899, 30.109078733000107 ], [ -95.440572053999915, 30.109583730000054 ], [ -95.439861053999891, 30.108827732000062 ], [ -95.439598052999898, 30.110225732000064 ], [ -95.438333057999841, 30.110248729000148 ], [ -95.437200058999906, 30.109836734000055 ], [ -95.436523057999921, 30.110579731000144 ], [ -95.436449580999863, 30.11082298000014 ], [ -95.436069054999905, 30.112082731000157 ], [ -95.434726056999921, 30.113159733000092 ], [ -95.43161705599988, 30.113619733000181 ], [ -95.430063055999938, 30.113482734000115 ], [ -95.427692058999924, 30.114903730000098 ], [ -95.426098060999948, 30.115622730000041 ], [ -95.425982210999848, 30.115616813000205 ], [ -95.424610055999949, 30.11554673000018 ], [ -95.423898056999917, 30.114699730000098 ], [ -95.423634057999834, 30.113485730000153 ], [ -95.423185060999913, 30.112477733000162 ], [ -95.423132056999862, 30.110943734000156 ], [ -95.422472062999873, 30.109912734000144 ], [ -95.4215650619999, 30.109364732000074 ], [ -95.420390060999921, 30.108630734000091 ], [ -95.419284059999882, 30.108768732000161 ], [ -95.419046057999935, 30.107783733000076 ], [ -95.418914057999928, 30.106523731000095 ], [ -95.417596060999927, 30.10640973000017 ], [ -95.417253062999862, 30.105356732000072 ], [ -95.418334059999907, 30.105676730000081 ], [ -95.419097058999853, 30.104920733000199 ], [ -95.418412063999938, 30.10416473000004 ], [ -95.416594062999877, 30.103363731000115 ], [ -95.414301064999904, 30.101554735000033 ], [ -95.412509062999845, 30.101280729999985 ], [ -95.412456065999891, 30.10004373300006 ], [ -95.411428060999924, 30.099425733000093 ], [ -95.4107170609999, 30.098623734000057 ], [ -95.41092706399985, 30.097638737000182 ], [ -95.411875062999798, 30.096699733000154 ], [ -95.412849063999829, 30.09585173400006 ], [ -95.411637060999908, 30.094981735000093 ], [ -95.410636061999867, 30.094042731 ], [ -95.410424066999909, 30.092920734000188 ], [ -95.409107060999929, 30.092553735000077 ], [ -95.407606062999889, 30.093081735000112 ], [ -95.406526066999902, 30.092508731000123 ], [ -95.405261066999913, 30.09189073400006 ], [ -95.402916063999896, 30.091776738000195 ], [ -95.401810067999861, 30.09138773300009 ], [ -95.400255067999808, 30.090082735000294 ], [ -95.399728066999899, 30.088731736000167 ], [ -95.398964068999931, 30.087471735000065 ], [ -95.398911065999869, 30.08646373200008 ], [ -95.397593067999821, 30.085318737000136 ], [ -95.396618067999896, 30.084745735000126 ], [ -95.395512065999924, 30.083829735 ], [ -95.393747067999925, 30.083371738000039 ], [ -95.39190306799992, 30.082685738000126 ], [ -95.390823070999943, 30.083097733000045 ], [ -95.39048106599995, 30.084769737000162 ], [ -95.388795071999823, 30.084632738000096 ], [ -95.386635071999876, 30.084884736000166 ], [ -95.385713067999916, 30.085311735000062 ], [ -95.385239069999841, 30.08713073500013 ], [ -95.384028072999911, 30.087886732000072 ], [ -95.382263068999919, 30.087244736000059 ], [ -95.380761071999871, 30.087199733000148 ], [ -95.379681068999901, 30.08770373800019 ], [ -95.37870707299993, 30.08735973600017 ], [ -95.377785069999845, 30.086581735000156 ], [ -95.376652069999921, 30.085893736000173 ], [ -95.37520307099993, 30.086374737000082 ], [ -95.374070070999949, 30.085664735000137 ], [ -95.374360069999909, 30.08385573400011 ], [ -95.374281072999906, 30.082686736000195 ], [ -95.374255073999905, 30.081656736000067 ], [ -95.373649076999925, 30.080006737000076 ], [ -95.372938075999912, 30.079273737000165 ], [ -95.370804076999832, 30.078197734000071 ], [ -95.369118074999847, 30.077716734000031 ], [ -95.367354074999923, 30.077990738000153 ], [ -95.365852072999814, 30.076960737000093 ], [ -95.364772076999898, 30.076639738000271 ], [ -95.363798073999931, 30.077211737000084 ], [ -95.363271078999844, 30.07817473699998 ], [ -95.36198008, 30.078723737000136 ], [ -95.361032076999891, 30.078402734000125 ], [ -95.36040007899993, 30.076913737000098 ], [ -95.358398079999915, 30.076478737000169 ], [ -95.356976077999946, 30.07732573900012 ], [ -95.3564691549999, 30.077340743000093 ], [ -95.355422077999947, 30.077371734000113 ], [ -95.354184081999861, 30.076409738000056 ], [ -95.3521030779998, 30.076019736000152 ], [ -95.350233078999793, 30.076477739000101 ], [ -95.349206078999885, 30.07533273800016 ], [ -95.349891079999907, 30.074140736000121 ], [ -95.349154079999963, 30.072949735000069 ], [ -95.347126080999942, 30.072651734000203 ], [ -95.345045077999885, 30.072284735000096 ], [ -95.343913083999837, 30.071505734000201 ], [ -95.343571078999844, 30.070314738000096 ], [ -95.343492082999887, 30.069214739000135 ], [ -95.342992084999935, 30.067885739000076 ], [ -95.341912081999908, 30.067152740000097 ], [ -95.340727083999923, 30.066923738000185 ], [ -95.338384082999937, 30.066349737 ], [ -95.337515083999904, 30.065364738000142 ], [ -95.33759408099985, 30.064219736000094 ], [ -95.337041085999942, 30.063211740000156 ], [ -95.335778081999877, 30.062615740000069 ], [ -95.334566086999871, 30.062706738000145 ], [ -95.332854085999884, 30.063393736000137 ], [ -95.331432083999914, 30.063301740000153 ], [ -95.330089086999919, 30.062544738000039 ], [ -95.328930086999947, 30.062361737999989 ], [ -95.328299087999937, 30.061307740000188 ], [ -95.328220085999817, 30.06016174000024 ], [ -95.327668088999872, 30.058855738000204 ], [ -95.326931089999903, 30.057778737000145 ], [ -95.325378085999944, 30.05677074100004 ], [ -95.323956085999896, 30.056403740000182 ], [ -95.322955085999922, 30.055761739000051 ], [ -95.322930084999882, 30.054180741000156 ], [ -95.321798089999845, 30.052760743000078 ], [ -95.320113086999868, 30.052461738000144 ], [ -95.319296085999895, 30.053148743000065 ], [ -95.317189089999886, 30.054201741000156 ], [ -95.315951087999849, 30.0541787370002 ], [ -95.314978089999897, 30.053009740000164 ], [ -95.314373089999947, 30.051909742000138 ], [ -95.313267088999908, 30.050900740000145 ], [ -95.312689092999904, 30.049388740000097 ], [ -95.312479089999897, 30.047922740000107 ], [ -95.311426089999941, 30.046662740000098 ], [ -95.30990009199985, 30.045790738000107 ], [ -95.308926089999943, 30.044736741000179 ], [ -95.30940109199986, 30.043201742000125 ], [ -95.311429091999912, 30.041988742000111 ], [ -95.311772087999941, 30.040591742000174 ], [ -95.312379091999901, 30.039492741000061 ], [ -95.312669090999918, 30.038461742000155 ], [ -95.312513088999879, 30.035345745000143 ], [ -95.312171091999915, 30.034200743000099 ], [ -95.311435089999861, 30.033283744000187 ], [ -95.310224091999942, 30.032847741000069 ], [ -95.309250088999931, 30.032617741000198 ], [ -95.307934095999826, 30.031746744000202 ], [ -95.30692309199992, 30.031296742000055 ], [ -95.305881094999847, 30.030279746000136 ], [ -95.304934092999929, 30.02936274100017 ], [ -95.303632094999841, 30.031324744000074 ], [ -95.297327096999879, 30.031476746000067 ], [ -95.296409094999945, 30.030929742000065 ], [ -95.295127092999849, 30.030284745000074 ], [ -95.294111095999881, 30.029825743000114 ], [ -95.293372093999949, 30.028879744000108 ], [ -95.293087099999923, 30.027897747000111 ], [ -95.289672097999883, 30.028389744000037 ], [ -95.287951097999951, 30.028737746000107 ], [ -95.286821099999884, 30.028932741000119 ], [ -95.285303096999939, 30.029603743000052 ], [ -95.283310100999927, 30.030009741000068 ], [ -95.281734101999916, 30.030283746000126 ], [ -95.278903098999933, 30.030775743000049 ], [ -95.277405103999911, 30.03114374 ], [ -95.276306102999911, 30.031413744000133 ], [ -95.274344099999951, 30.031948739000089 ], [ -95.272288099999912, 30.032534742000166 ], [ -95.270729104999873, 30.03223274200008 ], [ -95.268841099999918, 30.0318617420001 ], [ -95.26530110099992, 30.032240742 ], [ -95.264004103999923, 30.032214743000115 ], [ -95.263629229999879, 30.032547547000092 ], [ -95.263104105999787, 30.033013739000125 ], [ -95.260667105999858, 30.03491773900009 ], [ -95.259851103999949, 30.035558742000095 ], [ -95.255789107999945, 30.03880773700007 ], [ -95.254707106999945, 30.039707742000189 ], [ -95.254656672999943, 30.039747656000149 ], [ -95.250541106999947, 30.043004741000061 ], [ -95.249716104999948, 30.043603738000169 ], [ -95.249261636999847, 30.043967188000185 ], [ -95.246066110999948, 30.046522737000149 ], [ -95.243711110999868, 30.048351741000147 ], [ -95.241935109999929, 30.049725737000131 ], [ -95.240627109999878, 30.050831738999989 ], [ -95.239717107999923, 30.051475738000246 ], [ -95.238712107999902, 30.052291734000111 ], [ -95.237717110999881, 30.05311073900009 ], [ -95.236818111999924, 30.053852737000057 ], [ -95.235853107999844, 30.054647734000181 ], [ -95.234047108999903, 30.056188735000035 ], [ -95.231020111999896, 30.058678737000033 ], [ -95.22926711, 30.060106736000137 ], [ -95.227515111999878, 30.061534735000066 ], [ -95.222103113999935, 30.0659127360002 ], [ -95.2208031159999, 30.067012734000063 ], [ -95.219703111999934, 30.067812736000182 ], [ -95.218663111999945, 30.06874373200014 ], [ -95.214292112999942, 30.072161729000076 ], [ -95.212799116999804, 30.07332173399999 ], [ -95.211614117999886, 30.074256732000094 ], [ -95.209044112999891, 30.076266731000029 ], [ -95.203988119999906, 30.080220729000192 ], [ -95.198203119999846, 30.085311729000068 ], [ -95.196560118999912, 30.086653728000158 ], [ -95.195717116999901, 30.087315725000167 ], [ -95.191691117999937, 30.090515731000036 ], [ -95.19066112299987, 30.09133872800021 ], [ -95.180616124999929, 30.099369727000067 ], [ -95.173089124999933, 30.105464726000147 ], [ -95.172844904999877, 30.105661433000197 ], [ -95.166556122999907, 30.11072672500023 ], [ -95.165202126999873, 30.11181072100004 ], [ -95.135673130999919, 30.135528715000135 ], [ -95.13460613299992, 30.136328715000047 ], [ -95.133032135999883, 30.137551716000075 ], [ -95.131722133999858, 30.138527714000194 ], [ -95.13097913699994, 30.139297716000154 ], [ -95.130178135999927, 30.14004271500005 ], [ -95.128312136999909, 30.141352713000117 ], [ -95.127067132999912, 30.14237771500018 ], [ -95.126029136999932, 30.143251715000186 ], [ -95.125127134999914, 30.144033715000035 ], [ -95.124563781999939, 30.144427656000065 ], [ -95.124299136999866, 30.144612717000204 ], [ -95.1235921359999, 30.145349716000108 ], [ -95.122160138999902, 30.14654371500006 ], [ -95.119071138999914, 30.149112713000193 ], [ -95.117850137999881, 30.150096714000142 ], [ -95.116306133999842, 30.151342711000151 ], [ -95.115443137999932, 30.152079710000066 ], [ -95.096701142999905, 30.167208706000054 ], [ -95.095401144999926, 30.163608706000105 ], [ -95.09305414499994, 30.158087708000156 ], [ -95.087047145999861, 30.14200871200012 ], [ -95.086648144999913, 30.140941715 ], [ -95.085615148999864, 30.138174712000136 ], [ -95.085242145999928, 30.137176715000066 ], [ -95.084585147999917, 30.135493714000173 ], [ -95.083646143999943, 30.132852714000141 ], [ -95.073278151999887, 30.103703720000112 ], [ -95.073028374999922, 30.103076085000112 ], [ -95.064397152999845, 30.081387725000123 ], [ -95.062886150999873, 30.077580723000043 ], [ -95.056874154999946, 30.061074729000037 ], [ -95.049702159999924, 30.041757733000285 ], [ -95.049635018999936, 30.041590597000098 ], [ -95.048482156999853, 30.038720734000094 ], [ -95.048395622999806, 30.038509688000207 ], [ -95.047387856999933, 30.03605186700014 ], [ -95.046208161999914, 30.033174734 ], [ -95.045125157999905, 30.029203734000077 ], [ -95.04460616299987, 30.028043737000132 ], [ -95.042363160999912, 30.022359739000194 ], [ -95.039768162999906, 30.015681738000183 ], [ -95.035685165999894, 30.005058741000088 ], [ -95.031070162999811, 29.993052745000057 ], [ -95.030612166999859, 29.993990744000147 ], [ -95.029331164999917, 29.994330739000134 ], [ -95.028476169999919, 29.994993741000084 ], [ -95.027759165999839, 29.996023743000197 ], [ -95.026645168999949, 29.996565741000097 ], [ -95.025424165999937, 29.996817739000164 ], [ -95.024555167999949, 29.997610738000166 ], [ -95.023181169999873, 29.997084742000141 ], [ -95.021915164999939, 29.997042741000147 ], [ -95.020633170999929, 29.997355744 ], [ -95.019870165999919, 29.99650874200012 ], [ -95.018863167999939, 29.99639374200018 ], [ -95.017643170999861, 29.996210743000066 ], [ -95.016513167999904, 29.996550744000047 ], [ -95.01539917, 29.996119739000164 ], [ -95.01504917099993, 29.995005742000107 ], [ -95.014530169999944, 29.994028745000147 ], [ -95.013523172999783, 29.994509744000087 ], [ -95.012546168999847, 29.995100740000108 ], [ -95.011585172999901, 29.994780740000124 ], [ -95.01219517099986, 29.993853745000081 ], [ -95.011280170999896, 29.993353741000075 ], [ -95.010211168999945, 29.993406745000019 ], [ -95.008838171999912, 29.993059741000085 ], [ -95.007053171999928, 29.992685741000063 ], [ -95.004519171999902, 29.992521743000168 ], [ -95.000399172999963, 29.991804744000092 ], [ -94.999636173999889, 29.991091742000094 ], [ -95.000338174999911, 29.990069740000081 ], [ -94.999529174999907, 29.989233741000078 ], [ -94.998354173999871, 29.988825741000085 ], [ -94.998919176999891, 29.987814742000182 ], [ -94.998461173999942, 29.986468743000099 ], [ -94.997958173999791, 29.985274744000154 ], [ -94.997548175999896, 29.984088740000114 ], [ -94.996828175999894, 29.982973744000052 ], [ -94.997042173999944, 29.981913745000156 ], [ -94.996630172999858, 29.980539742000072 ], [ -94.995715177999898, 29.979777747000188 ], [ -94.995409176999843, 29.978191742000146 ], [ -94.994509178999863, 29.977473745000172 ], [ -94.993456172999856, 29.977031744000097 ], [ -94.991900176999934, 29.977039745000127 ], [ -94.990725174999852, 29.97667674600018 ], [ -94.989565177999907, 29.976550744000068 ], [ -94.989565177999907, 29.975444743000143 ], [ -94.988284174999876, 29.974960746000139 ], [ -94.98736098299986, 29.974449493000012 ], [ -94.987215174999903, 29.974368746000042 ], [ -94.986315175999948, 29.973895746000149 ], [ -94.985445178999896, 29.972911746000083 ], [ -94.985252599999853, 29.972518310000051 ], [ -94.984910175999858, 29.971818743000142 ], [ -94.984179179999842, 29.967925749000077 ], [ -94.984225180999942, 29.966739745000044 ], [ -94.984835179999948, 29.965938746000063 ], [ -94.983859180999843, 29.965549748000058 ], [ -94.982806182999923, 29.965686746000191 ], [ -94.982088179999948, 29.964832750000085 ], [ -94.981219179999925, 29.963901747000193 ], [ -94.980257177999874, 29.963458747000178 ], [ -94.981173177999835, 29.962665748000177 ], [ -94.981585179999911, 29.961513744000062 ], [ -94.982180181999922, 29.960624749000143 ], [ -94.982607180999935, 29.959464750000169 ], [ -94.983675182999889, 29.959285751000039 ], [ -94.982699176999802, 29.958629750000174 ], [ -94.981631179999908, 29.958850745000063 ], [ -94.980563179999876, 29.959026750000191 ], [ -94.980318182999895, 29.957687745000161 ], [ -94.979250181999873, 29.956649749000064 ], [ -94.980669180999882, 29.955817745000164 ], [ -94.9815851809999, 29.955276752000035 ], [ -94.981905179999899, 29.95431475100014 ], [ -94.983019182999911, 29.953796749000048 ], [ -94.984087177999868, 29.954402747000131 ], [ -94.985674180999922, 29.954059750000056 ], [ -94.986834178999914, 29.953521752000142 ], [ -94.988314175999847, 29.952918749000105 ], [ -94.987856178999891, 29.951808747000083 ], [ -94.988467176999904, 29.950938748000116 ], [ -94.988924180999902, 29.949752750000073 ], [ -94.988161181999885, 29.948909750000098 ], [ -94.988161181999885, 29.947777748000078 ], [ -94.989184180999928, 29.947567750000079 ], [ -94.990252176999888, 29.947163750000072 ], [ -94.991061175999846, 29.94641175300012 ], [ -94.990710179999965, 29.94545475100017 ], [ -94.991625179999915, 29.944832751000039 ], [ -94.990206181999895, 29.944569749000095 ], [ -94.989443176999941, 29.943569753000137 ], [ -94.990557178999893, 29.94264675300013 ], [ -94.991122181999856, 29.941406753000081 ], [ -94.99183917899984, 29.940518749000145 ], [ -94.992846175999958, 29.93938574900017 ], [ -94.994021177999912, 29.938961752000179 ], [ -94.994677177999904, 29.937805756000106 ], [ -94.99559417699993, 29.936466751000069 ], [ -94.995960177999848, 29.935177755000154 ], [ -94.995349179999835, 29.934132750000117 ], [ -94.995090177999884, 29.933064756000022 ], [ -94.995349179999835, 29.931980753000119 ], [ -94.99631118099984, 29.931050754000179 ], [ -94.995197177999785, 29.92929575300019 ], [ -94.995044176999897, 29.928284756000156 ], [ -94.99356418099984, 29.928818753000197 ], [ -94.992541180999922, 29.929440753000108 ], [ -94.991839179999943, 29.928604755000038 ], [ -94.991930178999894, 29.927506755000142 ], [ -94.992602178999903, 29.926399753000055 ], [ -94.994281178999927, 29.925614755000083 ], [ -94.993259177999903, 29.924896758000102 ], [ -94.994083180999894, 29.924118756000045 ], [ -94.995090177999884, 29.924198756000123 ], [ -94.996219177999876, 29.923809754000047 ], [ -94.996768178999901, 29.922860758000073 ], [ -94.997638177999946, 29.921968754000147 ], [ -94.998096180999951, 29.920957756000178 ], [ -94.998554177999893, 29.919999755000049 ], [ -94.997379176999914, 29.919816756000099 ], [ -94.996372178999934, 29.919748755000228 ], [ -94.996463175999907, 29.918695757000137 ], [ -94.996677174999945, 29.917623755000083 ], [ -94.995502179999903, 29.91688776 ], [ -94.994846180999787, 29.915746758000122 ], [ -94.995456178999916, 29.91477375400007 ], [ -94.994174178999856, 29.91414875900017 ], [ -94.993778178999946, 29.913095761000079 ], [ -94.992603178999914, 29.912137760000125 ], [ -94.99306118099986, 29.911092756000183 ], [ -94.992038181999874, 29.910520757000199 ], [ -94.990726182999936, 29.909707756000159 ], [ -94.990360181999847, 29.908677762000199 ], [ -94.990207180999903, 29.907670758000169 ], [ -94.988833178999926, 29.907541760000189 ], [ -94.987917178999794, 29.908315762000083 ], [ -94.987643180999839, 29.907228756000052 ], [ -94.986391179999941, 29.906469757000199 ], [ -94.986803181999903, 29.905465762000176 ], [ -94.987414179999917, 29.904504759000133 ], [ -94.987872181999876, 29.903478759000169 ], [ -94.986605178999866, 29.903623757999981 ], [ -94.985995181999854, 29.90257076000012 ], [ -94.986041181999894, 29.901414762000176 ], [ -94.986697182999933, 29.900335757000168 ], [ -94.986895179999863, 29.89923675700004 ], [ -94.986346184999945, 29.898320758000182 ], [ -94.985674182999901, 29.897527759000187 ], [ -94.984515183999861, 29.897748760000074 ], [ -94.984621184999924, 29.895994759000189 ], [ -94.984057180999912, 29.895009759000057 ], [ -94.98431618199993, 29.893972761000043 ], [ -94.98521718499984, 29.893396764000045 ], [ -94.985018182999909, 29.892072762000112 ], [ -94.985979180999948, 29.891561762000094 ], [ -94.98521718499984, 29.890494760000138 ], [ -94.984209183999837, 29.890363763000039 ], [ -94.985033179999903, 29.889361765000107 ], [ -94.98521718499984, 29.887816764000036 ], [ -94.984301179999932, 29.886900764000128 ], [ -94.983187182999927, 29.887259762000159 ], [ -94.982119181999849, 29.886771766000091 ], [ -94.98089818599982, 29.886465763999979 ], [ -94.980517181999915, 29.885466761000202 ], [ -94.980318185999863, 29.884211765000085 ], [ -94.981478184999844, 29.884089763000134 ], [ -94.982546185999922, 29.884390764999981 ], [ -94.983370182999806, 29.883631766000178 ], [ -94.98265318499989, 29.882655761000137 ], [ -94.982455182999857, 29.881602762000171 ], [ -94.981539182999938, 29.881193762000066 ], [ -94.980364180999857, 29.881438764999988 ], [ -94.979754182999841, 29.882433762000172 ], [ -94.978487184999892, 29.882578761000165 ], [ -94.97731218499986, 29.882769761000194 ], [ -94.977724186999922, 29.881853763000041 ], [ -94.978274185999851, 29.880808764 ], [ -94.97883818299988, 29.879820764000154 ], [ -94.977419183999928, 29.879900765000112 ], [ -94.976397183999893, 29.879778763000164 ], [ -94.97731218499986, 29.878977763000137 ], [ -94.978442182999913, 29.87875276200009 ], [ -94.978487185999882, 29.87773776400013 ], [ -94.977618185999859, 29.877150764000024 ], [ -94.976504183999793, 29.877394767000055 ], [ -94.976504183999793, 29.876200764000149 ], [ -94.976656184999925, 29.875162767000123 ], [ -94.97604618499993, 29.874152768000158 ], [ -94.975893185999951, 29.872851765000064 ], [ -94.975939187999941, 29.871840767000098 ], [ -94.976855186999899, 29.871294768000098 ], [ -94.977114187999931, 29.870276767000178 ], [ -94.976458187999881, 29.869391766000035 ], [ -94.976916184999936, 29.868414768000033 ], [ -94.976046185999905, 29.867792769000175 ], [ -94.975802188999864, 29.866808768000165 ], [ -94.974780187999897, 29.866057764000061 ], [ -94.973513183999842, 29.865618764000143 ], [ -94.972643186999903, 29.864958770000044 ], [ -94.972459900999922, 29.864691652000147 ], [ -94.972033187999784, 29.864069768000036 ], [ -94.971072184999855, 29.863585766000142 ], [ -94.970568186999913, 29.862463768000055 ], [ -94.970461186999898, 29.861304769000132 ], [ -94.969592188999854, 29.860796771000029 ], [ -94.968829188999905, 29.859843769000065 ], [ -94.967892233999919, 29.859131381000115 ], [ -94.967715185999907, 29.858996768000054 ], [ -94.966601188999846, 29.858374767000043 ], [ -94.965426188999857, 29.857951769000181 ], [ -94.964358186999903, 29.857592771000157 ], [ -94.963198188999911, 29.857245767000052 ], [ -94.962069189999852, 29.856882768000105 ], [ -94.960864188999892, 29.856589767000116 ], [ -94.960407876999923, 29.856612784000166 ], [ -94.959734189999836, 29.856646765000054 ], [ -94.958666188999928, 29.85655476900007 ], [ -94.957858192999936, 29.855948769000182 ], [ -94.95683519399995, 29.856123769000135 ], [ -94.955767191999939, 29.856154768000124 ], [ -94.954455192999944, 29.854754767000145 ], [ -94.953494190999834, 29.853995768000175 ], [ -94.952486193999917, 29.854403769000101 ], [ -94.951418192999938, 29.854689767000135 ], [ -94.950396191999914, 29.854437768000135 ], [ -94.949587191999854, 29.853701769000114 ], [ -94.949175195999899, 29.852396770000041 ], [ -94.948519196999939, 29.851557769000127 ], [ -94.947710195999889, 29.850745768000195 ], [ -94.946733191999897, 29.850298766000154 ], [ -94.946641195999931, 29.849200771000199 ], [ -94.946031196999911, 29.848338767000087 ], [ -94.945772194999861, 29.847254770000177 ], [ -94.94577219599995, 29.845924772000107 ], [ -94.944703195999864, 29.845252770000119 ], [ -94.943437195999934, 29.844978771000058 ], [ -94.944658192999896, 29.844455770000085 ], [ -94.94520719399992, 29.843517771000165 ], [ -94.943849193999938, 29.843170770000143 ], [ -94.942567191999899, 29.843109771000172 ], [ -94.941408193999905, 29.842719769000041 ], [ -94.939836192999906, 29.842582769000046 ], [ -94.938875196999845, 29.842235772000038 ], [ -94.939271195999936, 29.841224773000132 ], [ -94.939683197999841, 29.840293771000063 ], [ -94.940843196999879, 29.840286769000159 ], [ -94.940951060999851, 29.840076958000115 ], [ -94.941408193999905, 29.839187768000102 ], [ -94.941560195999898, 29.838115773000144 ], [ -94.942170193999914, 29.837261770000055 ], [ -94.941041193999922, 29.836696774000078 ], [ -94.939927196999861, 29.836440773999982 ], [ -94.938768198999867, 29.836368773000174 ], [ -94.937951982999948, 29.837030609000067 ], [ -94.937700196999913, 29.837234772000159 ], [ -94.937501194999868, 29.83868476900011 ], [ -94.93643319899985, 29.839073772000066 ], [ -94.935472197999843, 29.838455774000124 ], [ -94.935060195999952, 29.83708977200007 ], [ -94.935411199999862, 29.836120775000101 ], [ -94.936128196999846, 29.835349774000179 ], [ -94.935548198999925, 29.834400773000084 ], [ -94.9369521939999, 29.834106774000134 ], [ -94.936738194999862, 29.833007772000201 ], [ -94.935747198999934, 29.832446773000019 ], [ -94.934739197999932, 29.832336771000147 ], [ -94.933946197999887, 29.83327477000006 ], [ -94.933030198999859, 29.834209772000175 ], [ -94.931962197999951, 29.83491877 ], [ -94.932222196999874, 29.835929774000192 ], [ -94.931108200999915, 29.836284772000056 ], [ -94.930192199999851, 29.835719770000196 ], [ -94.929277198999841, 29.834987775000119 ], [ -94.929063200999906, 29.833812774000194 ], [ -94.928743200999818, 29.832618769000081 ], [ -94.92769019699989, 29.833312769000091 ], [ -94.9266832, 29.833537771000067 ], [ -94.926790199999857, 29.832515771000033 ], [ -94.925874198999907, 29.831989773000036 ], [ -94.925050201999852, 29.832950769000146 ], [ -94.92396720399995, 29.833568769000124 ], [ -94.9231582029999, 29.834400773000084 ], [ -94.922304199999928, 29.83379777000016 ], [ -94.921464200999878, 29.833095769000064 ], [ -94.921312198999942, 29.832034770000124 ], [ -94.921922203999941, 29.831188773000068 ], [ -94.920946198999857, 29.830829774000112 ], [ -94.919938202999859, 29.8310127750002 ], [ -94.919328203999896, 29.829906774000104 ], [ -94.918153202999875, 29.829276773000117 ], [ -94.917299199999889, 29.83003676900012 ], [ -94.917802198999937, 29.831142772000078 ], [ -94.916536200999928, 29.831188773000068 ], [ -94.915208204999885, 29.83087577100008 ], [ -94.915056202999949, 29.829845775000134 ], [ -94.91369820299991, 29.829688773000104 ], [ -94.912828203999879, 29.830554772000081 ], [ -94.912385203999918, 29.829555775000134 ], [ -94.91162120599995, 29.828697772000055 ], [ -94.910797202999902, 29.827911775000135 ], [ -94.910551232999865, 29.827759494000077 ], [ -94.909928202999936, 29.827373771000058 ], [ -94.908860207999851, 29.82675577200018 ], [ -94.907914201999915, 29.825908770000066 ], [ -94.90776120799984, 29.824886776000085 ], [ -94.908982203999926, 29.825199773000122 ], [ -94.909378203999893, 29.824176773000204 ], [ -94.91023320599993, 29.823329773000125 ], [ -94.909684204999905, 29.822448772000143 ], [ -94.909835201999783, 29.821438772000079 ], [ -94.909974080999916, 29.820996101000109 ], [ -94.910013964999905, 29.820868971000095 ], [ -94.910187204999943, 29.820316775000041 ], [ -94.910797203999891, 29.819251775 ], [ -94.91104220599982, 29.818225774000158 ], [ -94.910736204999921, 29.817085778000145 ], [ -94.910645207999835, 29.81583377600009 ], [ -94.909775202999867, 29.815082773000029 ], [ -94.908768204999944, 29.815082773000029 ], [ -94.908921204999899, 29.813922775000151 ], [ -94.910034203999885, 29.813705775000134 ], [ -94.91104220599982, 29.813518774000045 ], [ -94.910660204999942, 29.81245477400012 ], [ -94.911622205999947, 29.811954775000061 ], [ -94.911714201999928, 29.813075775000073 ], [ -94.912767204999909, 29.813335775000102 ], [ -94.913392201999841, 29.812446779000084 ], [ -94.912629203999927, 29.811561777000069 ], [ -94.91237020199992, 29.810569776000136 ], [ -94.913591203999943, 29.810474778000074 ], [ -94.912874206999788, 29.809280774000062 ], [ -94.912721204999912, 29.808269776000088 ], [ -94.913835201999916, 29.807872777000171 ], [ -94.914644202999867, 29.807250777000039 ], [ -94.914694439999948, 29.807209790000112 ], [ -94.915513202999932, 29.806541780000092 ], [ -94.916017200999875, 29.80561877800011 ], [ -94.917024204999791, 29.804962778000345 ], [ -94.916765203999944, 29.803855777000138 ], [ -94.917268202999836, 29.802960778000116 ], [ -94.918290202999913, 29.803051776000135 ], [ -94.919343201999823, 29.802525779000067 ], [ -94.921266205999927, 29.802891779000163 ], [ -94.92248720099991, 29.802891779000163 ], [ -94.92318920199989, 29.801999776000173 ], [ -94.922532202999889, 29.800774780000044 ], [ -94.922624201999838, 29.799477776 ], [ -94.923433200999909, 29.798653779000176 ], [ -94.922777201999907, 29.797505782000091 ], [ -94.923387198999933, 29.796433780000029 ], [ -94.924348201999862, 29.795685779000056 ], [ -94.923890198999914, 29.794716781000201 ], [ -94.922578204999866, 29.794457779000084 ], [ -94.921159199999863, 29.793965782000157 ], [ -94.920106200999896, 29.794579782000142 ], [ -94.919847199999936, 29.793400780000127 ], [ -94.920701202999908, 29.792553779000112 ], [ -94.921663204999845, 29.792260777000191 ], [ -94.920350205999924, 29.791676778000127 ], [ -94.91945020199995, 29.790577778000113 ], [ -94.918580203999909, 29.789383780000097 ], [ -94.917879206999942, 29.788639779000107 ], [ -94.918687202999877, 29.78766777900012 ], [ -94.917833205999841, 29.786828778000086 ], [ -94.918229205999921, 29.785737780000204 ], [ -94.918275200999915, 29.784581782000146 ], [ -94.918428201999859, 29.783562782000047 ], [ -94.919649203999825, 29.783036780000202 ], [ -94.920762201999878, 29.782807785000159 ], [ -94.921510204999947, 29.78185778600016 ], [ -94.920152203999862, 29.781048784 ], [ -94.919948202999876, 29.78004178100014 ], [ -94.920152203999862, 29.778862786000165 ], [ -94.919847200999868, 29.7777527830001 ], [ -94.920305203999931, 29.776616781000033 ], [ -94.920305203999931, 29.77550978600004 ], [ -94.919282204999945, 29.77510178600005 ], [ -94.918123205999848, 29.775109786000144 ], [ -94.917116201999931, 29.774617783000057 ], [ -94.916459203999921, 29.773648785000148 ], [ -94.916176204999886, 29.772311785 ], [ -94.91681520599991, 29.771388783000191 ], [ -94.917951201999927, 29.771652784000139 ], [ -94.917375202999949, 29.770772787000226 ], [ -94.916513441999882, 29.770037960000082 ], [ -94.916506202999926, 29.770031787 ], [ -94.915303206999852, 29.769532788000049 ], [ -94.914247205999857, 29.769140788000126 ], [ -94.913365205999938, 29.76809478500013 ], [ -94.913428206999868, 29.766700786000172 ], [ -94.914654208999877, 29.766254785000172 ], [ -94.916209208999931, 29.76689578800017 ], [ -94.918505201999949, 29.765847789000016 ], [ -94.919198203999883, 29.764919787000171 ], [ -94.918585202999907, 29.763496787 ], [ -94.917955202999906, 29.762276783000178 ], [ -94.917776202999903, 29.761032784000179 ], [ -94.918467207999925, 29.759768788000201 ], [ -94.918321202999891, 29.75702079000007 ], [ -94.918626204999896, 29.755739788000085 ], [ -94.919893202999859, 29.755117788000181 ], [ -94.923173202999919, 29.755220787000042 ], [ -94.925599205999916, 29.752923789000079 ], [ -94.927501202999906, 29.751922789000044 ], [ -94.928555199999948, 29.751520791000189 ], [ -94.929796204999889, 29.750528790000093 ], [ -94.930238204999853, 29.748765790000046 ], [ -94.930192203999866, 29.747011788000012 ], [ -94.929277202999913, 29.74467379400005 ], [ -94.928880204999928, 29.741137794000057 ], [ -94.928544204999923, 29.739874790000158 ], [ -94.928392202999873, 29.738829793000093 ], [ -94.928697204999878, 29.73785379300017 ], [ -94.92981120099995, 29.736784793000201 ], [ -94.930772203999879, 29.736178795000345 ], [ -94.931795202999936, 29.735064793000159 ], [ -94.932496205999882, 29.734183791000078 ], [ -94.933717200999922, 29.73334479100015 ], [ -94.934678203999795, 29.732802792000143 ], [ -94.935792200999856, 29.732058790000053 ], [ -94.9365552, 29.731272794 ], [ -94.937013202999879, 29.730189796000104 ], [ -94.938127199999883, 29.728617794000119 ], [ -94.939393198999824, 29.727229793999982 ], [ -94.939851200999897, 29.726138796000324 ], [ -94.940660201999947, 29.725527792000033 ], [ -94.94162119799995, 29.724852795000064 ], [ -94.942323199999862, 29.723311793000107 ], [ -94.942170198999918, 29.721621798000172 ], [ -94.943040197999963, 29.720080797000097 ], [ -94.944399196999939, 29.718741798000057 ], [ -94.945162202999938, 29.718009798000079 ], [ -94.945186418999825, 29.717951726000084 ], [ -94.945574198999907, 29.717021798000022 ], [ -94.944918197999925, 29.715838796000018 ], [ -94.943788200999848, 29.714964797000111 ], [ -94.943040197999963, 29.713992797000174 ], [ -94.942018202999861, 29.713454799000033 ], [ -94.940095201999895, 29.713889799000189 ], [ -94.938325203999909, 29.714472800000181 ], [ -94.936448201999781, 29.714907799000173 ], [ -94.935289201999808, 29.715182795000207 ], [ -94.934022203999859, 29.715132794000056 ], [ -94.932908200999805, 29.714835798000195 ], [ -94.932451203999904, 29.713687795000173 ], [ -94.933259204999899, 29.712523796000198 ], [ -94.934373201999847, 29.71143279800015 ], [ -94.934678204999898, 29.710181796000029 ], [ -94.934373201999847, 29.709219800000199 ], [ -94.933854201999907, 29.70798480000019 ], [ -94.93370220699984, 29.706973796000167 ], [ -94.933107203999839, 29.705546796000078 ], [ -94.932283205999852, 29.704745796000054 ], [ -94.929902205999838, 29.704535798000052 ], [ -94.928285204999895, 29.704443802000128 ], [ -94.926362206999897, 29.704306797000072 ], [ -94.925203207999914, 29.70312079900015 ], [ -94.925798203999932, 29.701693799000054 ], [ -94.926713204999885, 29.70107179900009 ], [ -94.927736202999881, 29.700511803000101 ], [ -94.929094204999899, 29.70038180000012 ], [ -94.929963204999922, 29.699690801000202 ], [ -94.930009206999898, 29.698168803000048 ], [ -94.928789201999791, 29.697333803000053 ], [ -94.927629204999903, 29.697054799000057 ], [ -94.925752208999938, 29.697073803000023 ], [ -94.924745205999841, 29.697600799000096 ], [ -94.924226203999808, 29.69887879800007 ], [ -94.923326205999842, 29.699629802000061 ], [ -94.921495204999871, 29.699847802000136 ], [ -94.920228207999855, 29.699709798000075 ], [ -94.919313206999902, 29.699137800000187 ], [ -94.918153208999854, 29.698756802000108 ], [ -94.916841207999951, 29.69919180200003 ], [ -94.91577220899984, 29.699069799000142 ], [ -94.915573206999909, 29.697806802 ], [ -94.916275208999878, 29.696147799000187 ], [ -94.917192205999925, 29.695334798000147 ], [ -94.918153208999854, 29.694850802000076 ], [ -94.92027421, 29.694315800000197 ], [ -94.921235204999846, 29.693675801000097 ], [ -94.921754205999903, 29.692751801000043 ], [ -94.921449209999878, 29.691134803000185 ], [ -94.92012220799991, 29.689337804000051 ], [ -94.919664210999827, 29.688448802000043 ], [ -94.919862207999927, 29.686850801999984 ], [ -94.920427208999911, 29.685797802000081 ], [ -94.921602204999942, 29.684683801 ], [ -94.922410206999928, 29.683894802 ], [ -94.924180209999918, 29.682791803000161 ], [ -94.926985949999903, 29.681283533000059 ], [ -94.926986769999871, 29.681283093000051 ], [ -94.926013607999892, 29.684885732000112 ], [ -94.927987703999918, 29.686028054000303 ], [ -94.931235831999928, 29.685074677000042 ], [ -94.930548984999916, 29.681002363000061 ], [ -94.933254635999845, 29.678925326000073 ], [ -94.935581205999895, 29.679593143000147 ], [ -94.937247204999892, 29.680689161000092 ], [ -94.936007705999941, 29.684141803000102 ], [ -94.936318765999886, 29.687056912000173 ], [ -94.9360452, 29.689322801000174 ], [ -94.936155312999858, 29.691849248000043 ], [ -94.937855449999859, 29.694277049999982 ], [ -94.93998720299993, 29.695241804000094 ], [ -94.94168420099993, 29.696538302000075 ], [ -94.943077699999947, 29.697975792000136 ], [ -94.946683794999899, 29.698737399000041 ], [ -94.949662699999806, 29.698924801000089 ], [ -94.954682196999912, 29.698871803000149 ], [ -94.956845198999872, 29.698917548999987 ], [ -94.960007196999868, 29.6994052030002 ], [ -94.964926191999893, 29.700142046000163 ], [ -94.968438528999911, 29.700455962000039 ], [ -94.971626768999897, 29.701405516000079 ], [ -94.974566925999909, 29.703157527000144 ], [ -94.975761639999916, 29.705990915000143 ], [ -94.979352684999924, 29.708792291000123 ], [ -94.983160691999913, 29.70957454500018 ], [ -94.985735688999853, 29.709061797000178 ], [ -94.989244853999935, 29.709005581000156 ], [ -94.988820903999851, 29.711944580000079 ], [ -94.990253901999949, 29.713374456000054 ], [ -94.990490614999828, 29.71361065300016 ], [ -94.988745730999938, 29.715743720000148 ], [ -94.9877266069999, 29.718312884000113 ], [ -94.987454689999879, 29.721087992000037 ], [ -94.990028261999839, 29.721368519000066 ], [ -94.991671487999895, 29.722043326000062 ], [ -94.992805104999945, 29.720010131000091 ], [ -94.990652435999891, 29.717804045000037 ], [ -94.991592628999854, 29.715615136000167 ], [ -94.992663095999887, 29.713459108000166 ], [ -94.993167663999884, 29.712442858000092 ], [ -94.995212380999874, 29.710239097000056 ], [ -94.99609098399992, 29.713313001000017 ], [ -94.998623066999926, 29.712631855000158 ], [ -95.000700725999934, 29.711200264000123 ], [ -95.002215010999919, 29.711828098000183 ], [ -95.003770326999927, 29.712472945000172 ], [ -94.998489738999808, 29.715727492000045 ], [ -95.001920957999914, 29.717830496000147 ], [ -95.006237651999868, 29.72115103200008 ], [ -95.014096249999852, 29.721483085000159 ], [ -95.016199255999936, 29.718715973000144 ], [ -95.016199255999936, 29.714067226000168 ], [ -95.015977886999849, 29.71152148200002 ], [ -95.018359701999955, 29.707662695000064 ], [ -95.018823182999881, 29.709587302000045 ], [ -95.01948431999989, 29.711531082000132 ], [ -95.01895877499993, 29.714730254000131 ], [ -95.018528558999947, 29.717648963000155 ], [ -95.017505946999904, 29.720279682000125 ], [ -95.019566679999869, 29.722535797000035 ], [ -95.02077667899988, 29.725314295000032 ], [ -95.020537980999848, 29.728342093000261 ], [ -95.021051181999951, 29.732288379000128 ], [ -95.025331183999882, 29.732345797000107 ], [ -95.02736367499989, 29.732793796000127 ], [ -95.028744509999854, 29.735488628000155 ], [ -95.029074749999893, 29.738961790000129 ], [ -95.027694177999877, 29.741237130000059 ], [ -95.030300172999944, 29.745730429000162 ], [ -95.032477755999935, 29.747566369000193 ], [ -95.035395775999859, 29.749762458000077 ], [ -95.039057757999899, 29.750125127000157 ], [ -95.043288880999853, 29.749563837000096 ], [ -95.04531942299991, 29.748541297000205 ], [ -95.047144116999903, 29.747652794000143 ], [ -95.049332894999907, 29.745168067000183 ], [ -95.050895987999922, 29.743712427000162 ], [ -95.052965454999821, 29.745584503000146 ], [ -95.05400463299992, 29.747653874000036 ], [ -95.053657294999937, 29.749992668000118 ], [ -95.051975674999881, 29.751757541000018 ], [ -95.050318725999944, 29.753954234000162 ], [ -95.051243837999834, 29.757003275000102 ], [ -95.048701165999944, 29.757791459000146 ], [ -95.044324169999925, 29.760184789000107 ], [ -95.04250051899993, 29.762253600000122 ], [ -95.043331248999891, 29.765347487000042 ], [ -95.042259673999865, 29.768403457000094 ], [ -95.042448730999809, 29.771084566000187 ], [ -95.043468173999941, 29.773743992000103 ], [ -95.04517706799993, 29.777223185000143 ], [ -95.047175172999914, 29.778375286000145 ], [ -95.048858514999893, 29.77820836 ], [ -95.051201250999839, 29.779028698000051 ], [ -95.054212165999843, 29.779566539000026 ], [ -95.05812085999986, 29.778482290000113 ], [ -95.06263634599992, 29.778301672000111 ], [ -95.062469450999856, 29.780471303000127 ], [ -95.062455725999897, 29.780649724000057 ], [ -95.066609972999913, 29.782997777000105 ], [ -95.064081300999931, 29.78516521000012 ], [ -95.061372008999911, 29.784803971000148 ], [ -95.060107672999948, 29.789500076000134 ], [ -95.060288291999882, 29.791667510000028 ], [ -95.059988486999956, 29.791872640000122 ], [ -95.056856521999805, 29.794015562000141 ], [ -95.056856521999805, 29.798892287000175 ], [ -95.06191386699993, 29.804852729000064 ], [ -95.065706874999933, 29.807923261000099 ], [ -95.06642935299989, 29.81207750800019 ], [ -95.068596785999887, 29.813522464000187 ], [ -95.072931652999898, 29.813341844000146 ], [ -95.076182801999948, 29.813883703000158 ], [ -95.07798899699992, 29.812799986000076 ], [ -95.081420765999837, 29.81027131400009 ], [ -95.081412674999854, 29.810357620000165 ], [ -95.080878907999931, 29.816051137000045 ], [ -95.076905279999963, 29.81966352500018 ], [ -95.077266517999931, 29.823275914000192 ], [ -95.074557226999843, 29.825443347000206 ], [ -95.074730835999915, 29.82573268900018 ], [ -95.07509908499992, 29.826346444000027 ], [ -95.079614571999855, 29.825623967000016 ], [ -95.081240146999903, 29.820747242000039 ], [ -95.082504481999877, 29.821108481000184 ], [ -95.082504481999877, 29.823817773000147 ], [ -95.086116870999888, 29.822914675000046 ], [ -95.087019968999925, 29.81984414499999 ], [ -95.089187401999936, 29.820024764000095 ], [ -95.09460598499993, 29.823817773000147 ], [ -95.094786604999854, 29.820566623000108 ], [ -95.094064126999854, 29.817676711999983 ], [ -95.092980410999871, 29.814786800000149 ], [ -95.095509081999865, 29.811716270000151 ], [ -95.09460598599992, 29.803949634 ], [ -95.090271118999908, 29.799795386000195 ], [ -95.089132710999934, 29.799620246000075 ], [ -95.08322696099988, 29.79871166900017 ], [ -95.075821563999909, 29.797989191000053 ], [ -95.06660997199981, 29.797627952000088 ], [ -95.064081300999931, 29.794557422000082 ], [ -95.063873780999813, 29.793623581000134 ], [ -95.06335882299993, 29.791306271000114 ], [ -95.067151831999865, 29.787693881000166 ], [ -95.07130607799985, 29.789861316000156 ], [ -95.076182802999895, 29.790222555000131 ], [ -95.083588199999838, 29.787874503000182 ], [ -95.092257933999804, 29.785707068000189 ], [ -95.099663331999864, 29.784984591000182 ], [ -95.101108285999942, 29.777940434000072 ], [ -95.10165014599994, 29.771799371000103 ], [ -95.102292280999848, 29.768680426 ], [ -95.099177334999865, 29.763997355000068 ], [ -95.096412181999881, 29.762768399000148 ], [ -95.095249277999926, 29.763878992000116 ], [ -95.09220115699992, 29.764120791000153 ], [ -95.089851156999885, 29.762921038000115 ], [ -95.092001156999856, 29.759621793000068 ], [ -95.09260115799988, 29.756522793000162 ], [ -95.093055054999923, 29.755556193000075 ], [ -95.093481153999846, 29.754648788000058 ], [ -95.095451157999946, 29.752221792000174 ], [ -95.099101158999872, 29.750921795000181 ], [ -95.101775153999881, 29.749011796000165 ], [ -95.106310772999919, 29.749233060000169 ], [ -95.103429602999839, 29.747530411000071 ], [ -95.104720677999879, 29.744706454000095 ], [ -95.108333064999897, 29.742177782000113 ], [ -95.116280321999852, 29.740190969000139 ], [ -95.122963240999923, 29.73748167500014 ], [ -95.128020584999945, 29.735314244000111 ], [ -95.132897309999919, 29.735314244000111 ], [ -95.13524536299991, 29.737481676000019 ], [ -95.139580228999932, 29.737481676000019 ], [ -95.145645003999903, 29.737763759000188 ], [ -95.147346864999918, 29.737842915000154 ], [ -95.149694917999852, 29.7382041540003 ], [ -95.152946069999928, 29.73820415400013 ], [ -95.155655360999901, 29.740010349999977 ], [ -95.155474739999818, 29.742539022000127 ], [ -95.15872589099979, 29.743983978000134 ], [ -95.160170843999879, 29.743983978000134 ], [ -95.166673145999937, 29.747415748000041 ], [ -95.16854210799994, 29.74762341100012 ], [ -95.169924294999873, 29.747776987000179 ], [ -95.169664073999854, 29.746996324000179 ], [ -95.169201816999873, 29.745609553000122 ], [ -95.165589428999851, 29.742900262000035 ], [ -95.161435181999934, 29.740732828000091 ], [ -95.153668545999949, 29.734411146000184 ], [ -95.148249962999842, 29.734953006000072 ], [ -95.146042859999852, 29.734617142000157 ], [ -95.139941467999847, 29.733688668000074 ], [ -95.131632973999785, 29.732243714000052 ], [ -95.124227576999886, 29.732243713000173 ], [ -95.119350851999911, 29.735133623000195 ], [ -95.113932268999918, 29.73766229600005 ], [ -95.107791207999924, 29.738023535000195 ], [ -95.104178817999923, 29.740190968000039 ], [ -95.10092766899993, 29.74434521500012 ], [ -95.098579616999814, 29.742900259000123 ], [ -95.095870323999918, 29.743442118000079 ], [ -95.09713466099987, 29.745790171000124 ], [ -95.097065292999844, 29.749117430000179 ], [ -95.093704493999951, 29.750894462000133 ], [ -95.091217360999849, 29.753704595000045 ], [ -95.089926571999911, 29.755941207000149 ], [ -95.088636162999876, 29.759863906000078 ], [ -95.085445493999941, 29.761917955000115 ], [ -95.081875158999935, 29.762432457000102 ], [ -95.080437428999915, 29.762150258000077 ], [ -95.079491386999905, 29.761964567000064 ], [ -95.077613161999921, 29.760925791000094 ], [ -95.076143668999919, 29.759948043000065 ], [ -95.07332632899994, 29.758481289000201 ], [ -95.070187900999883, 29.755424582000163 ], [ -95.068285161999938, 29.752858105000147 ], [ -95.06658335599991, 29.749189981000033 ], [ -95.065081169999871, 29.746298933000105 ], [ -95.064342009999905, 29.74217250300012 ], [ -95.068004726999902, 29.741175353000187 ], [ -95.066843671999948, 29.738981044000131 ], [ -95.064051721999874, 29.737717023000073 ], [ -95.061825526999883, 29.735201704000016 ], [ -95.059527498999898, 29.732242707000069 ], [ -95.056853898999918, 29.729711801000118 ], [ -95.058414788999926, 29.727933416000155 ], [ -95.056930837999914, 29.727070124000139 ], [ -95.053552700999887, 29.728888132000069 ], [ -95.051017109999918, 29.727383463000194 ], [ -95.053097561999891, 29.724619462000195 ], [ -95.054156287999945, 29.721794908999982 ], [ -95.05616509399988, 29.719521336000074 ], [ -95.057993663999923, 29.717159805000168 ], [ -95.058767836999948, 29.714510909000015 ], [ -95.058666835999929, 29.711381133000092 ], [ -95.058275838999919, 29.708859913000193 ], [ -95.057156418999924, 29.706791300000191 ], [ -95.055010672999913, 29.705484180000042 ], [ -95.05233676999984, 29.704789207000143 ], [ -95.049744602999851, 29.704454947000045 ], [ -95.04750992299995, 29.704051300000117 ], [ -95.044833739999945, 29.703805516000219 ], [ -95.038537878999875, 29.704125796000085 ], [ -95.035822842999949, 29.704308133000158 ], [ -95.034019061999913, 29.703775357000151 ], [ -95.031435178999914, 29.701242605000171 ], [ -95.027529552999852, 29.699168361000147 ], [ -95.026437931999851, 29.699352753000142 ], [ -95.025125279999884, 29.697876460000089 ], [ -95.024168538999845, 29.696800447000157 ], [ -95.02627154299995, 29.694808126000055 ], [ -95.02627154299995, 29.686174736 ], [ -95.02195484899994, 29.683296939 ], [ -95.020626635999918, 29.698128660000176 ], [ -95.023172377999856, 29.697907291000089 ], [ -95.02317055899988, 29.698347083000041 ], [ -95.023164110999915, 29.699905755000028 ], [ -95.021428816999844, 29.702424259000058 ], [ -95.019422337999913, 29.701578306000158 ], [ -95.018332848999933, 29.701118966000138 ], [ -95.015010466999911, 29.69971495100009 ], [ -95.011585978999904, 29.698940003000075 ], [ -95.009088850999944, 29.698658142000117 ], [ -95.004772063999837, 29.698088673000143 ], [ -95.000760041999911, 29.69669708400005 ], [ -94.998693470999868, 29.694360371000247 ], [ -94.996501686999807, 29.692562300000077 ], [ -94.994501617999802, 29.691062951000102 ], [ -94.991851433999898, 29.689342804000152 ], [ -94.989626883999847, 29.686407339000141 ], [ -94.992137716999935, 29.684156420000193 ], [ -94.995444799999916, 29.683725060000199 ], [ -95.000812823999922, 29.682958200000144 ], [ -95.002474381999889, 29.685289261000033 ], [ -95.005684231999908, 29.68683884300015 ], [ -95.009668872999896, 29.685953367000085 ], [ -95.008672712999896, 29.679201614000192 ], [ -95.001568481999811, 29.68112785500017 ], [ -94.990028066999912, 29.68224780800017 ], [ -94.987753356999917, 29.681435306000083 ], [ -94.985622187999866, 29.681539803000131 ], [ -94.983290919999902, 29.68074735600004 ], [ -94.982357282999942, 29.67778434600018 ], [ -94.985874691999925, 29.674975304000103 ], [ -94.988703192999878, 29.672987807000023 ], [ -94.991227017999904, 29.671074309000179 ], [ -94.993167527999901, 29.669885475000054 ], [ -94.994098188999885, 29.669365809000055 ], [ -94.99510518599989, 29.668483809000069 ], [ -94.99588318799988, 29.667118807000126 ], [ -95.002323189999913, 29.662877810000111 ], [ -95.002872190999938, 29.662038810000187 ], [ -95.004367219999835, 29.660294832000087 ], [ -95.00539118699993, 29.659360810000067 ], [ -95.006976686999906, 29.65718030700009 ], [ -95.007740184999932, 29.655938810000123 ], [ -95.008279988999902, 29.654910208999979 ], [ -95.009313543999895, 29.65328460700016 ], [ -95.010043521999933, 29.652136478999982 ], [ -95.010562186999948, 29.651265812000194 ], [ -95.011371188999874, 29.650147813000103 ], [ -95.011890183999924, 29.648957816000173 ], [ -95.012637824999914, 29.647358286000159 ], [ -95.013291933999938, 29.645945627000064 ], [ -95.013775185999918, 29.644847814000116 ], [ -95.01398518499991, 29.643856814000173 ], [ -95.014347729999884, 29.642344 ], [ -95.014858644999961, 29.641016614000197 ], [ -95.015324181999915, 29.639882813000099 ], [ -95.01578218599991, 29.638692815000187 ], [ -95.015935185999865, 29.637326813000133 ], [ -95.015732517999879, 29.635788636000147 ], [ -95.015599184999871, 29.634679316000021 ], [ -95.015324182999848, 29.633473816000105 ], [ -95.015037822999943, 29.632374817000027 ], [ -95.014705185999901, 29.631211817000125 ], [ -95.014008150999928, 29.630225044000099 ], [ -95.013492182999869, 29.629188816000124 ], [ -95.012744186999896, 29.628350820000097 ], [ -95.009681042999944, 29.625136680000082 ], [ -95.008899186999884, 29.623864820000165 ], [ -95.007892188999918, 29.622914820000066 ], [ -95.006417018999855, 29.621885576000185 ], [ -95.00448918799988, 29.620392822000152 ], [ -95.002933189999908, 29.619240818000041 ], [ -95.001971504999915, 29.618681543000037 ], [ -95.000949188999925, 29.617818816000128 ], [ -94.999989907999918, 29.617505951000116 ], [ -94.999031511999931, 29.616920244000031 ], [ -94.997165486999904, 29.615595179000081 ], [ -95.015027207999879, 29.614645383000038 ], [ -95.020735857999796, 29.614390133000029 ], [ -95.024711185999934, 29.612981821000062 ], [ -95.024782180999921, 29.60876282400011 ], [ -95.019562183999938, 29.608696822000184 ], [ -95.017145850999839, 29.607101488000129 ], [ -95.018129429999874, 29.610245317000132 ], [ -95.015971688999855, 29.611865315000049 ], [ -95.010013189999881, 29.612301818 ], [ -95.003374186999906, 29.612390818999987 ], [ -94.993465188999892, 29.612605822000095 ], [ -94.991077192999853, 29.611559483000125 ], [ -94.988598618999845, 29.609421102000056 ], [ -94.987995196999918, 29.608449819000047 ], [ -94.986467693999941, 29.606893506000176 ], [ -94.985451814999919, 29.605688511000039 ], [ -94.984877216999919, 29.604869953000158 ], [ -94.983004318999917, 29.601995445000167 ], [ -94.98380286299988, 29.599592827000151 ], [ -94.984413563999908, 29.598495509000145 ], [ -94.985136191999914, 29.597197073000189 ], [ -94.98656419699995, 29.595218826000178 ], [ -94.987591405999922, 29.593713730000164 ], [ -94.988461508999876, 29.592438824 ], [ -94.98973879499988, 29.590652189000085 ], [ -94.992219953999893, 29.588363197000035 ], [ -94.993539850999866, 29.587367308000182 ], [ -94.994464192999885, 29.586907825000086 ], [ -94.995390012999906, 29.58598832000013 ], [ -94.996301459999927, 29.585315563000108 ], [ -94.997570440999937, 29.584378884000163 ], [ -94.998401190999914, 29.583748824000057 ], [ -94.999317190999875, 29.582997826000113 ], [ -95.000080189999949, 29.582158827000057 ], [ -95.002039287999935, 29.579976328000043 ], [ -95.003116190999961, 29.579279825000075 ], [ -95.003609666999921, 29.578359523000131 ], [ -95.004393192999956, 29.57755282800008 ], [ -95.006000190999941, 29.576143827000127 ], [ -95.006900189999897, 29.575090828000103 ], [ -95.007663188999857, 29.574251828000175 ], [ -95.00842618799993, 29.57304982900007 ], [ -95.0097776, 29.571139385000095 ], [ -95.010654187999876, 29.570028830000126 ], [ -95.012037603999943, 29.5677813530001 ], [ -95.01277518899991, 29.566976827000133 ], [ -95.013596623999888, 29.564864721000166 ], [ -95.015800409999883, 29.562338609000047 ], [ -95.017365677999919, 29.5590112810001 ], [ -95.018267928999876, 29.55520957400006 ], [ -95.019154184999934, 29.554321898000122 ], [ -95.02053418699991, 29.553439833000088 ], [ -95.020928187999914, 29.5524258330002 ], [ -95.019828189999942, 29.551470835000032 ], [ -95.018919212999947, 29.551004622000107 ], [ -95.018313646999843, 29.548602484000128 ], [ -95.018176595999876, 29.54805883500018 ], [ -95.018461711999933, 29.548095947000036 ], [ -95.019015581999838, 29.548168040000064 ], [ -95.019275188999927, 29.548201831000085 ], [ -95.020728188999783, 29.548448831000144 ], [ -95.021290220999902, 29.548601973000189 ], [ -95.022556628999894, 29.54865117300017 ], [ -95.024919260999809, 29.548742961000134 ], [ -95.026049081999929, 29.548786855 ], [ -95.02857631299986, 29.549801626000033 ], [ -95.028959081999915, 29.549955321000084 ], [ -95.029003614999908, 29.551404921000088 ], [ -95.029861480999898, 29.552170321000176 ], [ -95.031161680999901, 29.552181921000113 ], [ -95.031565887999875, 29.552236080000061 ], [ -95.03175202299991, 29.552261021000049 ], [ -95.031752279999921, 29.552261055000088 ], [ -95.032601879999902, 29.552573655000177 ], [ -95.034039213999904, 29.553217921000169 ], [ -95.034898679999856, 29.553857054000044 ], [ -95.03796988299986, 29.55388423300008 ], [ -95.039810478999925, 29.553900521000177 ], [ -95.043557877999831, 29.554691321000064 ], [ -95.044895888999861, 29.555848406000166 ], [ -95.048279075999915, 29.558774120000127 ], [ -95.054922008999895, 29.55908465400006 ], [ -95.055724734999842, 29.559065626000066 ], [ -95.056348704999948, 29.5590508360001 ], [ -95.05915723499993, 29.558984262000084 ], [ -95.062726605999842, 29.558899654000186 ], [ -95.062784414999896, 29.55890163700008 ], [ -95.063136174999897, 29.558883834000142 ], [ -95.063360092999915, 29.558835951000162 ], [ -95.065041005999944, 29.558666921 ], [ -95.066490004999935, 29.558300521000092 ], [ -95.06717287499994, 29.557715228000063 ], [ -95.070283070999963, 29.555049456000123 ], [ -95.070586204999927, 29.553789056000085 ], [ -95.070608804999893, 29.551768522000089 ], [ -95.070906204999915, 29.551013323 ], [ -95.071639803999858, 29.550009257000113 ], [ -95.073536069999946, 29.548383657000041 ], [ -95.073970870999915, 29.548261057000051 ], [ -95.076742202999867, 29.545884924000177 ], [ -95.077461873999937, 29.545577993000112 ], [ -95.077903467999874, 29.545389658000207 ], [ -95.078511523999907, 29.544738244000147 ], [ -95.079073068999833, 29.544136658000131 ], [ -95.079160503999788, 29.543483047000105 ], [ -95.079269923999846, 29.542665082000042 ], [ -95.079357534999943, 29.542010152000046 ], [ -95.079545602999929, 29.540604259000133 ], [ -95.079559601999847, 29.539341459000131 ], [ -95.078704068999855, 29.538323725000282 ], [ -95.07828340199984, 29.537183525000103 ], [ -95.078293203999863, 29.536299527000097 ], [ -95.0793124409999, 29.534559530000138 ], [ -95.079380172999961, 29.534399840000049 ], [ -95.079440348999924, 29.534341170000175 ], [ -95.079662590999931, 29.534124489000078 ], [ -95.079908602999865, 29.533913526000163 ], [ -95.082966599999907, 29.533523141000103 ], [ -95.086209329999932, 29.533109173000124 ], [ -95.091046465999909, 29.532491661000169 ], [ -95.091327065999792, 29.533251861 ], [ -95.092757599999914, 29.534526726000081 ], [ -95.094192263999901, 29.53542286000015 ], [ -95.095926864999797, 29.535310925000203 ], [ -95.096655865999878, 29.534685527000136 ], [ -95.096661664999942, 29.534150727000171 ], [ -95.096662464999895, 29.534075527000141 ], [ -95.096375616999865, 29.533372045000196 ], [ -95.095930902999839, 29.532281405 ], [ -95.094724397999869, 29.529322508000064 ], [ -95.093708200999913, 29.526830329000177 ], [ -95.093149799999878, 29.52505746200012 ], [ -95.093167665999943, 29.523415729000078 ], [ -95.094042465999905, 29.522665263000192 ], [ -95.094690605999915, 29.522444745000026 ], [ -95.094767264999859, 29.522418663000053 ], [ -95.095955397999944, 29.521623930000086 ], [ -95.096143197999936, 29.521533663000188 ], [ -95.098605931999941, 29.519858832000065 ], [ -95.099356739999905, 29.519348230000109 ], [ -95.099655894999785, 29.519272339000285 ], [ -95.101127831999804, 29.518898931000138 ], [ -95.101131098999872, 29.518897836000118 ], [ -95.103243396999858, 29.518189864000188 ], [ -95.103278281999906, 29.518175599000127 ], [ -95.104867662999879, 29.517525665 ], [ -95.105496395999921, 29.517525730000102 ], [ -95.105758461999869, 29.517388331000177 ], [ -95.106046662999915, 29.51711346400009 ], [ -95.106256262999921, 29.516632132000041 ], [ -95.106256395999935, 29.516013532000045 ], [ -95.10533979499985, 29.514730332000113 ], [ -95.104449396999826, 29.513263732000098 ], [ -95.104030596999905, 29.512232665000109 ], [ -95.104030663999822, 29.511476466000147 ], [ -95.104187863999925, 29.511178666000038 ], [ -95.104580864999946, 29.510812133000176 ], [ -95.10544539599988, 29.51049146600019 ], [ -95.108064862999925, 29.509850266000058 ], [ -95.10921746199989, 29.509827533000074 ], [ -95.10976874499994, 29.510048607000048 ], [ -95.109894543999872, 29.510106589000092 ], [ -95.110710461999872, 29.510560933000193 ], [ -95.111522461999925, 29.510904666000098 ], [ -95.112151061999896, 29.510973666000154 ], [ -95.11351319399995, 29.510859133000171 ], [ -95.114115861999892, 29.510584266000084 ], [ -95.114849260999861, 29.509965667000131 ], [ -95.116394792999927, 29.508636867000121 ], [ -95.11708729299994, 29.508207461000154 ], [ -95.118811123999933, 29.507138550000093 ], [ -95.118884282999943, 29.507093186000017 ], [ -95.118981696999924, 29.507147332999978 ], [ -95.119697636999888, 29.507545285000049 ], [ -95.119777873999794, 29.507589884000083 ], [ -95.119905611999855, 29.507660887000043 ], [ -95.120347215999914, 29.508397010000124 ], [ -95.120524066999906, 29.508691807000051 ], [ -95.121251245999929, 29.509903961000191 ], [ -95.121749978999901, 29.510255022000194 ], [ -95.122419458999843, 29.510538933000138 ], [ -95.123388590999923, 29.51067646600006 ], [ -95.125195991999931, 29.510378733000035 ], [ -95.126951057999918, 29.509760067000173 ], [ -95.127841657999795, 29.509278867000145 ], [ -95.1284440579999, 29.508774734000099 ], [ -95.129046456999902, 29.507720867000049 ], [ -95.129255990999923, 29.505704469000136 ], [ -95.130904493999878, 29.504921547000041 ], [ -95.131089389999957, 29.504833735000034 ], [ -95.132634788999894, 29.505566868000074 ], [ -95.132896788999915, 29.505818868000119 ], [ -95.133761188999927, 29.506070935000082 ], [ -95.134939855999903, 29.506254135000177 ], [ -95.140309587999866, 29.506528668000044 ], [ -95.141278653999905, 29.50650573400009 ], [ -95.142300186999876, 29.506230669000161 ], [ -95.143408845999943, 29.50551356699998 ], [ -95.1435048539999, 29.505451468000103 ], [ -95.14355488699988, 29.505232562000117 ], [ -95.143583386999921, 29.505107868000096 ], [ -95.143781540999896, 29.504772076 ], [ -95.144715146999943, 29.50318998500012 ], [ -95.144786853999847, 29.503068469000194 ], [ -95.145126852999908, 29.502678936000052 ], [ -95.146776185999784, 29.502289136000112 ], [ -95.146809824999877, 29.502272155000011 ], [ -95.147457251999924, 29.501945336000066 ], [ -95.147614252999915, 29.501739069000166 ], [ -95.148005853999905, 29.500238137000114 ], [ -95.150846538999929, 29.500239284000145 ], [ -95.151306051999939, 29.500239470000164 ], [ -95.153105154999935, 29.500240101000148 ], [ -95.153584651999836, 29.50024026900013 ], [ -95.154226963999875, 29.499713560000149 ], [ -95.155780851999907, 29.498439338000086 ], [ -95.155964158999893, 29.498384433000073 ], [ -95.156775984999911, 29.49814127000008 ], [ -95.157418286999928, 29.498008140000081 ], [ -95.160651782999878, 29.497337937000172 ], [ -95.161071280999863, 29.497264363000113 ], [ -95.161175582999874, 29.497246070000134 ], [ -95.161856781999916, 29.497245870000086 ], [ -95.162800181999899, 29.497497470000098 ], [ -95.163612448999913, 29.497794937000151 ], [ -95.164031981999869, 29.498115470000073 ], [ -95.164163248999898, 29.498344670000051 ], [ -95.164111648999892, 29.498802937000054 ], [ -95.163249248999932, 29.500243070000124 ], [ -95.163277972999936, 29.50026630300016 ], [ -95.165402338999911, 29.501984595000064 ], [ -95.165688847999832, 29.502216336000121 ], [ -95.166809679999915, 29.502301304000074 ], [ -95.166893647999927, 29.502307669000121 ], [ -95.167208180999864, 29.502605469000063 ], [ -95.170708273999935, 29.507068634000124 ], [ -95.170950564999885, 29.507377593000058 ], [ -95.17103437999981, 29.507484470000065 ], [ -95.171270445999895, 29.508355068000188 ], [ -95.17128659899987, 29.508626085000117 ], [ -95.171288148999849, 29.508631845000082 ], [ -95.171287535999795, 29.508641805000195 ], [ -95.171349646999943, 29.509683935000112 ], [ -95.171218845999874, 29.509981735000054 ], [ -95.171204230999933, 29.509995042000188 ], [ -95.171202149999942, 29.510028845000193 ], [ -95.171136060999856, 29.510057109000059 ], [ -95.170564246999902, 29.51057773500014 ], [ -95.167866579999895, 29.511243068000056 ], [ -95.168776997999885, 29.513076182000191 ], [ -95.16907258, 29.513671334000037 ], [ -95.169592385999863, 29.514068076000175 ], [ -95.170464688999914, 29.514733861000135 ], [ -95.170933045999845, 29.515091334000147 ], [ -95.173996546999945, 29.516203364000088 ], [ -95.175941396999917, 29.516909331000193 ], [ -95.178872854999895, 29.51797343000004 ], [ -95.180183125999875, 29.5184490490002 ], [ -95.180391443999895, 29.518524667000062 ], [ -95.183216978999894, 29.5192872450001 ], [ -95.183200257999943, 29.519482957000154 ], [ -95.182985642999952, 29.520012733000101 ], [ -95.182985775999839, 29.520264866000101 ], [ -95.1830906429999, 29.520402133000175 ], [ -95.185057146999895, 29.521768567000034 ], [ -95.185265774999948, 29.521913533000141 ], [ -95.186471774999916, 29.523516733000122 ], [ -95.186485276999861, 29.523541137000169 ], [ -95.187238663999949, 29.524902870000119 ], [ -95.188488447999873, 29.527161836000175 ], [ -95.188866233999875, 29.527844677000079 ], [ -95.189806132999934, 29.529543529000136 ], [ -95.189855373999933, 29.529632531000065 ], [ -95.188658284999917, 29.533100310000179 ], [ -95.188573840999936, 29.533344930000169 ], [ -95.188525113999901, 29.534630900000199 ], [ -95.188496573999942, 29.535384129000079 ], [ -95.188919663999854, 29.535565638000037 ], [ -95.188923863999946, 29.53556744000019 ], [ -95.18984748299988, 29.535963679000023 ], [ -95.194304107999926, 29.537875605000124 ], [ -95.194524438999849, 29.537970129000144 ], [ -95.194369791999861, 29.538073465000195 ], [ -95.193782816999885, 29.538465687000098 ], [ -95.193529238999943, 29.538635129000117 ], [ -95.193347638999853, 29.54115552799999 ], [ -95.193583437999919, 29.541086662000168 ], [ -95.19455297199994, 29.541154862000155 ], [ -95.194650825999929, 29.541188159000118 ], [ -95.19466314399989, 29.541186842000119 ], [ -95.194662831999949, 29.541192244 ], [ -95.197288746999902, 29.542085758000042 ], [ -95.197645437999824, 29.542207129000079 ], [ -95.198064837999894, 29.5423902620002 ], [ -95.198182320999933, 29.542492836000179 ], [ -95.198326970999915, 29.542619128000126 ], [ -95.198799237999879, 29.543329128000096 ], [ -95.199507570999799, 29.544543128000157 ], [ -95.199455437999916, 29.545024328000185 ], [ -95.199430453999923, 29.545445313000126 ], [ -95.199403770999936, 29.54589492700018 ], [ -95.199188764999803, 29.546212610000111 ], [ -95.198984970999902, 29.546513727000047 ], [ -95.198722970999896, 29.546513927000095 ], [ -95.197963370999844, 29.546858061000137 ], [ -95.197515691999911, 29.547167449000145 ], [ -95.19647057099985, 29.547889726000108 ], [ -95.196495049999839, 29.547933016000169 ], [ -95.197020659999907, 29.548862533000094 ], [ -95.1974938379999, 29.549699327000042 ], [ -95.199382170999911, 29.55178325900016 ], [ -95.199872480999886, 29.552228538000126 ], [ -95.200037636999866, 29.552378526000098 ], [ -95.199854768999842, 29.553088860000063 ], [ -95.199750169999902, 29.553363860000108 ], [ -95.199706298999899, 29.553448383000042 ], [ -95.199357636999935, 29.554120126000154 ], [ -95.198245048999866, 29.554005428000039 ], [ -95.19781143799986, 29.553960726000067 ], [ -95.196079195999914, 29.554549500000121 ], [ -95.196056170999782, 29.554557326000065 ], [ -95.194610533999878, 29.556350263000184 ], [ -95.194521914999939, 29.556460172000133 ], [ -95.194485237999928, 29.556505660000141 ], [ -95.194045657999936, 29.557423807000195 ], [ -95.193804971999839, 29.557926525000024 ], [ -95.193504334999943, 29.559488798000075 ], [ -95.193519954999886, 29.559714315000178 ], [ -95.193789859999924, 29.559830126000069 ], [ -95.20044512599992, 29.562685780000095 ], [ -95.200788879999948, 29.562833278000028 ], [ -95.20125136899992, 29.563031724000101 ], [ -95.201250836999861, 29.562344324000097 ], [ -95.201314172999901, 29.562088748000061 ], [ -95.20147987699994, 29.561690252000179 ], [ -95.201807876999808, 29.561266858000124 ], [ -95.203920768999936, 29.558539460000024 ], [ -95.205728168999883, 29.557576125000196 ], [ -95.206199834999893, 29.557484125000201 ], [ -95.207641567999929, 29.557964325000174 ], [ -95.20772023499984, 29.558124659000104 ], [ -95.207615634999911, 29.558330924000192 ], [ -95.207799766999869, 29.559086925000145 ], [ -95.208429107999905, 29.559750135000115 ], [ -95.208560367999894, 29.559888459000032 ], [ -95.208822567999846, 29.559956925000169 ], [ -95.211199870999906, 29.558967715000051 ], [ -95.213013966999881, 29.558212859000097 ], [ -95.212934033999943, 29.556838259000077 ], [ -95.212964646999922, 29.556672432000081 ], [ -95.213090566999938, 29.555990326000138 ], [ -95.213195233999897, 29.555852860000133 ], [ -95.213328052999941, 29.555773284000164 ], [ -95.213981134999926, 29.556275841000058 ], [ -95.214790136999852, 29.555588836000144 ], [ -95.215953135999825, 29.555057841000178 ], [ -95.21652913399987, 29.556065838 ], [ -95.215880136999886, 29.557794838000063 ], [ -95.216061822999848, 29.55792456100022 ], [ -95.216027365999935, 29.558004659000119 ], [ -95.216079326999875, 29.558053496000184 ], [ -95.216709365999861, 29.558645660000138 ], [ -95.217102765999925, 29.559011925000167 ], [ -95.217602431999879, 29.560821525000112 ], [ -95.218465631999948, 29.559308859 ], [ -95.218857507999871, 29.55855346900006 ], [ -95.218858031999901, 29.558552459000172 ], [ -95.219984164999914, 29.557864326000125 ], [ -95.220769763999897, 29.557130661000087 ], [ -95.221424631999923, 29.55703846 ], [ -95.221657913999934, 29.557357331000045 ], [ -95.222211963999939, 29.558114660000061 ], [ -95.2224068619999, 29.55823285300016 ], [ -95.223758749999945, 29.559052690000044 ], [ -95.223759030999872, 29.559052860000062 ], [ -95.224755030999859, 29.559304125000153 ], [ -95.226065762999895, 29.559623925000157 ], [ -95.227088029999891, 29.559920924000156 ], [ -95.228188962999866, 29.560126259000125 ], [ -95.22848593, 29.559971938999979 ], [ -95.229732698999896, 29.559324054000118 ], [ -95.229734228999916, 29.559323259000163 ], [ -95.230493228999933, 29.558451926000089 ], [ -95.231044119999922, 29.557969363000041 ], [ -95.231121561999942, 29.55790152600019 ], [ -95.231308050999928, 29.557832970000046 ], [ -95.232745561999934, 29.557304526000166 ], [ -95.234529671999965, 29.558210764000197 ], [ -95.234817027999895, 29.558356726 ], [ -95.234988187999875, 29.558680246000165 ], [ -95.235132027999782, 29.558952127000168 ], [ -95.236131896999893, 29.559621661000165 ], [ -95.236259827999902, 29.559707326000023 ], [ -95.236678704999861, 29.560301070000154 ], [ -95.236679997999943, 29.560302676999981 ], [ -95.238934546999829, 29.561308690000207 ], [ -95.240377224999932, 29.562384259000112 ], [ -95.240296625999804, 29.560643126 ], [ -95.241108424999823, 29.560184126000081 ], [ -95.241312798999843, 29.559809035 ], [ -95.241945225999871, 29.558648327000068 ], [ -95.241945342999884, 29.55864795000014 ], [ -95.242101825999839, 29.55814426 ], [ -95.242258158999903, 29.557342260000095 ], [ -95.24225775899987, 29.557021461000151 ], [ -95.242081341999949, 29.556588350000084 ], [ -95.24196855799994, 29.556311461000181 ], [ -95.242006088999915, 29.55626761700012 ], [ -95.243067394999912, 29.555027796000104 ], [ -95.243067625999913, 29.555027527000163 ], [ -95.243958225999904, 29.554751727000109 ], [ -95.24497326199986, 29.554679248000014 ], [ -95.245582757999841, 29.554635727000065 ], [ -95.247679256999845, 29.554748323000183 ], [ -95.249929420999933, 29.555327088000187 ], [ -95.250763623999944, 29.555541661000234 ], [ -95.252909822999811, 29.557743127000151 ], [ -95.252801822999913, 29.560469461000139 ], [ -95.252827755999931, 29.56074432600002 ], [ -95.254501955999899, 29.563288860000057 ], [ -95.255856589999894, 29.566285145000112 ], [ -95.257404399999814, 29.569708709000054 ], [ -95.258501753999838, 29.572135923000136 ], [ -95.259128953999948, 29.573717258000162 ], [ -95.259861820999902, 29.574657257000013 ], [ -95.259861903999933, 29.574658171000127 ], [ -95.259965420999947, 29.575802857000156 ], [ -95.259543335999865, 29.576570575000062 ], [ -95.259543125999926, 29.576571838000064 ], [ -95.25909911899987, 29.577378549000116 ], [ -95.25904630999986, 29.577474499000065 ], [ -95.259046122999848, 29.577474838 ], [ -95.259046467999895, 29.577475886000116 ], [ -95.259098020999943, 29.578093256000045 ], [ -95.259438220999868, 29.578666257000126 ], [ -95.259439215999862, 29.578667280999976 ], [ -95.260485419999895, 29.579743921999981 ], [ -95.260486996999873, 29.580053333000137 ], [ -95.26050707599984, 29.583982522000046 ], [ -95.261266219999868, 29.584876521000012 ], [ -95.262155820999908, 29.586458121000138 ], [ -95.26239115299984, 29.586916655000095 ], [ -95.262364552999884, 29.587374720000071 ], [ -95.261576552999941, 29.588886121000147 ], [ -95.261393697999949, 29.589159506000097 ], [ -95.261392752999939, 29.589160919000165 ], [ -95.26050075299986, 29.589847520000035 ], [ -95.260579019999852, 29.590260054000058 ], [ -95.261284751999881, 29.592208054000139 ], [ -95.261597624999922, 29.592756376000178 ], [ -95.261598121999896, 29.592757833000064 ], [ -95.262358139999947, 29.593491839000198 ], [ -95.263852120999843, 29.593928834000049 ], [ -95.263857026999915, 29.593929640999988 ], [ -95.266472617999852, 29.594526054000088 ], [ -95.267022420999822, 29.594503684000191 ], [ -95.267023123999948, 29.594503834000193 ], [ -95.267023785999925, 29.59450358700008 ], [ -95.269081404999895, 29.594285395000153 ], [ -95.269382617999781, 29.594253454000068 ], [ -95.272475813999904, 29.594484862 ], [ -95.272476122999933, 29.594484831000106 ], [ -95.272492524999791, 29.594486974 ], [ -95.276302548999922, 29.594968654000073 ], [ -95.276931548999812, 29.595175254000143 ], [ -95.278162747999886, 29.596115454000142 ], [ -95.278477747999943, 29.595703253000011 ], [ -95.278713747999916, 29.595543054000188 ], [ -95.279447747999896, 29.5955206550002 ], [ -95.280653413999914, 29.595819320000146 ], [ -95.280661809999913, 29.595827180000075 ], [ -95.282539213999883, 29.597584718000181 ], [ -95.282958545999861, 29.597768252000094 ], [ -95.284701720999919, 29.59775653600019 ], [ -95.285910825999906, 29.597748410000069 ], [ -95.286287589999802, 29.597745878000186 ], [ -95.287095567999927, 29.597740448000081 ], [ -95.28919774499991, 29.597726320000163 ], [ -95.289205230999869, 29.597726475000119 ], [ -95.292605610999942, 29.597797053000079 ], [ -95.293156353999848, 29.597179012000193 ], [ -95.293523810999901, 29.596766654000191 ], [ -95.293864810999935, 29.596629320000208 ], [ -95.295385410999927, 29.596378253000125 ], [ -95.296980522999903, 29.596163651000097 ], [ -95.304036808999854, 29.595214321000075 ], [ -95.309358339999903, 29.594987655000125 ], [ -95.309961206999844, 29.595125455000186 ], [ -95.310838024999839, 29.595580097000152 ], [ -95.311376107999948, 29.595858835000115 ], [ -95.312582112999905, 29.596569836000072 ], [ -95.312853574999906, 29.596793228000081 ], [ -95.313446538999926, 29.597280454000046 ], [ -95.313545831999875, 29.59733221800025 ], [ -95.314737427999887, 29.597751895000044 ], [ -95.31776713, 29.597689178000113 ], [ -95.31998879199989, 29.597643188000063 ], [ -95.320721013999901, 29.597628031000109 ], [ -95.321232137999914, 29.597283521000065 ], [ -95.322018603999879, 29.596940121000046 ], [ -95.32267400399985, 29.596848721000129 ], [ -95.323486602999935, 29.597123921000161 ], [ -95.32443331099995, 29.597551183000181 ], [ -95.326998140999876, 29.597499281000122 ], [ -95.329020250999918, 29.597459183000186 ], [ -95.331442663999894, 29.59741114700012 ], [ -95.333260619999862, 29.597375098000047 ], [ -95.334610200999805, 29.597054416000162 ], [ -95.334734228999935, 29.597024945000157 ], [ -95.335624050999911, 29.59602787700004 ], [ -95.337826732999929, 29.59364585600008 ], [ -95.338350999999875, 29.593531456 ], [ -95.340243395999948, 29.593303763000112 ], [ -95.340631598999948, 29.593257055000151 ], [ -95.340762143999882, 29.592701503000171 ], [ -95.340841531999899, 29.592363656000202 ], [ -95.341523131999907, 29.59215765600004 ], [ -95.341523418999884, 29.592157380000177 ], [ -95.343882931999929, 29.589890057000101 ], [ -95.344197530999907, 29.589477723000073 ], [ -95.34459093199979, 29.588698857000111 ], [ -95.344617097999901, 29.588378029000015 ], [ -95.344355198999892, 29.587644923000145 ], [ -95.344355331999907, 29.587392924000145 ], [ -95.346268997999914, 29.585766724000056 ], [ -95.34710734399988, 29.585675300000162 ], [ -95.347108103999858, 29.585674835000191 ], [ -95.347109301999865, 29.585675233000021 ], [ -95.352821728999928, 29.585584459000188 ], [ -95.353108346999932, 29.585629975000188 ], [ -95.353110102999892, 29.585629838000102 ], [ -95.35370425099984, 29.58601984600006 ], [ -95.354053118999843, 29.586248809000097 ], [ -95.354053557999862, 29.586249019000093 ], [ -95.355888101999881, 29.586982835000072 ], [ -95.355890380999881, 29.586982471 ], [ -95.356569726999908, 29.5870282570001 ], [ -95.360684993999939, 29.585081259000106 ], [ -95.36068615399995, 29.585081223000088 ], [ -95.368102525999859, 29.584852525000205 ], [ -95.36810820199986, 29.584855406000145 ], [ -95.371667190999915, 29.586662258999976 ], [ -95.375205723999784, 29.588288724 ], [ -95.376096922999864, 29.588449058000155 ], [ -95.379923588999873, 29.587555458999987 ], [ -95.380002612999931, 29.587520911000183 ], [ -95.380709988999911, 29.587211659000161 ], [ -95.381496321999862, 29.587028326000166 ], [ -95.382270368999912, 29.587021079000181 ], [ -95.383959987999901, 29.587005259000136 ], [ -95.384419891999926, 29.586872771000177 ], [ -95.384799006999856, 29.586763557000044 ], [ -95.386043904999894, 29.586404928000093 ], [ -95.388415094999914, 29.585721839000147 ], [ -95.388415519999853, 29.585721725000326 ], [ -95.389044587999933, 29.585446725000168 ], [ -95.389778385999932, 29.584851059000073 ], [ -95.391926032999905, 29.5828815230002 ], [ -95.3919270909999, 29.582880842 ], [ -95.392110951999825, 29.582620843000086 ], [ -95.392608091999932, 29.581917836000116 ], [ -95.39260935, 29.58191828300005 ], [ -95.400417457999936, 29.582099895000169 ], [ -95.400418783999839, 29.582099926000069 ], [ -95.408779713999934, 29.58296825900004 ], [ -95.408783157999949, 29.582968012000155 ], [ -95.410063914999853, 29.582876327000118 ], [ -95.412513702999945, 29.583146387000056 ], [ -95.412632085999917, 29.58315084100008 ], [ -95.414205084999935, 29.583332842000118 ], [ -95.415184517999876, 29.582969488000064 ], [ -95.416249379999897, 29.58298886100016 ], [ -95.41730443799986, 29.583176674000185 ], [ -95.420793083999854, 29.58166529200037 ], [ -95.424111079999932, 29.58022784100018 ], [ -95.426911083999812, 29.581527840000152 ], [ -95.427592381999943, 29.581855866000122 ], [ -95.430878081999936, 29.583437838000126 ], [ -95.430879663999917, 29.583438570000165 ], [ -95.431143871999893, 29.583560821000052 ], [ -95.435611080999877, 29.585627838000107 ], [ -95.437132079999856, 29.586405840000172 ], [ -95.438095079999869, 29.586897837000151 ], [ -95.440111077999859, 29.587927838000155 ], [ -95.446963073999882, 29.590972839000134 ], [ -95.450655075999919, 29.592852838000173 ], [ -95.452074075999917, 29.593523840000106 ], [ -95.453737071999925, 29.594217841000049 ], [ -95.454968072999918, 29.594798839000134 ], [ -95.456512073999932, 29.595527837000077 ], [ -95.458712072999901, 29.596527838000153 ], [ -95.46081207199984, 29.597527840999987 ], [ -95.465312069999925, 29.599727838000035 ], [ -95.469112068999948, 29.601327841000344 ], [ -95.472212069999898, 29.602827840000202 ], [ -95.473142067999845, 29.603267837000089 ], [ -95.47411206899983, 29.603727837000179 ], [ -95.476012068999921, 29.60472784000001 ], [ -95.478612066999915, 29.605827837 ], [ -95.484613067999874, 29.608627837000032 ], [ -95.48811306399989, 29.610527836000127 ], [ -95.492813060999936, 29.612627836000176 ], [ -95.494913060999863, 29.613327834000184 ], [ -95.500313061999918, 29.615927839000108 ], [ -95.501413060999823, 29.616427836000188 ], [ -95.508113060999847, 29.619527838000128 ], [ -95.509813060999932, 29.62032783300015 ], [ -95.513313055999902, 29.621927835000069 ], [ -95.514361054999824, 29.622407838000072 ], [ -95.51571305899995, 29.62302783500013 ], [ -95.517728057999875, 29.623996838000092 ], [ -95.518676054999901, 29.624451832000091 ], [ -95.520516923999878, 29.625336899000047 ], [ -95.520914051999853, 29.625527834000081 ], [ -95.523970812999949, 29.626922747000155 ], [ -95.532686053999896, 29.630899835000033 ], [ -95.533789054999943, 29.631396831000131 ], [ -95.53496905499992, 29.631927832000091 ], [ -95.536484049999842, 29.632609832000071 ], [ -95.537414047999846, 29.633027831000103 ], [ -95.538367049999934, 29.6334778330002 ], [ -95.541014046999919, 29.634727831000077 ], [ -95.542014048999874, 29.635227835000194 ], [ -95.543914047999806, 29.636027836000043 ], [ -95.546183049999854, 29.637067835000042 ], [ -95.547914047999939, 29.638026835000176 ], [ -95.54931404999985, 29.638526833000128 ], [ -95.551114045999896, 29.639426830000104 ], [ -95.553614045999893, 29.64052683500006 ], [ -95.555214048999858, 29.641268833000201 ], [ -95.557714043999852, 29.642426835000091 ], [ -95.559614043999943, 29.643426830000124 ], [ -95.563515043999871, 29.645126830000098 ], [ -95.56521504299991, 29.645826835000037 ], [ -95.566509044999862, 29.646473828000186 ], [ -95.567591044999858, 29.646992829000052 ], [ -95.569615042999942, 29.647926834000142 ], [ -95.570862044999899, 29.64850283300007 ], [ -95.570930412999871, 29.648535681000169 ], [ -95.575876036999887, 29.650911831000087 ], [ -95.57821503699995, 29.652026832000107 ], [ -95.579515041999798, 29.652526829000124 ], [ -95.580915036999897, 29.653226834000062 ], [ -95.583115039999939, 29.654126831000038 ], [ -95.586821039999904, 29.656070829000104 ], [ -95.588491039999781, 29.656926830000145 ], [ -95.590169032999881, 29.657658831000049 ], [ -95.591085032999914, 29.658074832000072 ], [ -95.592922038999916, 29.658916830000127 ], [ -95.595315032999849, 29.660026832000142 ], [ -95.597115034999945, 29.660826826000058 ], [ -95.600012034999907, 29.662092831000106 ], [ -95.601315034999928, 29.662726826000039 ], [ -95.609916033999866, 29.666926827000054 ], [ -95.611916029999861, 29.668026831000077 ], [ -95.616116028999841, 29.670426829000235 ], [ -95.618744029999846, 29.671834826000119 ], [ -95.621216027999935, 29.673226828 ], [ -95.622880023999926, 29.674133828000098 ], [ -95.624122026999942, 29.674841827000137 ], [ -95.625510026999862, 29.675656824000161 ], [ -95.627280023999845, 29.676637829000072 ], [ -95.628195025999915, 29.677155826000046 ], [ -95.62993502199987, 29.67819782700019 ], [ -95.630850022999937, 29.678754829000074 ], [ -95.631827027999918, 29.679173827000117 ], [ -95.634299024999905, 29.680619825000012 ], [ -95.635458024999878, 29.681317825000068 ], [ -95.636679019999917, 29.682020825000112 ], [ -95.637831022999933, 29.682684825000194 ], [ -95.639700024999868, 29.683762825000201 ], [ -95.641570024999851, 29.684748829000057 ], [ -95.642817019999939, 29.685426828000121 ], [ -95.643419554999866, 29.685796489000094 ], [ -95.643720020999922, 29.68598082800014 ], [ -95.647474018999844, 29.688193824000109 ], [ -95.649013021999906, 29.689100823999979 ], [ -95.650734021999938, 29.69011482400003 ], [ -95.651613019999843, 29.690632827 ], [ -95.652748016999794, 29.691116823000073 ], [ -95.654258019999929, 29.691650827000043 ], [ -95.655479016999891, 29.692341826000131 ], [ -95.656547017999799, 29.692955825000183 ], [ -95.658317013999863, 29.693973825000061 ], [ -95.659904016999917, 29.694813824 ], [ -95.660774015999948, 29.695331826000086 ], [ -95.661933013999942, 29.696014824000084 ], [ -95.663001015999896, 29.696586822000143 ], [ -95.663917015999857, 29.697136822000175 ], [ -95.665000014999919, 29.697689823000189 ], [ -95.665962014999934, 29.698234823000174 ], [ -95.666831014999914, 29.69878082300005 ], [ -95.66849001199995, 29.69970782400009 ], [ -95.670082013999945, 29.700619825000334 ], [ -95.671409016999803, 29.701259822999987 ], [ -95.672477011999945, 29.701828824000071 ], [ -95.673652012999923, 29.702537822000124 ], [ -95.674568012999941, 29.703075820000095 ], [ -95.675529014999881, 29.70362582600012 ], [ -95.682305010999926, 29.707409825000127 ], [ -95.683908007999946, 29.708319821000089 ], [ -95.685928006999916, 29.709443820999979 ], [ -95.68683701, 29.709949823000105 ], [ -95.688210008999931, 29.710800824000103 ], [ -95.689431010999783, 29.711403821000147 ], [ -95.701691001999791, 29.71822282100004 ], [ -95.705795004999914, 29.72050582300011 ], [ -95.70842800399987, 29.721969819000098 ], [ -95.710930001999941, 29.723396818000143 ], [ -95.711343034999913, 29.723629418000201 ], [ -95.727944997999941, 29.732978816000127 ], [ -95.729721995999853, 29.733933822000097 ], [ -95.730764997999927, 29.734519818000081 ], [ -95.732486994999931, 29.73556182100009 ], [ -95.734930997999925, 29.737040819000072 ], [ -95.736454991999892, 29.737963821000054 ], [ -95.739006997999923, 29.739508817000171 ], [ -95.740644993999922, 29.740282818000139 ], [ -95.741595992999919, 29.740732815000172 ], [ -95.742531994999922, 29.741175815000073 ], [ -95.74402099, 29.742074816000127 ], [ -95.744881995999947, 29.742594819000036 ], [ -95.747261990999846, 29.743952815000171 ], [ -95.749871992999886, 29.745429817000172 ], [ -95.751579992999893, 29.746561819000366 ], [ -95.752466991999952, 29.747126815000062 ], [ -95.753345988999911, 29.747611817 ], [ -95.75467799099988, 29.748347817000198 ], [ -95.755985989999942, 29.749032819000035 ], [ -95.757531989999805, 29.749842816000125 ], [ -95.759080985999901, 29.750722813000042 ], [ -95.759968989999891, 29.751226818000077 ], [ -95.761268987999927, 29.751966813000138 ], [ -95.762514984999882, 29.752672815000096 ], [ -95.763726987999803, 29.753360819000193 ], [ -95.764843985999846, 29.753971815999989 ], [ -95.766294988999903, 29.754754817000048 ], [ -95.767253987999936, 29.755272813000094 ], [ -95.768564988999813, 29.755980818000122 ], [ -95.769686986999886, 29.756695812000142 ], [ -95.77106698699987, 29.757575815000056 ], [ -95.773172983999928, 29.75862481400009 ], [ -95.777114298999891, 29.760756464000053 ], [ -95.777750985999944, 29.761100814000148 ], [ -95.786844978999909, 29.766268811000092 ], [ -95.790614977999894, 29.768381811000101 ], [ -95.792968979999898, 29.76970081300016 ], [ -95.796141981999881, 29.771478811000176 ], [ -95.797320975999924, 29.772139815000177 ], [ -95.799068980999948, 29.773119815000086 ], [ -95.801875973999927, 29.774694811000188 ], [ -95.80430197599992, 29.776039813000121 ], [ -95.806091974999902, 29.777032812000133 ], [ -95.808163971999932, 29.778181814000163 ], [ -95.811343974999943, 29.779944814000149 ], [ -95.812263263999924, 29.780455099000047 ], [ -95.812296976999846, 29.780473813000128 ], [ -95.813212975999875, 29.781042810000088 ], [ -95.818998973999896, 29.784232811000148 ], [ -95.82012497199986, 29.784853812000112 ], [ -95.821345973999939, 29.785570810000081 ], [ -95.822566969999855, 29.78617680900004 ], [ -95.823634971999866, 29.786764809000204 ], [ -95.824702972999944, 29.787378814000135 ], [ -95.826200248999839, 29.788267264000069 ], [ -95.826212969999915, 29.788274812000171 ], [ -95.826314277999927, 29.788541452 ], [ -95.826731970999859, 29.789640807000129 ], [ -95.827082968999946, 29.790617810000075 ], [ -95.82743397299987, 29.791681813000086 ], [ -95.827799972999856, 29.792627813000191 ], [ -95.828203967999912, 29.794157812000034 ], [ -95.828608965999933, 29.795370806000051 ], [ -95.828959969999914, 29.796308812000177 ], [ -95.829535611999916, 29.797510605000074 ], [ -95.829677966999895, 29.797807806000037 ], [ -95.830486966999786, 29.800157808000169 ], [ -95.830772656999898, 29.801054403000194 ], [ -95.830898967999929, 29.801450811000109 ], [ -95.833025966999912, 29.807324805000121 ], [ -95.833181806999903, 29.807761320000107 ], [ -95.833407967999904, 29.808394805000031 ], [ -95.836086966999872, 29.816456806000133 ], [ -95.839946965999843, 29.828332800000055 ], [ -95.840984960999947, 29.830853802000149 ], [ -95.843207446999884, 29.836924815000206 ], [ -95.84627996099988, 29.845317798000167 ], [ -95.851390960999936, 29.859798795000078 ], [ -95.85674695799986, 29.874243794000051 ], [ -95.857204959999947, 29.875788797000045 ], [ -95.86204195699986, 29.889159790000178 ], [ -95.867093955999906, 29.903722785000127 ], [ -95.868861954999886, 29.908541791000061 ], [ -95.868895184999872, 29.908633618000067 ], [ -95.873150954999915, 29.920393788000073 ], [ -95.882789948999857, 29.946991781000122 ], [ -95.887875946999884, 29.961025777000089 ], [ -95.889310947999945, 29.965053774000069 ], [ -95.888226944999872, 29.965499776000172 ], [ -95.888638945999844, 29.966556776000058 ], [ -95.892132943999854, 29.975463776000197 ], [ -95.892682942999897, 29.977019773000048 ], [ -95.893399947999796, 29.978781776000123 ], [ -95.894055946999927, 29.980372773000056 ], [ -95.90238793899988, 30.00239376900004 ], [ -95.907468939999887, 30.016305770000145 ], [ -95.909568939999929, 30.022061768000128 ], [ -95.914198939999963, 30.034750762000044 ], [ -95.921156934999942, 30.053821763000148 ], [ -95.921287177999886, 30.054818275000173 ], [ -95.921308936999822, 30.054984761000075 ], [ -95.921308936999822, 30.05606875900008 ], [ -95.921415936999949, 30.057758760000183 ], [ -95.921323933999815, 30.059113760000059 ], [ -95.92150793299993, 30.06055875900012 ], [ -95.92146193099984, 30.061671758000102 ], [ -95.921443932999864, 30.062765758000069 ], [ -95.921454935999918, 30.063884760000064 ], [ -95.923017934999905, 30.064052758000113 ], [ -95.924925935999852, 30.063959757000138 ], [ -95.925108929999908, 30.06549475900016 ], [ -95.925148933999935, 30.067194759000131 ], [ -95.925214929999868, 30.068629755000188 ], [ -95.926634933999949, 30.06862275900016 ], [ -95.926975540999877, 30.069637779000118 ], [ -95.92768693499994, 30.07175775900004 ], [ -95.928053933999934, 30.072836757000115 ], [ -95.935156928999845, 30.092028753000189 ], [ -95.936553928999899, 30.09581175400012 ], [ -95.938760927999908, 30.101916752000022 ], [ -95.947081922999928, 30.124928745000265 ], [ -95.954116918999887, 30.144668743000182 ], [ -95.956796917999895, 30.152098742000078 ], [ -95.95732091699989, 30.153552741 ], [ -95.958262921999847, 30.156248741000184 ], [ -95.960768919999907, 30.163416737000095 ], [ -95.959594918999926, 30.163382737000116 ], [ -95.958678918999908, 30.162958740000136 ], [ -95.957778919999839, 30.162207742000195 ], [ -95.956649919999904, 30.161707738000192 ], [ -95.955641918999902, 30.16117073800018 ], [ -95.954787921999923, 30.160266739000065 ], [ -95.953963917999943, 30.159686740000154 ], [ -95.953658921999931, 30.15859974100016 ], [ -95.952697918999846, 30.158073739000145 ], [ -95.952452922999896, 30.157096741000146 ], [ -95.9516449219999, 30.15644074100004 ], [ -95.950515920999862, 30.156119742000048 ], [ -95.949553920999847, 30.155593739000153 ], [ -95.948394920999874, 30.155471737000198 ], [ -95.947737922999863, 30.154578741000197 ], [ -95.946822920999921, 30.154002742000049 ], [ -95.9469749229998, 30.152984742000172 ], [ -95.946105922999948, 30.15231674100005 ], [ -95.945601924999949, 30.151347738000084 ], [ -95.945296922999944, 30.150329739000142 ], [ -95.945357921999857, 30.149318740000066 ], [ -95.944640922999838, 30.148334740000049 ], [ -95.943526924999901, 30.147670740000141 ], [ -95.942458924999883, 30.147010738000059 ], [ -95.941649924999922, 30.146346745000134 ], [ -95.940688921999879, 30.145793743000183 ], [ -95.940947922999896, 30.144797742000094 ], [ -95.940078923999863, 30.143970743000182 ], [ -95.938964926999915, 30.14431774000019 ], [ -95.938048926999841, 30.144961739000113 ], [ -95.937087923999911, 30.145301740000093 ], [ -95.936171923999893, 30.144633739000145 ], [ -95.935316929999942, 30.144099740999987 ], [ -95.934354928999937, 30.144721742000062 ], [ -95.933485928999815, 30.14413474000014 ], [ -95.932676927999921, 30.143382742000085 ], [ -95.9317609269998, 30.144084744000107 ], [ -95.930753924999863, 30.144599744000065 ], [ -95.929838929999846, 30.144183743000042 ], [ -95.928724927999781, 30.14404674400015 ], [ -95.928007927999957, 30.143081742000053 ], [ -95.927305927999896, 30.142234740000159 ], [ -95.926389928999924, 30.141707744000087 ], [ -95.925382929999842, 30.14196374300019 ], [ -95.924467928999888, 30.142371745000052 ], [ -95.924162927999873, 30.141265742000083 ], [ -95.924421927999902, 30.14028174200001 ], [ -95.924055927999916, 30.139194743000076 ], [ -95.9227889309999, 30.138709742000174 ], [ -95.921735931999876, 30.138606743000086 ], [ -95.921980927999925, 30.137474742000109 ], [ -95.921735931999876, 30.136383743000181 ], [ -95.920820930999923, 30.135651743000039 ], [ -95.919599928999901, 30.135475744000132 ], [ -95.918745930999933, 30.134861745000197 ], [ -95.918226930999879, 30.133877743000145 ], [ -95.91732693299987, 30.133267746 ], [ -95.916517931999806, 30.131935744000149 ], [ -95.915601932999948, 30.131104745000187 ], [ -95.914427929999874, 30.131184746000145 ], [ -95.913374932999886, 30.131233743000053 ], [ -95.912397933999898, 30.130837743000139 ], [ -95.911543929999937, 30.129940746000099 ], [ -95.910886931999926, 30.129059745000124 ], [ -95.910841934999837, 30.12801474600008 ], [ -95.893750937999869, 30.120216745000107 ], [ -95.892636935999917, 30.120125746000159 ], [ -95.891583937999883, 30.119423745000063 ], [ -95.890622940999947, 30.119061745000124 ], [ -95.889645936999784, 30.119511747000214 ], [ -95.888562938999883, 30.120125746000159 ], [ -95.887234936999846, 30.120380747000183 ], [ -95.88578593799987, 30.120255744000076 ], [ -95.884717942999885, 30.119629743000022 ], [ -95.883191937999925, 30.119637743000169 ], [ -95.882641938999882, 30.120747745000187 ], [ -95.881985938999946, 30.121628748000148 ], [ -95.881619937999801, 30.12268874099999 ], [ -95.880658941999855, 30.123001745000185 ], [ -95.880108942999925, 30.122128744000122 ], [ -95.879895941999919, 30.121002747000151 ], [ -95.87868994399993, 30.120289743000118 ], [ -95.877179939999849, 30.120613743000096 ], [ -95.876156941999852, 30.120838743000093 ], [ -95.87524194, 30.121315743000086 ], [ -95.874034942999799, 30.121990747000158 ], [ -95.873165941999844, 30.121410741000147 ], [ -95.87240294399993, 30.120648747000132 ], [ -95.872951944999897, 30.11947374600004 ], [ -95.872860946999936, 30.118221746000184 ], [ -95.871440942999939, 30.117851745 ], [ -95.870235941999795, 30.117954742999981 ], [ -95.869213941999874, 30.117817744000149 ], [ -95.86840494799992, 30.116848746000077 ], [ -95.868328946999853, 30.115742745000151 ], [ -95.867290943999933, 30.114845742000057 ], [ -95.865764946999946, 30.114239744000201 ], [ -95.864254942999821, 30.112989747000089 ], [ -95.863491943999918, 30.112313745000108 ], [ -95.862835944999915, 30.111447746000124 ], [ -95.863247945999888, 30.110154744000052 ], [ -95.86353694699983, 30.108781747000076 ], [ -95.863552948999825, 30.107759746000173 ], [ -95.863247945999888, 30.106713748000118 ], [ -95.862484946999928, 30.106004745000174 ], [ -95.861522945999923, 30.105333748000138 ], [ -95.860103946999914, 30.105027747000126 ], [ -95.858638946999918, 30.104570749000171 ], [ -95.857311948999893, 30.103925746000183 ], [ -95.857021948999886, 30.102246748000194 ], [ -95.855999949999898, 30.102021746000048 ], [ -95.854625946999874, 30.101728751000049 ], [ -95.85303895099986, 30.101892748000182 ], [ -95.851360949999844, 30.1021097470001 ], [ -95.850277951999942, 30.101720750000371 ], [ -95.84997195099993, 30.100667751000174 ], [ -95.848949950999838, 30.100209748000054 ], [ -95.847774950999792, 30.100339750000099 ], [ -95.846660952999912, 30.100503748 ], [ -95.84629495199988, 30.099069746000055 ], [ -95.84605095399985, 30.098081751000109 ], [ -95.845684954999854, 30.097139746000039 ], [ -95.844462953999937, 30.096700747000227 ], [ -95.843440951999924, 30.095956750000202 ], [ -95.842875949999893, 30.095094747000186 ], [ -95.842875949999893, 30.094075748000197 ], [ -95.843028950999837, 30.092881749000071 ], [ -95.843440951999924, 30.091958747000088 ], [ -95.844050951999861, 30.090936747000057 ], [ -95.844309952999893, 30.08991874700007 ], [ -95.8446169529999, 30.088663751000126 ], [ -95.844248954999898, 30.087652753000157 ], [ -95.843073954999852, 30.086687750000127 ], [ -95.84196095, 30.086004752000118 ], [ -95.840525954999876, 30.085348752000186 ], [ -95.839350953999826, 30.084856749000096 ], [ -95.839152951999949, 30.083792754000058 ], [ -95.838328953999905, 30.083090753000135 ], [ -95.837260951999895, 30.083445750000127 ], [ -95.836192956999923, 30.083731751000073 ], [ -95.834956951999814, 30.084147751000049 ], [ -95.833933953999875, 30.084822748000192 ], [ -95.83291195199979, 30.085116748000189 ], [ -95.832057955999915, 30.084559752000132 ], [ -95.830973958999962, 30.084379748000121 ], [ -95.830058957999881, 30.083742750000166 ], [ -95.829752955999879, 30.082766753000158 ], [ -95.828883956999846, 30.081976748999981 ], [ -95.827448955999841, 30.082152754000106 ], [ -95.824442959999956, 30.082365754000136 ], [ -95.823222956999871, 30.082419749000053 ], [ -95.82178795599981, 30.082277751000046 ], [ -95.820811956999876, 30.0826867510001 ], [ -95.819804959999942, 30.083090752000093 ], [ -95.81856896099994, 30.082773748000136 ], [ -95.817759959999933, 30.082106751000083 ], [ -95.816676960999871, 30.082030750000115 ], [ -95.81632595799995, 30.082987753000168 ], [ -95.81643295799995, 30.084189752000043 ], [ -95.8159139629999, 30.085139752000146 ], [ -95.814737961999924, 30.084986751000198 ], [ -95.813714956999945, 30.084681750000069 ], [ -95.812341959999912, 30.084552750000057 ], [ -95.81116796299996, 30.08499675000019 ], [ -95.809688957999867, 30.085661747000078 ], [ -95.808972958999902, 30.086543747000064 ], [ -95.809492958999897, 30.087607749000139 ], [ -95.808867962999898, 30.08840074800014 ], [ -95.807541958999892, 30.088714750000179 ], [ -95.806534961999944, 30.088990750999983 ], [ -95.805711962999908, 30.089692744999983 ], [ -95.804643960999897, 30.089475745000186 ], [ -95.8032569639999, 30.090144749999979 ], [ -95.801056960999858, 30.091270749000159 ], [ -95.800134964999927, 30.090858747000201 ], [ -95.799424963999911, 30.089987750000148 ], [ -95.797764966999864, 30.090536751 ], [ -95.796105963999949, 30.090695749000187 ], [ -95.793866967999918, 30.091679749000097 ], [ -95.792312966999873, 30.091403748000062 ], [ -95.791232963999789, 30.091402748000181 ], [ -95.790178966999861, 30.092135748000089 ], [ -95.78920496399985, 30.091859747000061 ], [ -95.788492964999932, 30.092637749000119 ], [ -95.789334966999945, 30.094104747000184 ], [ -95.788149967999857, 30.09387474700014 ], [ -95.78699097, 30.094148745000094 ], [ -95.785357964999946, 30.093575748000038 ], [ -95.784700966999935, 30.092795749000064 ], [ -95.783067969999934, 30.092107745000135 ], [ -95.782463967999945, 30.090916749000204 ], [ -95.781094969999913, 30.09045774700007 ], [ -95.779540968999868, 30.089998746000166 ], [ -95.778382968999892, 30.08953974600001 ], [ -95.776985968999853, 30.090042750000066 ], [ -95.776168966999933, 30.090752746000025 ], [ -95.774219970999866, 30.090956747000124 ], [ -95.772666968999886, 30.089879745000186 ], [ -95.771956971999955, 30.088939749000019 ], [ -95.771246970999925, 30.087473751000065 ], [ -95.768903970999929, 30.087494746000118 ], [ -95.7684019699999, 30.08845674500003 ], [ -95.767348970999876, 30.088386748 ], [ -95.765215969999872, 30.088522747000187 ], [ -95.764425972999902, 30.087857749000023 ], [ -95.763794973999893, 30.08707874900006 ], [ -95.761925972999904, 30.086321748000046 ], [ -95.760687975999872, 30.086434750000194 ], [ -95.759870973999853, 30.087052749000065 ], [ -95.759737975999883, 30.088357747000202 ], [ -95.760104975999866, 30.089503747000155 ], [ -95.758154973999922, 30.090600750000078 ], [ -95.756599974999858, 30.091355748000186 ], [ -95.755466973999887, 30.091995746 ], [ -95.754412976999902, 30.092315745000125 ], [ -95.75341097199987, 30.093413747000053 ], [ -95.751856976999818, 30.093687745000125 ], [ -95.75024197699986, 30.094467745000205 ], [ -95.749531975999915, 30.095430744000058 ], [ -95.748083975999918, 30.095454746000087 ], [ -95.746765978999917, 30.095249746000153 ], [ -95.745448979999935, 30.095182745000042 ], [ -95.744421980999903, 30.094931745000057 ], [ -95.743052976999934, 30.095413743000108 ], [ -95.742368979999924, 30.096330748000074 ], [ -95.741077979999943, 30.095988743000078 ], [ -95.739971978999904, 30.09578374400013 ], [ -95.738495981999918, 30.095074747000126 ], [ -95.737168978999932, 30.094556745000094 ], [ -95.736085979999928, 30.093984746000107 ], [ -95.73454298199988, 30.093543743000112 ], [ -95.733173979999947, 30.094025742000156 ], [ -95.731172982999851, 30.094348743000126 ], [ -95.729951980999886, 30.09488974300012 ], [ -95.728750980999905, 30.095014747000164 ], [ -95.727355983999928, 30.09604674600018 ], [ -95.726223981999908, 30.096711744000174 ], [ -95.725222982999924, 30.096986747000074 ], [ -95.723564983999893, 30.098087744000114 ], [ -95.722934982999902, 30.099508741000196 ], [ -95.721827981999866, 30.099600744000103 ], [ -95.719905983999922, 30.099625744000036 ], [ -95.718218983999861, 30.099282741000135 ], [ -95.717191983999896, 30.099100746000087 ], [ -95.716481982999937, 30.100177746000039 ], [ -95.715876982999816, 30.101070742000047 ], [ -95.715218985999911, 30.10194174600014 ], [ -95.714218983999842, 30.102125745000194 ], [ -95.712979982999911, 30.101737745000097 ], [ -95.711768985999925, 30.101738744000045 ], [ -95.71024198899994, 30.102403743000082 ], [ -95.708608985999945, 30.102702742000076 ], [ -95.707185984999853, 30.102588740000041 ], [ -95.705423988999939, 30.101888742000195 ], [ -95.70455098799988, 30.101262742000131 ], [ -95.70341699099987, 30.099888744000111 ], [ -95.70259998899985, 30.099133740000184 ], [ -95.701281985999856, 30.098149741000046 ], [ -95.699437990999911, 30.097990742000096 ], [ -95.698331988999939, 30.098334744000166 ], [ -95.697172989999899, 30.098633742000175 ], [ -95.694776992999891, 30.098978743000032 ], [ -95.693669992999844, 30.098589745000201 ], [ -95.690399990999936, 30.099126744000106 ], [ -95.689271988999849, 30.099851742000055 ], [ -95.687560991999931, 30.10049374300019 ], [ -95.686717991999842, 30.101341744000077 ], [ -95.686823992999905, 30.102372744000089 ], [ -95.686772992999863, 30.103448740000147 ], [ -95.687878988999898, 30.103470739000102 ], [ -95.68698399099992, 30.104822741000078 ], [ -95.686747991999937, 30.106151743000055 ], [ -95.686088266999946, 30.106461612000206 ], [ -95.685691990999956, 30.106647740000145 ], [ -95.684616992999906, 30.107012741000066 ], [ -95.683424992999846, 30.106848738999986 ], [ -95.682111993999854, 30.106245742000116 ], [ -95.680605991999926, 30.105990741000081 ], [ -95.679344991999869, 30.104964740000188 ], [ -95.677483990999917, 30.103951739000141 ], [ -95.676367996999943, 30.103316739000149 ], [ -95.675102996999897, 30.102446741000108 ], [ -95.673890993999919, 30.102172741000178 ], [ -95.672124998999891, 30.101325742000196 ], [ -95.67101899599993, 30.100982739000131 ], [ -95.6691, 30.101166744000182 ], [ -95.66798999699995, 30.101739740000138 ], [ -95.667700995999951, 30.102861744000052 ], [ -95.667633993999914, 30.104257740000097 ], [ -95.667385995999894, 30.105381741000144 ], [ -95.66635, 30.105799739000076 ], [ -95.665542998999911, 30.106939742000183 ], [ -95.664049997999939, 30.107495739000058 ], [ -95.662583997999945, 30.107946740000159 ], [ -95.66167, 30.108864742000097 ], [ -95.660986996999895, 30.109987738000086 ], [ -95.661987996999926, 30.110238738000078 ], [ -95.66133, 30.111659741000153 ], [ -95.660111999999799, 30.112071737000122 ], [ -95.658959995999851, 30.112484737000042 ], [ -95.658016998999926, 30.112040739000065 ], [ -95.658326999999815, 30.110881739000092 ], [ -95.657245997999894, 30.110515738000117 ], [ -95.655981996999856, 30.109645740000076 ], [ -95.655007002999938, 30.109095740000047 ], [ -95.654006002999949, 30.109187736000194 ], [ -95.653663998999889, 30.110195739000176 ], [ -95.654586001999917, 30.110859740000137 ], [ -95.653173998999932, 30.111398737000062 ], [ -95.652584000999809, 30.112463737000098 ], [ -95.652347997999925, 30.113608737000103 ], [ -95.65163599899995, 30.114318739000058 ], [ -95.650267000999875, 30.115052738000145 ], [ -95.648818001999871, 30.116907735000041 ], [ -95.648107000999914, 30.117686735000063 ], [ -95.647212002999936, 30.118190739000173 ], [ -95.646369001999915, 30.118763736000062 ], [ -95.645485776999919, 30.119221813000024 ], [ -95.644866998999817, 30.119542737000135 ], [ -95.64397, 30.120275737000043 ], [ -95.642996999999809, 30.120001739000088 ], [ -95.642075002999889, 30.120390737000154 ], [ -95.640600003999921, 30.121604736000108 ], [ -95.639151004999917, 30.122292733000112 ], [ -95.638756003999902, 30.12330073500004 ], [ -95.637808001999815, 30.124170735000064 ], [ -95.638124005999941, 30.125224738000156 ], [ -95.637228001999858, 30.125774735999979 ], [ -95.636227000999895, 30.126484733000041 ], [ -95.635200001999934, 30.12579773400012 ], [ -95.634172004999868, 30.126255737000125 ], [ -95.632960007999884, 30.126759735000068 ], [ -95.631669007999918, 30.12705773499999 ], [ -95.629877006999948, 30.127996733000089 ], [ -95.628938226999935, 30.127885527000103 ], [ -95.628138007999951, 30.12779073500008 ], [ -95.62716400599993, 30.127538736000137 ], [ -95.626215004999949, 30.126851738000138 ], [ -95.625214005999794, 30.126737735000177 ], [ -95.62379100499993, 30.127149737000192 ], [ -95.623912007999877, 30.128435731000195 ], [ -95.624871006999911, 30.129211737000052 ], [ -95.625425007999922, 30.130173732000173 ], [ -95.624555008999891, 30.130906731000156 ], [ -95.624661009999784, 30.132051734000129 ], [ -95.624322335999921, 30.13259636700019 ], [ -95.62394900399994, 30.133196733000261 ], [ -95.624213004999945, 30.134456735000068 ], [ -95.623159006999913, 30.1354187300002 ], [ -95.622421008999879, 30.136174733000136 ], [ -95.621209006999891, 30.135739734000079 ], [ -95.620741265999925, 30.135770863000118 ], [ -95.61949600699991, 30.135853736000115 ], [ -95.61860000899992, 30.135258733000171 ], [ -95.617256005999934, 30.135693733000096 ], [ -95.616281011999888, 30.135258733000171 ], [ -95.61506900899991, 30.134983731000151 ], [ -95.613699005999877, 30.135395732000067 ], [ -95.612645009999895, 30.135487735000137 ], [ -95.611696007999797, 30.135143733000067 ], [ -95.610642009999879, 30.135532731000126 ], [ -95.609404009999935, 30.134891734000117 ], [ -95.608297007999909, 30.134456735000068 ], [ -95.606795010999917, 30.134158734000039 ], [ -95.60571500899988, 30.134502730000062 ], [ -95.604450014999941, 30.133883732000189 ], [ -95.60344901499991, 30.133814733000065 ], [ -95.602895014999888, 30.132921731000071 ], [ -95.601920014999791, 30.133585732000029 ], [ -95.601077013999941, 30.132875736000077 ], [ -95.598284011999851, 30.133447733000256 ], [ -95.597572013999923, 30.134409734000204 ], [ -95.596571012999789, 30.134455729000198 ], [ -95.595675014999927, 30.135073735000162 ], [ -95.594489010999894, 30.135027734000179 ], [ -95.593356011999788, 30.135439730000144 ], [ -95.592355011999928, 30.135806735000077 ], [ -95.591722015999963, 30.136699731000022 ], [ -95.590694017999908, 30.136286732000091 ], [ -95.589877015999946, 30.137134730000071 ], [ -95.588718016999906, 30.137637729 ], [ -95.588217014999941, 30.138759733000143 ], [ -95.587162018999948, 30.139973732000101 ], [ -95.586846012999899, 30.141325731000165 ], [ -95.585659017999944, 30.142332727000163 ], [ -95.585132015999932, 30.14409672700009 ], [ -95.584446016999948, 30.145538731000048 ], [ -95.584340015999942, 30.146592727000044 ], [ -95.583075014999906, 30.147462732000179 ], [ -95.581994013999918, 30.14867672600008 ], [ -95.581282014999886, 30.149638727000365 ], [ -95.580149013999915, 30.150073726000191 ], [ -95.578989016999799, 30.150072728000112 ], [ -95.577540017999823, 30.150301729000152 ], [ -95.576512018999949, 30.150759726000103 ], [ -95.575721017999854, 30.151560725000028 ], [ -95.574561018999873, 30.152018728000201 ], [ -95.573428019999938, 30.151697731000183 ], [ -95.57211001599984, 30.152132730000062 ], [ -95.571688016999929, 30.153094730000081 ], [ -95.570185021999919, 30.153460725000059 ], [ -95.57058001799993, 30.155292724 ], [ -95.570157019999897, 30.156414728000133 ], [ -95.569893018999835, 30.157514727000095 ], [ -95.568654018999908, 30.158269725000022 ], [ -95.568074018999937, 30.159300725000094 ], [ -95.566650020999944, 30.159826728000155 ], [ -95.565437019999933, 30.160604724000052 ], [ -95.563935023999875, 30.160329728000022 ], [ -95.562854021999897, 30.160512728000072 ], [ -95.562196018999884, 30.159527723000053 ], [ -95.560957024999936, 30.159320727000136 ], [ -95.560345818999906, 30.158679124000063 ], [ -95.560193019999872, 30.158518727000057 ], [ -95.559455021999838, 30.159296724 ], [ -95.558769021999922, 30.160212724000132 ], [ -95.557846020999932, 30.160945725000147 ], [ -95.556791024999882, 30.161173726000072 ], [ -95.558478023999839, 30.16179372300013 ], [ -95.558557020999899, 30.162915727000037 ], [ -95.558634020999932, 30.165320724000196 ], [ -95.55760702099991, 30.164884726000142 ], [ -95.557501020999894, 30.165984724000054 ], [ -95.556845313999929, 30.166505837000106 ], [ -95.556551020999905, 30.166739722000163 ], [ -95.554969022999842, 30.167426727000077 ], [ -95.553651024999851, 30.168227727000101 ], [ -95.55283302599986, 30.169097724000043 ], [ -95.552586576999886, 30.169966956000049 ], [ -95.552463024999895, 30.170402722 ], [ -95.550723021999886, 30.170241721000135 ], [ -95.549537023999903, 30.169989722000135 ], [ -95.548456022999915, 30.169965726000097 ], [ -95.547788436999895, 30.169398943000207 ], [ -95.547403023999891, 30.169071726000197 ], [ -95.546005024999886, 30.169414722000173 ], [ -95.54495102899989, 30.169253720000142 ], [ -95.544080025999847, 30.170100722000086 ], [ -95.543078026999922, 30.170329723000126 ], [ -95.541813025999943, 30.170489725000042 ], [ -95.540654026999903, 30.169892720000121 ] ] ] ] } }\r\n" + 
						"]\r\n" + 
						"}";
				
				owlMap.addGeoJson(geoJsonStr, (featureMap)->{
					// featureMap is a Map<feature_id_string, IMapDataFeature>  
					viewLogger.log(LogLevel.INFO, "[DemoModel.addGeoJson()] Found featureMap = "+featureMap);
					
					// Print out the features that were found.
					featureMap.forEach( (featureId, feature) -> {
						viewLogger.log(LogLevel.INFO, "[addGeoJson()] Found feature (ID = "+featureId+") properties: "+feature.getProperties());
						viewLogger.log(LogLevel.INFO, "[addGeoJson()] Found feature.getGeometry()  = "+feature.getGeometry());
						viewLogger.log(LogLevel.INFO, "[addGeoJson()] Found feature.getGeometry().getLatLngs().size() = "+feature.getGeometry().getLatLngs().size());
						viewLogger.log(LogLevel.INFO, "[addGeoJson()] Found feature.getGeometry().getLatLngs()  = ");
						
						// Print the list of ILatLngs in chunks because println can't properly printout a single string with all 2500+ elements.
						// This could also have been done using List.subList()
						feature.getGeometry().getLatLngs().forEach( (Consumer<? super ILatLng>) new Consumer<ILatLng>() {
							// Explicit declaration using anonymous inner class rather than lambda expression so that the function can hold state. 
							int count = 0; // counter for elements per line
							
							public void accept(ILatLng latLng){
								String outputStr = "";
								if(10<=++count) {  // max 10 elements per line
									count = 0;
									outputStr += "(%3.5f, %3.5f)".formatted(latLng.getLat(), latLng.getLng()); // print with newline at end
									viewLogger.log(LogLevel.INFO, outputStr);
									outputStr = ""; // reset the output string
								}
								else {
									outputStr += "(%3.5f, %3.5f), ".formatted(latLng.getLat(), latLng.getLng()); // print with no newline at end
								}
								
							}
						});
					});
					owlMap.setZoom(9);
				});
			} catch (InterruptedException e) {
				viewLogger.log(LogLevel.ERROR, "[addGeoJson()] Exception while waiting for countdownLatch: "+e);
				e.printStackTrace();
			}
				
		})).start();
	}


}

