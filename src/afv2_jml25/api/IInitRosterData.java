package afv2_jml25.api;

import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Data for a team roster
 * 
 * @author Andres Villada
 * @author Jason Lee
 */
public interface IInitRosterData extends IRoomConnectionData {
	/**
	 * @return ID of the data packet for Initializing statuses of players in teams
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IInitRosterData.class);
	}

	@Override
	public default IDataPacketID getID() {
		return IInitRosterData.GetID();
	}

	/**
	 * Make instance of this message type.
	 * 
	 * @return data for game initialization
	 */
	public static IInitRosterData make() {

		return new IInitRosterData() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -2441063564499294402L;

		};

	}

}
