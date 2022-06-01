package compiladores;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import compiladores.compiladoresParser.AssignationContext;
import compiladores.compiladoresParser.DeclarationContext;
import compiladores.compiladoresParser.ProgContext;
import compiladores.domain.*;
import compiladores.utils.SymbolTable;

public class myListener extends compiladoresBaseListener {
  private SymbolTable symbolTable;

  public myListener() {
    this.symbolTable = SymbolTable.getInstance();
  }



  @Override
  public void exitProg(ProgContext ctx) {
    symbolTable.print();
  }

  @Override
  public void enterProg(ProgContext ctx) {
    System.out.println("enterProg");
  }

  @Override
  public void exitDeclaration(DeclarationContext ctx) {
    
    String id = ctx.getStop().getText();
    Type type = Type.valueOf(ctx.getStart().getText().toUpperCase());

    if (symbolTable.containsId(id)) {
      System.out.println("Error: variable " + id + " already declared");
    } else {
      Id id2 = new Variable(id, type);
      symbolTable.addId(id2);
    }

  }

 

  @Override
  public void exitAssignation(AssignationContext ctx) {
    
      String id = ctx.getChild(0).getText();
      if(!symbolTable.containsId(id)){
        System.out.println("Error: variable " + id + " not declared");
      }
    
  }



  @Override
  public void visitErrorNode(ErrorNode node) {
    System.out.println("Error: " + node.getText());
  }

  
}