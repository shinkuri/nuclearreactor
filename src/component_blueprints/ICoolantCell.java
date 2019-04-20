package component_blueprints;

/**
 * Coolant Cells simply absorb heat until they break or are replaced.
 * @author Kekzdealer
 *
 */
public interface ICoolantCell {

	public int addHeat(int heat);
	public void removeHeat(int heat);
}
