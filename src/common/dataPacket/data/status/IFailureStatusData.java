package common.dataPacket.data.status;

import common.dataPacket.data.IStatusData;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketData;
import provided.datapacket.IDataPacketID;

/**
 * Status message data type indicating that a received message failed to process.
 * @author Group G
 *
 * @param <T> type of data within the received data packet that failed to process
 */
public interface IFailureStatusData<T extends IDataPacketData> extends IStatusData<T> {

	/**
	 * @return the DataPacketID (host identifier) of this data type
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IFailureStatusData.class);
	}
	
	/**
	 * Delegates to the static GetID() method to retrieve the DataPacketID of this object.
	 * @return the DataPacketID (host identifier) of this object
	 */
	@Override
	public default IDataPacketID getID() {
		return IFailureStatusData.GetID();
	}
	
	/**
	 * Factory method that constructs a concrete IFailureStatusData object.
	 * @param <E> type of data within the received data packet that failed to process
	 * @param causalDataPacket received data packet that failed to process
	 * @param statusMsg status message
	 * @return concrete IFailureStatusData object
	 */
	public static <E extends IDataPacketData> IFailureStatusData<E> make(DataPacket<E, ?> causalDataPacket, String statusMsg) {
		return new IFailureStatusData<E>() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = -777237861184750313L;

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
