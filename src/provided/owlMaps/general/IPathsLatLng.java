/**
 * 
 */
package provided.owlMaps.general;

import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * *** FOR INTERNAL USE ONLY!  DEVELOPER CODE SHOULD NEVER DIRECTLY UTILIZE THIS INTERFACE! ***<br/>
 * An IPaths explicitly filled with IPath&lt;ILatLng&gt;
 * This is used for situations where the generic type of the IPaths needs to be known, e.g. for JSON encoding/decoding.
 * 
 * @author swong
 *
 */
public interface IPathsLatLng extends IPaths<ILatLng> {



	/**
	 * Factory for the GSON TypeAdapterFactory to process this class
	 * @return The TypeAdapterFactory to be used by GSON to process instances of this class.
	 */
	public static TypeAdapterFactory makeTypeAdapterFactory() {

		return new TypeAdapterFactory() {

			@SuppressWarnings("unchecked")
			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {

				if(typeToken.getRawType().equals(IPathsLatLng.class)) {

					return (TypeAdapter<T>) new TypeAdapter<IPaths<ILatLng>>() {

						@Override
						public IPaths<ILatLng> read(JsonReader jsonReader) throws IOException {
							System.out.println("[IPathsLatLng TypeAdapter.read()] Processing IPaths<ILatLng>...");
							throw new UnsupportedOperationException("[IPathsLatLng TypeAdapter.read()] Not implemented yet!");

							//							AJsonObjectTokenProcessor jsonTokenProcessor = new AJsonObjectTokenProcessor() {
							//								@Override
							//								public void caseName(JsonReader jsonReader) throws IOException {
							//									String fieldName = jsonReader.nextName();
							//									System.out.println("[IPathsLatLng TypeAdapter.read()] fieldName = "+fieldName);
							//									JsonToken nextToken = jsonReader.peek();
							//									System.out.println("[IPathsLatLng TypeAdapter.read()] nextToken = "+nextToken);
							//									switch(fieldName) {
							//										case "i":
							//											
							////											System.out.println("[IPathsLatLng TypeAdapter.read()] field 'i' array nextToken = "+nextToken);
							//											if(JsonToken.BEGIN_ARRAY.equals(nextToken)) {
							//												jsonReader.beginArray();
							//												while(jsonReader.hasNext()) {
							//													System.out.println("[IPathsLatLng TypeAdapter.read()] field 'i': In array, nextToken = "+jsonReader.peek());	
							//													jsonReader.skipValue();
							//												}
							//												jsonReader.endArray();
							//											}
							//											else {
							//												System.out.println("[IPathsLatLng TypeAdapter.read()] field 'i': Next token is NOT an array!");
							//											}
							//											break;
							//											
							//
							//										case "length":
							////											double length = jsonReader.nextDouble();
							//											System.out.println("[IPathsLatLng TypeAdapter.read()] field 'length' = "+jsonReader.peek()); 
							//											jsonReader.skipValue();
							//											break;
							//											
							//										default:
							//											
							//											System.out.println("[IPathsLatLng TypeAdapter.read()] Ignoring fieldName = "+fieldName);
							//											jsonReader.skipValue(); // skip the processing of the associated value
							//											break;
							//									}									
							//								}
							//							};
							//							
							//							jsonTokenProcessor.process(jsonReader);
							//							
							//							return null;

						}

						@Override
						public void write(JsonWriter jsonWriter, IPaths<ILatLng> value) throws IOException {
							System.out.println("[IPathsLatLng TypeAdapter.write()] Processing input...");
							value.execute(new IPathsAlgo<Void, ILatLng, Void>() {

								private static final long serialVersionUID = -1158595349895005618L;

								@Override
								public Void pathCase(IPath<ILatLng> host, Void... nu) {
									gson.toJson(host, IPathLatLng.class, jsonWriter);
									return null;
								}

								@Override
								public Void pathListCase(IPathList<ILatLng> host, Void... nu) {
									try {
										jsonWriter.beginArray();
										host.forEach((elt)->{
											gson.toJson(elt, IPathLatLng.class, jsonWriter);
										});
										jsonWriter.endArray();
									} catch (IOException e) {
										System.out.println("[IPathsLatLng TypeAdapter.write()] Exception while processing IPathList<ILatLng> input: "+e);
										e.printStackTrace();
									}
									return null;
								}

							});

						}

					};
				}
				else {
					return null;
				}
			}

		};
	}	
}
