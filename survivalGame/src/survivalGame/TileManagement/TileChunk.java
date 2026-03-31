package survivalGame.TileManagement;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import graphics.GameGraphics;
import graphics.WorldRenderable;

/**
 * Contains a List of {@link Tile} (s) 
 * And an x and y coordinate.
 * 
 * List size is usually chunkSize * chunkSize
 */
public final class TileChunk implements WorldRenderable{

	List<Tile> tiles = new ArrayList<>();

	private final int x;
	private final int y;
	
	//If screen was stretched to world size, 
	//these pixel coordinates would represent where they are in the screen from the top left corner
	final int pixelX; 
	final int pixelY;
	final int chunkPixelSize;
	
	private boolean activatedTiles = false;
	public TileChunk(int x, int y, int chunkSize, int tileSize) {
		this.x = x;
		this.y = y;

		pixelX = x * chunkSize * tileSize;
		pixelY = y * chunkSize * tileSize;
		chunkPixelSize = chunkSize * tileSize;
		
		GameGraphics.registerWorldObj(this, 1);
	}
	
	public void add(Tile tile) {
		getTiles().add(tile);
	}

	@Override
	public void render(Graphics2D g, GameGraphics graphics) {
		renderChunk(g,graphics);
		
	}
	
	public void renderChunk(Graphics2D g, GameGraphics graphics) {
		//tile chunk works like gamegraphics, but groups tiles together avoiding repeated checks
		int width = (int) (graphics.getWidth() / GameGraphics.getCameraZoom()); 
		int height = (int) (graphics.getHeight() / GameGraphics.getCameraZoom() ); 
		//Renders tiles if within range
		
		/*
		 * BOUNDARY LOGIC:
		 * 
		 * WIDTH/2 and HEIGHT/2 are offsets due to the player centering offset in gameGraphics.
		 * 
		 */
		if (pixelX > GameGraphics.getOriginOffset()[0] - chunkPixelSize - width / 2 && pixelX < GameGraphics.getOriginOffset()[0] + width / 2 
				&& pixelY > GameGraphics.getOriginOffset()[1] - chunkPixelSize - height / 2 && pixelY < GameGraphics.getOriginOffset()[1] + height / 2 ) {
			
			for (Tile tile : getTiles()) {
				tile.setActive(true);
				activatedTiles = true;
			}
			
		}
		else if (activatedTiles){
			for (Tile tile : getTiles()) {
				tile.setActive(false);
				activatedTiles = false;
			}
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}



	
}
