package afv2_jml25.gameApp.mainView;

import afv2_jml25.api.GameRoom;
import common.serverObj.INamedAppConnection;
import common.serverObj.INamedRoomID;

/**
 * Interface for adapter from app view to app model
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public interface IMainView2ModelAdapter {

	/**
	 * Null object / singleton instance of this adapter.
	 */
	public static IMainView2ModelAdapter NULL_OBJECT = new IMainView2ModelAdapter() {

		@Override
		public void createChatRoom(String name) {

		}

		@Override
		public void quit() {

		}

		@Override
		public void connectToRoom(String RemoteHost) {

		}

		@Override
		public void sendMessage(String text) {

		}

		@Override
		public void joinRoom(GameRoom selectedItem) {

		}

		@Override
		public void inviteToRoom(GameRoom selectedItem, INamedAppConnection selectedItem2) {

		}

		@Override
		public void genGame() {

		}

		@Override
		public void requestRooms(INamedAppConnection iNamedAppConnection) {

		}

		@Override
		public void joinServerRoom(INamedRoomID selectedItem) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * Creates a chat room
	 * 
	 * @param name name of chat room
	 */
	void createChatRoom(String name);

	/**
	 * Quit out of app
	 */
	void quit();

	/**
	 * Connect to room
	 * 
	 * @param remoteHost remote server name of room
	 */
	void connectToRoom(String remoteHost);

	/**
	 * Sends message to rooms
	 * 
	 * @param text message to send
	 */
	void sendMessage(String text);

	/**
	 * Method to join a room
	 * 
	 * @param selectedItem selected room from droplist
	 */
	void joinRoom(GameRoom selectedItem);

	/**
	 * Method to invite a connected user to a room.
	 * 
	 * @param selectedItem  selected GameRoom
	 * @param selectedItem2 selected connected user
	 */
	void inviteToRoom(GameRoom selectedItem, INamedAppConnection selectedItem2);

	/**
	 * Generates the game and passes it along to players
	 * 
	 */
	public void genGame();

	/**
	 * A client request to receive the selected user's rooms
	 * 
	 * @param iNamedAppConnection the user the client wants the rooms from
	 */
	void requestRooms(INamedAppConnection iNamedAppConnection);

	/**
	 * A client request to join the selected room from the server's list of rooms
	 * 
	 * @param selectedRoom room from server room list the user wishes to join 
	 */
	void joinServerRoom(INamedRoomID selectedRoom);

}
