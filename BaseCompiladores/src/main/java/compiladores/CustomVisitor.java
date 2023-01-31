package compiladores;

import java.util.Stack;

import org.antlr.v4.runtime.tree.ParseTree;
import org.javatuples.Quartet;
import org.javatuples.Quintet;

import compiladores.compiladoresParser.EContext;
import compiladores.compiladoresParser.EqContext;
import compiladores.compiladoresParser.ExpContext;
import compiladores.compiladoresParser.FactorContext;
import compiladores.compiladoresParser.LaContext;
import compiladores.compiladoresParser.LoContext;
import compiladores.compiladoresParser.LogAndContext;
import compiladores.compiladoresParser.LogOrContext;
import compiladores.compiladoresParser.NumberContext;
import compiladores.compiladoresParser.RContext;
import compiladores.compiladoresParser.RelContext;
import compiladores.compiladoresParser.TContext;
import compiladores.compiladoresParser.TermContext;
import compiladores.compiladoresParser.ValueContext;
import compiladores.utils.Quintets;
import compiladores.utils.ThreeAddressCodeManager;

public class CustomVisitor extends compiladoresBaseVisitor<Integer>{
	private ThreeAddressCodeManager tacManager = new ThreeAddressCodeManager(); 
	Quintets alopStack = new Quintets();


	@Override
	public Integer visitAssignation(compiladoresParser.AssignationContext ctx){

		if(ctx.value() != null) {
			System.out.println(ctx.value().getText());
			Quintet<String,String,String,String,String> newInstruction = new Quintet(null,ctx.value().getText(),null,ctx.ID().getText(), null);
			tacManager.getTac().add(newInstruction);
		}else if(ctx.alop() != null) {
			System.out.println(ctx.alop().getText());
		}

		return super.visitChildren(ctx);
	}

	@Override
	public Integer visitAlop(compiladoresParser.AlopContext ctx){
		//System.out.println("alop " + ctx.getText());
		return super.visitChildren(ctx);
	}

	@Override
	public Integer visitLogOr(LogOrContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogOr(ctx);
	}

	@Override
	public Integer visitLo(LoContext ctx) {
		//System.out.println("lo" + ctx.getText());
		return super.visitLo(ctx);
	}

	@Override
	public Integer visitLogAnd(LogAndContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogAnd(ctx);
	}

	@Override
	public Integer visitLa(LaContext ctx) {
		//System.out.println("La" + ctx.getText());
		return super.visitLa(ctx);
	}

	@Override
	public Integer visitEq(EqContext ctx) {
		// TODO Auto-generated method stub
		return super.visitEq(ctx);
	}

	@Override
	public Integer visitE(EContext ctx) {
		//System.out.println("E" + ctx.getText());
		return super.visitE(ctx);
	}

	@Override
	public Integer visitRel(RelContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRel(ctx);
	}

	@Override
	public Integer visitR(RContext ctx) {
		//System.out.println("R" + ctx.getText());
		return super.visitR(ctx);
	}

	@Override
	public Integer visitTerm(TermContext ctx) {
		// TODO Auto-generated method stub
		return super.visitTerm(ctx);
	}

	@Override
	public Integer visitT(TContext ctx) {
		//System.out.println("T" + ctx.getText());
		return super.visitT(ctx);
	}
	
	@Override
	public Integer visitExp(ExpContext ctx) {
		//System.out.println("exp " + ctx.getStart().getText());
		return super.visitExp(ctx);
	}

	@Override
	public Integer visitFactor(FactorContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFactor(ctx);
	}

	@Override
	public Integer visitValue(ValueContext ctx) {
		Quintet<String, String, String, String, String> quintet = alopStack.createEmptyQuintet();
		if(!ctx.children.isEmpty()) {

		}
		alopStack.add(quintet);		
		return super.visitValue(ctx);
	}

	@Override
	public Integer visitNumber(NumberContext ctx) {
		//System.out.println("number " + ctx.getText());
		return super.visitNumber(ctx);
	}

	
}
