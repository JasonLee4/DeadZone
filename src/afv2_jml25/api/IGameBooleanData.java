package afv2_jml25.api;

import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Indicates whether or not the game has finished
 * 
 * @author Andres Villada
 * @author Jason Lee
 */
public interface IGameBooleanData extends IRoomConnectionData {
	/**
	 * @return ID of the data packet for confirming game status with boolean value
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IGameBooleanData.class);
	}

	@Override
	public default IDataPacketID getID() {
		return IGameBooleanData.GetID();
	}

	/**
	 * @return data for game initialization
	 */
	public static IGameBooleanData make() {

		return new IGameBooleanData() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -9166135692646664999L;

		};

	}

}
