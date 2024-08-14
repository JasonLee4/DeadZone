package common.dataPacket.data.app;
import java.util.HashSet;

import common.dataPacket.data.IAppConnectionData;
import common.serverObj.INamedAppConnection;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * DataPacketData type for sending one's set of connected hosts.
 * @author Group G
 *
 */
public interface IConnectionSetData extends IAppConnectionData {
	
	/**
	 * @return the DataPacketID (host identifier) of this data type
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IConnectionSetData.class);
	}
	
	/**
	 * Delegates to the static GetID() method to retrieve the DataPacketID of this object.
	 * @return the DataPacketID (host identifier) of this object
	 */
	@Override
	public default IDataPacketID getID() {
		return IConnectionSetData.GetID();
	}
	
	/**
	 * @return the set of connected hosts contained in this data object
	 */
	public HashSet<INamedAppConnection> getConnectionSet();
	
	/**
	 * Factory method for constructing a concrete IConnectionSetData object.
	 * @param connectionSet set of connected hosts contained in the constructed data object
	 * @return concrete IConnectionSetData object
	 */
	public static IConnectionSetData make(HashSet<INamedAppConnection> connectionSet) {
		return new IConnectionSetData() {

			private static final long serialVersionUID = -2343991137697473801L;
			
			@Override
			public HashSet<INamedAppConnection> getConnectionSet() {
				return connectionSet;
			}
			
		};
	}
}
