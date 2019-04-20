package component_data;

import component_blueprints.IDepletedFuelRod;
import component_blueprints.ReactorComponent;

public class DepletedUraniumDualFuelRod extends ReactorComponent implements IDepletedFuelRod {

	protected DepletedUraniumDualFuelRod(int posX, int posY) {
		super(posX, posY, Integer.MAX_VALUE);
	}

}
