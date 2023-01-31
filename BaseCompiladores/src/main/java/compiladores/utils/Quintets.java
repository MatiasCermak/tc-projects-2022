package compiladores.utils;

import java.util.LinkedList;

import org.javatuples.Quintet;

public class Quintets extends LinkedList<Quintet<String,String,String,String,String>> {

	public Quintet<String, String, String, String, String> createEmptyQuintet() {
		return new Quintet<String,String,String,String,String>(null, null, null, null, null);
	}
}
