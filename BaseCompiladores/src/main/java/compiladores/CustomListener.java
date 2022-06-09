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
import compiladores.compiladoresParser.IforContext;
import compiladores.compiladoresParser.ParametersContext;
import compiladores.compiladoresParser.ParametersDeclarationContext;
import compiladores.compiladoresParser.ProgContext;
import compiladores.compiladoresParser.SimpleDeclarationContext;
import compiladores.compiladoresParser.TypesDeclarationContext;
import compiladores.domain.*;
import compiladores.utils.SymbolTable;

public class CustomListener extends compiladoresBaseListener {
  private SymbolTable symbolTable;
  private String vartype = null;
  private List<Map<String, Id>> stack;
  private List<List<String>> parameters;
  private List<Type> parametersTypes;
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
    // System.out.println("Entrada a bloque");
    if (functionName != null) {

      Function function = (Function) this.symbolTable.getId(functionName);

      if (function.getParameters() == null) {
        List<Type> params = new LinkedList<Type>();
        for (int i = 0; i < parameters.size(); i++) {
          params.add(Type.valueOf(parameters.get(i).get(0).toUpperCase()));
        }
        function.setParameters(params);

      } else {
        if (parameters.size() != function.getParameters().size()) {
          System.out.println("Error: Numero de parametros incorrecto");
        } else {

          for (int i = 0; i < parameters.size(); i++) {
            if (!function.getParameters().get(i).getType()
                .equals(parameters.get(i).get(0))) {
              System.out.println("Error: El tipo de parametro " + parameters.get(i).get(0)
                  + " no coincide con el tipo de la funcion " + functionName);
            }
          }
        }

      }

    }

    if (!(ctx.getParent() instanceof IforContext)) {
      this.symbolTable.addScope();
    }

    if (functionName != null) {
      for (int i = 0; i < parameters.size(); i++) {

        Id id = new Variable(parameters.get(i).get(1), Type.valueOf(parameters.get(i).get(0).toUpperCase()));
        id.setInitialized(true);
        this.symbolTable.addId(id);
      }
      functionName = null;
      parameters.clear();
    }

  }

  @Override
  public void exitBlock(BlockContext ctx) {
    // symbolTable.print();
    List<Id> notInitialized = this.symbolTable.searchNotInitialized();
    List<Id> notUsed = this.symbolTable.searchNotUsed();

    System.out.println("\n No Inicializado ->" + notInitialized);
    System.out.println("\n No Utilizado ->" + notUsed);

    stack.add(this.symbolTable.removeScope());

    // symbolTable.print();
  }

  @Override
  public void enterIfor(IforContext ctx) {
    this.symbolTable.addScope();
  }

  @Override
  public void exitIfor(IforContext ctx) {
    stack.add(this.symbolTable.removeScope());
  }

  @Override
  public void enterDeclaration(DeclarationContext ctx) {
    // System.out.println(ctx.start.getText());
    this.vartype = ctx.getStart().getText();
  }

  @Override
  public void exitDeclaration(DeclarationContext ctx) {
    this.vartype = null;
    // System.out.println(ctx.getText());
    // System.out.println("Exit Declaration");
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
    System.out.println(ctx.getText());
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
        if (id instanceof Function) {
          System.out.println("Attempted assignation to " + ctx.getChild(0).getText() + " which is a function");
        } else {
          ((Variable) id).setValue(ctx.getChild(2).getText());
          id.setInitialized(true);
        }
      } else {
        System.out.println("Variable " + ctx.getChild(0).getText() + " not declared");
      }
    }
  }

  @Override
  public void exitFunctionId(FunctionIdContext ctx) {

    if (symbolTable.containsLocalId(ctx.getChild(1).getText())) {
      Id id = symbolTable.getId(ctx.getChild(1).getText());
      if (id.isInitialized() || ctx.getParent() instanceof FunctionForwardDeclarationContext) {
        System.out.println("Function " + ctx.getChild(1).getText() + " already declared");
      } else {
        id.setInitialized(true);
      }
    } else {
      Id id = new Function(ctx.getChild(1).getText(), Type.valueOf(ctx.getChild(0).getText().toUpperCase()), null);
      if (ctx.getParent() instanceof FunctionDeclarationContext) {
        id.setInitialized(true);
      }
      this.symbolTable.addId(id);

    }
    this.functionName = ctx.getChild(1).getText();

  }

  @Override
  public void exitTypesDeclaration(TypesDeclarationContext ctx) {
    List<String> params = new LinkedList<String>();

    params.add(ctx.getChild(0).getText());

    parameters.add(params);
  }

  @Override
  public void exitParametersDeclaration(ParametersDeclarationContext ctx) {

    List<String> params = new LinkedList<String>();

    params.add(ctx.getChild(0).getText());
    params.add(ctx.getChild(1).getText());

    parameters.add(params);
  }

  @Override
  public void exitFunctionForwardDeclaration(FunctionForwardDeclarationContext ctx) {

    if (functionName != null) {
      Function function = (Function) this.symbolTable.getId(functionName);
      List<Type> params = new LinkedList<Type>();
      for (int i = 0; i < parameters.size(); i++) {
        params.add(Type.valueOf(parameters.get(i).get(0).toUpperCase()));
      }
      function.setParameters(params);
    }
    functionName = null;
    parameters.clear();
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
    parametersTypes.clear();
  }

  @Override
  public void exitParameters(ParametersContext ctx) {
    if (symbolTable.containsId(ctx.getChild(0).getText())) {
      Id id = symbolTable.getId(ctx.getChild(0).getText());
      if (id.isInitialized() == false) {
        System.out.println("Variable " + ctx.getChild(0).getText() + " not initialized");
      }
      id.setUsed(true);
      Type type = id.getType();
      parametersTypes.add(type);
    }
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    System.out.println("Error: " + node.getText());
  }

  // Utility functions

  private Type getTypeFromValue(String text) {
    if (symbolTable.containsId(text)) {
      return symbolTable.getId(text).getType();
    } else if (text.startsWith("\"") || text.startsWith("\'")) {
      return Type.CHAR;
    } else if (true)
      return null;
  }

}