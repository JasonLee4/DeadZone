package provided.owlMaps.map.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Represents a GeoJSON feature on the map
 * @author swong
 *
 */
public interface IMapDataFeature {
	
	/**
	 * Get the ID of the feature
	 * @return The feature's ID 
	 */
	public String getMapDataFeatureId();
	
	/**
	 * Get a dictionary of the feature's defined properties
	 * @return A dictionary mapping the name of the property to its value
	 */
	public Map<String, String> getProperties();
	
	/**
	 * Get the IMapDataGeometry that defines this feature.
	 * @return The geometry object for this feature
	 */
	public IMapDataGeometry getGeometry();

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Factory method to instantiate an IMapDataFeature instance from the given data
	 * @param id The ID value of the feature
	 * @param propertiesMap  A dictionary of the names of the properties of this feture mapped to the associated property value
	 * @param geometry The geometry that defines this feature.
	 * @return A new IMapDataFeature instance
	 */
	public static IMapDataFeature make(String id, Map<String, String> propertiesMap, IMapDataGeometry geometry) {
		
		
		return new IMapDataFeature() {
			
			/**
			 * Internal copy of the given properties map used to guard against mutation of the original dictionary
			 */
			Map<String, String> properties = Map.copyOf(propertiesMap); // Make sure it is immutable
			
			@Override
			public String getMapDataFeatureId() {
				return id;
			}

			@Override
			public Map<String, String> getProperties() {
				return properties;
			}

			@Override
			public IMapDataGeometry getGeometry() {
				return geometry;
			}
			
			@Override
			public String toString() {
				return "IMapDataFeature("+id+")";
			}
			
		};
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

				if(typeToken.getRawType().equals(IMapDataFeature.class)) {
					
					return (TypeAdapter<T>) new TypeAdapter<IMapDataFeature>() {

						@Override
						public IMapDataFeature read(JsonReader jsonReader) throws IOException {
							System.out.println("[IMapDataFeature TypeAdapter.read()] Processing IMapDataFeatureMap...");
							
							String id = null;
							Map<String, String> properties = null;
							IMapDataGeometry geometry = null;
							
							jsonReader.beginObject();
							while(jsonReader.hasNext()) {
								String fieldName = jsonReader.nextName();
								System.out.println("[IMapDataFeature TypeAdapter.read()] fieldName = "+fieldName);

								switch(fieldName) {
								
									case "id":
										id = jsonReader.nextString();
										break;
										
									case "properties":
										jsonReader.beginObject();
										properties = new HashMap<String, String>();
										
										while(jsonReader.hasNext()) {
											String propertyName = jsonReader.nextName();
											System.out.println("[IMapDataFeature TypeAdapter.read()] fieldName = "+fieldName);
											String propertyValue = jsonReader.nextString();
											properties.put(propertyName, propertyValue);
										}
										jsonReader.endObject();
										break;
										
									case "geometry":
										geometry = gson.fromJson(jsonReader, IMapDataGeometry.class);
										break;
									
									default:	
										System.out.println("[IMapDataFeature TypeAdapter.read()] Ignoring fieldName = "+fieldName+" = "+jsonReader.peek());
										jsonReader.skipValue(); // skip the processing of the associated value
										break;
								}
							}
							jsonReader.endObject();
							
							if(null == id || null == properties || null == geometry) {
								System.err.println("[IMapDataFeature TypeAdapter.read()] Missing fields during JSON decoding: id = "+id+", properties = "+properties+", geometry = "+geometry);
							}
							
							return make(id, properties, geometry);
						}

						@Override
						public void write(JsonWriter jsonWriter, IMapDataFeature feature) throws IOException {
							System.out.println("[IMapDataFeatureMap TypeAdapter.write()] Processing IMapDataFeature: "+feature);
							throw new UnsupportedOperationException("[IMapDataFeatureMap TypeAdapter.write()] Not implemented yet!");

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
