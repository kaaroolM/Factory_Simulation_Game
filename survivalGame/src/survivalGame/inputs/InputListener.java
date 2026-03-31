package survivalGame.inputs;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import graphics.CurrentGameState;
import graphics.GameGraphics;
import graphics.UIClickable;
import survivalGame.GameState;

public final class InputListener implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {

	private static final InputListener InputListenerInstance = new InputListener();;
	
	public static InputListener getInstance() {
        return InputListenerInstance;
    }
	
	

	private int[] clickedCoords = new int[2];

	public boolean leftButtonHeld = false;
	private int mouseX = 0, mouseY = 0;
	 
	//private boolean mouseDragging = false;
	
	private EnumMap<GameState, List<MouseClickListener>> clickMap = new EnumMap<>(GameState.class);
	private List<UIClickable> clickableUI = new ArrayList<>();
	private List<GameKeyListener> keyListeners = new ArrayList<>();
	private List<GameMouseReleaseListener> mouseReleaseListeners = new ArrayList<>();
	
	public void registerClickableUI(UIClickable clickableInterface) {
		clickableUI.add(clickableInterface);
	}
	public void registerClickListenerToWorld(MouseClickListener c) {
		clickMap.computeIfAbsent(GameState.GAME, k -> new ArrayList<>()).add(c);
	}
	public void registerClickListener(GameState state, MouseClickListener c) {
		clickMap.computeIfAbsent(state, k -> new ArrayList<>()).add(c);
	}
	public void registerKeyListener(GameKeyListener keyListener) {
		keyListeners.add(keyListener);
	}
	public void registerReleaseListener(GameMouseReleaseListener releaseListener) {
		mouseReleaseListeners.add(releaseListener);
	}
	
	@Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
  
        for (GameKeyListener keyListener : keyListeners) {
        	keyListener.onKeyPressed(keyCode);
        }
   
    }
	
	@Override
	public void keyReleased(KeyEvent e) {
	
		// Reset movement 
        int keyCode = e.getKeyCode();
        for (GameKeyListener keyListener : keyListeners) {
        	keyListener.onKeyReleased(keyCode);
        }
	}

	/**
	 * @return most recently clicked coordinates in int[]{x,y} format
	 */
	public int[] listenClick() {
		return clickedCoords;
	}

	boolean first = true;
	
	
	
	@Override
    public void mousePressed(MouseEvent e) {
		handleClick(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftButtonHeld = true;
        }
        
    }
	
	private void handleClick(MouseEvent e) {

		for (UIClickable UI : clickableUI) {
			//If the UI is an instance of Interactable UI, and the mouse click coordinates are within the bounds of that UI.. 
			if (UI.isActive() && UI.getBounds().contains(new Point(e.getX(),e.getY()))) {
				UI.onClick();
				return;
			}
		}
		
		if (!clickMap.containsKey(CurrentGameState.gameState)) return;
		for (MouseClickListener click : clickMap.get(CurrentGameState.gameState)) { 
			click.onClick(e);
		}
		
	}
	
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftButtonHeld = false;
        }
        for (GameMouseReleaseListener releaseListener : mouseReleaseListeners) {
        	releaseListener.mouseReleased(e);
        }
    }
    
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) {
            	GameGraphics.setCameraZoom(GameGraphics.getCameraZoom() * 1.1f); // zoom in
            } else {
            	GameGraphics.setCameraZoom(GameGraphics.getCameraZoom() / 1.1f); // zoom out
            }
        }
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
        mouseY = e.getY();
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		
		mouseX = e.getX();
		mouseY = e.getY();
		 
	}

	public int getMouseX() {
		return mouseX;
	}
	public int getMouseY() {
		return mouseY;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
}
