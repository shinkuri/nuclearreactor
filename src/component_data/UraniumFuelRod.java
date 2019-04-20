package component_data;

import component_blueprints.IDepletedFuelRod;
import component_blueprints.IFuelRod;
import component_blueprints.ReactorComponent;

public class UraniumFuelRod extends ReactorComponent implements IFuelRod{
	
	private static final int DURABILITY = 20000;
	
	private static final int HEAT_PER_SECOND = 4;
	private static final int ELECTRICITY_PER_SECOND = 5 * 20;
	private static final int NEUTRON_PULSES_EMITTED = 1; 
	
	private int pulsesReceived = 0;
	
	public UraniumFuelRod(int posX, int posY) {
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
		return new DepletedUraniumFuelRod(super.getX(), super.getY(), Integer.MAX_VALUE);
	}
}
