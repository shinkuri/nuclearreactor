package component_blueprints;

/**
 * Fuel rods provide energy. Each type lasts for a certain amount of time, generates some amount of heat and, 
 * if the reactor is in EU-mode, electricity.
 * <br>
 * Durability represents lifetime and one point is removed for each reactor tick
 * @author Kekzdealer
 *
 */
public interface IFuelRod {
	
	public int getHeatPerSecond();
	public int getElectricityPerSecond();
	public int getNeutronPulsesEmitted();
	public void addNeutronPulse(int p);
	public int getNeutronPulses();
}
