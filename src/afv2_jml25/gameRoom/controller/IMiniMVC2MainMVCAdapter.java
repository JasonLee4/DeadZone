package afv2_jml25.gameRoom.controller;

import java.util.UUID;

import afv2_jml25.api.GameRoom;
import afv2_jml25.gameApp.mainController.MyAppConfig;
import provided.pubsubsync.IPubSubSyncManager;
import provided.rmiUtils.IRMIUtils;

/**
 * Adapter from mini MVC system to main MVC system.
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public interface IMiniMVC2MainMVCAdapter {

	/**
	 * Quits out the chat room
	 */
	public void exit();

	/**
	 * Gets the RMIUtils of the chat app
	 * @return IRMIUtils rmi utils of app
	 */
	public IRMIUtils getRMIUtils();

	/**
	 * Gets the pubsubsync manager of the app
	 * @return The Ipubsubsyncmanager manager from the app
	 */
	public IPubSubSyncManager getPubSubManager();

	/**
	 * Gets appconfigs
	 * @return MyAppConfig configs from chat app/main system
	 */
	public MyAppConfig getAppConfig();

	/**
	 * Sends the room to the main system/app level MVC
	 * @param room A chat room
	 */
	public void sendRoom(GameRoom room);

	/**
	 * Removes room from the list of chat rooms
	 * @param uuid UUID of chat room
	 */
	public void removeRoom(UUID uuid);

	/**
	 * Gets the user name
	 * @return String user name associated with app user
	 */
	public String getUserName();

	/**
	 * Broadcasts a message to all team rooms 
	 * 
	 * @param msg message to be broadcasted to all team rooms
	 */
	public void broadcastMessage(String msg);

	/**
	 * Eliminates a team from the game
	 * 
	 * @param uuid unique id for the team room
	 */
	public void eliminateTeam(UUID uuid);

	/**
	 * Checks to see if a team has won 
	 */
	public void checkWinner();

}
