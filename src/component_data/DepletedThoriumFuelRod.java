package component_data;

import component_blueprints.IDepletedFuelRod;
import component_blueprints.ReactorComponent;

public class DepletedThoriumFuelRod extends ReactorComponent implements IDepletedFuelRod {

	protected DepletedThoriumFuelRod(int posX, int posY) {
		super(posX, posY, Integer.MAX_VALUE);
	}

}
