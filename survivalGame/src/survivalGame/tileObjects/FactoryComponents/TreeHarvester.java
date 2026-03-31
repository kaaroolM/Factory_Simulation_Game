package survivalGame.tileObjects.FactoryComponents;

import java.awt.Graphics2D;

import graphics.GameGraphics;
import graphics.TextureManager;
import graphics.ImageManipulation.ImageRotater;
import survivalGame.ActionTimer;
import survivalGame.IItemReciever;
import survivalGame.ITickable;
import survivalGame.TickManager;
import survivalGame.ConveyorSystem.ConveyorSpriteManager;
import survivalGame.ItemManagement.Item;
import survivalGame.ItemManagement.ItemFactory;
import survivalGame.ItemManagement.ItemID;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileObjectID;
import survivalGame.tileObjects.Direction;
import survivalGame.tileObjects.TileTree;

public final class TreeHarvester extends FactoryComponent implements ITickable {

	public static final TileObjectID ID = TileObjectID.TREE_HARVESTER;
	
	private TileTree targetTree;
	private IItemReciever targetOutput;
	
	private Tile targetTile;
	private Tile behindTile;
	private final ActionTimer actionTime = new ActionTimer(3);
	
	public TreeHarvester(Tile parentTile, Direction rotation) {
		super(parentTile, rotation);
		
		GameGraphics.registerWorldObj(this, 2);
		TickManager.getInstance().register(this);
		TextureManager textureManager = GameGraphics.getTextureManager();
		switch (rotation) {
		case NORTH ->
			super.setTexture(ImageRotater.rotateImage(textureManager.getTexture("TreeHarvester"), 90));
		case EAST ->
			super.setTexture(ImageRotater.rotateImage(textureManager.getTexture("TreeHarvester"), 180));
		case SOUTH ->
			super.setTexture(ImageRotater.rotateImage(textureManager.getTexture("TreeHarvester"), -90));
		case WEST ->
			super.setTexture("TreeHarvester", textureManager);
		}
		
		Direction opposite = rotation.rotatedClockwise().rotatedClockwise();
		targetTile = getTargetTile(rotation);
		behindTile = getTargetTile(opposite);
		
		if (targetTile.getTileObject() instanceof TileTree tree) {
			targetTree = tree;
		}
		checkForOutput();
	}
	
	/**
	 * checks for any object that can recieve the item it dispenses.
	 */
	public void checkForOutput() {
		if (behindTile.getTileObject() instanceof IItemReciever output) {
			targetOutput = output;
		}
		if (targetOutput instanceof Conveyor conv) {
			Direction opposite = getRotation().rotatedClockwise().rotatedClockwise();
			ConveyorSpriteManager.changeSprite(conv, opposite);
		}
	}
	@Override
	public void onTick() {
		checkForOutput();
		
		if (! actionTime.actionTick()) return;
		if (targetTree == null || targetOutput == null) return;
		action();	
	}

	private void action() {
		if (! targetOutput.canRecieve()) return;
		Item item = ItemFactory.createItem(ItemID.LOG);
		targetOutput.recieveItem(item);
	}
	
	@Override
	public int getY() {
		return getParentTile().y;
	}
	
	@Override
	public void render(Graphics2D g, GameGraphics graphics) {
		int x = this.getParentTile().pixelX;
		int y = this.getParentTile().pixelY;
		g.drawImage(super.texture ,x ,y , graphics);
	}

	@Override
	public void removeObject() {
		GameGraphics.removeWorldObj(this, 2);
		TickManager.getInstance().remove(this);
	}

	@Override
	public TileObjectID getTileObjectID() {
		return ID;
	}
	

}
