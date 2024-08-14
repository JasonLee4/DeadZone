package provided.owlMaps.cefUtils.impl;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.CefApp.CefAppState;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.CefMessageRouter.CefMessageRouterConfig;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefAppHandlerAdapter;
import org.cef.handler.CefLoadHandlerAdapter;
import org.cef.handler.CefMessageRouterHandlerAdapter;

import com.google.gson.Gson;

import provided.logger.LogLevel;
import provided.mixedData.IMixedDataDictionary;
import provided.owlMaps.cefUtils.ICefDefs;
import provided.owlMaps.cefUtils.ICefObject;
import provided.owlMaps.cefUtils.ICefObjectFactory;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.ISystemInfo;
import provided.owlMaps.cefUtils.gson.GsonTypeWrap;
import provided.owlMaps.cefUtils.gson.FuncParams;
import provided.owlMaps.cefUtils.gson.FuncParamsVoid;
import provided.owlMaps.cefUtils.gson.IGsonFactory;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.map.IMapOptions;


/**
 * Carrier object for the parameters from the JavaScript side back to the Java side 
 * with which to invoke an associated callback function.
 * Must have no param constructor for gson to use it
 * @author swong
 *
 */
class CallbackParams {
	/**
	 * The ID of the callback function
	 */
	private String callbackId;

	/**
	 * The parameters to be given to the callback function
	 */
	private Object[] params = new Object[] {};


	//	public CallbackParams(String callbackId, Object...params) {
	//		this.callbackId = callbackId;
	//		this.params = params;
	//	}

	/**
	 * Accessor for the callback function's ID
	 * @return The ID of the associated callback
	 */
	public String getCallbackId() {
		return this.callbackId;
	}

	/**
	 * Accessor for the parameters for the associated callback
	 * @return An array of parameters with which to call the callback function
	 */
	public Object[] getParams() {
		return params;
	}

}

/**
 * Carrier object for the return value from the JavaScript side back to the Java side.
 * Returned request object for CEF calls 
 * Must have no param constructor for gson to use it
 * @author swong
 *
 */
class ReturnValueParam {
	/**
	 * The ID of the request call
	 */
	private String requestId;

	/**
	 * The return value of the call
	 */
	private Object param = null;

	/**
	 * Accessor for the request call ID
	 * @return The ID of the request call
	 */
	public String getRequestId() {
		return this.requestId;
	}

	/**
	 * The return value of the request call
	 * @return The return value of the request call
	 */
	public Object getParam() {
		return param;
	}

}


/**
 * Implementation of ICefUtils
 * @author swong
 *
 */
public final class CefUtils implements ICefUtils {
	/**
	 * Format string for JavaScript call to run a given window function.
	 */
	private static final String JS_CALL_FN_FORMAT = "window.callFn(\"%s\", %s)";   // window.callFn("funcName", jsonArgString)

	/**
	 * Accessors for accessing dynamic system values
	 */
	private final ISystemInfo sysInfo;

	/**
	 * The Google Maps API key in use
	 */
	private final String googleMapsApiKey;

	/**
	 * The CEF client object 
	 */
	private final CefClient cefClient;

	/**
	 * GSON JSON converter to use for data transmission to/from the JavaScript side
	 */
	private Gson gson = IGsonFactory.make(this);

	/**
	 * If true the browser background is transparent
	 */
	boolean isTransparent = false;

	/**
	 * If true, create the dev tools window after the current devToolsDelay
	 */
	private boolean makeDevTools = MAKE_DEV_TOOLS_DEFAULT;
	/**
	 * The current delay to use before creating and displaying the dev tools window 
	 */
	private long devToolsDelay = DEV_TOOLS_DELAY_DEFAULT;
//	/**
//	 * The current port to use for the dev tools window
//	 */
//	private int devToolsPort = DEV_TOOLS_PORT_DEFAULT;
	/**
	 * If true, use Off-Screen Rendering
	 */
	private boolean useOSR = USE_OFF_SCREEN_RENDERING_DEFAULT;
	/**
	 * The factory to use to create ICefObjects
	 */
	private ICefObjectFactory cefObjFac =  ICefObjectFactory.makeFac(this);
	/**
	 * The message router to use for callbacks from the JavaScript side.   The callbacks are assumed to be persistent and utilized  multiple times.
	 */
	private CefMessageRouter callbackMsgRouter; // Must be initialized AFTER the CEF client is instantiated.
	/**
	 * The message router to use when creating objects on the JavaScript side
	 */
	private CefMessageRouter objCreateMsgRouter; // Must be initialized AFTER the CEF client is instantiated.
	/**
	 * The message router to use for return values from JavaScript method calls.  The callbacks are assumed to be single use for a particular function call's return value.
	 */
	private CefMessageRouter returnValMsgRouter; // Must be initialized AFTER the CEF client is instantiated.
	/**
	 * The map of Runnable function ID's to the Runnable itself.
	 */
	private Map<UUID, Runnable> runnableCallbackMap = new HashMap<UUID, Runnable>();
	/**
	 * The map of Consumer function ID's to the Consumer itself.
	 */
	private Map<UUID, Consumer<?>> consumerCallbackMap = new HashMap<UUID, Consumer<?>>();
	/**
	 * The map of Consumer function ID's to the Consumer itself where the Consumer is used for processing returned values from the JavaScript side.
	 */
	private Map<UUID, Consumer<?>> pendingReturnValMap = new HashMap<UUID, Consumer<?>>();
	/**
	 * The current CefBrowser instance holding the frame that holds the map
	 */
	CefBrowser cefBrowser;
	/**
	 * The current CefFrame holding the map in the browser
	 */
	CefFrame cefFrame;
	/**
	 * The current map object being held in the browser
	 */
	ICefObject cefMap;
	
	/**
	 * Operation used for post operations clean up.  
	 * Used as part of the SYS_EVENT_NAME_POST_OP system event callback to restore the Java side
	 * of the system to a proper working order.
	 */
	private Runnable postOpCleanup = () ->{
		System.out.println("[CEFUtils.postOpCleanup] Running post-op cleanup.");
		
		// Force CEF to release the keyboard focus.   This helps prevent text input lockup b/c those components can't get keyboard focus.
		KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
	};

	// Initialize the ICefUtils
	{  

		//				System.out.println("CefObject JSON = "+gson.toJson(cefObjFac.make(UUID.randomUUID()), ICefObject.class));
		//				
		//				System.out.println("Junk JSON = "+gson.toJson(new Junk(), Junk.class));



		//				client_.addMessageRouter(msgRouter);		

	}

	/**
	 * Constructor for the class
	 * @param googleMapsApiKey The Google Maps API key to use
	 * @param sysInfo ISytemInfo object to provide dynamically set system-wide info such as the unit of length in use.
	 */
	public CefUtils(String googleMapsApiKey, ISystemInfo sysInfo) {
		this.sysInfo = sysInfo;
		this.googleMapsApiKey = googleMapsApiKey;


		// (1) The entry point to JCEF is always the class CefApp. There is only one
		//     instance per application and therefore you have to call the method
		//     "getInstance()" instead of a CTOR.
		//
		//     CefApp is responsible for the global CEF context. It loads all
		//     required native libraries, initializes CEF accordingly, starts a
		//     background task to handle CEF's message loop and takes care of
		//     shutting down CEF after disposing it.
		CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
			@Override
			public void stateHasChanged(org.cef.CefApp.CefAppState state) {
				// Shutdown the app if the native CEF part is terminated
				if (state == CefAppState.TERMINATED) System.exit(0);
			}
		});
		CefSettings settings = new CefSettings();
		System.out.println("[CefUtils:263] useOSR before settings.windowless_rendering_enabled : " + useOSR);
		settings.windowless_rendering_enabled = useOSR;
		settings.remote_debugging_port = DEV_TOOLS_PORT_DEFAULT;
		System.out.println("[DemoModel.start()] settings.remote_debugging_port = "+settings.remote_debugging_port);
		
		String emptyArgs[] = {}; // Add this for Macs
		CefApp.startup(emptyArgs); // Add this for Macs
		
		CefApp cefApp = CefApp.getInstance(settings);

		// (2) JCEF can handle one to many browser instances simultaneous. These
		//     browser instances are logically grouped together by an instance of
		//     the class CefClient. In your application you can create one to many
		//     instances of CefClient with one to many CefBrowser instances per
		//     client. To get an instance of CefClient you have to use the method
		//     "createClient()" of your CefApp instance. Calling an CTOR of
		//     CefClient is not supported.
		//
		//     CefClient is a connector to all possible events which come from the
		//     CefBrowser instances. Those events could be simple things like the
		//     change of the browser title or more complex ones like context menu
		//     events. By assigning handlers to CefClient you can control the
		//     behavior of the browser. See tests.detailed.MainFrame for an example
		//     of how to use these handlers.
		cefClient = cefApp.createClient();

		// Can't create message routers until after the CEF client is created (or maybe just after CEF app is created)!   
		// Will get "unsatisfied link error" if created too soon, probably because the native code is being dynamically linked when the CEF app starts.
		callbackMsgRouter = CefMessageRouter.create(new CefMessageRouterConfig(ICefDefs.CALLBACK_FN_NAME, ICefDefs.CALLBACK_FN_NAME_CANCEL));
		objCreateMsgRouter = CefMessageRouter.create(new CefMessageRouterConfig(ICefDefs.OBJ_CREATE_FN_NAME, ICefDefs.OBJ_CREATE_FN_NAME_CANCEL));
		returnValMsgRouter = CefMessageRouter.create(new CefMessageRouterConfig(ICefDefs.RETURN_VALUE_FN_NAME, ICefDefs.RETURN_VALUE_FN_NAME_CANCEL));		

		callbackMsgRouter.addHandler(new CefMessageRouterHandlerAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, java.lang.String request, boolean persistent, CefQueryCallback callback) {
				System.out.println("[CefUtils.callbackMsgRouter: onQuery]  ("+queryId+") "+request); 

				CallbackParams callbackParams = gson.fromJson(request, CallbackParams.class);

				UUID uuid = UUID.fromString(callbackParams.getCallbackId());

				if(0==callbackParams.getParams().length) {
					// TODO Switch to using thread pool
					(new Thread(()->{  // Dispatch to new thread to avoid problems of blocking/locking this CEF-generated thread.
						runnableCallbackMap.getOrDefault(uuid, ()->{
							System.err.println("[ICefUtils.make() callbackMsgRouter] ERROR: No Runnable callback keyed to UUID = "+ uuid);
						}).run();
					})).start();
					callback.success("[callbackMsgRouter] Runnable ID =  "+uuid ); // Must make call to success() or will get "Unexpected call to CefQueryCallback_N::finalize()" error.
				}
				else if(1==callbackParams.getParams().length ){
					// TODO Switch to using thread pool
					(new Thread(()->{  // Dispatch to new thread to avoid problems of blocking/locking this CEF-generated thread.
						((Consumer<Object>)	consumerCallbackMap.getOrDefault(uuid, (param)->{
							System.err.println("[ICefUtils.make() callbackMsgRouter] ERROR: No Consumer callback keyed to UUID = "+ uuid+", callback param = "+param);
						})).accept(callbackParams.getParams()[0]);
					})).start();
					callback.success("[callbackMsgRouter] Consumer ID =  "+uuid ); // Must make call to success() or will get "Unexpected call to CefQueryCallback_N::finalize()" error.
				}
				else {
					System.err.println("[[ICefUtils.make() callbackMsgRouter]] (ID = "+uuid+") Too many callback parameters found: "+callbackParams.getParams().length);
					callback.failure(-1, "[callbackMsgRouter] Failure: Too many callback parameters ("+callbackParams.getParams().length+") ID = "+uuid );
				}


				return true;
			}
		}, true);

		cefClient.addMessageRouter(callbackMsgRouter);

		objCreateMsgRouter.addHandler(new CefMessageRouterHandlerAdapter() {
			@Override
			public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, java.lang.String request, boolean persistent, CefQueryCallback callback) {
				System.out.println("[CefUtils.objCreateMsgRouter: onQuery]  ("+queryId+") "+request); 

				ReturnValueParam returnValParam = gson.fromJson(request, ReturnValueParam.class);

				UUID requestId = UUID.fromString(returnValParam.getRequestId());

				UUID cefObjId = UUID.fromString((String) returnValParam.getParam());

				(new Thread(()->{
					cefObjFac.serviceObjectReturn(requestId, cefObjId);
				})).start();
				
				
				callback.success("[objCreateMsgRouter] Success cefObjId = "+cefObjId);  // Must make call to success() or will get "Unexpected call to CefQueryCallback_N::finalize()" error.)

				return true;
			}
		}, true);

		cefClient.addMessageRouter(objCreateMsgRouter);

		returnValMsgRouter.addHandler(new CefMessageRouterHandlerAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, java.lang.String request, boolean persistent, CefQueryCallback callback) {
				System.out.println("[CefUtils.returnValMsgRouter: onQuery]  ("+queryId+") "+request); 

				CallbackParams callbackParams = gson.fromJson(request, CallbackParams.class);

				UUID uuid = UUID.fromString(callbackParams.getCallbackId());

				if(1==callbackParams.getParams().length ){   // Must have a Consumer type callback
					// TODO Switch to using thread pool
					(new Thread(()->{  // Dispatch to new thread to avoid problems of blocking/locking this CEF-generated thread.
			
						Consumer<Object> callbackConsumer = (Consumer<Object>) pendingReturnValMap.remove(uuid);
						if (null == callbackConsumer) {
							callbackConsumer = (returnVal)->{
								System.err.println("[CefUtils returnValMsgRouter] ERROR: No Pending Return Value callback keyed to UUID = "+ uuid+", returnVal = "+returnVal);
							};
						}
//						System.out.println("!!!! [CefUtils returnValMsgRouter] Running return value Consumer...");
						callbackConsumer.accept(callbackParams.getParams()[0]);
					})).start();
					callback.success("[CefUtils returnValMsgRouter] Callback ID =  "+uuid ); // Must make call to success() or will get "Unexpected call to CefQueryCallback_N::finalize()" error.
				}
				else {
					System.err.println("[[CefUtils returnValMsgRouter]] (ID = "+uuid+") Invalid number of callback parameters found: "+callbackParams.getParams().length);
					callback.failure(-1, "[returnValMsgRouter] Failure: Invalid number of callback parameters ("+callbackParams.getParams().length+") ID = "+uuid );
				}


				return true;
			}
		}, true);

		cefClient.addMessageRouter(returnValMsgRouter);
		

	}

	@Override
	public JComponent start(String startURL, IMixedDataDictionary mapOptionsDict, Runnable onMapLoad ) {
		// Macs need full path from the root but must fix compatibility issues:
		// - Win uses back slashes instead of forward stashes in user.dir
		// - Spaces must be converted to "%20" to be URL compatible
		// - If user.dir starts with a "/" then need to convert the resultant "////" after "file:" back to "///".  Triple slash is equivalent to "//localhost/"
//		String startURLFull = ("file:///"+System.getProperty("user.dir")+startURL).replace("\\", "/").replace(" ", "%20").replace("////", "///");
		
		try {
			// Note:  startURL must be a relative location w.r.t. the working directory (bin).
			// The string representation of a file URL object does not have the syntax that CEF wants, that is, it will come out "file:/pathname"
			// not "file:///pathname" ("file:///" is a shortcut for "file://localhost/" see https://en.wikipedia.org/wiki/File_URI_scheme ).
			// Thus, below, "file://" is explicitly added to just the path portion of the URL which starts with a "/" since is relative to the drive root.
			// A URI object does not substitute "%20" for spaces. CEF will automatically convert spaces to %20 when obtaining a URL via frame.getURL() which is used below.
			String startURLFull  = "file://"+(new File(startURL)).toURI().toURL().getPath(); // Automatically converts backslashes and changes spaces to "%20". 

//			System.out.println("converted startURL = "+"file://"+(new File("provided/owlMaps/cefUtils/map.html")).toURI().toURL().getPath());
	
			System.out.println("[ICefUtils.start()] startURLFull = "+startURLFull);
	
			Consumer<String> onMapLoadCallback = (String mapIdStr)->{
				UUID mapId = UUID.fromString(mapIdStr);
				System.out.println("[ICefUtils.start() onMapLoad] Map ID = "+ mapId);
				cefMap = cefObjFac.make(mapId);
	
				// --- start debug code ---
	
				Object x;
				try {
	
					//						IPlace place = IPlace_Samples.SAMPLE_PLACES_DICT.get("TAJ_MAHAL");
					//						ILatLng centerLatLng = place.getLatLng();
					//						int zoom = place.getZoom();
	
					//						ILatLng centerLatLng = mapOptionsDict.get(IMapOptions.CENTER);
					//						int zoom = mapOptionsDict.get(IMapOptions.ZOOM);
					//						
					//						cefMap.callVoid("setCenter", new GsonTypeWrap(centerLatLng, ILatLng.class));
					//						cefMap.callVoid("setZoom", zoom);
	
					x = cefMap.call("getCenter", ILatLng.class).get();
					System.out.println("[CefUtils.start()] Raw map getCenter result = "+x+" "+x.getClass());
	
					//						cefMap.addEvent(MapMouseEventType.RIGHT_CLICK.getName(), (mouseEvent)->{
					//								System.out.println("["+MapMouseEventType.RIGHT_CLICK.getName()+"] mouseEvent = "+mouseEvent);
					//						}, IMouseEvent.class);
	
	
	
				} catch (InterruptedException | ExecutionException e) {
					System.err.println("[CefUtils.start()] Continuing on, even though there was an exception in the debug code: "+e);
					e.printStackTrace();
				}
	
				// --- end debug code ---
	
				onMapLoad.run();
			};
	
			//				UUID onMapLoadId = this.registerConsumerCallback((String mapIdStr)->{
			//					UUID mapId = UUID.fromString(mapIdStr);
			//					System.out.println("[ICefUtils.start() onMapLoad] Map ID = "+ mapId);
			//					cefMap = cefObjFac.make(mapId);
			//					
			//					onMapLoad.run();
			//				}); //this.registerRunnableCallback(onMapLoad);
	
	
			cefClient.addLoadHandler(new CefLoadHandlerAdapter() {
				@Override
				public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) { // Gets called both for the map page and the dev tools page
					// WARNING!!  DO NOT EVER BLOCK THIS THREAD! 
					// This is the same thread used to service message passing and other CEF communications.
					// Blocking this thread will cause deadlocks!
					// E.g. beware of implicit calls to Future.get() that may block if not yet ready.
	
					System.out.println("[ICefUtils.start() loadHandler] Loaded page at URL = "+frame.getURL());
					if(frame.getURL().equals(startURLFull)) {  // WARNING:  startURLFull MUST be specified with "%20" for spaces as this is what frame.getURL() will return. 
						System.out.println("[ICefUtils.start() loadHandler] in startup frame." );
						cefFrame = frame;
						if(makeDevTools) {
							(new Thread(()-> {
								try {
									Thread.sleep(devToolsDelay);	
									SwingUtilities.invokeLater(()->{
										JFrame devFrame = new JFrame("Dev Tools");
										CefBrowser devBrowser = browser.getDevTools();
										System.out.println("[ICefUtils.start() loadHandler dev tools thread] dev URL: "+devBrowser.getURL());
										devFrame.getContentPane().add(devBrowser.getUIComponent(), BorderLayout.CENTER);
										devFrame.setSize(500, 500);
										devFrame.setVisible(true);
									});
	
								} catch (InterruptedException e) {
									System.err.println("[ICefUtils.start() loadHandler dev tools thread] Exception while delaying to open Dev Tools frame: "+e);
									e.printStackTrace();
								}
	
							})).start();
						}
	
						//							frame.executeJavaScript("window.start('"+googleMapsApiKey+"');", frame.getURL(), 0);
	
	
	
						//							(new Thread(()-> {
						//								System.out.println("[ICefUtils.start() onLoadEnd] cefMapOptions ID = "+cefMapOptions.getId());
						//							})).start();
	
						//							call("start", googleMapsApiKey, onMapLoadId.toString());
	
						// Must launch to new thread to avoid blocking the original thread.
						(new Thread(()-> {
							// WARNING:  Manipulating the cefMapOptions object may cause the thread to block waiting for the completion 
							// of the object instantiation on the JavaScript side.
	
							//								ICefObject cefMapOptions = cefObjFac.make("");
							//								cefMapOptions.setProperty("mapTypeId", MapTypeId.HYBRID.getName());
							//								cefMapOptions.setProperty("center", new GsonTypeWrap(ILatLng.make(29.718545,-95.399054), ILatLng.class));
							//								cefMapOptions.setProperty("zoom", 10);
							//								
							//								
							//								try {
							//									Object x =  cefMapOptions.call("getCenter", ILatLng.class).get();
							//									//Double l = gson.fromJson(gson.toJsonTree(x), Double.class);
							//									System.out.println("x = "+x+" "+x.getClass());
							//									//System.out.println("l = "+l+" "+l.getClass());
							//									
							//								} catch (InterruptedException e) {
							//									// TODO Auto-generated catch block
							//									e.printStackTrace();
							//								} catch (ExecutionException e) {
							//									// TODO Auto-generated catch block
							//									e.printStackTrace();
							//								}
							//
							//								
							//								System.out.println("[ICefUtils.start() onLoadEnd] cefMapOptions ID = "+cefMapOptions.getId());
	
							System.out.println("[ICefUtils.start() onLoadEnd] mapOptions JSON = "+gson.toJson(mapOptionsDict, IMapOptions.class));
	
							//								try {
							//									Thread.sleep(10*devToolsDelay);
							//								} catch (InterruptedException e) {
							//									// TODO Auto-generated catch block
							//									e.printStackTrace();
							//								}	
							System.out.println("[ICefUtils.start() onLoadEnd] Starting map loading:  googleMapsApiKey = " + googleMapsApiKey);
	
							//								callVoid("start", googleMapsApiKey, new GsonTypeWrap(mapOptionsDict, IMapOptions.class), onMapLoadId.toString());
							callVoid("initialize", googleMapsApiKey, new GsonTypeWrap(mapOptionsDict, IMapOptions.class), new GsonTypeWrap(onMapLoadCallback, Consumer.class));
	
						})).start();
	
						//							(new Thread(()-> {
						//								browser.executeJavaScript("alert('Yo!')", browser.getURL(), 0);
						//							})).start();
	
						//							browser.executeJavaScript("cefQuery({request: 'my_request_A',persistent: false,onSuccess: function(response) { console.log(response); },onFailure: function(error_code, error_message) { console.error(\"[Console error] (\"+error_code+\") \"+error_message);} })", frame.getURL(), 0);
						//							browser.executeJavaScript("cefQuery({request: 'my_request_B',persistent: false,onSuccess: function(response) { console.log(response); },onFailure: function(error_code, error_message) { console.error(\"[Console error] (\"+error_code+\") \"+error_message);} })", frame.getURL(), 0);
						//							browser.executeJavaScript("cefQuery({request: 'my_request_A',persistent: false,onSuccess: function(response) { console.log(response); },onFailure: function(error_code, error_message) { console.error(\"[Console error] (\"+error_code+\") \"+error_message);} })", frame.getURL(), 0);
	
					}
				}
			}
					);
			// (3) One CefBrowser instance is responsible to control what you'll see on
			//     the UI component of the instance. It can be displayed off-screen
			//     rendered or windowed rendered. To get an instance of CefBrowser you
			//     have to call the method "createBrowser()" of your CefClient
			//     instances.
			//
			//     CefBrowser has methods like "goBack()", "goForward()", "loadURL()",
			//     and many more which are used to control the behavior of the displayed
			//     content. The UI is held within a UI-Compontent which can be accessed
			//     by calling the method "getUIComponent()" on the instance of CefBrowser.
			//     The UI component is inherited from a java.awt.Component and therefore
			//     it can be embedded into any AWT UI.
			System.out.println("[CefUtils:576] useOSR before cefClient.createBrowser : " + useOSR);
			cefBrowser = cefClient.createBrowser(startURLFull, useOSR, isTransparent);
	
			//		     	devFrame.setSize(800, 800); 
			;
	
	
	
			//		        SwingUtilities.invokeLater(()->{
			;
			//		        	 Component devComp = devBrowser.getUIComponent();
	
			//		        	 devFrame.pack();
			//		        	 devFrame.validate();
			//		        	 devFrame.repaint();
	
	
			//		     	SwingUtilities.invokeLater(()->{
			//		     		devFrame.setSize(800, 800);  // Can't be set at same time as JFrame creation or will output will be blank
			//		     	});
	
	
			Component cefBrowserComp= cefBrowser.getUIComponent();
	
			JPanel pnlBrowser = new JPanel();
			pnlBrowser.setLayout(new BorderLayout());
			pnlBrowser.add(cefBrowserComp, BorderLayout.CENTER);
			return pnlBrowser;
		} catch (Exception e1) {
			String errMsg = "[CefUtils.start()] Exception while creating the map panel: " +e1;
			System.err.println(errMsg);
			e1.printStackTrace();
			return new JLabel(errMsg);
		}
	}

	@Override
	public void setDevToolsDelay(long delay) {
		this.devToolsDelay  = delay;
	}

	@Override
	public void setUseOSR(boolean useOSR) {
//		System.err.println("[ICefUtils.setUseOSR()]  NOT YET IMPLEMENTED! THIS METHOD HAS NO EFFECT.");
		this.useOSR = useOSR;
	}

	@Override
	public void setDevToolsPort(int port) {
		System.err.println("[ICefUtils.setDevToolsPort()]  NOT YET IMPLEMENTED! THIS METHOD HAS NO EFFECT.");
//		this.devToolsPort = port;
	}

	@Override
	public void setMakeDevTools(boolean makeDevTools) {
		this.makeDevTools  = makeDevTools;
	}

	@Override
	public ICefObject getCefMapObj() {
		return this.cefMap;
	}

	@Override
	public void callVoid(String fnName, Object... params) {
		FuncParamsVoid fnParams = new FuncParamsVoid(params);

		String jsonArgs = gson.toJson(fnParams);   //String.format("{\"params\":[\"%s\"]}", param);

		String arg = String.format(JS_CALL_FN_FORMAT, fnName, jsonArgs); 
		System.out.println("[ICefUtils.callVoid()] arg = "+arg);
		//				cefFrame.executeJavaScript(arg, cefFrame.getURL(), 0);  <== Using this technique causes strange deadlocks and an inability to make calls from different threads.
		//				System.out.println( "cefFrame.getURL() = "+cefFrame.getURL()+", cefBrowser.getURL() = "+cefBrowser.getURL());
		cefBrowser.executeJavaScript(arg, cefBrowser.getURL(), 0); // Works better than cefFrame.executeJavaScript() when making calls from multiple threads.
	}

	@Override
	public <T> Future<T> call(String fnName, Type returnType, Object... params) {
		CompletableFuture<T> result = new CompletableFuture<T>();				
		//				Object[] args = new Object[params.length+1];
		//				System.arraycopy(params, 0, args, 0, params.length);
		//				
		//				args[params.length] = new GsonTypeWrap((Consumer<T>) (x)->{
		//					result.complete(x);
		//				}, Consumer.class);

		//				FuncParams fnParams = new FuncParams(params);

		//			FuncParams fnArgs = new FuncParams((T x)->{
		//				result.complete(x);
		//			}, params);
		
		Consumer<T> callback = (rawReturnVal) -> {
			System.out.println("[CefUtils.call()] Processing raw returnVal = "+rawReturnVal);

			T returnVal = convertJsonMap(rawReturnVal, returnType);
			runPostOpCleanup(); // Do post-op cleanup just in case.
			result.complete(returnVal);
		};
		
		UUID requestID = registerPendingReturnValCallback(callback);

		FuncParams fnArgs = new FuncParams(requestID, params);  

		String jsonArgs = gson.toJson(fnArgs);   //String.format("{\"params\":[\"%s\"]}", param);

		String arg = String.format(JS_CALL_FN_FORMAT, fnName, jsonArgs);  
		System.out.println("[ICefUtils.call()] arg = "+arg);  
		//				cefFrame.executeJavaScript(arg, cefFrame.getURL(), 0);  <== Using this technique causes strange deadlocks and an inability to make calls from different threads.
		//				System.out.println( "cefFrame.getURL() = "+cefFrame.getURL()+", cefBrowser.getURL() = "+cefBrowser.getURL());



		cefBrowser.executeJavaScript(arg, cefBrowser.getURL(), 0); // Works better than cefFrame.executeJavaScript() when making calls from multiple threads.
		return result;
	}

	//		public <T> Future<T> callCreate(String fnName, Object... params) {
	//			CompletableFuture<T> result = new CompletableFuture<T>();				
	////			Object[] args = new Object[params.length+1];
	////			System.arraycopy(params, 0, args, 0, params.length);
	////			
	////			args[params.length] = new GsonTypeWrap((Consumer<T>) (x)->{
	////				result.complete(x);
	////			}, Consumer.class);
	//		
	////			FuncParams fnParams = new FuncParams(params);
	//		FuncParams fnArgs = new FuncParams((T x)->{
	//			result.complete(x);
	//		}, params);
	//		
	//		String jsonArgs = gson.toJson(fnArgs);   //String.format("{\"params\":[\"%s\"]}", param);
	//		
	//		String arg = String.format("window.callFn(\"%s\", %s)", fnName, jsonArgs);
	//		System.out.println("[ICefUtils.call()] arg = "+arg);
	////			cefFrame.executeJavaScript(arg, cefFrame.getURL(), 0);  <== Using this technique causes strange deadlocks and an inability to make calls from different threads.
	////			System.out.println( "cefFrame.getURL() = "+cefFrame.getURL()+", cefBrowser.getURL() = "+cefBrowser.getURL());
	//		
	//
	//		
	//		cefBrowser.executeJavaScript(arg, cefBrowser.getURL(), 0); // Works better than cefFrame.executeJavaScript() when making calls from multiple threads.
	//		return result;
	//	}

	@Override
	public UUID registerRunnableCallback(Runnable callback) {
		// No need to check if already registered?
		UUID callbackUUID = UUID.randomUUID();
		runnableCallbackMap.put(callbackUUID, callback);
		return callbackUUID;

	}

	@Override
	public UUID registerConsumerCallback(Consumer<?> callback) {
		UUID callbackUUID = UUID.randomUUID();
		consumerCallbackMap.put(callbackUUID, callback);
		return callbackUUID;

	}

	@Override
	public UUID registerPendingReturnValCallback(Consumer<?> callback) {
		UUID callbackUUID = UUID.randomUUID();

		pendingReturnValMap.put(callbackUUID, callback);
		return callbackUUID;

	}	
	
	/**
	 * Converts the given object into the given result type by converting back to JSON and sending back through the GSON parser.
	 */
	@Override
	public <T> T convertJsonMap(T x, Type resultType) {
		return gson.fromJson(gson.toJsonTree(x), resultType);
	}

	@Override
	public double getSysLengthPerMeter() {
		return sysInfo.getSysLengthPerMeter();
	}
	
	@Override
	public ICefObject makeCefObj(String jsClassname, Object... params) {
		return cefObjFac.make(jsClassname, params);
	}
	
	@Override
	public void addSysEventVoid(String eventName, Runnable runnableCallback) {
		
		System.out.println("[ICefObject.addSysEventVoid("+eventName+")] Adding Runnable System event.");
		callVoid("addSysEventVoid", eventName,
				new GsonTypeWrap((Runnable) () -> {
					System.out.println("[ICefUtils.addSysEventVoid("+eventName+") runnableCallback] Executing!");
					runnableCallback.run();
				}, Runnable.class));
	}
	
	@Override
	public void runPostOpCleanup() {
		postOpCleanup.run();
	}
}