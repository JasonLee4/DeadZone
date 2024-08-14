package afv2_jml25.api;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Factory for an image to display on a component.
 * 
 * @author Andres Villada
 * @author Jason Lee
 *
 */
public class ImageDataFactory implements Serializable, Supplier<JComponent> {
	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 889358638385189614L;
	/**
	 * String remote server address url.
	 */
	String remoteURL;

	/**
	 * Constructor for image factory.
	 * 
	 * @param url string path
	 */
	public ImageDataFactory(String url) {
		this.remoteURL = url;
	}

	/**
	 * 
	 */
	@Override
	public JComponent get() {
		// TODO Auto-generated method stub
		BufferedImage image = null;
		try {
			// image = ImageIO.read(new File("hlw2_jml25/chatRoom/model/mario2.jpg"));
//			URL path = new URL(this.remoteURL + "afv2_jml25/chatRoom/model/mario2.jpg");
			URL path = new URL("afv2_jml25/chatRoom/model/mario2.jpg");
			image = ImageIO.read(path);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not find image file.");
			e.printStackTrace();
		}

		// ImageComponent imageComp = new J(image);

		// JPanel imageComp = new JPanel();
		JLabel imageLabel = new JLabel(new ImageIcon(image));

		// imageComp.prepareImage(image, imageComp);

		// imageComp.paintComponent(imageComp.getGraphics());

		imageLabel.setBounds(0, 0, 100, 600);

		// return imageComp;
		return imageLabel;
	}

}
