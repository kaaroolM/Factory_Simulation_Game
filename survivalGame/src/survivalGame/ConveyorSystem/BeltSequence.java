package survivalGame.ConveyorSystem;

public final class BeltSequence {
	private int beltKey;
	
	public BeltSequence(int beltKey) {
		this.beltKey = beltKey;
	}
	public int getBeltKey() {
		return beltKey;
	}
	
	public void setBeltKey(int key) {
		beltKey = key;
	}
}
