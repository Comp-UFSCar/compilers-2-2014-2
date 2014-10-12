package compiler;

import grammar.ReceiptLexer;
import grammar.ReceiptParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 *
 * @author Lucas
 */
public class Compiler {

    String in;
    String out;
    
    Compiler() {
        this("src/input/test.txt", "src/json/test.json");
    }

    Compiler(String in, String out) {
        try {
            if (in.isEmpty() || out.isEmpty()) {
                throw new IllegalArgumentException("Parameters in and/or out cannot be an empty String");
            }

            this.in = in;
            this.out = out;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        
        Compiler compiler;

        if (args.length == 2) {
            compiler = new Compiler(args[0], args[1]);
        } else {
            compiler = new Compiler();
        }

        compiler.start();
    }

    private void start() throws IOException {
        ANTLRInputStream inputStream = new ANTLRInputStream(new FileInputStream(in));
        
        ReceiptLexer   lexer = new ReceiptLexer(inputStream);
        ReceiptParser parser = new ReceiptParser(new CommonTokenStream(lexer));
        
        parser.receipt();
    }
}
