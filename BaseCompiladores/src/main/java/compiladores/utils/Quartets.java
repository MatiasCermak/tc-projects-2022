package compiladores.utils;

import java.util.LinkedList;
import java.util.List;

import org.javatuples.Quartet;

public class Quartets extends LinkedList<Quartet<String,String,String,String>> {
	private List<String> tempVariables;
	private List<String> labels;

	public List<String> getTempVariables() {
		return tempVariables;
	}

	public void setTempVariables(List<String> tempVariables) {
		this.tempVariables = tempVariables;
	}
	
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}	
}
