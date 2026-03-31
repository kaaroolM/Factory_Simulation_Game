package survivalGame.ItemManagement;

import java.awt.Graphics2D;

import graphics.GameGraphics;
import graphics.WorldRenderable;
import survivalGame.Updatable;
import survivalGame.Updater;
import survivalGame.TileManagement.Tile;

public final class WorldItem implements WorldRenderable, Updatable {

	private int pixelX;
	private int pixelY;
	
	private int targetX;
	private int targetY;
	private float startX;
	private float startY;
	private float elapsedTime;
	private boolean moving;
	private Item item;
	
	private boolean active = true;
	
	public WorldItem(Item item, int pixelX, int pixelY) {
		this.item = item;
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		
		GameGraphics.registerWorldObj(this, 4);
		
		Updater.getInstance().register(this);
	}
	
	@Override
	public int getY() {
		return pixelY;
	}

	@Override
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean state) {
		active = state;
	}
	
	public Item getItem() {
		return item;
	}
	
	@Override
	public void render(Graphics2D g, GameGraphics graphics) {
		if (!active) return;
		g.drawImage(item.getTexture(),pixelX,pixelY,null);
	}
	
	public void fixToTile(Tile tile) {
		lerpToTile(tile);
	}
	
	public void lerpToTile(Tile tile) {
		startX = pixelX;
	    startY = pixelY;

	    targetX = tile.pixelX - item.getTexture().getWidth() / 2 + tile.tileSize / 2;
	    targetY = tile.pixelY - item.getTexture().getHeight() / 2 + tile.tileSize / 2;

	    elapsedTime = 0;
	    moving = true;
	}
	public float lerp(float a, float b, float t) {
	    return a + t * (b - a);
	}
	
	public void deleteItem() {
		Updater.getInstance().remove(this);
		GameGraphics.removeWorldObj(this, 4);
	}
	@Override
	public void fixedUpdate(long delta) {
		if (!active) return;
		if (moving && elapsedTime < 0.1f) {
			elapsedTime += delta / 1000f;
			
			float t = Math.min(elapsedTime / 0.1f, 1);
			pixelX = (int) lerp(startX,targetX,t);
			pixelY = (int) lerp(startY,targetY,t);
			
			if (t >= 1f) moving = false;  
		}
	}
	
	@Override
	public void update() {
	}
}
