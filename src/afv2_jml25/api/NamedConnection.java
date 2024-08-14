package afv2_jml25.api;

//import common.INamedConnection;

import common.serverObj.IAppConnection;
import common.serverObj.INamedAppConnection;

/**
 * Concrete class that represents a NamedConnection dyad.
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class NamedConnection implements INamedAppConnection {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = -1224028228381818126L;

	/**
	 * Username provided by the connection.
	 */
	private String username;

	/**
	 * Stub of the remote connection object.
	 */
	private IAppConnection connectionStub;

	/**
	 * Constructor for a NamedConnection object. Takes a string name and IRemoteConnectionStub.
	 * @param username String name of user
	 * @param connectionStub2 stub of remote connection object
	 */
	public NamedConnection(String username, IAppConnection connectionStub2) {
		this.username = username;
		this.connectionStub = connectionStub2;
	}

	/**
	 * Gets the name associated with this dyad's connection stub
	 * @return the String name contained in this dyad
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.username;
	}

	/**
	 * Gets the remote connection stub contained in this dyad.
	 * @return the connection stub
	 */

	public IAppConnection getConnectionStub() {
		// TODO Auto-generated method stub
		return this.connectionStub;

	}

	/**
	 * Returns the int hashcode of the connection stub.
	 */
	public int hashCode() {
		return this.getConnectionStub().hashCode();
	}

	/**
	 * Compares other NamedConnections with this dyad. Determines equality by connection stub.
	 */
	public boolean equals(Object other) {
		//		return this.connectionStub.equals(connection.getConnectionStub());
		//
		//		if (null != other && other instanceof INamedAppConnection) {
		//			return ((INamedAppConnection) other).getConnectionStub().equals(this.getConnectionStub()); // delegate to the stubs
		//		}
		return false;

	}

	/**
	 * method to get stub.
	 */
	@Override
	public IAppConnection getStub() {
		// TODO Auto-generated method stub
		return null;
	}

}
