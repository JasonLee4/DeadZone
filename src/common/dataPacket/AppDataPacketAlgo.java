package common.dataPacket;

import common.dataPacket.data.IAppConnectionData;
import provided.datapacket.DataPacketAlgo;

/**
 * Type-narrowed extension of DataPacketAlgo for visitor algorithms at the app level.
 * @author Group G
 *
 */
public class AppDataPacketAlgo extends DataPacketAlgo<Void, Void> {

	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = -1264985781072826904L;

	/**
	 * Constructs an AppDataPacketAlgo object. Delegates to the superclass constructor.
	 * @param defaultCmd default command of this visitor algorithm
	 */
	public AppDataPacketAlgo(AAppDataPacketAlgoCmd<? extends IAppConnectionData> defaultCmd) {
		super(defaultCmd);
	}

}
