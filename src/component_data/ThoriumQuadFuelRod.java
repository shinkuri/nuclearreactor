package component_data;

import component_blueprints.IFuelRod;
import component_blueprints.ReactorComponent;

public class ThoriumQuadFuelRod extends ReactorComponent implements IFuelRod{
	
	private static final int DURABILITY = 100000;
	private static final boolean IS_HEAT_GENERATOR = true;
	
	private final static int HEAT_PER_SECOND = 24;
	private final static int ELECTRICITY_PER_SECOND = 12 * 20;
	private static final int NEUTRON_PULSES = 4; 
	
	protected ThoriumQuadFuelRod(int posX, int posY) {
		super(posX, posY, DURABILITY, IS_HEAT_GENERATOR);
	}

	@Override
	public int getHeatPerSecond() {
		return HEAT_PER_SECOND;
	}

	@Override
	public int getElectricityPerSecond() {
		return ELECTRICITY_PER_SECOND;
	}

	@Override
	public int getNeutronPulseAmount() {
		return NEUTRON_PULSES;
	}
}