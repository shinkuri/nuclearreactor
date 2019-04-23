package logic;

import java.util.HashMap;

import component_blueprints.AbstractHeatExchanger;
import component_blueprints.AbstractHeatVent;
import component_blueprints.ReactorComponent;

/*
 * TODO: Add data for one of each component type
 */
public class ComponentFactory {
	
	public enum ComponentType {
		// Heat Vents
		T1HeatVent,
		// Heat Exchanger
		T1HeatExchanger
	}
	
	private static HashMap<ComponentType, Integer[]> componentData = new HashMap<>();
	static {
		// Heat Vents
		final Integer[] t1HeatVent = {1000, 0, 0, 6};
		componentData.put(ComponentType.T1HeatVent, t1HeatVent);
		// Heat Exchanger
		final Integer[] t1HeatExchanger = {2500, 12, 4};
		componentData.put(ComponentType.T1HeatExchanger, t1HeatExchanger);
	}
	
	public ReactorComponent generateComponent(ComponentType type, int posX, int posY) {
		ReactorComponent rc = null;
		final Integer[] data = componentData.get(type);
		switch(type) {
		case T1HeatVent:
			rc = new AbstractHeatVent(posX, posY, data[0], data[1], data[2], data[4]);
			break;
		case T1HeatExchanger:
			rc = new AbstractHeatExchanger(posX, posY, data[0], data[1], data[2]);
		}
		return rc;
	}
}
