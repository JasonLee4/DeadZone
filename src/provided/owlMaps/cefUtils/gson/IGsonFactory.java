package provided.owlMaps.cefUtils.gson;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import provided.owlMaps.cefUtils.ICefDefs;
import provided.owlMaps.cefUtils.ICefObjRef;
import provided.owlMaps.cefUtils.ICefObject;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.components.infowindow.IInfoWindowOptions;
import provided.owlMaps.components.marker.IMarkerLabelOptions;
import provided.owlMaps.components.marker.IMarkerOptions;
import provided.owlMaps.components.overlay.IGroundOverlayOptions;
import provided.owlMaps.components.shapes.ICircleOptions;
import provided.owlMaps.components.shapes.IPolyMouseEvent;
import provided.owlMaps.components.shapes.IPolygonOptions;
import provided.owlMaps.components.shapes.IPolylineOptions;
import provided.owlMaps.components.shapes.IRectangleOptions;
import provided.owlMaps.general.IIconOptions;
import provided.owlMaps.general.IIconSequence;
import provided.owlMaps.general.IIconSequenceOptions;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.general.ILatLngBounds;
import provided.owlMaps.general.IPathLatLng;
import provided.owlMaps.general.IPathListLatLng;
import provided.owlMaps.general.IPathsLatLng;
import provided.owlMaps.general.ISymbol;
import provided.owlMaps.general.ISymbolOptions;
import provided.owlMaps.general.ISymbolPath;
import provided.owlMaps.map.IMapOptions;
import provided.owlMaps.map.data.IListMapDataFeature;
import provided.owlMaps.map.data.IMapDataFeature;
import provided.owlMaps.map.data.IMapDataGeometry;
import provided.owlMaps.map.data.IMapDataStyleOptions;
import provided.owlMaps.mouse.IMouseEvent;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS INTERFACE! ***<br/>
 * Factory for making a customized Gson instance for the system.
 * @author swong
 *
 */
public interface IGsonFactory {
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Instantiate a new Gson object customized for the CEF system.  
	 * Uses both TypeAdapter and TypeAdapterFactory (recommended) instances to create converters 
	 * from various system classes to JSON.  
	 * If new classes are added to the system that need to be converted to JSON, this factory method must be modified.
	 * 
	 * Currently implemented type processing:
	 * <ul>
	 * <li>Runnable: (write only)  Registers given Runnable with ICefUtils and then creates JSON object with identifier info. </li>
	 * <li>Consumer: (write only) Registers given Runnable with ICefUtils and then creates JSON object with identifier info. </li>
	 * <li>ICefObject: (write only) Creates JSON object with the object's unique ID value</li>
	 * <li>ICefObjRef: (write only) Creates JSON object with the object's unique ID value</li>
	 * <li>UUID: (r/w) Converts UUID to/from string representation.</li>
	 * <li>IIconSequence: (w) Converts an options dictionary to a wrapper object holding the options dictionary</li>
	 * <li>IPathLatLng: (r/w) Explicit type for IPath&lt;ILatLng&gt; to convert to/from an array of LatLngs</li>
	 * <li>IPathsLatLng: (r/w) Explicit type for IPaths&lt;ILatLng&gt; to convert to/from an array of or array of arrays of ILatLngs</li>
	 * <li>ISymbol: (w) Converts an options dictionary to a wrapper object holding the options dictionary</li>
	 * <li>ISymbolPath: (w) Writes out the name or path string depending on its sub-type.</li>
	 * <li>Component Options:  (write only) Converts IMixedDataDictionary into a JSON dictionary.   Implemented option types:
	 * <ul>
	 * <li>ICircleOptions</li>
	 * <li>IGroundOverlayOptions</li>
	 * <li>IIconOptions</li>
	 * <li>IIconSequenceOptions</li>
	 * <li>IInfoWindowOptions</li>
	 * <li>IMapOptions</li>
	 * <li>IMapDataStyleOptions</li>
	 * <li>IMarkerOptions</li>
	 * <li>IMarkerLabelOptions</li>
	 * <li>IPolygonOptions</li>
	 * <li>IPolylineOptions</li>
	 * <li>IRectangleOptions</li>
	 * <li>ISymbolOptions</li>
	 * <li></li>
	 * </ul>
	 * </li>
	 * <li>Map components:  See the documentation for makeMapComponentsTypeAdapterFactory()</li>
	 * <li>GsonTypeWrap: (write only) Converts to JSON as per the contained type information.</li>
	 * <li></li>
	 * <li></li>
	 * </ul>
	 * 
	 * 
	 * 
	 * TODO:  Move this implementation to its own CEF-specific singleton and enable it to be more easily extended.  
	 * @param cefUtils   CEF utilities that are used by the conversion processes.
	 * @return A new customized Gson instance.
	 */
	public static Gson make(ICefUtils cefUtils) {
		
		GsonBuilder gsonBuilder = new GsonBuilder();

		
		gsonBuilder.registerTypeAdapter(Runnable.class, new TypeAdapter<Runnable>() {

			@Override
			public Runnable read(JsonReader jsonReader) throws IOException {
				// TODO Auto-generated method stub
				System.out.println("[runnableTypeAdapter.read()] called.");
				
				throw new UnsupportedOperationException("[runnableTypeAdapter.read()] Not implemented yet!");
			}

			@Override
			public void write(JsonWriter jsonWriter, Runnable runnable) throws IOException {
				System.out.println("[Runnable TypeAdapter.write()] called.");

				UUID id = cefUtils.registerRunnableCallback(runnable); // Register the callback
				jsonWriter.beginObject();
				jsonWriter.name(ICefDefs.CEF_ENTITY_TYPE_FIELD_NAME);
				jsonWriter.value(ICefDefs.CEF_ENTITY_TYPE_RUNNABLE);
				jsonWriter.name(ICefDefs.CEF_ENTITY_VALUE_FIELD_NAME);
				jsonWriter.value(id.toString());  // Use registration ID
				jsonWriter.endObject();
			}
			
		});
		
		
		gsonBuilder.registerTypeAdapter(Consumer.class, new TypeAdapter<Consumer<?>>() {

			@Override
			public Consumer<?> read(JsonReader jsonReader) throws IOException {
				// TODO Auto-generated method stub
				System.out.println("[Consumer TypeAdapter.read()] called.");
				throw new UnsupportedOperationException("[Consumer TypeAdapter.read()] Not implemented yet!");
			}

			@Override
			public void write(JsonWriter jsonWriter, Consumer<?> consumer) throws IOException {
				System.out.println("[Consumer TypeAdapter.write()] called.");
				UUID id = cefUtils.registerConsumerCallback(consumer); // Register the callback
				
				jsonWriter.beginObject();
				jsonWriter.name(ICefDefs.CEF_ENTITY_TYPE_FIELD_NAME);
				jsonWriter.value(ICefDefs.CEF_ENTITY_TYPE_CONSUMER);
				jsonWriter.name(ICefDefs.CEF_ENTITY_VALUE_FIELD_NAME);
				jsonWriter.value(id.toString());  // Use registration ID
				jsonWriter.endObject();
			}
			
		});		
		
		gsonBuilder.registerTypeAdapter(UUID.class, new TypeAdapter<UUID>() {

			@Override
			public UUID read(JsonReader jsonReader) throws IOException {
				System.out.println("[UUID TypeAdapter.read()] called.");
				return UUID.fromString(jsonReader.nextString());
			}

			@Override
			public void write(JsonWriter jsonWriter, UUID id) throws IOException {
				System.out.println("[UUID TypeAdapter.write()] called.");
				jsonWriter.value(id.toString()); 
			}
			
		});	
		
		// TODO refactor to eliminate duplicated code between this and ICefObjRef
		gsonBuilder.registerTypeAdapter(ICefObject.class, new TypeAdapter<ICefObject>() {

			@Override
			public ICefObject read(JsonReader jsonReader) throws IOException {
				
				System.out.println("[ICefObject TypeAdapter.read()] called.");
				throw new UnsupportedOperationException("[ICefObject TypeAdapter.read()] Not implemented yet!");
//				return null;
			}

			@Override
			public void write(JsonWriter jsonWriter, ICefObject cefObj) throws IOException {
				System.out.println("[ICefObject TypeAdapter.write()] called.");
				jsonWriter.beginObject();
				jsonWriter.name(ICefDefs.CEF_ENTITY_TYPE_FIELD_NAME);
				jsonWriter.value(ICefDefs.CEF_ENTITY_TYPE_OBJECT);
				jsonWriter.name(ICefDefs.CEF_ENTITY_VALUE_FIELD_NAME);
				jsonWriter.value(cefObj.getCefId().toString());
				jsonWriter.endObject();
			}
			
		});	
		
		
		// TODO refactor to eliminate duplicated code between this and ICefObject
		gsonBuilder.registerTypeAdapter(ICefObjRef.class, new TypeAdapter<ICefObjRef>() {

			@Override
			public ICefObject read(JsonReader jsonReader) throws IOException {
				
				System.out.println("[ICefObjRef TypeAdapter.read()] called.");
				throw new UnsupportedOperationException("[ICefObjRef TypeAdapter.read()] Not implemented yet!");
//				return null;
			}

			@Override
			public void write(JsonWriter jsonWriter, ICefObjRef cefObjRef) throws IOException {
				System.out.println("[ICefObjRef TypeAdapter.write()] called.");
				jsonWriter.beginObject();
				jsonWriter.name(ICefDefs.CEF_ENTITY_TYPE_FIELD_NAME);
				jsonWriter.value(ICefDefs.CEF_ENTITY_TYPE_OBJECT);
				jsonWriter.name(ICefDefs.CEF_ENTITY_VALUE_FIELD_NAME);
				jsonWriter.value(cefObjRef.getCefId().toString());
				jsonWriter.endObject();
			}
			
		});	
		
//		gsonBuilder.registerTypeAdapter(Junk.class, new TypeAdapter<Junk>() {
//
//			@Override
//			public Junk read(JsonReader jsonReader) throws IOException {
//				// TODO Auto-generated method stub
//				System.out.println("[ICefObject TypeAdapter.read()] called.");
//				return null;
//			}
//
//			@Override
//			public void write(JsonWriter jsonWriter, Junk cefObj) throws IOException {
//				System.out.println("[ICefObject TypeAdapter.write()] called.");
//				jsonWriter.beginObject();
//				jsonWriter.name("x");
//				jsonWriter.value(42);
//				jsonWriter.name("y");
//				jsonWriter.value(99);
//				jsonWriter.name("x");
//				jsonWriter.value(-42);
//				jsonWriter.endObject();
//			}
//			
//		});
		
//		gsonBuilder.registerTypeAdapter(ILatLng.class, new TypeAdapter<ILatLng>() {
//
//			
//			@Override
//			public ILatLng read(JsonReader jsonReader) throws IOException {
//				// TODO Auto-generated method stub
//				System.out.println("[ICefObject TypeAdapter.read()] called.");
//				return null;
//			}
//
//			@Override
//			public void write(JsonWriter jsonWriter, ILatLng latLng) throws IOException {
//				System.out.println("[ILatLng TypeAdapter.write()] called.");
//				jsonWriter.beginObject();
//				jsonWriter.name("lat");
//				jsonWriter.value(latLng.getLat());
//				jsonWriter.name("lng");
//				jsonWriter.value(latLng.getLng());
//				jsonWriter.endObject();
//			}
//			
//		});		
		
		gsonBuilder.registerTypeAdapterFactory(IMapOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(IMapDataStyleOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(IMarkerOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(IInfoWindowOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(IPolygonOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(IPolylineOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(IRectangleOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(ICircleOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(IGroundOverlayOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(IMarkerLabelOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
//		gsonBuilder.registerTypeAdapterFactory(.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(IIconSequence.makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(IIconSequenceOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(IIconOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(ISymbol.makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(ISymbolOptions.makeOptionsFiller(cefUtils).makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(makeMapComponentsTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(GsonTypeWrap.makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(FuncParamsVoid.makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(FuncParams.makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(IPathLatLng.makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(IPathListLatLng.makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(IPathsLatLng.makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(ISymbolPath.makeTypeAdapterFactory());
		
		gsonBuilder.registerTypeAdapterFactory(IMapDataFeature.makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(IMapDataGeometry.makeTypeAdapterFactory());
		gsonBuilder.registerTypeAdapterFactory(IListMapDataFeature.makeTypeAdapterFactory());

		
		gsonBuilder.setPrettyPrinting();
		gsonBuilder.serializeNulls();
		
		return gsonBuilder.create();

	}
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Factory method for a TypeAdapterFactory for various map components
	 * Currently implemented type processing:
	 * <ul>
	 * <li>ILatLng: (r/w) Converts to/from JSON dictionary with keys = ("lat", "lng").</li>
	 * <li>ILatLngBounds: (w) Converts to JSON dictionary with keys = ("north, "east", "south", "west").</li>
	 * <li>IMouseEvent: (read only) Reads "latLng" field from JSON object into a new IMouseEvent object </li>
	 * <li>IPolyMouseEvent: (read only) Reads "latLng", edge, path and vertex fields from JSON object into a new IPolyMouseEvent object </li>
	 * </ul>
	 * @return a TypeAdapterFactory instance
	 */
	public static TypeAdapterFactory makeMapComponentsTypeAdapterFactory() {
		return new TypeAdapterFactory() {

			@SuppressWarnings("unchecked")
			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
				Class<? super T> rawType = typeToken.getRawType();
				
				if(rawType.equals(ILatLng.class)) {
					return (TypeAdapter<T>) new TypeAdapter<ILatLng>() {

						
						@Override
						public ILatLng read(JsonReader jsonReader) throws IOException {
//							jsonReader.beginObject();
							double[] lat = new double[] {Double.NaN};
							double[] lng = new double[] {Double.NaN};
							
							AJsonObjectTokenProcessor jsonTokenProcessor = new AJsonObjectTokenProcessor() {
								@Override
								public void caseName(JsonReader jsonReader) throws IOException {
									String fieldName = jsonReader.nextName();
//									System.out.println("[IGsonFactory ILatLng TypeAdapter.read()] fieldName = "+fieldName);
									switch(fieldName) {
										case "lat":
											lat[0] = jsonReader.nextDouble(); 
											break;
										case "lng":
											lng[0] = jsonReader.nextDouble();
											break;
										default:
//											System.out.println("[IGsonFactory ILatLng TypeAdapter.read()] Ignoring fieldName = "+fieldName);
											jsonReader.skipValue(); // skip the processing of the associated value
											break;
									}									
								}
							};
							
							jsonTokenProcessor.process(jsonReader);
							
							
							if(Double.NaN == lat[0] ||Double.NaN == lng[0] ) {
								throw new IllegalArgumentException("[IGsonFactory ILatLng TypeAdapter.read()] Invalid value(s) encountered: lat = "+lat[0]+", lng = "+lng[0]);								
							}
							else {
								ILatLng latLng = ILatLng.make(lat[0], lng[0]);
								System.out.println("[IGsonFactory ILatLng TypeAdapter.read()] latLng = "+latLng);
								return latLng;
							}							
							
							
						}

						@Override
						public void write(JsonWriter jsonWriter, ILatLng latLng) throws IOException {
							System.out.println("[ILatLng TypeAdapter.write()] called.");
							jsonWriter.beginObject();
							jsonWriter.name("lat");
							jsonWriter.value(latLng.getLat());
							jsonWriter.name("lng");
							jsonWriter.value(latLng.getLng());
							jsonWriter.endObject();
						}
						
					};
				
				}
				
				if(rawType.equals(IMouseEvent.class)) {

					
					return (TypeAdapter<T>) new TypeAdapter<IMouseEvent>() {
						
						TypeAdapter<ILatLng> latLngTypeAdapter =  gson.getAdapter(ILatLng.class);

						@Override
						public IMouseEvent read(JsonReader jsonReader) throws IOException {
							IMouseEvent[] result = new IMouseEvent[]{null};
							
							AJsonObjectTokenProcessor jsonTokenProcessor = new AJsonObjectTokenProcessor() {
								@Override
								public void caseName(JsonReader jsonReader) throws IOException {
									String fieldName = jsonReader.nextName();
									System.out.println("[IGsonFactory IMouseEvent TypeAdapter.read()] fieldName = "+fieldName);
									switch(fieldName) {
										case "latLng":

											result[0] = IMouseEvent.make(latLngTypeAdapter.read(jsonReader));
											
											break;

										default:
											System.out.println("[IGsonFactory IMouseEvent TypeAdapter.read()] Ignoring fieldName = "+fieldName);
											jsonReader.skipValue();   // skip the processing of the associated value
											break;
									}							
								}
							};
							

							try {							
								jsonTokenProcessor.process(jsonReader);
								
								if(null == result[0] ) {
									throw new IllegalArgumentException("[IGsonFactory IMouseEvent TypeAdapter.read()] latLng field not found.");								
								}
								else {
									
									return result[0];
								}	
							}
							catch(Exception e) {
								System.err.println("Exception = "+e);
								e.printStackTrace();
								return result[0];
							}
						}

						@Override
						public void write(JsonWriter jsonWriter, IMouseEvent mouseEvent) throws IOException {
							throw new UnsupportedOperationException("[IMouseEvent TypeAdapter.write()] Not implemented yet!");
						}
						
					};
				}

				if(rawType.equals(IPolyMouseEvent.class)) {

					
					return (TypeAdapter<T>) new TypeAdapter<IPolyMouseEvent>() {
						
						TypeAdapter<ILatLng> latLngTypeAdapter =  gson.getAdapter(ILatLng.class);

						@Override
						public IPolyMouseEvent read(JsonReader jsonReader) throws IOException {
							
							ILatLng[] latLng = new ILatLng[] {null};
							int[] edge = new int[] {0};
							int[] path = new int[] {0};
							int[] vertex = new int[] {0};
							
							IPolyMouseEvent[] result = new IPolyMouseEvent[]{null};
							
							AJsonObjectTokenProcessor jsonTokenProcessor = new AJsonObjectTokenProcessor() {
								@Override
								public void caseName(JsonReader jsonReader) throws IOException {
									String fieldName = jsonReader.nextName();
									System.out.println("[IGsonFactory IPolyMouseEvent TypeAdapter.read()] fieldName = "+fieldName);
									switch(fieldName) {
										case "latLng":

											latLng[0] =latLngTypeAdapter.read(jsonReader);
											
											break;

										case "edge":
											edge[0] = jsonReader.nextInt();
											break;
										
										case "path":
											path[0] = jsonReader.nextInt();
											break;
										
										case "vertex":
											vertex[0] = jsonReader.nextInt();
											break;
										
										
										default:
											System.out.println("[IGsonFactory IPolyMouseEvent TypeAdapter.read()] Ignoring fieldName = "+fieldName);
											jsonReader.skipValue();   // skip the processing of the associated value
											break;
									}							
								}
							};
							

							try {							
								jsonTokenProcessor.process(jsonReader);
								
								if(null == latLng[0] ) {
									throw new IllegalArgumentException("[IGsonFactory IPolyMouseEvent TypeAdapter.read()] latLng field not found.");								
								}
								else {
									
									return IPolyMouseEvent.make(latLng[0], edge[0], path[0], vertex[0]);
								}	
							}
							catch(Exception e) {
								System.err.println("Exception = "+e);
								e.printStackTrace();
								return result[0];
							}
						}

						@Override
						public void write(JsonWriter jsonWriter, IPolyMouseEvent mouseEvent) throws IOException {
							throw new UnsupportedOperationException("[IPolyMouseEvent TypeAdapter.write()] Not implemented yet!");
						}
						
					};
				}
				
				if(rawType.equals(ILatLngBounds.class)) {

					
					return (TypeAdapter<T>) new TypeAdapter<ILatLngBounds>() {

						@Override
						public ILatLngBounds read(JsonReader in) throws IOException {
							throw new UnsupportedOperationException("[ILatLngBounds TypeAdapter.write()] Not implemented yet!");
//							return null;
						}

						@Override
						public void write(JsonWriter jsonWriter, ILatLngBounds latLngBounds) throws IOException {
							System.out.println("[ILatLngBounds TypeAdapter.write()] called.");
							jsonWriter.beginObject();
							
							ILatLng neLatLng = latLngBounds.getNorthEast();
							jsonWriter.name("east");
							jsonWriter.value(neLatLng.getLng());
							jsonWriter.name("north");
							jsonWriter.value(neLatLng.getLat());
							
							ILatLng swLatLng = latLngBounds.getSouthWest();
							jsonWriter.name("south");
							jsonWriter.value(swLatLng.getLat());
							jsonWriter.name("west");
							jsonWriter.value(swLatLng.getLng());
							
							jsonWriter.endObject();
						}
						
					};
				}
				
				if(rawType.equals(Point2D.class)) {

					
					return (TypeAdapter<T>) new TypeAdapter<Point2D>() {

						@Override
						public Point2D read(JsonReader jsonReader) throws IOException {
							
							double[] x = new double[] {Double.NaN};
							double[] y = new double[] {Double.NaN};
							
							AJsonObjectTokenProcessor jsonTokenProcessor = new AJsonObjectTokenProcessor() {
								@Override
								public void caseName(JsonReader jsonReader) throws IOException {
									String fieldName = jsonReader.nextName();
									System.out.println("[IGsonFactory Point2D TypeAdapter.read()] fieldName = "+fieldName);
									switch(fieldName) {
										case "x":
											x[0] = jsonReader.nextDouble();
											break;

										case "y":
											y[0] = jsonReader.nextDouble();
											break;

										
										default:
											System.out.println("[IGsonFactory Point2D TypeAdapter.read()] Ignoring fieldName = "+fieldName);
											jsonReader.skipValue();   // skip the processing of the associated value
											break;
									}							
								}
							};
							try {							
								jsonTokenProcessor.process(jsonReader);
								
								if(Double.NaN == x[0] || Double.NaN == y[0]) {
									throw new IllegalArgumentException("[IGsonFactory Point2D TypeAdapter.read()] Both x and y fields were not found: ("+x[0]+", "+y[0]+")");								
								}
								else {
									
									return new Point2D.Double(x[0], y[0]);
								}	
							}
							catch(Exception e) {
								System.err.println("IGsonFactory Point2D TypeAdapter.read()] Returning (0,0) because exception = "+e);
								e.printStackTrace();
								return new Point2D.Double(0, 0);
							}

						}

						@Override
						public void write(JsonWriter jsonWriter, Point2D pt) throws IOException {
							System.out.println("[Point2D TypeAdapter.write()] called.");
							jsonWriter.beginObject();
							jsonWriter.name("x");
							jsonWriter.value(pt.getX());
							jsonWriter.name("y");
							jsonWriter.value(pt.getY());							
							jsonWriter.endObject();
						}
						
					};
				}
				
				if(rawType.equals(Dimension2D.class)) {

					return (TypeAdapter<T>) new TypeAdapter<Dimension2D>() {

						@Override
						public Dimension2D read(JsonReader jsonReader) throws IOException {
							
							double[] width = new double[] {Double.NaN};
							double[] height = new double[] {Double.NaN};
							
							AJsonObjectTokenProcessor jsonTokenProcessor = new AJsonObjectTokenProcessor() {
								@Override
								public void caseName(JsonReader jsonReader) throws IOException {
									String fieldName = jsonReader.nextName();
									System.out.println("[IGsonFactory Dimension2D TypeAdapter.read()] fieldName = "+fieldName);
									switch(fieldName) {
										case "width":
											width[0] = jsonReader.nextDouble();
											break;

										case "height":
											height[0] = jsonReader.nextDouble();
											break;

										
										default:
											System.out.println("[IGsonFactory Dimension2D TypeAdapter.read()] Ignoring fieldName = "+fieldName);
											jsonReader.skipValue();   // skip the processing of the associated value
											break;
									}							
								}
							};
							try {							
								jsonTokenProcessor.process(jsonReader);
								
								if(Double.NaN == width[0] || Double.NaN == height[0]) {
									throw new IllegalArgumentException("[IGsonFactory Dimension2D TypeAdapter.read()] Both width and height fields were not found: ("+width[0]+", "+height[0]+")");								
								}
								else {
									Dimension dim = new Dimension();
									dim.setSize(width[0], height[0]);
									return dim;
								}	
							}
							catch(Exception e) {
								System.err.println("IGsonFactory Point2D TypeAdapter.read()] Returning (0,0) because exception = "+e);
								e.printStackTrace();
								return new Dimension(0, 0);
							}

						}

						@Override
						public void write(JsonWriter jsonWriter, Dimension2D dim) throws IOException {
							System.out.println("[Point2D TypeAdapter.write()] called.");
							jsonWriter.beginObject();
							jsonWriter.name("width");
							jsonWriter.value(dim.getWidth());
							jsonWriter.name("height");
							jsonWriter.value(dim.getHeight());							
							jsonWriter.endObject();
						}
						
					};
				}
				

				
				return null;

			}
		};
	}

}
