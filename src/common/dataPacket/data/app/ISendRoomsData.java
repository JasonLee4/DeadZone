package common.dataPacket.data.app;

import java.util.HashSet;

import common.dataPacket.data.IAppConnectionData;
import common.serverObj.INamedRoomID;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * DataPacketData type representing a set of rooms sent to another client.
 * 
 * The sent INamedRoomID dyads can have "fake" UUIDs to prevent stale data
 * problems.
 * 
 * @author Group C
 */
public interface ISendRoomsData extends IAppConnectionData {

	/**
	 * This method allows one to get the ID value directly from the interface.
	 * 
	 * @return The ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(ISendRoomsData.class);
	}

	/**
	 * Allows concrete implementation to automatically generate their ID value
	 * 
	 * @return The ID value associated with this data type.
	 */
	@Override
	public default IDataPacketID getID() {
		return ISendRoomsData.GetID();
	}

	/**
	 * @return a HashSet of INamedRoomID dyads.
	 */
	public HashSet<INamedRoomID> getChatRooms();

	/**
	 * Factory method to create a concrete instance of ISendRoomsData.
	 * 
	 * @param data - a HashSet of INamedRoomIDs representing the rooms sent
	 * @return a concrete instance of ISendRoomsData
	 */
	public static ISendRoomsData make(final HashSet<INamedRoomID> data) {
		return new ISendRoomsData() {

			/**
			 * For serialization.
			 */
			private static final long serialVersionUID = -8437492348536882333L;

			@Override
			public HashSet<INamedRoomID> getChatRooms() {
				return data;
			}

		};
	}

}
