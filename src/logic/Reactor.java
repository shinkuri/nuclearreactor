package logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

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
	private static final int HEAT_PER_EXTRA_PULSE = 4; // TODO: heat per pulse
	private static final int MAX_MOX_BOOST = 10;
	
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
	private final Grid<IFuelRod> moxRods = new Grid<>(IFuelRod.class, SIZE_X, SIZE_Y);
	
	public void addComponent(int posX, int posY, ComponentList type) {
		switch(type) {
		case UraniumFuelRod:
			UraniumFuelRod c = new UraniumFuelRod(posX, posY);
		}
		
		calculateStats();
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
		
		calculateStats();
	}
	
	public void tick() {
		if(reactorHeat >= RADIATION_THRESHOLD) {
			// Apply radiation & nausea effect
		}
		if(reactorHeat >= BURN_THRESHOLD) {
			// Set nearby entities on fire
		}
		if(reactorHeat >= EXPLOSION_THRESHOLD) {
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
	 * 1) Neutron Reflectors -> calculate all neutron pulses, including those caused by rods <br>
	 * 2) Fuel Rods <br> 
	 * 3) MOX Fuel Rods <br>
	 * 4) Heat Exchanger <br>
	 * 5) Heat Vents <br>
	 * 6) Coolant Cells <br>
	 * 		4, 5, 6 are heat management components <br>
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
			for(IFuelRod c : moxRods.getNeighbours(r.getX(), r.getY())) {
				if(c != null) {
					c.addNeutronPulse(refl.getNeutronPulsesEmitted());
					if(r.doDamage(refl.getNeutronPulsesEmitted()) == -1) {
						removeComponent(r.getX(), r.getY());
					}
				}
			}
		}
		for(IFuelRod rod: fuelRods.getAll()) {
			final ReactorComponent r = (ReactorComponent) rod;
			for(IFuelRod c : fuelRods.getNeighbours(r.getX(), r.getY())) {
				if(c != null) {
					c.addNeutronPulse(rod.getNeutronPulsesEmitted());					
				}
			}
			for(IFuelRod c : moxRods.getNeighbours(r.getX(), r.getY())) {
				if(c != null) {
					c.addNeutronPulse(rod.getNeutronPulsesEmitted());					
				}
			}
		}
		for(IFuelRod rod: moxRods.getAll()) {
			final ReactorComponent r = (ReactorComponent) rod;
			for(IFuelRod c : moxRods.getNeighbours(r.getX(), r.getY())) {
				if(c != null) {
					c.addNeutronPulse(rod.getNeutronPulsesEmitted());					
				}
			}
			for(IFuelRod c : fuelRods.getNeighbours(r.getX(), r.getY())) {
				if(c != null) {
					c.addNeutronPulse(rod.getNeutronPulsesEmitted());					
				}
			}
		}
		
		// 2) TODO: Implement heat going into hull if not transferred away
		for(IFuelRod rod : fuelRods.getAll()) {
			final ReactorComponent r = (ReactorComponent) rod;
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
		for(IFuelRod rod: moxRods.getAll()) {
			final ReactorComponent r = (ReactorComponent) rod;
			// MOX boost rate scaling exponentially from 1x to MAX_MOX_BOOST depending on current hull heat
			final double multiplier = ((MAX_MOX_BOOST - 1) / Math.pow(EXPLOSION_THRESHOLD, 2)) * Math.pow(reactorHeat, 2) + 1;
			currentOutputEU += (rod.getElectricityPerSecond() + (EU_PER_EXTRA_PULSE * rod.getNeutronPulses())) * multiplier;
			
			final int heatPerSide = (int) ((multiplier * rod.getHeatPerSecond()) / fuelRods.getMaxNeighbours(r.getX(), r.getY()));
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
		// 4)
		// TODO: check whether ints are suitable for fractions or not
		for(IHeatExchanger heatExch : heatExchangers.getAll()) {
			final ReactorComponent h = (ReactorComponent) heatExch;
			// Component exchange rate
			// - get heat of all adjacent components and the exchanger itself
			HashMap<ReactorComponent, Integer> set = new HashMap<>();
			set.put(h, h.getDurability());
			for(IHeatExchanger c : heatExchangers.getNeighbours(h.getX(), h.getY())) {
				if(c != null) {
					final ReactorComponent rc = (ReactorComponent) c;
					set.put(rc, rc.getDurability());
				}
			}
			for(IHeatVent c : heatVents.getNeighbours(h.getX(), h.getY())) {
				if(c != null) {
					final ReactorComponent rc = (ReactorComponent) c;
					set.put(rc, rc.getDurability());
				}
			}
			for(ICoolantCell c : coolantCells.getNeighbours(h.getX(), h.getY())) {
				if(c != null) {
					final ReactorComponent rc = (ReactorComponent) c;
					set.put(rc, rc.getDurability());
				}
			}
			// - calculate fraction of the total heat
			int summedComponentHeat = 0;
			for(int c : set.values()) {
				summedComponentHeat += c;
			}
			for(Entry<ReactorComponent, Integer> c : set.entrySet()) {
				final int fraction = c.getValue() / summedComponentHeat;
				set.put(c.getKey(), fraction);
			}
			// - calculate fraction of total exchanger throughput
			for(Entry<ReactorComponent, Integer> c : set.entrySet()) {
				final int fraction = c.getValue() * heatExch.getComponentExchangeRate();
				set.put(c.getKey(), fraction);
			}
			// - take calculated fraction of heat from each component and distribute it to the others
			for(Entry<ReactorComponent, Integer> cTake : set.entrySet()) {
				cTake.getKey().doDamage(-cTake.getValue());
				for(Entry<ReactorComponent, Integer> cDist : set.entrySet()) {
					if(!cDist.getKey().equals(cTake.getKey())) {
						if(cDist.getKey().doDamage(cDist.getValue() / (set.size() - 1)) == -1) {
							removeComponent(cDist.getKey().getX(), cDist.getKey().getY());
						}
						
					}
				}
			}
			// Hull exchange rate
			// - see above procedure
			final int summedHeat = reactorHeat + h.getDurability();
			int reactorHeatFraction = reactorHeat / summedHeat;
			int exchangerHeatFraction = h.getDurability() / summedHeat;
			reactorHeatFraction = reactorHeatFraction * heatExch.getHullExchangeRate();
			exchangerHeatFraction = exchangerHeatFraction * heatExch.getHullExchangeRate();
			// - move heat
			reactorHeat -= reactorHeatFraction;
			if(heatExch.addHeat(reactorHeatFraction) == -1) {
				removeComponent(h.getX(), h.getY());
			}
			heatExch.removeHeat(exchangerHeatFraction);
			reactorHeat += exchangerHeatFraction;
		}
		// 5)
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
		// 6)
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
