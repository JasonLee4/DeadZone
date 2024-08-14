package afv2_jml25.api;

import provided.datapacket.IDataPacketID;

/**
 * Concrete class that represents a NamedConnectionData instance.
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class NamedConnectionData {

	/**
	 * Int id of this NamedConnectionData
	 */
	int id;
	/**
	 * NamedConnection object
	 */
	NamedConnection namedConnection;

	/**
	 * Constructor for a class of data type that represents a NamedConnection dyad.
	 * @param id input ID of this data type
	 * @param namedConnection dyad to be sent as data
	 */
	public NamedConnectionData(int id, NamedConnection namedConnection) {
		this.id = id;
		this.namedConnection = namedConnection;
	}

	/**
	 * Gets the id associated with this data type.
	 * @return null
	 */
	public IDataPacketID getID() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets this data
	 * @return the named connection data contained by data type.
	 */
	public NamedConnection getData() {
		return this.namedConnection;
	}

}
