package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public final class TextureManager {

	private Map<String, BufferedImage> textures;
	
	public TextureManager() {
        textures = new HashMap<>();
    }
	
    public void loadTexture(String filename, String textureName) {
    	try (InputStream is = getClass().getResourceAsStream(filename)) {
            if (is == null) {
                throw new IllegalArgumentException("Missing resource: " + filename);
            }
            BufferedImage img = ImageIO.read(is);
            textures.put(textureName, img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTexture(BufferedImage image, String name) {
    	textures.put(name,image);
    }
    public BufferedImage getTexture(String filename) {
    	if (!textures.containsKey(filename)) System.out.println("----FILE NOT FOUND----");
        return textures.get(filename);
    }
    public boolean containsTexture(String fileName) {
    	return textures.containsKey(fileName);
    }
}
