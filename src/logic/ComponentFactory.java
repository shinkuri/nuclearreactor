package logic;

import java.util.HashMap;

import component_blueprints.CoolantCell;
import component_blueprints.DepletedFuelRod;
import component_blueprints.EmergencyFoam;
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
		HeliumCoolantCell360k(ComponentType.CoolantCell, "HeliumCoolantCell360k"),
		// Emergency Foam
		Foam85(ComponentType.EmergencyFoam, "Foam85"),
		Foam50(ComponentType.EmergencyFoam, "Foam50");
		
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
	
	private static HashMap<ComponentSubType, Double[]> componentData = new HashMap<>();
	static {
		// Specific Item to Type mapping
		//typeMapping.put(key, value)
		// Heat Vents
		final Double[] t1HeatVent = 			{1000d, 0d, 0d, 6d};
		final Double[] t2HeatVent = 			{1000d, 0d, 0d, 12d};
		final Double[] componentHeatVent = 	{1000d, 12d, 0d, 0d};
		final Double[] reactorHeatVent = 		{1000d, 0d, 5d, 5d};
		final Double[] overclockedHeatVent = 	{1000d, 0d, 36d, 20d};
		componentData.put(ComponentSubType.T1HeatVent, t1HeatVent);
		componentData.put(ComponentSubType.T2HeatVent, t2HeatVent);
		componentData.put(ComponentSubType.ComponentHeatVent, componentHeatVent);
		componentData.put(ComponentSubType.ReactorHeatVent, reactorHeatVent);
		componentData.put(ComponentSubType.OverclockedHeatVent, overclockedHeatVent);
		// Heat Exchangers
		final Double[] t1HeatExchanger = 			{2500d, 12d, 4d};
		final Double[] t2HeatExchanger = 			{10000d, 24d, 8d};
		final Double[] componentHeatExchanger = 	{5000d, 36d, 0d};
		final Double[] reactorHeatExchanger = 		{5000d, 0d, 72d};
		componentData.put(ComponentSubType.T1HeatExchanger, t1HeatExchanger);
		componentData.put(ComponentSubType.T2HeatExchanger, t2HeatExchanger);
		componentData.put(ComponentSubType.ComponentHeatExchanger, componentHeatExchanger);
		componentData.put(ComponentSubType.ReactorHeatExchanger, reactorHeatExchanger);
		// Fuel Rods
		final Double[] uraniumFuelRod = 		{20000d, 4d, 1d, 100d, 1d, 1d};
		final Double[] uraniumDualFuelRod = 	{20000d, 24d, 1d, 400d, 1d, 2d};
		final Double[] uraniumQuadFuelRod = 	{20000d, 96d, 1d, 1200d, 1d, 4d};
		final Double[] thoriumFuelRod = 		{100000d, 1d, 1d, 20d, 1d, 1d};
		final Double[] thoriumDualFuelRod = 	{100000d, 6d, 1d, 80d, 1d, 2d};
		final Double[] thoriumQuadFuelRod = 	{100000d, 24d, 1d, 240d, 1d, 4d};
		final Double[] moxUraniumFuelRod = 	{10000d, 4d, 5d, 100d, 5d, 1d};
		final Double[] moxUraniumDualFuelRod = {10000d, 24d, 5d, 400d, 5d, 2d};
		final Double[] moxUraniumQuadFuelRod = {10000d, 96d, 5d, 1200d, 5d, 4d};
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
		final Double[] t1NeutronReflector = {30000d};
		componentData.put(ComponentSubType.T1NeutronReflector, t1NeutronReflector);
		// Coolant Cells
		final Double[] coolantCell10k = {10000d};
		final Double[] heliumCoolantCell360k = {360000d};
		componentData.put(ComponentSubType.CoolantCell10k, coolantCell10k);
		componentData.put(ComponentSubType.HeliumCoolantCell360k, heliumCoolantCell360k);
		// Emergency Foam
		final Double[] foam85 = {0.85d};
		final Double[] foam50 = {0.50d};
		componentData.put(ComponentSubType.Foam85, foam85);
		componentData.put(ComponentSubType.Foam50, foam50);
		
	}
	
	public ReactorComponent generateComponent(ComponentSubType type, int posX, int posY) {
		ReactorComponent rc = null;
		final Double[] data = componentData.get(type);
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
		case Foam50:
		case Foam85:
			rc = new EmergencyFoam(posX, posY, data);
			break;
		}
		return rc;
	}
}
