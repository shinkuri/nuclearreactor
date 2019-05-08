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
	
	//TODO: Uranium and Thorium shouldn't use the same here
	private static final double HEAT_PER_PULSE = 4.0D;
	private static final double EU_PER_PULSE = 5.0D * 10.0D;
	
	private final double LIFETIME;
	private final ComponentSubType DEPLETED_TYPE;
	
	private final double HEAT_PER_SECOND;
	private final double HEAT_BOOST_RATE;
	private final double EU_PER_SECOND;
	private final double EU_BOOST_RATE;
	
	private final double NEUTRON_PULSES_EMITTED;
	
	private double remainingLifetime;
	private double pulsesReceived = 0;
	
	private double euProduced = 0;
	private double heatProduced = 0;
	
	public FuelRod(int posX, int posY, 
			double lifeTime, ComponentSubType depletedType, 
			double heatPerSecond, double heatBoostRate, double euPerSecond, double euBoostRate,
			double neutronPulsesEmitted) {
		
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
			Double[] data) {
		
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
	
	public double getLIFETIME() {
		return LIFETIME;
	}
	
	public double getRemainingLifetime() {
		return remainingLifetime;
	}
	
	public ComponentSubType getDepletedRod() {
		return DEPLETED_TYPE;
	}
	
	public void resetNeutronPulses() {
		pulsesReceived = 0;
	}
	
	public void addNeutronPulse(double pulses) {
		pulsesReceived += pulses;
	}
	
	public double getNEUTRON_PULSES_EMITTED() {
		return NEUTRON_PULSES_EMITTED;
	}
	
	public double getEUProduced() {
		return euProduced;
	}
	
	public double getHeatProduced() {
		return heatProduced;
	}
	
	public double getHeatPerSecond(double hullHeat, double hullHeatMax) {
		final double heatBoost = ((HEAT_BOOST_RATE - 1.0D) / Math.pow(hullHeatMax, 2.0D)) * Math.pow(hullHeat, 2.0D) + 1.0D;
		final double heat = heatBoost * (HEAT_PER_SECOND + (NEUTRON_PULSES_EMITTED * pulsesReceived * HEAT_PER_PULSE)); // NEUTRON_PULSES_EMITTED = Amount of rods. Using it like that.
		
		heatProduced = heat;
		return heat;
	}
	
	public double getElectricityPerSecond(double hullHeat, double hullHeatMax) {
		final double heatBoost = ((EU_BOOST_RATE - 1.0D) / Math.pow(hullHeatMax, 2.0D)) * Math.pow(hullHeat, 2.0D) + 1.0D;		
		final double eu = heatBoost * (EU_PER_SECOND + (NEUTRON_PULSES_EMITTED * pulsesReceived * EU_PER_PULSE));
		
		euProduced = eu / 20.0D * 10.0D;
		return eu;
	}
	
	public void use() {
		remainingLifetime--;
		if(remainingLifetime > 0) {
			if(super.applyEjectionFunction((int) remainingLifetime) == true) {
				super.setStatus(EJECTED);
				return;
			} else {
				super.setStatus(WORKING);
			}
		} else {
			super.setStatus(DESTROYED);
		}
	}
	
}
