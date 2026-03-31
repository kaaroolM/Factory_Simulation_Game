package survivalGame;

import java.io.IOException;

import graphics.GameGraphics;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileChunk;
import survivalGame.tileObjects.TileObject;
import survivalGame.tileObjects.TileRock;
import survivalGame.tileObjects.TileTree;

public final class WorldGenerator {
	
	private static int tiles = 0;
	
	public static WorldInfo generateWorld() {
		
		int worldSize = 36 * 9;
		//world size is length or width of world, so if world size 2, 4 tiles total
		//chunk size recommended: 6
		int chunkSize = 6;
		int chunkAmount = worldSize / chunkSize;
		
		TileChunk[] chunks = new TileChunk[chunkAmount * chunkAmount];	

		System.out.println(chunks.length + " chunks and " + tiles + " tiles") ;
		
		for (int x = 0; x < chunkAmount; x++) {
			for (int y = 0; y < chunkAmount; y++ ) {
				createChunk(x,y,chunkSize, chunkAmount, chunks);
			}
		}
		WorldInfo info = new WorldInfo(chunks, worldSize, chunkSize);
		
		Player player = new Player();
		GameGraphics.attachPlayer(player);
		
		return info;	
	}
	
	/**
	 * Each <@link TileChunk> has a List of Tiles, that is size of chunkSize squared.
	 * @param x coordinate of chunk
	 * @param y coordinate of chunk
	 * @param chunkSize is the width and height of the chunk square
	 * @param chunkAmount is the amount of chunks there will be in the world
	 */
	private static void createChunk(int x, int y, int chunkSize, int chunkAmount, TileChunk[] chunks) {
		TileChunk chunk = new TileChunk(x ,y, chunkSize, GameGraphics.TILESIZE);
		for (int Tx = 0; Tx < chunkSize; Tx++) {
			for (int Ty = 0; Ty < chunkSize; Ty++ ) {
				//Tile Coordinates are coordinates relative to chunk + number of tiles up to that chunk coordinate. 
				Tile tile = new Tile(Tx  + (x * chunkSize), Ty  + (y * chunkSize) ,chunk,  GameGraphics.TILESIZE);
				tiles++;
				
				int rNum = (int) (Math.random() * 285) + 1; 
				if (rNum <= 2) {
					TileObject tree = new TileTree(tile);
					tile.setTileObject(tree);
				}
				else if (rNum <= 3) {
					TileObject rock = new TileRock(tile);
					tile.setTileObject(rock);
				}
				
				chunk.add(tile);
			}
		}
		chunks[x * chunkAmount + y] = chunk;
	}
}
