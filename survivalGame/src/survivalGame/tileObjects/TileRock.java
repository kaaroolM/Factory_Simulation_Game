package survivalGame.tileObjects;

import graphics.GameGraphics;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileObjectID;

public final class TileRock extends TileObject {
	
	public static final TileObjectID ID = TileObjectID.ROCK;
	
	public TileRock(Tile parentTile) {
		super(parentTile);
		GameGraphics.registerWorldObj(this, 2);
		super.setTexture("Rock", GameGraphics.getTextureManager());
	}

	@Override
	public int getY() {
		return super.parentTile.y;
	}

	@Override
	public boolean isActive() {
		return super.parentTile.isActive();
	}
	
	@Override
	public TileObjectID getTileObjectID() {
		return ID;
	}
	
}
