package component_data;

import component_blueprints.IFuelRod;
import component_blueprints.ReactorComponent;

public class UraniumQuadFuelRod extends ReactorComponent implements IFuelRod{
	
	private static final int DURABILITY = 20000;
	private static final boolean IS_HEAT_GENERATOR = true;
	
	private final static int HEAT_PER_SECOND = 96;
	private final static int ELECTRICITY_PER_SECOND = 60 * 20;
	private static final int NEUTRON_PULSES = 4; 
	
	protected UraniumQuadFuelRod(int posX, int posY) {
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