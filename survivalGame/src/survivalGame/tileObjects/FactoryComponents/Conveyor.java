package survivalGame.tileObjects.FactoryComponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.EnumSet;

import graphics.GameGraphics;
import survivalGame.IItemReciever;
import survivalGame.ConveyorSystem.BeltSequence;
import survivalGame.ConveyorSystem.ConveyorManager;
import survivalGame.ConveyorSystem.IContainsConveyor;
import survivalGame.ItemManagement.Item;
import survivalGame.ItemManagement.WorldItem;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileObjectID;
import survivalGame.tileObjects.Direction;

public sealed class Conveyor extends FactoryComponent implements IContainsConveyor, IItemReciever permits ConveyorSplitter, Planker {
	
	public static final TileObjectID ID = TileObjectID.CONVEYOR;
	
	private Tile targetTile = super.getTargetTile(getRotation());
	
	//input conveyor is always the same beltSequence as the current one.
	public Conveyor inputConveyor;
	//Target conveyor doesn't rely on beltSequence. 
	public Conveyor targetConveyor;
	public WorldItem heldItem;
	public BeltSequence beltSequence;
	public int spriteMask;
	protected boolean locked;
	protected EnumSet<Direction> inputBlacklist;
	
	public Conveyor(Tile parentTile, Direction rotation) {
		super(parentTile, rotation);
		GameGraphics.registerWorldObj(this, 2);
	}
	
	@Override
	public void removeObject() {
		//Coupling needs to be reworked later, likely using interfaces and listeners. 
		ConveyorManager.getInstance().deleteConveyor(this);
		GameGraphics.removeWorldObj(this, 2);
		
		if (heldItem == null) return;
		heldItem.deleteItem();
		heldItem = null;
	}

	public EnumSet<Direction> getInputBlackList(){
		return inputBlacklist;
	}

	public boolean isPointingAt(Tile tile) {
		return targetTile == tile;
	}
	public void addInputConveyor(Conveyor conveyor) {
		if (inputConveyor == null) {
			inputConveyor = conveyor;
		}
	}
	
	public boolean hasInputConveyor() {
		return inputConveyor != null;
	}
	
	public boolean hasTargetConveyor() {
		return targetConveyor != null;
	}
	
	public WorldItem collectItem() {
		WorldItem item = heldItem;
		if (heldItem == null) return null;
		heldItem.setActive(false);
		heldItem = null;
		return item;
	}
	
	public void recieveWorldItem(WorldItem item) {
		item.fixToTile(this.getParentTile());
		heldItem = item;
	}
	
	public boolean isLocked() {
		return locked;
	}
	public boolean isEmpty() {
		return heldItem == null;
	}
	
	@Override
	public void render(Graphics2D g, GameGraphics graphics) {
		int x = this.getParentTile().pixelX;
		int y = this.getParentTile().pixelY;
		g.drawImage(super.texture ,x ,y , graphics);
		//g.setColor(Color.WHITE);
		//Font largeFont = new Font("Arial", Font.BOLD, 25);
		//g.setFont(largeFont);
		//g.drawString(beltSequence.getBeltKey() + "", x, y);
	}
	
	@Override
	public int getY() {
		return 0;
	}

	public Tile getTargetTile() {
		return targetTile;
	}

	@Override
	public Conveyor getConveyor() {
		return this;
	}
	
	protected void lock(boolean state) {
		locked = state;
	}

	@Override
	public void recieveItem(Item item) {
		//new worldItem instantiated in order to display the item visually
		recieveWorldItem(new WorldItem(item,getParentTile().pixelX,getParentTile().pixelY));
	}
	@Override
	public boolean canRecieve() {
		return isEmpty() && !locked;
	}
	
	public boolean canPassToTarget() {
		if (heldItem == null || isLocked()) {
			return false;
		}
		if ( !targetConveyor.isEmpty() ) return false;
		return true;
	}
	
	/**
	 * Disables regular conveyor textures
	 */
	protected void disableSpritemask() {
		spriteMask = -20;
	}
	
	@Override
	public TileObjectID getTileObjectID() {
		return ID;
	}
}
