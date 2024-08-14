package common.dataPacket.data.app;

import common.dataPacket.data.IAppConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * DataPacketData type for notifying the receiver that the sender is quitting ChatApp
 * and hence needs to be removed from the receiver's set of connected hosts.
 * @author Group G
 *
 */
public interface IQuitData extends IAppConnectionData {
	
	/**
	 * @return the DataPacketID (host identifier) of this data type
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IQuitData.class);
	}
	
	/**
	 * Delegates to the static GetID() method to retrieve the DataPacketID of this object.
	 * @return the DataPacketID (host identifier) of this object
	 */
	@Override
	public default IDataPacketID getID() {
		return IQuitData.GetID();
	}
	
	/**
	 * Factory method that constructs a concrete IQuitData object.
	 * @return concrete IQuitData object
	 */
	public static IQuitData make() {
		return new IQuitData() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = 3652116229781520401L;
			
		};
	}

}
