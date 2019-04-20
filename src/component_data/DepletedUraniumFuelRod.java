package component_data;

import component_blueprints.IDepletedFuelRod;
import component_blueprints.ReactorComponent;

public class DepletedUraniumFuelRod extends ReactorComponent implements IDepletedFuelRod {

	protected DepletedUraniumFuelRod(int posX, int posY, int durabilityMax) {
		super(posX, posY, durabilityMax);
	}

}
