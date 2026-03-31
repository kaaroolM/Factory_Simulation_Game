package survivalGame.userInterface;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import graphics.GameGraphics;
import graphics.UIRenderable;
import survivalGame.GameState;
import survivalGame.inputs.GameKeyListener;
import survivalGame.inputs.InputListener;
import survivalGame.userInterface.Button.ButtonBuilder;

public final class PauseMenu implements GameKeyListener, UIRenderable{

	Button[] buttons = new Button[2];
	final BufferedImage buttonTexture;
	
	private boolean active = false;
	
	public PauseMenu() {
		buttonTexture = GameGraphics.getTextureManager().getTexture("Button");
		int[] center = UIAlignment.getCoordinateFromAnchor(UIAnchor.CENTER);
		
		GameGraphics.registerUI(this);
		InputListener.getInstance().registerKeyListener(this);
		
		buttons[0] = new ButtonBuilder(() -> WorldDeleter.quitWorld(), GameState.GAME)
				.setTexture(buttonTexture)
				.centerTextureToPoint(UIAnchor.CENTER_LEFT)
				.setDimensionToTexture()
				.setText("Save")
				.setActive(false)
				.build();
		
		buttons[1] = new ButtonBuilder(() -> System.exit(0), GameState.GAME)
				.setTexture(buttonTexture)
				.centerTextureToPoint(UIAnchor.CENTER_BOTTOM_LEFT).offsetY(100)
				.setDimensionToTexture()
				.setText("Quit")
				.setActive(false)
				.build();
	}
	
	
	@Override
	public void onKeyPressed(int keyCode) {
		if (keyCode != KeyEvent.VK_ESCAPE) return;
		setActive(!active);
	}
	private void setActive(boolean state) {
		active = state;
		for (int i = 0; i < buttons.length; i++) {
			Button button = buttons[i];
			button.setActive(state);
		}
	}
	@Override
	public void onKeyReleased(int keyCode) {
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void renderUI(Graphics2D g, GameGraphics graphics) {
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0, graphics.getWidth() / 3 ,  graphics.getHeight());
		
		for (int i = 0; i < buttons.length; i++) {
			Button button = buttons[i];
			button.renderUI(g, graphics);
		}
	}

}
