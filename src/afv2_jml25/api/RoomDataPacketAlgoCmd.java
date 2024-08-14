package afv2_jml25.api;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.data.IRoomConnectionData;
import common.dataPacket.data.room.ICmdRequestData;
import common.serverObj.INamedRoomConnection;
import provided.datapacket.IDataPacketID;

/**
 * Class for a command for visitor algo at room level.
 * 
 * @author Andres Villada
 * @author Jason Lee
 *
 */
public class RoomDataPacketAlgoCmd extends ARoomDataPacketAlgoCmd<IRoomConnectionData> {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 8998682553221141079L;
	/**
	 * Room connection dyad
	 */
	INamedRoomConnection namedConnection;

	/**
	 * Cache for visitors to be processed
	 */
	Map<IDataPacketID, List<RoomDataPacket<IRoomConnectionData>>> cache;

	/**
	 * Constructor for a room level algo command.
	 * 
	 * @param connection dyad for room connection
	 * @param cache      cache for visitors to be processed
	 */
	public RoomDataPacketAlgoCmd(INamedRoomConnection connection,
			Map<IDataPacketID, List<RoomDataPacket<IRoomConnectionData>>> cache) {
		this.namedConnection = connection;
		this.cache = cache;
	}

	/**
	 * Applies command to host.
	 */
	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<IRoomConnectionData> host, Void... params) {
		// sysLogger.log(LogLevel.INFO, index.toString());
		if (!cache.containsKey(index)) {
			cache.put(index, new ArrayList<>(Arrays.asList(host)));
		} else {
			cache.get(index).add(host);
			cache.put(index, cache.get(index));
		}
		try {
			// sysLogger.log(LogLevel.INFO, "Sending Cmd Request to " +
			// host.getSender().getName());
			host.getSender()
					.sendMessage(new RoomDataPacket<>(ICmdRequestData.make(host.getData().getID()), namedConnection));
		} catch (RemoteException e) {
			// sendErrorStatusData(host);
			e.printStackTrace();
		}
		return null;
	}

}
