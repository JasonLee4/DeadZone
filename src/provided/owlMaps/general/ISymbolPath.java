package provided.owlMaps.general;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Representation of a Google Maps SymbolPath
 * @author swong
 *
 */
public interface ISymbolPath {
	
	/**
	 * Standard visitor pattern "accept" method for ISymbolPath processing visitor algorithms.
	 * @param <R> The return type of the algorithm
	 * @param <P> The parameter type of the algorithm
	 * @param algo The visitor algorithm to execute
	 * @param params Vararg parameter values for the algorithm
	 * @return The return value of the algorithm
	 */
	public <R,P> R execute(ISymbolPathAlgo<R, P> algo, @SuppressWarnings("unchecked") P... params);
	
	
	/**
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

				if(typeToken.getRawType().equals(ISymbolPath.class)) {
	
					return (TypeAdapter<T>) new TypeAdapter<ISymbolPath>() {

						@Override
						public ISymbolPath read(JsonReader jsonReader) throws IOException {
							throw new UnsupportedOperationException("[ISymbolPath TypeAdapterFactor.read()] TypeAdapter.read() not implemented.");
						}

						@Override
						public void write(JsonWriter jsonWriter, ISymbolPath symbolPath) throws IOException {
							System.out.println("[ISymbolPath TypeAdapter.write()] called.");
							
							symbolPath.execute(new ISymbolPathAlgo<Void, Void>() {

								@Override
								public Void caseSymbolPathConstants(SymbolPathConstants host, Void... nu) {
									try {
										jsonWriter.value(host.getName());
									} catch (IOException e) {
										System.err.println("[ISymbolPath TypeAdapter.write()] Exception while processing SymbolPathConstant = "+host+": "+e);
										e.printStackTrace();
									}
									return null;
								}

								@Override
								public Void caseSVGSymbolPath(SVGSymbolPath host, Void... nu) {
									try {
										jsonWriter.value(host.getPath());
									} catch (IOException e) {
										System.err.println("[ISymbolPath TypeAdapter.write()] Exception while processing SVGSymbolPath = "+host+": "+e);
										e.printStackTrace();
									}
									return null;
								}
								
							});
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