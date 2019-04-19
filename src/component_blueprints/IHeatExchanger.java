package component_blueprints;

/**
 * Heat Exchangers move around heat between adjacent components or the reactor hull and chamber.
 * @author Kekzdealer
 *
 */
public interface IHeatExchanger {
	
	public int getComponentExchangeRate();
	public int getHullExchangeRate();
}
