package afv2_jml25.api;

import common.dataPacket.AAppDataPacketAlgoCmd;
import common.dataPacket.AppDataPacket;
import common.dataPacket.data.IAppConnectionData;
import common.serverObj.INamedAppConnection;
import provided.datapacket.IDataPacketID;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;

/**
 * Concrete class for app-level algo command.
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class AppDataPacketAlgoCmd extends AAppDataPacketAlgoCmd<IAppConnectionData> {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 2529027638815086263L;

	/**
	 * Dyad for app-level connection.
	 */
	INamedAppConnection namedConnection;

	/**
	 * Logger
	 */
	ILogger sysLogger = ILoggerControl.getSharedLogger();

	/**
	 * Constructor for this app-level command class.
	 * 
	 * @param connection dyad for app level commands.
	 */
	public AppDataPacketAlgoCmd(INamedAppConnection connection) {
		this.namedConnection = connection;
	}

	/**
	 * Applies command to host.
	 */
	@Override
	public Void apply(IDataPacketID index, AppDataPacket<IAppConnectionData> host, Void... params) {
		// TODO Auto-generated method stub

		sysLogger.log(LogLevel.INFO, "Received unknown message type at app level");

		return null;
	}

	// INamedAppConnection namedConnection;
	//
	// ILogger sysLogger = ILoggerControl.getSharedLogger();
	//
	// public AppDataPacketAlgoCmd(INamedAppConnection namedConnection) {
	// this.namedConnection = namedConnection;
	// }
	//
	// @Override
	// public Void apply(IDataPacketID index, AppDataPacket host, Void... params) {
	// // TODO Need to implment the default case
	// sysLogger.log(LogLevel.INFO, "Received an unknown message type at the app
	// level.");
	// return null;
	// }

}
