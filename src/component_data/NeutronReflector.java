package component_data;

import component_blueprints.INeutronReflector;
import component_blueprints.ReactorComponent;

public class NeutronReflector extends ReactorComponent implements INeutronReflector {

	private static final int DURABILITY = 30000;
	private static final boolean IS_HEAT_GENERATOR = false;
	
	private static final int NEUTRON_PULSES = 1;
	
	protected NeutronReflector(int posX, int posY) {
		super(posX, posY, DURABILITY, IS_HEAT_GENERATOR);
	}

	@Override
	public int getNeutronPulseAmount() {
		return NEUTRON_PULSES;
	}

}
