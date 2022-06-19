package compiladores;

import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class CustomErrorListener extends BaseErrorListener {
    private List<String> errors;

    CustomErrorListener(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
            String text, RecognitionException e) {
        String errStr = "Syntax Error in line " + line + ": " + text;
        errors.add(errStr);
        System.out.println(errStr);
    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {
        errors.add("Report context sensitivity at line " + i);
        System.out.println("Report context sensitivity at line " + i);
    }
}
