package logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import component_blueprints.CoolantCell;
import component_blueprints.DepletedFuelRod;
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
	
	private static final int EXPLOSION_THRESHOLD = 10000;
		
	private boolean isActive = false;
	private boolean isEUMode = true;
	private int hullHeat = 0;
	
	private int hullVentingCapacity = 0;
	private int componentVentingCapacity = 0;
	
	private int currentOutputEU = 0;
	private int currentOutputHeat = 0;
	
	private static final int SIZE_X = 9;
	private static final int SIZE_Y = 6;
	private final Grid<HeatVent> heatVents = new Grid<>(HeatVent.class, SIZE_X, SIZE_Y);
	private final Grid<HeatExchanger> heatExchangers = new Grid<>(HeatExchanger.class, SIZE_X, SIZE_Y);
	private final Grid<FuelRod> fuelRods = new Grid<>(FuelRod.class, SIZE_X, SIZE_Y);
	private final Grid<DepletedFuelRod> depletedRods = new Grid<>(DepletedFuelRod.class, SIZE_X, SIZE_Y);
	private final Grid<NeutronReflector> neutronReflectors = new Grid<>(NeutronReflector.class, SIZE_X, SIZE_Y);
	private final Grid<CoolantCell> coolantCells = new Grid<>(CoolantCell.class, SIZE_X, SIZE_Y);
	private final Object componentLock = new Object();
	
	@Override
	public void run() {
		while(isActive) {
			long delta = System.currentTimeMillis();
			tick();
			delta = delta - System.currentTimeMillis();
			try {
				Thread.sleep(1000 - delta);
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
			}
			
			// Update cache
			calculateStats();			
		}
	}
	
	// =================== GETTERS ========================
	public boolean isRunning() {
		return isActive;
	}
	
	public int getHullHeat() {
		return hullHeat;
	}
	
	public int getEUOutput() {
		return currentOutputEU;
	}
	
	public int getHUOutput() {
		return currentOutputHeat;
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
		}
	}
	
	/**
	 * If a component with heat capacity is destroyed, all the heat it holds is released into the hull
	 * @param rc : Component to be destroyed
	 */
	public void removeComponent(ReactorComponent rc) {
		synchronized(componentLock) {
			System.out.println("Removed " +rc.getType() +"@" +rc.getX() +"/" +rc.getY());
			
			if(rc instanceof HeatManagementComponent) {
				HeatManagementComponent hmc = (HeatManagementComponent) rc;
				hullHeat += hmc.getHeat();
			} else if (rc instanceof FuelRod) {
				FuelRod fr = (FuelRod) rc;
				depletedRods.put(componentFactory.generateComponent(fr.getDepletedRod(), fr.getX(), fr.getY()));
			}
			
			switch(rc.getType()) {
			case HeatVent: heatVents.remove(rc); break;
			case HeatExchanger: heatExchangers.remove(rc); break;
			case FuelRod: fuelRods.remove(rc); break;
			case DepletedFuelRod: depletedRods.remove(rc); break;
			case NeutronReflector: neutronReflectors.remove(rc); break;
			case CoolantCell: coolantCells.remove(rc); break; 
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
		
		synchronized(componentLock) {
		// 1)
			for(NeutronReflector refl : neutronReflectors.getAll()) {
				for(FuelRod c : fuelRods.getNeighbours(refl)) {
					if(c != null) {
						refl.applyReflectedPulses(c);
						if(!refl.isAlive()) {
							removeComponent(refl);
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
		// 2)
			for(FuelRod rod : fuelRods.getAll()) {
				currentOutputEU += rod.getElectricityPerSecond(hullHeat, EXPLOSION_THRESHOLD);
				
				int heat = rod.getHeatPerSecond(hullHeat, EXPLOSION_THRESHOLD);
				int neighbourCount = 0;
				neighbourCount += heatVents.getNeighbours(rod).size();
				neighbourCount += heatExchangers.getNeighbours(rod).size();
				neighbourCount += coolantCells.getNeighbours(rod).size();
				final int heatPerSide = heat / neighbourCount;
				
				final HashSet<HeatManagementComponent> heatComponents = new HashSet<>();
				heatComponents.addAll(heatExchangers.getNeighbours(rod));
				heatComponents.addAll(heatVents.getNeighbours(rod));
				heatComponents.addAll(coolantCells.getNeighbours(rod));
				for(HeatManagementComponent c : heatComponents) {
					if(c != null) {
						heat -= c.tryAddHeat(heatPerSide);
						if(!c.isAlive()) {
							removeComponent(c);
						}
					}
				}
				
				hullHeat += heat;
				System.out.println("Added hullHeat: " +heat);
				rod.use();
				if(!rod.isAlive()) {
					removeComponent(rod);
				}
			}			
		// 3)
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
					int removedHeat = 0;
					removedHeat = cTake.getKey().tryRemoveHeat(cTake.getValue().intValue());
					
					// Try to distribute all the heat that was taken
					for(Entry<HeatManagementComponent, Double> cDist : targets.entrySet()) {
						if(!cDist.getKey().equals(cTake.getKey())) {
							
							removedHeat -= cDist.getKey().tryAddHeat(removedHeat / (targets.size() - 1));
						
							if(!cDist.getKey().isAlive()) {
								removeComponent(cDist.getKey());
							}
						}
					}
					// Add heat that couldn't be distributed back to the source component
					if(removedHeat > 0) {
						
						cTake.getKey().tryAddHeat(removedHeat);
						
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
			int hullHeatVented = 0;
			int componentHeatVented = 0;
			
			final int hullHeatIntakeTotal = Math.min(hullVentingCapacity, hullHeat);
			for(HeatVent vent : heatVents.getAll()) {
				// Hull vent rate
				if(vent.getHULL_VENT_RATE() > 0) {
					final int heatIntake = (vent.getHULL_VENT_RATE() / hullVentingCapacity) * hullHeatIntakeTotal;
					System.out.println("Vent took in hull heat: " +heatIntake);
					vent.tryAddHeat(heatIntake);
					if(!vent.isAlive()) {
						removeComponent(vent);
						continue;
					}				
				}
				// Component vent rate
				if(vent.getCOMPONENT_VENT_RATE() > 0) {
					final int ratePerSide = vent.getCOMPONENT_VENT_RATE() / heatVents.getMaxNeighbours(vent);
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
				// Self vent rate
				hullHeatVented += vent.tryRemoveHeat(vent.getSELF_VENT_RATE());
			}
			
			currentOutputHeat = hullHeatVented + componentHeatVented;	
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
