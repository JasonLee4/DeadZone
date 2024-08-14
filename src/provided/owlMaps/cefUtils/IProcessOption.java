package provided.owlMaps.cefUtils;



import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import provided.mixedData.MixedDataKey;

/**
 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS INTERFACE OR ANY IMPLEMENTATION OF IT! ***<br/>
 * A triple input Consumer used for processing option values into a Javascript options object in JSON form. 
 * IProcessOptions are used by AOptionsFiller to process each of the options in an options dictionary.
 * The parameters for the 3 input parameters are:
 * key = the MixedDataKey for the option
 * value = the Java value of the option
 * jsonWriter = the GSON JsonWriter object used to output JSON corresponding to the option value being processed.
 * @author swong
 *
 * @param <T>  The type of the option value
 */
@FunctionalInterface
public interface IProcessOption<T> { 
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Processes the given option key and value, outputting JSON via the given jsonWriter.
	 * @param key The option key for the given option
	 * @param value The value of the option to process
	 * @param jsonWriter  The GSON JsonWriter used to output the JSON representation of the given option
	 * @throws IOException thrown when the jsonWriter is encounters problems writing to its output stream
	 */
	public void accept(MixedDataKey<T> key, T value, JsonWriter jsonWriter) throws IOException;

	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Factory to make a default option processor that simply puts the given value directly into the Javascript options object. 
	 * Used for options whose Java values need to additional processing before being put into the Javascript
	 * options object.
	 * 
	 * @param <E>  The type of option the resultant processor will be used for.  Needed for type-safety.
	 * @param gson The GSON JSON processor object in use.
	 * @return Returns a default option processor
	 */
	public static <E> IProcessOption<E> makeDefault(Gson gson) {
		return (key, value, jsonWriter)->{
			
			try {
				jsonWriter.name(key.getDesc());
				jsonWriter.jsonValue(gson.toJson(value));
			} catch (IOException e) {
				System.err.println("[IProcessOption default] Exception while processing key = "+key.getDesc()+", value = "+value+", exception = "+e);
				e.printStackTrace();
			}	
		};
	}
	
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS METHOD! ***<br/>
	 * Factory to make a no-op option processor used to explicitly ignore a option type.  
	 * Typically used for options that are only used on the Java side.
	 * 
	 * @param <E>  The type of option the resultant processor will be used for.  Needed for type-safety.
	 * @return Returns a no-op option processor
	 */
	public static <E> IProcessOption<E> makeNoOp() {
		return (key, value, jsonWriter)->{
		};
	}
	
	
}