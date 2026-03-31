package survivalGame;

import survivalGame.TileManagement.TileChunk;

/**
 * WorldInfo class contains data for the world.
 * Including an array of {@link TileChunk} chunks
 * Size of the chunks, worldSize, and amount of chunks.
 * Amount of chunks is calculated by worldSize / chunkSize. 
 */
public final class WorldInfo {

	public final TileChunk[] chunks; 
	public final int worldSize; 
	public final int chunkSize;
	public final int chunkAmount;
	public WorldInfo(TileChunk[] chunks, int worldSize, int chunkSize) {
		this.chunks = chunks;
		this.worldSize = worldSize;
		this.chunkSize = chunkSize;
		chunkAmount = worldSize / chunkSize;
	}
}
