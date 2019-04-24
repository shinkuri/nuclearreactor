package logic;

import java.util.HashMap;
import java.util.Map.Entry;

import component_blueprints.AbstractCoolantCell;
import component_blueprints.AbstractDepletedFuelRod;
import component_blueprints.AbstractFuelRod;
import component_blueprints.AbstractHeatExchanger;
import component_blueprints.AbstractHeatVent;
import component_blueprints.AbstractNeutronReflector;
import component_blueprints.ReactorComponent;
import logic.ComponentFactory.ComponentType;

/*
 * TODO: Update caching method
 */
public class Reactor implements Runnable{
	
	private final ComponentFactory componentFactory = new ComponentFactory();
	
	private static final int EXPLOSION_THRESHOLD = 10000;
	private static final int BURN_THRESHOLD = 7000;
	private static final int RADIATION_THRESHOLD = 4000;
	
	private static final int EFFECT_RADIUS = 8; // Center is center of the 5x5x5 cube
	private static final int EXPLOSION_STRENGTH = 1;
	
	private StatusReport statusReport;
	
	private boolean isActive = false;
	private boolean isEUMode = true;
	private int hullHeat = 1;
	
	private int hullVentingCapacity = 0;
	
	private int currentOutputEU = 0;
	private int currentOutputHeat = 0;
	
	private static final int SIZE_X = 9;
	private static final int SIZE_Y = 6;
	private final Grid<AbstractHeatVent> heatVents = new Grid<>(AbstractHeatVent.class, SIZE_X, SIZE_Y);
	private final Grid<AbstractHeatExchanger> heatExchangers = new Grid<>(AbstractHeatExchanger.class, SIZE_X, SIZE_Y);
	private final Grid<AbstractFuelRod> fuelRods = new Grid<>(AbstractFuelRod.class, SIZE_X, SIZE_Y);
	private final Grid<AbstractDepletedFuelRod> depletedRods = new Grid<>(AbstractDepletedFuelRod.class, SIZE_X, SIZE_Y);
	private final Grid<AbstractNeutronReflector> neutronReflectors = new Grid<>(AbstractNeutronReflector.class, SIZE_X, SIZE_Y);
	private final Grid<AbstractCoolantCell> coolantCells = new Grid<>(AbstractCoolantCell.class, SIZE_X, SIZE_Y);
	
	@Override
	public void run() {
		while(isActive) {
			long delta = System.currentTimeMillis();
			tick(statusReport);
			delta = delta - System.currentTimeMillis();
			try {
				Thread.sleep(1000 - delta);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isRunning() {
		return isActive;
	}
	
	public void setOn() {
		isActive = true;
	}
	
	public void setOff() {
		isActive = false;
	}

	public void hookReactor(StatusReport statusReport) {
		this.statusReport = statusReport;
		final Thread simulationThread = new Thread(this);
		simulationThread.start();
	}
	
	public void insertComponent(ComponentType type, int posX, int posY) {
		final ReactorComponent rc = componentFactory.generateComponent(type, posX, posY);
		if(rc instanceof AbstractHeatVent) {
			heatVents.put(rc);
		}
		else if(rc instanceof AbstractHeatExchanger) {
			heatExchangers.put(rc);
		}
		else if(rc instanceof AbstractFuelRod) {
			fuelRods.put(rc);
		}
		else if(rc instanceof AbstractDepletedFuelRod) {
			depletedRods.put(rc);
		}
		else if(rc instanceof AbstractNeutronReflector) {
			neutronReflectors.put(rc);
		}
		else if(rc instanceof AbstractCoolantCell) {
			coolantCells.put(rc);
		}
		// Update cache
		calculateStats();
	}
	
	/**
	 * If a component with heat capacity is destroyed, all the heat it holds is released into the hull
	 * @param rc : Component to be destroyed
	 */
	public void removeComponent(ReactorComponent rc) {
		System.out.println("removed component " +rc.getX() +"/" +rc.getY());
		// TODO: Fix this really dumb way to remove components.
		if(rc instanceof AbstractNeutronReflector) {			
			neutronReflectors.remove(rc);
		}
		else if(rc instanceof AbstractHeatVent) {
			AbstractHeatVent c = (AbstractHeatVent) rc;
			hullHeat += c.getHeat();
			heatVents.remove(rc);
		}
		else if(rc instanceof AbstractHeatExchanger) {
			AbstractHeatExchanger c = (AbstractHeatExchanger) rc;
			hullHeat += c.getHeat();
			heatExchangers.remove(rc);
		}
		else if(rc instanceof AbstractCoolantCell) {
			AbstractCoolantCell c = (AbstractCoolantCell) rc;
			hullHeat += c.getHeat();
			coolantCells.remove(rc);
		}
		else if(rc instanceof AbstractFuelRod) {
			AbstractFuelRod c = (AbstractFuelRod) rc;
			depletedRods.put(componentFactory.generateComponent(c.getDepletedRod(), c.getX(), c.getY()));
			fuelRods.remove(rc);
		}
		// Update cache
		calculateStats();
	}
	
	public void tick(StatusReport statusReport) {
		if(hullHeat >= RADIATION_THRESHOLD) {
			// Apply radiation & nausea effect
		}
		if(hullHeat >= BURN_THRESHOLD) {
			// Set nearby entities on fire
		}
		if(hullHeat >= EXPLOSION_THRESHOLD) {
			// BOOM
		}
		
		if(isActive) {
			simulate();
			statusReport.setHullHeat(hullHeat);
			statusReport.setCurrentEUt(currentOutputEU / 20);
			statusReport.setCurrentHUs(currentOutputHeat);
			if(isEUMode) {
				// push EU to dynamo hatch
			} else {
				// push heat to either heat hatch or convert cooling fluid
			}
		}
	}
	
	/**
	 * Process reactor components in this order: <br>
	 * 1) Calculate all neutron pulses <br>
	 * 2) Fuel Rods <br> 
	 * 3) Heat Exchanger <br>
	 * 4) Heat Vents <br>
	 * 5) Update output values <br>
	 * 		3 & 4 are heat management components <br>
	 */
	private void simulate() {
		currentOutputEU = 0;
		currentOutputHeat = 0;
		
		// 1)
		for(AbstractNeutronReflector refl : neutronReflectors.getAll()) {
			for(AbstractFuelRod c : fuelRods.getNeighbours(refl)) {
				if(c != null) {
					refl.applyReflectedPulses(c);
					if(!refl.isAlive()) {
						removeComponent(refl);
						break;
					}
				}
			}
		}
		for(AbstractFuelRod rod: fuelRods.getAll()) {
			for(AbstractFuelRod c : fuelRods.getNeighbours(rod)) {
				if(c != null) {
					c.addNeutronPulse(rod.getNEUTRON_PULSES_EMITTED());					
				}
			}
		}
		// 2)
		for(AbstractFuelRod rod : fuelRods.getAll()) {
			currentOutputEU += rod.getElectricityPerSecond(hullHeat, EXPLOSION_THRESHOLD);
			
			int heat = rod.getHeatPerSecond(hullHeat, EXPLOSION_THRESHOLD);
			final int heatPerSide = heat / fuelRods.getMaxNeighbours(rod);
			
			for(AbstractHeatExchanger c : heatExchangers.getNeighbours(rod)) {
				if(c != null) {
					heat -= c.tryAddHeat(heatPerSide);
					if(!c.isAlive()) {
						removeComponent(c);
					}
				}
			}
			for(AbstractHeatVent c : heatVents.getNeighbours(rod)) {
				if(c != null) {
					heat -= c.tryAddHeat(heatPerSide);
					if(!c.isAlive()) {
						removeComponent(c);
					}
				}
			}
			for(AbstractCoolantCell c : coolantCells.getNeighbours(rod)) {
				if(c != null) {
					heat -= c.tryAddHeat(heatPerSide);
					if(!c.isAlive()) {
						removeComponent(c);
					}
				}
			}
			hullHeat += heat;
			rod.use();
			if(!rod.isAlive()) {
				removeComponent(rod);
			}
		}
		// 3)
		for(AbstractHeatExchanger heatExch : heatExchangers.getAll()) {
			// Component exchange rate
			// - get heat of all adjacent components
			HashMap<ReactorComponent, Double> targets = new HashMap<>();
			targets.put(heatExch, (double) heatExch.getHeat());
			for(AbstractHeatExchanger c : heatExchangers.getNeighbours(heatExch)) {
				if(c != null) {
					targets.put(c, (double) c.getHeat());
				}
			}
			for(AbstractHeatVent c : heatVents.getNeighbours(heatExch)) {
				if(c != null) {
					targets.put(c, (double) c.getHeat());
				}
			}
			for(AbstractCoolantCell c : coolantCells.getNeighbours(heatExch)) {
				if(c != null) {
					targets.put(c, (double) c.getHeat());
				}
			}
			// - do the thing
			heatExch.calculateFractionsOfThrougput(targets);
			// - take calculated fraction of heat from each component and distribute it to the others
			// TODO: Maybe fix this type-checking insanity?
			for(Entry<ReactorComponent, Double> cTake : targets.entrySet()) {
				// Take heat from the currently processed component
				int removedHeat = 0;
				if(cTake.getKey() instanceof AbstractHeatExchanger) {
					final AbstractHeatExchanger c = (AbstractHeatExchanger) cTake.getKey();
					removedHeat = c.tryRemoveHeat(cTake.getValue().intValue());
				}
				else if(cTake.getKey() instanceof AbstractHeatVent) {
					final AbstractHeatVent c = (AbstractHeatVent) cTake.getKey();
					removedHeat = c.tryRemoveHeat(cTake.getValue().intValue());
				}
				else if(cTake.getKey() instanceof AbstractCoolantCell) {
					final AbstractCoolantCell c = (AbstractCoolantCell) cTake.getKey();
					removedHeat = c.tryRemoveHeat(cTake.getValue().intValue());
				}
				// Try to distribute all the heat that was taken
				for(Entry<ReactorComponent, Double> cDist : targets.entrySet()) {
					if(!cDist.getKey().equals(cTake.getKey())) {
						if(cDist.getKey() instanceof AbstractHeatExchanger) {
							final AbstractHeatExchanger c = (AbstractHeatExchanger) cTake.getKey();
							removedHeat -= c.tryAddHeat(removedHeat / (targets.size() - 1));
						}
						else if(cDist.getKey() instanceof AbstractHeatVent) {
							final AbstractHeatVent c = (AbstractHeatVent) cTake.getKey();
							removedHeat -= c.tryAddHeat(removedHeat / (targets.size() - 1));
						}
						else if(cDist.getKey() instanceof AbstractCoolantCell) {
							final AbstractCoolantCell c = (AbstractCoolantCell) cTake.getKey();
							removedHeat -= c.tryAddHeat(removedHeat / (targets.size() - 1));
						}
						if(!cDist.getKey().isAlive()) {
							removeComponent(cDist.getKey());
						}
					}
				}
				// Add heat that couldn't be distributed back to the source component
				if(removedHeat > 0) {
					if(cTake.getKey() instanceof AbstractHeatExchanger) {
						final AbstractHeatExchanger c = (AbstractHeatExchanger) cTake.getKey();
						c.tryAddHeat(removedHeat);
					}
					else if(cTake.getKey() instanceof AbstractHeatVent) {
						final AbstractHeatVent c = (AbstractHeatVent) cTake.getKey();
						c.tryAddHeat(removedHeat);
					}
					else if(cTake.getKey() instanceof AbstractCoolantCell) {
						final AbstractCoolantCell c = (AbstractCoolantCell) cTake.getKey();
						c.tryAddHeat(removedHeat);
					}
					if(!cTake.getKey().isAlive()) {
						removeComponent(cTake.getKey());
					}
				}
			}
			// Hull exchange rate
			// TODO: Doesn't quite behave the same as the above procedure, I think
			// - see above procedure
			final int summedHeat = hullHeat + heatExch.getHeat();
			int reactorHeatFraction = hullHeat / summedHeat;
			int exchangerHeatFraction = heatExch.getHeat() / summedHeat;
			reactorHeatFraction = reactorHeatFraction * heatExch.getHULL_EXCHANGE_RATE();
			exchangerHeatFraction = exchangerHeatFraction * heatExch.getHULL_EXCHANGE_RATE();
			// - move heat
			hullHeat -= reactorHeatFraction;
			hullHeat += exchangerHeatFraction;
			heatExch.tryRemoveHeat(exchangerHeatFraction);
			heatExch.tryAddHeat(reactorHeatFraction);
			if(!heatExch.isAlive()) {
				removeComponent(heatExch);
			}
		}
		// 4)
		int totalHeatVented = 0;
		final int hullHeatIntakeTotal = Math.min(hullVentingCapacity, hullHeat);
		for(AbstractHeatVent vent : heatVents.getAll()) {
			// Hull vent rate
			final int heatIntake = (hullHeatIntakeTotal == 0) ? 0 : vent.getHULL_VENT_RATE() / hullHeatIntakeTotal;
			vent.tryAddHeat(heatIntake);
			if(!vent.isAlive()) {
				removeComponent(vent);
				continue;
			}
			// Component vent rate
			final int ratePerSide = vent.getCOMPONENT_VENT_RATE() / heatVents.getMaxNeighbours(vent);
			for(AbstractHeatExchanger c : heatExchangers.getNeighbours(vent)) {
				if(c != null) {
					c.tryRemoveHeat(ratePerSide);
				}
			}
			for(AbstractHeatVent c : heatVents.getNeighbours(vent)) {
				if(c != null) {
					c.tryRemoveHeat(ratePerSide);
				}
			}
			for(AbstractCoolantCell c : coolantCells.getNeighbours(vent)) {
				if(c != null) {
					c.tryRemoveHeat(ratePerSide);				}
			}
			// Self vent rate
			totalHeatVented += vent.tryRemoveHeat(vent.getSELF_VENT_RATE());
		}
		
		currentOutputHeat = totalHeatVented;
	}
	
	/**
	 * Cache some values that never change unless components change
	 */
	private void calculateStats() {
		// reset
		hullVentingCapacity = 0;
		
		for(AbstractHeatVent vent : heatVents.getAll()) {
			hullVentingCapacity += vent.getHULL_VENT_RATE();
		}
	}
	
}
