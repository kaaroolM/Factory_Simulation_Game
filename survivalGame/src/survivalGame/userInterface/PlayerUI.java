package survivalGame.userInterface;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import graphics.GameGraphics;
import graphics.UIRenderable;
import survivalGame.InventoryListener;
import survivalGame.Player;
import survivalGame.ItemManagement.ItemFactory;
import survivalGame.ItemManagement.ItemID;
import survivalGame.ItemManagement.ItemStack;
import survivalGame.inputs.GameKeyListener;
import survivalGame.inputs.InputListener;

public final class PlayerUI implements UIRenderable, GameKeyListener, InventoryListener{

	private boolean active = false;
	
	BufferedImage UI;
	InventorySlot[] slots = new InventorySlot[40];
	
	private Map<ItemID, Integer> inventory = new HashMap<>();
	private List<UIRenderable> allUI = new ArrayList<>();
	
	private List<InventoryListener> listeners = new ArrayList<>();
	
	private Player player;
	public PlayerUI(Player player) {
		this.player = player;
		GameGraphics.registerUI(this);
		InputListener.getInstance().registerKeyListener(this);
		UI = GameGraphics.getTextureManager().getTexture("PlayerUI");
		//allUI.add();
		int x = GameGraphics.SCREEN_WIDTH / 2 - UI.getWidth() / 2;
		int y = GameGraphics.SCREEN_HEIGHT / 2 - UI.getHeight() / 2;
		System.out.println(GameGraphics.SCREEN_WIDTH + ", " + GameGraphics.SCREEN_HEIGHT);
		for (int i = 0; i < 40; i ++) {
			int pixelX = x + (i % 5) * 70 + 40;
			int pixelY = y + (i / 5) * 70 + 80;
			slots[i] = new InventorySlot(pixelX, pixelY,this);
			allUI.add(slots[i]);
		}
		CraftingUI craftingUI = new CraftingUI(this,new Rectangle(x + 430, y + 80,UI.getWidth()/2,UI.getHeight()));
		new HotbarUI(this, player);
		allUI.add(craftingUI);
		listeners.add(craftingUI);
		new PauseMenu();
	}
	public void toggle() {
		active = !active;
	}
	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void renderUI(Graphics2D g, GameGraphics graphics) {
		int x = graphics.getSize().width / 2 - UI.getWidth() / 2;
		int y = graphics.getSize().height / 2 - UI.getHeight() / 2;
		g.drawImage(UI, x,y, graphics);
		
		for (UIRenderable ui : allUI) {
			ui.renderUI(g, graphics);
		}
	}
	
	
	
	public void organiseToSlots() {
	    for (InventorySlot slot : slots) {
	    	slot.clearItem();
	    }

	    int invCounter = 0;
	    
	    for (Map.Entry<ItemID, Integer> entry : inventory.entrySet()) {
	        ItemID item = entry.getKey();
	        int quantity = entry.getValue();

	        if (quantity <= 0) break;

	        //merge into existing slots with same item
	        for (InventorySlot slot : slots) {
	            if (slot.isEmpty() ||  !slot.getItemStack().getItem().getItemID().equals(item) ) continue;
	           
	            ItemStack stack = slot.getItemStack();
                int spaceLeft = ItemStack.MAX_STACK - stack.getQuantity();

                int toAdd = Math.min(quantity, spaceLeft);
                stack.setQuantity(stack.getQuantity() + toAdd);
                quantity -= toAdd;
                if (quantity == 0) break;
	        }
	        	
	        // if quantity left, make new stacks until there is none left 
	        while (quantity > 0 && invCounter < slots.length) {
	        	InventorySlot slot = slots[invCounter];
	            if (!slot.isEmpty()) continue;
	            
	            int toPlace = Math.min(quantity, ItemStack.MAX_STACK); 
                ItemStack newStack = new ItemStack(ItemFactory.createItem(item), toPlace);
                slot.setItemStack(newStack);
                quantity -= toPlace;
                
	            invCounter++;
	        }
	    }
	}

	public void mergeInventory(Map<ItemID, Integer> inv) {
		inv.forEach((key,value) -> inventory.merge(key,value, Integer::sum ));
		onInventoryChanged(); //including themselves
	}
	@Override
	public void onKeyPressed(int keyCode) {
		
		if (keyCode == KeyEvent.VK_I) {
			toggle();
		}
		
	}
	
	@Override
	public void onInventoryChanged() {
		organiseToSlots();
		listeners.forEach(InventoryListener::onInventoryChanged);
	}
	
	public Map<ItemID, Integer> getInventory(){
		return inventory;
	}
	
	public int getItemQuantity(ItemID item) {
		return inventory.get(item);
	}
		
	public void changeItemQuantityBy(int amount, ItemID item) {
		inventory.put(item, inventory.get(item) + amount);
	}
	public void selectSlot(InventorySlot slot) {
		
		if (slot == player.getSelectedInventorySlot()) {slot.toggleSelect(); return;}
		
		if (player.getSelectedInventorySlot() != null) player.getSelectedInventorySlot().setSelected(false);;
		player.setSelectedInventorySlot(slot);
		slot.toggleSelect();
	}
	
	public InventorySlot getSelectedSlot() {
		if(player.getSelectedInventorySlot() == null) return null;
		if (! player.getSelectedInventorySlot().isSelected()) return null;
		
		return player.getSelectedInventorySlot();
	}
	
	public void addToInventory(ItemID id, int quantity) {
		inventory.put(id, quantity);
	}
	
	public Player getPlayer() {
		return player;
	}
	@Override
	public void onKeyReleased(int keyCode) {	
	}
}
