package compiladores.utils;

import compiladores.domain.Id;
import compiladores.domain.Variable;


import java.util.List;

import compiladores.domain.Function;
import compiladores.domain.Type;

public class main {
  public static void main(String[] args) {
    SymbolTable symbolTable = SymbolTable.getInstance();
    Id id = new Variable("id", Type.INT);
    symbolTable.addId(id);
    System.out.println(symbolTable.getId("id"));

    List<Type> types = List.of(Type.INT, Type.INT);

    Id id2 = new Function("id2", Type.INT, types);

    symbolTable.addId(id2);
    System.out.println(symbolTable.getId("id2"));
  }
}
