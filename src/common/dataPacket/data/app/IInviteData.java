package common.dataPacket.data.app;

import common.dataPacket.data.IAppConnectionData;
import common.serverObj.INamedRoomID;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * DataPacketData type for sending an optable chatroom invite represented by an
 * INamedRoomID.
 * 
 * The UUID of the INamedRoomID could be a "fake" UUID to prevent stale data
 * problems.
 * 
 * @author Group C
 */
public interface IInviteData extends IAppConnectionData {

	/**
	 * @return the DataPacketID (host identifier) of this data type
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IInviteData.class);
	}

	/**
	 * Delegates to the static GetID() method to retrieve the DataPacketID of this
	 * object.
	 * 
	 * @return the DataPacketID (host identifier) of this object
	 */
	@Override
	public default IDataPacketID getID() {
		return IInviteData.GetID();
	}

	/**
	 * @return the name-roomID dyad of the chatroom the receiver is being invited to
	 */
	public INamedRoomID getNamedRoomID();

	/**
	 * Factory method for constructing a concrete instance of IInviteData.
	 * 
	 * @param namedRoomID - the name-roomID dyad of the chatroom the receiver is
	 *                    being invited to
	 * @return a concrete instance of IInviteData
	 */
	public static IInviteData make(INamedRoomID namedRoomID) {
		return new IInviteData() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = -3418690903902445702L;

			@Override
			public INamedRoomID getNamedRoomID() {
				return namedRoomID;
			}

		};
	}

}
