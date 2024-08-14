package afv2_jml25.gameApp.mainModel;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import afv2_jml25.api.AppDataPacketAlgoCmd;
import afv2_jml25.api.GameRoom;
import afv2_jml25.api.IGameInitData;
import afv2_jml25.api.IInitRosterData;
import afv2_jml25.gameApp.mainController.MyAppConfig;
import common.dataPacket.AAppDataPacketAlgoCmd;
import common.dataPacket.AppDataPacket;
import common.dataPacket.AppDataPacketAlgo;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.data.IAppConnectionData;
import common.dataPacket.data.app.IConnectionSetData;
import common.dataPacket.data.app.IInviteData;
import common.dataPacket.data.app.IQuitData;
import common.dataPacket.data.app.IRequestJoinRoomData;
import common.dataPacket.data.app.IRequestRoomsData;
import common.dataPacket.data.app.ISendRoomsData;
import common.dataPacket.data.room.ITextData;
import common.serverObj.IAppConnection;
import common.serverObj.IInitialAppConnection;
import common.serverObj.INamedAppConnection;
import common.serverObj.INamedRoomConnection;
import common.serverObj.INamedRoomID;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;
import provided.logger.ILogEntry;
import provided.logger.ILogEntryFormatter;
import provided.logger.ILogEntryProcessor;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.pubsubsync.IPubSubSyncConnection;
import provided.pubsubsync.IPubSubSyncManager;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.RMIUtils;

/**
 * Model for MVC-system at app level
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class GameAppModel {

	/**
	 * List of game rooms
	 */
	HashSet<GameRoom> roomList = new HashSet<>();
	/**
	 * Set of chat room UUIDs
	 */
	HashSet<UUID> idSet = new HashSet<>();

	/**
	 * Mapping from teams to game status (active or eliminated)
	 */
	ConcurrentHashMap<GameRoom, Boolean> gameResultMap = new ConcurrentHashMap<>();

	/**
	 * List of game rooms from server for client to select from
	 */
	HashSet<INamedRoomID> roomsFromServer = new HashSet<>();

	/**
	 * Datapacketid factory
	 */
	DataPacketIDFactory idFac = DataPacketIDFactory.Singleton;
	/**
	 * Set of connection to this chat app.
	 */
	HashSet<INamedAppConnection> connectionSet = new HashSet<>();

	/**
	 * Adapter from model to view (app-level)
	 */
	private IMainModel2ViewAdapter mainAdpt;

	/**
	 * Adapter from App-level model to chat room GUI
	 */
	private MainModel2MiniViewAdapter miniAdpt;

	/**
	 * RMIUtils
	 */
	private IRMIUtils rmiUtils;

	/**
	 * System logger
	 */
	private ILogger sysLogger;
	/**
	 * View logger
	 */
	private ILogger viewLogger;
	/**
	 * Stub registry
	 */
	private Registry registry;

	// private HashMap<UUID, IMainModel2MiniModelAdapter> adapterMap;

	/**
	 * app configurations
	 */
	private MyAppConfig appConfig;

	/**
	 * String name of user
	 */
	private String username;

	/**
	 * local connection stub
	 */
	IAppConnection connectionStub;

	/**
	 * local connection object
	 */
	IAppConnection connection;

	/**
	 * Dyad containing connection stub and name
	 */
	INamedAppConnection remoteNamedConnection;

	/**
	 * Local NamedConnection dyad to send
	 */
	INamedAppConnection localNamedConnection;

	/**
	 * remote connection object
	 */
	IAppConnection remoteConnection;

	/**
	 * PubSyncSync manager for channels
	 */
	IPubSubSyncManager pubSubSyncManager;

	/**
	 * InitialConnection wrapper for named connection dyads to be sent
	 */
	IInitialAppConnection initialConnection;

	// TODO make a concrete algo cmd
	/**
	 * app level visitor algo
	 */
	AppDataPacketAlgo appAlgo;

	/**
	 * Game lobby for all players
	 */
	GameRoom lobby;
	/**
	 * main to mini model adapter
	 */
	IMainModel2MiniModelAdapter mainModel2MiniModelAdpt;
	/**
	 * Dyad of the game server
	 */
	private INamedAppConnection serverConnection;

	/**
	 * Constructor for model
	 * 
	 * @param appConfig app configurations
	 * @param logger    logger for system
	 * @param mainAdpt  App-level model to view adapter
	 */
	public GameAppModel(MyAppConfig appConfig, ILogger logger, IMainModel2ViewAdapter mainAdpt) {

		System.out.println("HERE IS THE PORT:    " + appConfig.stubPort);
		this.sysLogger = logger;
		this.rmiUtils = new RMIUtils(logger);
		this.mainAdpt = mainAdpt;

		this.appConfig = appConfig;
		viewLogger = ILoggerControl.makeLogger(new ILogEntryProcessor() {
			ILogEntryFormatter formatter = ILogEntryFormatter.MakeFormatter("[%1s] %2s"); // custom log entry formatting
																							// "[level] msg"

			@Override
			public void accept(ILogEntry logEntry) {
				// GameAppModel.this.mainAdpt.displayMsg(formatter.apply(logEntry)); // plain
				// "this" refers to the
				// ILogEntryProcessor!
			}

		}, LogLevel.INFO);
		viewLogger.append(sysLogger); // Chain the system logger to the end of the view logger so that anything logged
										// to the view also goes to the system log (default = to console).
										// this.lobby = new GameRoom();
	}

	/**
	 * Starts the model for the chat app-level MVC system
	 */
	public void start() {

		rmiUtils.startRMI(appConfig.classServerPort);

		try {
			this.pubSubSyncManager = IPubSubSyncConnection.getPubSubSyncManager(sysLogger, rmiUtils,
					appConfig.stubPort);
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		username = appConfig.username;

		sysLogger.log(LogLevel.ERROR, "OUR NAME IS: " + username);

		connection = new IAppConnection() {

			@Override
			public void sendMessage(AppDataPacket<? extends IAppConnectionData> data) throws RemoteException {
				// TODO Fill this in and delete every other method here
				data.execute(appAlgo, (Void) null);
			}

		};

		// create the connection stub

		try {
			// connectionStub = (IConnection) UnicastRemoteObject.exportObject(connection,
			// IRMI_Defs.STUB_PORT_SERVER);
			connectionStub = (IAppConnection) UnicastRemoteObject.exportObject(connection, appConfig.stubPort);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		localNamedConnection = INamedAppConnection.make(username, connectionStub);

		this.connectionSet.add(localNamedConnection);

		initialConnection = new IInitialAppConnection() {

			@Override
			public void receiveNamedConnection(INamedAppConnection namedConnection) throws RemoteException {
				// TODO Auto-generated method stub
				// connectToStub(namedConnection);
				connectToDyad(namedConnection);
			}

		};

		try {
			// IInitialAppConnection initialStub =
			// (IInitialAppConnection)UnicastRemoteObject.exportObject(initialConnection,
			// IRMI_Defs.STUB_PORT_SERVER);
			IInitialAppConnection initialStub = (IInitialAppConnection) UnicastRemoteObject
					.exportObject(initialConnection, appConfig.stubPort);
			registry = rmiUtils.getLocalRegistry();
			registry.rebind(this.appConfig.boundName, initialStub);

			// registry.rebind(appConfig.boundName, initialStub);

			viewLogger.log(LogLevel.INFO, "Local Registry = " + registry);
			sysLogger.log(LogLevel.ERROR, "***** BOUND STUB TO REGISTRY*******");
		} catch (Exception e) {
			viewLogger.log(LogLevel.ERROR, "Exception while initializing RMI: " + e);
			e.printStackTrace();
			quit();
		}

		this.appAlgo = new AppDataPacketAlgo(new AppDataPacketAlgoCmd(this.localNamedConnection));

		// command for connection set
		IDataPacketID connectionSetID = idFac.makeID(IConnectionSetData.class);
		AAppDataPacketAlgoCmd<IConnectionSetData> connectionSetCmd = new AAppDataPacketAlgoCmd<IConnectionSetData>() {

			private static final long serialVersionUID = 8515296256324368979L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IConnectionSetData> host, Void... params) {
				// TODO Auto-generated method stub

				// do auto-connect back
				HashSet<INamedAppConnection> remoteNamedSet = host.getData().getConnectionSet();

				for (INamedAppConnection remoteDyad : remoteNamedSet) {

					// We aren't connected. Add dyad to connection set and send it our connection
					// set
					if (!connectionSet.contains(remoteDyad)) {
						connectionSet.add(remoteDyad);

						mainAdpt.addHostToDropDown(remoteDyad);

						IConnectionSetData cSetData = IConnectionSetData.make(connectionSet);

						AppDataPacket<IConnectionSetData> dp = new AppDataPacket<IConnectionSetData>(cSetData,
								localNamedConnection);

						try {
							remoteDyad.getStub().sendMessage(dp);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							sysLogger.log(LogLevel.ERROR, "Auto connect back failed.");
							e.printStackTrace();
						}
					}

				}

				return null;
			}

			// @Override
			// public Void apply(IDataPacketID index, AppDataPacket host, Void... params) {
			// // TODO Auto-generated method stub
			// return null;
			// }
			//
		};

		this.appAlgo.setCmd(connectionSetID, connectionSetCmd);

		// command for invite
		IDataPacketID inviteID = idFac.makeID(IInviteData.class);
		AAppDataPacketAlgoCmd<IInviteData> inviteCmd = new AAppDataPacketAlgoCmd<IInviteData>() {

			private static final long serialVersionUID = 7102004855867080393L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IInviteData> host, Void... params) {
				// TODO Auto-generated method stub

				// add the uuid and friendly name to the drop down list on the gui

				GameRoom newRoom = new GameRoom(host.getData().getNamedRoomID());

				// mainAdpt.addRoom(newRoom);
				if (!roomList.contains(newRoom)) {
					// add to gui and room list and connect
					joinChatRoom(newRoom);
					roomList.add(newRoom);
					mainAdpt.addRoomToList(newRoom);

				} else {
					sysLogger.log(LogLevel.INFO, "We are already in this room!");
				}

				return null;
			}

			// @Override
			// public Void apply(IDataPacketID index, AppDataPacket<IConnectionSetData>
			// host, Void... params) {
			// // TODO Auto-generated method stub
			// return null;
			// }

		};
		// IDataPacketID inviteID = idFac.makeID(IInviteData.class)

		this.appAlgo.setCmd(inviteID, inviteCmd);

		IDataPacketID quitID = idFac.makeID(IQuitData.class);
		AAppDataPacketAlgoCmd<IQuitData> quitCmd = new AAppDataPacketAlgoCmd<IQuitData>() {

			private static final long serialVersionUID = 6077733402864220095L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IQuitData> host, Void... params) {
				// TODO Auto-generated method stub

				// remove the sender from our list of connections
				connectionSet.remove(host.getSender());
				return null;
			}

			// @Override
			// public Void apply(IDataPacketID index, AppDataPacket<IConnectionSetData>
			// host, Void... params) {
			// // TODO Auto-generated method stub
			// return null;
			// }

		};

		this.appAlgo.setCmd(quitID, quitCmd);

		// Server to client: Client chooses which room it wants and sends it to server
		IDataPacketID sendRoomID = idFac.makeID(ISendRoomsData.class);
		AAppDataPacketAlgoCmd<ISendRoomsData> sendRoomCmd = new AAppDataPacketAlgoCmd<ISendRoomsData>() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = -6890813621982750747L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<ISendRoomsData> host, Void... params) {
				roomsFromServer.clear();
				roomsFromServer.addAll(host.getData().getChatRooms());
				mainAdpt.serverRooms(roomsFromServer);
				return null;
			}

		};

		this.appAlgo.setCmd(sendRoomID, sendRoomCmd);

		// Client to server: Client tells server which room it would like to join
		IDataPacketID requestJoinRoomID = idFac.makeID(IRequestJoinRoomData.class);
		AAppDataPacketAlgoCmd<IRequestJoinRoomData> joinRoomCmd = new AAppDataPacketAlgoCmd<IRequestJoinRoomData>() {

			/**
			 * For serialization.
			 */
			private static final long serialVersionUID = 682931779979136391L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IRequestJoinRoomData> host, Void... params) {
				try {
					host.getSender().sendMessage(new AppDataPacket<IInviteData>(
							IInviteData.make(host.getData().getNamedRoomID()), localNamedConnection));
				} catch (RemoteException e) {
					e.printStackTrace();

				}
				return null;
			}

		};

		this.appAlgo.setCmd(requestJoinRoomID, joinRoomCmd);

		// Client to server: Client requests room list from server. Server reads this to
		// send the client a list of its rooms
		IDataPacketID requestRoomsID = idFac.makeID(IRequestRoomsData.class);
		AAppDataPacketAlgoCmd<IRequestRoomsData> requestRoomsCmd = new AAppDataPacketAlgoCmd<IRequestRoomsData>() {

			/**
			 * For serialization.
			 */
			private static final long serialVersionUID = -4781878770843398412L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IRequestRoomsData> host, Void... params) {
				HashSet<INamedRoomID> roomsToSend = new HashSet<>();
				for (GameRoom room : roomList) {
					roomsToSend.add(INamedRoomID.make(room.getName(), room.getUUID()));

				}
				try {
					host.getSender().sendMessage(
							new AppDataPacket<ISendRoomsData>(ISendRoomsData.make(roomsToSend), localNamedConnection));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return null;
			}

		};

		this.appAlgo.setCmd(requestRoomsID, requestRoomsCmd);
	}

	/**
	 * Manual connection to a Registry to retrieve and process a stub from it.
	 * Connects to the Registry at the given address, retrieves a stub from it, then
	 * delegates to connectToStub() to process the stub.
	 * 
	 * @param remoteHost The IP address of the remote Registry
	 * @return A status message string
	 */

	public String connectTo(String remoteHost) {

		// TODO Fix this
		try {
			Registry registry = rmiUtils.getRemoteRegistry(remoteHost);
			System.out.println("!!!!GOT THE REMOTE REGISTRY!!!!\n");
			// gotta use a different bound name!!
			IInitialAppConnection remoteStub = (IInitialAppConnection) registry.lookup(appConfig.boundName);

			// connectToStub(remoteStub);

			sysLogger.log(LogLevel.ERROR, "****** GOT THE REMOTE STUB *******");

			// sendMsgToComputeEngine("Hi from Client!");

		} catch (Exception e) {
			e.printStackTrace();

			return "No connection established!";
		}
		return "Connection to " + remoteHost + " established!";
	}

	/**
	 * Process the newly acquired stub. This is the method that the discovery model
	 * uses in "Client" or "Client + Server" usage modes
	 * 
	 * @param stub The newly acquired stub
	 */
	public void connectToStub(IInitialAppConnection stub) { // Replace "IRemoteStubType" with the appropriate for the
															// application, i.e. the Remote type of stub in Registry)

		/**
		 * What should this method do? - When we get an initial app connection, send it
		 * our named connection
		 */

		try {
			stub.receiveNamedConnection(localNamedConnection);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Adds dyad to set of connection dyads and sends to the contained remote
	 * connection the current set of connections.
	 * 
	 * @param remoteDyad dyad containing connection stub and name
	 */
	public void connectToDyad(INamedAppConnection remoteDyad) {

		/**
		 * Start the connection process - we want to add the dyad to our complete graph
		 * - if we aren't already connected, add to our set of connections and send over
		 * our connection set - otherwise, do nothing.
		 */

		if (!this.connectionSet.contains(remoteDyad)) {
			this.connectionSet.add(remoteDyad);
			this.mainAdpt.addHostToDropDown(remoteDyad);

			IAppConnection remoteStub = remoteDyad.getStub();

			IConnectionSetData data = IConnectionSetData.make(connectionSet);

			AppDataPacket<IConnectionSetData> dp = new AppDataPacket<IConnectionSetData>(data,
					this.localNamedConnection);

			try {
				remoteStub.sendMessage(dp);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				this.sysLogger.log(LogLevel.ERROR, "Failed to send connection set.");
				e.printStackTrace();
			}

		}

	}

	/**
	 * Gets manager of pubsubsync channels
	 * 
	 * @return pubsubsync manager
	 */
	public IPubSubSyncManager getPubSubManager() {
		return this.pubSubSyncManager;
	}

	/**
	 * Create a chat room either by joining or starting one.
	 * 
	 * @param name name of chat room
	 */
	public void createChatRoom(String name) {

		miniAdpt = mainAdpt.createChatRoom(this.pubSubSyncManager, name);

		this.sysLogger.log(LogLevel.INFO, "Our list of rooms: " + this.roomList.toString());

	}

	/**
	 * Method for a user to join a chat room.
	 * 
	 * @param room a chat room
	 */
	public void joinChatRoom(GameRoom room) {
		if (!roomList.contains(room)) {
			mainAdpt.joinChatRoom(room);
			// joinChatRoom(chatRoom);
			this.roomList.add(room);
			this.idSet.add(room.getUUID());
		} else {
			this.sysLogger.log(LogLevel.INFO, "You're already in this chat room!");
		}

	}

	/**
	 * Quit out of app.
	 */
	public void quit() {
		try {

			// TODO: Change to message passing
			for (INamedAppConnection c : connectionSet) {
				// c.getStub().removeNamedConnection(this.localNamedConnection);
				IQuitData data = IQuitData.make();
				AppDataPacket<IQuitData> dp = new AppDataPacket<IQuitData>(data, localNamedConnection);
				c.getStub().sendMessage(dp);
			}
			registry.unbind(this.appConfig.boundName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rmiUtils.stopRMI();
		System.exit(0);
	}

	/**
	 * sends a message to the client
	 * 
	 * @param text the text to be sent.
	 */
	public void sendMsgToClient(String text) {

	}

	/**
	 * Sends a string message to the connected compute engine using the
	 * IRemoteTaskViewAdapter STUB received from the engine server. This message
	 * should also be echoed to the local user interface.
	 * 
	 * @param text The message to be sent
	 */

	public void sendMsgToRemoteUser(String text) {

	}

	/**
	 * Gets the app config for app
	 * 
	 * @return app configs
	 */
	public MyAppConfig getAppConfig() {
		return this.appConfig;
	}

	/**
	 * gets the RMIUtils
	 * 
	 * @return the RMIUtils.
	 */
	public IRMIUtils getRMIUtils() {
		return this.rmiUtils;
	}

	/**
	 * Receive a chat room
	 * 
	 * @param room chat room sent
	 */

	// TODO: Eventually delete this, DON'T NEED IT!
	public void receiveRoom(GameRoom room) {
		// TODO Auto-generated method stub
		this.sysLogger.log(LogLevel.INFO, "Receiving room in main model." + room.toString());
		this.roomList.add(room);
		this.idSet.add(room.getUUID());
		this.mainAdpt.addRoomToList(room);
		this.sysLogger.log(LogLevel.INFO, "ADDED ROOM TO LIST: " + room.toString());
		this.sysLogger.log(LogLevel.INFO, "Our room list: " + this.roomList.toString());
	}

	/**
	 * Remove a chat room
	 * 
	 * @param uuid id of chat room
	 */
	public void removeRoom(UUID uuid) {
		// TODO Auto-generated method stub
		for (GameRoom room : this.roomList) {
			if (room.getUUID().equals(uuid)) {
				this.roomList.remove(room);
			}
		}
		this.idSet.remove(uuid);
	}

	/**
	 * Adds a chat room to the list of chat rooms.
	 * 
	 * @param room a chat room
	 */
	public void addRoomToList(GameRoom room) {
		this.mainAdpt.addRoomToList(room);
	}

	/**
	 * Gets name of user of app.
	 * 
	 * @return username string.
	 */
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	/**
	 * Invites connected remote user to a room.
	 * 
	 * @param room                 Chatroom to invite to.
	 * @param remoteConnectionDyad dyad containing stub for connection to user that
	 *                             should be invited to the chatroom.
	 */
	public void inviteToRoom(GameRoom room, INamedAppConnection remoteConnectionDyad) {
		// TODO Auto-generated method stub
		IInviteData data = IInviteData.make(room.getNamedRoomID());
		AppDataPacket<IInviteData> dp = new AppDataPacket<IInviteData>(data, localNamedConnection);

		try {
			remoteConnectionDyad.getStub().sendMessage(dp);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Broadcasts the provided message to all chat rooms.
	 * 
	 * @param msg message to be broadcasted
	 */
	public void broadcastMessage(String msg) {
		roomList.forEach(room -> room.getRoster().forEach(user -> {
			try {
				user.sendMessage(new RoomDataPacket<ITextData>(ITextData.make(msg), room.getLocalRoomConnection())); // need
																														// our
																														// own
																														// named
																														// connection
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}));
	}

	/**
	 * @return the dyad encapsulating data for a user connected to this user's app
	 */
	public INamedAppConnection getNamedAppConnection() {
		// TODO Auto-generated method stub
		return this.localNamedConnection;
	}

	/**
	 * Generates the game for all users connected to this user
	 */
	public void genGame() {
		for (GameRoom lobby : roomList) {
			if (lobby != null) {
				gameResultMap.put(lobby, true);
			}
	
			IInitRosterData rosterData = IInitRosterData.make();
			RoomDataPacket<IInitRosterData> rosterPacket = new RoomDataPacket<>(rosterData, lobby.getLocalRoomConnection());
			try {
				lobby.getLocalRoomConnection().sendMessage(rosterPacket);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
	
			for (INamedRoomConnection player : lobby.getRoster()) {
				IGameInitData data = IGameInitData.make(player);
				RoomDataPacket<IGameInitData> dp = new RoomDataPacket<>(data, lobby.getLocalRoomConnection());
				// UNCOMMENT FOR DEMO DAY, server should not play the game
				if (!player.equals(lobby.getLocalRoomConnection())) {
					ITextData textData = ITextData.make("Sent game to " + player.getName() + ".");
					RoomDataPacket<ITextData> msgDp = new RoomDataPacket<>(textData, lobby.getLocalRoomConnection());
					try {
						player.sendMessage(msgDp);
						player.sendMessage(dp);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Checks for winner of or draw game based on the size of the map of the teams.
	 * Game continues if not found.
	 */
	public void checkWinner() {
		// periodically checked
//		this.sysLogger.log(LogLevel.INFO, "Size of map, currently: " + gameResultMap.size());
		for (GameRoom team : gameResultMap.keySet()) {
			this.sysLogger.log(LogLevel.INFO, team.getName() + " is mapped to " + gameResultMap.get(team).toString());
			if (!gameResultMap.get(team)) {
				gameResultMap.remove(team);
//				this.sysLogger.log(LogLevel.INFO, "Size of map after removal: " + gameResultMap.size());
			}

		}

		if (gameResultMap.keySet().size() == 0) {
			// stop game
			// all games should be stopped at this point so nothing should be done.
			// no one wins
			this.sysLogger.log(LogLevel.INFO, "DRAW GAME HAS OCCURRED!");
			// send message that all players have been eliminated
			for (GameRoom losers : this.roomList) {
				ITextData drawMessageData = ITextData.make("THE GAME IS A DRAW. EVERYONE HAS PERISHED.");
				RoomDataPacket<ITextData> drawMsgDp = new RoomDataPacket<ITextData>(drawMessageData,
						losers.getLocalRoomConnection());

				for (INamedRoomConnection loser : losers.getRoster()) {
					try {
						loser.sendMessage(drawMsgDp);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}

		} else if (gameResultMap.keySet().size() == 1) {
			// stop game
			// we could let the team continue to try to survive and not stop the game
			// winner
			this.sysLogger.log(LogLevel.INFO, "WINNER FOUND");
			// send winning alert, maybe with survivors
			for (GameRoom room : this.roomList) {
				// Send message about winner to players in all rooms
				// At this point, only one team in the map, the winner
				ITextData winMessageData = ITextData.make(
						"Team [" + gameResultMap.keySet().iterator().next().getName() + "] HAS SURVIVED AND WON!");
				RoomDataPacket<ITextData> winMsgDp = new RoomDataPacket<ITextData>(winMessageData,
						room.getLocalRoomConnection());

				for (INamedRoomConnection member : room.getRoster()) {
					try {
						member.sendMessage(winMsgDp);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

	/**
	 * Eliminate team from map by using false value in map
	 * 
	 * @param uuid id of team to eliminate
	 */
	public void eliminateTeam(UUID uuid) {
		this.sysLogger.log(LogLevel.INFO, "Searching for team to eliminate...");
		for (GameRoom team : gameResultMap.keySet()) {
			// found specific team that needs to be eliminated
			if (team.getUUID().equals(uuid)) {
				this.sysLogger.log(LogLevel.INFO, "Elimination of " + team.getName());
				ITextData elimMsgData = ITextData.make(team.getName() + " has been eliminated.");
				// send to everyone playing that a team is eliminated
				for (GameRoom room : this.roomList) {
					RoomDataPacket<ITextData> elimMsgDp = new RoomDataPacket<ITextData>(elimMsgData,
							room.getLocalRoomConnection());
					for (INamedRoomConnection member : room.getRoster()) {
						try {
							member.sendMessage(elimMsgDp);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
				gameResultMap.put(team, false);
			}
		}
	}

	/**
	 * Request, in the form of a message, to the server for its rooms
	 * 
	 * @param server the game server
	 */
	public void requestRooms(INamedAppConnection server) {
		this.serverConnection = server;
		try {
			server.sendMessage(
					new AppDataPacket<IRequestRoomsData>(IRequestRoomsData.make(), this.localNamedConnection));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Request, in the form of a message, to the server to join one of its rooms
	 * 
	 * @param selectedRoom the room the client wishes to join
	 */
	public void joinServerRoom(INamedRoomID selectedRoom) {
		try {
			this.serverConnection.sendMessage(new AppDataPacket<IRequestJoinRoomData>(
					IRequestJoinRoomData.make(selectedRoom), this.localNamedConnection));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
