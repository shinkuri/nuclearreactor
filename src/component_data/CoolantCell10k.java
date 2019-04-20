package component_data;

import component_blueprints.ICoolantCell;
import component_blueprints.ReactorComponent;

public class CoolantCell10k extends ReactorComponent implements ICoolantCell {

	private static final int HEAT_CAPACITY = 10000;	
	
	protected CoolantCell10k(int posX, int posY) {
		super(posX, posY, HEAT_CAPACITY);
	}
	
	@Override
	public int addHeat(int heat) {
		return super.doDamage(heat);
	}

	@Override
	public void removeHeat(int heat) {
		super.doDamage(-heat);
	}

}
