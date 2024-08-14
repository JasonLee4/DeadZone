package common.dataPacket;

import common.dataPacket.data.IAppConnectionData;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.IDataPacketID;

/**
 * Type-narrowed extension of ADataPacketAlgoCmd for commands used to process app-level data types.
 * @author Group G
 *
 * @param <D> type of data processed by this command
 */
public abstract class AAppDataPacketAlgoCmd<D extends IAppConnectionData>
		extends ADataPacketAlgoCmd<Void, D, Void, Void, AppDataPacket<D>> {

	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = -1677755354876967834L;

	/**
	 * Applies this command to the input host with the associated ID index.
	 * @param index ID of host for identification by the visitor
	 * @param host host to be processed
	 */
	@Override
	public abstract Void apply(IDataPacketID index, AppDataPacket<D> host, Void... params);

}
