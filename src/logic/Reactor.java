package logic;

import component_blueprints.ICoolantCell;
import component_blueprints.IDepletedFuelRod;
import component_blueprints.IFuelRod;
import component_blueprints.IHeatExchanger;
import component_blueprints.IHeatVent;
import component_blueprints.INeutronReflector;
import component_blueprints.ReactorComponent;
import component_data.UraniumFuelRod;

public class Reactor {
	
	private static final int EXPLOSION_THRESHOLD = 10000;
	private static final int BURN_THRESHOLD = 7000;
	private static final int RADIATION_THRESHOLD = 4000;
	
	private static final int EFFECT_RADIUS = 8; // Center is center of the 5x5x5 cube
	private static final int EXPLOSION_STRENGTH = 1;
	
	private static final int EU_PER_EXTRA_PULSE = 5;
	private static final int HEAT_PER_EXTRA_PULSE = 4;
	
	private boolean isActive = false;
	private boolean isFluidMode = false;
	private int reactorHeat = 0;
	private int tickCounter = 0;
	
	private int hullVentingCapacity = 0;
	
	private int currentOutputEU = 0;
	private int currentOutputHeat = 0;
	
	private static final int SIZE_X = 9;
	private static final int SIZE_Y = 6;
	private final Grid<IFuelRod> fuelRods = new Grid<>(IFuelRod.class, SIZE_X, SIZE_Y);
	private final Grid<IHeatVent> heatVents = new Grid<>(IHeatVent.class, SIZE_X, SIZE_Y);
	private final Grid<IHeatExchanger> heatExchangers = new Grid<>(IHeatExchanger.class, SIZE_X, SIZE_Y);
	private final Grid<ICoolantCell> coolantCells = new Grid<>(ICoolantCell.class, SIZE_X, SIZE_Y);
	private final Grid<INeutronReflector> neutronReflectors = new Grid<>(INeutronReflector.class, SIZE_X, SIZE_Y);
	private final Grid<IDepletedFuelRod> depletedRods = new Grid<>(IDepletedFuelRod.class, SIZE_X, SIZE_Y);
	
	public void addComponent(int posX, int posY, ComponentList type) {
		switch(type) {
		case UraniumFuelRod:
			UraniumFuelRod c = new UraniumFuelRod(posX, posY);
			//reactorChamber[posX][posY] = c;
		}
	}
	
	public void removeComponent(int posX, int posY) {
		// really dumb way of doing this
		final IFuelRod fuelRod = fuelRods.get(posX, posY);
		if(fuelRod != null) {
			depletedRods.put(fuelRod.getDepletedRod(), posX, posY);
			fuelRods.remove(posX, posY);
			return;
		}
		heatVents.remove(posX, posY);
		heatExchangers.remove(posX, posY);
		coolantCells.remove(posX, posY);
		neutronReflectors.remove(posX, posY);
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
	 * 1) Neutron Reflectors <br>
	 * 2) Fuel Rods <br> 
	 * 3) Heat Exchanger <br>
	 * 4) Heat Vents <br>
	 * 5) Coolant Cells <br>
	 * 		3, 4, 5 are heat management components <br>
	 */
	private void simulate() {
		currentOutputEU = 0;
		currentOutputHeat = 0;
		
		// 1)
		for(INeutronReflector refl : neutronReflectors.getAll()) {
			final ReactorComponent r = (ReactorComponent) refl;
			for(IFuelRod c : fuelRods.getNeighbours(r.getX(), r.getY())) {
				if(c != null) {
					c.addNeutronPulse(refl.getNeutronPulsesEmitted());
					if(r.doDamage(refl.getNeutronPulsesEmitted()) == -1) {
						removeComponent(r.getX(), r.getY());
					}
				}
			}
		}
		// 2) TODO: Implement heat going into hull if not transferred away
		for(IFuelRod rod : fuelRods.getAll()) {
			final ReactorComponent r = (ReactorComponent) rod;
			for(IFuelRod c : fuelRods.getNeighbours(r.getX(), r.getY())) {
				if(c != null) {
					c.addNeutronPulse(rod.getNeutronPulsesEmitted());					
				}
			}
			currentOutputEU += rod.getElectricityPerSecond() + (EU_PER_EXTRA_PULSE * rod.getNeutronPulses());
			
			final int heatPerSide = rod.getHeatPerSecond() / fuelRods.getMaxNeighbours(r.getX(), r.getY());
			for(IHeatExchanger c : heatExchangers.getNeighbours(r.getX(), r.getY())) {
				if(c != null) {
					if(c.addHeat(heatPerSide) == -1) {
						final ReactorComponent he = (ReactorComponent) c;
						removeComponent(he.getX(), he.getY());
					}
				}
			}
			for(IHeatVent c : heatVents.getNeighbours(r.getX(), r.getY())) {
				if(c != null) {
					if(c.addHeat(heatPerSide) == -1) {
						final ReactorComponent h = (ReactorComponent) c;
						removeComponent(h.getX(), h.getY());
					}
				}
			}
			for(ICoolantCell c : coolantCells.getNeighbours(r.getX(), r.getY())) {
				if(c != null) {
					if(c.addHeat(heatPerSide) == -1) {
						final ReactorComponent co = (ReactorComponent) c;
						removeComponent(co.getX(), co.getY());
					}
				}
			}
			
			if(r.doDamage(1) == -1) {
				removeComponent(r.getX(), r.getY());
			}
		}
		// 3)
		for(IHeatExchanger heatExch : heatExchangers.getAll()) {
			// Component exchange rate
			
			// Hull exchange rate
			
		}
		// 4)
		final int hullHeatIntakeTotal = Math.min(hullVentingCapacity, reactorHeat);
		for(IHeatVent vent : heatVents.getAll()) {
			final ReactorComponent v = (ReactorComponent) vent;
			// Hull vent rate
			final int heatIntake = vent.getReactorVentRate() / hullHeatIntakeTotal;
			if(vent.addHeat(heatIntake) == -1) {
				removeComponent(v.getX(), v.getY());
				continue;
			}
			// Component vent rate
			final int ratePerSide = vent.getComponentVentRate() / heatVents.getMaxNeighbours(v.getX(), v.getY());
			for(IHeatExchanger c : heatExchangers.getNeighbours(v.getX(), v.getY())) {
				if(c != null) {
					c.removeHeat(ratePerSide);
				}
			}
			for(IHeatVent c : heatVents.getNeighbours(v.getX(), v.getY())) {
				if(c != null) {
					c.removeHeat(ratePerSide);
				}
			}
			for(ICoolantCell c : coolantCells.getNeighbours(v.getX(), v.getY())) {
				if(c != null) {
					c.removeHeat(ratePerSide);
				}
			}
			// Self vent rate
			vent.removeHeat(vent.getSelfVentRate());
		}
		// 5)
		for(ICoolantCell cool : coolantCells.getAll()) {
			// passive
		}
	}
	
	/**
	 * Cache some values that never change unless components change
	 */
	private void calculateStats() {
		// reset
		hullVentingCapacity = 0;
		
		for(IHeatVent vent : heatVents.getAll()) {
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
