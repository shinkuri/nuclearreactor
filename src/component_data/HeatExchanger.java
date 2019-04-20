package component_data;

import component_blueprints.IHeatExchanger;
import component_blueprints.ReactorComponent;

public class HeatExchanger extends ReactorComponent implements IHeatExchanger {
	
	private static final int HEAT_CAPACITY = 2500;
	
	private static final int COMPONENT_EXCHANGE_RATE = 12;
	private static final int HULL_EXCHANGE_RATE = 4;
	
	protected HeatExchanger(int posX, int posY) {
		super(posX, posY, HEAT_CAPACITY);
	}

	@Override
	public int getComponentExchangeRate() {
		return COMPONENT_EXCHANGE_RATE;
	}

	@Override
	public int getHullExchangeRate() {
		return HULL_EXCHANGE_RATE;
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
