package component_data;

import component_blueprints.IHeatManagementComponent;
import component_blueprints.IHeatVent;
import component_blueprints.ReactorComponent;

public class ReactorHeatVent extends ReactorComponent implements IHeatVent, IHeatManagementComponent {
	
	private static final int DURABILITY = 1000;
	
	private static final int SELF_VENT_RATE = 5;
	private static final int REACTOR_VENT_RATE = 5;
	private static final int COMPONENT_VENT_RATE = 0;
	
	private int heat = 0;
	
	protected ReactorHeatVent(int posX, int posY) {
		super(posX, posY, DURABILITY);
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
	public void acceptHeat(int heat) {
		this.heat += heat;
	}

	@Override
	public void removeHeat(int heat) {
		this.heat -= heat;
	}

}
