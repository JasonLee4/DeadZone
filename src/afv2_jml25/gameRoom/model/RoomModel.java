package afv2_jml25.gameRoom.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import javax.swing.JComponent;

import afv2_jml25.api.GameRoom;
import afv2_jml25.api.GameInitAlgoCmd;
import afv2_jml25.api.IDeathData;
import afv2_jml25.api.IEliminationData;
import afv2_jml25.api.IGameBooleanData;
import afv2_jml25.api.IGameInitData;
import afv2_jml25.api.IImageData;
import afv2_jml25.api.IInitRosterData;
import afv2_jml25.api.IMapData;
import afv2_jml25.api.ImageAlgoCmd;
import afv2_jml25.api.MapAlgoCmd;
import afv2_jml25.gameApp.mainController.MyAppConfig;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.map.IMapOptions;
import provided.pubsubsync.IPubSubSyncChannelUpdate;
import provided.pubsubsync.IPubSubSyncManager;
import provided.pubsubsync.IPubSubSyncUpdater;
import provided.rmiUtils.IRMIUtils;
import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.ICmd2ModelAdapter;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.RoomDataPacketAlgo;
import common.dataPacket.data.IRoomConnectionData;
import common.dataPacket.data.room.ICmdData;
import common.dataPacket.data.room.ICmdRequestData;
import common.dataPacket.data.room.IPacketListData;
import common.dataPacket.data.room.ITextData;
import common.dataPacket.data.status.IErrorStatusData;
import common.dataPacket.data.status.IFailureStatusData;
import common.dataPacket.data.status.IRejectStatusData;
import common.serverObj.INamedAppConnection;
import common.serverObj.INamedRoomConnection;
import common.serverObj.INamedRoomID;
import common.serverObj.IRoomConnection;
//import afv2_jml25.api.*;

/**
 * Class that represents the model of the mini-MVC system.
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class RoomModel {

	/**
	 * room-level model to room-level view adapter
	 */
	IMiniModel2ViewAdapter miniModel2ViewAdpt;

	/**
	 * room-level model to app-level model adapter
	 */
	IMiniModel2MainModelAdapter miniModel2MainModelAdpt;

	/**
	 * Pubsubsync manager of app
	 */
	IPubSubSyncManager pubSubSyncManager;

	/**
	 * Datapacketid factory
	 */
	DataPacketIDFactory idFac = DataPacketIDFactory.Singleton;

	/**
	 * message receiver for chat room.
	 */
	IRoomConnection receiver;

	/**
	 * Stub of message receiver.
	 */
	IRoomConnection receiverStub;

	/**
	 * Dyad containing associated name + message receiver
	 */
	// INamedMember namedReceiver;

	INamedRoomConnection namedReceiver;

	/**
	 * Map of data packet ids to room data packet algo commands.
	 */
	HashMap<IDataPacketID, ARoomDataPacketAlgoCmd<? extends IRoomConnectionData>> cmdMap = new HashMap<>();

	/**
	 * Name of user.
	 */
	String name;

	/**
	 * Visitor algo used to process data packets.
	 */
	RoomDataPacketAlgo algo;

	/**
	 * Logger of the system
	 */
	ILogger logger = ILoggerControl.getSharedLogger();

	/**
	 * Room roster
	 */
	HashSet<INamedRoomConnection> roomRoster = new HashSet<>();

	/**
	 * UUID of room
	 */
	UUID uuid;

	/**
	 * URL String for remote message sending for images.
	 */
	String remoteURL;

	/**
	 * Room associated with this chat room MVC system
	 */
	GameRoom room;

	/**
	 * Map for status of players in room.
	 */
	ConcurrentHashMap<INamedRoomConnection, Boolean> playerStatusMap = new ConcurrentHashMap<>();

	/**
	 * 
	 */
	IMixedDataDictionary mixedDict = new MixedDataDictionary();

	/**
	 * Cache to process unknown message types
	 */
	private Map<IDataPacketID, List<RoomDataPacket<IRoomConnectionData>>> unknownMsgCache = new HashMap<>();

	/**
	 * Command to model adapter
	 */
	ICmd2ModelAdapter cmd2ModelAdpt = new ICmd2ModelAdapter() {

		@Override
		public void displayText(String text) {
			// TODO Auto-generated method stub
			miniModel2ViewAdpt.displayTextMessage(text);
		}

		@Override
		public Runnable displayJComponent(String label, Supplier<JComponent> component) {
			return miniModel2ViewAdpt.displayJComponent(label, component);
		}

		@Override
		public String getUsername() {
			return name;
		}

		@Override
		public String getRoomName() {
			return roomName;
		}

		@Override
		public <T extends IRoomConnectionData> void sendMessageToRoom(T data) {
			RoomDataPacket<T> dp = new RoomDataPacket<T>(data, namedReceiver);

			for (INamedRoomConnection user : roomRoster) {
				try {
					user.getStub().sendMessage(dp);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		}

		@Override
		public <T extends IRoomConnectionData> void sendMessageToDyad(T data, INamedRoomConnection dyad) {
			RoomDataPacket<T> dp = new RoomDataPacket<T>(data, namedReceiver);

			try {
				dyad.getStub().sendMessage(dp);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}

		@Override
		public <T> T putLocalData(MixedDataKey<T> key, T value) {
			return mixedDict.put(key, value);
		}

		@Override
		public <T> T getLocalData(MixedDataKey<T> key) {
			return mixedDict.get(key);
		}

		@Override
		public Set<MixedDataKey<?>> getLocalDataKeys(UUID id) {
			return mixedDict.getKeys(id);
		}

		@Override
		public String getAPIKey() {
			return "AIzaSyApkLYsPwlSJfEKhE1PJrSjm200Of1go4A";
		}

		@Override
		public ILogger getLocalLogger() {
			return logger;
		}

	};

	/**
	 * Name of the room
	 */
	String roomName;

	/**
	 * Pubsubsync channel update
	 */
	IPubSubSyncChannelUpdate<HashSet<INamedRoomConnection>> channelUpdate;

	/**
	 * Dyad encapsulating info for an instance of app to app connection
	 */
	INamedAppConnection serverConnection;

	/**
	 * ID of this room
	 */
	INamedRoomID roomNamedID;

	/**
	 * @param miniAdpt      room level model to room view adapter
	 * @param mainAdpt      room level model to app level model adapter
	 * @param roomName      name of chat room
	 * @param appConnection dyad for app to app connection data
	 */
	// @SuppressWarnings("unchecked")
	public RoomModel(IMiniModel2ViewAdapter miniAdpt, IMiniModel2MainModelAdapter mainAdpt, String roomName,
			INamedAppConnection appConnection) {
		this.miniModel2ViewAdpt = miniAdpt;
		// this.install();
		this.miniModel2MainModelAdpt = mainAdpt;
		this.pubSubSyncManager = this.miniModel2MainModelAdpt.getPubSubManager();

		this.roomName = roomName;
		this.name = mainAdpt.getUserName();

		this.serverConnection = appConnection;
	}

	/**
	 * Starts the chatroom-level model.
	 */
	public void start() {
		IRMIUtils rmiUtils = this.miniModel2MainModelAdpt.getRMIUtils();

		this.remoteURL = rmiUtils.getClassFileServerURL();
		this.logger.log(LogLevel.INFO, remoteURL);

		// Acquire the pubsub manager
		MyAppConfig appConfig = this.miniModel2MainModelAdpt.getAppConfig();

		this.receiver = new IRoomConnection() {

			@Override
			public void sendMessage(RoomDataPacket<? extends IRoomConnectionData> data) throws RemoteException {
				// TODO Auto-generated method stub
				data.execute(algo);
			}

		};

		try {
			this.receiverStub = (IRoomConnection) UnicastRemoteObject.exportObject(this.receiver, appConfig.stubPort);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.namedReceiver = INamedRoomConnection.make(name, receiverStub, serverConnection);

		this.channelUpdate = this.pubSubSyncManager.createChannel("New Channel!", this.roomRoster, (pubSubSyncData) -> {

			this.roomRoster.clear();

			logger.log(LogLevel.INFO, "PubSubSyncData: " + pubSubSyncData.getData().toString());

			for (INamedRoomConnection receiver : pubSubSyncData.getData()) {
				this.roomRoster.add(receiver);
			}
			logger.log(LogLevel.INFO, "This is the room roster: " + roomRoster.toString());

		}, (statusMsg) -> {
			this.logger.log(LogLevel.INFO, "Room Roster updated: " + statusMsg);
		});

		this.uuid = this.channelUpdate.getChannelID();

		// initialize algo MS2

		algoSetUp();
		this.channelUpdate.update(IPubSubSyncUpdater.makeSetAddFn(this.namedReceiver));

		this.roomNamedID = INamedRoomID.make(this.roomName, this.uuid);

		room = new GameRoom(this.roomNamedID);
		room.setRoster(roomRoster);
		room.setLocalRoomConnection(namedReceiver);
		logger.log(LogLevel.INFO, "New chat room in room model before sending to main model: " + room.toString());

		this.miniModel2MainModelAdpt.sendRoom(room);
	}

	/**
	 * Sets up algorithms for known messages
	 */
	public void algoSetUp() {
		// initialize algo MS2

		this.algo = new RoomDataPacketAlgo(new ARoomDataPacketAlgoCmd<IRoomConnectionData>() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = -5608814799093188882L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRoomConnectionData> host, Void... params) {
				if (!unknownMsgCache.containsKey(index)) {
					unknownMsgCache.put(index, new ArrayList<>(Arrays.asList(host)));
				} else {
					// unknownMsgCache.get(index) is null
					unknownMsgCache.get(index).add(host);
					unknownMsgCache.put(index, unknownMsgCache.get(index));
				}
				try {
					logger.log(LogLevel.INFO, "Sending cmd request to " + host.getSender().getName());
					host.getSender().sendMessage(new RoomDataPacket<ICmdRequestData>(
							ICmdRequestData.make(host.getData().getID()), namedReceiver));
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

		});

		// text command
		IDataPacketID textID = ITextData.GetID();
		ARoomDataPacketAlgoCmd<ITextData> textCmd = new ARoomDataPacketAlgoCmd<ITextData>() {

			private static final long serialVersionUID = -2413320790005537647L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<ITextData> host, Void... params) {

				cmd2ModelAdpt.displayText(host.getSender().getName() + ": " + host.getData().getText());
				logger.log(LogLevel.INFO, host.getData().getText());
				return null;
			}
		};
		this.algo.setCmd(textID, textCmd);

		// command for commands
		IDataPacketID cmdID = ICmdData.GetID();
		ARoomDataPacketAlgoCmd<ICmdData<IRoomConnectionData>> cmdCmd = new ARoomDataPacketAlgoCmd<ICmdData<IRoomConnectionData>>() {
			private static final long serialVersionUID = 312225302837098905L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<ICmdData<IRoomConnectionData>> host, Void... params) {
				IDataPacketID newID = host.getData().getUnknownMsgID();
				ARoomDataPacketAlgoCmd<IRoomConnectionData> cmdToInstall = host.getData().getAlgoCmd();

				// set command first
				cmdToInstall.setCmd2ModelAdpt(cmd2ModelAdpt);
				algo.setCmd(newID, cmdToInstall);

				// remove first message in the cache and run it
				logger.log(LogLevel.INFO, host.getData().getUnknownMsgID().toString());
				List<RoomDataPacket<IRoomConnectionData>> cache = unknownMsgCache.get(host.getData().getUnknownMsgID());
				cache.remove(0).execute(algo, params);
				unknownMsgCache.put(host.getData().getUnknownMsgID(), cache); //

				return null;
			}
		};
		this.algo.setCmd(cmdID, cmdCmd);
		this.cmdMap.put(cmdID, cmdCmd);

		// command for command requests
		IDataPacketID cmdReqID = ICmdRequestData.GetID();
		ARoomDataPacketAlgoCmd<ICmdRequestData> cmdReqCmd = new ARoomDataPacketAlgoCmd<ICmdRequestData>() {
			private static final long serialVersionUID = -1336422669790334676L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<ICmdRequestData> host, Void... params) {

				ARoomDataPacketAlgoCmd unknownMsgCmd = (ARoomDataPacketAlgoCmd<? extends IRoomConnectionData>) cmdMap
						.get(host.getData().getUnknownMsgID());
				ICmdData<IRoomConnectionData> cmdData = ICmdData.make(host.getData().getUnknownMsgID(), unknownMsgCmd);

				RoomDataPacket<ICmdData<IRoomConnectionData>> dp = new RoomDataPacket<ICmdData<IRoomConnectionData>>(
						cmdData, namedReceiver);

				try {
					ARoomDataPacketAlgoCmd<?> cmd = (ARoomDataPacketAlgoCmd<?>) algo
							.getCmd(host.getData().getUnknownMsgID());

					if (cmd.equals(null)) {
						sendRejectStatusData(host);
					}
					host.getSender().sendMessage(new RoomDataPacket<ICmdData<?>>(
							ICmdData.make(host.getData().getUnknownMsgID(), cmd), namedReceiver));
				} catch (RemoteException e) {
					sendErrorStatusData(host);
					e.printStackTrace();
				}
				return null;
			}
		};
		this.algo.setCmd(cmdReqID, cmdReqCmd);
		this.cmdMap.put(cmdReqID, cmdReqCmd);

		// Error status
		this.algo.setCmd(IErrorStatusData.GetID(), new ARoomDataPacketAlgoCmd<IErrorStatusData<?>>() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = 2851511816960260552L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IErrorStatusData<?>> host, Void... params) {
				logger.log(LogLevel.ERROR, "Error Status message: " + host.getData().getStatusMsg());
				return null;
			}

		});

		// Reject status
		this.algo.setCmd(IRejectStatusData.GetID(), new ARoomDataPacketAlgoCmd<IRejectStatusData<?>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5621298939574620126L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRejectStatusData<?>> host, Void... params) {
				logger.log(LogLevel.ERROR, "Reject Status message: " + host.getData().getStatusMsg());
				return null;
			}

		});

		// Failure status
		this.algo.setCmd(IFailureStatusData.GetID(), new ARoomDataPacketAlgoCmd<IFailureStatusData<?>>() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = -4074661367286508558L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IFailureStatusData<?>> host, Void... params) {
				logger.log(LogLevel.ERROR, "Failure Status message: " + host.getData().getStatusMsg());
				return null;
			}

		});

		// image command
		IDataPacketID imageID = IImageData.GetID();
		ARoomDataPacketAlgoCmd<IImageData> imageCmd = new ImageAlgoCmd();
		imageCmd.setCmd2ModelAdpt(cmd2ModelAdpt);

		this.algo.setCmd(imageID, imageCmd);
		this.cmdMap.put(imageID, imageCmd);

		// Game command
		IDataPacketID gameID = IGameInitData.GetID();
		ARoomDataPacketAlgoCmd<IGameInitData> gameCmd = new GameInitAlgoCmd(this.uuid, this.namedReceiver);
		gameCmd.setCmd2ModelAdpt(cmd2ModelAdpt);
		this.algo.setCmd(gameID, gameCmd);
		this.cmdMap.put(gameID, gameCmd);

		// our map command
		IDataPacketID mapID = IMapData.GetID();
		ARoomDataPacketAlgoCmd<IMapData> mapCmd = new MapAlgoCmd();
		mapCmd.setCmd2ModelAdpt(cmd2ModelAdpt);
		this.algo.setCmd(mapID, mapCmd);
		this.cmdMap.put(mapID, mapCmd);

		// command for a boolean value to check state of game
		IDataPacketID gameBoolID = IGameBooleanData.GetID();
		ARoomDataPacketAlgoCmd<IGameBooleanData> gameBoolCmd = new ARoomDataPacketAlgoCmd<IGameBooleanData>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -3807955103961622917L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IGameBooleanData> host, Void... params) {
				// We could try to stop all games if a winner or draw is reached.
				miniModel2MainModelAdpt.checkWinner();
				return null;
			}

		};
		gameBoolCmd.setCmd2ModelAdpt(cmd2ModelAdpt);
		this.algo.setCmd(gameBoolID, gameBoolCmd);
		this.cmdMap.put(gameBoolID, gameBoolCmd);

		// command for team elimination
		IDataPacketID elimID = IEliminationData.GetID();
		ARoomDataPacketAlgoCmd<IEliminationData> elimCmd = new ARoomDataPacketAlgoCmd<IEliminationData>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -3082947353721238180L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IEliminationData> host, Void... params) {
				logger.log(LogLevel.INFO,
						"Checking to eliminate the team with UUID: " + host.getData().getEliminationUUID().toString());
				checkTeamSurvival();
				return null;
			}

		};
		elimCmd.setCmd2ModelAdpt(cmd2ModelAdpt);
		this.algo.setCmd(elimID, elimCmd);
		this.cmdMap.put(elimID, elimCmd);

		// command for player death
		IDataPacketID deathID = IDeathData.GetID();
		ARoomDataPacketAlgoCmd<IDeathData> deathCmd = new ARoomDataPacketAlgoCmd<IDeathData>() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = -8410079871904145065L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IDeathData> host, Void... params) {
				// set player status to false or dead
				logger.log(LogLevel.INFO, host.getData().getPlayer().getName() + " has perished.");
				// death message sent to room
				sendMessage(host.getData().getPlayer().getName() + " has perished.");
				playerStatusMap.put(host.getData().getPlayer(), false);
				return null;
			}

		};
		deathCmd.setCmd2ModelAdpt(cmd2ModelAdpt);
		this.algo.setCmd(deathID, deathCmd);
		this.cmdMap.put(deathID, deathCmd);

		// command for initializing roster of players statuses in room model
		// teamstatusmap
		IDataPacketID rosterID = IInitRosterData.GetID();
		ARoomDataPacketAlgoCmd<IInitRosterData> rosterCmd = new ARoomDataPacketAlgoCmd<IInitRosterData>() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = 6144550849415841425L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IInitRosterData> host, Void... params) {
				initPlayerStatusMap();
				return null;
			}

		};
		rosterCmd.setCmd2ModelAdpt(cmd2ModelAdpt);
		this.algo.setCmd(rosterID, rosterCmd);
		this.cmdMap.put(rosterID, rosterCmd);

		IDataPacketID packetListID = IPacketListData.GetID();
		// messageVisitor is the RoomDataPacketAlgo
		ARoomDataPacketAlgoCmd<IPacketListData> packetListCmd = new ARoomDataPacketAlgoCmd<IPacketListData>() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = 6266389224642420064L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IPacketListData> host, Void... params) {

				// The data packets sent to us in the IPacketListData
				IPacketListData data = (IPacketListData) host.getData();

				// Go through each data packet in order and execute it.
				for (RoomDataPacket<IRoomConnectionData> packet : data.getPackets()) {
					packet.execute(algo);
				}
				return null;
			}
		};

		this.algo.setCmd(packetListID, packetListCmd);
		this.cmdMap.put(packetListID, packetListCmd);
	}

	/**
	 * Status message sent when the message was ok, but the receiver refused to
	 * process it
	 * 
	 * @param <T>  RoomDataPacket type that extends IRoomConnectionData
	 * @param host data packet sent including receiver information
	 */
	private <T extends IRoomConnectionData> void sendRejectStatusData(RoomDataPacket<T> host) {
		IRejectStatusData<T> error = IRejectStatusData.make(host,
				namedReceiver.getName() + " refused to process message of type " + host.getData().getID());
		try {
			host.getSender().sendMessage(new RoomDataPacket<IRejectStatusData<?>>(error, namedReceiver));
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Status message sent when the message was ok, but the requested operation
	 * failed.
	 * 
	 * @param <T>  RoomDataPacket type that extends IRoomConnectionData
	 * @param host data packet sent including receiver information
	 */
	@SuppressWarnings("unused")
	private <T extends IRoomConnectionData> void sendFailureStatusData(RoomDataPacket<T> host) {
		IFailureStatusData<T> error = IFailureStatusData.make(host,
				namedReceiver.getName() + " failed requested operation of message of type " + host.getData().getID());
		try {
			host.getSender().sendMessage(new RoomDataPacket<IFailureStatusData<?>>(error, namedReceiver));
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Status message sent when there was an error receiving a message, or there was
	 * something wrong with the message.
	 * 
	 * @param <T>  RoomDataPacket type that extends IRoomConnectionData
	 * @param host data packet sent including receiver information
	 */
	private <T extends IRoomConnectionData> void sendErrorStatusData(RoomDataPacket<T> host) {
		IErrorStatusData<T> error = IErrorStatusData.make(host,
				namedReceiver.getName() + " failed to recieve message of type " + host.getData().getID() + " from "
						+ host.getSender().getName());
		try {
			host.getSender().sendMessage(new RoomDataPacket<IErrorStatusData<?>>(error, namedReceiver));
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Quit out of chat room. remote room from list of chat rooms in app and updates
	 * the channel, unsubscribes from the channel too.
	 */
	public void quit() {

		miniModel2MainModelAdpt.removeRoom(this.uuid);
		// this.channelUpdate.update(IPubSubSyncUpdater.makeRemoteSetRemoveFn(this.memberStub));
		this.channelUpdate.update(IPubSubSyncUpdater.makeSetAddFn(this.namedReceiver));
		this.channelUpdate.unsubscribe();
	}

	/**
	 * Method for joining a room.
	 * 
	 * @param remoteID id of chat room
	 */
	public void joinRoom(UUID remoteID) {

		IRMIUtils rmiUtils = this.miniModel2MainModelAdpt.getRMIUtils();
		this.remoteURL = rmiUtils.getClassFileServerURL();
		MyAppConfig appConfig = this.miniModel2MainModelAdpt.getAppConfig();

		this.receiver = new IRoomConnection() {

			@Override
			public void sendMessage(RoomDataPacket<? extends IRoomConnectionData> data) throws RemoteException {
				data.execute(algo);
			}

		};

		try {
			this.receiverStub = (IRoomConnection) UnicastRemoteObject.exportObject(this.receiver, appConfig.stubPort);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// this.namedReceiver = new NamedReceiver(this.name, this.receiverStub);
		this.namedReceiver = INamedRoomConnection.make(name, receiverStub, serverConnection);

		channelUpdate = this.pubSubSyncManager.subscribeToUpdateChannel(remoteID, (pubSubSyncData) -> {
			this.roomRoster.clear();

			for (INamedRoomConnection receiver : pubSubSyncData.getData()) {

				logger.log(LogLevel.INFO, receiver.toString());

				this.roomRoster.add(receiver);
			}
		}, (statusMsg) -> {
			this.logger.log(LogLevel.INFO, "Room Roster updated: " + statusMsg);
		});

		this.uuid = this.channelUpdate.getChannelID();
		// MS2 additional room level algo
		algoSetUp();

		this.channelUpdate.update(IPubSubSyncUpdater.makeSetAddFn(this.namedReceiver));

		this.roomNamedID = INamedRoomID.make(this.roomName, this.uuid);
		room = new GameRoom(roomNamedID);
		room.setRoster(roomRoster);
		room.setLocalRoomConnection(namedReceiver);
	}

	/**
	 * Displays text on room-level GUI
	 * 
	 * @param text message to display on chat room GUi
	 */
	public void displayMessage(String text) {
		this.miniModel2ViewAdpt.displayTextMessage(text);
	}

	/**
	 * Sends message to every member in roster.
	 * 
	 * @param text message to be sent
	 */
	public void sendMessage(String text) {

		for (INamedRoomConnection dyad : this.roomRoster) {

			try {

				RoomDataPacket<ITextData> dp = new RoomDataPacket<ITextData>(ITextData.make(text), this.namedReceiver);
				logger.log(LogLevel.INFO, "Sent message to Member: " + dyad.toString());

				dyad.getStub().sendMessage(dp);

			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * @return the roster for this chat room
	 */
	public HashSet<INamedRoomConnection> getRoster() {
		return this.roomRoster;
	}

	/**
	 * Sends map to all players of the game
	 */
	public void sendMap() {
		for (INamedRoomConnection dyad : this.roomRoster) {

			try {

				RoomDataPacket<IMapData> dp = new RoomDataPacket<IMapData>(IMapData.make(IMapOptions.makeDefault()),
						this.namedReceiver);
				logger.log(LogLevel.INFO, "Sent map to Member: " + dyad.toString());
				// member.getNamedMember().getStub().receiveMessage(dp);

				dyad.getStub().sendMessage(dp);

			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Initialize map of player connections to boolean status.
	 */
	public void initPlayerStatusMap() {
		for (INamedRoomConnection player : roomRoster) {
			//this.logger.log(LogLevel.INFO, "Initializing player: " + player.getName());
			// UNCOMMENT FOR DEMO DAY
			if (player.equals(this.namedReceiver)) {
				this.logger.log(LogLevel.INFO, "Initializing player: " + player.getName());

				playerStatusMap.put(player, false);
			} else {
			playerStatusMap.put(player, true);
			}

		}
	}

	/**
	 * Checks if team of this room is still alive.
	 */
	public void checkTeamSurvival() {
		boolean teamEliminated = false;
		// Iterate through the roster to check if all of the team is dead.
		for (INamedRoomConnection player : playerStatusMap.keySet()) {
			if (playerStatusMap.get(player)) {
				teamEliminated = true;
			}
		}
		if (!teamEliminated) {
			// set team status to eliminated in app
			this.logger.log(LogLevel.INFO, "Team \'" + roomName + "\' should be eliminated.");
			this.miniModel2MainModelAdpt.eliminateTeam(this.uuid);
		}
	}

}
