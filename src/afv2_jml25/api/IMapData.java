package afv2_jml25.api;

import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;
import provided.mixedData.IMixedDataDictionary;

/**
 * A map instance
 * 
 * @author Andres Villada
 * @author Jason Lee
 */
public interface IMapData extends IRoomConnectionData {

	/**
	 * @return ID of the data packet for map
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IMapData.class);
	}

	@Override
	public default IDataPacketID getID() {
		return IMapData.GetID();
	}

	/**
	 * @param dict this map's configurations
	 * @return data for game initialization
	 */
	public static IMapData make(IMixedDataDictionary dict) {

		return new IMapData() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = 2596642091366501845L;

			@Override
			public IMixedDataDictionary getMapOptions() {
				return dict;
			}

		};

	}

	/**
	 * @return this map's configurations 
	 */
	public IMixedDataDictionary getMapOptions();
}
