package survivalGame;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import survivalGame.ItemManagement.ItemID;
import survivalGame.tileObjects.PlacementInfo;
import survivalGame.tileObjects.TileObject;
import survivalGame.tileObjects.FactoryComponents.Conveyor;
import survivalGame.tileObjects.FactoryComponents.ConveyorSplitter;
import survivalGame.tileObjects.FactoryComponents.Planker;
import survivalGame.tileObjects.FactoryComponents.RockDriller;
import survivalGame.tileObjects.FactoryComponents.TreeHarvester;

/**
 * A factory that maps itemID to placeable object that can be instantiated
 */
public final class PlaceablesFactory {
	
	static Map<ItemID, Function<PlacementInfo, TileObject>> placementMap;
	
	/*
	 * Links itemID from inventory to instantiating an object, normally placed on a tile. 
	 */
	static {
		placementMap = new HashMap<>();
		placementMap.put(ItemID.CONVEYOR, info -> new Conveyor(info.getTile(),info.getDirection()));
		placementMap.put(ItemID.TREEHARVESTER, info -> new TreeHarvester(info.getTile(),info.getDirection()));
		placementMap.put(ItemID.PLANKER, info -> new Planker(info.getTile(),info.getDirection()));
		placementMap.put(ItemID.CONVEYORSPLITTER_R, info -> new ConveyorSplitter(info.getTile(),info.getDirection(),true));
		placementMap.put(ItemID.CONVEYORSPLITTER_L, info -> new ConveyorSplitter(info.getTile(),info.getDirection(),false));
		placementMap.put(ItemID.ROCKDRILLER, info -> new RockDriller(info.getTile(),info.getDirection()));
	}
	
	/**
	 * Instantiates the placeable object according to the itemID.
	 * @param itemID specifies which placeable to create.
	 * @param info contains tile position data and rotation.
	 * @return The instantiated {@link TileObject}
	 */
	public static TileObject createPlaceable(ItemID itemID, PlacementInfo info) {
		if (!placementMap.containsKey(itemID)) {
			 throw new IllegalArgumentException("ItemID: " + itemID.toString() + " is invalid! ");
		}
		
		Function<PlacementInfo, TileObject> function = placementMap.get(itemID);

		return function.apply(info);
		
	}
}
