package provided.owlMaps.cefUtils.gson;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

/**
 * Abstract visitor JSON object token read processor for use with the GSON library.
 * Assumes that the JSON reader is about to read an object.
 * 
 * Provides default behavior for all the cases.  Implementations should override 
 * the desired cases, saving values via their closures.
 * Usage:  To process the object token, run the process() method.
 * @author swong
 *
 */
public abstract class AJsonObjectTokenProcessor {
	/**
	 * Process the next token in the stream by delegating to the associated case for that type of token
	 * @param jsonReader The reader for the JSON token stream
	 */
	public void process(JsonReader jsonReader) {
		try { 
			jsonReader.beginObject();
			while(jsonReader.hasNext()) {
				JsonToken token = jsonReader.peek();
//				System.out.println("[AJsonTokenProcessor.process()] token = "+token);
				if(JsonToken.NAME.equals(token)) {
					this.caseName(jsonReader);
				}
				else if(JsonToken.BEGIN_OBJECT.equals(token)) {
					this.caseBeginObject(jsonReader);
				}
				else if(JsonToken.END_OBJECT.equals(token)) {
					this.caseEndObject(jsonReader);
				}
				else if(JsonToken.BOOLEAN.equals(token)) {
					this.caseBoolean(jsonReader);
				}
				else if(JsonToken.NUMBER.equals(token)) {
					this.caseNumber(jsonReader);
				}
				else if(JsonToken.STRING.equals(token)) {
					this.caseString(jsonReader);
				}
				else if(JsonToken.NULL.equals(token)) {
					this.caseNull(jsonReader);
				}
				else if(JsonToken.BEGIN_ARRAY.equals(token)) {
					this.caseBeginArray(jsonReader);
				}
				else if(JsonToken.END_ARRAY.equals(token)) {
					this.caseEndArray(jsonReader);
				}
				else {
					System.out.println("[AJsonTokenProcessor.process()] Ignoring token = "+token);
					
				}
				
			}
			jsonReader.endObject();
			
		}
		catch(Exception e) {
			System.err.println("[AJsonTokenProcessor.process()] Exception = "+e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Process the token that begins an object definition.
	 * Defaults to configuring the jsonReader to begin reading an object definition.
	 * @param jsonReader The reader for the JSON token stream
	 * @throws IOException Thrown if there is an error reading the token stream.
	 */
	public void caseBeginObject(JsonReader jsonReader) throws IOException {
		System.out.println("[AJsonTokenProcessor.caseBeginObject()] Consuming beginning of object.");
		jsonReader.beginObject();
	}

	/**
	 * Process the token that ends an object definition.
	 * Defaults to configuring the jsonReader to stop reading an object definition.
	 * @param jsonReader The reader for the JSON token stream
	 * @throws IOException Thrown if there is an error reading the token stream.
	 */
	public void caseEndObject(JsonReader jsonReader)  throws IOException {
		System.out.println("[AJsonTokenProcessor.caseEndObject()] Consuming end of object.");
		jsonReader.endObject();		
	}
	
	/**
	 * Process the token that begins an array definition.
	 * Defaults to configuring the jsonReader to begin reading an array definition.
	 * @param jsonReader The reader for the JSON token stream
	 * @throws IOException Thrown if there is an error reading the token stream.
	 */
	public void caseBeginArray(JsonReader jsonReader)  throws IOException {
		System.out.println("[AJsonTokenProcessor.caseBeginArray()] Consuming beginning of array.");
		jsonReader.beginArray();		
	}
	
	/**
	 * Process the token that ends an array definition.
	 * Defaults to configuring the jsonReader to stop reading an array definition.
	 * @param jsonReader The reader for the JSON token stream
	 * @throws IOException Thrown if there is an error reading the token stream.
	 */
	public void caseEndArray(JsonReader jsonReader)  throws IOException {
		System.out.println("[AJsonTokenProcessor.caseEndObject()] Consuming end of array.");
		jsonReader.endArray();			
	}
	
	/**
	 * Process the token that indicates the name of a field in an object.  
	 * jsonReader.nextName() will read the String value of the name.  
	 * Follow nextName() with jsonReader.nextDouble()/String()/Boolean()/etc to read out the 
	 * value of the field.
	 * Defaults to reading and printing but otherwise ignoring the value and advancing to the next token.
	 * @param jsonReader The reader for the JSON token stream
	 * @throws IOException Thrown if there is an error reading the token stream.
	 */
	public void caseName(JsonReader jsonReader)  throws IOException{
		System.out.println("[AJsonTokenProcessor.caseName()] Consuming name: "+jsonReader.nextName());
	}
	
	/**
	 * Process the token that represents a number value.  Use jsonReader.nextDouble() to read the value.
	 * Defaults to reading and printing but otherwise ignoring the value and advancing to the next token.
	 * @param jsonReader The reader for the JSON token stream
	 * @throws IOException Thrown if there is an error reading the token stream.
	 */
	public void caseNumber(JsonReader jsonReader)  throws IOException {
		System.out.println("[AJsonTokenProcessor.caseNumber()] Consuming number:"+jsonReader.nextDouble());
	}
	
	/**
	 * Process the token that represents a string value.  Use jsonReader.nextString() to read the value.
	 * Defaults to reading and printing but otherwise ignoring the value and advancing to the next token.
	 * @param jsonReader The reader for the JSON token stream
	 * @throws IOException Thrown if there is an error reading the token stream.
	 */
	public void caseString(JsonReader jsonReader)  throws IOException {
		System.out.println("[AJsonTokenProcessor.caseString()] Consuming string: "+jsonReader.nextString());
	}
	
	/**
	 * Process the token that represents a boolean value.  Use jsonReader.nextBoolean() to read the value.
	 * Defaults to reading and printing but otherwise ignoring the value and advancing to the next token.
	 * @param jsonReader The reader for the JSON token stream
	 * @throws IOException Thrown if there is an error reading the token stream.
	 */
	public void caseBoolean(JsonReader jsonReader)  throws IOException {
		System.out.println("[AJsonTokenProcessor.caseBoolean()] Consuming boolean: "+jsonReader.nextBoolean());
	}
	
	/**
	 * Process the token that represents a null value.  Use jsonReader.nextNull() to skip to the next token.
	 * @param jsonReader The reader for the JSON token stream
	 * @throws IOException Thrown if there is an error reading the token stream.
	 */
	public void caseNull(JsonReader jsonReader)  throws IOException {
		System.out.println("[AJsonTokenProcessor.caseNull()] Consuming null value.");
		jsonReader.nextNull();
	}
	
}