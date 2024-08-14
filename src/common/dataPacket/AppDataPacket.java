package common.dataPacket;

import common.dataPacket.data.IAppConnectionData;
import common.serverObj.INamedAppConnection;
import provided.datapacket.DataPacket;

/**
 * Type-narrowed extension of DataPacket for packets sent at the app level.
 * @author Group G
 *
 * @param <T> type of data contained in this packet
 */
public class AppDataPacket<T extends IAppConnectionData> extends DataPacket<T, INamedAppConnection> {

	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = -6351687592901000576L;

	/**
	 * Constructs an AppDataPacket object with the input data and sender.
	 * @param data data of AppDataPacket object
	 * @param sender sender of the AppDataPacket object
	 */
	public AppDataPacket(T data, INamedAppConnection sender) {
		super(data, sender);
	}

}
