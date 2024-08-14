package common.serverObj;

import java.rmi.Remote;
import java.rmi.RemoteException;

import common.dataPacket.AppDataPacket;
import common.dataPacket.data.IAppConnectionData;

/**
 * Server object for sending app-level messages.
 * @author Group G
 *
 */
public interface IAppConnection extends Remote {

	/**
	 * Sends a app-level message to be received by the owner of this stub's corresponding server object.
	 * @param data data packet to send
	 * @throws RemoteException for communication-related errors
	 */
	public void sendMessage(AppDataPacket<? extends IAppConnectionData> data) throws RemoteException;

}
