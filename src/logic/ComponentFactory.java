package logic;

import java.util.HashMap;

import component_blueprints.CoolantCell;
import component_blueprints.DepletedFuelRod;
import component_blueprints.FuelRod;
import component_blueprints.HeatExchanger;
import component_blueprints.HeatVent;
import component_blueprints.NeutronReflector;
import component_blueprints.ReactorComponent;

public class ComponentFactory {
	
	public enum ComponentType {
		// Heat Vents
		T1HeatVent("T1HeatVent"),
		T2HeatVent("T2HeatVent"),
		ComponentHeatVent("ComponentHeatVent"),
		ReactorHeatVent("ReactorHeatVent"),
		OverclockedHeatVent("OverclockedHeatVent"),
		// Heat Exchanger
		T1HeatExchanger("T1HeatExchanger"),
		T2HeatExchanger("T2HeatExchanger"),
		ComponentHeatExchanger("ComponentHeatExchanger"),
		ReactorHeatExchanger("ReactorHeatExchanger"),
		// Fuel Rod
		UraniumFuelRod("UraniumFuelRod"),
		UraniumDualFuelRod("UraniumDualFuelRod"),
		UraniumQuadFuelRod("UraniumQuadFuelRod"),
		ThoriumFuelRod("ThoriumFuelRod"),
		ThoriumDualFuelRod("ThoriumDualFuelRod"),
		ThoriumQuadFuelRod("ThoriumQuadFuelRod"),
		MOXUraniumFuelRod("MOXUraniumFuelRod"),
		MOXUraniumDualFuelRod("MOXUraniumDualFuelRod"),
		MOXUraniumQuadFuelRod("MOXUraniumQuadFuelRod"),
		// Depleted Fuel Rod
		DepletedUraniumFuelRod("DepletedUraniumFuelRod"),
		DepletedUraniumDualFuelRod("DepletedUraniumDualFuelRod"),
		DepletedUraniumQuadFuelRod("DepletedUraniumQuadFuelRod"),
		DepletedThoriumFuelRod("DepletedThoriumFuelRod"),
		DepletedThoriumDualFuelRod("DepletedThoriumDualFuelRod"),
		DepletedThoriumQuadFuelRod("DepletedThoriumQuadFuelRod"),
		DepletedMOXUraniumFuelRod("DepletedMOXUraniumFuelRod"),
		DepletedMOXUraniumDualFuelRod("DepletedMOXUraniumDualFuelRod"),
		DepletedMOXUraniumQuadFuelRod("DepletedMOXUraniumQuadFuelRod"),
		// Neutron Reflector
		T1NeutronReflector("T1NeutronReflector"),
		// Coolant Cell
		CoolantCell10k("CoolantCell10k");
		
		private final String stringName;
		
		ComponentType(String stringName) {
			this.stringName = stringName;
		}
		
		public String getStringName() {
			return stringName;
		}
	}
	
	private static HashMap<ComponentType, Integer[]> componentData = new HashMap<>();
	static {
		// Heat Vents
		final Integer[] t1HeatVent = 			{1000, 0, 0, 6};
		final Integer[] t2HeatVent = 			{1000, 0, 0, 12};
		final Integer[] componentHeatVent = 	{1000, 12, 0, 0};
		final Integer[] reactorHeatVent = 		{1000, 0, 5, 5};
		final Integer[] overclockedHeatVent = 	{1000, 0, 36, 20};
		componentData.put(ComponentType.T1HeatVent, t1HeatVent);
		componentData.put(ComponentType.T2HeatVent, t2HeatVent);
		componentData.put(ComponentType.ComponentHeatVent, componentHeatVent);
		componentData.put(ComponentType.ReactorHeatVent, reactorHeatVent);
		componentData.put(ComponentType.OverclockedHeatVent, overclockedHeatVent);
		// Heat Exchangers
		final Integer[] t1HeatExchanger = 			{2500, 12, 4};
		final Integer[] t2HeatExchanger = 			{10000, 24, 8};
		final Integer[] componentHeatExchanger = 	{5000, 36, 0};
		final Integer[] reactorHeatExchanger = 		{5000, 0, 72};
		componentData.put(ComponentType.T1HeatExchanger, t1HeatExchanger);
		componentData.put(ComponentType.T2HeatExchanger, t2HeatExchanger);
		componentData.put(ComponentType.ComponentHeatExchanger, componentHeatExchanger);
		componentData.put(ComponentType.ReactorHeatExchanger, reactorHeatExchanger);
		// Fuel Rods
		final Integer[] uraniumFuelRod = 		{20000, 4, 1, 100, 1, 1};
		final Integer[] uraniumDualFuelRod = 	{20000, 24, 1, 400, 1, 2};
		final Integer[] uraniumQuadFuelRod = 	{20000, 96, 1, 1200, 1, 4};
		final Integer[] thoriumFuelRod = 		{100000, 1, 1, 20, 1, 1};
		final Integer[] thoriumDualFuelRod = 	{100000, 6, 1, 80, 1, 2};
		final Integer[] thoriumQuadFuelRod = 	{100000, 24, 1, 240, 1, 4};
		final Integer[] moxUraniumFuelRod = 	{10000, 4, 10, 100, 10, 1};
		final Integer[] moxUraniumDualFuelRod = {10000, 24, 10, 400, 10, 2};
		final Integer[] moxUraniumQuadFuelRod = {10000, 96, 10, 1200, 10, 4};
		componentData.put(ComponentType.UraniumFuelRod, uraniumFuelRod);
		componentData.put(ComponentType.UraniumDualFuelRod, uraniumDualFuelRod);
		componentData.put(ComponentType.UraniumQuadFuelRod, uraniumQuadFuelRod);
		componentData.put(ComponentType.ThoriumFuelRod, thoriumFuelRod);
		componentData.put(ComponentType.ThoriumDualFuelRod, thoriumDualFuelRod);
		componentData.put(ComponentType.ThoriumQuadFuelRod, thoriumQuadFuelRod);
		componentData.put(ComponentType.MOXUraniumFuelRod, moxUraniumFuelRod);
		componentData.put(ComponentType.MOXUraniumDualFuelRod, moxUraniumDualFuelRod);
		componentData.put(ComponentType.MOXUraniumQuadFuelRod, moxUraniumQuadFuelRod);
		// Neutron Reflectors
		final Integer[] t1NeutronReflector = {30000};
		componentData.put(ComponentType.T1NeutronReflector, t1NeutronReflector);
		// Coolant Cells
		final Integer[] coolantCell10k = {10000};
		componentData.put(ComponentType.CoolantCell10k, coolantCell10k);
	}
	
	public ReactorComponent generateComponent(ComponentType type, int posX, int posY) {
		ReactorComponent rc = null;
		final Integer[] data = componentData.get(type);
		switch(type) {
		case T1HeatVent:
		case T2HeatVent:
		case ComponentHeatVent:
		case ReactorHeatVent:
		case OverclockedHeatVent:
			rc = new HeatVent(posX, posY, data);
			break;
			
		case T1HeatExchanger:
		case T2HeatExchanger:
		case ComponentHeatExchanger:
		case ReactorHeatExchanger:
			rc = new HeatExchanger(posX, posY, data);
			break;
			
		case UraniumFuelRod:
			rc = new FuelRod(posX, posY, ComponentType.DepletedUraniumFuelRod, data);
			break;
		case UraniumDualFuelRod:
			rc = new FuelRod(posX, posY, ComponentType.DepletedUraniumDualFuelRod, data);
			break;
		case UraniumQuadFuelRod:
			rc = new FuelRod(posX, posY, ComponentType.DepletedUraniumQuadFuelRod, data);
			break;
		case ThoriumFuelRod:
			rc = new FuelRod(posX, posY, ComponentType.DepletedThoriumFuelRod, data);
			break;
		case ThoriumDualFuelRod:
			rc = new FuelRod(posX, posY, ComponentType.DepletedThoriumDualFuelRod, data);
			break;
		case ThoriumQuadFuelRod:
			rc = new FuelRod(posX, posY, ComponentType.DepletedThoriumQuadFuelRod, data);
			break;
		case MOXUraniumFuelRod:
			rc = new FuelRod(posX, posY, ComponentType.DepletedMOXUraniumFuelRod, data);
			break;
		case MOXUraniumDualFuelRod:
			rc = new FuelRod(posX, posY, ComponentType.DepletedMOXUraniumDualFuelRod, data);
			break;
		case MOXUraniumQuadFuelRod:
			rc = new FuelRod(posX, posY, ComponentType.DepletedMOXUraniumQuadFuelRod, data);
			break;
			
		case DepletedUraniumFuelRod:
		case DepletedUraniumDualFuelRod:
		case DepletedUraniumQuadFuelRod:
		case DepletedThoriumFuelRod:
		case DepletedThoriumDualFuelRod:
		case DepletedThoriumQuadFuelRod:
		case DepletedMOXUraniumFuelRod:
		case DepletedMOXUraniumDualFuelRod:
		case DepletedMOXUraniumQuadFuelRod:
			rc = new DepletedFuelRod(posX, posY, type);
			break;
			
		case T1NeutronReflector:
			rc = new NeutronReflector(posX, posY, data);
			break;
		
		case CoolantCell10k:
			rc = new CoolantCell(posX, posY, data);
			break;
		}
		return rc;
	}
}
