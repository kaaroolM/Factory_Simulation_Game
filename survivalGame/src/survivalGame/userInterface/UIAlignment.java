package survivalGame.userInterface;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import graphics.GameGraphics;

public final class UIAlignment {

	final private static int WIDTH = GameGraphics.SCREEN_WIDTH;
	final private static int HEIGHT = GameGraphics.SCREEN_HEIGHT;
	
	//Scaling which can be changed to change how close the UI is to edge / from center. 
	private static double centerScaling = 0.25;
	private static double edgeScaling = 0.1;

	private static int leftEdge = (int) (0 + WIDTH * edgeScaling);
	private static int rightEdge = (int) (WIDTH - WIDTH * edgeScaling);
	private static int topEdge =  (int) (0 + HEIGHT * edgeScaling);
	private static int bottomEdge = (int) (HEIGHT - HEIGHT * edgeScaling);
	
	private static int topCenter = (int) (HEIGHT/2 - HEIGHT * centerScaling);
	private static int bottomCenter = (int) (HEIGHT/2 + HEIGHT * centerScaling);
	private static int rightCenter = (int) (WIDTH/2 + WIDTH * centerScaling);
	private static int leftCenter = (int) (WIDTH/2 - WIDTH * centerScaling);
			
	//Maps UIAnchors to their coordinates. 
	static EnumMap<UIAnchor,int[]> anchorCoordinates = new EnumMap<>(Map.ofEntries(
			Map.entry(UIAnchor.CENTER, new int[] {WIDTH/2, HEIGHT/2}),
			Map.entry(UIAnchor.LEFT, new int[] {leftEdge, HEIGHT/2}),
			Map.entry(UIAnchor.RIGHT, new int[] {rightEdge, HEIGHT/2}),
			Map.entry(UIAnchor.TOP, new int[] {WIDTH/2, topEdge} ),
			Map.entry(UIAnchor.BOTTOM, new int[] {WIDTH/2,bottomEdge}),
			
			Map.entry(UIAnchor.CENTER_LEFT, new int[] {leftCenter, HEIGHT/2}),
			Map.entry(UIAnchor.CENTER_RIGHT,  new int[] {rightCenter, HEIGHT/2}),
			Map.entry(UIAnchor.CENTER_TOP, new int[] { (WIDTH/2), topCenter }),
			Map.entry(UIAnchor.CENTER_BOTTOM, new int[] { (WIDTH/2), bottomCenter}),
			Map.entry(UIAnchor.CENTER_BOTTOM_LEFT, new int[] { leftCenter, bottomCenter}),
			Map.entry(UIAnchor.CENTER_BOTTOM_RIGHT, new int[] { rightCenter, bottomCenter}),
			Map.entry(UIAnchor.CENTER_TOP_LEFT, new int[] { leftCenter, topCenter}),
			Map.entry(UIAnchor.CENTER_TOP_RIGHT, new int[] { rightCenter, topCenter}),
			
			Map.entry(UIAnchor.TOP_LEFT, new int[] {(int) leftEdge, topEdge}),
			Map.entry(UIAnchor.TOP_RIGHT, new int[] {(int) rightEdge, topEdge}),
			Map.entry(UIAnchor.BOTTOM_LEFT, new int[] {leftEdge, bottomEdge}),
			Map.entry(UIAnchor.BOTTOM_RIGHT, new int[] {rightEdge, bottomEdge})
	));
	
	/**
	 * Gets screen coordinates for the Anchor point
	 * @param anchorPoint is the anchor you want the coordinate for
	 * @return int[] coordinate where int[0] is the x coordinate, and int[1] is the y coordinate. 
	 */
	public static int[] getCoordinateFromAnchor(UIAnchor anchorPoint) {
		if (!anchorCoordinates.containsKey(anchorPoint)) {
			throw new IllegalArgumentException("Invalid anchor point: " + anchorPoint);
		}
		return anchorCoordinates.get(anchorPoint);
	}
	
	public static int[] centerTexture(int[] coordinate, BufferedImage texture) {
		return new int[] {coordinate[0] - texture.getWidth() / 2, coordinate[1] - texture.getHeight() /2} ;
	}
	
	public static int[] centerTexture(UIAnchor anchorPoint, BufferedImage texture) {
		int[] coordinate = getCoordinateFromAnchor(anchorPoint);
		return new int[] {coordinate[0] - texture.getWidth() / 2, coordinate[1] - texture.getHeight() /2} ;
	}
	
	public static int[] centerTexture(UIAnchor anchorPoint, String textureName) {
		if (!GameGraphics.getTextureManager().containsTexture(textureName)) {
			throw new IllegalArgumentException("Texture does not exist in TextureManager: " + textureName);
		}
		BufferedImage texture = GameGraphics.getTextureManager().getTexture(textureName);
		int[] coordinate = getCoordinateFromAnchor(anchorPoint);
		return new int[] {coordinate[0] - texture.getWidth() / 2, coordinate[1] - texture.getHeight() /2} ;
	}
	
}
