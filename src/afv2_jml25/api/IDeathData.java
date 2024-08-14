package afv2_jml25.api;

import common.dataPacket.data.IRoomConnectionData;
import common.serverObj.INamedRoomConnection;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Data on team room-level to indicate whether a player is dead
 * 
 * @author Andres Villada
 * @author Jason Lee
 */
public interface IDeathData extends IRoomConnectionData {

	/**
	 * @return ID of the data packet for player death
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IDeathData.class);
	}

	@Override
	public default IDataPacketID getID() {
		return IDeathData.GetID();
	}

	/**
	 * Makes the death data indication for the player
	 * 
	 * @param player dyad of a player
	 * @return data for game initialization
	 */
	public static IDeathData make(INamedRoomConnection player) {

		return new IDeathData() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 5042437520366243856L;

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
