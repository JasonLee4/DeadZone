package provided.owlMaps.general;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * The representation of an icon shown on the map.
 * @author swong
 *
 */
public interface IIcon extends Serializable {
	
	/**
	 * The position at which to anchor an image in correspondence to the location of the marker on the map. 
	 * By default, the anchor is located along the center point of the bottom of the image.
	 * @return The anchor location
	 */
	public Point2D getAnchor();
	
	/**
	 * The origin of the label relative to the top-left corner of the icon image, if a label is supplied by the marker. 
	 * By default, the origin is located in the center point of the image.
	 * @return The label origin
	 */
	public Point2D getLabelOrigin();
	
	/**
	 * The position of the image within a sprite, if any. 
	 * By default, the origin is located at the top left corner of the image (0, 0).
	 * @return The origin of the image
	 */
	public Point2D getOrigin();
	
	/**
	 * The size of the entire image after scaling, if any. Use this property to stretch/shrink an image or a sprite.
	 * @return The scaled size of the image
	 */
	public Dimension getScaledSize();
	
	/**
	 * The display size of the sprite or image. When using sprites, you must specify the sprite size. 
	 * If the size is not provided, it will be set when the image loads.
	 * @return  the size of the entity
	 */
	public Dimension getSize();
	
	/**
	 * The URL of the image or sprite sheet.
	 * @return  The URL the source of the image or sprite.
	 */
	public String getURL();
}
