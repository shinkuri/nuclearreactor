package logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.function.Function;

import component_blueprints.CoolantCell;
import component_blueprints.DepletedFuelRod;
import component_blueprints.EmergencyFoam;
import component_blueprints.FuelRod;
import component_blueprints.HeatExchanger;
import component_blueprints.HeatManagementComponent;
import component_blueprints.HeatVent;
import component_blueprints.NeutronReflector;
import component_blueprints.ReactorComponent;
import logic.ComponentFactory.ComponentSubType;

/*
 * TODO: Update caching method
 */
public class Reactor implements Runnable{
	
	private final ComponentFactory componentFactory = new ComponentFactory();
	
	private static final double EXPLOSION_THRESHOLD = 10000d;
	private static final double EU_MULTIPLIER = 10d; // GTNH 10x EU boost
		
	private boolean isActive = false;
	private boolean isEUMode = true;
	private double hullHeat = 0d;
	
	private double hullVentingCapacity = 0d;
	private double componentVentingCapacity = 0d;
	
	private double currentOutputEU = 0d;
	private double currentOutputHeat = 0d;
	
	private static final int SIZE_X = 9;
	private static final int SIZE_Y = 6;
	private final Grid<HeatVent> heatVents = new Grid<>(HeatVent.class, SIZE_X, SIZE_Y);
	private final Grid<HeatExchanger> heatExchangers = new Grid<>(HeatExchanger.class, SIZE_X, SIZE_Y);
	private final Grid<FuelRod> fuelRods = new Grid<>(FuelRod.class, SIZE_X, SIZE_Y);
	private final Grid<DepletedFuelRod> depletedRods = new Grid<>(DepletedFuelRod.class, SIZE_X, SIZE_Y);
	private final Grid<NeutronReflector> neutronReflectors = new Grid<>(NeutronReflector.class, SIZE_X, SIZE_Y);
	private final Grid<CoolantCell> coolantCells = new Grid<>(CoolantCell.class, SIZE_X, SIZE_Y);
	private final Grid<EmergencyFoam> emergencyFoams = new Grid<>(EmergencyFoam.class, SIZE_X, SIZE_Y);
	private final Object componentLock = new Object();
	
	@Override
	public void run() {
		while(isActive) {
			long delta = System.currentTimeMillis();
			tick();
			delta = delta - System.currentTimeMillis();
			try {
				Thread.sleep(100 - delta);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void hookReactor() {
		final Thread simulationThread = new Thread(this);
		simulationThread.setName("Reactor simulation thread");
		simulationThread.start();
	}
	
	// =================== SETTERS ========================
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public void setHullHeat(int hullHeat) {
		this.hullHeat = hullHeat;
	}
	
	public void insertComponent(ComponentSubType subType, int posX, int posY) {
		synchronized(componentLock) {
			final ReactorComponent rc = componentFactory.generateComponent(subType, posX, posY);
			
			switch(subType.getType()) {
			case HeatVent: heatVents.put(rc); break;
			case HeatExchanger: heatExchangers.put(rc); break;
			case FuelRod: fuelRods.put(rc); break;
			case DepletedFuelRod: depletedRods.put(rc); break;
			case NeutronReflector: neutronReflectors.put(rc); break;
			case CoolantCell: coolantCells.put(rc); break;
			case EmergencyFoam: emergencyFoams.put(rc); break;
			}
			
			// Update cache
			calculateStats();			
		}
	}
	
	// =================== GETTERS ========================
	public boolean isRunning() {
		return isActive;
	}
	
	public long getHullHeat() {
		return Math.round(hullHeat);
	}
	
	public long getEUOutput() {
		return Math.round(currentOutputEU);
	}
	
	public long getHUOutput() {
		return Math.round(currentOutputHeat);
	}
	
	public ReactorComponent getComponent(int posX, int posY) {
		synchronized(componentLock) {
			if(heatVents.get(posX, posY) != null) {
				return heatVents.get(posX, posY);
			}
			else if(heatExchangers.get(posX, posY) != null) {
				return heatExchangers.get(posX, posY);
			}
			else if(fuelRods.get(posX, posY) != null) {
				return fuelRods.get(posX, posY);
			}
			else if(depletedRods.get(posX, posY) != null) {
				return depletedRods.get(posX, posY);
			}
			else if(neutronReflectors.get(posX, posY) != null) {
				return neutronReflectors.get(posX, posY);
			}
			else if(coolantCells.get(posX, posY) != null) {
				return coolantCells.get(posX, posY);
			}
			else if(emergencyFoams.get(posX, posY) != null) {
				return emergencyFoams.get(posX, posY);
			}
			return null;			
		}
	}
	
	public HashSet<ReactorComponent> getAllComponents(){
		synchronized(componentLock) {
			final HashSet<ReactorComponent> rcs = new HashSet<>();
			
			rcs.addAll(heatVents.getAll());
			rcs.addAll(heatExchangers.getAll());
			rcs.addAll(fuelRods.getAll());
			rcs.addAll(depletedRods.getAll());
			rcs.addAll(neutronReflectors.getAll());
			rcs.addAll(coolantCells.getAll());
			rcs.addAll(emergencyFoams.getAll());
			
			return rcs;
		}
	}
	
	// =================== REMOVERS =======================
	
	public void removeAllComponents() {
		synchronized(componentLock) {
			heatVents.clear();
			heatExchangers.clear();
			fuelRods.clear();
			depletedRods.clear();
			neutronReflectors.clear();
			coolantCells.clear();
			emergencyFoams.clear();
		}
	}
	
	/**
	 * If a component with heat capacity is destroyed, all the heat it holds is released into the hull
	 * @param rc : Component to be destroyed
	 */
	public void removeComponent(ReactorComponent rc, boolean foamed) {
		synchronized(componentLock) {
			System.out.println(
					((foamed) ? "Foamed :" : "Removed ") +rc.getType() +"@" +rc.getX() +"/" +rc.getY());
			
			if(!foamed) {
				if(rc instanceof HeatManagementComponent) {
					HeatManagementComponent hmc = (HeatManagementComponent) rc;
					hullHeat += hmc.getHeat();
				} else if (rc instanceof FuelRod) {
					FuelRod fr = (FuelRod) rc;
					depletedRods.put(componentFactory.generateComponent(fr.getDepletedRod(), fr.getX(), fr.getY()));
				}	
			}
			
			switch(rc.getType()) {
			case HeatVent: heatVents.remove(rc); break;
			case HeatExchanger: heatExchangers.remove(rc); break;
			case FuelRod: fuelRods.remove(rc); break;
			case DepletedFuelRod: depletedRods.remove(rc); break;
			case NeutronReflector: neutronReflectors.remove(rc); break;
			case CoolantCell: coolantCells.remove(rc); break; 
			case EmergencyFoam: emergencyFoams.remove(rc); break;
			}
			
			// Update cache
			calculateStats();
		}
	}
	
	public void tick() {
		if(hullHeat >= EXPLOSION_THRESHOLD) {
			// BOOM
			System.out.println("REACTOR EXPLODED");
			isActive = false;
		}
		
		if(isActive) {
			simulate();
		}
	}
	
	/**
	 * Process reactor components in this order: <br>
	 * 1) Emergency foam
	 * 2) Calculate all neutron pulses <br>
	 * 3) Fuel Rods <br> 
	 * 4) Heat Exchanger <br>
	 * 5) Heat Vents <br>
	 * 		4 & 5 are heat management components <br>
	 */
	private void simulate() {
		currentOutputEU = 0;
		currentOutputHeat = 0;
		
		synchronized(componentLock) {
		// 1)
			for(EmergencyFoam foam : emergencyFoams.getAll()) {
				final double hullHeatPercentage = hullHeat / EXPLOSION_THRESHOLD;
				if(hullHeatPercentage >= foam.getHULL_HEAT_THRESHOLD()) {
					final HashSet<ReactorComponent> set = new HashSet<>();
					set.addAll(heatVents.getNeighbours(foam));
					set.addAll(heatExchangers.getNeighbours(foam));
					set.addAll(coolantCells.getNeighbours(foam));
					set.addAll(fuelRods.getNeighbours(foam));
					set.addAll(depletedRods.getNeighbours(foam));
					set.addAll(neutronReflectors.getNeighbours(foam));
					for(ReactorComponent rc : set) {
						removeComponent(rc, true);
					}
					removeComponent(foam, true);
				}
			}
			
		// 2)
			for(FuelRod rod : fuelRods.getAll()) {
				rod.resetNeutronPulses();
			}
			for(NeutronReflector refl : neutronReflectors.getAll()) {
				for(FuelRod c : fuelRods.getNeighbours(refl)) {
					if(c != null) {
						refl.applyReflectedPulses(c);
						if(!refl.isAlive()) {
							removeComponent(refl, false);
							break;
						}
					}
				}
			}
			for(FuelRod rod: fuelRods.getAll()) {
				for(FuelRod c : fuelRods.getNeighbours(rod)) {
					if(c != null) {
						c.addNeutronPulse(rod.getNEUTRON_PULSES_EMITTED());					
					}
				}
			}			
		// 3)
			for(FuelRod rod : fuelRods.getAll()) {
				currentOutputEU += rod.getElectricityPerSecond(hullHeat, EXPLOSION_THRESHOLD) / 20 * EU_MULTIPLIER;
				
				double heat = rod.getHeatPerSecond(hullHeat, EXPLOSION_THRESHOLD);
				int neighbourCount = 0;
				neighbourCount += heatVents.getNeighbours(rod).size();
				neighbourCount += heatExchangers.getNeighbours(rod).size();
				neighbourCount += coolantCells.getNeighbours(rod).size();
				final double heatPerSide = (neighbourCount > 0) ? (heat / neighbourCount) : heat;
				
				final HashSet<HeatManagementComponent> heatComponents = new HashSet<>();
				heatComponents.addAll(heatVents.getNeighbours(rod));
				heatComponents.addAll(heatExchangers.getNeighbours(rod));
				heatComponents.addAll(coolantCells.getNeighbours(rod));
				for(HeatManagementComponent c : heatComponents) {
					if(c != null) {
						heat -= c.tryAddHeat(heatPerSide);
						if(!c.isAlive()) {
							removeComponent(c, false);
						}
					}
				}
				
				hullHeat += heat;
				rod.use();
				if(!rod.isAlive()) {
					removeComponent(rod, false);
				}
			}			
		// 4)
			for(HeatExchanger heatExch : heatExchangers.getAll()) {
				// Component exchange rate
				// - get heat of all adjacent components
				HashMap<HeatManagementComponent, Double> targets = new HashMap<>();
				targets.put(heatExch, (double) heatExch.getHeat());
				for(HeatExchanger c : heatExchangers.getNeighbours(heatExch)) {
					if(c != null) {
						targets.put(c, (double) c.getHeat());
					}
				}
				for(HeatVent c : heatVents.getNeighbours(heatExch)) {
					if(c != null) {
						targets.put(c, (double) c.getHeat());
					}
				}
				for(CoolantCell c : coolantCells.getNeighbours(heatExch)) {
					if(c != null) {
						targets.put(c, (double) c.getHeat());
					}
				}
				// - do the thing
				heatExch.calculateFractionsOfThrougput(targets);
				// - take calculated fraction of heat from each component and distribute it to the others
				for(Entry<HeatManagementComponent, Double> cTake : targets.entrySet()) {
					// Take heat from the currently processed component
					double removedHeat = 0;
					removedHeat = cTake.getKey().tryRemoveHeat(cTake.getValue().intValue());
					
					// Try to distribute all the heat that was taken
					for(Entry<HeatManagementComponent, Double> cDist : targets.entrySet()) {
						if(!cDist.getKey().equals(cTake.getKey())) {
							
							removedHeat -= cDist.getKey().tryAddHeat(removedHeat / (targets.size() - 1));
						
							if(!cDist.getKey().isAlive()) {
								removeComponent(cDist.getKey(), false);
							}
						}
					}
					// Add heat that couldn't be distributed back to the source component
					if(removedHeat > 0) {
						
						cTake.getKey().tryAddHeat(removedHeat);
						
						if(!cTake.getKey().isAlive()) {
							removeComponent(cTake.getKey(), false);
						}
					}
				}
				// Hull exchange rate
				// TODO: Doesn't quite behave the same as the above procedure, I think
				// - see above procedure
				final double summedHeat = hullHeat + heatExch.getHeat();
				double reactorHeatFraction = (summedHeat == 0) ? 0 : (hullHeat / summedHeat);
				double exchangerHeatFraction = (summedHeat == 0) ? 0 : (heatExch.getHeat() / summedHeat);
				reactorHeatFraction = reactorHeatFraction * heatExch.getHULL_EXCHANGE_RATE();
				exchangerHeatFraction = exchangerHeatFraction * heatExch.getHULL_EXCHANGE_RATE();
				// - move heat
				hullHeat -= reactorHeatFraction;
				hullHeat += exchangerHeatFraction;
				heatExch.tryRemoveHeat(exchangerHeatFraction);
				heatExch.tryAddHeat(reactorHeatFraction);
				if(!heatExch.isAlive()) {
					removeComponent(heatExch, false);
				}
			}
		// 5)
			double totalSelfVent = 0;
			double totalCompHeatVented = 0;
			
			final double hullHeatIntakeTotal = Math.min(hullVentingCapacity, hullHeat);
			for(HeatVent vent : heatVents.getAll()) {
				// Hull vent rate
				if(vent.getHULL_VENT_RATE() > 0) {
					final double ventFraction = vent.getHULL_VENT_RATE_D() / hullVentingCapacity;
					final double heatIntake = ventFraction * hullHeatIntakeTotal;
					final double hullHeatTaken = vent.tryAddHeat((int) heatIntake);
					hullHeat -= hullHeatTaken;
					vent.setHullHeatDrawn(hullHeatTaken);
					if(!vent.isAlive()) {
						removeComponent(vent, false);
						continue;
					}				
				}
				// Component vent rate
				double componentHeatVented = 0;
				if(vent.getCOMPONENT_VENT_RATE() > 0) {
					final double ratePerSide = vent.getCOMPONENT_VENT_RATE() / heatVents.getNeighbours(vent).size();
					for(HeatExchanger c : heatExchangers.getNeighbours(vent)) {
						if(c != null) {
							componentHeatVented += c.tryRemoveHeat(ratePerSide);
						}
					}
					for(HeatVent c : heatVents.getNeighbours(vent)) {
						if(c != null) {
							componentHeatVented += c.tryRemoveHeat(ratePerSide);
						}
					}
					for(CoolantCell c : coolantCells.getNeighbours(vent)) {
						if(c != null) {
							componentHeatVented += c.tryRemoveHeat(ratePerSide);				}
					}				
				}
				vent.setComponentHeatVented(componentHeatVented);
				totalCompHeatVented += componentHeatVented;
				// Self vent rate
				final double selfVent = vent.tryRemoveHeat(vent.getSELF_VENT_RATE());
				vent.setSelfHeatVented(selfVent);
				totalSelfVent += selfVent;
				
			}
			
			currentOutputHeat = totalSelfVent + totalCompHeatVented;	
		}
	}
	
	/**
	 * Cache some values that never change unless components change
	 */
	private void calculateStats() {
		// reset
		hullVentingCapacity = 0;
		componentVentingCapacity = 0;
		// new
		for(HeatVent vent : heatVents.getAll()) {
			hullVentingCapacity += vent.getHULL_VENT_RATE();
			componentVentingCapacity += vent.getCOMPONENT_VENT_RATE();
		}
	}
	
}
