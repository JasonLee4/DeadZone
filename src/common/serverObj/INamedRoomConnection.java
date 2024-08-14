package common.serverObj;

import java.io.Serializable;
import java.rmi.RemoteException;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.data.IRoomConnectionData;

/**
 * Dyad encapsulating an IRoomConnection stub and a friendly name for displaying on the GUI.
 * @author Group G
 *
 */
public interface INamedRoomConnection extends Serializable {
	
	/**
	 * @return the friendly name of this dyad
	 */
	public String getName();
	
	/**
	 * @return the IRoomConnection stub of this dyad
	 */
	public IRoomConnection getStub();
	
	/**
	 * @return the connection level dyad associated with this room level dyad
	 */
	public INamedAppConnection getNamedAppConnection();
	
	/**
	 * Calls the IRoomConnection Stub to send the corresponding message. 
	 * Sends a room-level message to be received by the owner of this stub's corresponding server object.
	 * @param data data packet to send
	 * @throws RemoteException for communication-related errors
	 */
	public default void sendMessage(RoomDataPacket<? extends IRoomConnectionData> data) throws RemoteException {
		this.getStub().sendMessage(data);
	}
	
	/**
	 * Factory method that constructs a concrete INamedRoomConnection object.
	 * @param name friendly name of the constructed dyad
	 * @param stub IRoomConnection stub of the constructed dyad
	 * @param appConn Connection level dyad associated with this room level dyad
	 * @return concrete INamedRoomConnection object
	 */
	public static INamedRoomConnection make(String name, IRoomConnection stub, INamedAppConnection appConn) {
		return new INamedRoomConnection() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = 8299994436426515145L;

			@Override
			public String getName() {
				return name;
			}

			@Override
			public IRoomConnection getStub() {
				return stub;
			}
			
			@Override
			public String toString() {
				return this.getName();
			}
			
			@Override
			public boolean equals(Object obj) {
				if (!(obj instanceof INamedRoomConnection o)) {
					return false;
				}
				return this.getStub().equals(o.getStub());
			}
			
			@Override
			public int hashCode() {
				return this.getStub().hashCode();
			}

			@Override
			public INamedAppConnection getNamedAppConnection() {
				return appConn;
			}
			
		};
	}

}
