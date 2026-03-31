package survivalGame.userInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import graphics.CurrentGameState;
import graphics.GameGraphics;
import graphics.TextureManager;
import survivalGame.GameState;
import survivalGame.userInterface.Button.ButtonBuilder;

public final class SettingsDisplay {
	List<Button> buttons = new ArrayList<>();
	
	BufferedImage buttonTexture;
	BufferedImage background;
	public SettingsDisplay(TextureManager Tmanager) {
		background = Tmanager.getTexture("Background");
		buttonTexture = Tmanager.getTexture("Button");
		
		buttons.add(new ButtonBuilder(() -> CurrentGameState.gameState = GameState.MENU, GameState.SETTINGS)
				.setTexture(buttonTexture)
				.setDimensionToTexture()
				.centerDimensionToPoint(UIAnchor.BOTTOM)
				.offsetY(-50)
				.setText("Return")
				.build());
		

	
	}	
	
	public void renderSettings(Graphics2D g, GameGraphics graphics) {
		g.drawImage(background,0,0, GameGraphics.SCREEN_WIDTH, GameGraphics.SCREEN_HEIGHT, graphics);
		
		g.setColor(new Color(0,0,0,150));
		g.fillRect(GameGraphics.SCREEN_WIDTH / 4, GameGraphics.SCREEN_HEIGHT / 4, GameGraphics.SCREEN_WIDTH / 2, GameGraphics.SCREEN_HEIGHT / 2 + 200);
		
		for (Button button : buttons) {
			button.renderUI(g, graphics);
		}
		g.setColor(new Color(250,250,250));
		Font largeFont = new Font("Arial", Font.BOLD, 57);
	    g.setFont(largeFont);
	    g.drawString("Settings", GameGraphics.SCREEN_WIDTH / 4 + 255, GameGraphics.SCREEN_HEIGHT / 4);
	    
		largeFont = new Font("Arial", Font.BOLD, 15);
	    g.setFont(largeFont);
	    g.drawString("Do not spoil what you have by desiring what you have not;", GameGraphics.SCREEN_WIDTH / 4 + 20, 740);
	    g.drawString("remember that what you now have was once among the things you only hoped for.  —Epicurus",GameGraphics.SCREEN_WIDTH / 4 + 20, 760);
	}
}
