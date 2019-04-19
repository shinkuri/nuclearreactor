package component_data;

import component_blueprints.IHeatVent;
import component_blueprints.ReactorComponent;

public class ComponentHeatVent extends ReactorComponent implements IHeatVent {
	
	private static final int DURABILITY = 1000;
	private static final boolean IS_HEAT_GENERATOR = false;
	
	private static final int SELF_VENT_RATE = 0;
	private static final int REACTOR_VENT_RATE = 0;
	private static final int COMPONENT_VENT_RATE = 12;
	
	protected ComponentHeatVent(int posX, int posY) {
		super(posX, posY, DURABILITY, IS_HEAT_GENERATOR);
	}

	@Override
	public int getSelfVentRate() {
		return SELF_VENT_RATE;
	}

	@Override
	public int getReactorVentRate() {
		return REACTOR_VENT_RATE;
	}

	@Override
	public int getComponentVentRate() {
		return COMPONENT_VENT_RATE;
	}

}
