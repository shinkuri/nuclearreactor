package component_data;

import component_blueprints.IHeatExchanger;
import component_blueprints.ReactorComponent;

public class HeatExchanger extends ReactorComponent implements IHeatExchanger {
	
	private static final int DURABILITY = 2500;
	private static final boolean IS_HEAT_GENERATOR = false;
	
	private static final int COMPONENT_EXCHANGE_RATE = 12;
	private static final int HULL_EXCHANGE_RATE = 4;
	
	protected HeatExchanger(int posX, int posY) {
		super(posX, posY, DURABILITY, IS_HEAT_GENERATOR);
	}

	@Override
	public int getComponentExchangeRate() {
		return COMPONENT_EXCHANGE_RATE;
	}

	@Override
	public int getHullExchangeRate() {
		return HULL_EXCHANGE_RATE;
	}

}
