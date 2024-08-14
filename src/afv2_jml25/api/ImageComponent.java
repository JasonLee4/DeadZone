package afv2_jml25.api;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * Component containing an image.
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class ImageComponent extends JComponent {
	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 3324661835585015074L;
	/**
	 * Image to display
	 */
	BufferedImage image;

	/**
	 * Constructor for a component with an image painted on it.
	 * @param i An Image
	 */
	public ImageComponent(BufferedImage i) {
		this.image = i;
	}

	/**
	 * Paint image onto component
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
	}

}
