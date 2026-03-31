package survivalGame.tileObjects;

import survivalGame.TileManagement.Tile;

public final class PlacementInfo {
	//Wrapper class for PlaceableFactory
	//Did this since the Tile placed is yet to be decided
	//Only the class which is going to be instantiated is decided. 
	private final Tile tile;
	private final Direction direction;
	
	public PlacementInfo(Tile tile, Direction direction) {
		this.tile = tile;
		this.direction = direction;
	}

	public Tile getTile() {
		return tile;
	}

	public Direction getDirection() {
		return direction;
	}
}
