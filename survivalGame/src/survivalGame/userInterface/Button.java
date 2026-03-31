package survivalGame.userInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import graphics.GameGraphics;
import graphics.UIClickable;
import survivalGame.GameState;
import survivalGame.inputs.InputListener;
import survivalGame.inputs.MouseClickListener;

public final class Button implements MouseClickListener{
	
	//Make x and y "virtual" and make it scale with resolution
	
	private BufferedImage texture;
	private String text;
	public final int xPosition;
	public final int yPosition;
	
	//width and height where you can click to interact with this button
	public final int width;
	public final int height;
	
	//function to execute upon button click
	private final Runnable function;
	
	//Active meaning visible and interactable.
	private boolean isActive = true;
	
	private int fontSize = 42;
	
	private Color colour;
	private Color textColour;
	
	private Button(ButtonBuilder builder) {
		this.xPosition = builder.xPosition;
		this.yPosition = builder.yPosition;
		this.text = builder.text;
		this.texture = builder.texture;
		this.function = builder.function;
		this.isActive = builder.isActive;
		this.fontSize = builder.fontSize;
		this.width = builder.width;
		this.height = builder.height;
		this.colour = builder.colour;
		this.textColour = builder.textColour;
		InputListener.getInstance().registerClickListener(builder.gameState, this);
	}

	
	public void renderUI(Graphics2D g, GameGraphics graphics) {
		if (!isActive) return;
		if (texture != null) {
			g.drawImage(texture ,xPosition, yPosition,width,height, graphics);
		}
		else {
			g.setColor(colour);
			g.fillRect(xPosition, yPosition, width, height);
		}
		if (text == null || text.equals("")) return;
		
		g.setColor(textColour);
		Font largeFont = new Font("Arial", Font.BOLD, fontSize);
	    g.setFont(largeFont);
	    
	    int textX =  xPosition + width / 2 - g.getFontMetrics().stringWidth(text) / 2;
	    int textY = yPosition + height / 2;
		g.drawString(text,textX, textY + 15);
	}
	
	@Override
	public void onClick(MouseEvent e) {
		if (!isActive) return;
		if (!(e.getX() > xPosition && e.getX() < xPosition + width)) return;
		if (!(e.getY() > yPosition && e.getY() < yPosition + height)) return;
		
		function.run();
	}
	
	public void setActive(boolean state){
		this.isActive = state;
	}
	
	public void ToggleActive(){
		isActive = !isActive;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setColour(Color colour) {
		this.colour = colour;
	}
	
	public static class ButtonBuilder{
		
		private int xPosition = 300;
		private int yPosition = 300;
		private int width = 100;
		private int height = 100;
		private final Runnable function;
		private int fontSize = 42;
		private boolean isActive = true;
		private BufferedImage texture;
		private String text;
		private GameState gameState;
		private Color colour;
		private Color textColour = Color.BLACK;
		/**
		 * Constructor for the ButtonBuilder
		 * @param function to execute upon click
		 * @param gameState in which this button is interactable
		 */
		public ButtonBuilder(Runnable function, GameState gameState) {
			this.function = function;
			this.gameState = gameState;
		}
		
		public ButtonBuilder setPosition(int xPosition, int yPosition) {
			this.xPosition = xPosition;
			this.yPosition = yPosition;
			return this;
		}
		
		public ButtonBuilder setTexture(BufferedImage texture) {
			this.texture = texture;
			return this;
		}
		
		public ButtonBuilder setText(String text) {
			this.text = text;
			return this;
		}
		
		public ButtonBuilder setFontSize(int fontSize) {
			this.fontSize = fontSize;
			return this;
		}
		
		public ButtonBuilder setActive(boolean state){
			this.isActive = state;
			return this;
		}
		
		public ButtonBuilder ToggleActive(){
			isActive = !isActive;
			return this;
		}
		
		public ButtonBuilder setWidth(int width) {
			this.width = width;
			return this;
		}
		
		public ButtonBuilder setHeight(int height) {
			this.height = height;
			return this;
		}
		
		/**
		 * Offsets x position. Negative values go towards the left. 
		 * @param amount to offset rightwards
		 */
		public ButtonBuilder offsetX(int amount) {
			xPosition += amount;
			return this;
		}
		
		/**
		 * Offsets y position. Negative values go towards the top. 
		 * @param amount to offset downwards
		 */
		public ButtonBuilder offsetY(int amount) {
			yPosition += amount;
			return this;
		}
		
		/**
		 * Sets the fill colour of the button.
		 */
		public ButtonBuilder setColour(Color colour) {
			this.colour = colour;
			return this;
		}

		public ButtonBuilder setTextColour(Color colour) {
			textColour = colour;
			return this;
		}
		
		public ButtonBuilder fixToPoint(UIAnchor anchorPoint) {
			int[] coordinate = UIAlignment.getCoordinateFromAnchor(anchorPoint);
			this.xPosition = coordinate[0];
			this.yPosition = coordinate[1];
			return this;
		}
		
		public ButtonBuilder centerTextureToPoint(UIAnchor anchorPoint) {
			if (texture == null) throw new NullPointerException("Texture is null, therefore unable to center texture to point! ");
			
			int[] coordinate = UIAlignment.getCoordinateFromAnchor(anchorPoint);
			this.xPosition = coordinate[0] - texture.getWidth() / 2;
			this.yPosition = coordinate[1] - texture.getHeight() / 2;
			return this;
		}
		
		public ButtonBuilder centerDimensionToPoint(UIAnchor anchorPoint) {
			int[] coordinate = UIAlignment.getCoordinateFromAnchor(anchorPoint);
			this.xPosition = coordinate[0] - width / 2;
			this.yPosition = coordinate[1] - height / 2;
			return this;
		}
		
		/**
		 * Sets width of height of the button to be the same as the texture width and height. 
		 * Dimension of button refers to the interactive area of the button.
		 * @return
		 */
		public ButtonBuilder setDimensionToTexture() {
			if (texture == null) {
				throw new NullPointerException("Texture is null, therefore unable to set dimension: ");
			}
			this.width = texture.getWidth();
			this.height = texture.getHeight();
			return this;
		}

		public Button build() {
			return new Button(this);
		}
		
		
	}
}
