package afv2_jml25.gameApp.mainModel;


import afv2_jml25.gameRoom.controller.RoomController;

/**
 * Concrete Adapter from app-level model to chat room view
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class MainModel2MiniViewAdapter {
	/**
	 * Controller of a chat room MVC system
	 */
	RoomController room;

	/**
	 * Constructor that takes a controller for a room/mini-mvc
	 * @param room controller of chat room
	 */
	public MainModel2MiniViewAdapter(RoomController room) {
		this.room = room;
	}
}
