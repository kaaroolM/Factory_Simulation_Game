package survivalGame;

import java.util.ArrayList;
import java.util.List;

public final class TickManager implements Updatable{
	
	private static List<ITickable> toTick = new ArrayList<>();
	private static final TickManager TickManagerInstance;
	static {
		TickManagerInstance = new TickManager();
		Updater.getInstance().register(TickManagerInstance);
	}
	private int tick = 0;
	private static final int tickrate = 300;
	public static TickManager getInstance() {
        return TickManagerInstance;
    }
	
	
	public static void TickAll() {
		for (ITickable tick : toTick) {
			tick.onTick();
			 
		}
		
	}

	public void register(ITickable obj) {
		toTick.add(obj);
	}
	
	public void remove(ITickable obj) {
		toTick.remove(obj);
	}

	@Override
	public void update() {
	}

	@Override
	public void fixedUpdate(long delta) {
		tick += tickrate * delta / 1000f;
		if (tick > 250) {
			TickAll();
			tick = 0;
		}
		
	}
}
