package component_blueprints;

import java.util.HashMap;

/**
 * A component that sits in a reactor.
 * 
 * Depending on placement in the 6x9 reactor grid components can have a differing amount of available sides.
 * All values are evenly distributed to all available sides (2 - 4) <br>
 * <br>
 * Durability for fuel rods is reduced by one each reactor tick. <br>
 * Durability for heat management components is reduced by one for every 60 heat. <br>
 * Durability for neutron reflectors is reduced by one/two/four for each single/dual/quad cell adjacent to it. <br>
 * 
 * @author Kekzdealer
 *
 */
public abstract class ReactorComponent {
	
	private final int posX;
	private final int posY;
	
	protected final int durabilityMax;
	protected int durability;
		
	// Available sides
	public enum Sides {
		top, right, bottom, left
	}
	private int availableSides;
	private final HashMap<Sides, Boolean> sides = new HashMap<>();
	
	protected ReactorComponent(int posX, int posY, int durabilityMax) {
		this.posX = posX;
		this.posY = posY;
		this.durabilityMax = durabilityMax;
		
		durability = durabilityMax;
		// Available sides:
		sides.put(Sides.top, (posY > 0) ? true : false);
		sides.put(Sides.right, (posX < 8) ? true : false);
		sides.put(Sides.bottom, (posY < 5) ? true : false);
		sides.put(Sides.left, (posX > 0) ? true : false);
		for(boolean b : sides.values()) {
			if(b) {
				availableSides++;
			}
		}
	}
	
	/**
	 * Damages the component and returns the updated durability value. Returns -1 if the component has been destroyed.
	 * @param damage Damage dealt
	 * @return Updated durability or -1 if broken
	 */
	public int doDamage(int damage) {
		durability -= damage;
		return (durability > 0) ? durability : -1;
	}
	
	public int getX() {
		return posX;
	}
	
	public int getY() {
		return posY;
	}
	
	public int getDurabilityMax() {
		return durabilityMax;
	}
	
	public int getDurability() {
		return durability;
	}
	
	public boolean getSide(ReactorComponent.Sides side) {
		return sides.get(side);
	}
	
	public int getSideCount() {
		return availableSides;
	}
}
