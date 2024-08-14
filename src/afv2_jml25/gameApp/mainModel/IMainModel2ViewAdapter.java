package afv2_jml25.gameApp.mainModel;

import java.util.HashSet;

import afv2_jml25.api.GameRoom;
import common.serverObj.INamedAppConnection;
import common.serverObj.INamedRoomID;
import provided.pubsubsync.IPubSubSyncManager;

/**
 * Adapter from main model to main view
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public interface IMainModel2ViewAdapter {

	/**
	 * Null Object singleton
	 */
	public static IMainModel2ViewAdapter NULL_OBJECT = new IMainModel2ViewAdapter() {

		@Override
		public MainModel2MiniViewAdapter createChatRoom(IPubSubSyncManager pubSubSyncManager, String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void joinChatRoom(GameRoom room) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addRoomToList(GameRoom room) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addHostToDropDown(INamedAppConnection remoteDyad) {
			// TODO Auto-generated method stub

		}

		@Override
		public void serverRooms(HashSet<INamedRoomID> roomsFromServer) {
			// TODO Auto-generated method stub

		}

		// @Override
		// public MainModel2MiniViewAdapter createChatRoom() {
		// // TODO Auto-generated method stub
		// return null;
		// }

	};

	/**
	 * Creates chat rooms
	 * 
	 * @param pubSubSyncManager manager to create data channels
	 * @param name              name of room
	 * @return adapter
	 */
	public MainModel2MiniViewAdapter createChatRoom(IPubSubSyncManager pubSubSyncManager, String name);

	/**
	 * Method for joining a chat room
	 * 
	 * @param room a chat room
	 */
	public void joinChatRoom(GameRoom room);

	/**
	 * Adds chat room to list of chat rooms
	 * 
	 * @param room a chat room
	 */
	public void addRoomToList(GameRoom room);

	/**
	 * Method to add remote host to drop list.
	 * 
	 * @param remoteDyad dyad encapsulating data for user app to app connection
	 */
	public void addHostToDropDown(INamedAppConnection remoteDyad);

	/**
	 * Sends to client the requested server rooms
	 * 
	 * @param roomsFromServer rooms connected to by the server
	 */
	public void serverRooms(HashSet<INamedRoomID> roomsFromServer);

}
