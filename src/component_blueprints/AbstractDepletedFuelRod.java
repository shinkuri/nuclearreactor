package component_blueprints;

import logic.ComponentFactory.ComponentType;

public class AbstractDepletedFuelRod extends ReactorComponent {
	
	private final ComponentType DEPLETED_TYPE;
	
	public AbstractDepletedFuelRod(int posX, int posY, 
			ComponentType depletedType) {
		
		super(posX, posY);
		
		this.DEPLETED_TYPE = depletedType;
	}
	
	public ComponentType getDepletedRod() {
		return DEPLETED_TYPE;
	}

}
