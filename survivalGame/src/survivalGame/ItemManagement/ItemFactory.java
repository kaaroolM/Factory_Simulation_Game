package survivalGame.ItemManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ItemFactory {

	static Map<ItemType, Function<ItemID, Item>> itemCreators;
	
	static {
	    itemCreators = new HashMap<>();
	    itemCreators.put(ItemType.RESOURCE, id -> new ResourceItem(id) );
	    itemCreators.put(ItemType.PLACEABLE, id -> new PlaceableItem(id) );
	}
	
	public static Item createItem(ItemID itemID) {
		
		Function<ItemID, Item> func = itemCreators.get(itemID.getItemType());

		return func.apply(itemID);
		
	}
}
