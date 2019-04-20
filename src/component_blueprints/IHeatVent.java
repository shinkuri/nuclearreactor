package component_blueprints;

/**
 * Heat Vents draw zero to N heat from themselves, the reactor hull, or adjacent components and remove it.
 * @author Kekzdealer
 *
 */
public interface IHeatVent {
	
	public int getSelfVentRate();
	public int getReactorVentRate();
	public int getComponentVentRate();
	
	public int addHeat(int heat);
	public void removeHeat(int heat);
}
