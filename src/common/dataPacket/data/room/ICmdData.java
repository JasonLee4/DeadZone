package common.dataPacket.data.room;

import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;


/**
 * DataPacketData type for sending a command to process a given unknown type of host.
 * @author Group G
 *
 * @param <T> type of host for which a processing command was sent
 */
public interface ICmdData<T extends IRoomConnectionData> extends IRoomConnectionData {
	
	/**
	 * @return the DataPacketID (host identifier) of this data type
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(ICmdData.class);
	}
	
	/**
	 * Delegates to the static GetID() method to retrieve the DataPacketID of this object.
	 * @return the DataPacketID (host identifier) of this object
	 */
	@Override
	public default IDataPacketID getID() {
		return ICmdData.GetID();
	}
	
	/**
	 * @return the DataPacketID (host identifier) for which a command was sent
	 */
	public IDataPacketID getUnknownMsgID();
	
	/**
	 * @return the processing command for the given unknown host type
	 */
	public ARoomDataPacketAlgoCmd<T> getAlgoCmd();
	
	/**
	 * Factory method for constructing a concrete ICmdData object.
	 * @param <H> type of data for which a processing command is being sent
	 * @param unknownMsgID DataPacketID (host identifier) for which a command is being sent
	 * @param unknownMsgCmd processing command for the given unknown host type
	 * @return concrete ICmdData object
	 */
	public static <H extends IRoomConnectionData> ICmdData<H> make(IDataPacketID unknownMsgID, ARoomDataPacketAlgoCmd<H> unknownMsgCmd) {
		return new ICmdData<H>() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = -7891443637898342687L;

			@Override
			public IDataPacketID getUnknownMsgID() {
				return unknownMsgID;
			}

			@Override
			public ARoomDataPacketAlgoCmd<H> getAlgoCmd() {
				return unknownMsgCmd;
			}
			
		};
	}

}
