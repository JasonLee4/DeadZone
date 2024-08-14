package common.serverObj;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Bootstrap server object to be bound in the Registry, whose only purpose
 * is to receive the INamedAppConnection dyad of a remote user.
 * @author Group G
 *
 */
public interface IInitialAppConnection extends Remote {
	
	/**
	 * Receives the input INamedAppConnection dyad and beings the connection process.
	 * @param namedConnection the dyad-wrapped stub we are sending over
	 * @throws RemoteException for communication-related errors
	 */
	public void receiveNamedConnection(INamedAppConnection namedConnection) throws RemoteException;

}
