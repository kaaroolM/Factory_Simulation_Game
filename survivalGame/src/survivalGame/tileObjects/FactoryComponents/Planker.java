package survivalGame.tileObjects.FactoryComponents;

import java.awt.Graphics2D;
import java.util.EnumSet;

import graphics.GameGraphics;
import graphics.TextureManager;
import graphics.ImageManipulation.ImageRotater;
import survivalGame.ActionTimer;
import survivalGame.ItemManagement.ItemID;
import survivalGame.ItemManagement.WorldItem;
import survivalGame.TileManagement.Tile;
import survivalGame.tileObjects.Direction;

public final class Planker extends Conveyor{
	
	private ActionTimer actionTimer;
	public Planker(Tile parentTile, Direction rotation) {
		super(parentTile, rotation);
		disableSpritemask();
		TextureManager textureManager = GameGraphics.getTextureManager();
		
		switch (rotation) {
		case NORTH ->
			super.setTexture(textureManager.getTexture("Planker"));
		case EAST ->
			super.setTexture(ImageRotater.rotateImage(textureManager.getTexture("Planker"), 90));
		case SOUTH ->
			super.setTexture(ImageRotater.rotateImage(textureManager.getTexture("Planker"), 180));
		case WEST ->
			super.setTexture(ImageRotater.rotateImage(textureManager.getTexture("Planker"), -90));
		}
		
		inputBlacklist = EnumSet.of(rotation.rotatedAntiClockwise(), rotation.rotatedClockwise());

		actionTimer = new ActionTimer(3);
		GameGraphics.registerWorldObj(this, 2);
	}

	@Override
	public void process() {
		if (actionTimer.actionTick()) {
			lock(false);
		}
		else {
			lock(true);
		}

		WorldItem planks = collectItem();
		if (planks == null) return;
		planks.getItem().changeItemInto(ItemID.WOOD); //now becomes planks
		planks.setActive(true);
		recieveWorldItem(planks);
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public void removeObject() {	
		GameGraphics.removeWorldObj(this, 2);
		super.removeObject();
	}

	@Override
	public void render(Graphics2D g, GameGraphics graphics) {
		int x = this.getParentTile().pixelX;
		int y = this.getParentTile().pixelY;
		g.drawImage(super.texture ,x ,y , graphics);
	}
	
	@Override
	public Conveyor getConveyor() {
		return this;
	}

}
