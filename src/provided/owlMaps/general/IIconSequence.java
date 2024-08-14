/**
 * 
 */
package provided.owlMaps.general;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.cefUtils.ICefDefs;

/**
 * Represents a google.maps.IconSequence object which is a sequence of identical icons on a line (could be polyline).
 * Instances of this entity are generally not used on the Java side.   
 * Specifications for this object in options dictionaries are implemented 
 * as the options dictionary needed to instantiate the object on the JavaScript side 
 * @author swong
 *
 */
public interface IIconSequence extends Serializable {

	/**
	 * Get the setting for whether or not the icons have a fixed rotation 
	 * @return True if each icon in the sequence has the same fixed rotation regardless of the angle of the edge on which it lies, false if each icon in the sequence is rotated to align with its edge. 
	 */
	public boolean getFixedRotation();
	
	/**
	 * Get the icon that is being displayed
	 * @return The icon as an ISymbol 
	 */
	public ISymbol getIcon();
	
	/**
	 * Get the distance from the start of the line at which the icon is to be rendered.
	 * The String value could be in a percentage format, e.g. "XX%" of the line's length or in pixels, e.g. "YYpx".
	 * @return The icon offset setting.
	 */
	public String getOffset();
	
	/**
	 * Get the distance between consecutive icons on the line.  
	 * The String value could be in a percentage format, e.g. "XX%" of the line's length or in pixels, e.g. "YYpx".
	 * A value of "0" means no repeated icons are displayed.
	 * @return The icon repeat setting
	 */
	public String getRepeat();
	
	/**
	 * Get the options dictionary that would produce the equivalent IIconSequence object.
	 * @return An options dictionary corresponding to this IIconSequence object.
	 */
	public IMixedDataDictionary getOptions();
	
	
	/**
	 * Factory method to instantiate an IIconSequence object from an options dictionary.
	 * @param options The options dictionary to configure the IIconSequence instance
	 * @return A new IIconSequence instance
	 */
	@SuppressWarnings("unchecked")
	public static IIconSequence make(IMixedDataDictionary options) {
		
		IMixedDataDictionary _options = new MixedDataDictionary(); // internal storage of the options dictionary
		
		Set<MixedDataKey<?>> keys = options.getKeys(IIconSequenceOptions.OPTIONS_ID);
		
		// Copy the given options dictionary to guard against mutation of the original.
		for(MixedDataKey<?> key: keys) {
			_options.put((MixedDataKey<Object>) key, (Object) options.get(key));
		}
		
		return new IIconSequence() {

			private static final long serialVersionUID = -1149138799120165931L;

			@Override
			public boolean getFixedRotation() {
				return  _options.get(IIconSequenceOptions.FIXED_ROTATION);
			}

			@Override
			public ISymbol getIcon() {
				return  ISymbol.make(_options.get(IIconSequenceOptions.ICON));
			}

			@Override
			public String getOffset() {
				return  _options.get(IIconSequenceOptions.OFFSET);
			}

			@Override
			public String getRepeat() {
				return  _options.get(IIconSequenceOptions.REPEAT);
			}

			@Override
			public IMixedDataDictionary getOptions() {
				MixedDataDictionary result = new MixedDataDictionary();
				Set<MixedDataKey<?>> keys = _options.getKeys(IIconSequenceOptions.OPTIONS_ID);
				// Copy the given options dictionary to guard against mutation of the original.
				for(MixedDataKey<?> key: keys) {
					result.put((MixedDataKey<Object>) key, (Object) _options.get(key));
				}
				
				return result;
			}
			
		};
	}
	
	/**
	 * *** FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER DIRECTLY USE THIS METHOD! ***<br/>
	 * Instantiate a GSON TypeAdapterFactory for the class
	 * @return A TypeAdapterFactory to process the class type
	 */
	public static TypeAdapterFactory makeTypeAdapterFactory() {
		
		// Assumes that the class is being represented as its options dictionary and thus needs to create 
		// a wrapper for the options object

		return new TypeAdapterFactory() {

			@SuppressWarnings("unchecked")
			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {

				if(typeToken.getRawType().equals(IIconSequence.class)) {
	
					return (TypeAdapter<T>) new TypeAdapter<IMixedDataDictionary>() {

						@Override
						public IMixedDataDictionary read(JsonReader jsonReader) throws IOException {
							throw new UnsupportedOperationException("[IIconSequence TypeAdapter.read()] TypeAdapter.read() not implemented.");
						}

						@Override
						public void write(JsonWriter jsonWriter, IMixedDataDictionary optionsDict) throws IOException {
							// The object is expressed as its options --> wrap it so the JS side knows what type of object to instantiate 
			
							System.out.println("[IIconSequence TypeAdapter.write()] called.");
							jsonWriter.beginObject();
							jsonWriter.name(ICefDefs.CEF_ENTITY_TYPE_FIELD_NAME);
							jsonWriter.value(ICefDefs.CEF_ENTITY_TYPE_GM_ICON_SEQUENCE);
							jsonWriter.name(ICefDefs.CEF_ENTITY_VALUE_FIELD_NAME);	
							jsonWriter.jsonValue(gson.toJson(optionsDict, IIconSequenceOptions.class));
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

	
}
