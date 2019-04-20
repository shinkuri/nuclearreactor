package component_data;

import component_blueprints.IDepletedFuelRod;
import component_blueprints.ReactorComponent;

public class DepletedThoriumDualFuelRod extends ReactorComponent implements IDepletedFuelRod {

	protected DepletedThoriumDualFuelRod(int posX, int posY) {
		super(posX, posY, Integer.MAX_VALUE);
	}

}
