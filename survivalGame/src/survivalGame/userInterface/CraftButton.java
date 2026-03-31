package survivalGame.userInterface;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import graphics.GameGraphics;
import graphics.UIClickable;
import survivalGame.InventoryListener;
import survivalGame.ItemManagement.CraftingRecipe;
import survivalGame.inputs.InputListener;

public final class CraftButton implements UIClickable, InventoryListener{

	private int pixelX;
	private int pixelY;
	private int itemPixelY; 
	private int itemPixelX; 
	private CraftingUI craftingUI;
	private CraftingRecipe recipe;
	
	//active meaning craftable
	private BufferedImage buttonActive;
	private BufferedImage buttonInactive;
	private boolean craftable = false;
	
	public CraftButton(int pixelX, int pixelY, CraftingUI craftingUI, CraftingRecipe recipe) {
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		this.craftingUI = craftingUI;
		this.recipe = recipe;
		InputListener.getInstance().registerClickableUI(this);
		
		buttonActive = GameGraphics.getTextureManager().getTexture("ButtonActive");
		buttonInactive = GameGraphics.getTextureManager().getTexture("ButtonInactive");
		BufferedImage texture = GameGraphics.getTextureManager().getTexture(recipe.getOutputItem().getIdString());
		itemPixelY = pixelY + buttonActive.getHeight() / 2 - texture.getHeight() / 2; 
		itemPixelX = pixelX + buttonActive.getWidth() / 2 - texture.getWidth() / 2;
	}
	@Override
	public boolean isActive() {
		return craftingUI.isActive();
	}
	@Override
	public void renderUI(Graphics2D g, GameGraphics graphics) {
		g.drawImage(craftable ? buttonActive : buttonInactive, pixelX, pixelY, null);
		g.drawImage(GameGraphics.getTextureManager().getTexture(recipe.getOutputItem().getIdString()), itemPixelX, itemPixelY , null);
	}

	private boolean canCraft() {
		return recipe.canCraft(craftingUI.getInventory());
	}
	@Override
	public void onInventoryChanged() {
		craftable = canCraft();
		System.out.println("Craftable? "  + craftable);
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(pixelX,pixelY,buttonActive.getWidth(),buttonActive.getHeight());
	}
	
	//crafts item on click
	@Override 
	public void onClick() {
		if (!craftable)	return;
		
		recipe.craftItem(craftingUI.getInventory());
		craftingUI.getPlayerUI().onInventoryChanged();
		craftingUI.printInventory();
		
	}
}
