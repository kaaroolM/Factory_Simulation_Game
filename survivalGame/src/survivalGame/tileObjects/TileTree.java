package survivalGame.tileObjects;


import graphics.GameGraphics;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileObjectID;

public final class TileTree extends TileObject{
	
	public static final TileObjectID ID = TileObjectID.TREE;
	
	public TileTree(Tile parentTile) {
		super(parentTile);
		super.verticalOffset = (int) -(50 + Math.random() * 25);
		GameGraphics.registerWorldObj(this, 2);
		super.setTexture("Tree", GameGraphics.getTextureManager());
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
