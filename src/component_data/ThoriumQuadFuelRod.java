package component_data;

import component_blueprints.IDepletedFuelRod;
import component_blueprints.IFuelRod;
import component_blueprints.ReactorComponent;

public class ThoriumQuadFuelRod extends ReactorComponent implements IFuelRod{
	
	private static final int DURABILITY = 100000;
	
	private final static int HEAT_PER_SECOND = 24;
	private final static int ELECTRICITY_PER_SECOND = 12 * 20;
	private static final int NEUTRON_PULSES_EMITTED = 4; 
	
	private int pulsesReceived = 0;
	
	protected ThoriumQuadFuelRod(int posX, int posY) {
		super(posX, posY, DURABILITY);
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
	public int getNeutronPulsesEmitted() {
		return NEUTRON_PULSES_EMITTED;
	}

	@Override
	public void addNeutronPulse(int p) {
		pulsesReceived += p;
	}

	@Override
	public int getNeutronPulses() {
		return pulsesReceived;
	}

	@Override
	public IDepletedFuelRod getDepletedRod() {
		return new DepletedThoriumQuadFuelRod(super.getX(), super.getY());
	}
}