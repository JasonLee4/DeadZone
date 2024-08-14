package common.serverObj;

import java.io.Serializable;
import java.rmi.RemoteException;
import common.dataPacket.AppDataPacket;
import common.dataPacket.data.IAppConnectionData;

/**
 * Dyad encapsulating an IAppConnection stub and a friendly name for displaying on the GUI.
 * @author Group G
 *
 */
public interface INamedAppConnection extends Serializable {

	/**
	 * @return the friendly name of this dyad
	 */
	public String getName();
	
	/**
	 * @return the IAppConnection stub of this dyad
	 */
	public IAppConnection getStub();
	
	/**
	 * Calls the IAppConnection Stub to send the corresponding message. 
	 * Sends a app-level message to be received by the owner of this stub's corresponding server object.
	 * @param data data packet to send
	 * @throws RemoteException for communication-related errors
	 */
	public default void sendMessage(AppDataPacket<? extends IAppConnectionData> data) throws RemoteException {
		this.getStub().sendMessage(data);
	}
	
	/**
	 * Factory method for constructing a concrete INamedAppConnection object.
	 * @param name friendly name of the constructed dyad
	 * @param stub IAppConnection stub of the constructed dyad
	 * @return concrete INamedAppConnection object
	 */
	public static INamedAppConnection make(String name, IAppConnection stub) {
		return new INamedAppConnection() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = 7322289565060883522L;

			@Override
			public String getName() {
				return name;
			}

			@Override
			public IAppConnection getStub() {
				return stub;
			}
			
			@Override
			public String toString() {
				return this.getName();
			}
			
			@Override
			public boolean equals(Object obj) {
				if (!(obj instanceof INamedAppConnection o)) {
					return false;
				}
				return this.getStub().equals(o.getStub());
			}
			
			@Override
			public int hashCode() {
				return this.getStub().hashCode();
			}
			
		};
	}

}
