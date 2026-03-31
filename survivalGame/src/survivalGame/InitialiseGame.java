package survivalGame;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import graphics.CurrentGameState;
import graphics.GameGraphics;
import graphics.TextureManager;
import graphics.ImageManipulation.ImageFlipper;
import survivalGame.inputs.InputListener;

public final class InitialiseGame {
	
	
	static TextureManager textureManager = new TextureManager();
	

	public static void main(String[] args) {
		//playMusic();
		
		Thread updaterThread = new Thread(Updater.getInstance());
		
		loadTextures();
		CurrentGameState.gameState = GameState.MENU;
		
		
		GameGraphics gameGraphics = new GameGraphics();
		gameGraphics.addOnTextureManager(textureManager);
		gameGraphics.addMouseMotionListener(InputListener.getInstance());
		
		Updater.getInstance().register(gameGraphics);
		gameGraphics.initialiseMenu(textureManager);

		new MainScreen(gameGraphics);
		
        updaterThread.start();  // Start the game loop in a separate thread
	}
	
	
	
	public static void playMusic() {
		String location = "src/music.wav";
		File musicPath = new File(location);
		try {
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
			Clip clip;
			try {
				clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
				clip.loop(clip.LOOP_CONTINUOUSLY);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void loadTextures() {

		textureManager.loadTexture("/images/Stickman.png", "Player");
		textureManager.loadTexture("/images/grasy.jpg", "Grass");
		textureManager.loadTexture("/images/Tree.png", "Tree");
		textureManager.loadTexture("/images/Rock.png", "Rock");
		
		String[] directions = {"N", "E", "S", "W"};
		for (String direction : directions) {
		    textureManager.loadTexture("/images/Conveyors/Conveyor" + direction + ".png", "Conveyor" + direction);
		}

		String[] turns = {"NE", "SE", "SW", "NW", "EN", "ES", "WS", "WN"};
		for (String turn : turns) {
		    textureManager.loadTexture("/images/Conveyors/ConveyorTurn" + turn + ".png", "Conveyor" + turn);
		    textureManager.loadTexture("/images/Conveyors/Junctions/ConveyorT_" + turn + ".png", "ConveyorT_" + turn);
		}
		turns = new String[]{"VE", "VW", "HN", "HS"};
		for (String turn : turns) {
			 textureManager.loadTexture("/images/Conveyors/Junctions/ConveyorT_" + turn + ".png", "ConveyorT_" + turn);
		}
		
		textureManager.loadTexture("/images/PlayerUI.png", "PlayerUI");

		textureManager.loadTexture("/images/InventorySquare.png", "InventorySlot");
		textureManager.loadTexture("/images/itemSelection.png", "SelectedSlot");
		textureManager.loadTexture("/images/CraftingSquareActive.png", "ButtonActive");
		textureManager.loadTexture("/images/CraftingSquareInactive.png", "ButtonInactive");
		textureManager.loadTexture("/images/Hotbar.png", "Hotbar");
		
		textureManager.loadTexture("/images/FactoryComponents/TreeHarvester.png", "TreeHarvester");
		textureManager.loadTexture("/images/FactoryComponents/Planker.png", "Planker");
		textureManager.loadTexture("/images/FactoryComponents/ConveyorSplitter.png", "ConveyorSplitterR");
		textureManager.addTexture(ImageFlipper.flipImageHorizontal(textureManager.getTexture("ConveyorSplitterR")), "ConveyorSplitterL");
		textureManager.loadTexture("/images/FactoryComponents/RockDriller.png", "RockDriller");
		
		textureManager.loadTexture("/images/Items/Item_Conveyor.png", "ConveyorItem");
		textureManager.loadTexture("/images/Items/Item_TreeHarvester.png", "TreeHarvesterItem");
		textureManager.loadTexture("/images/Items/Item_Wood.png", "WoodItem");
		textureManager.loadTexture("/images/Items/Item_Log.png", "LogItem");
		textureManager.loadTexture("/images/Items/Item_Rock.png", "RockItem");
		textureManager.loadTexture("/images/Items/Item_Planker.png", "PlankerItem");
		textureManager.loadTexture("/images/Items/Item_ConveyorSplitter.png", "ConveyorSplitterItemR");
		textureManager.addTexture(ImageFlipper.flipImageHorizontal(textureManager.getTexture("ConveyorSplitterItemR")), "ConveyorSplitterItemL");
		textureManager.loadTexture("/images/Items/Item_RockDriller.png", "RockDrillerItem");
		
		textureManager.loadTexture("/images/BlueprintDirection.png", "Blueprint");
		textureManager.loadTexture("/images/BlueprintDelete.png", "DeleteBlueprint");
		
		textureManager.loadTexture("/images/Button.png", "Button");
		textureManager.loadTexture("/images/Background.png", "Background");
		textureManager.loadTexture("/images/WorldSelection.png", "WorldSlot");
		
		textureManager.loadTexture("/images/Icons/ExitIcon.png", "ExitIcon");
		textureManager.loadTexture("/images/Icons/PlayIcon.png", "PlayIcon");
		textureManager.loadTexture("/images/Icons/SettingsIcon.png", "SettingsIcon");
	}
}
