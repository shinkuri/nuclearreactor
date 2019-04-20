package component_data;

import component_blueprints.IDepletedFuelRod;
import component_blueprints.ReactorComponent;

public class DepletedUraniumMOXFuelRod extends ReactorComponent implements IDepletedFuelRod {

	protected DepletedUraniumMOXFuelRod(int posX, int posY) {
		super(posX, posY, Integer.MAX_VALUE);
	}

}
