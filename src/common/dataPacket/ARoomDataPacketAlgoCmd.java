package common.dataPacket;

import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.IDataPacketID;

/**
 * Type-narrowed extension of ADataPacketAlgoCmd for commands used to process room-level data types.
 * @author Group G
 *
 * @param <D> type of data processed by this command
 */
public abstract class ARoomDataPacketAlgoCmd<D extends IRoomConnectionData>
		extends ADataPacketAlgoCmd<Void, D, Void, ICmd2ModelAdapter, RoomDataPacket<D>> {

	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = 3133934802055702596L;

	/**
	 * Applies this command to the input host with the associated ID index.
	 * @param index ID of host for identification by the visitor
	 * @param host host to be processed
	 */
	@Override
	public abstract Void apply(IDataPacketID index, RoomDataPacket<D> host, Void... params);

}
