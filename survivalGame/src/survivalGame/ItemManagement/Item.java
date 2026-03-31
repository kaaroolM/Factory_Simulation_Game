package survivalGame.ItemManagement;

import java.awt.image.BufferedImage;
import graphics.GameGraphics;

public sealed abstract class Item permits PlaceableItem, ResourceItem {
	
	private ItemID id;
	private BufferedImage texture;
	
	
	public Item(ItemID itemID) {
		id = itemID;
		texture = GameGraphics.getTextureManager().getTexture(itemID.getIdString());
	}
	
	
	public BufferedImage getTexture() {
		return texture;
	}
	
	public ItemID getItemID()
	{
		return id;
	}
	
	/**
	 * Sets the itemID and changes texture of the item
	 * @param itemID
	 */
	public void changeItemInto(ItemID itemID) {
		id = itemID;
		texture = GameGraphics.getTextureManager().getTexture(itemID.getIdString());
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Item item)) return false;
		
		return this.getItemID().equals(item.getItemID());

	}
	
    @Override
    public int hashCode() {
        return getItemID().hashCode();
    }

}
