package survivalGame;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import graphics.GameGraphics;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileChunk;
import survivalGame.TileManagement.TileObjectFactory;
import survivalGame.TileManagement.TileObjectID;
import survivalGame.TileManagement.TileType;
import survivalGame.tileObjects.Direction;
import survivalGame.tileObjects.PlacementInfo;
import survivalGame.tileObjects.TileObject;

public final class WorldIO {

	/**
	 * Used for saving files to inform which version of the software the save file is from.
	 * A deployed game will need this when introducing new features after release and prevent loss of progress.
	 * In this NEA this likely won't be used as there are no users. 
	 */
	public static final int SAVE_VERSION = 1;


	/*
	 * SAVING PROCESS:
	 * 
	 * Save Version,
	 * WorldSize,
	 * ChunkAmount,
	 * 
	 * +Tile Saving:
	 * [TileType ID]
	 * [HasTileObject?]
	 * [TileObjectID]
	 * [TileObject instanceof FactoryComponent?]
	 * [RotationID]
	 * + more info???
	 * 
	 * so far  Byte, Boolean ? Byte Boolean ? Byte
	 * 
	 * HOW SAVING WORKS:
	 * saving and loading from binary file needs to have correct order of processing.
	 * If you save Byte, Byte, boolean, you must also load Byte, Byte, Boolean, in that correct order, at the correct pointer location. 
	 * You must load exactly the same way as you save. 
	 * DataInputStreams has an internal pointer that moves accordingly to what you read, For example, reading Boolean/Byte makes pointer move 1 byte.
	 * Integer makes pointer move 4 bytes. Files are Byte-Addressed, which means boolean won't make pointer move 1 bit since the minimum is a byte. 
	 * 	
	 * It knows when a byte belongs to a TileTypeID or a TileObjectID, since it has been saved the exact same order.
	 * Inproper reading, like reading an integer rather than a byte, corrupts the loading process. 
	 * This is because the pointer will be permanently misaligned. 
	 */
    public static void save(WorldInfo world, File file) throws IOException {
    	 try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                         
    		 out.writeInt(SAVE_VERSION);
    		 out.writeInt(world.worldSize);
    		 out.writeInt(world.chunkAmount);
    		 
    		 saveWorld(world,out);
    		 out.close();
    	 }
    }
    
    private static void saveWorld(WorldInfo world, DataOutputStream out) throws IOException {
    	int chunkAmount = world.chunkAmount;
    	
    	 for (int x = 0; x < chunkAmount; x++) {
				for (int y = 0; y < chunkAmount; y++ ) {
					for (Tile tile : world.chunks[x * chunkAmount + y].getTiles()) {
						writeTile(tile,out);
					}
				}
		 }
    }
    private static void writeTile(Tile tile, DataOutputStream out) throws IOException {
		TileObject tileObject = tile.getTileObject();
		
		//Write TyleType ID
	    out.writeByte(tile.getTileType().getID());
	    
	    //Write boolean if there is a tileObject
	    out.writeBoolean(tileObject != null);
	
	    if (tileObject == null) return;
	    
	    //If so, write the ID of the tileObject
	    out.writeByte(tileObject.getTileObjectID().id);
	    
	    //Write boolean asking if the tileObject is a factoryComponent
	    //out.writeBoolean(tileObject instanceof FactoryComponent);
	    
	    //if (!(tileObject instanceof FactoryComponent)) return;
	    //out.writeByte( ((FactoryComponent) tileObject).getRotation().getRotationID() );
	    
	}
    
    
    /**
     * Loads from the given file
     * @param file to load from. 
     * @return {@link WorldInfo}
     */
    private static WorldInfo load(File file) throws IOException {
    	try (DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
	                        
	   		int saveVersion = input.readInt();
	   		int worldSize = input.readInt();
	   		int chunkAmount = input.readInt();
	   		
	   		int chunkSize = 6;
	   		TileChunk[] chunks = new TileChunk[chunkAmount * chunkAmount];	
	   		
	   		for (int x = 0; x < chunkAmount; x++) {
	   				for (int y = 0; y < chunkAmount; y++ ) {
	   					chunks[chunkAmount * x + y] = loadChunk(x , y , 
									   						new TileChunk(x, y, chunkSize, GameGraphics.TILESIZE),
									   						chunkSize, input);
	   				}
		   	}
	   		input.close();
	   		return new WorldInfo(chunks, worldSize, chunkSize);
	   	}
		 
	   }
    
    /**
     * Loads the chunk 
     * @param chunkSize which is the size of chunk's list (chunkSize)^2
     * @param chunk instance {@link TileChunk} to add to. Must be an empty chunk.
     * @return new {@link TileChunk} instance
     */
    private static TileChunk loadChunk(int x, int y, TileChunk chunk, int chunkSize, DataInputStream input) throws IOException {
		for (int Tx = 0; Tx < chunkSize; Tx++) {
			for (int Ty = 0; Ty < chunkSize; Ty++ ) {
				
				int tileX = Tx  + (x * chunkSize), tileY = Ty  + (y * chunkSize);
	
				Tile tile = readTile(tileX, tileY, chunk, input);
				
				chunk.add(tile);
				
			}
		}
		return chunk;
	}
    
    private static Tile readTile(int x, int y, TileChunk chunk, DataInputStream input) throws IOException {
    	/* TileType
	   	* Is there an object? 
	   	* If so, Write TileObject ID.
		* Is it a factoryComponent?
		* If so write rotationID required for factory component.
		* 
		* Byte, Boolean ? Byte Boolean ? Byte
		*/
    	
    	Tile tile = new Tile(x, y , chunk,  GameGraphics.TILESIZE);
		
		tile.setTileType(TileType.fromId(input.readByte())); //READS tileType Byte
		boolean hasTileObject = input.readBoolean(); //READS tileObject boolean
		if (hasTileObject) {
			TileObjectID ID = TileObjectID.fromId(input.readByte());  //READS tileObjectID
			TileObject object = TileObjectFactory.createTileObject(ID, new PlacementInfo(tile, Direction.NORTH));
			tile.setTileObject(object);
		}
		
		return tile;
    	
    }
    
    public static void saveFile(WorldInfo world, String fileName) throws IOException {
    	File file = new File("saves/" + fileName + ".dat");

    	file.getParentFile().mkdirs(); // creates "saves" folder if missing

    	System.out.println("\\ SUCCESSFULLY SAVED! \\");
    	save(world, file);
    }
    
    /**
     * Loads from given File.
     * @return {@link WorldInfo} containing world data 
     * @throws IOException
     */
    public static WorldInfo loadFromFile(String fileName) throws IOException {
    	File file = new File("saves/" + fileName + ".dat");

    	if (!file.exists()) {
    	    return null;
    	}
    	System.out.println("// LOADING! //");
    	return load(file);
    }
    
    public static void deleteFile(String fileName) {
    	File file = new File("saves/" + fileName + ".dat");
    
    	if (file.delete()) { 
    	    System.out.println("Deleted the file: " + file.getName());
    	} 
    	else {
    		System.out.println("Failed to delete the file.");
    	} 
    }
    
    /**
     * Searches the "saves/" folder for the fileName.dat file
     * @param fileName to search for. 
     * @return true boolean value if the file is present
     */
    public static boolean isFilePresent(String fileName) {
    	File file = new File("saves/" + fileName + ".dat");

    	if (!file.exists()) return false;
    	
    	return true;
    }
}
