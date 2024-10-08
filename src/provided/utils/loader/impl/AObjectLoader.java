package provided.utils.loader.impl;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.utils.loader.IObjectLoader;

/**
 * Factory that dynamically class loads and instantiates an object of type ReturnT
 * @author Stephen Wong
 *
 * @param <ReturnT>  The type of object to be created.
 */
public abstract class AObjectLoader<ReturnT> implements IObjectLoader<ReturnT> {

	/**
	 * Logger for this and subclasses to use
	 */
	protected ILogger logger = ILoggerControl.getSharedLogger();

	/**
	 * Maps the primitive  type class to the corresponding wrapper class, e.g.
	 * Integer.TYPE which is the class representation of "int", to
	 * Class&lt;Integer&gt;. This is needed when a primitive input parameter
	 * type is encountered.
	 * An anonymous inner class is used here so that the map can be loaded at instantiation time.
	 */
	private Map<Class<?>, Class<?>> primitiveTypes = new HashMap<Class<?>, Class<?>>() {
		private static final long serialVersionUID = 582688125217694628L;

		{
			// Manually load the map with the primitive types and their wrapper classes
			Class<?>[][] type2Types = new Class<?>[][] { { Boolean.TYPE, Boolean.class },
					{ Character.TYPE, Character.class }, { Byte.TYPE, Byte.class }, { Short.TYPE, Short.class },
					{ Integer.TYPE, Integer.class }, { Long.TYPE, Long.class }, { Float.TYPE, Float.class },
					{ Double.TYPE, Double.class }, { Void.TYPE, Void.class } };

			for (Class<?>[] typePair : type2Types) {
				put(typePair[0], typePair[1]);
			}
		}
	};

	/**
	 * Constructor for the class.   The given errorFac is used to generate instances when the loadInstance() method
	 * is otherwise unable to do so because of a processing error.   Uses the shared logger from ILoggerControl.
	 */
	public AObjectLoader() {
		this(ILoggerControl.getSharedLogger());
	}

	/**
	 * Constructor for the class.   The given errorFac is used to generate instances when the loadInstance() method
	 * is otherwise unable to do so because of a processing error.
	 * @param logger The logger to use
	 */
	public AObjectLoader(ILogger logger) {
		this.logger = logger;
	}

	/* (non-Javadoc)
	 * @see util.IObjectLoader#loadInstance(java.lang.String, java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ReturnT loadInstance(String className, Object... args) {
		try {
			Objects.requireNonNull(className, "Given classname was null!");
			Class<?> objectClass = Class.forName(className);
			return (ReturnT) getConstructor(objectClass, args).newInstance(args);

		} catch (Exception ex) {
			return errorHandler(ex, className, args); // delegate to the abstract error handler
		} catch (NoClassDefFoundError err) { // NoClassDefFound Error is not an Exception so must be handled separately.
			String errorMsg = "[AObjectLoader.loadInstance()] Invalid class name: " + className;
			logger.log(LogLevel.ERROR, errorMsg);
			return errorHandler(new TypeNotPresentException(errorMsg, err), className, args); // delegate to the abstract error handler, converting the Error to the equivalent Exception
		}
	}

	/**
	 * Abstract loading exception handler method
	 * @param ex The Exception that was generated
	 * @param className The fully qualified name of the class that was attempted to be loaded
	 * @param args The constructor parameters for the given class name.
	 * @return An "error" object
	 */
	abstract protected ReturnT errorHandler(Exception ex, String className, Object... args);

	/**
	 * Proper form of Class.getConstructor(Class&lt;?&gt;[] parameterTypes) that
	 * returns a constructor that will return a constructor of the given class
	 * that will accept the given argument list.  Correctly finds a constructor
	 * even if the constructor types are superclasses of the given arguments except 
	 * that it will not necessarily choose the more narrowed choice if more than one 
	 * possibility exists. 
	 * 
	 * @param aClass
	 *            A class object whose constructor is being searched for.
	 * @param args
	 *            a list of input values for the constructor
	 * @return a Constructor&lt;?&gt; that will work with the given set of input
	 *         parameter values.
	 * @throws IllegalArgumentException
	 *             when no constructor can be found that would accept the given
	 *             parameters.
	 */
	private Constructor<?> getConstructor(Class<?> aClass, Object[] args) throws IllegalArgumentException {
		// Get all the constructors of the given class
		Constructor<?> cs[] = aClass.getConstructors();

		// search the constructors for the first one whose types allow the given input values.
		for (int i = 0; i < cs.length; i++) {
			if (checkTypes(cs[i].getParameterTypes(), args)) {
				return cs[i]; // Found one! Return the constructor.   note that there might be more that would fit, perhaps better than this one.
			}
		}
		throw new IllegalArgumentException(
				"No constructor found that matches the arguments of the given input parameters.");
	}

	/**
	 * Utility method to check if the types of the elements in the given args
	 * array match the types in the given paramTypes array. That is, if a method
	 * takes input parameters of types paramTypes, would args be an allowable
	 * input?
	 * 
	 * @param paramTypes
	 *            An array of Class objects representing an ordered list of
	 *            types
	 * @param args
	 *            An array of input parameter values, whose types are being
	 *            checked against paramTypes.
	 * @return true if all arg elements are subclasses (assignable to) their
	 *         corresponding paramTypes elements. Returns false otherwise.
	 */
	private boolean checkTypes(Class<?>[] paramTypes, Object[] args) {
		if (args.length == paramTypes.length) {
			for (int idx = 0; idx < args.length; idx++) {
				// Special case if the input parameter is a primitive.
				// e.g.  Integer.TYPE is the type of int and even though int x = new Integer(42) is ok,
				// Integer.TYPE.isAssignableFrom(Integer.class) will return false!!!
				if (paramTypes[idx].isPrimitive()) {
					// Substitute the boxing class of the primitive type, e.g. Integer for int
					paramTypes[idx] = primitiveTypes.get(paramTypes[idx]);
				}
				// check if input parameter is an allowable subclass of the formal constructor parameter type.
				if (!paramTypes[idx].isAssignableFrom(args[idx].getClass())) {
					return false; // Types don't match
				}
			}
			return true; // types match!
		} else {
			return false; // wrong number of input parameters
		}
	}
}
