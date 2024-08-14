package afv2_jml25.gameRoom.view;

/**
 * Interface of adapter from room-level view to room model.
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public interface IMiniView2ModelAdapter {

	/**
	 * Quit out of chat app
	 */
	void exit();

	/**
	 * Sends message to all members part of the chat room.
	 * 
	 * @param msg message to be sent to all members of chat room
	 */
	void sendMessage(String msg);

	/**
	 * Broadcasts a message to all team rooms
	 * 
	 * @param msg message to be sent to all team rooms
	 */
	void broadcastMessage(String msg);

	/**
	 * Sends maps to other players
	 */
	void sendMap();

}
