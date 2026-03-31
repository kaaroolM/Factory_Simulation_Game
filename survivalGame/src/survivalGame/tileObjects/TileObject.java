
package survivalGame.tileObjects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.GameGraphics;
import graphics.TextureManager;
import graphics.WorldRenderable;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileObjectID;
import survivalGame.tileObjects.FactoryComponents.FactoryComponent;

public sealed abstract class TileObject implements WorldRenderable permits FactoryComponent, TileRock, TileTree {
	
	protected BufferedImage texture; 
	protected Tile parentTile;
	
	protected int verticalOffset = 0;
	protected boolean toRender = true; 
	
	public TileObject(Tile parentTile) {
		this.parentTile = parentTile;
	}
	
	public abstract TileObjectID getTileObjectID();
	
	public boolean isActive() {
		return toRender;
	}

	public void setActive(boolean isActive) {
		this.toRender = isActive;
	}

	public void setTexture(String Texture, TextureManager textureM) {
		texture = textureM.getTexture(Texture);
	}
	
	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}
	
	@Override
	public void render(Graphics2D g, GameGraphics graphics) {
		if (toRender) {
			int pixelX = parentTile.pixelX;
			int pixelY = parentTile.pixelY;
			g.drawImage(texture, pixelX, pixelY + verticalOffset, null); 
		}
		
	}
	
	@Override
	public int getY() {
		return parentTile.y;
	}
	
}
