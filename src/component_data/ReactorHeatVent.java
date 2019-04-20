package component_data;

import component_blueprints.IHeatVent;
import component_blueprints.ReactorComponent;

public class ReactorHeatVent extends ReactorComponent implements IHeatVent {
	
	private static final int HEAT_CAPACITY = 1000;
	
	private static final int SELF_VENT_RATE = 5;
	private static final int REACTOR_VENT_RATE = 5;
	private static final int COMPONENT_VENT_RATE = 0;
		
	protected ReactorHeatVent(int posX, int posY) {
		super(posX, posY, HEAT_CAPACITY);
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

	@Override
	public int addHeat(int heat) {
		return super.doDamage(heat);
	}

	@Override
	public void removeHeat(int heat) {
		super.doDamage(-heat);
	}

}
