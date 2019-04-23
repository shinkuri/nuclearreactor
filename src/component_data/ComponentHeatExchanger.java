package component_data;

import component_blueprints.IHeatExchanger;
import component_blueprints.ReactorComponent;

public class ComponentHeatExchanger extends ReactorComponent implements IHeatExchanger {
	
	private static final int HEAT_CAPACITY = 5000;
	
	private static final int COMPONENT_EXCHANGE_RATE = 36;
	private static final int HULL_EXCHANGE_RATE = 0;
	
	protected ComponentHeatExchanger(int posX, int posY) {
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
	public int tryAddHeat(int heat) {
		return super.doDamage(heat);
	}

	@Override
	public void tryRemoveHeat(int heat) {
		super.doDamage(-heat);
	}
}
