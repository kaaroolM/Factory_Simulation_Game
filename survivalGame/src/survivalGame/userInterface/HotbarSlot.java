package survivalGame.userInterface;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.GameGraphics;
import graphics.UIRenderable;
import survivalGame.ItemManagement.Item;
import survivalGame.ItemManagement.ItemStack;

public final class HotbarSlot implements UIRenderable{
	//private BufferedImage UI;
	private BufferedImage selectedUI;
	final int pixelX;
	final int pixelY;
	
	private Item item; 
	private PlayerUI playerUI; 
	
	private boolean selected = false;
	public HotbarSlot(int pixelX, int pixelY, PlayerUI playerUI) {
		selectedUI = GameGraphics.getTextureManager().getTexture("SelectedSlot");
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		this.playerUI = playerUI;
	}

	@Override
	public boolean isActive() {
		return playerUI.isActive();
	}

	@Override
	public void renderUI(Graphics2D g, GameGraphics graphics) {
		if (selected) {
			g.drawImage(selectedUI,pixelX,pixelY,graphics);
			
		}

		if (item != null && playerUI.getItemQuantity(item.getItemID()) > 0) {
			UIItem.renderUIItem(g, graphics, item , playerUI.getItemQuantity(item.getItemID()), pixelX, pixelY);
		}
		
		
		
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}
	public void setSelected(boolean state) {
		selected = state;
	}
	
	public boolean isSelected() {
		return selected;
	}
}
