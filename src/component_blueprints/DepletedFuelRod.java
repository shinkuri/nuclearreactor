package component_blueprints;

import logic.ComponentFactory.ComponentSubType;

public class DepletedFuelRod extends ReactorComponent {
	
	private final ComponentSubType DEPLETED_TYPE;
	
	public DepletedFuelRod(int posX, int posY, 
			ComponentSubType depletedType) {
		
		super(ComponentType.DepletedFuelRod, posX, posY);
		
		this.DEPLETED_TYPE = depletedType;
	}
	
	public ComponentSubType getDepletedRod() {
		return DEPLETED_TYPE;
	}

}
