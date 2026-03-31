package survivalGame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import survivalGame.inputs.GameKeyListener;
import survivalGame.inputs.InputListener;

public final class MovementController implements GameKeyListener {
	private int horiz = 0;
	private int vert = 0;
	
	public MovementController() {
		InputListener.getInstance().registerKeyListener(this);
	}

	@Override
	public void onKeyPressed(int keyCode) {
		switch (keyCode) {
        case KeyEvent.VK_W ->
            vert = 1;
        case KeyEvent.VK_A ->
            horiz = 1;
        case KeyEvent.VK_S ->
            vert = -1;
        case KeyEvent.VK_D ->
            horiz = -1;
            }      
	}
		
	@Override
	public void onKeyReleased(int keyCode) {
		 switch (keyCode) {
         case KeyEvent.VK_W, KeyEvent.VK_S ->
             vert = 0;
         case KeyEvent.VK_A, KeyEvent.VK_D ->
             horiz = 0;
		 }
		 
	}
	
	public int getHorizontal() {
		return horiz;
	}
	public int getVertical() {
		return vert;
	}
}
