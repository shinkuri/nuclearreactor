package component_data;

import component_blueprints.IDepletedFuelRod;
import component_blueprints.IFuelRod;
import component_blueprints.ReactorComponent;

public class UraniumQuadFuelRod extends ReactorComponent implements IFuelRod{
	
	private static final int DURABILITY = 20000;
	
	private final static int HEAT_PER_SECOND = 96;
	private final static int ELECTRICITY_PER_SECOND = 60 * 20;
	private static final int NEUTRON_PULSES = 4; 
	
	private int pulsesReceived = 0;
	
	protected UraniumQuadFuelRod(int posX, int posY) {
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
		return NEUTRON_PULSES;
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
		return new DepletedUraniumQuadFuelRod(super.getX(), super.getY());
	}
}