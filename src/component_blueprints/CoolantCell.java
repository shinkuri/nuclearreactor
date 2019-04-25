package component_blueprints;

public class CoolantCell extends HeatManagementComponent {
	
	public CoolantCell(int posX, int posY,
			int heatCapacity) {
		
		super(posX, posY, heatCapacity);
		
	}
	
	public CoolantCell(int posX, int posY, 
			Integer[] data) {
		
		super(posX, posY, data[0]);
		
	}
	
}
