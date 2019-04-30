package component_blueprints;

public class CoolantCell extends HeatManagementComponent {
	
	public CoolantCell(int posX, int posY,
			double heatCapacity) {
		
		super(ComponentType.CoolantCell, posX, posY, heatCapacity);
		
	}
	
	public CoolantCell(int posX, int posY, 
			Double[] data) {
		
		super(ComponentType.CoolantCell, posX, posY, data[0]);
		
	}
	
}
