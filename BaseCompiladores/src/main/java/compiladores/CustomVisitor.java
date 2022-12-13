package compiladores;

import org.antlr.v4.runtime.tree.ParseTree;
import org.javatuples.Quartet;

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
import compiladores.utils.Quartets;

public class CustomVisitor extends compiladoresBaseVisitor<Integer>{
	private Quartets quartets = new Quartets(); 
	Quartet<String,String,String,String> tempQuartet;

	@Override
	public Integer visitAssignation(compiladoresParser.AssignationContext ctx){

		if(ctx.value() != null) {
			tempQuartet = new Quartet(null,ctx.value().getText(),null,ctx.ID().getText());
			quartets.add(tempQuartet);
			tempQuartet = new Quartet<String,String,String,String>(null, null, null, null);
		}else{
			tempQuartet = new Quartet(null,null,null,ctx.ID().getText());			
			//System.out.println(tempQuartet.toString());
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
	public Integer visitLogAnd(LogAndContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogAnd(ctx);
	}

	@Override
	public Integer visitEq(EqContext ctx) {
		// TODO Auto-generated method stub
		return super.visitEq(ctx);
	}

	@Override
	public Integer visitRel(RelContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRel(ctx);
	}

	@Override
	public Integer visitTerm(TermContext ctx) {
		// TODO Auto-generated method stub
		return super.visitTerm(ctx);
	}

	@Override
	public Integer visitLo(LoContext ctx) {
		//System.out.println("lo" + ctx.getText());
		return super.visitLo(ctx);
	}

	@Override
	public Integer visitLa(LaContext ctx) {
		//System.out.println("La" + ctx.getText());
		return super.visitLa(ctx);
	}

	@Override
	public Integer visitE(EContext ctx) {
		//System.out.println("E" + ctx.getText());
		return super.visitE(ctx);
	}

	@Override
	public Integer visitR(RContext ctx) {
		//System.out.println("R" + ctx.getText());
		return super.visitR(ctx);
	}

	@Override
	public Integer visitExp(ExpContext ctx) {
		//System.out.println("exp " + ctx.getStart().getText());
		
		 
		if (tempQuartet.getValue1()==null){
			return super.visitExp(ctx);
		}

		if (tempQuartet.getValue0()==null){
			tempQuartet.setAt0(ctx.getStart().getText());
			
		}

		return super.visitExp(ctx);
	}

	@Override
	public Integer visitT(TContext ctx) {
		//System.out.println("T" + ctx.getText());
		return super.visitT(ctx);
	}

	@Override
	public Integer visitFactor(FactorContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFactor(ctx);
	}

	@Override
	public Integer visitValue(ValueContext ctx) {
		System.out.println(tempQuartet.toString());
		if (tempQuartet.getValue3()==null){
			return super.visitValue(ctx);
		}

		if (tempQuartet.getValue1()==null){
			tempQuartet.addAt1(ctx.getText());
			System.out.println(tempQuartet.toString());
		}else if (tempQuartet.getValue2()==null){
			tempQuartet.setAt2(ctx.getText());
			quartets.add(tempQuartet);
			tempQuartet = new Quartet<String,String,String,String>(null, null, null, null);
			//System.out.println(quartets.toString());
		}

		
		return super.visitValue(ctx);
	}

	@Override
	public Integer visitNumber(NumberContext ctx) {
		//System.out.println("number " + ctx.getText());
		return super.visitNumber(ctx);
	}

	
}
