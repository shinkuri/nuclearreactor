package component_blueprints;

public class AbstractHeatExchanger extends ReactorComponent {
	
	protected final int HEAT_CAPACITY;
	protected final int COMPONENT_EXCHANGE_RATE;
	protected final int HULL_EXCHANGE_RATE;
	
	protected int heat;
	
	public AbstractHeatExchanger(int posX, int posY, 
			int heatCapacity, 
			int componentExchangeRate, int hullExchangeRate) {
		
		super(posX, posY);
		
		this.HEAT_CAPACITY = heatCapacity;
		this.COMPONENT_EXCHANGE_RATE = componentExchangeRate;
		this.HULL_EXCHANGE_RATE = hullExchangeRate;
	}
	
	@Override
	protected void destroy() {
		
	}
	
	public int getCOMPONENT_EXCHANGE_RATEe() {
		return COMPONENT_EXCHANGE_RATE;
	}
	
	public int getHULL_EXCHANGE_RATE() {
		return HULL_EXCHANGE_RATE;
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
