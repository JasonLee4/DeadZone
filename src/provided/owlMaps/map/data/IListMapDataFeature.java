package provided.owlMaps.map.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * *** FOR INTERNAL USE ONLY!  DEVELOPER CODE SHOULD NEVER DIRECTLY UTILIZE THIS CLASS! ***<br/>
 * Concrete subclass used to instantiate instances of IListMapDataFeature
 * @author swong
 *
 */
class ListMapDataFeature extends ArrayList<IMapDataFeature> implements IListMapDataFeature {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 8980018605221618955L;
	
}

/**
 * *** FOR INTERNAL USE ONLY!  DEVELOPER CODE SHOULD NEVER DIRECTLY UTILIZE THIS INTERFACE! ***<br/>
 * A List that is explicitly filled with IMapDataFeatures.
 * This is used for situations where the generic type of the List needs to be known, e.g. for JSON encoding/decoding.
 * @author swong
 */
public interface IListMapDataFeature extends List<IMapDataFeature> {
	
	/**
	 *  *** FOR INTERNAL USE ONLY!  DEVELOPER CODE SHOULD NEVER DIRECTLY UTILIZE THIS METHOD! ***<br/>
	 * Factory to instantiate an empty list.
	 * @return An empty list of IMapDataFeatures
	 */
	public static IListMapDataFeature make() {
		return new ListMapDataFeature();
	}
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Factory method to make the TypeAdapterFactory for use by GSON to process instances of this class.
	 * @return A TypeAdapterFactory instance
	 */
	public static TypeAdapterFactory makeTypeAdapterFactory() {

		return new TypeAdapterFactory() {

			@SuppressWarnings("unchecked")
			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {

				if(typeToken.getRawType().equals(IListMapDataFeature.class)) {
					
					return (TypeAdapter<T>) new TypeAdapter<IListMapDataFeature>() {

						@Override
						public IListMapDataFeature read(JsonReader jsonReader) throws IOException {
							System.out.println("[IListMapDataFeature TypeAdapter.read()] Processing [IMapDataFeature,...]");
							
							IListMapDataFeature result = IListMapDataFeature.make(); 
							
							jsonReader.beginArray();
							
							while(jsonReader.hasNext()) {
								result.add(gson.fromJson(jsonReader, IMapDataFeature.class));
							}
							
							jsonReader.endArray();

							return result;
						}

						@Override
						public void write(JsonWriter jsonWriter, IListMapDataFeature featureList) throws IOException {
							System.out.println("[IListMapDataFeature TypeAdapter.write()] Processing List<IMapDataFeature>: "+featureList);
							throw new UnsupportedOperationException("[IListMapDataFeature TypeAdapter.write()] Not implemented yet!");

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
