package component_blueprints;


public class HeatVent extends HeatManagementComponent {
	
	private final double COMPONENT_VENT_RATE;
	private final double HULL_VENT_RATE;
	private final double SELF_VENT_RATE;
	
	private double componentHeatVented = 0;
	private double hullHeatDrawn = 0;
	private double selfHeatVented = 0;
	
	public HeatVent(int posX, int posY, 
			double heatCapacity, 
			double componentVentRate, double hullVentRate, double selfVentRate) {
		
		super(ComponentType.HeatVent, posX, posY, heatCapacity);
		
		this.COMPONENT_VENT_RATE = componentVentRate;
		this.HULL_VENT_RATE = hullVentRate;
		this.SELF_VENT_RATE = selfVentRate;

	}
	
	public HeatVent(int posX, int posY, 
			Double[] data) {
		
		super(ComponentType.HeatVent, posX, posY, data[0]);
		
		this.COMPONENT_VENT_RATE = data[1];
		this.HULL_VENT_RATE = data[2];
		this.SELF_VENT_RATE = data[3];

	}
	
	public double getCOMPONENT_VENT_RATE() {
		return COMPONENT_VENT_RATE;
	}

	public double getHULL_VENT_RATE() {
		return HULL_VENT_RATE;
	}
	
	public double getHULL_VENT_RATE_D() {
		return HULL_VENT_RATE;
	}

	public double getSELF_VENT_RATE() {
		return SELF_VENT_RATE;
	}
	
	public double getComponentHeatVented() {
		return componentHeatVented;
	}

	public double getHullHeatDrawn() {
		return hullHeatDrawn;
	}

	public double getSelfHeatVented() {
		return selfHeatVented;
	}

	public void setComponentHeatVented(double heat) {
		this.componentHeatVented = heat;
	}
	
	public void setHullHeatDrawn(double heat) {
		this.hullHeatDrawn = heat;
	}
	
	public void setSelfHeatVented(double heat) {
		this.selfHeatVented = heat;
	}
	
}
	
