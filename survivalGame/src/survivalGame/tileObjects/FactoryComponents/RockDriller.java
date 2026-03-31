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
import survivalGame.tileObjects.TileRock;

public final class RockDriller extends FactoryComponent implements ITickable {
	
	public static final TileObjectID ID = TileObjectID.ROCK_DRILLER;
	
	private TileRock targetRock;
	private IItemReciever targetOutput;
	
	private Tile targetTile;
	private Tile behindTile;
	private final ActionTimer actionTime = new ActionTimer(4);
	
	public RockDriller(Tile parentTile, Direction rotation) {
		super(parentTile, rotation);
		
		GameGraphics.registerWorldObj(this, 2);
		TickManager.getInstance().register(this);
		TextureManager textureManager = GameGraphics.getTextureManager();
		switch (rotation) {
		case NORTH ->
			super.setTexture(ImageRotater.rotateImage(textureManager.getTexture("RockDriller"), 90));
		case EAST ->
			super.setTexture(ImageRotater.rotateImage(textureManager.getTexture("RockDriller"), 180));
		case SOUTH ->
			super.setTexture(ImageRotater.rotateImage(textureManager.getTexture("RockDriller"), -90));
		case WEST ->
			super.setTexture("RockDriller", textureManager);
		}
		
		Direction opposite = rotation.rotatedClockwise().rotatedClockwise();
		targetTile = getTargetTile(rotation);
		behindTile = getTargetTile(opposite);
		
		if (targetTile.getTileObject() instanceof TileRock rock) {
			targetRock = rock;
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
		if (targetRock == null || targetOutput == null) return;
		action();
		
	}

	private void action() {
		if (! targetOutput.canRecieve()) return;
		Item item = ItemFactory.createItem(ItemID.ROCK);
		System.out.println("----------LOG OUTPUT");
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
