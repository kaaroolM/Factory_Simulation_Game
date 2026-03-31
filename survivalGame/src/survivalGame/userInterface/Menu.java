package survivalGame.userInterface;

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

public final class Menu {
	List<Button> buttons = new ArrayList<>();
	
	BufferedImage buttonTexture;
	BufferedImage background;
	
	private final int iconOffset = 20;
	
	private final int[] titlePosition;
	private final String title = "REFACTOR";
	//Play, settings, exit
	BufferedImage[] icons = new BufferedImage[3];
	public Menu(TextureManager Tmanager) {
		background = Tmanager.getTexture("Background");
		buttonTexture = Tmanager.getTexture("Button");
		
		icons[0] = Tmanager.getTexture("PlayIcon");
		icons[1] = Tmanager.getTexture("SettingsIcon");
		icons[2] = Tmanager.getTexture("ExitIcon");
		
		buttons.add(new ButtonBuilder(() -> CurrentGameState.gameState = GameState.WORLDSELECTION, GameState.MENU)
				.setTexture(buttonTexture)
				.centerTextureToPoint(UIAnchor.CENTER)
				.setDimensionToTexture()
				.setText("Play")
				.build());
		
		buttons.add(new ButtonBuilder(() -> CurrentGameState.gameState = GameState.SETTINGS, GameState.MENU)
				.setTexture(buttonTexture)
				.centerTextureToPoint(UIAnchor.CENTER).offsetY(100)
				.setDimensionToTexture()
				.setText("Settings")
				.build());
		
		buttons.add(new ButtonBuilder(() -> System.exit(0), GameState.MENU)
				.setTexture(buttonTexture)
				.centerTextureToPoint(UIAnchor.BOTTOM)
				.offsetY(-50)
				.setDimensionToTexture()
				.setText("Quit")
				.build());
		
		
		titlePosition = UIAlignment.getCoordinateFromAnchor(UIAnchor.CENTER_TOP);
		
	}
	
	
	public void renderMenu(Graphics2D g, GameGraphics graphics) {
		g.drawImage(background,0,0, GameGraphics.SCREEN_WIDTH, GameGraphics.SCREEN_HEIGHT, graphics);
		
		for (int i = 0; i < buttons.size(); i++) {
			Button button = buttons.get(i);
			button.renderUI(g, graphics);
			g.drawImage(icons[i], button.xPosition - icons[i].getWidth() - iconOffset, button.yPosition , graphics);
		}
		Font largeFont = new Font("Arial", Font.BOLD, 150);
	    g.setFont(largeFont);
	    g.drawString(title, titlePosition[0] - g.getFontMetrics().stringWidth(title) / 2, titlePosition[1]);
	}
}
