package cellularAutomata;

import ecosystem.WorldConstants.PatchType;

public class Patch extends MajorityCell{
	
	private long eatenTime;
	private int timeToGrow;

	public Patch(CellularAutomata ca, int row, int col, int timeToGrow) {
		super(ca, row, col);
		this.timeToGrow = timeToGrow;
		eatenTime = System.currentTimeMillis();
	}
	
	public void setFertile() {
		state = PatchType.FERTILE.ordinal();
		eatenTime = System.currentTimeMillis();
	}
	
	public void regenerate() {
		if(state == PatchType.FERTILE.ordinal() && System.currentTimeMillis() > (eatenTime + timeToGrow))
			state = PatchType.FOOD.ordinal();
	}
	
}
