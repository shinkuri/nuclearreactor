package component_blueprints;

public class AbstractHeatVent extends ReactorComponent {
	
	protected final int HEAT_CAPACITY;
	protected final int COMPONENT_VENT_RATE;
	protected final int HULL_VENT_RATE;
	protected final int SELF_VENT_RATE;
	
	protected int heat;
	
	public AbstractHeatVent(int posX, int posY, 
			int heatCapacity, 
			int componentVentRate, int hullVentRate, int selfVentRate) {
		
		super(posX, posY);
		
		this.HEAT_CAPACITY = heatCapacity;
		this.COMPONENT_VENT_RATE = componentVentRate;
		this.HULL_VENT_RATE = hullVentRate;
		this.SELF_VENT_RATE = selfVentRate;

	}
	
	@Override
	protected void destroy() {
		
	}
	
	public int getCOMPONENT_VENT_RATE() {
		return COMPONENT_VENT_RATE;
	}

	public int getHULL_VENT_RATE() {
		return HULL_VENT_RATE;
	}

	public int getSELF_VENT_RATE() {
		return SELF_VENT_RATE;
	}
	
	/**
	 * Attempts to increase the component's heat.
	 * @param delta : Positive change in heat (delta = natural number).
	 * @return Alive status
	 */
	public boolean tryAddHeat(int delta) {
		if(delta <= 0) {
			return true;
		}
		heat += delta;
		if(heat >= HEAT_CAPACITY) {
			destroy();
			return false;
		}
		return true;
	}
	
	/**
	 * Attempts to decrease the component's heat.
	 * @param delta : Negative change in heat (delta = natural number).
	 * @return Amount of heat removed.
	 */
	public int tryRemoveHeat(int delta) {
		if(delta <= 0) {
			return 0;
		}
		final int heatChange = Math.min((HEAT_CAPACITY - heat), delta);
		heat -= heatChange;
		return heatChange;
	}	
	
}
	
