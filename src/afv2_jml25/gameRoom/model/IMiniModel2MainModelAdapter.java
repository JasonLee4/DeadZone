package afv2_jml25.gameRoom.model;

import java.util.UUID;

import afv2_jml25.api.GameRoom;
import afv2_jml25.gameApp.mainController.MyAppConfig;
import provided.pubsubsync.IPubSubSyncManager;
import provided.rmiUtils.IRMIUtils;

/**
 * Interface for an adapter from room-level model to app-level model.
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public interface IMiniModel2MainModelAdapter {

	/**
	 * Gets the RMIUtils from app
	 * 
	 * @return rmi utils
	 */
	IRMIUtils getRMIUtils();

	/**
	 * Gets the pubsubsyncmanager from the app
	 * 
	 * @return the pubsubsync manager
	 */
	IPubSubSyncManager getPubSubManager();

	/**
	 * Gets the app configurations of the app
	 * 
	 * @return app configs from app
	 */
	MyAppConfig getAppConfig();

	/**
	 * Sends the chat room from the room-level to the app-level
	 * 
	 * @param room chat room
	 */
	void sendRoom(GameRoom room);

	/**
	 * Remove the room from the list of rooms in the app
	 * 
	 * @param uuid identifier of the chat room
	 */
	void removeRoom(UUID uuid);

	/**
	 * Gets the username of the app user
	 * 
	 * @return String user name
	 */
	String getUserName();

	/**
	 * Eliminates the team from the game
	 * 
	 * @param uuid unique id for the team room
	 */
	void eliminateTeam(UUID uuid);

	/**
	 * Checks to see if a team has won
	 */
	void checkWinner();
}
