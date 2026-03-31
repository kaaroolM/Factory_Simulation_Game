package survivalGame;

public final class ActionTimer {

	private int cooldown;
	private int current;
	public ActionTimer(int cooldown) {
		this.cooldown = cooldown;
		current = cooldown;
	}
	
	/**
	 * returns true if cooldown has passed
	 * @return True/False 
	 */
	public boolean actionTick() {
		current --;
		if (current > 0 ) return false;
		
		current = cooldown;
		return true;
	}
	
	public boolean ongoingCooldown() {
		return current > 0; 
	}
}
