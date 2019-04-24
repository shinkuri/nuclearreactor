package component_blueprints;

public class AbstractNeutronReflector extends ReactorComponent {

	private final int DURABILITY;	
	
	private int durability;
	
	public AbstractNeutronReflector(int posX, int posY,
			int durability) {
		
		super(posX, posY);
		
		this.DURABILITY = durability;
		
		durability = DURABILITY;
	}
	
	public AbstractNeutronReflector(int posX, int posY, 
			Integer[] data) {
		
		super(posX, posY);
		
		this.DURABILITY = data[0];
		
		durability = DURABILITY;
	}
	
	public void applyReflectedPulses(AbstractFuelRod fuelRod) {
		fuelRod.addNeutronPulse(fuelRod.getNEUTRON_PULSES_EMITTED());
		durability -= fuelRod.getNEUTRON_PULSES_EMITTED();
		if(durability <= 0) {
			super.setDestroyed();
		}
	}

}
