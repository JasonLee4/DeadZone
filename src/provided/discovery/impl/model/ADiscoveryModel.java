package provided.discovery.impl.model;

import java.rmi.Remote;

import provided.logger.ILogger;
import provided.logger.LogLevel;
import provided.rmiUtils.IRMIUtils;

/**
 * Abstract Discovery Model that provides common services to its implementations
 * @author swong
 *
 * @param <TRemoteStub> The type of stub to which the endpoints are referencing
 */
public abstract class ADiscoveryModel<TRemoteStub extends Remote> {

	/**
	 * The IRMIUtils to use
	 */
	protected IRMIUtils rmiUtils;

	/**
	 * The DiscoveryConnector to use
	 */
	protected DiscoveryConnector discConn;
	
	/**
	 * The stub factory to use
	 */
	protected RemoteAPIStubFactory<TRemoteStub> remAPIStubFac;

	/**
	 * The logger for this class and subclasses to use
	 */
	protected ILogger logger;
	
	/**
	 * Constructor for the class
	 * @param logger The logger to use
	 */
	public ADiscoveryModel(ILogger logger) {
		setLogger(logger);
	}
	
	
	/**
	 * Sets the logger in use to the given logger
	 * @param logger The new logger to use
	 */
	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	/**
	 * Internal service to perform some of the common processes to start the discovery model
	 * @param rmiUtils The ALREADY STARTED IRMIUtils to use
	 */
	protected void startInit(IRMIUtils rmiUtils) {
		this.rmiUtils = rmiUtils;
		this.remAPIStubFac = new RemoteAPIStubFactory<TRemoteStub>(rmiUtils, logger);
	}

	/**
	 * Disconnect from the discovery server
	 */
	public void disconnectFromDiscoveryServer() {
		logger.log(LogLevel.INFO, "Disconnecting from the discovery server.");
		discConn.disconnect();
	}

	/**
	 * Stop this discovery model
	 */
	public void stop() {
		logger.log(LogLevel.INFO, "Stopping...");
		this.disconnectFromDiscoveryServer();
	}

}