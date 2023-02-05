package compiladores.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ThreeAddressCodeManager {
	private LinkedList<String> tempVariables = new LinkedList<>();
	private LinkedList<String> labels = new LinkedList<>();
	private Map<String, String> labelMap = new HashMap<>();
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

	public void addFunction(Quintet function) {
		labelMap.put(function.getRes(), function.getLabel());
		tac.add(function);

	}

	public String getFuntionLabel(String functionID) {
		System.out.println("getFuntionLabel");
		return labelMap.get(functionID);
	}

	public String createNewLabel() {
		if (this.labels.isEmpty()) {
			this.labels.add("L1");
			return this.labels.getLast();
		}

		String lastLabel = this.labels.getLast();
		Integer lastLabelNumber = Integer.parseInt(lastLabel.replace("L", ""));
		this.labels.add("L" + (lastLabelNumber + 1));
		return this.labels.getLast();
	}

	public String createNewTempVariable() {
		if (this.tempVariables.isEmpty()) {
			this.tempVariables.add("T1");
			return this.tempVariables.getLast();
		}

		String lastTempVariable = this.tempVariables.getLast();
		Integer lastTVNumber = Integer.parseInt(lastTempVariable.replace("T", ""));
		this.tempVariables.add("T" + (lastTVNumber + 1));
		return this.tempVariables.getLast();
	}

}