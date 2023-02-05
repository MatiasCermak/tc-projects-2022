package compiladores;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

import compiladores.compiladoresParser.AssignationContext;
import compiladores.compiladoresParser.BlockContext;
import compiladores.compiladoresParser.DeclarationContext;
import compiladores.compiladoresParser.DeclaredVariableContext;
import compiladores.compiladoresParser.FunctionCallContext;
import compiladores.compiladoresParser.FunctionDeclarationContext;
import compiladores.compiladoresParser.FunctionForwardDeclarationContext;
import compiladores.compiladoresParser.FunctionIdContext;
import compiladores.compiladoresParser.IforContext;
import compiladores.compiladoresParser.ParametersContext;
import compiladores.compiladoresParser.ParametersDeclarationContext;
import compiladores.compiladoresParser.ProgContext;
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
  private List<String> errors;

  public CustomListener(List<String> errors) {
    this.symbolTable = SymbolTable.getInstance();
    this.stack = new LinkedList<Map<String, Id>>();
    this.parameters = new LinkedList<List<String>>();
    this.parametersTypes = new LinkedList<Type>();
    this.errors = errors;
  }

  @Override
  public void exitProg(ProgContext ctx) {
    if (this.symbolTable.containsLocalId("main")) {
      this.symbolTable.getId("main").setUsed(true);
    }
    variableCheck(ctx);
    stack.add(this.symbolTable.removeScope());
  }

  @Override
  public void enterProg(ProgContext ctx) {
    this.symbolTable.addScope();
  }

  @Override
  public void enterBlock(BlockContext ctx) {
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
          printSemanticError("Incorrect number of parameters", ctx);
        } else {

          for (int i = 0; i < parameters.size(); i++) {
            if (!function.getParameters().get(i).getType()
                .equals(parameters.get(i).get(0))) {
              printSemanticError("The parameter of type " + parameters.get(i).get(0)
                  + " does not match function type " + functionName, ctx);
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
    variableCheck(ctx);
    stack.add(this.symbolTable.removeScope());
  }

  @Override
  public void enterIfor(IforContext ctx) {
    this.symbolTable.addScope();
  }

  @Override
  public void exitIfor(IforContext ctx) {
    variableCheck(ctx);
    stack.add(this.symbolTable.removeScope());
  }

  @Override
  public void enterDeclaration(DeclarationContext ctx) {
    this.vartype = ctx.getStart().getText();
  }

  @Override
  public void exitDeclaration(DeclarationContext ctx) {
    this.vartype = null;
  }

  @Override
  public void exitDeclaredVariable(DeclaredVariableContext ctx) {
    if (symbolTable.containsLocalId(ctx.getText())) {
      printSemanticError("Variable " + ctx.getText() + " already declared", ctx);
    } else {
      Id id = new Variable(ctx.getText(), Type.valueOf(this.vartype.toUpperCase()));
      this.symbolTable.addId(id);
    }
  }

  @Override
  public void exitAssignation(AssignationContext ctx) {
    if (vartype != null) {
      if (symbolTable.containsLocalId(ctx.getChild(0).getText())) {
        printSemanticError("Variable " + ctx.getChild(0).getText() + " already declared", ctx);
      } else {
        Id id = new Variable(ctx.getChild(0).getText(), Type.valueOf(this.vartype.toUpperCase()));
        id.setInitialized(true);
        this.symbolTable.addId(id);
      }
    } else {
      if (symbolTable.containsLocalId(ctx.getChild(0).getText())) {
        Id id = symbolTable.getLocalId(ctx.getChild(0).getText());
        if (id instanceof Function) {
          printSemanticError("Attempted assignation to " + ctx.getChild(0).getText() + " which is a function", ctx);
        } else {
          id.setInitialized(true);
          id.setUsed(true);
        }
      } else {
        printSemanticError("Variable " + ctx.getChild(0).getText() + " not declared", ctx);
      }
    }
  }

  @Override
  public void exitFunctionId(FunctionIdContext ctx) {

    if (symbolTable.containsLocalId(ctx.getChild(1).getText())) {
      Id id = symbolTable.getId(ctx.getChild(1).getText());
      if (id.isInitialized() || ctx.getParent() instanceof FunctionForwardDeclarationContext) {
        printSemanticError("Function " + ctx.getChild(1).getText() + " already declared", ctx);
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
    if(ctx.getChild(0) == null) return;

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
        if (function.getParameters().size() == this.parametersTypes.size()) {
          for (int i = 0; i < this.parametersTypes.size(); i++) {
            if (!function.getParameters().get(i)
                .equals(this.parametersTypes.get(i))) {
              printSemanticError("Invalid parameter types", ctx);
              return;
            }
          }
          function.setUsed(true);
        } else {
          printSemanticError("Invalid number of parameters", ctx);
        }
      } else {
        printSemanticError("Invalid function call", ctx);
      }
    } else {
      printSemanticError("Function " + ctx.getChild(0).getText() + " not declared", ctx);
    }
    parametersTypes.clear();
  }

  @Override
  public void exitParameters(ParametersContext ctx) {
    if(ctx.getChild(0)==null){
      return;
    }
    if (symbolTable.containsId(ctx.getChild(0).getText())) {
      Id id = symbolTable.getId(ctx.getChild(0).getText());
      if (id.isInitialized() == false) {
        printSemanticError("Variable " + ctx.getChild(0).getText() + " not initialized", ctx);
      }
      id.setUsed(true);
      Type type = id.getType();
      parametersTypes.add(type);
    }
  }

  private void printSemanticError(String text, RuleContext ctx) {
    ParserRuleContext pctx = (ParserRuleContext) ctx;
    String errStr = "Semantic error in line " + pctx.getStart().getLine() + ": " + text;
    System.out.println("\n" + errStr);
    this.errors.add(errStr);
  }

  public void printSymbolTable() {
    int level = this.stack.size();
    for (Map<String, Id> scope : this.stack) {
      System.out.println("\n\n\nScope level: " + level);
      level--;
      for (Map.Entry<String, Id> entry : scope.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
      }
    }
  }

  public void variableCheck(RuleContext ctx) {
    List<Id> notInitialized = this.symbolTable.searchNotInitialized();
    List<Id> notUsed = this.symbolTable.searchNotUsed();

    if (!notInitialized.isEmpty()) {
      printSemanticError("\n Not initialized ->" + notInitialized, ctx);
    }
    if (!notUsed.isEmpty()) {
      printSemanticError("\n Not used ->" + notUsed, ctx);
    }
  }
}