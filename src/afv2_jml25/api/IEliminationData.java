package afv2_jml25.api;

import java.util.UUID;

import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Data type indicating whether a team has been eliminated from the game
 * 
 * @author Andres Villada
 * @author Jason Lee
 */
public interface IEliminationData extends IRoomConnectionData {

	/**
	 * @return ID of the data packet for map
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IEliminationData.class);
	}

	@Override
	public default IDataPacketID getID() {
		return IEliminationData.GetID();
	}

	/**
	 * Makes an instance of this data type
	 * 
	 * @param teamUUID unique ID for this elimination data type
	 * @return data for game initialization
	 */
	public static IEliminationData make(UUID teamUUID) {

		return new IEliminationData() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -7790509049186355553L;

			@Override
			public UUID getEliminationUUID() {
				// TODO Auto-generated method stub
				return teamUUID;
			}

		};

	}

	/**
	 * @return the unique ID for this elimination data type
	 */
	public UUID getEliminationUUID();

}
