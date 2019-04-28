package component_blueprints;

public class HeatManagementComponent extends ReactorComponent {

	private final int HEAT_CAPACITY;
	
	private int heat = 0;
	
	protected HeatManagementComponent(ComponentType type, int posX, int posY, int heatCapacity) {
		
		super(type, posX, posY);
		
		this.HEAT_CAPACITY = heatCapacity;
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
