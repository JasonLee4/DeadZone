package common.dataPacket;

import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketAlgo;

/**
 * Type-narrowed extension of DataPacketAlgo for visitor algorithms at the room level.
 * @author Group G
 *
 */
public class RoomDataPacketAlgo extends DataPacketAlgo<Void, Void> {

	
	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = -8179645259010875252L;

	/**
	 * Constructs an RoomDataPacketAlgo object. Delegates to the superclass constructor.
	 * @param defaultCmd default command of this visitor algorithm
	 */
	public RoomDataPacketAlgo(ARoomDataPacketAlgoCmd<? extends IRoomConnectionData> defaultCmd) {
		super(defaultCmd);
	}

}
