package logic;

import java.util.ArrayList;

import component_blueprints.ICoolantCell;
import component_blueprints.IFuelRod;
import component_blueprints.IHeatExchanger;
import component_blueprints.IHeatVent;
import component_blueprints.INeutronReflector;
import component_blueprints.ReactorComponent;
import component_blueprints.ReactorComponent.Sides;
import component_data.UraniumFuelRod;

public class Reactor {
	
	private static final int EXPLOSION_THRESHOLD = 10000;
	private static final int BURN_THRESHOLD = 7000;
	private static final int RADIATION_THRESHOLD = 4000;
	
	private static final int EFFECT_RADIUS = 8; // Center is center of the 5x5x5 cube
	private static final int EXPLOSION_STRENGTH = 1;
	
	private boolean isActive = false;
	private boolean isFluidMode = false;
	private int reactorHeat = 0;
	private int tickCounter = 0;
	
	private int hullVentingCapacity = 0;
	
	private int currentOutputEU = 0;
	private int currentOutputHeat = 0;
	
	private final ReactorComponent[][] reactorChamber = new ReactorComponent[9][6];
	private final ArrayList<IFuelRod> fuelRods = new ArrayList<>();
	private final ArrayList<IHeatVent> heatVents = new ArrayList<>();
	private final ArrayList<IHeatExchanger> heatExchangers = new ArrayList<>();
	private final ArrayList<ICoolantCell> coolantCells = new ArrayList<>();
	private final ArrayList<INeutronReflector> neutronReflectors = new ArrayList<>();
	
	public void addComponent(int posX, int posY, ComponentList type) {
		switch(type) {
		case UraniumFuelRod:
			UraniumFuelRod c = new UraniumFuelRod(posX, posY);
			reactorChamber[posX][posY] = c;
		}
	}
	
	public void tick() {
		if(reactorHeat >= 4000) {
			// Apply radiation & nausea effect
		}
		if(reactorHeat >= 7000) {
			// Set nearby entities on fire
		}
		if(reactorHeat >= 10000) {
			// BOOM
		}
		
		if(!isActive) {
			if(tickCounter == 20) {
				simulate();
			}
			
			if(isFluidMode) {
				// push heat to either heat hatch or convert cooling fluid
			} else {
				// push EU to dynamo hatch
			}
		}
	}
	
	/**
	 * Process reactor components in this order: <br>
	 * 1) Fuel Rods <br>
	 * 2) Neutron Reflectors <br>
	 * 3) Heat Exchanger <br>
	 * 4) Heat Vents <br>
	 * 5) Coolant Cells <br>
	 */
	private void simulate() {
		// 1)
		for(IFuelRod rod : fuelRods) {
			
		}
		// 2)
		for(INeutronReflector refl : neutronReflectors) {
			
		}
		// 3)
		for(IHeatExchanger heatExch : heatExchangers) {
			
		}
		// 4)
		final int hullHeatIntakeTotal = Math.min(hullVentingCapacity, reactorHeat);
		for(IHeatVent vent : heatVents) {
			final ReactorComponent v = (ReactorComponent) vent;
			// Hull vent rate
			final int heatIntake = vent.getReactorVentRate() / hullHeatIntakeTotal;
			v.doDamage(heatIntake);
			// Component vent rate
			final int ratePerSide = vent.getComponentVentRate() / v.getSideCount();
			if(v.getSide(Sides.top)) {
				
			}
			// Self vent rate
			v.doDamage(-vent.getSelfVentRate());
		}
		// 5)
		for(ICoolantCell cool : coolantCells) {
			
		}
	}
	
	/**
	 * Cache some values that never change unless components change
	 */
	private void calculateStats() {
		// reset
		hullVentingCapacity = 0;
		
		for(IHeatVent vent : heatVents) {
			hullVentingCapacity += vent.getReactorVentRate();
		}
	}
	
	public enum ComponentList {
		// Fuel rods
		UraniumFuelRod, UraniumDualFuelRod, UraniumQuadFuelRod,
		ThoriumFuelRod, ThoriumDualFuelRod, ThoriumQuadFuelRod,
		
		// Heat vents
		HeatVent, AdvancedHeatVent, ReactorHeatVent, OverclockedHeatVent, ComponentHeatVent,
		
		// Heat exchanger
		HeatExchanger, AdvancedHeatExchanger, ReactorHeatExchanger, ComponentHeatExchanger,
		
		// Coolant cells
		CoolantCell10k,
		
		// Neutron reflector
		NeutronReflector
	}
}
