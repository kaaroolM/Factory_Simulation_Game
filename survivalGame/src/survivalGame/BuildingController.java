package survivalGame;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import survivalGame.ConveyorSystem.ConveyorManager;
import survivalGame.ItemManagement.PlaceableItem;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileProvider;
import survivalGame.inputs.GameKeyListener;
import survivalGame.inputs.GameMouseReleaseListener;
import survivalGame.inputs.InputListener;
import survivalGame.inputs.MouseClickListener;
import survivalGame.tileObjects.Direction;
import survivalGame.tileObjects.TileObject;
import survivalGame.tileObjects.FactoryComponents.Conveyor;
import survivalGame.tileObjects.FactoryComponents.FactoryComponent;

public final class BuildingController implements GameKeyListener, MouseClickListener, GameMouseReleaseListener  {
	private BuildMode buildMode = BuildMode.BUILD;
	
	private Direction buildRotation = Direction.NORTH;
	private Player player;
	
	private Tile lastClicked = null;
	private BuildDisplayer displayer = new BuildDisplayer(this);
	
	public BuildingController(Player player) {
		this.player = player;
		InputListener.getInstance().registerKeyListener(this);
		InputListener.getInstance().registerClickListenerToWorld(this);
		InputListener.getInstance().registerReleaseListener(this);
	}
	
	
	public void placeBuild(Tile tile, Player player) {
		//If tile is occupied, don't place build.
		if (tile.getTileObject() != null) {
			return;
		}
		//If player hasn't selected a hotbarSlot, return.
		if (player.getSelectedHotbarSlot() == null) return;
		if (!(player.getSelectedHotbarSlot().getItem() instanceof PlaceableItem toPlace)) return;
		
 		//place the item if it is placeable.
		if (toPlace == null) return;
		TileObject placedObject = toPlace.place(tile, buildRotation);
	
		//If it is a conveyor register it to conveyor manager.
		if (placedObject instanceof Conveyor conv) {
			ConveyorManager.getInstance().registerConveyor(conv);
			
		}
	}

	public void deleteBuild(Tile tile) {
		if (tile.getTileObject() instanceof FactoryComponent component) {
			component.removeObject();
			tile.setTileObject(null);
		}
	}
	@Override
	public void onKeyPressed(int keyCode) {
		//E -> rotate clockwise
		//Q -> rotate antiClockwise
		//B -> toggle building
		if (keyCode == KeyEvent.VK_E) {
    		buildRotation = buildRotation.rotatedClockwise();
    	}
    	else if(keyCode == KeyEvent.VK_Q) {
    		buildRotation = buildRotation.rotatedAntiClockwise();
    	}
    	else if (keyCode == KeyEvent.VK_B) {
    		if (buildMode == BuildMode.BUILD) buildMode = BuildMode.SELECT;
    		else {
    		buildMode = BuildMode.BUILD;
    		}
    	}
    	else if (keyCode == KeyEvent.VK_V) {
    		if (buildMode == BuildMode.DELETE) buildMode = BuildMode.SELECT;
    		else {
    		buildMode = BuildMode.DELETE;
    		}
    	}
	}

	@Override
	public void onKeyReleased(int keyCode) {
	}
	
	public BuildMode getBuildMode() {
		return buildMode;
	}
	
	public Direction getBuildRotation() {
		return buildRotation;
	}

	@Override
	public void onClick(MouseEvent e) {
		//Select Tile and place build if buidling enabled.
		lastClicked = TileProvider.pixel_AccessTile(e.getX(), e.getY());
		displayer.setLastClicked(lastClicked);
		if (buildMode != BuildMode.DELETE) return;
		
		deleteBuild(lastClicked);
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		if (buildMode == BuildMode.DELETE) {
			
			
			Tile target = TileProvider.pixel_AccessTile(e.getX(), e.getY());
			int tilesX = target.x - lastClicked.x;
			int tilesY = target.y - lastClicked.y;
			for (int x = 0; x < Math.abs(tilesX); x++) {
				for (int y = 0; y <  Math.abs(tilesY); y++) {
					Tile tile = TileProvider.world_AccessTile(
							tilesX > 0 ? lastClicked.x + x : lastClicked.x - x,
							tilesY > 0 ? lastClicked.y + y : lastClicked.y - y );
					
					if (tile == null || tile.isEmpty() || !(tile.getTileObject() instanceof FactoryComponent component)) continue;
					component.removeObject();
					tile.setTileObject(null);
				}
			}
		}
		if (lastClicked == null || buildMode != BuildMode.BUILD) return;
		Tile target = TileProvider.pixel_AccessTile(e.getX(), e.getY());
		if (target == null) return;
		
		if (lastClicked == target) {
			
			placeBuild(lastClicked,player);
			return;
		}

		List<Tile> bestPath = new Pathfinding(lastClicked, target).bestPath();
		
		for (int i = 1; i < bestPath.size(); i++) {
			Tile tile = bestPath.get(i);
			
			placeBuild(tile,player);
			
			if (i + 1 >= bestPath.size()) continue;
			
			if (bestPath.get(i+1).y == tile.y + 1) {
				buildRotation = Direction.NORTH;
			}
			else if (bestPath.get(i+1).x == tile.x + 1) {
				buildRotation = Direction.WEST;
			}
			else if (bestPath.get(i+1).y == tile.y - 1) {
				buildRotation = Direction.SOUTH;
			}
			else if (bestPath.get(i+1).x == tile.x - 1) {
				buildRotation = Direction.EAST;
			}
		}
	}
}
