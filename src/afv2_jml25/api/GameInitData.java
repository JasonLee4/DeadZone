package afv2_jml25.api;

import common.serverObj.INamedRoomConnection;

/**
 * Data necessary for initializing a game
 * 
 * @author Andres Villada (afv2)
 * @author Jason Lee (jml25)
 */
public class GameInitData implements IGameInitData {
	/**
	 * Player of the game.
	 */
	INamedRoomConnection player;
	/**
	 * For serialization
	 */
	private static final long serialVersionUID = -8403069656724447336L;

	/**
	 * Constructor for this GameInitData
	 * @param playerConnection player dyad
	 */
	public GameInitData(INamedRoomConnection playerConnection) {
		this.player = playerConnection;
	}

	@Override
	public INamedRoomConnection getPlayer() {
		// TODO Auto-generated method stub
		return player;
	}
}
