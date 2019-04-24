package logic;

import javax.swing.JLabel;

/**
 * This class is used by the reactor planner to get updated information about the reactor
 * @author Kekzdealer
 *
 */
public class StatusReport {
	
	private volatile JLabel labelHullHeat;
	private volatile JLabel labelEUOutput;
	private volatile JLabel labelHUOutput;
	
	private int hullHeat;
	private int currentEUt;
	private int currentHUs;
	
	public StatusReport(JLabel l1, JLabel l2, JLabel l3) {
		this.labelHullHeat = l1;
		this.labelEUOutput = l2;
		this.labelHUOutput = l3;
	}
	
	public synchronized int getHullHeat() {
		return hullHeat;
	}
	public synchronized void setHullHeat(int hullHeat) {
		this.hullHeat = hullHeat;
		labelHullHeat.setText("Hull Heat: " + hullHeat);
	}
	public synchronized int getCurrentEUt() {
		return currentEUt;
	}
	public synchronized void setCurrentEUt(int currentEUt) {
		this.currentEUt = currentEUt;
		labelEUOutput.setText("EU/t: " + currentEUt);
	}
	public synchronized int getCurrentHUs() {
		return currentHUs;
	}
	public synchronized void setCurrentHUs(int currentHUs) {
		this.currentHUs = currentHUs;
		labelHUOutput.setText("Hu/s " + currentHUs);
	}
}
