package survivalGame.TileManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import survivalGame.tileObjects.PlacementInfo;
import survivalGame.tileObjects.TileObject;
import survivalGame.tileObjects.TileRock;
import survivalGame.tileObjects.TileTree;
import survivalGame.tileObjects.FactoryComponents.Conveyor;
import survivalGame.tileObjects.FactoryComponents.ConveyorSplitter;
import survivalGame.tileObjects.FactoryComponents.Planker;
import survivalGame.tileObjects.FactoryComponents.RockDriller;
import survivalGame.tileObjects.FactoryComponents.TreeHarvester;

public final class TileObjectFactory {
	
	//Map linking ID's to functions that can instantiate the appropiate class.
	static private Map<TileObjectID, Function<PlacementInfo, TileObject>> IDtoInstance;
	
	static {
		IDtoInstance = new HashMap<>();
		IDtoInstance.put(TileObjectID.CONVEYOR, info -> new Conveyor(info.getTile(),info.getDirection()));
		IDtoInstance.put(TileObjectID.TREE_HARVESTER, info -> new TreeHarvester(info.getTile(),info.getDirection()));
		IDtoInstance.put(TileObjectID.PLANKER, info -> new Planker(info.getTile(),info.getDirection()));
		IDtoInstance.put(TileObjectID.CONVEYOR_SPLITTER_L, info -> new ConveyorSplitter(info.getTile(),info.getDirection(),true));
		IDtoInstance.put(TileObjectID.CONVEYOR_SPLITTER_R, info -> new ConveyorSplitter(info.getTile(),info.getDirection(),false));
		IDtoInstance.put(TileObjectID.ROCK_DRILLER, info -> new RockDriller(info.getTile(),info.getDirection()));
		IDtoInstance.put(TileObjectID.TREE, info -> new TileTree(info.getTile()));
		IDtoInstance.put(TileObjectID.ROCK, info -> new TileRock(info.getTile()));
	}
	
	/**
	 * Instantiates the placeable object according to the itemID.
	 * @param tileObjectID specifies which TileObject to create.
	 * @param info contains tile position data and rotation.
	 * @return The instantiated {@link TileObject}
	 */
	public static TileObject createTileObject(TileObjectID tileObjectID, PlacementInfo info) {
		Function<PlacementInfo, TileObject> function = IDtoInstance.get(tileObjectID);
		if (function == null) {
			throw new IllegalArgumentException("ItemID: " + tileObjectID.toString() + " is invalid! ");
		}
		return function.apply(info);
		
	}
}
