package component_data;

import component_blueprints.IHeatExchanger;
import component_blueprints.ReactorComponent;

public class AdvancedHeatExchanger extends ReactorComponent implements IHeatExchanger {
	
	private static final int HEAT_CAPACITY = 10000;
	
	private static final int COMPONENT_EXCHANGE_RATE = 24;
	private static final int HULL_EXCHANGE_RATE = 8;
	
	protected AdvancedHeatExchanger(int posX, int posY) {
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
