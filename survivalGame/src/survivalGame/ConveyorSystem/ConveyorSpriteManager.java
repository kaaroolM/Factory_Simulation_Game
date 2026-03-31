package survivalGame.ConveyorSystem;

import java.util.Map;

import graphics.GameGraphics;
import survivalGame.tileObjects.Direction;
import survivalGame.tileObjects.FactoryComponents.Conveyor;

public final class ConveyorSpriteManager {
	//integers used for input. North is 0001 for example 
	public static final int NORTH = Direction.NORTH.getRotationMask();
	public static final int EAST  = Direction.EAST.getRotationMask();
	public static final int SOUTH = Direction.SOUTH.getRotationMask();
	public static final int WEST  = Direction.WEST.getRotationMask();
	
	//Integers shifted by 4 to represent output. North_Out is now 00010000
	public static final int NORTH_out = (NORTH << 4);
	public static final int EAST_out = (EAST << 4);
	public static final int SOUTH_out = (SOUTH << 4);
	public static final int WEST_out = (WEST << 4);
	
	//These maps keys are formatted in: OutputDirection_InputDirections
	private static final Map<Integer, String> conveyorSpritemap = (Map<Integer, String>) Map.ofEntries(
			Map.entry(EAST_out, "ConveyorE"),
			Map.entry(WEST_out, "ConveyorW"),
			Map.entry(NORTH_out, "ConveyorN"),
			Map.entry(SOUTH_out, "ConveyorS"),
			Map.entry(EAST_out  | EAST, "ConveyorE"),
			Map.entry(WEST_out  | WEST, "ConveyorW"),
			Map.entry(NORTH_out | NORTH, "ConveyorN"),
			Map.entry(SOUTH_out | SOUTH, "ConveyorS"),
			
			//Curved
			Map.entry(EAST_out  | NORTH, "ConveyorNE"),
		    Map.entry(WEST_out  | NORTH, "ConveyorNW"),
		    Map.entry(NORTH_out | EAST,  "ConveyorWN"),
		    Map.entry(SOUTH_out | EAST,  "ConveyorWS"),
		    Map.entry(NORTH_out | WEST,  "ConveyorEN"),
		    Map.entry(SOUTH_out | WEST,  "ConveyorES"),
		    Map.entry(EAST_out  | SOUTH, "ConveyorSE"),
		    Map.entry(WEST_out  | SOUTH, "ConveyorSW"),

		    // T-junctions
		    Map.entry(EAST_out  | (EAST  | NORTH),  "ConveyorT_NE"),
		    Map.entry(WEST_out  | (NORTH | WEST),  "ConveyorT_NW"),
		    Map.entry(NORTH_out | (NORTH | EAST),  "ConveyorT_EN"),
		    Map.entry(SOUTH_out | (EAST  | SOUTH),  "ConveyorT_ES"),
		    Map.entry(NORTH_out | (NORTH | WEST),  "ConveyorT_WN"),
		    Map.entry(SOUTH_out | (WEST  | SOUTH),  "ConveyorT_WS"),
		    Map.entry(EAST_out  | (SOUTH | EAST),  "ConveyorT_SE"),
		    Map.entry(WEST_out  | (SOUTH | WEST),  "ConveyorT_SW"),
		    
		    Map.entry(WEST_out  | (NORTH | SOUTH), "ConveyorT_VE"),
		    Map.entry(EAST_out  | (NORTH | SOUTH), "ConveyorT_VW"),
		    Map.entry(NORTH_out | (WEST  | EAST),   "ConveyorT_HN"),
		    Map.entry(SOUTH_out | (WEST  | EAST),   "ConveyorT_HS")
		);
	
	/**
	 * changes sprite according to the added input direction
	 * @param inputRotation is the input direction added.
	 */
	public static void changeSprite(Conveyor conveyor, Direction inputRotation) {
		if (conveyor == null || conveyor.spriteMask < 0) return;
		
		conveyor.spriteMask |=  inputRotation.getRotationMask();
		
		if ( !conveyorSpritemap.containsKey(conveyor.spriteMask) ) return;
		conveyor.setTexture(conveyorSpritemap.get(conveyor.spriteMask), GameGraphics.getTextureManager());
	}
	
	public static void updateSprite(Conveyor conveyor) {
		if ( !conveyorSpritemap.containsKey(conveyor.spriteMask) ) return;
		conveyor.setTexture(conveyorSpritemap.get(conveyor.spriteMask), GameGraphics.getTextureManager());
	}
	
}
