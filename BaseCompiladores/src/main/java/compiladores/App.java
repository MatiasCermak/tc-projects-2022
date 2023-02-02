package compiladores;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

// Las diferentes entradas se explicaran oportunamente
public class App {
    private static final String fileName = "input/parentesis.txt";
    private static final List<String> errors = new ArrayList<String>();

    public static void main(String[] args) throws Exception {
        // create a CharStream that reads from file
        CharStream input = CharStreams.fromFileName(fileName);

        // create a lexer that feeds off of input CharStream
        compiladoresLexer lexer = new compiladoresLexer(input);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        compiladoresParser parser = new compiladoresParser(tokens);

        // create Listener
        CustomListener listener = new CustomListener(errors);

        // Conecto el objeto con Listeners al parser
        parser.addParseListener(listener);

        // Solicito al parser que comience indicando una regla gramatical
        // En este caso la regla es el simbolo inicial
        ParseTree tree = parser.prog();

        if (errors.isEmpty()) {
            // Mostrar por consola
            // System.out.println(tree.toStringTree(parser));
            listener.printSymbolTable();
            // Mostrar en JFrame
            JFrame frame = new JFrame("Antlr Syntactic Tree");
            JPanel panel = new JPanel();
            TreeViewer viewer = new TreeViewer(Arrays.asList(
                    parser.getRuleNames()), tree);
            panel.add(viewer);
            JScrollPane jScrollPane = new JScrollPane(panel);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            frame.getContentPane().add(jScrollPane);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

						CustomVisitor visitor = new CustomVisitor();
						visitor.visit(tree);
        }
    }
}
