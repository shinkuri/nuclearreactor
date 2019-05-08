package component_blueprints;

import java.util.function.Function;

/**
 * A component that sits in a reactor.
 * 
 * Depending on placement in the 6x9 reactor grid components can have a differing amount of available sides.
 * All values are evenly distributed to all available sides (2 - 4) <br>
 * <br> 
 * @author Kekzdealer
 *
 */
public abstract class ReactorComponent {
	
	public static final int DESTROYED = 1;
	public static final int EJECTED = 2;
	public static final int WORKING = 3;
	
	public static enum ComponentType {
		FuelRod,
		DepletedFuelRod,
		NeutronReflector,
		CoolantCell,
		HeatExchanger,
		HeatVent,
		EmergencyFoam
	}
	
	private final ComponentType type;
	 
	private final int posX;
	private final int posY;

	private Function<Integer, Boolean> ejectionFunction;
	protected boolean alive = true;
	private int status = WORKING;
	
	protected ReactorComponent(ComponentType type, int posX, int posY) {
		this.type = type;
		this.posX = posX;
		this.posY = posY;
	}
	
	public void setEjectionFunction(Function<Integer, Boolean> f) {
		ejectionFunction = f;
	}
	
	protected boolean applyEjectionFunction(int a) {
		return ejectionFunction.apply(a);
	}
	
	protected void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * Call after processing this component! <br>
	 * DESTROYED -> 1 <br>
	 * EJECTED -> 2 <br>
	 * WOKRING -> 3 <br>
	 * @return Component status
	 */
	public int getStatus() {
		return status;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof ReactorComponent) {
			final ReactorComponent rc = (ReactorComponent) o;
			return ((this.posX == rc.getX()) && (this.posY == rc.getY())) ? true : false;
		} else {
			return false;
		}
	}
	
	public ComponentType getType() {
		return type;
	}
	
	public int getX() {
		return posX;
	}
	
	public int getY() {
		return posY;
	}

}
