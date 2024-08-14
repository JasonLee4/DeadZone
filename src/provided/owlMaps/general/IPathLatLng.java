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
import provided.owlMaps.cefUtils.gson.GsonTypeWrap;

/**
 * 
 * *** FOR INTERNAL USE ONLY!  DEVELOPER CODE SHOULD NEVER DIRECTLY UTILIZE THIS INTERFACE! ***<br/>
 * A Path that is explicitly filled with ILatLngs.
 * This is used for situations where the generic type of the IPath needs to be known, e.g. for JSON encoding/decoding.
 * 
 * @author swong
 *
 */
public interface IPathLatLng extends IPath<ILatLng> {

	
	/**
	 * Factory for the GSON TypeAdapterFactory to process this class
	 * @return The TypeAdapterFactory to be used by GSON to process instances of this class.
	 */
	public static TypeAdapterFactory makeTypeAdapterFactory() {

		return new TypeAdapterFactory() {

			@SuppressWarnings("unchecked")
			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {

				if(typeToken.getRawType().equals(IPathLatLng.class)) {
					
					return (TypeAdapter<T>) new TypeAdapter<IPath<ILatLng>>() {

						@Override
						public IPath<ILatLng> read(JsonReader jsonReader) throws IOException {
							System.out.println("[IPathLatLng TypeAdapter.read()] Processing IPath<ILatLng>...");
							
							IPath<ILatLng> pathLatLng = IPath.make(); 
							
							AJsonObjectTokenProcessor jsonTokenProcessor = new AJsonObjectTokenProcessor() {
								@Override
								public void caseName(JsonReader jsonReader) throws IOException {
									String fieldName = jsonReader.nextName();
									System.out.println("[IPathLatLng TypeAdapter.read()] fieldName = "+fieldName);

									switch(fieldName) {
										case "i":
											if(JsonToken.BEGIN_ARRAY.equals(jsonReader.peek())) {
												// Should be an array of LatLngs
												System.out.println("[IPathLatLng TypeAdapter.read()] Processing array of LatLngs...");
												jsonReader.beginArray();
												while(jsonReader.hasNext()) {
													ILatLng latLng = gson.fromJson(jsonReader, ILatLng.class);
													System.out.println("[IPathLatLng TypeAdapter.read()] field 'i': latLng = "+latLng);
													pathLatLng.add(latLng);
												}
												jsonReader.endArray();
											}
											else {
												System.err.println("[IPathLatLng TypeAdapter.read()] field 'i': Next token is NOT an array!  Value was IGNORED!");
												jsonReader.skipValue();
											}
											
											break;
											
										default:	
											System.out.println("[IPathLatLng TypeAdapter.read()] Ignoring fieldName = "+fieldName);
											jsonReader.skipValue(); // skip the processing of the associated value
											break;
									}									
								}
							};
							
							jsonTokenProcessor.process(jsonReader);
							return pathLatLng;
						}

						@Override
						public void write(JsonWriter jsonWriter, IPath<ILatLng> value) throws IOException {
							jsonWriter.beginArray();				
							value.forEach((latLng)->{
								try {
									jsonWriter.jsonValue(gson.toJson(new GsonTypeWrap(latLng, ILatLng.class)));
								} catch (IOException e) {
									System.err.println("[IPathLatLng TypeAdapter.write()] Exception while processing latLng = "+latLng+": "+e);
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
