package common.dataPacket.data.status;

import common.dataPacket.data.IStatusData;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketData;
import provided.datapacket.IDataPacketID;

/**
 * Status message data type indicating that a received message was rejected.
 * @author Group G
 *
 * @param <T> type of data within the received data packet being rejected
 */
public interface IRejectStatusData<T extends IDataPacketData> extends IStatusData<T> {

	/**
	 * @return the DataPacketID (host identifier) of this data type
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRejectStatusData.class);
	}
	
	/**
	 * Delegates to the static GetID() method to retrieve the DataPacketID of this object.
	 * @return the DataPacketID (host identifier) of this object
	 */
	@Override
	public default IDataPacketID getID() {
		return IRejectStatusData.GetID();
	}
	
	/**
	 * Factory method that constructs a concrete IRejectStatusData object.
	 * @param <E> type of data within the received data packet that is being rejected
	 * @param causalDataPacket received data packet that is being rejected
	 * @param statusMsg status message
	 * @return concrete IRejectStatusData object
	 */
	public static <E extends IDataPacketData> IRejectStatusData<E> make(DataPacket<E, ?> causalDataPacket, String statusMsg) {
		return new IRejectStatusData<E>() {

			/**
			 * For serialization.
			 */
			private static final long serialVersionUID = 5887915609408574275L;

			@Override
			public DataPacket<E, ?> getCausalDataPacket() {
				return causalDataPacket;
			}

			@Override
			public String getStatusMsg() {
				return statusMsg;
			}
			
		};
	}
	
}
