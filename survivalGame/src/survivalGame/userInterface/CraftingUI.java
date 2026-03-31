package survivalGame.userInterface;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import graphics.GameGraphics;
import graphics.UIRenderable;
import survivalGame.InventoryListener;
import survivalGame.Recipes;
import survivalGame.ItemManagement.Item;
import survivalGame.ItemManagement.ItemID;

public final class CraftingUI implements UIRenderable, InventoryListener {
	
	private Map<ItemID, Integer> inventory;
	private PlayerUI playerUI;
	private List<CraftButton> craftButtons = new ArrayList<>();
	public CraftingUI(PlayerUI playerUI,Rectangle bounds) {
		
		this.inventory = playerUI.getInventory();
		this.playerUI = playerUI;
	
		//1300,470 are bounds corner
		
		
		craftButtons.add(new CraftButton(bounds.x,bounds.y, this, Recipes.CONVEYOR.getRecipe()));
		craftButtons.add(new CraftButton(bounds.x + 100,bounds.y, this, Recipes.TREEHARVESTER.getRecipe()));
		craftButtons.add(new CraftButton(bounds.x + 200,bounds.y, this, Recipes.PLANKER.getRecipe()));		
		craftButtons.add(new CraftButton(bounds.x,bounds.y + 100, this, Recipes.CONVEYORSPLITTER_R.getRecipe()));	
		craftButtons.add(new CraftButton(bounds.x + 100,bounds.y + 100, this, Recipes.CONVEYORSPLITTER_L.getRecipe()));	
		craftButtons.add(new CraftButton(bounds.x + 200,bounds.y + 100, this, Recipes.ROCKDRILLER.getRecipe()));	
	}
	@Override
	public boolean isActive() {
		return playerUI.isActive();
	}

	@Override
	public void renderUI(Graphics2D g, GameGraphics graphics) {
		for (CraftButton button : craftButtons) {
			button.renderUI(g, graphics);
		}
	}


	public Map<ItemID, Integer> getInventory() {
		return inventory;
	}
	public void printInventory() {
		 for (Map.Entry<ItemID, Integer> entry : inventory.entrySet()) {
			 System.out.println(entry.getKey() + ": " + entry.getValue());
		 }
	}
	
	@Override
	public void onInventoryChanged() {
		System.out.println("Inventory changed");
		craftButtons.forEach(CraftButton::onInventoryChanged);
	}
	
	public PlayerUI getPlayerUI() {
		return playerUI;
	}
	

}
