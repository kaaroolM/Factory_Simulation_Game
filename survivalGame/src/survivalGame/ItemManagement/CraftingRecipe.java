package survivalGame.ItemManagement;

import java.util.Map;

public final class CraftingRecipe {
	
	private Map<ItemID, Integer> recipe;
	private ItemID outputItem;
	
	public CraftingRecipe(Map<ItemID, Integer> recipe, ItemID outputItem) {
		this.recipe = recipe;
		this.outputItem = outputItem;
	}
	
	public ItemID craftItem(Map<ItemID, Integer> inventory) {
		
		if (!canCraft(inventory)) return null;
		
		//remove required items
		recipe.forEach((item, quantity) -> inventory.merge(item, quantity, (a,b) -> a - b ));
		inventory.merge(outputItem, 1, Integer::sum);
		return outputItem;
		
	}
	public boolean canCraft(Map<ItemID, Integer> inventory) {
		
		 for (Map.Entry<ItemID, Integer> entry : recipe.entrySet()) {
			 int available = inventory.getOrDefault(entry.getKey(), 0);
			 if (available < entry.getValue()) return false;
		 }

		return true;
	}
	
	public ItemID getOutputItem() {
		return outputItem;
	}
	
	public void printRecipe() {
		 for (Map.Entry<ItemID, Integer> entry : recipe.entrySet()) {
			 System.out.println(entry.getKey() + ": " + entry.getValue());
		 }
	}
}
