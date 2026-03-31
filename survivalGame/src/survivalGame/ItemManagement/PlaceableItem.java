package survivalGame.ItemManagement;

import survivalGame.PlaceablesFactory;
import survivalGame.TileManagement.Tile;
import survivalGame.tileObjects.Direction;
import survivalGame.tileObjects.PlacementInfo;
import survivalGame.tileObjects.TileObject;

public final class PlaceableItem extends Item {

	public PlaceableItem(ItemID id) {
		super(id);
	}

	public TileObject place(Tile tile, Direction placementRotation) {
		TileObject placedObject = PlaceablesFactory.createPlaceable(this.getItemID(), new PlacementInfo(tile, placementRotation));
		tile.setTileObject(placedObject);
		return placedObject;
	}
}
