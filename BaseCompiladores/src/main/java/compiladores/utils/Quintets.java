package compiladores.utils;

import java.util.LinkedList;

public class Quintets extends LinkedList<Quintet> {

	private Integer trueIndex = 0;

	@Override
	public boolean add(Quintet e) {
		if(trueIndex == this.size()) {
			return super.add(e);
		}
		try {
			super.add(trueIndex, e);
			trueIndex += 1;
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	} 


	
}
