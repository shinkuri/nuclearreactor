package component_blueprints;


public class HeatVent extends HeatManagementComponent {
	
	private final int COMPONENT_VENT_RATE;
	private final int HULL_VENT_RATE;
	private final int SELF_VENT_RATE;
	
	private int componentHeatVented = 0;
	private int hullHeatDrawn = 0;
	private int selfHeatVented = 0;
	
	public HeatVent(int posX, int posY, 
			int heatCapacity, 
			int componentVentRate, int hullVentRate, int selfVentRate) {
		
		super(ComponentType.HeatVent, posX, posY, heatCapacity);
		
		this.COMPONENT_VENT_RATE = componentVentRate;
		this.HULL_VENT_RATE = hullVentRate;
		this.SELF_VENT_RATE = selfVentRate;

	}
	
	public HeatVent(int posX, int posY, 
			Integer[] data) {
		
		super(ComponentType.HeatVent, posX, posY, data[0]);
		
		this.COMPONENT_VENT_RATE = data[1];
		this.HULL_VENT_RATE = data[2];
		this.SELF_VENT_RATE = data[3];

	}
	
	public int getCOMPONENT_VENT_RATE() {
		return COMPONENT_VENT_RATE;
	}

	public int getHULL_VENT_RATE() {
		return HULL_VENT_RATE;
	}

	public int getSELF_VENT_RATE() {
		return SELF_VENT_RATE;
	}
	
	public int getComponentHeatVented() {
		return componentHeatVented;
	}

	public int getHullHeatDrawn() {
		return hullHeatDrawn;
	}

	public int getSelfHeatVented() {
		return selfHeatVented;
	}

	public void setComponentHeatVented(int heat) {
		this.componentHeatVented = heat;
	}
	
	public void setHullHeatDrawn(int heat) {
		this.hullHeatDrawn = heat;
	}
	
	public void setSelfHeatVented(int heat) {
		this.selfHeatVented = heat;
	}
	
}
	
