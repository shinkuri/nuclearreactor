package component_blueprints;

import java.util.HashMap;
import java.util.Map.Entry;

public class HeatExchanger extends HeatManagementComponent {
	
	private final int COMPONENT_EXCHANGE_RATE;
	private final int HULL_EXCHANGE_RATE;
		
	public HeatExchanger(int posX, int posY, 
			int heatCapacity, 
			int componentExchangeRate, int hullExchangeRate) {
		
		super(posX, posY, heatCapacity);
		
		this.COMPONENT_EXCHANGE_RATE = componentExchangeRate;
		this.HULL_EXCHANGE_RATE = hullExchangeRate;
	}
	
	public HeatExchanger(int posX, int posY, 
			Integer[] data) {
		
		super(posX, posY, data[0]);
		
		this.COMPONENT_EXCHANGE_RATE = data[1];
		this.HULL_EXCHANGE_RATE = data[2];
	}
	
	public int getCOMPONENT_EXCHANGE_RATEe() {
		return COMPONENT_EXCHANGE_RATE;
	}
	
	public int getHULL_EXCHANGE_RATE() {
		return HULL_EXCHANGE_RATE;
	}
	
	/**
	 * Calculates how much heat can be taken from each participant while not exceeding 
	 * the maximum throughput of this heat exchanger.
	 * Assigned throughput is directly relative to how large the fraction of the total heat
	 * between all participants is.
	 * @param participants : The heat exchanger itself and up to four surrounding components
	 * @return A map with each participant and the throughput it got assigned
	 */
	public HashMap<ReactorComponent, Double> calculateFractionsOfThrougput(HashMap<ReactorComponent, Double> participants) {
		double summedHeat = 0;
		for(Double i : participants.values()) {
			summedHeat += i;
		}
		for(Entry<ReactorComponent, Double> i : participants.entrySet()) {
			final double fractionOfTotalHeat = i.getValue() / summedHeat;
			final double fractionOfThroughput = fractionOfTotalHeat * COMPONENT_EXCHANGE_RATE;
			participants.put(i.getKey(), fractionOfThroughput);
		}		
		return participants;
	}
	
}
