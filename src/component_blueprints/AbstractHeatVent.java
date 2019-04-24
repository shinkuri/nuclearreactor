package component_blueprints;

public class AbstractHeatVent extends ReactorComponent {
	
	private final int HEAT_CAPACITY;
	private final int COMPONENT_VENT_RATE;
	private final int HULL_VENT_RATE;
	private final int SELF_VENT_RATE;
	
	private int heat = 0;
	
	public AbstractHeatVent(int posX, int posY, 
			int heatCapacity, 
			int componentVentRate, int hullVentRate, int selfVentRate) {
		
		super(posX, posY);
		
		this.HEAT_CAPACITY = heatCapacity;
		this.COMPONENT_VENT_RATE = componentVentRate;
		this.HULL_VENT_RATE = hullVentRate;
		this.SELF_VENT_RATE = selfVentRate;

	}
	
	public AbstractHeatVent(int posX, int posY, 
			Integer[] data) {
		
		super(posX, posY);
		
		this.HEAT_CAPACITY = data[0];
		this.COMPONENT_VENT_RATE = data[1];
		this.HULL_VENT_RATE = data[2];
		this.SELF_VENT_RATE = data[3];

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
	public int getHeat() {
		return heat;
	}
	
	/**
	 * <b>MIGHT DESTROY COMPONENT!</b> <br>
	 * Attempts to increase the component's heat.
	 * @param delta : Positive change in heat (delta = natural number).
	 * @return Amount of heat added
	 */
	public int tryAddHeat(int delta) {
		if(delta <= 0) {
			return 0;
		}
		final int heatChange = Math.min((HEAT_CAPACITY - heat), delta);
		if(delta > heatChange) {
			super.setDestroyed();
		}
		heat += heatChange;
		return heatChange;
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
		final int heatChange = Math.min(heat, delta);
		heat -= heatChange;
		return heatChange;
	}
	
}
	
