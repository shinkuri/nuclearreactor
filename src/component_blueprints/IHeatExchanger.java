package component_blueprints;

/**
 * Heat Exchangers move around heat between adjacent components or the reactor hull and chamber.
 * @author Kekzdealer
 *
 */
public interface IHeatExchanger {
	
	public int getComponentExchangeRate();
	public int getHullExchangeRate();
	
	/**
	 * Try to add heat. If heat added pushes the component over it's heat capacity limit, return -1.
	 * @param heat : Heat to be added
	 * @return -1 if component got destroyed, >0 otherwise
	 */
	public int tryAddHeat(int heat);
	/**
	 * Try to remove heat and return how much heat was removed.
	 * @param heat : Heat to be removed
	 * @return Amount of removed heat
	 */
	public int tryRemoveHeat(int heat);
}
