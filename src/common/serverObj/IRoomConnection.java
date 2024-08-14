package common.serverObj;

import java.rmi.Remote;
import java.rmi.RemoteException;

import common.dataPacket.RoomDataPacket;
import common.dataPacket.data.IRoomConnectionData;

/**
 * Server object for sending room-level messages.
 * @author Group G
 *
 */
public interface IRoomConnection extends Remote {
	
	/**
	 * Sends a room-level message to be received by the owner of this stub's corresponding server object.
	 * @param data data packet to send
	 * @throws RemoteException for communication-related errors
	 */
	public void sendMessage(RoomDataPacket<? extends IRoomConnectionData> data) throws RemoteException;
	
	

}
