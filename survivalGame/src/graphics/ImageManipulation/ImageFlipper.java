package graphics.ImageManipulation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class ImageFlipper {


	/**
	 * rotates horizontally
	 * @param bufferedImage input
	 * @return A horizontally flipped buffered image
	 */
	public static BufferedImage flipImageHorizontal(BufferedImage image) {
	    int width = image.getWidth();
	    int height = image.getHeight();

	    //new blank image
	    BufferedImage flipped = new BufferedImage(width, height, image.getType());
	    Graphics2D g2d = flipped.createGraphics();

	    g2d.scale(-1, 1); //flip horizontally
	    g2d.translate(-width, 0); //center it
	    g2d.drawImage(image, 0, 0, null);
	    g2d.dispose();

	    return flipped;
	}
	
	/**
	 * rotates vertically
	 * @param bufferedImage input
	 * @return A vertically flipped buffered image
	 */
	public static BufferedImage flipImageVertical(BufferedImage image) {
	    int width = image.getWidth();
	    int height = image.getHeight();

	    //new blank image
	    BufferedImage flipped = new BufferedImage(width, height, image.getType());
	    Graphics2D g2d = flipped.createGraphics();

	    g2d.scale(1,  -1); //flip vertically
	    g2d.translate(0, -height); //Center it back
	    g2d.drawImage(image, 0, 0, null);
	    g2d.dispose();

	    return flipped;
	}
	
}
