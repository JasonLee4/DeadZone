package provided.owlMaps.cefUtils;

/**
 * Static definitions used by the CEF utilities, so both on the Java and Javascript sides.
 * IF THESE VALUES ARE MODIFIED, BE SURE TO ALSO CHANGE THE CORRESPONDING CONSTANT VALUES IN THE JAVASCRIPT CODE!!
 * @author swong
 *
 */
public interface ICefDefs {

	/**
	 * The name of the Javascript function to route callback functions
	 */
	public static final String CALLBACK_FN_NAME = "cefCallbackQuery";

	/**
	 * The name of the Javascript function to cancel callback functions
	 */
	public static final String CALLBACK_FN_NAME_CANCEL = CALLBACK_FN_NAME + "Cancel";

	/**
	 * The name of the Javascript function to route object creation return functions
	 */
	public static final String OBJ_CREATE_FN_NAME = "cefObjCreateQuery";

	/**
	 * The name of the Javascript function to cancel object creation return functions
	 */
	public static final String OBJ_CREATE_FN_NAME_CANCEL = OBJ_CREATE_FN_NAME + "Cancel";
	
    /**
     * The name of the system event used for post operations callback
     */
	public static final String SYS_EVENT_NAME_POST_OP = "___SYS_EVENT_NAME_POST_OP";

	/**
	 * The name of the Javascript function to route return values from function calls
	 */
	public static final String RETURN_VALUE_FN_NAME = "cefReturnValueQuery";

	/**
	 * The name of the Javascript function to cancel return values from function calls
	 */
	public static final String RETURN_VALUE_FN_NAME_CANCEL = RETURN_VALUE_FN_NAME + "Cancel";

	/**
	 * The name of the field in an CEF object structure that identifies its type.
	 */
	public static final String CEF_ENTITY_TYPE_FIELD_NAME = "___CEF_ENTITY_TYPE"; // Need to make sure won't accidentally conflict with any other names

	/**
	 * The name of the field in an CEF object structure that identifies its value.
	 */
	public static final String CEF_ENTITY_VALUE_FIELD_NAME = "___CEF_ENTITY_VALUE"; // Need to make sure won't accidentally conflict with any other names

	/**
	 * The value of the field in an CEF object structure that identifies its type as an ICefObject.
	 */
	public static final String CEF_ENTITY_TYPE_OBJECT = "___CEF_OBJECT"; // Need to make sure won't accidentally conflict with any other names

	/**
	 * The value of the field in an CEF object structure that identifies its type as a Runnable.
	 */
	public static final String CEF_ENTITY_TYPE_RUNNABLE = "___CEF_RUNNABLE"; // Need to make sure won't accidentally conflict with any other names

	/**
	 * The value of the field in an CEF object structure that identifies its type as a Consumer.
	 */
	public static final String CEF_ENTITY_TYPE_CONSUMER = "___CEF_CONSUMER"; // Need to make sure won't accidentally conflict with any other names

	/**
	 * The value of the field in an CEF object structure that identifies its type as a Google Maps google.maps.IconSequence.
	 * The value of the CEF_ENTITY_VALUE_FIELD_NAME field is the IconSquence options object to be used to instantiate the IconSequence.
	 */
	public static final String CEF_ENTITY_TYPE_GM_ICON_SEQUENCE = "___CEF_GM_ICON_SEQUENCE"; // Need to make sure won't accidentally conflict with any other names

	/**
	 * The value of the field in an CEF object structure that identifies its type as a Google Maps google.maps.Symbol.
	 * The value of the CEF_ENTITY_VALUE_FIELD_NAME field is the Symbol options object to be used to instantiate the Symbol.
	 */
	public static final String CEF_ENTITY_TYPE_GM_SYMBOL = "___CEF_GM_SYMBOL"; // Need to make sure won't accidentally conflict with any other names
	
}