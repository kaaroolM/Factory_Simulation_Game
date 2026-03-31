package survivalGame;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class Updater implements Runnable {
    private static final Updater instance = new Updater();
    private final List<Updatable> updatables = new ArrayList<>();
    private final Queue<Updatable> toAdd = new ArrayDeque<>();
    
    final int fps = 70;
    final long frameTime = 1000 / fps; // 16 ms target
    
    private Updater() {}

    public static Updater getInstance() {
        return instance;
    }

    public void register(Updatable updatable) {
    	 if (updatable == null) {
    	        throw new IllegalArgumentException("Trying to register null!");
    	    }
        toAdd.add(updatable);
    }
    
    public void remove(Updatable updatable) {
   	 if (updatable == null) {
   	        throw new IllegalArgumentException("Trying to remove null!");
   	    }
       toAdd.remove(updatable);
   }
    
    @Override
    public void run() {
       
    	long lastTime = System.currentTimeMillis();
        while (true) {
            long now = System.currentTimeMillis();
            long delta = now - lastTime;
            lastTime = now;
            
            //delta means difference, fixed update is updating on time passed, not on performance. 
            updateAll(); 
            fixedUpdateAll(delta);
          //If frameTime = 16 ms (for 60 FPS) And updateAll() took 6 ms Then you sleep for 10 ms to keep consistent framerate 
            long sleep = frameTime - (System.currentTimeMillis() - now);
            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateAll() {
        
    	//Exception in thread "Thread-0" java.util.ConcurrentModificationException 
    	//list is modified during the foreach loop, commonly occurs when placing conveyor belts with item on it :(
    	
    	//This issue has been fixed with queuing, so that list won't be modified if items are instantiated inbetween frames.
    	
    	while (!toAdd.isEmpty()) {
    		updatables.add(toAdd.poll());
    	}	
        for (Updatable updatable : updatables) {
            updatable.update();  
        }
        
    }
    public void fixedUpdateAll(long delta) {
        for (Updatable updatable : updatables) {
            updatable.fixedUpdate(delta);  
        }
     
    }
}

