package provided.owlMaps.cefUtils.gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS CLASS! ***<br/>
 * A FuncParamsVoid extended to handle function calls that have a return value.
 * Enables the transmission of a request ID, which is needed for the return value callback.
 * Enables the JSON conversion of a vararg of parameters into a JSON object
 * with "params" key and JSON converted array of values.
 *
 * @author swong
 *
 */
public class FuncParams extends FuncParamsVoid{
	/**
	 * 
	 */
	//	/**
	//	 * Internal storage of the variable parameters
	//	 */
	//	private Consumer<?> callback;

	//	/**
	//	 * @param params  Vararg of the parameters to encapsulate
	//	 */
	//	public FuncParams(Consumer<?> callback, Object...params) {
	//		super(params);
	//		this.callback = callback;
	//		System.out.println("[FuncParams()] params = "+Arrays.deepToString(params));
	//	}

	//	/**
	//	 * Getter for the encapsulated callback Consumer
	//	 * @return a Consumer
	//	 */
	//	public Consumer<?> getCallback() {
	//		return callback;
	//	}

	/**
	 * The request ID for this call
	 */
	private UUID requestID;


//	/**
//	 * Construct an instance with a random request ID value.
//	 * @param params A vararg of parameters to encapsulate
//	 */
//	public FuncParams(Object...params) {
//		this(UUID.randomUUID(), params);
//	}	

	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Construct an instance with a given request ID value.
	 * @param requestID The request ID for the call
	 * @param params A vararg of parameters to encapsulate
	 */
	public FuncParams(UUID requestID, Object...params) {
		super(params);
		this.requestID = requestID;
		System.out.println("[FuncParams("+requestID+")] params = "+Arrays.deepToString(params));
	}	

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Getter for the encapsulated request ID
	 * @return a UUID
	 */
	public UUID getRequestID() {
		return this.requestID;
	}


	@Override
	public String toString() {
		return "FuncParams("+Arrays.deepToString(this.getParams())+")";
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
				if (typeToken.getRawType().equals(FuncParams.class) ) { 
					System.out.println("[FuncParams.makeTypeAdapterFactory() TypeAdapterFactory.create()] Making TypeAdapter for FuncParams object.");
					return (TypeAdapter<T>) new TypeAdapter<FuncParams>() {

						@Override
						public FuncParams read(JsonReader in) throws IOException {
							System.out.println("[FuncParams TypeAdapter.read()] called.");
							// Is there no need for a deserialization process for this class?
							throw new UnsupportedOperationException(
									"[FuncParams TypeAdapter.read()] Not implemented yet.");
						}

						@Override
						public void write(JsonWriter  out, FuncParams funcParams) throws IOException {
							System.out.println("[FuncParams TypeAdapter.write()] called.   funcParams = "+funcParams);
							out.beginObject();
							writeParams(gson, out, funcParams);

							out.name("requestID");
							out.jsonValue(gson.toJson(funcParams.getRequestID())); //new GsonTypeWrap(funcParams.getCallback(), Consumer.class)));
							out.endObject();
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