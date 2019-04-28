package component_blueprints;

public class NeutronReflector extends ReactorComponent {

	private final int DURABILITY;	
	
	private int durability;
	
	public NeutronReflector(int posX, int posY,
			int durability) {
		
		super(ComponentType.NeutronReflector, posX, posY);
		
		this.DURABILITY = durability;
		
		durability = DURABILITY;
	}
	
	public NeutronReflector(int posX, int posY, 
			Integer[] data) {
		
		super(ComponentType.NeutronReflector, posX, posY);
		
		this.DURABILITY = data[0];
		
		durability = DURABILITY;
	}
	
	public void applyReflectedPulses(FuelRod fuelRod) {
		fuelRod.addNeutronPulse(fuelRod.getNEUTRON_PULSES_EMITTED());
		durability -= fuelRod.getNEUTRON_PULSES_EMITTED();
		if(durability <= 0) {
			super.setDestroyed();
		}
	}

}
