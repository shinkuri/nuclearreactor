package component_data;

import component_blueprints.IDepletedFuelRod;
import component_blueprints.ReactorComponent;

public class DepletedUraniumQuadFuelRod extends ReactorComponent implements IDepletedFuelRod {

	protected DepletedUraniumQuadFuelRod(int posX, int posY) {
		super(posX, posY, Integer.MAX_VALUE);
	}

}
