package compiladores;

import org.antlr.v4.runtime.tree.RuleNode;
import org.apache.commons.lang3.StringUtils;

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
import compiladores.utils.Quintet;
import compiladores.utils.Quintets;
import compiladores.utils.ThreeAddressCodeManager;

public class CustomVisitor extends compiladoresBaseVisitor<Void> {
	private ThreeAddressCodeManager tacManager = new ThreeAddressCodeManager();
	Quintets alopStack = new Quintets();
	Quintets jumpStack = new Quintets();
	Boolean labelToSet = false;
	Boolean closeBracket = false;

	@Override
	public Void visitAssignation(compiladoresParser.AssignationContext ctx) {

		if (ctx.value() != null) {
			System.out.println("visitAssignation" + ctx.value().getText());
			Quintet newInstruction = new Quintet();
			newInstruction.setArg1(ctx.value().getText());
			newInstruction.setRes(ctx.ID().getText());
			tacManager.getTac().add(newInstruction);
		} else if (ctx.alop() != null) {
			super.visitChildren(ctx);
			// System.out.println(ctx.alop().getText());
		}
		return null;
	}

	@Override
	public Void visitFunctionDeclaration(compiladoresParser.FunctionDeclarationContext ctx) {
		//System.out.println(ctx.functionId().ID().getText());
		Quintet newFunction = new Quintet();
		newFunction.setOp("lbl");
		newFunction.setLabel(tacManager.createNewLabel());
		newFunction.setRes(ctx.functionId().ID().getText());
		tacManager.addFunction(newFunction);
		return super.visitFunctionDeclaration(ctx);
	}

	@Override
	public Void visitFunctionCall(compiladoresParser.FunctionCallContext ctx) {
		System.out.println(ctx.ID().getText());
		Quintet newInstruction = new Quintet();
		newInstruction.setOp("jmp");
		newInstruction.setArg1(tacManager.getFuntionLabel(ctx.ID().getText()));
		tacManager.getTac().add(newInstruction);
		tacManager.getTac().printQuintets();
		return super.visitFunctionCall(ctx);
	}

	@Override
	public Void visitIif(compiladoresParser.IifContext ctx) {
		Quintet startIf = new Quintet();
		//labelToSet = true;
		startIf.setOp("jnc");
		startIf.setLabel(tacManager.createNewLabel());
		tacManager.getTac().add(startIf);
		
		super.visitChildren(ctx);
		
		Quintet endIf = new Quintet();
		endIf.setOp("lbl");
		endIf.setLabel(tacManager.createNewLabel());
		tacManager.getTac().getLast().setRes(endIf.getLabel());
		tacManager.getTac().add(endIf);
		return super.visitIif(ctx);
	}

	@Override
	public Void visitIwhile(compiladoresParser.IwhileContext ctx){
		Quintet startWhile = new Quintet();
		//labelToSet = true;
		startWhile.setOp("jnc");
		startWhile.setLabel(tacManager.createNewLabel());
		tacManager.getTac().add(startWhile);
		
		super.visitChildren(ctx);
		
		Quintet endIf = new Quintet();
		endIf.setOp("lbl");
		endIf.setLabel(tacManager.createNewLabel());
		endIf.setRes(startWhile.getLabel());
		tacManager.getTac().getLast().setRes(endIf.getLabel());
		tacManager.getTac().add(endIf);
		return super.visitIwhile(ctx);
	}

	@Override
	public Void visitAlop(compiladoresParser.AlopContext ctx) {

		alopStack.clear();
		super.visitChildren(ctx);
		// System.out.println(alopStack);
		tacManager.getTac().addAll(alopStack);
		return null;
	}

	@Override
	public Void visitLo(LoContext ctx) {
		// System.out.println("lo" + ctx.getText());
		return super.visitLo(ctx);
	}

	@Override
	public Void visitLogOr(LogOrContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogOr(ctx);
	}

	@Override
	public Void visitLa(LaContext ctx) {
		// System.out.println("La" + ctx.getText());
		return super.visitLa(ctx);
	}

	@Override
	public Void visitLogAnd(LogAndContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogAnd(ctx);
	}

	@Override
	public Void visitE(EContext ctx) {
		// System.out.println("E" + ctx.getText());
		return super.visitE(ctx);
	}

	@Override
	public Void visitEq(EqContext ctx) {
		System.out.println("Eq" + ctx.r().getStart().getText());


		Quintet lastQuintet = tacManager.getTac().getLast();
			if (StringUtils.equalsAny(lastQuintet.getOp(), "jnc")) {
				Quintet quintet = new Quintet();
				quintet.setArg1(ctx.rel().term().factor().value().number().getText());
				quintet.setOp(ctx.r().getStart().getText());
				quintet.setArg2(ctx.rel().term().factor().value().number().getText());
				quintet.setRes(tacManager.createNewTempVariable());
				lastQuintet.setArg1(quintet.getRes());
				tacManager.getTac().add(quintet);
			} 
	
		return super.visitEq(ctx);
	}

	@Override
	public Void visitR(RContext ctx) {
		//System.out.println("R" + ctx.getText());

		

		return super.visitR(ctx);
	}

	@Override
	public Void visitRel(RelContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRel(ctx);
	}

	@Override
	public Void visitExp(ExpContext ctx) {
		if (ctx.children == null) {
			// System.out.println("Nodo final " + ctx.getClass().getSimpleName());
			return null;
		}
		// System.out.println("visitExp" + ctx.getStart().getText());
		if (StringUtils.isEmpty(alopStack.getLast().getOp())) {
			alopStack.getLast().setOp(ctx.getStart().getText());
		} else {
			Quintet lastQuintet = alopStack.getLast();
			if (StringUtils.equalsAny(lastQuintet.getRes(), "*", "/", "%", "+", "-")) {
				Quintet quintet = new Quintet();
				quintet.setArg1(tacManager.createNewTempVariable());
				quintet.setOp(ctx.getStart().getText());
				quintet.setRes(lastQuintet.getRes());
				lastQuintet.setRes(quintet.getArg1());
				alopStack.add(quintet);
			} else {
				Quintet quintet = new Quintet();
				quintet.setArg1(lastQuintet.getArg2());
				quintet.setOp(ctx.getStart().getText());
			}

		}
		return super.visitExp(ctx);
	}

	@Override
	public Void visitTerm(TermContext ctx) {
		// TODO Auto-generated method stub
		return super.visitTerm(ctx);
	}

	@Override
	public Void visitT(TContext ctx) {
		if (ctx.children == null) {
			// System.out.println("Nodo final " + ctx.getClass().getSimpleName());
			return null;
		}

		if (StringUtils.isEmpty(alopStack.getLast().getOp())) {
			alopStack.getLast().setOp(ctx.getStart().getText());
		} else {
			Quintet lastQuintet = alopStack.getLast();
			if (StringUtils.equalsAny(lastQuintet.getRes(), "*", "/", "%")) {
				Quintet quintet = new Quintet();
				quintet.setArg1(lastQuintet.getRes());
				quintet.setOp(ctx.getStart().getText());
				quintet.setRes(tacManager.createNewTempVariable());
				alopStack.add(quintet);
			} else {

			}
		}
		return super.visitT(ctx);
	}

	@Override
	public Void visitFactor(FactorContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFactor(ctx);
	}

	@Override
	public Void visitValue(ValueContext ctx) {
		Quintet newOp;

		if (alopStack.isEmpty()) {
			newOp = new Quintet();
			newOp.setRes(tacManager.createNewTempVariable());
			alopStack.add(newOp);
		} else {
			newOp = alopStack.getLast();
		}

		if (StringUtils.isEmpty(newOp.getArg1())) {
			newOp.setArg1(ctx.getText());
		} else {
			newOp.setArg2(ctx.getText());
		}
		return super.visitValue(ctx);
	}

	@Override
	public Void visitNumber(NumberContext ctx) {
		// System.out.println("number " + ctx.getText());
		return super.visitNumber(ctx);
	}

}
