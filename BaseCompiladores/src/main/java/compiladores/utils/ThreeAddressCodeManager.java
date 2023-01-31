package compiladores.utils;

import java.util.LinkedList;

public class ThreeAddressCodeManager {
	private LinkedList<String> tempVariables;
	private LinkedList<String> labels;
    private Quintets tac = new Quintets();

	public LinkedList<String> getTempVariables() {
		return tempVariables;
	}

	public void setTempVariables(LinkedList<String> tempVariables) {
		this.tempVariables = tempVariables;
	}
	
	public LinkedList<String> getLabels() {
		return labels;
	}
	public void setLabels(LinkedList<String> labels) {
		this.labels = labels;
	}	

    public Quintets getTac() {
        return tac;
    }
}