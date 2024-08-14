package provided.utils.loader.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.utils.loader.IObjectLoader;

/**
 * An IObjectLoader implementation that will walk through multiple locations, defined by multiple classname prefixes, to load an object of 
 * the specified type.
 * @author swong
 *
 * @param <ReturnT>   The type of object loaded by this IObjectLoader
 */
public class ObjectLoaderPath<ReturnT> implements IObjectLoader<ReturnT> {

	/**
	 * Logger to use
	 */
	private ILogger logger = ILoggerControl.getSharedLogger();

	/**
	 * List of of the given classname prefixes, which is effectively a path list
	 */
	private ArrayList<String> classnamePrefixes = new ArrayList<String>(Arrays.asList(new String[] { "" })); // default to one entry

	/**
	 * The error factory to use in case the class is never found
	 */
	BiFunction<String, Object[], ReturnT> errorFac;

	/**
	 * Constructor for the class.   The given errorFac is used to generate instances when the loadInstance() method
	 * is otherwise unable to do so because of a processing error.
	 * @param errorFac A factory method that takes the same array of input parameters that loadInstance() 
	 * takes and returns an instance of ReturnT.
	 * @param classnamePrefixes Vararg of the prefixes to search for the given classnames.  
	 * The prefixes should complete the fully qualified pathname when prepended to the className given to loadInstance().
	 * The prefixes are searched in the order given.
	 */
	@Deprecated
	public ObjectLoaderPath(Function<Object[], ReturnT> errorFac, String... classnamePrefixes) {
		// Wrap in a BiFunction and delegate to the other constructor
		this((classname, args) -> { 
			System.err.println("WARNING!! [ObjectLoaderPath] Using deprecated error factory type that discards the attempted classname:  "+classname);
			return errorFac.apply(args); 
		});


	}
	
	/**
	 * Constructor for the class.   The given errorFac is used to generate instances when the loadInstance() method
	 * is otherwise unable to do so because of a processing error.
	 * @param errorFac A factory method that takes 2 params: 
	 * The fully-qualified pathname of the attempted class and the array of input parameters.  
	 * The errofFac function returns an instance of ReturnT.	 
	 * @param classnamePrefixes Vararg of the prefixes to search for the given classnames.  
	 * The prefixes should complete the fully qualified pathname when prepended to the className given to loadInstance().
	 * The prefixes are searched in the order given.
	 */	
	public ObjectLoaderPath(BiFunction<String, Object[], ReturnT> errorFac, String... classnamePrefixes) {
		this.errorFac = errorFac;

		if (classnamePrefixes.length > 0) { // Want at least one element in the classnamePrefixes list
			this.classnamePrefixes = new ArrayList<String>(Arrays.asList(classnamePrefixes)); // convert the varargs array into an ArrayList
		}

	}

	/**
	 * Handler for exceptions or running out of places to search.
	 * @param ex The Exception to be handled
	 * @param fullClassName The fully qualified class name being searched for
	 * @param params Intended class constructor parameters
	 * @return The error object generated by the stored errorFac.
	 */
	ReturnT exceptionHandler(Exception ex, String fullClassName, Object... params) {
		// Print the exception to stderr
		logger.log(LogLevel.ERROR, "ObjectLoaderPath.loadInstance(" + fullClassName + ", "
				+ (new ArrayList<Object>(Arrays.asList(params))) + "):\n   Exception = " + ex);
		ex.printStackTrace();
		return errorFac.apply(fullClassName, params); // Use the errorFac to generate an error object.	
	}

	/**
	 * Instantiates and then delegates to a recursive IObjectLoader instance that goes through the saved list of classname prefixes searching
	 * for the partial name, className.   If the  class is never found, the exception printed and the saved errorFac will be invoked to 
	 * generate an error object result.
	 */
	@Override
	public ReturnT loadInstance(String className, Object... args) {
		try {
			Objects.requireNonNull(className, "Given classname was null!");
			return (new AObjectLoader<ReturnT>(logger) { // Explicitly pass the logger to ensure that the slave loaders use the same logger
				int prefixIdx = 0; // current prefix index.  A field is being used here so that prefixIdx is mutable.

				@Override
				protected ReturnT errorHandler(Exception ex, String fullClassName, Object... params) {
					if (prefixIdx < classnamePrefixes.size() - 1) { // Check if any more prefixes to use.  At this point, prefixIdx is the index that was just tried. 
						prefixIdx++; // increment to the next prefix
						try {
							return this.loadInstance(classnamePrefixes.get(prefixIdx) + className, params); // recurse using the next prefix
						} catch (Exception e) {
							logger.log(LogLevel.ERROR,
									"[ObjectLoaderPath.loadInstance()] Exception while loading instance!");
							return exceptionHandler(e, fullClassName, params);
						}
					} else { // No more prefixes to check.
						logger.log(LogLevel.ERROR,
								"[ObjectLoaderPath.loadInstance()] Class not found!  Class name prefixes searched: "
										+ classnamePrefixes);
						return exceptionHandler(ex, fullClassName, params);
					}
				}

			}).loadInstance(classnamePrefixes.get(0) + className, args); // Make initial recursive call, presuming at least one entry in the prefixes list
		} catch (Exception e) {
			logger.log(LogLevel.ERROR,
					"[ObjectLoaderPath.loadInstance()] Exception while searching for or instantiating the class!");
			return exceptionHandler(e, className, args);
		}
	}

}
