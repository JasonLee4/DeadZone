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

import provided.owlMaps.general.ILatLng;

/**
 * A Java representation of a JavaScript google.maps.data.Geometry object such as that found in IMapDataFeatures
 * @author swong
 *
 */
public interface IMapDataGeometry {
	/**
	 * Get the type of this geometry
	 * @return This geometry's type
	 */
	public String getType();
	
	/**
	 * Get the ILatLngs that define this geometry
	 * @return a list of ILatLngs
	 */
	public List<ILatLng> getLatLngs();
	
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Factory method to instantiate an IMapDataGeometry instance from the given data
	 * @param type  The type of geometry object being created
	 * @param latLngList A list of ILatLngs that define the geometry
	 * @return An IMapDataGeometry instance
	 */
	public static IMapDataGeometry make(String type, List<ILatLng> latLngList) {
		return new IMapDataGeometry() {
			
			/**
			 * Internal copy of the given list of ILatLngs to protect against mutation of the original list.
			 */
			List<ILatLng> latLngs = List.copyOf(latLngList);  // Make sure its immutable

			@Override
			public String getType() {
				return type;
			}

			@Override
			public List<ILatLng> getLatLngs() {
				return latLngs;
			}

			@Override
			public String toString() {
				return "IMapDataGeometry("+type+")";
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

				if(typeToken.getRawType().equals(IMapDataGeometry.class)) {
					
					return (TypeAdapter<T>) new TypeAdapter<IMapDataGeometry>() {

						@Override
						public IMapDataGeometry read(JsonReader jsonReader) throws IOException {
							System.out.println("[IMapDataGeometry TypeAdapter.read()] Processing IMapDataGeometry...");
							
							String type = null;
							List<ILatLng> latLngs = null;
							
							jsonReader.beginObject();
							while(jsonReader.hasNext()) {
								String fieldName = jsonReader.nextName();
								System.out.println("[IMapDataGeometry TypeAdapter.read()] fieldName = "+fieldName);

								switch(fieldName) {
								
									case "type":
										type = jsonReader.nextString();
										break;
										
									case "latLngs":
										jsonReader.beginArray();
										latLngs = new ArrayList<ILatLng>();
										
										while(jsonReader.hasNext()) {
											latLngs.add(gson.fromJson(jsonReader, ILatLng.class));
										}
										jsonReader.endArray();
										break;

									
									default:	
										System.out.println("[IMapDataGeometry TypeAdapter.read()] Ignoring fieldName = "+fieldName+" = "+jsonReader.peek());
										jsonReader.skipValue(); // skip the processing of the associated value
										break;
								}
							}
							jsonReader.endObject();
							
							if(null == type || null == latLngs)  {
								System.err.println("[IMapDataGeometry TypeAdapter.read()] Missing fields during JSON decoding: type = "+type+", latLngs = "+latLngs);
							}
							
							return make(type, latLngs);
						}

						@Override
						public void write(JsonWriter jsonWriter, IMapDataGeometry geometry) throws IOException {
							System.out.println("[IMapDataGeometry TypeAdapter.write()] Processing IMapDataGeometry: "+geometry);
							throw new UnsupportedOperationException("[IMapDataGeometry TypeAdapter.write()] Not implemented yet!");

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
