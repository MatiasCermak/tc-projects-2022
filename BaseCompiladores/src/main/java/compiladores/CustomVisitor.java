package compiladores;

import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.lang3.StringUtils;

import compiladores.compiladoresParser.AlopContext;
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
	Stack<Quintets> alopStack = new Stack<Quintets>();

	@Override
	public Void visitAssignation(compiladoresParser.AssignationContext ctx) {

		if (ctx.value() != null) {
			Quintet newInstruction = new Quintet();
			newInstruction.setArg1(ctx.value().getText());
			newInstruction.setRes(ctx.ID().getText());
			tacManager.getTac().add(newInstruction);
		} else if (ctx.alop() != null) {
			super.visitChildren(ctx);
			tacManager.getTac().addAll(alopStack.peek());
			Quintet newInstruction = new Quintet();
			newInstruction.setArg1(alopStack.peek().getLast().getRes());
			newInstruction.setRes(ctx.ID().getText());
			tacManager.getTac().add(newInstruction);
		}
		return null;
	}

	@Override
	public Void visitFunctionDeclaration(compiladoresParser.FunctionDeclarationContext ctx) {
		Quintet newFunction = new Quintet();
		newFunction.setOp("lbl");
		newFunction.setLabel(tacManager.createNewLabel());
		newFunction.setRes(ctx.functionId().ID().getText());
		tacManager.addFunction(newFunction);
		return super.visitChildren(ctx);
	}

	@Override
	public Void visitFunctionCall(compiladoresParser.FunctionCallContext ctx) {
		System.out.println(ctx.ID().getText());
		Quintet newInstruction = new Quintet();
		newInstruction.setOp("jmp");
		newInstruction.setArg1(tacManager.getFuntionLabel(ctx.ID().getText()));
		tacManager.getTac().add(newInstruction);
		tacManager.getTac().printQuintets();
		return super.visitChildren(ctx);
	}

	@Override
	public Void visitIif(compiladoresParser.IifContext ctx) {
		Quintet startIf = new Quintet();
		startIf.setOp("jnc");
		startIf.setArg2(tacManager.createNewLabel());

		super.visitAlop(ctx.alop());

		startIf.setArg1(alopStack.peek().getLast().getRes());
		tacManager.getTac().addAll(alopStack.peek());
		tacManager.getTac().add(startIf);

		if (ctx.block() != null) {
			super.visitBlock(ctx.block());
		} else {
			super.visitInstruction(ctx.instruction());
		}

		Quintet endIf = new Quintet();
		endIf.setOp("lbl");
		endIf.setLabel(startIf.getArg2());
		tacManager.getTac().add(endIf);
		return null;
	}

	@Override
	public Void visitIwhile(compiladoresParser.IwhileContext ctx) {
		Quintet startIf = new Quintet();
		startIf.setOp("jnc");
		startIf.setArg2(tacManager.createNewLabel());

		super.visitAlop(ctx.alop());

		startIf.setArg1(alopStack.peek().getLast().getRes());
		alopStack.peek().getFirst().setLabel(tacManager.createNewLabel());
		tacManager.getTac().addAll(alopStack.peek());
		tacManager.getTac().add(startIf);

		if (ctx.block() != null) {
			super.visitBlock(ctx.block());
		} else {
			super.visitInstruction(ctx.instruction());
		}

		Quintet jumpWhile = new Quintet();
		jumpWhile.setOp("jmp");
		jumpWhile.setArg1(alopStack.peek().getFirst().getLabel());
		tacManager.getTac().add(jumpWhile);

		Quintet endIf = new Quintet();
		endIf.setOp("lbl");
		endIf.setLabel(startIf.getArg2());
		tacManager.getTac().add(endIf);
		return null;
	}

	@Override
	public Void visitAlop(AlopContext ctx) {

		if (!(ctx.parent instanceof FactorContext)) {
			alopStack.clear();
		}
		alopStack.add(new Quintets());
		super.visitChildren(ctx);
		if (ctx.parent instanceof FactorContext) {
			Quintets parenthesesQuintets = alopStack.pop();
			if (alopStack.peek().isEmpty()) {
				alopStack.peek().addAll(parenthesesQuintets);
				Quintet unitingOp = new Quintet();
				unitingOp.setArg1(alopStack.peek().getLast().getRes());
				unitingOp.setRes(tacManager.createNewTempVariable());
				alopStack.peek().add(unitingOp);
			} else {
				Quintet lastIncompleQuintet = alopStack.peek().getLastIncompleteQuintet();
				alopStack.peek().addAll(alopStack.peek().indexOf(alopStack.peek().getLastIncompleteQuintet()),
						parenthesesQuintets);
				Quintet unitingOp = lastIncompleQuintet;
				if (StringUtils.isEmpty(unitingOp.getArg1())) {
					unitingOp.setArg1(parenthesesQuintets.getLast().getRes());
				} else {
					unitingOp.setArg2(parenthesesQuintets.getLast().getRes());
				}
			}
		}
		return null;
	}

	@Override
	public Void visitLo(LoContext ctx) {
		return super.visitLo(ctx);
	}

	@Override
	public Void visitLogOr(LogOrContext ctx) {
		return super.visitLogOr(ctx);
	}

	@Override
	public Void visitLa(LaContext ctx) {
		return super.visitLa(ctx);
	}

	@Override
	public Void visitLogAnd(LogAndContext ctx) {
		return super.visitLogAnd(ctx);
	}

	@Override
	public Void visitE(EContext ctx) {

		if (ctx.children == null) {
			System.out.println("Nodo final " + ctx.getClass().getSimpleName());
			return null;
		}

		this.processAlop(ctx, "*", "/", "%", "+", "-", ">", "<", "<=", ">=", "==", "!=");
		return super.visitE(ctx);
	}

	@Override
	public Void visitEq(EqContext ctx) {
		return super.visitEq(ctx);
	}

	@Override
	public Void visitR(RContext ctx) {
		if (ctx.children == null) {
			System.out.println("Nodo final " + ctx.getClass().getSimpleName());
			return null;
		}

		this.processAlop(ctx, "*", "/", "%", "+", "-", ">", "<", "<=", ">=");

		return super.visitR(ctx);
	}

	@Override
	public Void visitRel(RelContext ctx) {
		return super.visitRel(ctx);
	}

	@Override
	public Void visitExp(ExpContext ctx) {
		if (ctx.children == null) {
			System.out.println("Nodo final " + ctx.getClass().getSimpleName());
			return null;
		}

		this.processAlop(ctx, "*", "/", "%", "+", "-");

		return super.visitExp(ctx);
	}

	@Override
	public Void visitTerm(TermContext ctx) {
		return super.visitTerm(ctx);
	}

	@Override
	public Void visitT(TContext ctx) {
		if (ctx.children == null) {
			System.out.println("Nodo final " + ctx.getClass().getSimpleName());
			return null;
		}

		this.processAlop(ctx, "*", "/", "%");

		return super.visitT(ctx);
	}

	@Override
	public Void visitFactor(FactorContext ctx) {
		return super.visitFactor(ctx);
	}

	@Override
	public Void visitValue(ValueContext ctx) {
		Quintet newOp;

		if (alopStack.peek().isEmpty()) {
			newOp = new Quintet();
			newOp.setRes(tacManager.createNewTempVariable());
			alopStack.peek().add(newOp);
		} else {
			newOp = alopStack.peek().getLastIncompleteQuintet();
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
		return super.visitNumber(ctx);
	}

	private void processAlop(ParserRuleContext ctx, String... searchStrings) {
		if (StringUtils.isEmpty(alopStack.peek().getLastIncompleteQuintet().getOp())) {
			alopStack.peek().getLastIncompleteQuintet().setOp(ctx.getStart().getText());
		} else {
			Quintet lastQuintet = alopStack.peek().getLastIncompleteQuintet();
			if (StringUtils.equalsAny(lastQuintet.getOp(), searchStrings)) {
				Quintet quintet = new Quintet();
				quintet.setArg1(tacManager.createNewTempVariable());
				quintet.setOp(ctx.getStart().getText());
				quintet.setRes(lastQuintet.getRes());
				lastQuintet.setRes(quintet.getArg1());
				alopStack.peek().add(quintet);
			} else {
				Quintet quintet = new Quintet();
				String newVar = tacManager.createNewTempVariable();
				quintet.setArg1(lastQuintet.getArg2());
				quintet.setOp(ctx.getStart().getText());
				lastQuintet.setArg2(lastQuintet.getRes());
				quintet.setRes(lastQuintet.getRes());
				lastQuintet.setRes(newVar);
				alopStack.peek().add(alopStack.peek().size() - 1, quintet);
			}
		}
	}
}
