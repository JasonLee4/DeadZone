/**
 * TODO The devToolsPort and useOSR cannot be set dynamically because they are part of the CEF configuration needed before the ICefUtils 
 * can be created.  (is this true?)   
 * The initialization needs to be modified to take this into account.
 * Currently, only the default values are ever used.
 */

package provided.owlMaps.cefUtils;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import javax.swing.JComponent;

//import org.cef.CefApp;
//import org.cef.CefSettings;
//import org.cef.CefApp.CefAppState;
//import org.cef.CefClient;
//import org.cef.handler.CefAppHandlerAdapter;

import provided.mixedData.IMixedDataDictionary;
import provided.owlMaps.cefUtils.impl.CefUtils;


/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS INTERFACE DIRECTLY! ***<br/>
 * Utilities for communicating with the JavaScript side of CEF.
 * @author swong
 *
 */
public interface ICefUtils {
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS FIELD DIRECTLY! ***<br/>
	 * Default delay to start the dev tools window in millisec
	 * Needed to overcome race conditions on connecting the dev tools to the main frame.
	 */
	public static final long DEV_TOOLS_DELAY_DEFAULT = 1000;  
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS FIELD DIRECTLY! ***<br/>
	 * Default value for whether or not to use Off-Screen Rendering (OSR).
	 * If true, use off screen rendering which requires that the appropriate Java Open GL (JOGL) drivers be installed. 
	 */
	public static final boolean USE_OFF_SCREEN_RENDERING_DEFAULT = false;
	
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS FIELD DIRECTLY! ***<br/>
	 * Default port for the dev tools window to run on.
	 */
	public static final int DEV_TOOLS_PORT_DEFAULT = 9222;
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS FIELD DIRECTLY! ***<br/>
	 * Default value to control whether or not the dev tools window is shown.  If true, construct and display the dev tools window.
	 */
	public static final boolean MAKE_DEV_TOOLS_DEFAULT = false;
	

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Start the Map on the Javascript side
	 * @param startURL The URL with which to open the map
	 * @param mapOptionsDict A dictionary of IMapOptions options
	 * @param onMapLoad Callback to run after the map is loaded
	 * @return A JComponent that can be displayed on the view.
	 */
	public JComponent start(String startURL, IMixedDataDictionary mapOptionsDict, Runnable onMapLoad);

	/**
	 * Get the ICefObject that wraps the Javascript map entity
	 * @return  The ICefObject for the map
	 */
	public ICefObject getCefMapObj();
	

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Sets the delay to wait before opening the dev tools window.
	 * The dev tools window needs to be delayed so that it can show what's happening in the map. 
	 * @param delay The number of milliseconds to delay
	 */
	public void setDevToolsDelay(long delay);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Sets whether or not the system uses the Off-Screen Rendering (OSR).
	 * @param useOSR  If true, use OSR
	 */
	public void setUseOSR(boolean useOSR);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Set the port used by the dev tools window
	 * @param port  A valid port number not being used by any other app.
	 */
	public void setDevToolsPort(int port);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Enables the display of the browser dev console window
	 * @param makeDevTools If true, show the dev console window
	 */
	public void setMakeDevTools(boolean makeDevTools);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * A call to the given Javascript function that is a void return
	 * @param fnName The name of the Javascript function to invoke
	 * @param params A vararg of parameters to pass to the function
	 */
	public void callVoid(String fnName, Object... params);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Call the specified Javascript functuion with the given parameters and return the
	 * result as a Future
	 * @param <T>  The type of the return value
	 * @param fnName   The Javascript function to call
	 * @param returnType TODO
	 * @param params Vararg of parameters with which to invoke the function
 	 * @return The return value of the function, available when ready.
	 */
	public <T> Future<T> call(String fnName, Type returnType, Object... params);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Register the given Runnable in the system so that it can be used for callbacks.
	 * @param callback  The Runnable to register
	 * @return A unique ID associated with the given Runnable.
	 */
	public UUID registerRunnableCallback(Runnable callback);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Register the given Consumer in the system so that it can be used for callbacks.
	 * The callback is assumed to be persistent and used multiple times.
	 * @param callback The Consumer to register
	 * @return A unique ID associated with the given Consumer
	 */
	public UUID registerConsumerCallback(Consumer<?> callback);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Register the given Consumer in the system so that it can be used for return values of function calls.
	 * The callback is assumed to be single use and must be removed from the mapping when executed.
	 * @param callback The Consumer to register
	 * @return A unique ID associated with the given Consumer
	 */
	public UUID registerPendingReturnValCallback(Consumer<?> callback);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Converts an object of an unspecified type into a specified type.   Not all information 
	 * from the given object will necessarily be transferred to the result.
	 * Typically used to convert a generic received object into a specific instance type. 
	 * @param <T> The type of the original object
	 * @param x The original object
	 * @param resultType The desired type of the result
	 * @return An object of the desired type, not usually the same instance as the given object
	 */
	public <T> T convertJsonMap(T x, Type resultType);
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Get the conversion factor from the length units currently being used by the system into meters.
	 * @return A length conversion factor of system_units/meter
	 */
	public double getSysLengthPerMeter();
	
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Instantiate the given class on the JavaScript side with the given parameters
	 * @param jsClassname The name of the class to instantiate
	 * @param params The class's constructor parameters
	 * @return An ICefObject representing the instantiated JavaScript object.   The object is NOT to be considered viable until it is able to return a valid ID value.
	 */
	public ICefObject makeCefObj(String jsClassname, Object... params);
	
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD DIRECTLY! ***<br/>
	 * Factory to instantiate an ICefUtils instance
	 * @param googleMapsApiKey  The Google Maps API key to use
	 * @param sysInfo A supplier of dynamic system information
	 * @return An ICefUtils instance
	 */
	public static ICefUtils make(String googleMapsApiKey, ISystemInfo sysInfo ) {
		
		
//		TypeAdapter<Runnable> runnableTypeAdapter = new TypeAdapter<Runnable>() {
//
//			@Override
//			public Runnable read(JsonReader jsonReader) throws IOException {
//				// TODO Auto-generated method stub
//				System.out.println("[runnableTypeAdapter.read()] called.");
//				return null;
//			}
//
//			@Override
//			public void write(JsonWriter jsonWriter, Runnable runnable) throws IOException {
//				System.out.println("[runnableTypeAdapter.write()] called.");
//				jsonWriter.beginObject();
//				jsonWriter.name("CEF_OBJECT_TYPE");
//				jsonWriter.value("RUNNABLE");
//				jsonWriter.name("VALUE");
//				jsonWriter.value(UUID.randomUUID().toString());
//				jsonWriter.endObject();
//			}
//			
//		};
		
		
		

		
//		Map<Integer, String> testMap = new HashMap<Integer, String>();
//		testMap.put(2, "two");
//		testMap.put(42, "forty-two");
//		testMap.put(13, "thirteen");
//		
//		System.out.println("testMap JSON = "+gson.toJson(testMap));
		

		
		
//		Runnable r = () -> {
//			System.out.println("???");
//
//		};
//			
//		Consumer c = (x) -> {
//			System.out.println("??? = "+x);
//
//		};	
		
		
//		System.out.println("Runnable JSON = "+gson.toJson( r, Runnable.class));
//		System.out.println("Consumer JSON = "+gson.toJson( c, Consumer.class));
//		ILatLng latLng = ILatLng.make(12.34, 56.89);
//		
//		System.out.println("ILatLng JSON = "+gson.toJson(latLng, ILatLng.class));
		
		




//		CefMessageRouterConfig config = new CefMessageRouterConfig();
//		config.jsQueryFunction = "cefQuery";
//		config.jsCancelFunction = "cefQueryCancel";
//
//		CefMessageRouter msgRouter = CefMessageRouter.create(config);
//		msgRouter.addHandler(new CefMessageRouterHandlerAdapter() {
//			@Override
//			public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, java.lang.String request, boolean persistent, CefQueryCallback callback) {
//				System.out.println("("+queryId+") "+request); 
//
//				
//				if(0.5 > Math.random()) {
//					callback.success("("+queryId+") Success: "+request);
//				}
//				else {
//					callback.failure((int) queryId, "Failure: "+request);
//				}
//				return true;
//			}
//		}, true);

		
		
		


		return new CefUtils(googleMapsApiKey, sysInfo);
	}

	/**
	 * Adds a Runnable system-level event.   These events are used for the Javascript-side to communicate back to the Java side
	 * independent of any object or specific operation, e.g. to trigger the post-op cleanup process.
	 * @param eventName The name of the event to register.  Will overwrite an existing event with the same name.
	 * @param runnableCallback The callback to run when the event is triggered on the Javascript side
	 */
	void addSysEventVoid(String eventName, Runnable runnableCallback);

	/**
	 * Run the post-operation cleanup process.   
	 * This process is used to restore the Java side to operational order after a CEF operation.
	 * This process is automatically added to the end CEF calls IF it is registered as the SYS_EVENT_NAME_POST_OP
	 * system event callback (ICefUtils.addSysEventVoid()).  
	 * HOWEVER, it must be explicitly added as part of an object's event callback since they are asynchronously generated 
	 * on the Javascript side.   
	 */
	void runPostOpCleanup();



	



}
