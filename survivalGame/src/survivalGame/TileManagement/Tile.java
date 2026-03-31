package survivalGame.TileManagement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;

import graphics.GameGraphics;
import graphics.WorldRenderable;
import survivalGame.tileObjects.TileObject;
import survivalGame.tileObjects.FactoryComponents.FactoryComponent;

public final class Tile implements WorldRenderable{
	
	private TileType tileType = TileType.GRASS; //NOT FINAL FOR NOW
	
	final public int x;
	final public int y;
	public final int tileSize;
	
	final public int pixelX;
	final public int pixelY;
	
	private boolean toRender = false;
	private boolean selected = false;
	private TileObject tileObject; 
	public final TileChunk chunkParent;
	
	BufferedImage texture = GameGraphics.getTextureManager().getTexture("Grass");
	// Declare image outside the try block
	public Tile(int x, int y, TileChunk parent, int tileSize) {
		
		this.x = x;
		this.y = y;
		this.tileSize = tileSize;
		pixelX = x * tileSize;
		pixelY = y * tileSize;

		GameGraphics.registerWorldObj(this, 1);
		
		chunkParent = parent;
	}
	
	@Override
	public void render(Graphics2D g, GameGraphics graphics) {
		if (toRender) {
			
			g.setColor(new Color(66,100,74));
			g.fillRect(pixelX, pixelY, tileSize, tileSize);
			g.drawImage(texture, pixelX, pixelY, tileSize,tileSize, graphics);
			
			if (selected) {
				g.setColor(new Color(0,0,111));
				g.drawRect(pixelX + tileSize / 4, pixelY + tileSize / 4, tileSize - tileSize / 2, tileSize - tileSize / 2);
				g.setColor(new Color(0,0,50,35));
				g.fillRect(pixelX, pixelY, tileSize, tileSize);
			}
			
		}	
	}

	@Override
	public int getY() {
		return y;
	}
	
	public void setActive(boolean state) {
		toRender = state;
		if (tileObject != null) {
			tileObject.setActive(state);
		}
	}
	@Override
	public boolean isActive() {
		return false;
	}
	public void setSelect(boolean selected) {
		this.selected = selected;
	}
	public boolean isSelected() {
		return selected;
	}
	
	public void setTileObject(TileObject object) {
		tileObject = object;
	}
	
	public TileObject getTileObject() {
		return tileObject;
	}
	
	public boolean isEmpty() {
		return tileObject == null;
	}

	public TileType getTileType() {
		return tileType;
	}
	
	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}
	
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
}
