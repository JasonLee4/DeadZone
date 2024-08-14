package provided.owlMaps.cefUtils.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefObject;
import provided.owlMaps.cefUtils.ICefUtils;
import provided.owlMaps.cefUtils.IOptionsFiller;
import provided.owlMaps.cefUtils.IProcessOption;

/**
 * FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS CLASS OR ANY SUBCLASS OF IT!
 * An abstract IOptionsFiller that provides the basic functionality of an options filler.  
 * Subclasses need only to add component-specific options processors.
 * @author swong
 * @param <TOptionsClass> The class associated with this options processor
 *
 */
public abstract class AOptionsFiller<TOptionsClass> implements IOptionsFiller {

	/**
	 * The map-dependent Javascript utilities object.
	 */
	private ICefUtils cefUtils;

	/**
	 * The mapping from options key to options processor for the given options type
	 */
	public Map<MixedDataKey<?>, IProcessOption<?>> optionFnMap = new HashMap <MixedDataKey<?>, IProcessOption<?>>();

	/**
	 * The UUID corresponding to the type of options this options filler is built for.  
	 * Each options type will have a unique ID value.
	 */
	private UUID optionsID;

	/**
	 * The class associated with this options filler.
	 */
	private Class<TOptionsClass> optionsClass;

	/**
	 * The Gson processing object.  Not available until after the TypeAdapterFactory has been accessed and its create() method is called.
	 */
	private Gson gson;


	/**
	 * Constructor for this abstract class
	 * @param cefUtils CEF utilities library
	 * @param optionsClass The Class of associated with this options filler.  MUST HAVE AN OPTIONS_ID STATIC FIELD!!
	 */
	public AOptionsFiller(ICefUtils cefUtils, Class<TOptionsClass> optionsClass) {
		this.cefUtils = cefUtils;
		this.optionsClass = optionsClass;
		try {
			this.optionsID = (UUID) optionsClass.getField("OPTIONS_ID").get(null);

		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			System.err.println("[AOptionsFiller constructor] Exception while retrieving OPTIONS_ID value: "+e);
			e.printStackTrace();
		}
	}

//	/**
//	 * Internal encapsulation of the ability to make a Javascript function call.
//	 * Used to decouple any subclass code from the ICefUtils object
//	 * @return A functional that can be used to make Javascript function calls.
//	 */
//	protected ICallCefFunc getCallCefFunc() {
//		return cefUtils::call;
//	}

	/**
	 * Utility method to convert a value to a JSON string where the proper processing is explictly determined by the 
	 * given Class object. 
	 * Use this method for anonymous inner class objects.
	 * @param <T> The type of the value to convert
	 * @param value The value to convert
	 * @param optionClass The Class which determines how the value will be processed.
	 * @return A JSON string
	 */
	protected <T> String toJson(T value, Class<?> optionClass) {
		return gson.toJson(value, optionClass);
	}

	/**
	 * Utility method to convert a value to a JSON string where the proper processing can be deduced from the object itself.
	 * WILL NOT WORK FOR ANONYMOUS INNER CLASS INSTANCES!! 
	 * This method simply delegates to the underlying Gson object. 
	 * @param <T> The type of the value to convert
	 * @param value The value to convert 
	 * @return A JSON string
	 */
	protected <T> String toJson(T value) {
		return gson.toJson(value);
	}

//	/**
//	 * Internal accessor for the options processor map 
//	 * @return The mapping from options key to options processor
//	 */
//	protected Map<MixedDataKey<?>, IProcessOption<?>> getOptionFnMap() {
//		return optionFnMap;
//	}

	/**
	 * Put the given options processor into the options processor map associated with the given options key.
	 * @param <T> The type of options value that will be processed by the given options processor
	 * @param key The options type to be processed
	 * @param value The option processor to use for the given option type
	 */
	protected <T> void put(MixedDataKey<T> key, IProcessOption<T> value) {
		optionFnMap.put(key, value);
	}


	@Override
	public TypeAdapterFactory makeTypeAdapterFactory() {

		return new TypeAdapterFactory() {

			@SuppressWarnings("unchecked")
			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {

				if(typeToken.getRawType().equals(optionsClass)) {
					AOptionsFiller.this.gson = gson;
					return (TypeAdapter<T>) new TypeAdapter<IMixedDataDictionary>() {

						@Override
						public IMixedDataDictionary read(JsonReader jsonReader) throws IOException {
							throw new UnsupportedOperationException("[AOptionsFiller.makeTypeAdapterFactory() TypeAdapterFactor.create()] TypeAdapter.read() not implemented.");
						}

						@Override
						public void write(JsonWriter jsonWriter, IMixedDataDictionary optionsDict) throws IOException {
							jsonWriter.beginObject();				
							processEntries(optionsDict, jsonWriter);
							jsonWriter.endObject();
						}

					};
				}
				else {
					return null;
				}
			}

		};
	}


	// TODO Isn't this just a standard ICefObject processor?
	/**
	 * Utility method to make the default Map option processor
	 * @return A processor for a map option value
	 */
	protected IProcessOption<ICefObject> makeDefaultMapOptionProcess() {
		return (key, value, jsonWriter)->{
			jsonWriter.name(key.getDesc());
			jsonWriter.jsonValue(gson.toJson(value, ICefObject.class));
		};
	}
	
//	protected <T> IProcessOption<T> makeNoOpProcessOption(){
//		return IProcessOption.makeNoOp();
//	}


	/**
	 * Utility method to process a given Java options key-value with any installed handler in the 
	 * internal optionFnMap or to use the IProcessOption.DEFAULT processing if no handler is installed.
	 * This method is necessary in order to insure that the type specified the key is the same as the type 
	 * that is retrieved from the optionsDict dictionary.
	 * @param <T> The type of the options value
	 * @param key The key to the options value
	 * @param value A dictionary of options values
	 * @param defaultProcessOption The processor to use if there is no override defined in optionFnMap
	 * @param jsonWriter The GSON JSON writer to output the processed values
	 */	
	@SuppressWarnings("unchecked")
	protected <T> void addToCefOptions(MixedDataKey<T> key, T value,  IProcessOption<?> defaultProcessOption, JsonWriter jsonWriter)  {
		try {
			((IProcessOption<T>)optionFnMap.getOrDefault(key, defaultProcessOption)).accept(key, value, jsonWriter);
		} 
		catch (IOException e) {
			System.err.println("[AOptionsFiller.addToCefOptions()] While processing key = "+key+", value = "+value+": Exception while writing out JSON value: "+e);			
			e.printStackTrace();
		}
	}

	/**
	 * Process the associated entries in the given optionsDict
	 * @param optionsDict  The dictionary of options
	 * @param jsonWriter The GSON JSON writer to output the processed values
	 */
	@SuppressWarnings("unchecked")
	protected void processEntries(IMixedDataDictionary optionsDict, JsonWriter jsonWriter) {

		IProcessOption<?> defaultProcessOption = IProcessOption.makeDefault(gson);
		for (MixedDataKey<?> key : optionsDict.getKeys(optionsID)) {						
			addToCefOptions((MixedDataKey<Object>)key, (Object) optionsDict.get(key), defaultProcessOption, jsonWriter);
		}

		MixedDataKey<ICefObject> mapKey = IOptionsFiller.makeMapKey(optionsID);
		
		if(!optionFnMap.containsKey(mapKey) ) { // Do the default map options processing if it wasn't overridden
			// Run default option to add map key, getting the map option value if it already exists in the optionsDict
			ICefObject value = optionsDict.containsKey(mapKey) ? optionsDict.get(mapKey) : cefUtils.getCefMapObj();  
			try {
				makeDefaultMapOptionProcess().accept(mapKey, value, jsonWriter);
			} 
			catch (IOException e) {
				System.err.println("[AOptionsFiller.processEntries()] Exception while creating default 'map' option: "+e);
				e.printStackTrace();
			}
		}


	}	

}
