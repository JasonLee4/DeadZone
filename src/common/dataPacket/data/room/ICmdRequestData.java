package common.dataPacket.data.room;

import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * DataPacketData type for requesting a command to process a given unknown type of host.
 * @author Group G
 *
 */
public interface ICmdRequestData extends IRoomConnectionData {
	
	/**
	 * @return the DataPacketID (host identifier) of this data type
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(ICmdRequestData.class);
	}
	
	/**
	 * Delegates to the static GetID() method to retrieve the DataPacketID of this object.
	 * @return the DataPacketID (host identifier) of this object
	 */
	@Override
	public default IDataPacketID getID() {
		return ICmdRequestData.GetID();
	}
	
	/**
	 * @return the DataPacketID (host identifier) for which a command request was sent
	 */
	public IDataPacketID getUnknownMsgID();
	
	/**
	 * Factory method for constructing a concrete ICmdRequestData object.
	 * @param unknownMsgID DataPacketID (host identifier) for the command of which a request is being constructed
	 * @return concrete ICmdRequestData object
	 */
	public static ICmdRequestData make(IDataPacketID unknownMsgID) {
		return new ICmdRequestData() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = -7062327737926911870L;

			@Override
			public IDataPacketID getUnknownMsgID() {
				return unknownMsgID;
			}
			
		};
	}

}
