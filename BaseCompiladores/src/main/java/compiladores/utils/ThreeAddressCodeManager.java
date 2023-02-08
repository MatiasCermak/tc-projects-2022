package compiladores.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import compiladores.utils.optimizer.IOptimizer;

public class ThreeAddressCodeManager {
	private LinkedList<String> tempVariables = new LinkedList<>();
	private LinkedList<String> labels = new LinkedList<>();
	private Map<String, String> labelMap = new HashMap<>();
	private Quintets tac = new Quintets();
	private static String OPTIMIZER_IMPL_PACKAGE = "compiladores.utils.optimizer.impl";
	public static String POP = "pop";
	public static String LBL = "lbl";
	public static String END = "end";
	public static String JMP = "jmp";
	public static String PSH = "psh";
	public static String JE = "je";

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
		if (StringUtils.isEmpty(function.getOp()))
			return;
		tac.add(function);

	}

	public String getFuntionLabel(String functionID) {
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

	public void optimizeTac(Integer numberOfIterations) {
		// Getting all classes that implement IOptimizer interface from the impl package
		Reflections reflections = new Reflections(OPTIMIZER_IMPL_PACKAGE, new SubTypesScanner());
		List<Class<? extends IOptimizer>> optimizerList = reflections.getSubTypesOf(IOptimizer.class).stream().collect(Collectors.toList());
		
		// Running the optimizations until  the specified number of iterations is reached.
		System.out.println("Optimization Start");
		for(Integer i = 0; i < numberOfIterations; i++) {
			optimizerList.forEach(optimizer -> {
				try {
					optimizer.getConstructor().newInstance().process(tac);
				} catch (Exception e) {
					System.out.println("Optimization failed, aborting.");
					return;
				}
			});
		}
		System.out.println("Optimization Finish");
	}

}