package compiladores;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ErrorNode;

import compiladores.compiladoresParser.AssignationContext;
import compiladores.compiladoresParser.BlockContext;
import compiladores.compiladoresParser.DeclarationContext;
import compiladores.compiladoresParser.DeclaredVariableContext;
import compiladores.compiladoresParser.ExtendedDeclarationContext;
import compiladores.compiladoresParser.FunctionCallContext;
import compiladores.compiladoresParser.FunctionDeclarationContext;
import compiladores.compiladoresParser.FunctionForwardDeclarationContext;
import compiladores.compiladoresParser.FunctionIdContext;
import compiladores.compiladoresParser.ParametersContext;
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
  private static List<Type> parametersTypes;
  private String functionName = null;

  public CustomListener() {
    this.symbolTable = SymbolTable.getInstance();
    this.stack = new LinkedList<Map<String, Id>>();
    this.parameters = new LinkedList<List<String>>();
    this.parametersTypes = new LinkedList<Type>();
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
      Function function = (Function)this.symbolTable.getId(functionName);
      List<Type> params = new LinkedList<Type>();
      for(int i = 0; i < parameters.size(); i++) {
        params.add(Type.valueOf(parameters.get(i).get(0).toUpperCase()));
      }
      function.setParameters(params);
    }

    this.symbolTable.addScope();

    if(functionName != null) {
      for(int i = 0; i < parameters.size(); i++) {
        Id id = new Variable(parameters.get(i).get(1), Type.valueOf(parameters.get(i).get(0).toUpperCase()));
        id.setInitialized(true);
        this.symbolTable.addId(id);
      }
      functionName=null;
      parameters.clear();
    }
    

  }

  @Override
  public void exitBlock(BlockContext ctx) {
    //symbolTable.print();
    List<Id> notInitialized = this.symbolTable.searchNotInitialized();
    List<Id> notUsed = this.symbolTable.searchNotUsed();

    System.out.println("\n No Inicializado ->" + notInitialized);
    System.out.println("\nNo Utilizado ->" + notUsed);

    stack.add(this.symbolTable.removeScope());

    //symbolTable.print();
  }

  @Override
  public void enterDeclaration(DeclarationContext ctx) {
    //System.out.println(ctx.start.getText());
    this.vartype = ctx.getStart().getText();
  }

  @Override
  public void exitDeclaration(DeclarationContext ctx) {
    this.vartype = null;
    //System.out.println(ctx.getText());
    //System.out.println("Exit Declaration");
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
    //System.out.println(ctx.getChildCount());
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
        id.setInitialized(true);
        this.symbolTable.addId(id);
      }
    } else {
      if (symbolTable.containsLocalId(ctx.getChild(0).getText())) {
        Id id = symbolTable.getLocalId(ctx.getChild(0).getText());
        ((Variable) id).setValue(ctx.getChild(2).getText());
        id.setInitialized(true);
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
      id.setInitialized(true);
      this.symbolTable.addId(id);
      this.functionName = ctx.getChild(1).getText();
    }
    
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
        System.out.println(function.getParameters());
        System.out.println(this.parametersTypes);
        if (function.getParameters().size() == this.parametersTypes.size()) {
          for (int i = 0; i < this.parametersTypes.size(); i++) {
            if (!function.getParameters().get(i)
                .equals(this.parametersTypes.get(i))) {
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
    this.parametersTypes.clear();
  }

  

  @Override
  public void exitParameters(ParametersContext ctx) {
    if(symbolTable.containsId(ctx.getChild(0).getText())){
      Id id = symbolTable.getId(ctx.getChild(0).getText());
      if(id.isInitialized()==false){
        System.out.println("Variable " + ctx.getChild(0).getText() + " not initialized");
      }
        id.setUsed(true);
        Type type = id.getType();
        this.parametersTypes.add(type);
    }
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    System.out.println("Error: " + node.getText());
  }

  // Utility functions
  
}