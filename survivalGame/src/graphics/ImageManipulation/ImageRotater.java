package graphics.ImageManipulation;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class ImageRotater {

	/**
	 * rotates Clockwise 
	 * @param bufferedImage input
	 * @param angle to rotate clockwise
	 * @return A rotated buffered image
	 */
	public static BufferedImage rotateImage(BufferedImage image, int angle) {
	    double rads = Math.toRadians(angle);
	    double sin = Math.abs(Math.sin(rads));
	    double cos = Math.abs(Math.cos(rads));

	    int width = image.getWidth();
	    int height = image.getHeight();

	    // calculate new image size to avoid clipping
	    int newWidth = (int) Math.floor(width * cos + height * sin);
	    int newHeight = (int) Math.floor(height * cos + width * sin);

	    BufferedImage rotated = new BufferedImage(newWidth, newHeight, image.getType());
	    Graphics2D g2d = rotated.createGraphics();

	    // move the original image to the center of the new canvas
	    g2d.translate((newWidth - width) / 2.0, (newHeight - height) / 2.0);

	    // rotate around the center of the original image
	    g2d.rotate(rads, width / 2.0, height / 2.0);
	    g2d.drawImage(image, 0, 0, null);
	    g2d.dispose();

	    return rotated;
	}
}
