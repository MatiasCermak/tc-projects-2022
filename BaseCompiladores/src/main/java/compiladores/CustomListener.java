package compiladores;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ErrorNode;

import compiladores.compiladoresParser.AssignationContext;
import compiladores.compiladoresParser.BlockContext;
import compiladores.compiladoresParser.DeclarationContext;
import compiladores.compiladoresParser.ExtendedDeclarationContext;
import compiladores.compiladoresParser.ProgContext;
import compiladores.compiladoresParser.SimpleDeclarationContext;
import compiladores.domain.*;
import compiladores.utils.SymbolTable;

public class CustomListener extends compiladoresBaseListener {
  private SymbolTable symbolTable;
  private String vartype = null;
  private static List<Map<String, Id>> stack;

  public CustomListener() {
    this.symbolTable = SymbolTable.getInstance();
    this.stack = new LinkedList<Map<String, Id>>();
  }

  @Override
  public void exitProg(ProgContext ctx) {
  }

  @Override
  public void enterProg(ProgContext ctx) {
    System.out.println("enterProg");
  }

  @Override
  public void enterBlock(BlockContext ctx) {
    System.out.println("Entrada a bloque");
    this.symbolTable.addScope();
  }

  @Override
  public void exitBlock(BlockContext ctx) {
    try {
      System.out.println("Salida de bloque");
      this.symbolTable.print();
      stack.add(this.symbolTable.removeScope());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void enterDeclaration(DeclarationContext ctx) {
    System.out.println("Enter Declaration");
    System.out.println(ctx.start.getText());
    this.vartype = ctx.getStart().getText();
  }

  @Override
  public void exitDeclaration(DeclarationContext ctx) {
    System.out.println(ctx.getText());
    System.out.println("Exit Declaration");
  }

  @Override
  public void enterExtendedDeclaration(ExtendedDeclarationContext ctx) {
  }

  @Override
  public void enterSimpleDeclaration(SimpleDeclarationContext ctx) {
  }

  @Override
  public void exitExtendedDeclaration(ExtendedDeclarationContext ctx) {
    try {
      System.out.println("Hijo" + ctx.getChild(1).getText());
      System.out.println("Hijo" + ctx.getChild(1).getChildCount());
    } catch (Exception e) {

    }
  }

  @Override
  public void exitSimpleDeclaration(SimpleDeclarationContext ctx) {
    try {
      System.out.println("Hijo" + ctx.getChild(0).getText());
    } catch (Exception e) {

    }
  }

  @Override
  public void exitAssignation(AssignationContext ctx) {
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    System.out.println("Error: " + node.getText());
  }

  // Utility functions

}