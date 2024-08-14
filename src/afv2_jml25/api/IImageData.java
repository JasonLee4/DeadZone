package afv2_jml25.api;

import common.dataPacket.data.IRoomConnectionData;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Interface for image data type.
 * 
 * @author Andres Villada
 * @author Jason Lee
 *
 */
public interface IImageData extends IRoomConnectionData {

	/**
	 * @return the DataPacketID (host identifier) of this data type
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IImageData.class);
	}

	/**
	 * Delegates to the static GetID() method to retrieve the DataPacketID of this
	 * object.
	 * 
	 * @return the DataPacketID (host identifier) of this object
	 */
	@Override
	public default IDataPacketID getID() {
		return IImageData.GetID();
	}

	/**
	 * @return text data contained in this data object
	 */
	// public String getText();

	public ImageDataFactory getSupplier();

	/**
	 * Makes an instant of image data.
	 * 
	 * @param component factory for image data
	 * @return new image data instance.
	 */
	public static IImageData make(ImageDataFactory component) {
		// return new IImageData() {

		return new IImageData() {

			/**
			 * For serialization
			 */
			private static final long serialVersionUID = 3962899359444511227L;

			@Override
			public ImageDataFactory getSupplier() {
				// TODO Auto-generated method stub
				return component;
			}

		};

		// private static final long serialVersionUID = -826931305519937488L;

		// };
	}

}
