package provided.owlMaps.cefUtils.gson;

import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS CLASS! ***<br/>
 * Input parameter encapsulation for void return function calls!
 * Enables the JSON conversion of a vararg of parameters into a JSON object
 * with "params" key and JSON converted array of values.  
 * @author swong
 *
 */
public class FuncParamsVoid {
	/**
	 * Internal storage of the variable parameters
	 */
	private Object[] params;

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Constructor for the class
	 * @param params  Vararg of the parameters to encapsulate
	 */
	public FuncParamsVoid(Object...params) {
		this.params = params;
		System.out.println("[FuncParams()] params = "+Arrays.deepToString(params));
	}
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Getter for the encapsulated parameters
	 * @return An array of parameters
	 */
	public Object[] getParams() {
		return params;
	}
	
	@Override
	public String toString() {
		return "FuncParamsVoid("+Arrays.deepToString(this.params)+")";
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
				if (typeToken.getRawType().equals(FuncParamsVoid.class) ) { 
					System.out.println("[FuncParams.makeTypeAdapterFactory() TypeAdapterFactory.create()] Making TypeAdapter for FuncParams object.");
					return (TypeAdapter<T>) new TypeAdapter<FuncParamsVoid>() {

						@Override
						public FuncParamsVoid read(JsonReader in) throws IOException {
							System.out.println("[FuncParams TypeAdapter.read()] called.");
							// Is there no need for a deserialization process for this class?
							throw new UnsupportedOperationException(
									"[FuncParams TypeAdapter.read()] Not implemented yet.");
						}

						@Override
						public void write(JsonWriter  out, FuncParamsVoid funcParams) throws IOException {
							System.out.println("[FuncParams TypeAdapter.write()] called.   funcParams = "+funcParams);
							out.beginObject();
							writeParams(gson, out, funcParams);
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
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Utility method to write the stored parameters out as JSON using the suppled JsonWriter.   
	 * This utility is used the TypeAdapterFactories created by FuncParamsVoid and its subclasses.
	 * @param gson The GSON parser in use
	 * @param out The JsonWriter JSON output formatter
	 * @param funcParams The FuncParamsVoid or subclass that holds the parameters to process.
	 * @throws IOException if an error occurs while writing to the JsonWriter's output stream.
	 */
	protected static final void writeParams(Gson gson, JsonWriter out, FuncParamsVoid funcParams) throws IOException {
		out.name("params");
		out.beginArray();
		for(Object param:funcParams.getParams() ) {
			String jsonStr = gson.toJson(param);
			System.out.println("[FuncParamsVoid.writeParams()] processing param = "+(param.getClass().isArray() ? Arrays.deepToString((Object[])param) : param)+", jsonStr = "+jsonStr);
			out.jsonValue(jsonStr);
		}
		out.endArray();
	}
	
}