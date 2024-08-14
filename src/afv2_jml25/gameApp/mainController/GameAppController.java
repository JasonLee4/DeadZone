package afv2_jml25.gameApp.mainController;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import afv2_jml25.api.GameRoom;
import afv2_jml25.gameApp.mainModel.GameAppModel;
import afv2_jml25.gameApp.mainModel.IMainModel2ViewAdapter;
import afv2_jml25.gameApp.mainModel.MainModel2MiniViewAdapter;
import afv2_jml25.gameApp.mainView.GameAppView;
import afv2_jml25.gameApp.mainView.IMainView2ModelAdapter;
import afv2_jml25.gameRoom.controller.IMiniMVC2MainMVCAdapter;
import afv2_jml25.gameRoom.controller.RoomController;
import common.serverObj.IInitialAppConnection;
import common.serverObj.INamedAppConnection;
import common.serverObj.INamedRoomID;
import provided.config.impl.AppConfigChooser;
import provided.discovery.IEndPointData;
import provided.discovery.impl.model.DiscoveryModel;
import provided.discovery.impl.model.IDiscoveryModelToViewAdapter;
import provided.discovery.impl.view.DiscoveryPanel;
import provided.discovery.impl.view.IDiscoveryPanelAdapter;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.pubsubsync.IPubSubSyncManager;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.IRMIUtils;

//import hlw2_jlo7.client.controller.ClientController;

/**
 * Class for controller of mini-MVC/room-level
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class GameAppController {

	/**
	 * ChatApp GUI
	 */
	private GameAppView view;

	/**
	 * Model of the app MVC
	 */
	private GameAppModel model;

	/**
	 * Appconfig selected by user
	 */
	MyAppConfig selectedAppConfig;

	/**
	 * logger
	 */
	private ILogger sysLogger = ILoggerControl.getSharedLogger();

	/**
	 * The Discovery server UI panel for the view
	 */
	private DiscoveryPanel<IEndPointData> discPnl;

	/**
	 * A self-contained model to handle the discovery server. MUST be started AFTER
	 * the main model as it needs the IRMIUtils from the main model!
	 */
	private DiscoveryModel<?> discModel;

	/**
	 * Controller of ChatApp MVC system
	 */
	public GameAppController() {

		AppConfigChooser<MyAppConfig> appConfigChooser = new AppConfigChooser<MyAppConfig>(0,
				new MyAppConfig("Andres", IRMI_Defs.STUB_PORT_CLIENT, IRMI_Defs.CLASS_SERVER_PORT_CLIENT, "Andres",
						"Andres"),
				new MyAppConfig("Jason", IRMI_Defs.STUB_PORT_SERVER, IRMI_Defs.CLASS_SERVER_PORT_SERVER, "Jason",
						"Jason"),
				new MyAppConfig("Swong", IRMI_Defs.STUB_PORT_EXTRA, IRMI_Defs.CLASS_SERVER_PORT_EXTRA, "Swong",
						"Swong"));

		selectedAppConfig = appConfigChooser.choose();

		// IPubSubSyncManager pubSubSyncManager =
		// IPubSubSyncConnection.getPubSubSyncManager(sysLogger, , 0)

		model = new GameAppModel(selectedAppConfig, sysLogger, new IMainModel2ViewAdapter() {

			@Override
			public MainModel2MiniViewAdapter createChatRoom(IPubSubSyncManager pubSubSyncManager, String name) {

				return new MainModel2MiniViewAdapter(null);
			}

			@Override
			public void joinChatRoom(GameRoom room) {
				// TODO Auto-generated method stub
				Runnable[] closeTabCmd = new Runnable[] { null }; // One-element array trick enables access below.

				// The mainView.addComponentFac() just delegates to its internal TabbedPanel
				// instance.
				closeTabCmd[0] = view.installRoomView(room.getName(), () -> { // Lambda expression for Supplier. The
																				// code is for the Supplier.get() method

					// By instantiating the entire mini-controller inside of the Suppler.get()
					// method,
					// we ensure that the mini-View is instantiated and started on the GUI thread!

					RoomController miniController = new RoomController(room.getName(), new IMiniMVC2MainMVCAdapter() {

						// Called by the mini-MVC when it has terminated and wants to have its view
						// removed from the main MVC.
						public void exit() {

							closeTabCmd[0].run(); // Close the tab when the miniMVC wants to exit
							// If the instantiated mini-controllers are being stored in the main controller,
							// be sure to remove this mini-controller!
							// May need to use one-element array trick instead of simple local variable
							// above or pass the reference to the mini-Controller as
							// an input parameter to this exit() method in order to access the
							// mini-Controller instance at this point.
						}

						@Override
						public IRMIUtils getRMIUtils() {
							return model.getRMIUtils();
						}

						@Override
						public IPubSubSyncManager getPubSubManager() {
							return model.getPubSubManager();
						}

						@Override
						public MyAppConfig getAppConfig() {
							return model.getAppConfig();
						}

						@Override
						public void sendRoom(GameRoom room) {
							sysLogger.log(LogLevel.INFO, "New room in miniMVC2MainMVC " + room.toString());
							model.receiveRoom(room);
						}

						@Override
						public void removeRoom(UUID uuid) {
							model.removeRoom(uuid);
						}

						@Override
						public String getUserName() {
							return model.getUsername();
						}

						@Override
						public void broadcastMessage(String msg) {
							model.broadcastMessage(msg);
						}

						@Override
						public void eliminateTeam(UUID uuid) {
							model.eliminateTeam(uuid);

						}

						@Override
						public void checkWinner() {

							model.checkWinner();
						}
					}, model.getNamedAppConnection());
					// If the mini-Controller instances are being stored by the main controller,
					// e.g. in a Set or UUID-MiniController dictionary, then save it here.

					return miniController.join(room); // The start() method returns the started mini-View which is
														// placed into a tab in the main View.
				});
			}

			@Override
			public void addRoomToList(GameRoom room) {
				// TODO Auto-generated method stub
				view.addRoomToList(room);
			}

			@Override
			public void addHostToDropDown(INamedAppConnection remoteDyad) {
				// TODO Auto-generated method stub
				view.addRemoteHost(remoteDyad);
			}

			@Override
			public void serverRooms(HashSet<INamedRoomID> roomsFromServer) {
				// TODO Auto-generated method stub
				view.updateServerRooms(roomsFromServer);
			}

		});
		discPnl = new DiscoveryPanel<IEndPointData>(new IDiscoveryPanelAdapter<IEndPointData>() {

			// sysLogger.setLogLevel(LogLevel.DEBUG);

			/**
			 * watchOnly is ignored b/c discovery model configured for watchOnly = true
			 */
			@Override
			public void connectToDiscoveryServer(String category, boolean watchOnly,
					Consumer<Iterable<IEndPointData>> endPtsUpdateFn) {
				// Ask the discovery model to connect to the discovery server on the given
				// category and use the given updateFn to update the endpoints list in the
				// discovery panel.
				discModel.connectToDiscoveryServer(category, endPtsUpdateFn);
			}

			@Override
			public void connectToEndPoint(IEndPointData selectedEndPt) {
				// Ask the discovery model to obtain a stub from a remote Registry using the
				// info from the given endpoint
				discModel.connectToEndPoint(selectedEndPt);
			}

		});

		discModel = new DiscoveryModel<IInitialAppConnection>(this.sysLogger,
				new IDiscoveryModelToViewAdapter<IInitialAppConnection>() {

					@Override
					public void addStub(IInitialAppConnection stub) {
						// TODO Auto-generated method stub
						model.connectToStub(stub);
					}

				});

		view = new GameAppView(new IMainView2ModelAdapter() {

			// A method to create a new mini-MVC with a given name
			public void createChatRoom(String name) {
				Runnable[] closeTabCmd = new Runnable[] { null }; // One-element array trick enables access below.

				// The mainView.addComponentFac() just delegates to its internal TabbedPanel
				// instance.
				closeTabCmd[0] = view.installRoomView(name, () -> { // Lambda expression for Supplier. The code is for
																	// the Supplier.get() method

					// By instantiating the entire mini-controller inside of the Suppler.get()
					// method,
					// we ensure that the mini-View is instantiated and started on the GUI thread!

					RoomController miniController = new RoomController(name, new IMiniMVC2MainMVCAdapter() {

						// Called by the mini-MVC when it has terminated and wants to have its view
						// removed from the main MVC.
						public void exit() {

							closeTabCmd[0].run(); // Close the tab when the miniMVC wants to exit
							// If the instantiated mini-controllers are being stored in the main controller,
							// be sure to remove this mini-controller!
							// May need to use one-element array trick instead of simple local variable
							// above or pass the reference to the mini-Controller as
							// an input parameter to this exit() method in order to access the
							// mini-Controller instance at this point.
						}

						@Override
						public IRMIUtils getRMIUtils() {
							// TODO Auto-generated method stub
							return model.getRMIUtils();
						}

						@Override
						public IPubSubSyncManager getPubSubManager() {
							// TODO Auto-generated method stub
							return model.getPubSubManager();
						}

						@Override
						public MyAppConfig getAppConfig() {
							// TODO Auto-generated method stub
							return model.getAppConfig();
						}

						@Override
						public void sendRoom(GameRoom room) {
							// TODO Auto-generated method stub
							model.receiveRoom(room);
						}

						@Override
						public void removeRoom(UUID uuid) {
							// TODO Auto-generated method stub
							model.removeRoom(uuid);
						}

						@Override
						public String getUserName() {
							// TODO Auto-generated method stub
							return model.getUsername();
						}

						@Override
						public void broadcastMessage(String msg) {
							// TODO Auto-generated method stub
							model.broadcastMessage(msg);
						}

						@Override
						public void eliminateTeam(UUID uuid) {
							model.eliminateTeam(uuid);

						}

						@Override
						public void checkWinner() {

							model.checkWinner();
						}
					}, model.getNamedAppConnection());
					// If the mini-Controller instances are being stored by the main controller,
					// e.g. in a Set or UUID-MiniController dictionary, then save it here.

					return miniController.start(); // The start() method returns the started mini-View which is placed
													// into a tab in the main View.
				});
			}

			@Override
			public void quit() {
				// TODO Auto-generated method stub
				model.quit();
			}

			@Override
			public void connectToRoom(String RemoteHost) {
				// TODO Auto-generated method stub
				model.connectTo(RemoteHost);
			}

			@Override
			public void sendMessage(String text) {
				// TODO Auto-generated method stub
				model.sendMsgToRemoteUser(text);
			}

			@Override
			public void joinRoom(GameRoom selectedItem) {
				// TODO Auto-generated method stub
				model.joinChatRoom(selectedItem);
			}

			@Override
			public void inviteToRoom(GameRoom selectedItem, INamedAppConnection selectedItem2) {
				// TODO Auto-generated method stub
				model.inviteToRoom(selectedItem, selectedItem2);
			}

			@Override
			public void genGame() {
				model.genGame();
			}

			@Override
			public void requestRooms(INamedAppConnection server) {
				model.requestRooms(server);
			}

			@Override
			public void joinServerRoom(INamedRoomID selectedRoom) {
				// TODO Auto-generated method stub
				model.joinServerRoom(selectedRoom);
			}

		});
	}

	/**
	 * Starts the discover server, model, and view.
	 * 
	 * @throws RemoteException remote invoke exception
	 */
	public void start() throws RemoteException {
		// IPubSubSyncManager pubSubSyncManager =
		// IPubSubSyncConnection.getPubSubSyncManager(sysLogger, model.getRMIUtils(), );

		model.start();

		this.discPnl.start();

		// discModel.start(model.getRMIUtils(), "Chat App", IConnection.BOUND_NAME);
		discModel.start(model.getRMIUtils(), this.selectedAppConfig.name, this.selectedAppConfig.boundName);
		view.addDiscoveryComponent(discPnl);

		view.start();
	}

	/**
	 * Main method
	 * 
	 * @param args parameters
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					(new GameAppController()).start();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
