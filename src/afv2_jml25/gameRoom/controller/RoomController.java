package afv2_jml25.gameRoom.controller;

import java.util.UUID;
import java.util.function.Supplier;

import javax.swing.JComponent;

import afv2_jml25.api.GameRoom;
import afv2_jml25.gameApp.mainController.MyAppConfig;
import afv2_jml25.gameRoom.model.IMiniModel2MainModelAdapter;
import afv2_jml25.gameRoom.model.IMiniModel2ViewAdapter;
import afv2_jml25.gameRoom.model.RoomModel;
import afv2_jml25.gameRoom.view.IMiniView2ModelAdapter;
import afv2_jml25.gameRoom.view.RoomView;
import common.serverObj.INamedAppConnection;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.pubsubsync.IPubSubSyncManager;
import provided.rmiUtils.IRMIUtils;

/**
 * Controller class for a mini MVC that connect to a chat room.
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class RoomController {

	/**
	 * mini-MVC to Main-MVC adapter
	 */
	IMiniMVC2MainMVCAdapter miniMVC2MainMVCAdpt;

	/**
	 * View of the room level MVC
	 */
	RoomView roomView;

	/**
	 * Model of the room level MVC
	 */
	RoomModel roomModel;

	/**
	 * Logger of the system
	 */
	ILogger sysLogger = ILoggerControl.getSharedLogger();

	/**
	 * Constructor for the room controller, instantiates the model and view of
	 * mini-MVC
	 * 
	 * @param name          name of chat room
	 * @param mini2MainAdpt miniMVC to mainMVC adapter
	 * @param appConnection instance of an app to app connection
	 */
	public RoomController(String name, IMiniMVC2MainMVCAdapter mini2MainAdpt, INamedAppConnection appConnection) {
		this.miniMVC2MainMVCAdpt = mini2MainAdpt;

		this.roomModel = new RoomModel(new IMiniModel2ViewAdapter() {

			@Override
			public void displayTextMessage(String text) {
				roomView.displayTextMessage(text);
			}

			@Override
			public Runnable displayJComponent(String label, Supplier<JComponent> component) {
				return roomView.displayJComponent(label, component);
			}

		}, new IMiniModel2MainModelAdapter() {

			@Override
			public IRMIUtils getRMIUtils() {
				return miniMVC2MainMVCAdpt.getRMIUtils();
			}

			@Override
			public IPubSubSyncManager getPubSubManager() {
				return miniMVC2MainMVCAdpt.getPubSubManager();
			}

			@Override
			public MyAppConfig getAppConfig() {
				return miniMVC2MainMVCAdpt.getAppConfig();
			}

			@Override
			public void sendRoom(GameRoom room) {
				sysLogger.log(LogLevel.INFO, "New room in minimodel2mainmodeladpt: " + room.toString());
				miniMVC2MainMVCAdpt.sendRoom(room);
			}

			@Override
			public void removeRoom(UUID uuid) {
				miniMVC2MainMVCAdpt.removeRoom(uuid);
			}

			@Override
			public String getUserName() {
				return miniMVC2MainMVCAdpt.getUserName();
			}

			@Override
			public void eliminateTeam(UUID uuid) {
				miniMVC2MainMVCAdpt.eliminateTeam(uuid);
				
			}

			@Override
			public void checkWinner() {

				miniMVC2MainMVCAdpt.checkWinner();
			}

		}, name, appConnection);

		this.roomView = new RoomView(new IMiniView2ModelAdapter() {

			@Override
			public void exit() {
				miniMVC2MainMVCAdpt.exit();
				roomModel.quit();
			}

			@Override
			public void sendMessage(String text) {
				roomModel.sendMessage(text);
			}



			@Override
			public void broadcastMessage(String msg) {
				miniMVC2MainMVCAdpt.broadcastMessage(msg);
			}

			@Override
			public void sendMap() {
				roomModel.sendMap();
			}

		});

		// mainView.installRoomView(roomView);

	}

	/**
	 * Starts the room-level model and view.
	 * 
	 * @return the mini view/ chat room tab
	 */
	public JComponent start() {
		this.roomModel.start();
		this.roomView.start();
		return roomView;
	}

	/**
	 * Makes a user join a room and creates it on the GUI
	 * 
	 * @param room Chat room
	 * @return the mini view
	 */
	public JComponent join(GameRoom room) {
		this.roomModel.start();
		this.roomModel.joinRoom(room.getUUID());
		this.roomView.start();
		return roomView;
	}

	/**
	 * Gets the view/chat room GUI
	 * 
	 * @return the RoomView of this mini-MVC
	 */
	public RoomView getView() {
		return this.roomView;
	}

}
