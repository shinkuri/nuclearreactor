package component_blueprints;

public class EmergencyFoam extends ReactorComponent {

	private final double HULL_HEAT_THRESHOLD;
	
	public EmergencyFoam(int posX, int posY, double hullHeatThreshold) {
		super(ComponentType.EmergencyFoam, posX, posY);
	
		this.HULL_HEAT_THRESHOLD = hullHeatThreshold;
	}
	
	public EmergencyFoam(int posX, int posY, Double[] data) {
		super(ComponentType.EmergencyFoam, posX, posY);
		
		this.HULL_HEAT_THRESHOLD = data[0];
	}
	
	public double getHULL_HEAT_THRESHOLD() {
		return HULL_HEAT_THRESHOLD;
	}

}
