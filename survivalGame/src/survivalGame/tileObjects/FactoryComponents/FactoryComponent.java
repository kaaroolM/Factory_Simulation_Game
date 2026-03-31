package survivalGame.tileObjects.FactoryComponents;

import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileProvider;
import survivalGame.tileObjects.Direction;
import survivalGame.tileObjects.TileObject;

public sealed abstract class FactoryComponent extends TileObject
		permits Conveyor, RockDriller, TreeHarvester {
	
	private Direction rotation;
	
	public void process() {
		// default: do nothing
	}
	
	protected Tile parentTile;
	
	public FactoryComponent(Tile parentTile, Direction rotation) {
		super(parentTile);
		this.parentTile = parentTile;
		this.setRotation(rotation);
	}
	public FactoryComponent(Tile parentTile) {
		super(parentTile);
		this.parentTile = parentTile;
	}
	
	/**
	 * This function returns the Tile in the direction of rotation.
	 * @param rotation or direction to get the next Tile from.
	 * @return the {@link Tile} that rotation is pointing towards.
	 */
	public Tile getTargetTile(Direction rotation) {
		return switch (rotation){
		case NORTH ->
			TileProvider.world_AccessTile(getParentTile().x,getParentTile().y - 1);
		case EAST ->
			TileProvider.world_AccessTile(getParentTile().x + 1, getParentTile().y);
		case SOUTH ->
			TileProvider.world_AccessTile(getParentTile().x, getParentTile().y + 1);
		case WEST ->
			TileProvider.world_AccessTile(getParentTile().x - 1, getParentTile().y);
		};
	}
	
	public Direction getRotation() {
		return rotation;
	}
	
	public Tile getParentTile() {
		return parentTile;
	}
	protected void setRotation(Direction rotation) {
		this.rotation = rotation;
	}
	
	public abstract void removeObject();
	
}
