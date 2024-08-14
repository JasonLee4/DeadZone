package common.dataPacket;

import common.dataPacket.data.IRoomConnectionData;
import common.serverObj.INamedRoomConnection;
import provided.datapacket.DataPacket;

/**
 * Type-narrowed extension of DataPacket for packets sent at the room level.
 * @author Group G
 *
 * @param <T> type of data contained in this packet
 */
public class RoomDataPacket<T extends IRoomConnectionData> extends DataPacket<T, INamedRoomConnection> {

	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = 6902827342296986895L;

	/**
	 * Constructs an RoomDataPacket object with the input data and sender.
	 * @param data data of RoomDataPacket object
	 * @param sender sender of the RoomDataPacket object
	 */
	public RoomDataPacket(T data, INamedRoomConnection sender) {
		super(data, sender);
	}

}
