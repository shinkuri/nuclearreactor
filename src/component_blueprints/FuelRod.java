package component_blueprints;

import logic.ComponentFactory.ComponentSubType;

/**
 * Fuel rods and MOX fuel rods.
 * Heat and EU generation scales linearily with additional neutron pulses, 
 * but exponentially with reactor hull heat. <br>
 * Formula for pulse scaling: <br>	<b>per pulse bonus * pulses received</b><br>
 * Formula for hull heat scaling: <br>	<b>(( heat boost multiplier - 1 ) / (maximum hull heat)² ) * (hull heat)² + 1</b>
 * @author Kekzdealer
 *
 */
public class FuelRod extends ReactorComponent {
	
	private static final int HEAT_PER_PULSE = 4;
	private static final int EU_PER_PULSE = 5;
	
	private final int LIFETIME;
	private final ComponentSubType DEPLETED_TYPE;
	
	private final int HEAT_PER_SECOND;
	private final double HEAT_BOOST_RATE;
	private final int EU_PER_SECOND;
	private final double EU_BOOST_RATE;
	
	private final int NEUTRON_PULSES_EMITTED;
	
	private int remainingLifetime;
	private int pulsesReceived = 0;
	
	private int euProduced = 0;
	private int heatProduced = 0;
	
	public FuelRod(int posX, int posY, 
			int lifeTime, ComponentSubType depletedType, 
			int heatPerSecond, int heatBoostRate, int euPerSecond, int euBoostRate,
			int neutronPulsesEmitted) {
		
		super(ComponentType.FuelRod, posX, posY);
		
		this.LIFETIME = lifeTime;
		this.DEPLETED_TYPE = depletedType;
		this.HEAT_PER_SECOND = heatPerSecond;
		this.HEAT_BOOST_RATE = heatBoostRate;
		this.EU_PER_SECOND = euPerSecond;
		this.EU_BOOST_RATE = euBoostRate;
		this.NEUTRON_PULSES_EMITTED = neutronPulsesEmitted;
		
		remainingLifetime = LIFETIME;
	}
	
	public FuelRod(int posX, int posY, 
			ComponentSubType depletedType, 
			Integer[] data) {
		
		super(ComponentType.FuelRod, posX, posY);
		
		this.LIFETIME = data[0];
		this.DEPLETED_TYPE = depletedType;
		this.HEAT_PER_SECOND = data[1];
		this.HEAT_BOOST_RATE = data[2];
		this.EU_PER_SECOND = data[3];
		this.EU_BOOST_RATE = data[4];
		this.NEUTRON_PULSES_EMITTED = data[5];
		
		remainingLifetime = LIFETIME;
	}
	
	public int getLIFETIME() {
		return LIFETIME;
	}
	
	public int getRemainingLifetime() {
		return remainingLifetime;
	}
	
	public ComponentSubType getDepletedRod() {
		return DEPLETED_TYPE;
	}
	
	public void resetNeutronPulses() {
		pulsesReceived = 0;
	}
	
	public void addNeutronPulse(int pulses) {
		pulsesReceived += pulses;
	}
	
	public int getNEUTRON_PULSES_EMITTED() {
		return NEUTRON_PULSES_EMITTED;
	}
	
	public int getEUProduced() {
		return euProduced;
	}
	
	public int getHeatProduced() {
		return heatProduced;
	}
	
	public int getHeatPerSecond(int hullHeat, int hullHeatMax) {
		final double heatBoost = ((HEAT_BOOST_RATE - 1.0D) / Math.pow(hullHeatMax, 2.0D)) * Math.pow(hullHeat, 2.0D) + 1.0D;
		final int heat = (int) Math.round((heatBoost * (HEAT_PER_SECOND + (pulsesReceived * HEAT_PER_PULSE))));
		
		heatProduced = heat;
		return heat;
	}
	
	public int getElectricityPerSecond(int hullHeat, int hullHeatMax) {
		final double heatBoost = ((EU_BOOST_RATE - 1.0D) / Math.pow(hullHeatMax, 2.0D)) * Math.pow(hullHeat, 2.0D) + 1.0D;		
		final int eu = (int) Math.round(heatBoost * (EU_PER_SECOND + (pulsesReceived * EU_PER_PULSE)));
		
		euProduced = eu / 20 * 10;
		return eu;
	}
	
	public void use() {
		remainingLifetime -= 1;
		if(remainingLifetime <= 0) {
			super.setDestroyed();
		}
	}
}
