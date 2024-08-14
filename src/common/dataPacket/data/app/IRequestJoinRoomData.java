package common.dataPacket.data.app;

import common.dataPacket.data.IAppConnectionData;
import common.serverObj.INamedRoomID;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * DataPacketData type to request to join a particular room indicated by an
 * INamedRoomID.
 * 
 * The UUID of the INamedRoomID could be a "fake" UUID such that you have to be
 * force invited to join the actual room and to prevent stale data problems.
 * 
 * @author Group C
 */
public interface IRequestJoinRoomData extends IAppConnectionData {
	/**
	 * This method allows one to get the ID value directly from the interface.
	 * 
	 * @return The ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRequestJoinRoomData.class);
	}

	/**
	 * Allows concrete implementation to automatically generate their ID value
	 * 
	 * @return The ID value associated with this data type.
	 */
	@Override
	public default IDataPacketID getID() {
		return IRequestJoinRoomData.GetID();
	}

	/**
	 * @return the name-roomID dyad of the chatroom the sender is requesting to join
	 */
	public INamedRoomID getNamedRoomID();

	/**
	 * Factory method for creating a concrete instance of IRequestJoinRoomData.
	 * 
	 * @param namedRoomID - the name-roomID dyad of the chatroom the receiver is
	 *                    being invited to
	 * @return a concrete instance of IRequestJoinRoomData
	 */
	public static IRequestJoinRoomData make(INamedRoomID namedRoomID) {
		return new IRequestJoinRoomData() {

			/**
			 * For serialization.
			 */
			private static final long serialVersionUID = -8169038563294735210L;

			@Override
			public INamedRoomID getNamedRoomID() {
				return namedRoomID;
			}

		};
	}
}
