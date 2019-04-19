package component_data;

import component_blueprints.ICoolantCell;
import component_blueprints.ReactorComponent;

public class CoolantCell10k extends ReactorComponent implements ICoolantCell {

	private static final int DURABILITY = 10000;
	private static final boolean IS_HEAT_GENERATOR = false;
	
	
	protected CoolantCell10k(int posX, int posY) {
		super(posX, posY, DURABILITY, IS_HEAT_GENERATOR);
	}

}
