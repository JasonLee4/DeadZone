package common.dataPacket.data;

import provided.datapacket.DataPacket;
import provided.datapacket.IDataPacketData;

/**
 * Intermediate interface for all status message data types.
 * @author Group G
 *
 * @param <T> type of data within the received data packet being responded to
 */
public interface IStatusData<T extends IDataPacketData> extends IAppConnectionData, IRoomConnectionData {
	
	/**
	 * @return status message contained in this data object
	 */
	public String getStatusMsg();
	
	/**
	 * @return received data packet that is being responded to
	 */
	public DataPacket<T, ?> getCausalDataPacket();

}
