package component_blueprints;

public class NeutronReflector extends ReactorComponent {

	private final double DURABILITY;	
	
	private double durability;
	
	public NeutronReflector(int posX, int posY,
			double durability) {
		
		super(ComponentType.NeutronReflector, posX, posY);
		
		this.DURABILITY = durability;
		
		durability = DURABILITY;
	}
	
	public NeutronReflector(int posX, int posY, 
			Double[] data) {
		
		super(ComponentType.NeutronReflector, posX, posY);
		
		this.DURABILITY = data[0];
		
		durability = DURABILITY;
	}
	
	public double getDURABILITY() {
		return DURABILITY;
	}
	
	public double getDurability() {
		return durability;
	}
	
	public void applyReflectedPulses(FuelRod fuelRod) {
		fuelRod.addNeutronPulse(fuelRod.getNEUTRON_PULSES_EMITTED());
		durability -= fuelRod.getNEUTRON_PULSES_EMITTED();
		if(durability > 0) {
			if(super.applyEjectionFunction((int) durability) == true) {
				super.setStatus(EJECTED);
			} else {
				super.setStatus(WORKING);
			}
		} else {
			super.setStatus(DESTROYED);
		}
	}

}
