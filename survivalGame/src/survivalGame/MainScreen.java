package survivalGame;



import javax.swing.JFrame;

import graphics.GameGraphics;
import survivalGame.inputs.InputListener;


public final class MainScreen {
	
	public MainScreen(GameGraphics gameGraphics) {
	    JFrame frame = new JFrame("Factory Game");

	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	    frame.setUndecorated(true); 
	    
	    frame.add(gameGraphics);
	    frame.setVisible(true);
	    
	    frame.addMouseListener(InputListener.getInstance());
	    gameGraphics.setFocusable(true);
	    gameGraphics.requestFocusInWindow();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}