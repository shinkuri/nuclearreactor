package logic;

import java.util.HashMap;

import component_blueprints.CoolantCell;
import component_blueprints.DepletedFuelRod;
import component_blueprints.FuelRod;
import component_blueprints.HeatExchanger;
import component_blueprints.HeatVent;
import component_blueprints.NeutronReflector;
import component_blueprints.ReactorComponent;
import component_blueprints.ReactorComponent.ComponentType;

public class ComponentFactory {
	
	public enum ComponentSubType {
		// Heat Vents
		T1HeatVent(ComponentType.HeatVent, "T1HeatVent"),
		T2HeatVent(ComponentType.HeatVent, "T2HeatVent"),
		ComponentHeatVent(ComponentType.HeatVent, "ComponentHeatVent"),
		ReactorHeatVent(ComponentType.HeatVent, "ReactorHeatVent"),
		OverclockedHeatVent(ComponentType.HeatVent, "OverclockedHeatVent"),
		// Heat Exchanger
		T1HeatExchanger(ComponentType.HeatExchanger, "T1HeatExchanger"),
		T2HeatExchanger(ComponentType.HeatExchanger, "T2HeatExchanger"),
		ComponentHeatExchanger(ComponentType.HeatExchanger, "ComponentHeatExchanger"),
		ReactorHeatExchanger(ComponentType.HeatExchanger, "ReactorHeatExchanger"),
		// Fuel Rod
		UraniumFuelRod(ComponentType.FuelRod, "UraniumFuelRod"),
		UraniumDualFuelRod(ComponentType.FuelRod, "UraniumDualFuelRod"),
		UraniumQuadFuelRod(ComponentType.FuelRod, "UraniumQuadFuelRod"),
		ThoriumFuelRod(ComponentType.FuelRod, "ThoriumFuelRod"),
		ThoriumDualFuelRod(ComponentType.FuelRod, "ThoriumDualFuelRod"),
		ThoriumQuadFuelRod(ComponentType.FuelRod, "ThoriumQuadFuelRod"),
		MOXUraniumFuelRod(ComponentType.FuelRod, "MOXUraniumFuelRod"),
		MOXUraniumDualFuelRod(ComponentType.FuelRod, "MOXUraniumDualFuelRod"),
		MOXUraniumQuadFuelRod(ComponentType.FuelRod, "MOXUraniumQuadFuelRod"),
		// Depleted Fuel Rod
		DepletedUraniumFuelRod(ComponentType.DepletedFuelRod, "DepletedUraniumFuelRod"),
		DepletedUraniumDualFuelRod(ComponentType.DepletedFuelRod, "DepletedUraniumDualFuelRod"),
		DepletedUraniumQuadFuelRod(ComponentType.DepletedFuelRod, "DepletedUraniumQuadFuelRod"),
		DepletedThoriumFuelRod(ComponentType.DepletedFuelRod, "DepletedThoriumFuelRod"),
		DepletedThoriumDualFuelRod(ComponentType.DepletedFuelRod, "DepletedThoriumDualFuelRod"),
		DepletedThoriumQuadFuelRod(ComponentType.DepletedFuelRod, "DepletedThoriumQuadFuelRod"),
		DepletedMOXUraniumFuelRod(ComponentType.DepletedFuelRod, "DepletedMOXUraniumFuelRod"),
		DepletedMOXUraniumDualFuelRod(ComponentType.DepletedFuelRod, "DepletedMOXUraniumDualFuelRod"),
		DepletedMOXUraniumQuadFuelRod(ComponentType.DepletedFuelRod, "DepletedMOXUraniumQuadFuelRod"),
		// Neutron Reflector
		T1NeutronReflector(ComponentType.NeutronReflector, "T1NeutronReflector"),
		// Coolant Cell
		CoolantCell10k(ComponentType.CoolantCell, "CoolantCell10k"),
		HeliumCoolantCell360k(ComponentType.CoolantCell, "HeliumCoolantCell360k");
		
		private final ComponentType type;
		private final String stringName;
		
		ComponentSubType(ComponentType type, String stringName) {
			this.type = type;
			this.stringName = stringName;
		}
		
		public ComponentType getType() {
			return type;
		}
		
		public String getStringName() {
			return stringName;
		}
	}
	
	private static HashMap<ComponentSubType, Integer[]> componentData = new HashMap<>();
	static {
		// Specific Item to Type mapping
		//typeMapping.put(key, value)
		// Heat Vents
		final Integer[] t1HeatVent = 			{1000, 0, 0, 6};
		final Integer[] t2HeatVent = 			{1000, 0, 0, 12};
		final Integer[] componentHeatVent = 	{1000, 12, 0, 0};
		final Integer[] reactorHeatVent = 		{1000, 0, 5, 5};
		final Integer[] overclockedHeatVent = 	{1000, 0, 36, 20};
		componentData.put(ComponentSubType.T1HeatVent, t1HeatVent);
		componentData.put(ComponentSubType.T2HeatVent, t2HeatVent);
		componentData.put(ComponentSubType.ComponentHeatVent, componentHeatVent);
		componentData.put(ComponentSubType.ReactorHeatVent, reactorHeatVent);
		componentData.put(ComponentSubType.OverclockedHeatVent, overclockedHeatVent);
		// Heat Exchangers
		final Integer[] t1HeatExchanger = 			{2500, 12, 4};
		final Integer[] t2HeatExchanger = 			{10000, 24, 8};
		final Integer[] componentHeatExchanger = 	{5000, 36, 0};
		final Integer[] reactorHeatExchanger = 		{5000, 0, 72};
		componentData.put(ComponentSubType.T1HeatExchanger, t1HeatExchanger);
		componentData.put(ComponentSubType.T2HeatExchanger, t2HeatExchanger);
		componentData.put(ComponentSubType.ComponentHeatExchanger, componentHeatExchanger);
		componentData.put(ComponentSubType.ReactorHeatExchanger, reactorHeatExchanger);
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
		componentData.put(ComponentSubType.UraniumFuelRod, uraniumFuelRod);
		componentData.put(ComponentSubType.UraniumDualFuelRod, uraniumDualFuelRod);
		componentData.put(ComponentSubType.UraniumQuadFuelRod, uraniumQuadFuelRod);
		componentData.put(ComponentSubType.ThoriumFuelRod, thoriumFuelRod);
		componentData.put(ComponentSubType.ThoriumDualFuelRod, thoriumDualFuelRod);
		componentData.put(ComponentSubType.ThoriumQuadFuelRod, thoriumQuadFuelRod);
		componentData.put(ComponentSubType.MOXUraniumFuelRod, moxUraniumFuelRod);
		componentData.put(ComponentSubType.MOXUraniumDualFuelRod, moxUraniumDualFuelRod);
		componentData.put(ComponentSubType.MOXUraniumQuadFuelRod, moxUraniumQuadFuelRod);
		// Neutron Reflectors
		final Integer[] t1NeutronReflector = {30000};
		componentData.put(ComponentSubType.T1NeutronReflector, t1NeutronReflector);
		// Coolant Cells
		final Integer[] coolantCell10k = {10000};
		final Integer[] heliumCoolantCell360k = {360000};
		componentData.put(ComponentSubType.CoolantCell10k, coolantCell10k);
		componentData.put(ComponentSubType.HeliumCoolantCell360k, heliumCoolantCell360k);
		
	}
	
	public ReactorComponent generateComponent(ComponentSubType type, int posX, int posY) {
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
			rc = new FuelRod(posX, posY, ComponentSubType.DepletedUraniumFuelRod, data);
			break;
		case UraniumDualFuelRod:
			rc = new FuelRod(posX, posY, ComponentSubType.DepletedUraniumDualFuelRod, data);
			break;
		case UraniumQuadFuelRod:
			rc = new FuelRod(posX, posY, ComponentSubType.DepletedUraniumQuadFuelRod, data);
			break;
		case ThoriumFuelRod:
			rc = new FuelRod(posX, posY, ComponentSubType.DepletedThoriumFuelRod, data);
			break;
		case ThoriumDualFuelRod:
			rc = new FuelRod(posX, posY, ComponentSubType.DepletedThoriumDualFuelRod, data);
			break;
		case ThoriumQuadFuelRod:
			rc = new FuelRod(posX, posY, ComponentSubType.DepletedThoriumQuadFuelRod, data);
			break;
		case MOXUraniumFuelRod:
			rc = new FuelRod(posX, posY, ComponentSubType.DepletedMOXUraniumFuelRod, data);
			break;
		case MOXUraniumDualFuelRod:
			rc = new FuelRod(posX, posY, ComponentSubType.DepletedMOXUraniumDualFuelRod, data);
			break;
		case MOXUraniumQuadFuelRod:
			rc = new FuelRod(posX, posY, ComponentSubType.DepletedMOXUraniumQuadFuelRod, data);
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
		case HeliumCoolantCell360k:
			rc = new CoolantCell(posX, posY, data);
			break;
		}
		return rc;
	}
}
