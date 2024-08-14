package common.dataPacket.data.room;

import java.util.ArrayList;
import common.dataPacket.RoomDataPacket;
import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Contains a list of data packets that must be processed by the visitor in order.<br><br>
 * 
 * Note: The packets must be stored in an ArrayList, as Lists are not serializable.
 * 
 * @author Phoebe Scaccia
 */
public interface IPacketListData extends IRoomConnectionData {

 /**
  * @return the DataPacketID (host identifier) of this data type
  */
 public static IDataPacketID GetID() {
  return DataPacketIDFactory.Singleton.makeID(IPacketListData.class);
 }
 
 /**
  * Delegates to the static GetID() method to retrieve the DataPacketID of this object.
  * @return the DataPacketID (host identifier) of this object
  */
 @Override
 public default IDataPacketID getID() {
  return IPacketListData.GetID();
 }
 
 /**
  * @return the list of data packets
  */
 public ArrayList<RoomDataPacket<IRoomConnectionData>> getPackets();
 
 /**
  * Makes a concrete IPacketListData.
  *
  * @param packets a list of data packets to process in order
  * @return a concrete IPacketListData
  */
 public static IPacketListData make(ArrayList<RoomDataPacket<IRoomConnectionData>> packets) {
	 return new IPacketListData() {
		 /**
		 * Serialize the IPacketListData
		 */
		private static final long serialVersionUID = 4266231950095323001L;

		@Override
		 public ArrayList<RoomDataPacket<IRoomConnectionData>> getPackets() {
    
			 return packets;
		 }
	 };
 }
 
 
}
