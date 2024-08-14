package common.dataPacket.data.status;

import common.dataPacket.data.IStatusData;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketData;
import provided.datapacket.IDataPacketID;

/**
 * Status message data type indicating that an error occurred when receiving a message.
 * @author Group G
 *
 * @param <T> type of data within the received data packet that caused an error
 */
public interface IErrorStatusData<T extends IDataPacketData> extends IStatusData<T> {

	/**
	 * @return the DataPacketID (host identifier) of this data type
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IErrorStatusData.class);
	}
	
	/**
	 * Delegates to the static GetID() method to retrieve the DataPacketID of this object.
	 * @return the DataPacketID (host identifier) of this object
	 */
	@Override
	public default IDataPacketID getID() {
		return IErrorStatusData.GetID();
	}
		
	/**
	 * Factory method that constructs a concrete IErrorStatusData object.
	 * @param <E> type of data within the received data packet that caused an error
	 * @param causalDataPacket received data packet that caused an error
	 * @param statusMsg status message
	 * @return concrete IErrorStatusData object
	 */
	public static <E extends IDataPacketData> IErrorStatusData<E> make(DataPacket<E, ?> causalDataPacket, String statusMsg){
		return new IErrorStatusData<E>() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = 962423290468853740L;

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
