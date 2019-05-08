package component_blueprints;

public class HeatManagementComponent extends ReactorComponent {

	private final double HEAT_CAPACITY;
	
	private double heat = 0;
	
	protected HeatManagementComponent(ComponentType type, int posX, int posY, double heatCapacity) {
		
		super(type, posX, posY);
		
		this.HEAT_CAPACITY = heatCapacity;
	}
	
	public double getHEAT_CAPACITY() {
		return HEAT_CAPACITY;
	}
	
	public double getHeat() {
		return heat;
	}
	
	/**
	 * <b>MIGHT DESTROY COMPONENT!</b> <br>
	 * Attempts to increase the component's heat.
	 * @param delta : Positive change in heat (delta = natural number).
	 * @return Amount of heat added
	 */
	public double tryAddHeat(double delta) {
		if(delta <= 0) {
			return 0;
		}
		final double heatChange = Math.min((HEAT_CAPACITY - heat), delta);
		heat += heatChange;
		if(heat < HEAT_CAPACITY) {
			if(super.applyEjectionFunction((int) heat) == true) {
				super.setStatus(EJECTED);
			} else {
				super.setStatus(WORKING);
			}
		} else {
			super.setStatus(DESTROYED);
		}
		return heatChange;
	}
	
	/**
	 * Attempts to decrease the component's heat.
	 * @param delta : Negative change in heat (delta = natural number).
	 * @return Amount of heat removed.
	 */
	public double tryRemoveHeat(double delta) {
		if(delta <= 0) {
			return 0;
		}
		final double heatChange = Math.min(heat, delta);
		heat -= heatChange;
		if(super.applyEjectionFunction((int) heat) == true) {
			super.setStatus(EJECTED);
		} else {
			super.setStatus(WORKING);
		}
		return heatChange;
	}

}
