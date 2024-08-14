package provided.owlMaps.control.impl;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import provided.logger.ILogger;
import provided.logger.LogLevel;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataDictionary;
import provided.owlMaps.mouse.ComponentMouseDragEventType;
import provided.owlMaps.mouse.ComponentMouseEventType;
import provided.owlMaps.mouse.IMouseEvent;
import provided.owlMaps.map.MapEventType;
import provided.owlMaps.map.MapTypeId;
import provided.owlMaps.map.data.IListMapDataFeature;
import provided.owlMaps.map.data.IMapDataFeature;
import provided.owlMaps.map.data.IMapDataStyleOptions;
import provided.owlMaps.mouse.MapMouseDragEventType;
import provided.owlMaps.mouse.MapMouseEventType;
import provided.owlMaps.cefUtils.ICefDefs;
import provided.owlMaps.cefUtils.ICefObjRef;
import provided.owlMaps.cefUtils.ICefObject;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.ISystemInfo;
import provided.owlMaps.cefUtils.gson.GsonTypeWrap;
import provided.owlMaps.cefUtils.wrapper.IOwlMapWrapped;
import provided.owlMaps.cefUtils.wrapper.IPolygonWrapped;
import provided.owlMaps.cefUtils.wrapper.IPolylineWrapped;
import provided.owlMaps.cefUtils.wrapper.IRectangleWrapped;
import provided.owlMaps.cefUtils.wrapper.ICircleWrapped;
import provided.owlMaps.cefUtils.wrapper.IGroundOverlayWrapped;
import provided.owlMaps.cefUtils.wrapper.IInfoWindowWrapped;
import provided.owlMaps.cefUtils.wrapper.IMarkerWrapped;
import provided.owlMaps.components.IMapComponentFactory;
import provided.owlMaps.components.infowindow.IInfoWindow;
import provided.owlMaps.components.infowindow.IInfoWindowOptions;
import provided.owlMaps.components.infowindow.InfoWindowEventType;
import provided.owlMaps.components.marker.IMarker;
import provided.owlMaps.components.marker.IMarkerLabel;
import provided.owlMaps.components.marker.IMarkerLabelOptions;
import provided.owlMaps.components.marker.IMarkerOptions;
import provided.owlMaps.components.marker.MarkerEventType;
import provided.owlMaps.components.overlay.GroundOverlayMouseEventType;
import provided.owlMaps.components.overlay.IGroundOverlay;
import provided.owlMaps.components.overlay.IGroundOverlayOptions;
import provided.owlMaps.components.shapes.CircleEventType;
import provided.owlMaps.components.shapes.ICircle;
import provided.owlMaps.components.shapes.ICircleOptions;
import provided.owlMaps.components.shapes.IPolyMouseEvent;
import provided.owlMaps.components.shapes.IPolygon;
import provided.owlMaps.components.shapes.IPolygonOptions;
import provided.owlMaps.components.shapes.IPolyline;
import provided.owlMaps.components.shapes.IPolylineOptions;
import provided.owlMaps.components.shapes.IRectangle;
import provided.owlMaps.components.shapes.IRectangleOptions;
import provided.owlMaps.components.shapes.RectangleEventType;
import provided.owlMaps.control.IOwlMapControl;
import provided.owlMaps.control.MapLengthUnits;
import provided.owlMaps.general.IAnchor;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.ILatLngBounds;
import provided.owlMaps.general.IPath;
import provided.owlMaps.general.IPathLatLng;
import provided.owlMaps.general.IPathList;
import provided.owlMaps.general.IPathListLatLng;
import provided.owlMaps.general.IPaths;
import provided.owlMaps.general.IPathsLatLng;
import provided.owlMaps.general.IPlace;
import provided.owlMaps.map.IMapOptions;
import provided.owlMaps.map.IOwlMap;
import provided.owlMaps.utils.IOwlMapUtils;
import provided.owlMaps.utils.IOwlMapsDefs;
import provided.owlMaps.utils.impl.AOwlMapUtils;



/**
 * The main map controlling and instantiating entity of the OwlMaps system.
 * Instantiate the OwlMapControl first, which will instantiate the map when started.   
 * Once the map has been created, OwlMapControl provides factories for all other map components.
 * In an RMI-based system, the OwlMapControl should be instantiated after the RMI system has started.
 * @author swong
 *
 */
public class OwlMapControl implements IOwlMapControl {

	/**
	 * Location of default map parent page file
	 */
	private static final String defaultPage = "provided/owlMaps/cefUtils/map.html"; //"/provided/owlMaps/cefUtils/map.html"; //"file:///provided/owlMaps/cefUtils/map.html"; //"provided/owlMaps/control/map.html";

	/**
	 * The local Google Maps API key to use
	 */
	private String googleMapsApiKey;
	
	/**
	 * The current IOwlMap object
	 */
	private IOwlMap owlMap;
	
	/**
	 * The current IMapComponentFactory object
	 */
	private IMapComponentFactory mapComponentFac;
	
	/**
	 * The IJsUtils utility to use
	 */
	private ICefUtils cefUtils;


	/**
	 * The current length unit being used by the system.
	 */
	private MapLengthUnits systemLengthUnit;

	/**
	 * The IOwlMapUtils that is tied to this instance of the system.
	 */
	private IOwlMapUtils owlMapUtils;

	/**
	 * The logger to use
	 */
	private ILogger logger;
	
	
	/**
	 * Instantiate the OwlMapControl
	 * @param googleMapsApiKey  The Google Maps API key to use
	 * @param systemLengthUnit The length unit to use by the system.
	 * @param logger The logger to use
	 */
	public OwlMapControl(String googleMapsApiKey, MapLengthUnits systemLengthUnit, ILogger logger) {
		this.logger = logger;
		logger.log(LogLevel.INFO, "[OwlMapControl] constructing..."); // Don't use logger yet b/c if this is running in model constructor, the view isn't ready yet.

		this.googleMapsApiKey = googleMapsApiKey;
		this.systemLengthUnit = systemLengthUnit;

		
		
		ISystemInfo sysInfo = new ISystemInfo() {

			@Override
			public double getSysLengthPerMeter() {
				return IOwlMapsDefs.LENGTH_CONVERSIONS.get(OwlMapControl.this.systemLengthUnit);
			}
			
		};

		this.cefUtils = ICefUtils.make(this.googleMapsApiKey, sysInfo);
		
		this.owlMapUtils = new AOwlMapUtils() {

			@Override
			public MapLengthUnits getSystemLengthUnit() {
				return OwlMapControl.this.systemLengthUnit;
			}
			
		};
	}
	
	/**
	 * Get the IOwlMapUtils configured for this system.
	 * @return an IOwlMapUtils instance
	 */
	@Override
	public IOwlMapUtils getMapUtils() {
		return owlMapUtils;
	}

	/**
	 * Make the map JComponent and start the mapping system.   
	 * THIS METHOD MUST BE RUN FROM THE GUI THREAD!!
	 * @param mapOptions A dictionary of IMapOptions to use when creating the map
	 * @param onMapLoad A Runnable to run when the map has finished loading.
	 *  @return A JComponent that will display the map.
	 * @throws ClassNotFoundException If an unknown map option is encountered
	 */
	public JComponent start(IMixedDataDictionary mapOptions, Runnable onMapLoad)
			throws ClassNotFoundException {
		logger.log(LogLevel.INFO, "Map subsystem starting...");
		
		final JComponent result = cefUtils.start(defaultPage, mapOptions, ()->{ 
			logger.log(LogLevel.DEBUG, "[OwlMapControl.start()] Map code loaded...");
				SwingUtilities.invokeLater(()->{ // To ensure that this happens after this GUI event thread event completes.
					owlMap = makeOwlMap(cefUtils);
					
					// Register the system post-op cleanup event for the Javascript side to call when CEF calls are complete.
					// This event does NOT cover asynchronous events aattached to object!  The post-op cleanup call must be explicitly 
					// added to such event callbacks (see ICefObject.addEvent() methods implementation).
					cefUtils.addSysEventVoid(ICefDefs.SYS_EVENT_NAME_POST_OP, cefUtils::runPostOpCleanup);
					
					owlMap.setMapEvent(MapEventType.IDLE, ()->{ 
						logger.log(LogLevel.DEBUG, "Map is idle.");
					});
					mapComponentFac = makeMapComponentFactory(cefUtils);
					logger.log(LogLevel.DEBUG, "[OwlMapControl.start()] External OwlMap and map component factory created."); 
					// TODO use Thread pool?
					(new Thread(()->{
						onMapLoad.run();
						SwingUtilities.invokeLater(cefUtils::runPostOpCleanup); // Do post-op cleanup just in case.
					})).start(); // Don't block the GUI thread.
				});
			});
		
		
//		browser.on(ConsoleMessageReceived.class, event -> {
//			ConsoleMessage consoleMessage = event.consoleMessage();
//			ConsoleMessageLevel level = consoleMessage.level();
//			String message = consoleMessage.message();
//			loggerFn.accept("[Map Console] " + level + ": " + message);
//		});
//		loggerFn.accept("[OwlMapControl.start()] Google maps internal logging function installed.");
		
//		browser.navigation().on(FrameDocumentLoadFinished.class, event -> {
//		    Frame frame = event.frame();
//		    ICefObject window = frame.executeJavaScript("window");
//		    loggerFn.accept("[OwlMapControl.start()] window.propertyNames() = "+ window.propertyNames());
//		});
//		browser.navigation().loadUrlAndWait(new File(defaultPage).getAbsolutePath());

		
//		BrowserView browserView = BrowserView.newInstance(browser);
		
//		jxFrame = browser.mainFrame().get();
//		jsWindow = jxFrame.executeJavaScript("window");
//		if (jsWindow != null) {
//			loggerFn.accept("[OwlMapControl.start()] jsWindow.propertyNames() = "+ jsWindow.propertyNames());
//			
//			ICefObject jsMapOptions =  mapOptionsFiller.makeOptions(mapOptions);
//			loggerFn.accept("[OwlMapControl.start()] map options created."); 
//			jsWindow.call("initialize", googleMapsApiKey, jsMapOptions, 
//				(JsFunctionCallback) args -> {
//					loggerFn.accept("[OwlMapControl.start()] internal Javascript map created."); 
//					// Careful! race condition between jsUtils and owlMap?
//					
//					owlMap = makeOwlMap(cefUtils);
//					mapComponentFac = makeMapComponentFactory(cefUtils);
//					loggerFn.accept("[OwlMapControl.start()] External OwlMap and map component factory created."); 
//					onMapLoad.run();
//					return null;
//				}		
//			);
//		} 
//		else {
//			String errorMsg = "[OwlMapControl.start()] ERROR: Map web page has a null window object!";
//			System.err.println(errorMsg);
//			loggerFn.accept(errorMsg);
//		}
		logger.log(LogLevel.INFO, "Map UI component instantiated and being returned to be installed into parent component...");

//		BrowserView browserView = BrowserView.newInstance(browser);
//
//		return browserView;
		return result;
	}

//	@Override
	/**
	 * Experimental method.  Not fully implemented yet.
	 * @param mapOptions  New options for the map
	 * @param onMapLoad Callback for when the map reloads
	 */
	public void resetMap(IMixedDataDictionary mapOptions, Runnable onMapLoad) {
		
		
//		browser.navigation().loadUrlAndWait("https://www.rice.edu");
//		browser.navigation().loadUrlAndWait(new File(defaultPage).getAbsolutePath());
//		jxFrame = browser.mainFrame().get();
//		jsWindow = jxFrame.executeJavaScript("window");
//		jsWindow.call("initialize", googleMapsApiKey, mapOptionsFiller.makeOptions(mapOptions), 
//				(JsFunctionCallback) args -> {
//		
//					// Careful! race condition between jsUtils and owlMap
//					
//					owlMap = makeOwlMap(jsUtils);
//					mapComponentFac = makeMapComponentFactory(jsUtils);
//					onMapLoad.run();
//					return null;
//				}		
//			);		
	}

//	/**
//	 * Utility method to execute arbitrary JavaScript code in the map web page
//	 * 
//	 * @param js The JavaScript code to execute
//	 * @return The result of executing the given Javascript code
//	 */
//	public Object executeJavaScript(String js) {
//		return jxFrame.executeJavaScript(js);
//	}


	/**
	 * Get the map object
	 * 
	 * @return The map object
	 */
	@Override
	public IOwlMap getMap() {
		return owlMap;
	}
	
	/**
	 * Make a new IOwlMap using the given IJsUtils utility. 
	 * Note that the underlying Javascript map has already been made.
	 * @param cefUtils The IJsUtils utility to use
	 * @return A new IOwlMap object
	 */
	private IOwlMap makeOwlMap(final ICefUtils cefUtils) {
		
		System.out.println("[IOWlMap constructor] New map, ID = "+cefUtils.getCefMapObj().getCefId());
		return new IOwlMapWrapped() {

			@Override
			public ICefObject getWrappedCefObject() {
				return cefUtils.getCefMapObj();
			}

			@Override
			public ILatLng getCenter() {
				return this.call("getCenter", ILatLng.class);
			}

			@Override
			public void setCenter(ILatLng latLng) {
				this.callVoid("setCenter", new GsonTypeWrap(latLng, ILatLng.class) );
			}

			@Override
			public MapTypeId getMapTypeId() {
				return MapTypeId.fromString(this.call("getMapTypeId",String.class));
			}

			@Override
			public void setMapTypeId(MapTypeId mapTypeID) {
				this.call("setMapTypeId",String.class, mapTypeID.getName());
				
			}

			@Override
			public int getZoom() {
				double zoom = this.call("getZoom", Double.class);// returns a double because it's coming from Javascript
				return (int) Math.round(zoom);
			}

			@Override
			public void setZoom(int zoom) {
				this.callVoid("setZoom", zoom);
			}

			@Override
			public void goTo(IPlace place) {
				panTo(place.getLatLng());
				setZoom(place.getZoom());
			}

			@Override
			public void panTo(ILatLng latLng) {
				this.callVoid("panTo",new GsonTypeWrap( latLng, ILatLng.class) );
			}

			@Override
			public void setOptions(IMixedDataDictionary options) throws ClassNotFoundException {
				this.callVoid("setOptions", IMapOptions.class, options); //new GsonTypeWrap(options, IMapOptions.class));
			}

			@Override
			public void setMapEvent(MapEventType eventType, Runnable eventFn) {
				System.out.println("[IOwlMap.setMapEvent()] Setting event for \""+eventType.getName()+"\".");
				
				this.addEvent(eventType.getName(), ()->{
					System.out.println("["+eventType.getName()+"] Executing!");
					eventFn.run();
				});

//				cefUtils.call("addEventTo", getWrappedCefObject(), eventType.getName(), (JsFunctionCallback) (args)->{
//					eventFn.run();
//					return null;
//				});
			}
			
			@Override
			public void setMapMouseEvent(MapMouseEventType eventType, Consumer<IMouseEvent> eventFn) {
				System.out.println("[IOwlMap.setMapMouseEvent()] Setting event for \""+eventType.getName()+"\".");
				this.addEvent(eventType.getName(), (IMouseEvent mouseEvent)->{
					System.out.println("[IOwlMap: "+eventType.getName()+"] mouseEvent = "+mouseEvent);
					eventFn.accept(mouseEvent);
				}, IMouseEvent.class);
				
				
//				cefUtils.call("addEventTo", getWrappedCefObject(), eventType.getName(), new GsonTypeWrap((Consumer<Object>) (args)->{
//					eventFn.accept(new IMouseEvent() {
//						
//					final ILatLng latLng; // = cefUtils.fromJsLatLng((ICefObject) ((ICefObject)args[0]).property("latLng").get());
//						
//						@Override
//						public ILatLng getLatLng() {
//			
//							return latLng;
//						}
//					});
//				}, Consumer.class));
			}
			
			@Override
			public void setMapMouseDragEvent(MapMouseDragEventType eventType, Runnable eventFn) {
				System.out.println("[IOwlMap.MapMouseDragEventType()] Setting event for \""+eventType.getName()+"\".");
				
//				cefUtils.callVoid("addEventTo", getWrappedCefObject(), eventType.getName(),  new GsonTypeWrap((Runnable)()-> {
//					System.out.println("[IOwlMap: "+eventType.getName()+"] Executing. ");	
//					eventFn.run();
//				}, Runnable.class));
				
				this.addEvent(eventType.getName(), ()->{
					System.out.println("[IOwlMap: "+eventType.getName()+"] Executing!");
					eventFn.run();
				});
			}
			
//			/**
//			 * Internal utility factory to instantiate IMapDataFeature objects
//			 * @param id The ID of the feature	
//			 * @param jsFeature The Javascript feature object
//			 * @return A new IMapDataFeature instance
//			 */
//			private IMapDataFeature makeIMapDataFeature(final String id, final ICefObject jsFeature) {
//				
//				return new IMapDataFeatureWrapped() {
//					
//					@Override
//					public ICefObject getWrappedCefObject() {
//						return jsFeature;
//					}
//
//					@Override
//					public String getMapDataFeatureId() {
//						return id;
//					}
//					
//					@Override 
//					public String toString() {
//						return "IMapDataFeature("+getMapDataFeatureId()+")";
//					}
//
//					@Override
//					public Map<String, String> getProperties() {
//						// TODO Auto-generated method stub
//						return null;
//					}
//
//					@Override
//					public IMapDataGeometry getGeometry() {
//						// TODO Auto-generated method stub
//						return null;
//					}
//					
//				};
//			}
			
			
			@Override
			public void loadGeoJson(String url, Consumer<Map<String, IMapDataFeature>> callback) {

				
				// WARNING:  Minimize the processing of the returned features because calls to the underlying Javascript objects
				// during the callback appears to cause deadlocking problems. -- this was with JxBrowser but is this still an issue?
				
				cefUtils.callVoid("loadGeoJson", url, new GsonTypeWrap((Consumer<?>)(param)->{
//					callback.accept((IListMapDataFeature) cefUtils.convertJsonMap(param, IListMapDataFeature.class));
					
					IListMapDataFeature features = (IListMapDataFeature) cefUtils.convertJsonMap(param, IListMapDataFeature.class);
					Map<String, IMapDataFeature> featuresMap = new HashMap<String, IMapDataFeature>();  // Should this be unmodifiable?
					features.forEach((feature)->{  // Block until return value from Javascript side is ready.
						featuresMap.put(feature.getMapDataFeatureId(), feature);
					});
					callback.accept(featuresMap);
				}, Consumer.class));
				
			}
			

			
			@Override
			public void addGeoJson(String geoJsonStr, Consumer<Map<String, IMapDataFeature>> callback) {
				(new Thread(()->{
					try {
						Future<IListMapDataFeature> featuresFuture =  cefUtils.call("addGeoJson", IListMapDataFeature.class, geoJsonStr);
						Map<String, IMapDataFeature> featuresMap = new HashMap<String, IMapDataFeature>();  // Should this be unmodifiable?
						featuresFuture.get().forEach((feature)->{  // Block until return value from Javascript side is ready.
							featuresMap.put(feature.getMapDataFeatureId(), feature);
						});
						callback.accept(featuresMap);
					} catch (InterruptedException | ExecutionException e) {
						System.err.println("[IOwlMap.addGeoJson()] Exception while processing GeoJson data: "+e);
						e.printStackTrace();
					}					
				})).start();
			}


			
			@Override
			public void setDataStyle(IMixedDataDictionary mapDataStyleOptions) {
				
				
				cefUtils.callVoid("callMapDataVoid", "setStyle", new GsonTypeWrap(mapDataStyleOptions, IMapDataStyleOptions.class));
			}
			
			@Override
			public void overrideDataStyleById(String featureId, IMixedDataDictionary featureDataStyleOptions) {
				// TODO Check that the featureId exists on the map first
				
				cefUtils.callVoid("callMapDataVoidXform", "overrideStyle", "xformMapDataFeatureId", featureId, new GsonTypeWrap(featureDataStyleOptions, IMapDataStyleOptions.class));
			}

			@Override
			public void revertDataStyleById(String featureId) {
//				// TODO Check that the featureId exists on the map first

				cefUtils.callVoid("callMapDataVoidXform", "revertStyle", "xformMapDataFeatureId");
			}
			
			@Override
			public void revertDataStyleAll() {
				cefUtils.callVoid("callMapDataVoid", "revertStyle");
			}
		};
	}

	

	

	/**
	 * Stop the map engine. Must be called when app exits or engine will keep
	 * running.
	 */
	@Override
	public void stop() {
		// TODO What needs to be done on stop()?
//		engine.close();
	}
	
	
	/**
	 * Get the current IMapComponentFactory object 
	 * @return The current IMapComponentFactory object
	 */
	@Override
	public IMapComponentFactory getMapComponentFactory() {
		return mapComponentFac;
	}
	
	/**
	 * Make a new IMapComponentFactory object
	 * @param cefUtils The IJsUtils utility to use
	 * @return A new IMapComponentFactory object
	 */
	private IMapComponentFactory makeMapComponentFactory(final ICefUtils cefUtils) {
		// TODO Implement this!
		//throw new UnsupportedOperationException("[IOwlMapControl.makeMapComponentFactory()]");
//		return null;
		
		return new IMapComponentFactory() {
			

			
//			/**
//			 * Internal utility method to set ComponentMouseEventType on the given Javascript Map component object
//			 * @param jsObject  The Javascript object to set the event on
//			 * @param eventType The type of the event
//			 * @param eventFn The callback to run when the event occurs
//			 */
//			private void setComponentMouseEventOn(ICefObject jsObject, ComponentMouseEventType eventType, Consumer<IMouseEvent> eventFn) {
//				System.out.println("[IMapComponentFactory.setComponentMouseEventOn()] Setting event for \""+eventType.getName()+"\".");
//				
//				jsObject.addEvent(eventType.getName(), (IMouseEvent mouseEvent)->{
//					System.out.println("[Map component ID = "+jsObject.getCefId()+": "+eventType.getName()+"] mouseEvent = "+mouseEvent);
//					eventFn.accept(mouseEvent);
//				}, IMouseEvent.class);
//				
//				
////				cefUtils.call("addEventTo", jsObject, eventType.getName(), (JsFunctionCallback) (args)->{
////					eventFn.accept(new IMouseEvent() {
////						
////						final ILatLng latLng = cefUtils.fromJsLatLng((ICefObject) ((ICefObject)args[0]).property("latLng").get());
////						
////						@Override
////						public ILatLng getLatLng() {
////			
////							return latLng;
////						}
////					});
////					return null;
////				});
//			}
//			
//			/**
//			 * Internal utility method to set ComponentMouseDragEventType on the given Javascript Map component object
//			 * @param jsObject  The Javascript object to set the event on
//			 * @param eventType The type of the event
//			 * @param eventFn The callback to run when the event occurs
//			 */
//			private void setComponentMouseDragEventOn(ICefObject jsObject, ComponentMouseDragEventType eventType, Consumer<IMouseEvent> eventFn) {
//				System.out.println("[IMapComponentFactory.setComponentMouseDragEventOn()] Setting event for \""+eventType.getName()+"\".");
//				
//				cefUtils.callVoid("addEventTo", jsObject, eventType.getName(),  new GsonTypeWrap((Consumer<IMouseEvent>)(IMouseEvent mouseEvent)-> {
//					System.out.println("[Map component ID = "+jsObject.getCefId()+": "+eventType.getName()+"] mouseEvent = "+mouseEvent);	
//					eventFn.accept(mouseEvent);
//				}, Consumer.class));
//				
//				
////				cefUtils.call("addEventTo", jsObject, eventType.getName(), (JsFunctionCallback) (args)->{
////					eventFn.accept(new IMouseEvent() {
////						
////						final ILatLng latLng = cefUtils.fromJsLatLng((ICefObject) ((ICefObject)args[0]).property("latLng").get());
////						
////						@Override
////						public ILatLng getLatLng() {
////			
////							return latLng;
////						}
////					});
////					return null;
////				});
//			}
			
			
			@Override	
			public IMarker makeMarker(IMixedDataDictionary options) throws ClassNotFoundException {
				GsonTypeWrap jsOptions = new GsonTypeWrap(options, IMarkerOptions.class);
				
//				IOptionsFiller markerLabelOptionsFiller = IMarkerLabelOptions.makeOptionsFiller(cefUtils);
				
//				cefUtils.printICefObject(jsOptions, "(Marker) ");
						
				try {
					return new IMarkerWrapped() {
//						ICefObject jsMarker  = (ICefObject) cefUtils.call("makeMapComponent", "Marker", jsOptions).get();
						ICefObject jsMarker  =   cefUtils.makeCefObj("Marker", jsOptions); //  (ICefObject) cefUtils.call("makeMapComponent", "Marker", jsOptions).get();  adsfdsaf
						
						{
							System.out.println("[IMarker constructor] New marker, ID = "+jsMarker.getCefId());
						}
						
						@Override
						public ICefObject getWrappedCefObject() {
							return jsMarker;
						}
						
						@Override
						public ILatLng getPosition() {
//							return  cefUtils.fromJsLatLng( getWrappedICefObject().call("getPosition"));
							return  this.call("getPosition", ILatLng.class);
						}
						
						@Override
						public void setPosition(ILatLng latLng) {
//							getWrappedICefObject().call("setPosition", cefUtils.toJsLatLng(latLng));
							this.callVoid("setPosition", new GsonTypeWrap(latLng, ILatLng.class));
						}

						@Override
						public IMarkerLabel getLabel() {
//							ICefObject jsMarkerLabel = getWrappedICefObject().call("getLabel");
							
//							ICefObject jsMarkerLabel = this.call("getLabel", ICefObject.class);
							
							IMixedDataDictionary options = IMarkerLabelOptions.makeDefault();
//							if(jsMarkerLabel.hasProperty("color")) options.put(IMarkerLabelOptions.COLOR, (String) jsMarkerLabel.property("color").get());
//							if(jsMarkerLabel.hasProperty("fontFamily")) options.put(IMarkerLabelOptions.FONT_FAMILY, (String) jsMarkerLabel.property("fontFamily").get());
//							if(jsMarkerLabel.hasProperty("fontSize")) options.put(IMarkerLabelOptions.FONT_SIZE, (String) jsMarkerLabel.property("fontSize").get());
//							if(jsMarkerLabel.hasProperty("fontWeight")) options.put(IMarkerLabelOptions.FONT_WEIGHT, (String) jsMarkerLabel.property("fontWeight").get());
//							if(jsMarkerLabel.hasProperty("text")) options.put(IMarkerLabelOptions.TEXT, (String) jsMarkerLabel.property("text").get());
							return IMarkerLabel.makeFromOptions(options);
						}

						@Override
						public void setLabel(IMarkerLabel label) {
							this.callVoid("setLabel", new GsonTypeWrap(label.getOptions(), IMarkerLabelOptions.class));
						}

						@Override
						public String getTitle() {
							return this.call("getTitle", String.class);
						}

						@Override
						public void setTitle(String title) {
							this.callVoid("setLabel", title);
						}

						@Override
						public void setComponentMouseEvent(ComponentMouseEventType eventType, Consumer<IMouseEvent> eventFn) {
							
							this.addEvent(eventType.getName(), (IMouseEvent mouseEvent)->{
								System.out.println("[IMarker title = "+this.getTitle()+": "+eventType.getName()+"] mouseEvent = "+mouseEvent);
								eventFn.accept(mouseEvent);
							}, IMouseEvent.class);
							
//							setComponentMouseEventOn(getWrappedCefObject(), eventType, eventFn);
							
						}
						
						@Override
						public void setComponentMouseDragEvent(ComponentMouseDragEventType eventType, Consumer<IMouseEvent> eventFn) {
							this.addEvent(eventType.getName(), (IMouseEvent mouseEvent)->{
								System.out.println("[IMarker title = "+this.getTitle()+": "+eventType.getName()+"] mouseEvent = "+mouseEvent);
								eventFn.accept(mouseEvent);
							}, IMouseEvent.class);
							
//							setComponentMouseDragEventOn(getWrappedCefObject(), eventType, eventFn);						
						}

						@Override
						public void setMarkerEvent(MarkerEventType eventType, Runnable eventFn) {
							System.out.println("[IMarker.setMarkerEventType()] Setting event for \""+eventType.getName()+"\".");
							
							this.addEvent(eventType.getName(),  ()->{
								System.out.println("[IMarker title = "+this.getTitle()+": "+eventType.getName()+"] Executing.");
								eventFn.run();
							});
							
							
//							cefUtils.call("addEventTo", getWrappedCefObject(), eventType.getName(), (JsFunctionCallback) (args)->{
//								eventFn.run();
//								return null;
//							});
						}

						@Override
						public boolean getClickable() {
							return this.call("getClickable", Boolean.class);					}

						@Override
						public void setClickable(boolean isClickable) {
							this.callVoid("setClickable", isClickable);	
							
						}

						@Override
						public boolean getDraggable() {
							return this.call("getDraggable", Boolean.class);	
						}

						@Override
						public void setDraggable(boolean isDraggable) {
							this.callVoid("setDraggable", isDraggable);	
						}

						@Override
						public Point2D.Double getPixelOffset() {
//							if(getWrappedICefObject().hasProperty("anchorPoint")) {
//								return cefUtils.fromJsPoint((ICefObject) jsMarker.property("anchorPoint").get());
//							}
//							else {
//								System.out.println("[IMarker.getPixelOffset()] Underlying Javascript object has no property 'anchorPoint'.");
//								return new Point2D.Double(0.0,0.0);
//							}
							return null;
						}

						@Override
						public void setOptions(MixedDataDictionary options) throws ClassNotFoundException {
							this.callVoid("setOptions", new GsonTypeWrap(options, IMarkerOptions.class));
						}

						@Override
						public double getZIndex() {
							return this.call("getZIndex", Double.class);
						}

						@Override
						public void setZIndex(double zIndex) {
							this.callVoid("setZIndex", zIndex);		
						}

//					@Override
//					public IMarkerShape getShape() {
//						throw new UnsupportedOperationException("Not yet implemented!");
//					}
//
//					@Override
//					public void setShape(IMarkerShape shape) {
//						throw new UnsupportedOperationException("Not yet implemented!");
//					}

						@Override
						public double getOpacity() {
							
//							throw new UnsupportedOperationException("OwlMapControl");
							return this.call("getOpacity", Double.class);
						}

						@Override
						public void setOpacity(double opacity) {
//							throw new UnsupportedOperationException("OwlMapControl");
							this.callVoid("setOpacity", opacity);	
							
						}

						@Override
						public String getIcon() {
//							throw new UnsupportedOperationException("OwlMapControl");
							return this.call("getIcon", String.class);
						}

						@Override
						public void setIcon(String url) {
//							throw new UnsupportedOperationException("OwlMapControl");
							this.callVoid("setIcon", url);	
						}

						@Override
						public String getCursor() {
//						throw new UnsupportedOperationException("OwlMapControl");
							return this.call("getCursor", String.class);
						}

						@Override
						public void setCursor(String cursor) {
//						throw new UnsupportedOperationException("OwlMapControl");
							this.callVoid("setCursor", cursor);
						}

//					@Override
//					public String getAnimation() {
//						return getWrappedICefObject().call("getAnimation");
//					}
//
//					@Override
//					public void setAnimation(String animation) {
//						getWrappedICefObject().call("setAnimation", animation);
//					}		
						
						
					};
				} catch (Exception e) {
					System.err.println("[IMapComponentFactory.makeMarker()] Returning null!! Exception: "+e);
					e.printStackTrace();
					
				} 
				return null;
			}

			@Override
			public IInfoWindow makeInfoWindow(IMixedDataDictionary options) throws ClassNotFoundException {
//				System.out.println("[IMapComponentFactory.makeInfoWindow()] option keys = "+options.getKeys(IInfoWindowOptions.OPTIONS_ID));
				
				// safety check in case options were manually created.
				if(!options.containsKey(IInfoWindowOptions.VISIBLE)) {
					options.put(IInfoWindowOptions.VISIBLE, false);
					System.err.println("[OwlMapControl.makeInfoWindow()] No VISIBLE option specified!  Defaulting to VISIBLE = false.");
				}
				
//				throw new UnsupportedOperationException("[IOwlMapControl IMapComponentFactory.makeInfoWindow()]");
				
				GsonTypeWrap jsOptions = new GsonTypeWrap(options, IInfoWindowOptions.class);
				
				IInfoWindowWrapped result = new IInfoWindowWrapped() {
					ICefObject jsInfoWindow  = cefUtils.makeCefObj("InfoWindow", jsOptions); //cefUtils.call("makeMapComponent", "InfoWindow", infoWindowOptionsFiller.makeOptions(options));
					
					boolean isVisible = options.get(IInfoWindowOptions.VISIBLE);
					
					IAnchor anchor = options.get(IInfoWindowOptions.ANCHOR); 
					
					{
						System.out.println("[IInfoWindow constructor] New info window, ID = "+jsInfoWindow.getCefId());
						 // Need to set this event because window can be closed separately from calling setVisible(false);
						this.setInfoWindowEvent(InfoWindowEventType.CLOSE_CLICK, ()->{
							System.out.println("[IInfoWindow CLOSE_CLICK] Closed using 'x' icon.");
							isVisible = false;
						});
						
						setVisible(isVisible);
					}
					
					@Override
					public ICefObject getWrappedCefObject() {
						return jsInfoWindow;
					}
					
					@Override 
					public String getContent() {
						return this.call("getContent", String.class);
					}
					
					public void setContent(String str) {
						this.callVoid("setContent", str);
					}


					
					/**
					 * Need to override this method b/c underlying InfoWindow Javascript object does not have the normal setVisible() capability.
					 */
					@Override
					public void setVisible(boolean isVisible) {
						System.out.println("IInfoWindow.setVisible("+isVisible+") called.");
						this.isVisible = isVisible;
						if(isVisible) {
							if(null == anchor) {
								// If no anchor, must have position in order to open
								if( null == this.call("getPosition", ILatLng.class)) {
									String errorMsg = "[IInfoWindow.setVisble()] In order to open an InfoWindow, either a Position or an Anchor must be specified!";
									System.err.println(errorMsg);
									throw new IllegalStateException(errorMsg);
								}
								else {
									this.callVoid("open", new GsonTypeWrap(cefUtils.getCefMapObj(), ICefObjRef.class));	
								}
							}
							else {
								// Having an anchor overrides having a position.
								this.callVoid("open", new GsonTypeWrap(cefUtils.getCefMapObj(), ICefObjRef.class), new GsonTypeWrap(anchor, ICefObjRef.class));
//								throw new UnsupportedOperationException("[IOwlMapControl IMapComponentFactory.makeInfoWindow()] InfoWindow has no anchor yet!");
//								this.callVoid("open", cefUtils.getCefMapObj(), anchor.getWrappedCefObject());
							}
						}
						else {
							this.callVoid("close");
						}
					}

					/**
					 * Need to override this method b/c underlying InfoWindow Javascript object does not have the normal getVisible() capability.
					 */
					@Override
					public boolean getVisible() {
						return isVisible;
					}


					@Override
					public ILatLng getPosition() {
						return  this.call("getPosition", ILatLng.class);
					}
					
					@Override
					public void setPosition(ILatLng latLng) {
						this.callVoid("setPosition", new GsonTypeWrap(latLng, ILatLng.class));
					}


					@Override
					public void setInfoWindowEvent(InfoWindowEventType eventType, Runnable eventFn) {
						System.out.println("[IMarker.setMarkerEventType()] Setting event for \""+eventType.getName()+"\".");
						
						this.addEvent(eventType.getName(), eventFn);
//						cefUtils.call("addEventTo", getWrappedICefObject(), eventType.getName(), (JsFunctionCallback) (args)->{
//							eventFn.run();
//							return null;
//						});
					}

					@Override
					public double getZIndex() {
						return this.call("getZIndex", Double.class);
					}

					@Override
					public void setZIndex(double zIndex) {
						this.callVoid("setZIndex", zIndex);		
					}

					@Override
					public void setOptions(MixedDataDictionary options) throws ClassNotFoundException {
						isVisible = options.get(IInfoWindowOptions.VISIBLE);
						
						anchor = options.get(IInfoWindowOptions.ANCHOR); 
						
//						jsInfoWindow.call("setOptions",  infoWindowOptionsFiller.makeOptions(options));
						
						this.callVoid("setOptions", new GsonTypeWrap(options, IInfoWindowOptions.class));
						
						
						setVisible(isVisible);
						
					}
					
				};
				

				return result;
				
			}

			@Override
			public IPolygon makePolygon(IMixedDataDictionary options) throws ClassNotFoundException {
				
//				ICefObject jsOptions = polygonOptionsFiller.makeOptions(options);
//				System.out.println("[makePolygon()] jsOptions = "+jsOptions.propertyNames());
//				jsUtils.printICefObject(jsOptions, " ");
				
//				throw new UnsupportedOperationException("[IOwlMapControl IMapComponentFactory.makePolygon()]");
				return new IPolygonWrapped() {
					
					ICefObject jsPolygon = cefUtils.makeCefObj("Polygon", new GsonTypeWrap(options, IPolygonOptions.class));

					@Override
					public ICefObject getWrappedCefObject() {
						return jsPolygon;
					}


					@Override
					public void setPolyMouseEvent(MapMouseEventType eventType, Consumer<IPolyMouseEvent> eventFn) {
						
						this.addEvent(eventType.getName(),  (IPolyMouseEvent polyMouseEvent)->{
							System.out.println("[IPolygon: "+eventType.getName()+"] polyMouseEvent = "+polyMouseEvent);
							eventFn.accept(polyMouseEvent);
						}, IPolyMouseEvent.class);
						
//						cefUtils.call("addEventTo", getWrappedICefObject(), eventType.getName(), (JsFunctionCallback) (args)->{
//							eventFn.accept(new IPolyMouseEvent() {
//								
//								final ILatLng latLng = cefUtils.fromJsLatLng((ICefObject) ((ICefObject)args[0]).property("latLng").get());
//								
//								@Override
//								public ILatLng getLatLng() {
//					
//									return latLng;
//								}
//
//								@Override
//								public int getEdge() {
//									int result = -1;
//									if(((ICefObject)args[0]).hasProperty("edge")) {
//										Integer x = (Integer) ((ICefObject)args[0]).property("edge").get();
//										if(null != x) {
//											result = x;
//										}
//									}
//									return result;
//								}
//
//								@Override
//								public int getPath() {
//									int result = -1;
//									if(((ICefObject)args[0]).hasProperty("path")) {
//										Integer x = (Integer) ((ICefObject)args[0]).property("path").get();
//										if(null != x) {
//											result = x;
//										}
//									}
//									return result;
//								}
//
//								@Override
//								public int getVertex() {
//									int result = -1;
//									if(((ICefObject)args[0]).hasProperty("vertex")) {
//										Integer x = (Integer) ((ICefObject)args[0]).property("vertex").get();
//										if(null != x) {
//											result = x;
//										}
//									}
//									return result;
//								}
//							});
//							return null;
//						});
					}

					
					@Override
					public void setComponentMouseDragEvent(ComponentMouseDragEventType eventType, Consumer<IMouseEvent> eventFn) {
						this.addEvent(eventType.getName(), (IMouseEvent mouseEvent)->{
							System.out.println("[IPolygon: "+eventType.getName()+"] mouseEvent = "+mouseEvent);
							eventFn.accept(mouseEvent);
						}, IMouseEvent.class);											
					}

					@Override
					public void setOptions(MixedDataDictionary options) throws ClassNotFoundException {
						this.callVoid("setOptions", new GsonTypeWrap(options, IPolygonOptions.class));
					}

					
					@Override
					public IPath<ILatLng> getPath() {
						return this.call("getPath", IPathLatLng.class);  // JSON is an array   (see old "jsUtils.fromJsArray")
					}

					@Override
					public void setPath(IPath<ILatLng> path) {
						this.callVoid("setPath", new GsonTypeWrap(path, IPathLatLng.class));  // JSON is an array   (see old "jsUtils.tosArray")
					}
					


					@Override
					public IPathList<ILatLng> getPaths() {
//						return cefUtils.fromJsArrays(getWrappedICefObject().call("getPaths"));
						return this.call("getPaths", IPathListLatLng.class);  
					}





					@Override
					public void setPaths(IPaths<ILatLng> paths) {
//						getWrappedICefObject().call("setPaths", cefUtils.toJsArray(paths));
						this.callVoid("setPaths", new GsonTypeWrap(paths, IPathsLatLng.class));
					}



//					@Override
//					public boolean getEditable() {
//						return getWrappedICefObject().call("getEditable");
//					}
//
//
//					@Override
//					public void setEditable(boolean isEditable) {
//						getWrappedICefObject().call("gsetEditable", isEditable);
//					}
					
				};
			
			}
			
			@Override
			public IPolyline makePolyline(IMixedDataDictionary options) throws ClassNotFoundException {
//				throw new UnsupportedOperationException("[IOwlMapControl IMapComponentFactory.makePolyline()]");
				
				return new IPolylineWrapped() {

					ICefObject jsPolyline = cefUtils.makeCefObj("Polyline", new GsonTypeWrap(options, IPolylineOptions.class));

					@Override
					public ICefObject getWrappedCefObject() {
						return jsPolyline;
					}

					@Override
					public void setPolyMouseEvent(MapMouseEventType eventType, Consumer<IPolyMouseEvent> eventFn) {
						System.out.println("[IPolyline.setPolyMouseEvent()] Setting event for \""+eventType.getName()+"\".");
						
						this.addEvent(eventType.getName(),  (IPolyMouseEvent polyMouseEvent)->{
							System.out.println("[IPolyline: "+eventType.getName()+"] polyMouseEvent = "+polyMouseEvent);
							eventFn.accept(polyMouseEvent);
						}, IPolyMouseEvent.class);
						
						
//						cefUtils.call("addEventTo", getWrappedICefObject(), eventType.getName(), (JsFunctionCallback) (args)->{
//							eventFn.accept(new IPolyMouseEvent() {
//								
//								final ILatLng latLng = cefUtils.fromJsLatLng((ICefObject) ((ICefObject)args[0]).property("latLng").get());
//								
//								@Override
//								public ILatLng getLatLng() {
//					
//									return latLng;
//								}
//
//								@Override
//								public int getEdge() {
//									int result = -1;
//									if(((ICefObject)args[0]).hasProperty("edge")) {
//										Integer x = (Integer) ((ICefObject)args[0]).property("edge").get();
//										if(null != x) {
//											result = x;
//										}
//									}
//									return result;
//								}
//
//								@Override
//								public int getPath() {
//									int result = -1;
//									if(((ICefObject)args[0]).hasProperty("path")) {
//										Integer x = (Integer) ((ICefObject)args[0]).property("path").get();
//										if(null != x) {
//											result = x;
//										}
//									}
//									return result;
//								}
//
//								@Override
//								public int getVertex() {
//									int result = -1;
//									if(((ICefObject)args[0]).hasProperty("vertex")) {
//										Integer x = (Integer) ((ICefObject)args[0]).property("vertex").get();
//										if(null != x) {
//											result = x;
//										}
//									}
//									return result;
//								}
//							});
//							return null;
//						});

					}
					
					@Override
					public void setComponentMouseDragEvent(ComponentMouseDragEventType eventType,
							Consumer<IMouseEvent> eventFn) {
						
						this.addEvent(eventType.getName(), (IMouseEvent mouseEvent)->{
							System.out.println("[IPolyline: "+eventType.getName()+"] mouseEvent = "+mouseEvent);
							eventFn.accept(mouseEvent);
						}, IMouseEvent.class);
						
//						setComponentMouseDragEventOn(getWrappedICefObject(), eventType, eventFn);				
					}

					@Override
					public void setOptions(MixedDataDictionary options) throws ClassNotFoundException {
						this.callVoid("setOptions", new GsonTypeWrap(options, IPolylineOptions.class));
					}

					@Override
					public IPath<ILatLng> getPath() {
						return this.call("getPath", IPathLatLng.class);  // JSON is an array   (see old "jsUtils.fromJsArray")
					}

					@Override
					public void setPath(IPath<ILatLng> path) {
						this.callVoid("setPath", new GsonTypeWrap(path, IPathLatLng.class));  // JSON is an array   (see old "jsUtils.tosArray")
					}

				};
				
			}
			
			@Override
			public IRectangle makeRectangle(IMixedDataDictionary options) throws ClassNotFoundException { 
//				throw new UnsupportedOperationException("[IOwlMapControl IMapComponentFactory.makeRectangle()]");
				
				return new IRectangleWrapped() {
					
					ICefObject jsRectangle = cefUtils.makeCefObj("Rectangle", new GsonTypeWrap(options, IRectangleOptions.class));
							
					@Override
					public ICefObject getWrappedCefObject() {
						return jsRectangle;
					}
//					@Override
//					public void setComponentMouseEvent(ComponentMouseEventType eventType,
//							Consumer<IMouseEvent> eventFn) {
//						setComponentMouseEventOn(getWrappedICefObject(), eventType, eventFn);
//					}
//
//					@Override
//					public void setComponentMouseDragEvent(ComponentMouseDragEventType eventType,
//							Consumer<IMouseEvent> eventFn) {
//						setComponentMouseDragEventOn(getWrappedICefObject(), eventType, eventFn);		
//					}
					
					@Override
					public void setComponentMouseEvent(ComponentMouseEventType eventType,
							Consumer<IMouseEvent> eventFn) {
//						setComponentMouseEventOn(getWrappedICefObject(), eventType, eventFn);
						this.addEvent(eventType.getName(), (IMouseEvent mouseEvent)->{
							System.out.println("[IRectangle: "+eventType.getName()+"] mouseEvent = "+mouseEvent);
							eventFn.accept(mouseEvent);
						}, IMouseEvent.class);
					}

					@Override
					public void setComponentMouseDragEvent(ComponentMouseDragEventType eventType,
							Consumer<IMouseEvent> eventFn) {
//						setComponentMouseDragEventOn(getWrappedICefObject(), eventType, eventFn);	
						
						this.addEvent(eventType.getName(), (IMouseEvent mouseEvent)->{
							System.out.println("[IRectangle: "+eventType.getName()+"] mouseEvent = "+mouseEvent);
							eventFn.accept(mouseEvent);
						}, IMouseEvent.class);
						
					}

					@Override
					public void setOptions(MixedDataDictionary options) throws ClassNotFoundException {
						this.callVoid("setOptions", new GsonTypeWrap(options, IRectangleOptions.class));
					}

					@Override
					public ILatLngBounds getBounds() {
//						return cefUtils.fromJsLatLngBounds(getWrappedICefObject().call("getBounds"));
						return this.call("getBounds", ILatLngBounds.class);
					}

					@Override
					public void setBounds(ILatLngBounds bounds) {
						this.callVoid("setBounds",  new GsonTypeWrap(bounds, ILatLngBounds.class));
					}

					@Override
					public void setRectangleEvent(RectangleEventType eventType, Runnable eventFn) {
						System.out.println("[IRectangle.setRectangleEvent()] Setting event for \""+eventType.getName()+"\".");
						
//						cefUtils.call("addEventTo", getWrappedICefObject(), eventType.getName(), (JsFunctionCallback) (args)->{
//							eventFn.run();
//							return null;
//						});
						
						this.addEvent(eventType.getName(),  ()->{
							System.out.println("[IRectangle: "+eventType.getName()+"] Executing.");
							eventFn.run();
						});
					
					}
					
				};
				
			}
			
			@Override
			public ICircle makeCircle(IMixedDataDictionary options) throws ClassNotFoundException { 
//				throw new UnsupportedOperationException("[IOwlMapControl IMapComponentFactory.makeCircle()]");
				
				//GsonTypeWrap jsOptions = new GsonTypeWrap(options, ICircleOptions.class);
				
				
				return new ICircleWrapped() {
					
					ICefObject jsCircle = cefUtils.makeCefObj("Circle", new GsonTypeWrap(options, ICircleOptions.class));
							
					@Override
					public ICefObject getWrappedCefObject() {
						return jsCircle;
					}

					@Override
					public void setComponentMouseEvent(ComponentMouseEventType eventType,
							Consumer<IMouseEvent> eventFn) {
//						setComponentMouseEventOn(getWrappedICefObject(), eventType, eventFn);
						this.addEvent(eventType.getName(), (IMouseEvent mouseEvent)->{
							System.out.println("[ICircle: "+eventType.getName()+"] mouseEvent = "+mouseEvent);
							eventFn.accept(mouseEvent);
						}, IMouseEvent.class);
					}

					@Override
					public void setComponentMouseDragEvent(ComponentMouseDragEventType eventType,
							Consumer<IMouseEvent> eventFn) {
//						setComponentMouseDragEventOn(getWrappedICefObject(), eventType, eventFn);	
						
						this.addEvent(eventType.getName(), (IMouseEvent mouseEvent)->{
							System.out.println("[ICircle: "+eventType.getName()+"] mouseEvent = "+mouseEvent);
							eventFn.accept(mouseEvent);
						}, IMouseEvent.class);
						
					}

					@Override
					public void setOptions(MixedDataDictionary options) throws ClassNotFoundException {
						this.callVoid("setOptions",new GsonTypeWrap(options, ICircleOptions.class));
					}

					@Override
					public ILatLngBounds getBounds() {
//						return cefUtils.fromJsLatLngBounds(getWrappedICefObject().call("getBounds"));
						return this.call("getBounds", ILatLngBounds.class);
					}

					@Override
					public ILatLng getCenter() {
//						return cefUtils.fromJsLatLng(getWrappedICefObject().call("getCenter"));
						return this.call("getCenter", ILatLng.class);
					}

					@Override
					public void setCenter(ILatLng center) {
//						getWrappedICefObject().call("setCenter", cefUtils.toJsLatLng(center));
						this.callVoid("setCenter", new GsonTypeWrap(center, ILatLng.class));
					}

					@Override
					public double getRadius() {
						return this.call("getRadius", Double.class);
					}

					@Override
					public void setRadius(double radius) {
						this.callVoid("setRadius", radius);
					}

					@Override
					public void setCircleEvent(CircleEventType eventType, Runnable eventFn) {
						System.out.println("[ICircle.setCircleEvent()] Setting event for \""+eventType.getName()+"\".");
						
//						cefUtils.call("addEventTo", getWrappedICefObject(), eventType.getName(), (JsFunctionCallback) (args)->{
//							eventFn.run();
//							return null;
//						});
						
						this.addEvent(eventType.getName(),  ()->{
							System.out.println("[ICircle: "+eventType.getName()+"] Executing.");
							eventFn.run();
						});
					}


				};
			}
			
			@Override
			public IGroundOverlay makeGroundOverlay(IMixedDataDictionary options) throws ClassNotFoundException { 
//				throw new UnsupportedOperationException("[IOwlMapControl IMapComponentFactory.makeGroundOverlay()]");
								
				return new IGroundOverlayWrapped() {

					Function<IMixedDataDictionary, String> getURLFrom = (optionsDict) ->{
						return optionsDict.get(IGroundOverlayOptions.URL);
					};
					
					Function<IMixedDataDictionary, GsonTypeWrap> getBoundsFrom = (optionsDict) ->{
						return new GsonTypeWrap(optionsDict.get(IGroundOverlayOptions.BOUNDS), ILatLngBounds.class);
					};
					
					Function<IMixedDataDictionary, ICefObject> makeInstance = (optionsDict) ->{
						return cefUtils.makeCefObj("GroundOverlay", getURLFrom.apply(optionsDict), getBoundsFrom.apply(optionsDict), new GsonTypeWrap(optionsDict, IGroundOverlayOptions.class));
					};
					
					 ICefObject jsGroundOverlay = makeInstance.apply(options);
					 
					@Override
					public ICefObject getWrappedCefObject() {
						return jsGroundOverlay;
					}

					@Override
					public void setOptions(MixedDataDictionary options) throws ClassNotFoundException {
						this.setVisible(false); // Make this instance invisible first.
						
						// Need to create new underlying Javascript GroundOverlay object
						jsGroundOverlay = makeInstance.apply(options);
						
					}

					@Override
					public ILatLngBounds getBounds() {
						return this.call("getBounds", ILatLngBounds.class);
					}

					@Override
					public void setBounds(ILatLngBounds bounds) {
						this.callVoid("setBounds", new GsonTypeWrap(bounds, ILatLngBounds.class));
					}

					@Override
					public String getUrl() {
						return this.call("getUrl", String.class) ;
					}

					@Override
					public double getOpacity() {
						return this.call("getOpacity", Double.class);
					}

					@Override
					public void setOpacity(double opacity) {
						this.callVoid("setOpacity", opacity) ;
					}
					
					/**
					 * Set this object's visibility
					 * @param isVisible True if visible, false otherwise
					 */
					@Override
					public  void setVisible(boolean isVisible) {
						// The overlay's visiblity is controlled by whether or not it's map field is set.
						this.callVoid("setMap", isVisible ? new GsonTypeWrap(getMap(), ICefObjRef.class) : null);
					};
					
					/**
					 * Gets the object's current visibility 
					 * @return true if the object is visible, false otherwise.
					 */
					@Override
					public boolean getVisible() {
						// The overlay's visiblity is controlled by whether or not it's map field is set.
						ICefObjRef jsMap = this.call("getMap", ICefObjRef.class);
						return null != jsMap;	
					}

					@Override
					public void setGroundOverlayMouseEvent(GroundOverlayMouseEventType eventType,
							Consumer<IMouseEvent> eventFn) {
						System.out.println("[IGroundOverlay.setGroundOverlayMouseEvent()] Setting event for \""+eventType.getName()+"\".");
						
						this.addEvent(eventType.getName(),  (IMouseEvent mouseEvent)->{
							System.out.println("[IGroundOverlay: "+eventType.getName()+"] mouseEvent = "+mouseEvent);
							eventFn.accept(mouseEvent);
						}, IMouseEvent.class);
												
//						cefUtils.call("addEventTo",  getWrappedICefObject(), eventType.getName(), (JsFunctionCallback) (args)->{
//							eventFn.accept(new IMouseEvent() {
//								
//								final ILatLng latLng = cefUtils.fromJsLatLng((ICefObject) ((ICefObject)args[0]).property("latLng").get());
//								
//								@Override
//								public ILatLng getLatLng() {
//					
//									return latLng;
//								}
//							});
//							return null;
//						});
					};
				};
			}
				
			
		};
		
	}
	
	
	@Override
	public void setMakeDevTools(boolean makeDevTools) {
		cefUtils.setMakeDevTools(makeDevTools);
	}
}
