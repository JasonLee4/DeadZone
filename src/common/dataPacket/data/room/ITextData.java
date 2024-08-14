package common.dataPacket.data.room;

import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * DataPacketData type for sending a simple text message.
 * @author Group G
 *
 */
public interface ITextData extends IRoomConnectionData {
	
	/**
	 * @return the DataPacketID (host identifier) of this data type
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(ITextData.class);
	}
	
	/**
	 * Delegates to the static GetID() method to retrieve the DataPacketID of this object.
	 * @return the DataPacketID (host identifier) of this object
	 */
	@Override
	public default IDataPacketID getID() {
		return ITextData.GetID();
	}
	
	/**
	 * @return text data contained in this data object
	 */
	public String getText();
	
	/**
	 * Factory method that constructs a concrete ITextData object.
	 * @param text text message
	 * @return concrete ITextData object
	 */
	public static ITextData make(String text) {
		return new ITextData() {

			private static final long serialVersionUID = -826931305519937488L;

			@Override
			public String getText() {
				return text;
			}
			
		};
	}

}
