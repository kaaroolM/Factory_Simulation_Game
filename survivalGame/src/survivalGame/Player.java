package survivalGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import graphics.GameGraphics;
import graphics.WorldRenderable;
import graphics.ImageManipulation.ImageRotater;
import survivalGame.ConveyorSystem.IContainsConveyor;
import survivalGame.ItemManagement.ItemID;
import survivalGame.ItemManagement.PlaceableItem;
import survivalGame.ItemManagement.WorldItem;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileProvider;
import survivalGame.inputs.GameKeyListener;
import survivalGame.inputs.InputListener;
import survivalGame.inputs.MouseClickListener;
import survivalGame.tileObjects.FactoryComponents.Conveyor;
import survivalGame.userInterface.HotbarSlot;
import survivalGame.userInterface.InventorySlot;
import survivalGame.userInterface.PlayerUI;

public final class Player implements Updatable, WorldRenderable, GameKeyListener, MouseClickListener{
	
	private MovementController movement = new MovementController();
	private BuildingController buildingTool = new BuildingController(this);
	
	private PlayerUI playerUI;
	BufferedImage[] blueprints = new BufferedImage[4];
	BufferedImage deleteBlueprint = GameGraphics.getTextureManager().getTexture("DeleteBlueprint");
	
	private double pixelX = -15000;
	private double pixelY = -10000;
	private int velocity = 360 * 4;
	
	
	private Tile selectedTile;
	
	private BufferedImage character;
	
	private InventorySlot selectedInventorySlot;
	private HotbarSlot selectedHotbarSlot;
	public Player() {
		InputListener.getInstance().registerKeyListener(this);
		InputListener.getInstance().registerClickListenerToWorld(this);
		playerUI = new PlayerUI(this);
		Updater.getInstance().register(this);
		GameGraphics.registerWorldObj(this, 3);
		character = GameGraphics.getTextureManager().getTexture("Player");
	
		playerUI.addToInventory(ItemID.CONVEYOR,20);
		playerUI.addToInventory(ItemID.TREEHARVESTER,20);
		playerUI.addToInventory(ItemID.WOOD,20);
		playerUI.addToInventory(ItemID.TREEHARVESTER,1);
		
		blueprints[0] = GameGraphics.getTextureManager().getTexture("Blueprint");
		blueprints[1] = ImageRotater.rotateImage(blueprints[0], 90);
		blueprints[2] = ImageRotater.rotateImage(blueprints[0], 180);
		blueprints[3] = ImageRotater.rotateImage(blueprints[0], -90);
		playerUI.onInventoryChanged();
	}
	@Override
	public void update() {
		if (selectedTile != null) {
			selectedTile.setSelect(false);
		}
		if (selectedTile != null) {
			selectedTile.setSelect(true);
		}
		
	}
	@Override
	public void fixedUpdate(long delta) {
		//Delta is in milliseconds, so divide it by 1000 to convert it to seconds lol
		int xMove = movement.getHorizontal();
		int yMove = movement.getVertical();
		if (xMove != 0 && yMove != 0) {
			double move = Math.sqrt(xMove * xMove + yMove * yMove);
			//(movement[0] / move) to restore direction, since move just gives magnitude. 
		    pixelX += (xMove / move) * velocity * delta / 1000f;
		    pixelY += (yMove / move) * velocity * delta / 1000f;
			
		}
		else {
			pixelX += xMove * velocity * delta / 1000f;
			pixelY += yMove * velocity * delta / 1000f;
		}
		
		
	}
	public double getYCoord() {
		return pixelY;
	}
	public double getXCoord() {
		return pixelX;
	}
	@Override
	public int getY() {
		return (int) pixelY;
	}
	@Override
	public boolean isActive() {
		return true;
	}
	
	@Override
	public void render(Graphics2D g, GameGraphics graphics) {
		int playerSize = 25;
		g.setColor(new Color(250,0,90,122));
		g.fillOval(-(int)pixelX - playerSize,-(int)pixelY - playerSize, 50, 50); 
		
		g.drawImage(character, -(int)pixelX - playerSize, -(int)pixelY - playerSize, graphics);
		
		renderBlueprint(g,graphics);
		renderDeleteBlueprint(g,graphics);
	}
	
	private void renderDeleteBlueprint(Graphics2D g, GameGraphics graphics) {
		if (buildingTool.getBuildMode() != BuildMode.DELETE) return;
		Tile tile = TileProvider.pixel_AccessTile(InputListener.getInstance().getMouseX(), InputListener.getInstance().getMouseY());
		if (tile == null ) return;
		
		BufferedImage texture = deleteBlueprint;
		g.drawImage(texture, (int) (tile.pixelX), (int) (tile.pixelY), graphics);
	}
	private void renderBlueprint(Graphics2D g, GameGraphics graphics) {
		if (buildingTool.getBuildMode() != BuildMode.BUILD) return;
		if (selectedHotbarSlot == null) return;
		
		if (!(selectedHotbarSlot.getItem() instanceof PlaceableItem)) return;
		Tile tile = TileProvider.pixel_AccessTile(InputListener.getInstance().getMouseX(), InputListener.getInstance().getMouseY());
		if (tile == null || !tile.isEmpty()) return;
		BufferedImage texture = switch (buildingTool.getBuildRotation()) {
		case NORTH ->
			blueprints[0];
		case EAST ->
			blueprints[1];
		case SOUTH ->
			blueprints[2];
		case WEST ->
			blueprints[3];
		};
		
		if (texture == null) return;
		g.drawImage(texture, (int) (tile.pixelX), (int) (tile.pixelY), graphics);
		g.drawImage(getSelectedHotbarSlot().getItem().getTexture(), tile.pixelX + 75, tile.pixelY + 75,25,25, graphics);
	}
	
	public Tile getSelectedTile() {
		return selectedTile;
	}
	public void selectTile(Tile selectedTile) {
		this.selectedTile = selectedTile;
	}
	
	public void collectItems() {
		Tile[] tiles = new Tile[9];
		tiles[0] = getTile(0,0);
		tiles[1] = getTile(0,100);
		tiles[2] = getTile(100,0);
		tiles[3] = getTile(100,100);
		tiles[4] = getTile(-100,0);
		tiles[5] = getTile(0,-100);
		tiles[6] = getTile(-100,-100);
		tiles[7] = getTile(100,-100);
		tiles[8] = getTile(-100,100);
		Map<ItemID, Integer> tempInventory = new HashMap<>();
		for (Tile tile : tiles) {
			if ( !(tile.getTileObject() instanceof IContainsConveyor containsConveyor) ) continue;
			
			Conveyor conv = containsConveyor.getConveyor();
			if (!conv.isEmpty()) {
				WorldItem worldItem = conv.collectItem();
				tempInventory.merge(worldItem.getItem().getItemID(), 1, Integer::sum); 
				// same as tempInventory.put(item,tempInventory.get(item) + 1)	
			}
		}
		playerUI.mergeInventory(tempInventory);
	}
	/**
	 * @param xOffset in pixel coordinates
	 * @param yOffset in pixel coordinates
	 * @return a reference to Tile instance
	 */
	private Tile getTile(int xOffset, int yOffset) {
		int x = (int) ((GameGraphics.getOriginOffset()[0] + xOffset )/ GameGraphics.TILESIZE);
		int y = (int) ((GameGraphics.getOriginOffset()[1] + yOffset ) / GameGraphics.TILESIZE);
		
		return TileProvider.world_AccessTile(x, y);
	}
	
	public InventorySlot getSelectedInventorySlot() {
		return selectedInventorySlot;
	}
	public void setSelectedInventorySlot(InventorySlot selectedInventorySlot) {
		this.selectedInventorySlot = selectedInventorySlot;
	}
	public HotbarSlot getSelectedHotbarSlot() {
		return selectedHotbarSlot;
	}
	public void setSelectedHotbarSlot(HotbarSlot selectedHotbarSlot) {
		this.selectedHotbarSlot = selectedHotbarSlot;
	}
	@Override
	public void onKeyPressed(int keyCode) {
		switch (keyCode) {
			case KeyEvent.VK_F:
				collectItems();	
		}
	}
	
	private Tile getClickedTile(int x, int y) {
		Tile tile = TileProvider.pixel_AccessTile(x, y);
		return tile;
	}
	
	@Override
	public void onClick(MouseEvent e) {
		if (selectedTile != null) {
			selectedTile.setSelect(false);
		}
		
		selectedTile = getClickedTile(e.getX(), e.getY());
	}

	public BuildMode getBuildingMode() {
		return buildingTool.getBuildMode();
	}
	@Override
	public void onKeyReleased(int keyCode) {
		
	}
	

}
