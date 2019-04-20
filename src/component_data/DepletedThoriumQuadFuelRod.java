package component_data;

import component_blueprints.IDepletedFuelRod;
import component_blueprints.ReactorComponent;

public class DepletedThoriumQuadFuelRod extends ReactorComponent implements IDepletedFuelRod {

	protected DepletedThoriumQuadFuelRod(int posX, int posY) {
		super(posX, posY, Integer.MAX_VALUE);
	}

}
