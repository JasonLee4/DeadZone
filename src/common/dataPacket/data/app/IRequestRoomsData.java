package common.dataPacket.data.app;

import common.dataPacket.data.IAppConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * DataPacketData type representing the ability for one ChatApp to request a
 * list of INamedRoomIDs from another ChatApp.
 * 
 * @author Group C
 */
public interface IRequestRoomsData extends IAppConnectionData {

	/**
	 * This method allows one to get the ID value directly from the interface.
	 * 
	 * @return The ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRequestRoomsData.class);
	}

	/**
	 * Allows concrete implementation to automatically generate their ID value
	 * 
	 * @return The ID value associated with this data type.
	 */
	@Override
	public default IDataPacketID getID() {
		return IRequestRoomsData.GetID();
	}

	/**
	 * Factory method to create a concrete instance of IRequestRoomsData.
	 * 
	 * @return a concrete instance of IRequestRoomsData
	 */
	public static IRequestRoomsData make() {
		return new IRequestRoomsData() {

			private static final long serialVersionUID = 5613435918144815189L;

		};
	}

}
