package provided.owlMaps.general;

import java.awt.geom.Point2D;
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
 * Represents a google.maps.Symbol object
 * Instances of this entity are generally not used on the Java side.   
 * Specifications for this object in options dictionaries are implemented 
 * as the options dictionary needed to instantiate the object on the JavaScript side 
 * Currently only built-in symbol shapes (google.maps.SymbolPath constants) are supported.
 * Custom SVG paths are not supported yet.
 * @author swong
 *
 */
public interface ISymbol extends Serializable {
	
	/**
	 * Get the ISymbolPath enum corresponding to the displayed shape of the symbol
	 * @return The shape of the symbol
	 */
	public ISymbolPath getPath();
	
	/**
	 * Get the position of the symbol relative to the marker or polyline 
	 * in pixels as a 2D point
	 * @return  The relative symbol position
	 */
	public Point2D getAnchor();
	
	
	/**
	 * Get the symbol's fill color
	 * @return A color string
	 */
	public String getFillColor();
	
	/**
	 * Get the symbol's fill opacity
	 * @return the fill opacity
	 */
	public double getFillOpacity();
	
	/**
	 * For symbols on markers only: Get the origin of the label relative to the origin of the path.
	 * The value is undefined for symbols on polylines.
	 * @return  The label origin position
	 */
	public Point2D getLabelOrigin();
	
	/**
	 * Get the clockwise rotation of the symbol in degrees.  For symbols in IIconSequences, 
	 * the angle is relative to the edge the symbol is on. 
	 * @return the rotation angle
	 */
	public double getRotation();
	
	/**
	 * Get the amount (multiplicative factor) the symbol is scaled when displayed.
	 * @return The scale factor.
	 */
	public double getScale();
	
	/**
	 * Get the symbol's stroke color
	 * @return a color string
	 */
	public String getStrokeColor();
	
	/**
	 * Get the symbol's stroke opacity
	 * @return The stroke opacity 
	 */
	public double getStrokeOpacity();
	
	/**
	 * The symbol's stroke weight
	 * @return The stroke weight.
	 */
	public double getStrokeWeight();
	
	/**
	 * Get the options dictionary that would produce the equivalent ISymbol object.
	 * @return An options dictionary corresponding to this IIconSequence object.
	 */
	public IMixedDataDictionary getOptions();
	
	
	/**
	 * Factory method to instantiate an ISymbol from an options dictionary
	 * @param options An options dictionary contating ISymbolOptions
	 * @return A new ISymbol instance
	 */
	@SuppressWarnings("unchecked")
	public static ISymbol make(IMixedDataDictionary options) {
		
		IMixedDataDictionary _options = new MixedDataDictionary();
		
		Set<MixedDataKey<?>> keys = options.getKeys(ISymbolOptions.OPTIONS_ID);
				
		for(MixedDataKey<?> key: keys) {
			_options.put((MixedDataKey<Object>) key, (Object) options.get(key));
		}
		
		return new ISymbol() {
			private static final long serialVersionUID = -2289656957037854362L;

			@Override
			public ISymbolPath getPath() {
				return  _options.get(ISymbolOptions.PATH);
			}

			@Override
			public Point2D getAnchor() {
				return  _options.get(ISymbolOptions.ANCHOR);
			}

			@Override
			public String getFillColor() {
				return  _options.get(ISymbolOptions.FILL_COLOR);
			}

			@Override
			public double getFillOpacity() {
				return  _options.get(ISymbolOptions.FILL_OPACITY);
			}

			@Override
			public Point2D getLabelOrigin() {
				return  _options.get(ISymbolOptions.LABEL_ORIGIN);
			}

			@Override
			public double getRotation() {
				return  _options.get(ISymbolOptions.ROTATION);
			}

			@Override
			public double getScale() {
				return  _options.get(ISymbolOptions.SCALE);
			}

			@Override
			public String getStrokeColor() {
				return  _options.get(ISymbolOptions.STROKE_COLOR);
			}

			@Override
			public double getStrokeOpacity() {
				return  _options.get(ISymbolOptions.STROKE_OPACITY);
			}

			@Override
			public double getStrokeWeight() {
				return  _options.get(ISymbolOptions.STROKE_WEIGHT);
			}

			@Override
			public IMixedDataDictionary getOptions() {
				MixedDataDictionary result = new MixedDataDictionary();
				Set<MixedDataKey<?>> keys = _options.getKeys(ISymbolOptions.OPTIONS_ID);
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

				if(typeToken.getRawType().equals(ISymbol.class)) {
	
					return (TypeAdapter<T>) new TypeAdapter<IMixedDataDictionary>() {

						@Override
						public IMixedDataDictionary read(JsonReader jsonReader) throws IOException {
							throw new UnsupportedOperationException("[ISymbol TypeAdapterFactor.read()] TypeAdapter.read() not implemented.");
						}

						@Override
						public void write(JsonWriter jsonWriter, IMixedDataDictionary optionsDict) throws IOException {
							System.out.println("[ISymbol TypeAdapter.write()] called.");			
							jsonWriter.beginObject();
							jsonWriter.name(ICefDefs.CEF_ENTITY_TYPE_FIELD_NAME);
							jsonWriter.value(ICefDefs.CEF_ENTITY_TYPE_GM_SYMBOL);
							jsonWriter.name(ICefDefs.CEF_ENTITY_VALUE_FIELD_NAME);	
							jsonWriter.jsonValue(gson.toJson(optionsDict, ISymbolOptions.class));
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
