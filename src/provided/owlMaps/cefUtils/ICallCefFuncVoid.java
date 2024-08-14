package provided.owlMaps.cefUtils;



/**
 * FOR INTERNAL USE ONLY!! DEVELOPER CODE SHOULD NEVER USE THIS INTERFACE OR ANY IMPLEMENTATION OF IT!
 * Represents an arbitrary call to a Javascript function as a Java functional entity.
 * The Javascript function is assumed to have a void return.
 * @author swong
 *
 */
@FunctionalInterface
public interface ICallCefFuncVoid {
	/**
	 * @param jsFuncName The name of the Javascript function to call
	 * @param params The vararg parameters to pass to the function call
	 */
	public void apply(String jsFuncName, Object...params); 
}