package compiladores;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.ParserRuleContext;

import compiladores.compiladoresParser.BlockContext;
import compiladores.compiladoresParser.DeclarationContext;

public class myListener extends compiladoresBaseListener {

  @Override
  public void enterBlock(BlockContext ctx) {
    System.out.println("Block");
  }

  @Override
  public void enterDeclaration(DeclarationContext ctx) {
    System.out.println("Declaration");
  }

  @Override
  public void enterEveryRule(ParserRuleContext ctx) {
    System.out.println("Rule");
  }

}