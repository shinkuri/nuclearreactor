package component_blueprints;

public class CoolantCell extends HeatManagementComponent {
	
	public CoolantCell(int posX, int posY,
			int heatCapacity) {
		
		super(ComponentType.CoolantCell, posX, posY, heatCapacity);
		
	}
	
	public CoolantCell(int posX, int posY, 
			Integer[] data) {
		
		super(ComponentType.CoolantCell, posX, posY, data[0]);
		
	}
	
}
