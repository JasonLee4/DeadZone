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
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import provided.owlMaps.cefUtils.gson.AJsonObjectTokenProcessor;

/**
 * *** FOR INTERNAL USE ONLY!  DEVELOPER CODE SHOULD NEVER DIRECTLY UTILIZE THIS INTERFACE! ***<br/>
 * A Path that is explicitly filled with ILatLngs.
 * This is used for situations where the generic type of the IPath needs to be known, e.g. for JSON encoding/decoding.
 * 
 * @author swong
 *
 */
public interface IPathListLatLng extends IPathList<ILatLng> {

	
	/**
	 * Factory for the GSON TypeAdapterFactory to process this class
	 * @return The TypeAdapterFactory to be used by GSON to process instances of this class.
	 */
	public static TypeAdapterFactory makeTypeAdapterFactory() {

		return new TypeAdapterFactory() {

			@SuppressWarnings("unchecked")
			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {

				if(typeToken.getRawType().equals(IPathListLatLng.class)) {
					
					return (TypeAdapter<T>) new TypeAdapter<IPathList<ILatLng>>() {

						@Override
						public IPathList<ILatLng> read(JsonReader jsonReader) throws IOException {
							System.out.println("[IPathLatLng TypeAdapter.read()] Processing IPath<ILatLng>...");
							
							IPathList<ILatLng> pathListLatLng = IPathList.make(); 
							
							AJsonObjectTokenProcessor jsonTokenProcessor = new AJsonObjectTokenProcessor() {
								@Override
								public void caseName(JsonReader jsonReader) throws IOException {
									String fieldName = jsonReader.nextName();
									System.out.println("[IPathListLatLng TypeAdapter.read()] fieldName = "+fieldName);

									switch(fieldName) {
										case "i":
											if(JsonToken.BEGIN_ARRAY.equals(jsonReader.peek())) {
												// Should be an array of IPath<ILatLng>
												System.out.println("[IPathListLatLng TypeAdapter.read()] Processing array of IPath<ILatLng>...");
												jsonReader.beginArray();
												while(jsonReader.hasNext()) {
													IPath<ILatLng> pathLatLng = gson.fromJson(jsonReader, IPathLatLng.class);
													System.out.println("[IPathListLatLng TypeAdapter.read()] field 'i': pathLatLng = "+pathLatLng);
													pathListLatLng.add(pathLatLng);
												}
												jsonReader.endArray();
											}
											else {
												System.err.println("[IPathListLatLng TypeAdapter.read()] field 'i': Next token is NOT an array!  Value was IGNORED!");
												jsonReader.skipValue();
											}
											
											break;
											
										default:	
											System.out.println("[IPathListLatLng TypeAdapter.read()] Ignoring fieldName = "+fieldName);
											jsonReader.skipValue(); // skip the processing of the associated value
											break;
									}									
								}
							};
							
							jsonTokenProcessor.process(jsonReader);
							return pathListLatLng;
						}

						@Override
						public void write(JsonWriter jsonWriter, IPathList<ILatLng> value) throws IOException {
							jsonWriter.beginArray();				
							value.forEach((path)->{
								try {
									jsonWriter.jsonValue(gson.toJson(path, IPathLatLng.class));
								} catch (IOException e) {
									System.err.println("[IPathListLatLng TypeAdapter.write()] Exception while processing latLng = "+path+": "+e);
									e.printStackTrace();
								}
							});
							
							jsonWriter.endArray();
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
