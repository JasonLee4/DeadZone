package provided.owlMaps.cefUtils.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS CLASS! ***<br/>
 * A concrete wrapper class used to enable GSON to process anonymous inner
 * classes where obj.getClass() does not return the parent class/interface of
 * the instance. This wrapper enables type information to be carried into the
 * GSON processing where the associated TypeAdapter can unwrap both the stored
 * object and its class information and redirect the processing as per that
 * specific class information.
 * 
 * @author swong
 *
 */
public class GsonTypeWrap {

	/**
	 * The stored class information about the store object
	 */
	private TypeToken<?> objType;

	/**
	 * The stored object to be processed
	 */
	private Object obj;

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Gson needs a no-parameter constructor to deserialization
	 */
	public GsonTypeWrap() {
	}

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Constructor for the class
	 * 
	 * @param obj      The object to be stored
	 * @param objType The class information about the stored object.
	 */
	public GsonTypeWrap(Object obj,Type objType) {
		this.obj = obj;
		this.objType = TypeToken.get(objType);
	}

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Accessor for the stored class information
	 * 
	 * @return The object's class information
	 */
	public Type getObjType() {
		return this.objType.getType();
	}
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Get the TypeToken corresponding to the type of the wrapped object
	 * @return a TypeToken
	 */
	public TypeToken<?> getObjTypeToken() {
		return this.objType;
	}

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Accessor for the stored object
	 * 
	 * @return The stored object
	 */
	public Object getObj() {
		return this.obj;
	}
	
	@Override
	public String toString() {
		return "GsonTypeWrap("+this.obj+", "+this.objType+")";
	}

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Instantiate a GSON TypeAdapterFactory that will instantiate a TypeAdapter for this class. 
	 * That TypeAdapter will redirect the JSON processing of the stored object back to the 
	 * supplied Gson processor using the stored Class object.
	 * 
	 * @return A new TypeAdapterFactory instance
	 */
	public static TypeAdapterFactory makeTypeAdapterFactory() {
		return new TypeAdapterFactory() {

			@SuppressWarnings("unchecked")
			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
				
				if (typeToken.getRawType().equals(GsonTypeWrap.class) ) { 
					System.out.println("[GsonTypeWrap.makeTypeAdapterFactory() TypeAdapterFactory.create()] Making TypeAdapter for GsonTypeWrap.");
					return (TypeAdapter<T>) new TypeAdapter<GsonTypeWrap>() {

						@Override
						public GsonTypeWrap read(JsonReader jsonReader) throws IOException {
							System.out.println("[GsonTypeWrap TypeAdapter.read()] called.");
							// Is there no need for a deserialization process for this class?
							throw new UnsupportedOperationException(
									"[GsonTypeWrap TypeAdapter.read()] Not implemented yet.");
						}

						@Override
						public void write(JsonWriter jsonWriter, GsonTypeWrap customObj) throws IOException {
							System.out.println("[GsonTypeWrap TypeAdapter.write()] called for wrapped class = "
									+ customObj.getObjType());

							// delegate the stored object to the to the gson processor using the stored class info
							
							// Simpler code but slower than delegating directly to associated TypeAdapter?   Does this handle more cases though?
//							jsonWriter.jsonValue(gson.toJson(customObj.getObj(), customObj.getObjType())); 
							
							// Delegating directly to the associated TypeAdapter may be faster that delegating to the Gson object.   Handles fewer cases?
							((TypeAdapter<Object>)gson.getAdapter(customObj.getObjTypeToken())).write(jsonWriter,  customObj.getObj());
						}
					};
				} else {
					return null; // given TypeToken not associated with this class.
				}

			}

		};
	}

}
