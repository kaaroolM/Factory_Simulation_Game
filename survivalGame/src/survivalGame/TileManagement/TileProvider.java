package survivalGame.TileManagement;

import graphics.GameGraphics;

public final class TileProvider {

	/**
	 * Accesses the Tile according to the pixel coordinates of the click.
	 * @param clickedX is x coordinate of the click.
	 * @param clickedY is y coordinate of the click
	 * @return {@link Tile}
	 */
	public static Tile pixel_AccessTile(int clickedX, int clickedY) {
		/* SUMMARY:
		 * To get Tile, get pixel Coordinates, convert to tile coordinates,
		 * Using them you can get chunk index. Get coordinates relative to chunk coordinates.
		 * Access Tile from the chunk using those relative coordinates.
		 */
		
		//clickedX - screenWidth / 2, to account for screen offset to make the character stay in the center. 
		//Divide it by zoom to account for the current zoom level.
		//Add the offset from origin. Result gives you pixel coordinates where origin is taken from the top left of map.
		int pixelX = (int) ( (clickedX - GameGraphics.SCREEN_WIDTH / 2) / GameGraphics.getCameraZoom() + GameGraphics.getOriginOffset()[0]);
		int pixelY = (int) ((clickedY - GameGraphics.SCREEN_HEIGHT / 2) / GameGraphics.getCameraZoom() + GameGraphics.getOriginOffset()[1]);
		
		//Divide by TileSize to get tile coordinates.
		int x = (pixelX / GameGraphics.TILESIZE);
		int y = (pixelY / GameGraphics.TILESIZE);
		
		//PositionInArray gives index, calculated knowing that world generation is a nested for loop for chunk generation. 
		int chunkSize = GameGraphics.chunkSize;
		int chunkAmount = GameGraphics.worldSize / chunkSize;
		int positionInArray = chunkAmount * (x / chunkSize) + (y / chunkSize);		
		if (positionInArray > GameGraphics.chunks.length) return null;
		TileChunk chunk = (TileChunk) GameGraphics.chunks[positionInArray];
		
		//Chunk coordinates gives coordinates relative to the top left of the chunk (chunk origin).
		
		int chunkX = x - (chunk.getX() * chunkSize);
		int chunkY = y - (chunk.getY() * chunkSize);
	
		if (chunkX * chunkSize + chunkY < 0 ) return null; //Out of bounds
		Tile tile = chunk.getTiles().get(chunkX * chunkSize + chunkY);

		//return tile.
		return tile;
	}
	
	/**
	 * Returns Tile reference given Tile x and y coordinates.
	 * @param x coordinate of wanted Tile
	 * @param y coordinate of wanted Tile
	 * @return Tile reference
	 */
	public static Tile world_AccessTile(int x, int y) {
		if (x < 0 || y < 0 || x > GameGraphics.worldSize || y > GameGraphics.worldSize) return null;
		int chunkSize = GameGraphics.chunkSize;
		int chunkAmount = GameGraphics.worldSize / chunkSize;
		int positionInArray = chunkAmount * (x / chunkSize) + (y / chunkSize);	
		if (positionInArray >= GameGraphics.chunks.length) return null;  //Out of bounds
		TileChunk chunk = (TileChunk) GameGraphics.chunks[positionInArray];
		
		int chunkX = x - (chunk.getX() * chunkSize);
		int chunkY = y - (chunk.getY() * chunkSize);
	
		if (chunkX * chunkSize + chunkY < 0 || chunkX * chunkSize + chunkY >= chunk.getTiles().size() ) return null; //Out of bounds
		Tile tile = chunk.getTiles().get(chunkX * chunkSize + chunkY);

		return tile;
	}
	
}
