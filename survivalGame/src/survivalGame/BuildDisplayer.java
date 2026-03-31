package survivalGame;

import java.awt.Graphics2D;

import graphics.GameGraphics;
import graphics.WorldRenderable;
import survivalGame.TileManagement.Tile;
import survivalGame.TileManagement.TileProvider;
import survivalGame.inputs.InputListener;

public final class BuildDisplayer implements WorldRenderable {

	private Tile lastClicked;
	private BuildingController controller;
	public BuildDisplayer(BuildingController controller) {
		this.controller = controller;
		GameGraphics.registerWorldObj(this, getY());
	}
	@Override
	public int getY() {
		//Its a highlight in the world.
		return Integer.MAX_VALUE;
	}

	public void setLastClicked(Tile tile) {
		lastClicked = tile;
	}
	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void render(Graphics2D g, GameGraphics graphics) {
		if (controller.getBuildMode() == BuildMode.SELECT) return;
		if (!InputListener.getInstance().leftButtonHeld || lastClicked == null) {lastClicked = null; return;}
		int pixelX = (int) ((InputListener.getInstance().getMouseX() - GameGraphics.SCREEN_WIDTH / 2) / GameGraphics.getCameraZoom());
		int pixelY = (int) ((InputListener.getInstance().getMouseY()  - GameGraphics.SCREEN_HEIGHT / 2) / GameGraphics.getCameraZoom());

		/*
		 * A vector based solution, where A is clicked point and B is target point.
		 * O is camera origin, R is world origin. 
		 */
		int OB_X = pixelX;
		int OB_Y = pixelY;
		
		int RO_X = GameGraphics.getOriginOffset()[0];
		int RO_Y = GameGraphics.getOriginOffset()[1];
		
		int RA_X = lastClicked.pixelX;
		int RA_Y = lastClicked.pixelY;
		
		int OA_X = RA_X - RO_X;
		int OA_Y = RA_Y - RO_Y;
		
		int AB_X = OB_X - OA_X;
		int AB_Y = OB_Y - OA_Y;
		
		int clickedPosition_X = lastClicked.x * 100 + 50;
		int clickedPosition_Y = lastClicked.y * 100 + 50;
		
		int centeringOffset = 50;
		

		if (controller.getBuildMode() == BuildMode.BUILD) {
			g.drawLine(clickedPosition_X , clickedPosition_Y, clickedPosition_X +  AB_X - centeringOffset , clickedPosition_Y +  AB_Y - centeringOffset);
			return;
		}
		g.fillPolygon(
			    new int[]{
			        clickedPosition_X,
			        clickedPosition_X + AB_X - centeringOffset,
			        clickedPosition_X + AB_X - centeringOffset,
			        clickedPosition_X
			    },
			    new int[]{
			        clickedPosition_Y,
			        clickedPosition_Y,
			        clickedPosition_Y + AB_Y - centeringOffset,
			        clickedPosition_Y + AB_Y - centeringOffset
			    },
			    4
		);
		
	}

}
