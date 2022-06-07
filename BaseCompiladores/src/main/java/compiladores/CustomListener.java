package compiladores;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ErrorNode;

import com.ibm.icu.impl.Pair;

import compiladores.compiladoresParser.AssignationContext;
import compiladores.compiladoresParser.BlockContext;
import compiladores.compiladoresParser.DeclarationContext;
import compiladores.compiladoresParser.DeclaredVariableContext;
import compiladores.compiladoresParser.ExtendedDeclarationContext;
import compiladores.compiladoresParser.FunctionCallContext;
import compiladores.compiladoresParser.FunctionDeclarationContext;
import compiladores.compiladoresParser.FunctionForwardDeclarationContext;
import compiladores.compiladoresParser.FunctionIdContext;
import compiladores.compiladoresParser.ParametersDeclarationContext;
import compiladores.compiladoresParser.ProgContext;
import compiladores.compiladoresParser.SimpleDeclarationContext;
import compiladores.domain.*;
import compiladores.utils.SymbolTable;

public class CustomListener extends compiladoresBaseListener {
  private SymbolTable symbolTable;
  private String vartype = null;
  private static List<Map<String, Id>> stack;
  private static List<List<String>> parameters;
  private String functionName = null;

  public CustomListener() {
    this.symbolTable = SymbolTable.getInstance();
    this.stack = new LinkedList<Map<String, Id>>();
    this.parameters = new LinkedList<List<String>>();
  }

  @Override
  public void exitProg(ProgContext ctx) {
    stack.add(this.symbolTable.removeScope());
  }

  @Override
  public void enterProg(ProgContext ctx) {
    this.symbolTable.addScope();
  }

  @Override
  public void enterBlock(BlockContext ctx) {
    System.out.println("Entrada a bloque");
    if(functionName != null) {
      Function function = this.symbolTable.getId(functionName).getFunction();
      for(int i = 0; i < parameters.size(); i++) {
        this.symbolTable.
      }
    }


    this.symbolTable.addScope();


  }

  @Override
  public void exitBlock(BlockContext ctx) {
    stack.add(this.symbolTable.removeScope());
  }

  @Override
  public void enterDeclaration(DeclarationContext ctx) {
    System.out.println(ctx.start.getText());
    this.vartype = ctx.getStart().getText();
  }

  @Override
  public void exitDeclaration(DeclarationContext ctx) {
    this.vartype = null;
    System.out.println(ctx.getText());
    System.out.println("Exit Declaration");
  }

  @Override
  public void enterExtendedDeclaration(ExtendedDeclarationContext ctx) {
  }

  @Override
  public void exitExtendedDeclaration(ExtendedDeclarationContext ctx) {
  }

  @Override
  public void enterSimpleDeclaration(SimpleDeclarationContext ctx) {

  }

  @Override
  public void exitSimpleDeclaration(SimpleDeclarationContext ctx) {
    System.out.println(ctx.getChildCount());
  }

  @Override
  public void exitDeclaredVariable(DeclaredVariableContext ctx) {
    if (symbolTable.containsLocalId(ctx.getText())) {
      System.out.println("Variable " + ctx.getText() + " already declared");
    } else {
      Id id = new Variable(ctx.getText(), Type.valueOf(this.vartype.toUpperCase()));
      this.symbolTable.addId(id);
    }
  }

  @Override
  public void exitAssignation(AssignationContext ctx) {
    if (vartype != null) {
      if (symbolTable.containsLocalId(ctx.getChild(0).getText())) {
        System.out.println("Variable " + ctx.getChild(0).getText() + " already declared");
      } else {
        Id id = new Variable(ctx.getChild(0).getText(), Type.valueOf(this.vartype.toUpperCase()));
        ((Variable) id).setValue(ctx.getChild(2).getText());
        this.symbolTable.addId(id);
      }
    } else {
      if (symbolTable.containsLocalId(ctx.getChild(0).getText())) {
        Id id = symbolTable.getLocalId(ctx.getChild(0).getText());
        ((Variable) id).setValue(ctx.getChild(2).getText());
      } else {
        System.out.println("Variable " + ctx.getChild(0).getText() + " not declared");
      }
    }
  }

  @Override
  public void enterFunctionDeclaration(FunctionDeclarationContext ctx) {

  }


  @Override
  public void exitFunctionId(FunctionIdContext ctx) {
    if (symbolTable.containsLocalId(ctx.getText())) {
      System.out.println("Function " + ctx.getText() + " already declared");
    } else {
      Id id = new Function(ctx.getChild(1).getText(), Type.valueOf(ctx.getChild(0).getText().toUpperCase()), null);
      this.symbolTable.addId(id);
    }
    this.functionName = ctx.getText();
  }

  
  @Override
  public void exitParametersDeclaration(ParametersDeclarationContext ctx) {
    System.out.println("Exit Parameters Declaration" + ctx.getText());
    
    List<String> params = new LinkedList<String>();

    params.add(ctx.getChild(0).getText());
    params.add(ctx.getChild(1).getText());

    this.parameters.add(params);
  }

  @Override
  public void exitFunctionForwardDeclaration(FunctionForwardDeclarationContext ctx) {
    // TODO Auto-generated method stub
    super.exitFunctionForwardDeclaration(ctx);
  }

  @Override
  public void exitFunctionCall(FunctionCallContext ctx) {
    if (symbolTable.containsId(ctx.getChild(0).getText())) {
      Id id = symbolTable.getId(ctx.getChild(0).getText());
      if (id instanceof Function) {
        Function function = (Function) id;
        if (function.getParameters().size() == ctx.getChildCount() - 1) {
          for (int i = 1; i < ctx.getChildCount(); i++) {
            if (!function.getParameters().get(i - 1).getType()
                .equals(Type.valueOf(ctx.getChild(i).getText().toUpperCase()))) {
              System.out.println("Invalid parameter type");
            }
          }
        } else {
          System.out.println("Invalid number of parameters");
        }
      } else {
        System.out.println("Invalid function call");
      }
    } else {
      System.out.println("Function " + ctx.getChild(0).getText() + " not declared");
    }
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    System.out.println("Error: " + node.getText());
  }

  // Utility functions

}