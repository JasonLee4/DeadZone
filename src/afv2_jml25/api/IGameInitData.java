package afv2_jml25.api;

import common.dataPacket.data.IRoomConnectionData;
import common.serverObj.INamedRoomConnection;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Data for game initialization
 * 
 * @author Andres Villada (afv2)
 * @author Jason Lee (jml25)
 */
public interface IGameInitData extends IRoomConnectionData {

	/**
	 * @return ID of the data packet for game initialization
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IGameInitData.class);
	}

	@Override
	public default IDataPacketID getID() {
		return IGameInitData.GetID();
	}

	/**
	 * Makes an instance of game initialization
	 * 
	 * @param player the player dyad
	 * @return data for game initialization
	 */
	public static IGameInitData make(INamedRoomConnection player) {

		return new IGameInitData() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = 205880865984700478L;

			@Override
			public INamedRoomConnection getPlayer() {
				return player;
			}

		};

	}

	/**
	 * @return the player dyad
	 */
	public INamedRoomConnection getPlayer();
}
