package runner;

import infrastructure.ErrorListeners.LexicalErrorListener;
import infrastructure.CompilationResultWriter;
import infrastructure.ErrorListeners.SemanticErrorListener;
import infrastructure.MessageBag;
import infrastructure.ErrorListeners.SyntaticErrorListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import laparser.LaLexer;
import laparser.LaParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Runs the LaParser on a inputFile and outputs the result in an outputFile.
 *
 * This is the main class of the LaParser project. Therefore, the method
 * Runner.main(String[] args) will be called by the evaluator.
 *
 * @author Lucas
 */
public class Runner {

    /**
     * Runs the LaParser on a file that contains a LA source-code.
     *
     * @param inputFile name of the LA source-code file
     * @param outputFile name of the analysis result file
     * @throws Exception
     */
    public void start(String inputFile, String outputFile) throws Exception {

        ANTLRInputStream in = new ANTLRInputStream(new FileInputStream(inputFile));
        MessageBag sintaticBag = new MessageBag();
        MessageBag semanticBag = new MessageBag();

        LaLexer lexer = new LaLexer(in);
        LaParser parser = new LaParser(new CommonTokenStream(lexer));

        parser.removeErrorListeners();
        lexer.removeErrorListeners();

        LexicalErrorListener lexical = new LexicalErrorListener(sintaticBag);
        SyntaticErrorListener syntatic = new SyntaticErrorListener(sintaticBag);
        SemanticErrorListener.DefineMessageBag(semanticBag);

        parser.addErrorListener(syntatic);
        lexer .addErrorListener(lexical);

        //parser.programa();
        LaParser.ProgramaContext resultado = parser.programa();
        File arquivo = new File("/resultado/resultado1.txt");
        
        try {
 
if (!arquivo.exists()) {
//cria um arquivo (vazio)
arquivo.createNewFile();
}
 
//caso seja um diretório, é possível listar seus arquivos e diretórios
File[] arquivos = arquivo.listFiles();
 
//escreve no arquivo
FileWriter fw = new FileWriter(arquivo, true);
 
BufferedWriter bw = new BufferedWriter(fw);
 
bw.write("texto no arquivo");
 
bw.newLine();
 
bw.close();
fw.close();

 
} catch (IOException ex) {
ex.printStackTrace();
}

        CompilationResultWriter writer = new CompilationResultWriter(outputFile);
        
        // put the first lexic/sintatic error in the writer's buffer
        try {
            writer.put(sintaticBag.first());
        } catch(IndexOutOfBoundsException e) {
            // or, case there none lexic/sintatic error, put all semantic errors
            // in the writer's buffer
            for (String message : semanticBag.all()) {
                writer.put(message);
            }
        }
        
        // close writer with standar message
        writer
            .put("Fim da compilacao")
            .close();
    }

    /**
     * Executes Runner.start() method with the arguments given.
     *
     * @param args array that contains the names of the input and bag files
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        new Runner().start(args[0], args[1]);
    }
}
