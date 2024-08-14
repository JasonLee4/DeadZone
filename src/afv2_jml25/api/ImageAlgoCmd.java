package afv2_jml25.api;

import common.dataPacket.ARoomDataPacketAlgoCmd;
import common.dataPacket.RoomDataPacket;
import provided.datapacket.IDataPacketID;

/**
 * Class for a command for image data type.
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class ImageAlgoCmd extends ARoomDataPacketAlgoCmd<IImageData> {

	//	ICmd2ModelAdapter cmd2ModelAdpt;
	//	
	//	public ImageAlgoCmd(ICmd2ModelAdapter cmdAdpt) {
	//		this.cmd2ModelAdpt = cmdAdpt;
	//	}

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = -6885725788281297303L;

	/**
	 * Empty Constructor 
	 */
	public ImageAlgoCmd() {

	}

	/**
	 * Applies command to host.
	 */
	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<IImageData> host, Void... params) {
		// TODO Auto-generated method stub

		this.getCmd2ModelAdpt().displayJComponent("Image!", host.getData().getSupplier());

		//		cmd2ModelAdpt.displayJComponent("Image!", host.getData().getSupplier());
		return null;
	}

}
