package compiladores.utils;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Quintets extends LinkedList<Quintet> {

	private Integer trueIndex = 0;

	
	public Quintet getLastIncompleteQuintet() {
		List<Quintet> quintets =  this.stream().filter(quintet -> StringUtils.isAnyEmpty(quintet.getArg1(), quintet.getArg2(), quintet.getOp(), quintet.getRes())).toList();
		if(quintets.isEmpty()) {
			return this.getLast();
		} else {
			return quintets.get(0);
		}
	} 
}
