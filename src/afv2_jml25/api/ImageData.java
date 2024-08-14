package afv2_jml25.api;

//import afv2_jml25.api.ImageDataFactory;

/**
 * Class for an image data type.
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class ImageData implements IImageData {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 5316120797807633430L;
	/**
	 * Component to display image on.
	 */
	ImageDataFactory component;

	/**
	 * Constructor for image data type.
	 * 
	 * @param c factory for creating images to display.
	 */
	public ImageData(ImageDataFactory c) {
		this.component = c;
	}

	/**
	 * 
	 */
	// private static final long serialVersionUID = 3962899359444511227L;

	@Override
	public ImageDataFactory getSupplier() {
		// TODO Auto-generated method stub
		return component;
	}

}
