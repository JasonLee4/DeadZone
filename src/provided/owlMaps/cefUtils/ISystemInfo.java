package provided.owlMaps.cefUtils;

/**
 *  *FOR INTERNAL USE ONLY!!* OWLMAPS  DEVELOPER CODE SHOULD *NOT* USE THIS INTERFACE!
 *  Provides dynamic system information for use by internal systems. 
 * @author swong
 *
 */
public interface ISystemInfo {
	/**
	 * Conversion factor from the length units currently being used by the system into meters.
	 * @return A length conversion factor of system_units/meter
	 */
	public double getSysLengthPerMeter();
	
}
