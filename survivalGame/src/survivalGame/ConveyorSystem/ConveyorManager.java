package survivalGame.ConveyorSystem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphics.GameGraphics;
import graphics.WorldRenderable;
import survivalGame.ITickable;
import survivalGame.TickManager;
import survivalGame.tileObjects.FactoryComponents.Conveyor;

public final class ConveyorManager implements ITickable{

	private ConveyorNetworkSystem networkSystem = new ConveyorNetworkSystem();
	
	private static final ConveyorManager ConveyorManagerInstance = new ConveyorManager();
	
	/**
	 * Maps beltKey to conveyor Leaf. 
	 * A conveyor leaf is the end of a conveyor belt sequence, treated like a linked list.
	 */
	
	//This will increment for each new conveyor leef you make.
	
	public static ConveyorManager getInstance() {
        return ConveyorManagerInstance;
    }
	
	public ConveyorManager() {
		TickManager.getInstance().register(this);
	}
	 
	
	@Override
	public void onTick() {
		
		List<Conveyor> tails = networkSystem.getTails();
		
		//Use DFS for each conveyor belt. Reversed because usually people make conveyor belts chronologically, so in numerical order
		for (Conveyor c : tails.reversed()) {
			traverse(c, c);
		}
	}
	
	private void traverse(Conveyor conveyor, Conveyor root) {
		
		//process first then pass to target.
		conveyor.process();
		networkSystem.conveyorPassToTarget(conveyor, conveyor.heldItem); //Updates the conveyor, pass item along
		
		//System.out.println("Traversed through " +  c.parentTile.x + ", " + c.parentTile.y);
		
		if (conveyor.inputConveyor == null || conveyor.inputConveyor.beltSequence != root.beltSequence || conveyor.inputConveyor == root) return;
		
		traverse(conveyor.inputConveyor, root);
	}

	public void registerConveyor(Conveyor conveyor) {
		networkSystem.initializeConveyor(conveyor);
	}

	public ConveyorNetworkSystem getNetworkSystem() {
		return networkSystem;
	}
	
	public void deleteConveyor(Conveyor conveyor) {
		networkSystem.deleteConveyor(conveyor);
	}

}
