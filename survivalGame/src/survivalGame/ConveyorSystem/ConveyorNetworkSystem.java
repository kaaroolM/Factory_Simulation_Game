package survivalGame.ConveyorSystem;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import survivalGame.ItemManagement.WorldItem;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileProvider;
import survivalGame.tileObjects.Direction;
import survivalGame.tileObjects.FactoryComponents.Conveyor;

public final class ConveyorNetworkSystem {
	
	//a hashmap linking the conveyor belt key to the end of the sequence in order to traverse backwards.
	private final Map<BeltSequence, Conveyor> keyToTail = new HashMap<>();

	//Key counter that represent the beltSequence value. It is important that this value is unique.
	private int keyCounter = 1;
	
	public BeltSequence generateConveyorKey() {
		return new BeltSequence(keyCounter++);
	}
	
	public void assignTail(BeltSequence key, Conveyor conv) {
		keyToTail.put(key, conv);
	}
	
	/*
	 * Returns a List<Conveyor> containing all the tails of the conveyors.
	 * A conveyor Tail is the end of the conveyor sequence. The tail has no target conveyor that is part of the sequence.
	 */
	public List<Conveyor> getTails(){
		return new ArrayList<>(keyToTail.values());
	}
	
	public void asignTargetConveyor(Conveyor currentConveyor) {
		Tile targetTile = currentConveyor.getTargetTile(currentConveyor.getRotation());
		Conveyor target = getConveyorFromTile(targetTile);
		currentConveyor.targetConveyor = target;
	}
	
	/*
	 * Initialises the conveyor by making it suitable for traversing.
	 * Connects the conveyors into a network of linked lists. 
	 */
	public void initializeConveyor(Conveyor currentConveyor) {
		currentConveyor.spriteMask |= currentConveyor.getRotation().getRotationMask() << 4;
		
		//First search surrounding area and update variables as needed
		searchForInputConveyors(currentConveyor);
		
		//update the sprite determined by surrounding conveyors
		ConveyorSpriteManager.updateSprite(currentConveyor);
		
		//Get conveyor from targetTile
		Tile targetTile = currentConveyor.getTargetTile(currentConveyor.getRotation());
		currentConveyor.targetConveyor = getConveyorFromTile(targetTile);
		
		ConveyorSpriteManager.changeSprite(currentConveyor.targetConveyor, currentConveyor.getRotation());
		
		//If Conveyor has no input and target conveyor, or target is in a different belt sequence then.. Create new beltSequence key
		if (currentConveyor.inputConveyor == null && (currentConveyor.targetConveyor == null || currentConveyor.targetConveyor.hasInputConveyor())) {
			becomeNewConveyorTail(currentConveyor);
		}	
		//If Conveyor has no input but the targetConveyor also doesn't have an input then connect them and inherit key.
		else if ( currentConveyor.inputConveyor == null && !currentConveyor.targetConveyor.hasInputConveyor()) {
			currentConveyor.beltSequence = currentConveyor.targetConveyor.beltSequence;
			currentConveyor.targetConveyor.addInputConveyor(currentConveyor);
		}
		//Else inherit from inputConveyor, and make the conveyor the new tail.
		else {
			currentConveyor.beltSequence = currentConveyor.inputConveyor.beltSequence;
			assignTail(currentConveyor.beltSequence, currentConveyor);
		}
		

	}
	
	/**
	 * Searches surrounding tiles for conveyors that point to this tile
	 * @param currentConveyor is the conveyor we are searching inputs for.
	 */
	private void searchForInputConveyors(Conveyor currentConveyor) {
		Tile parentTile = currentConveyor.getParentTile();
		EnumSet<Direction> inputBlackList = currentConveyor.getInputBlackList();
		
		//Get surrounding tiles.
		Tile[] surroundings = new Tile[4];
		surroundings[0] = TileProvider.world_AccessTile(parentTile.x,parentTile.y - 1);
		surroundings[1] = TileProvider.world_AccessTile(parentTile.x + 1, parentTile.y);
		surroundings[2] = TileProvider.world_AccessTile(parentTile.x, parentTile.y + 1);
		surroundings[3] = TileProvider.world_AccessTile(parentTile.x - 1, parentTile.y);
		for (Tile tile : surroundings) {
			//check surrounding tiles for conveyors, make them the input if they point towards you.
			//Ignore the one you point towards too. 

			if (currentConveyor.getTargetTile() != tile && tile.getTileObject() instanceof IContainsConveyor containingConveyor) {
				//If the tile isnt the targetTile (basically not facing it), and the object is a conveyor..
				Conveyor inputConveyor = containingConveyor.getConveyor();

				//And if its pointing at THIS tile, and obeys the inputBlacklist Directions...
				if ( !inputConveyor.isPointingAt(parentTile)) continue;
				if ( inputBlackList != null && inputBlackList.contains(inputConveyor.getRotation())) continue;
				
				//Make this conveyor the input, and therefore make this conveyor the input's target. Doubly Linked
				currentConveyor.addInputConveyor(inputConveyor);
				inputConveyor.targetConveyor = currentConveyor;

				ConveyorSpriteManager.changeSprite(currentConveyor, inputConveyor.getRotation());
			
			}
		}
	}
	
	/*
	 * Generates a new conveyor key as well as be asigned as tail to that key.
	 * Used for conveyors that are not connected or around any other conveyors. 
	 */
	private void becomeNewConveyorTail(Conveyor conveyor) {
		conveyor.beltSequence = generateConveyorKey();
		assignTail(conveyor.beltSequence, conveyor);
	}
	
	/**
	 * @param tile to get conveyor from
	 * @return any instance of conveyor on that tile. This function was made due to the interface: IContainsConveyor
	 */
	public static Conveyor getConveyorFromTile(Tile tile) {
		if (!(tile.getTileObject() instanceof IContainsConveyor conv)) return null;
		return conv.getConveyor();
	}
	
	/**
	 * Makes conveyor attempt to pass to the next target, It won't passed if its locked.
	 * @param conveyor that passes
	 * @param item to pass (in case you want to create an item)
	 */
	public void conveyorPassToTarget(Conveyor conveyor, WorldItem item) {
		if (item == null || conveyor.isLocked() || conveyor.targetConveyor == null ) {
			return;
		}
		if (conveyor.getTargetTile().isEmpty()) {
			//If the conveyor is removed but variable not set to null.
			conveyor.targetConveyor = null;
			return;
		}
		if ( !conveyor.targetConveyor.isEmpty() ) return;
		
		conveyor.targetConveyor.recieveWorldItem(item);
		conveyor.heldItem = null;
	}
	
	public void deleteConveyor(Conveyor conveyor) {
		//Input conveyors are always of the same beltSequence key
		//Target conveyors can be any conveyor. 
		
		
		if (keyToTail.containsValue(conveyor) && conveyor.targetConveyor == null) {
			//Conveyor is isolated
			keyToTail.remove(conveyor.beltSequence);
			
			//System.out.println("Delete ISOLATED");
		}
		else if (keyToTail.containsValue(conveyor) && conveyor.targetConveyor.beltSequence == conveyor.beltSequence) {
			//When removing last/tail conveyor, next conveyor is the new tail
			keyToTail.put(conveyor.beltSequence, conveyor.targetConveyor);
			conveyor.targetConveyor.inputConveyor = null;
		}
		else if (conveyor.hasInputConveyor() && conveyor.hasTargetConveyor()) {
			//Similar to removing in the middle of a linked list
			//Except the lower end needs to have a new beltKey
			//Input conveyor needs to be in that map.
			conveyor.inputConveyor.targetConveyor = null;
			BeltSequence key = generateConveyorKey();
			replaceBeltKey(conveyor.inputConveyor, key);
			keyToTail.put(key, conveyor.inputConveyor);
			
			if (conveyor.targetConveyor.inputConveyor == conveyor){
				conveyor.targetConveyor.inputConveyor = null;
			}
		}
		else if (conveyor.inputConveyor != null){
			conveyor.inputConveyor.targetConveyor = null;
		}
	}
	
	public void replaceBeltKey(Conveyor conveyor, BeltSequence beltKey) {
		if (conveyor == null) return;
		
		conveyor.beltSequence = beltKey;
		
		replaceBeltKey(conveyor.inputConveyor, beltKey);
	}
}
