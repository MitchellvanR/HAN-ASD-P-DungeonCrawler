package asd.project.compiler;

import asd.project.Agent;
import asd.project.ast.AST;
import asd.project.checker.Checker;
import asd.project.checker.SemanticError;
import asd.project.generator.Generator;
import asd.project.parser.ASTListener;
import asd.project.transforms.Evaluator;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Compiler implements ANTLRErrorListener {

  private static final Logger LOGGER = Logger.getLogger(Compiler.class.getName());
  private final Checker checker;
  private final Evaluator evaluator;
  private final Generator generator;
  private final List<String> errors;
  private AST ast;
  private boolean parsed;
  private boolean checked;
  private boolean transformed;
  private boolean generated;
  private Agent agent;
  private String generatedSourceCode;

  public Compiler() {
    this.errors = new ArrayList<>();

    this.parsed = false;
    this.checked = false;
    this.transformed = false;
    this.generated = false;

    this.checker = new Checker();
    this.evaluator = new Evaluator();
    this.generator = new Generator(this);
  }

  public void compile(String input) {
    parseString(input);
    if (parsed) {
      checkAST();
    }

    if (checked) {
      transformAST();
    }

    Optional<String> generatedAST = generateAST();
    if (transformed && generatedAST.isPresent()) {
      generatedSourceCode = generatedAST.get();
    }

    if (generated && ast.getErrors().isEmpty()) {
      String logMessage = "Generated code: " + getGeneratedSourceCode();
      LOGGER.log(Level.INFO, logMessage);
    } else {
      String logMessage = String.valueOf(ast.getErrors());
      LOGGER.log(Level.INFO, logMessage);
    }
  }

  private void parseString(String input) {
    CharStream inputStream = CharStreams.fromString(input.toLowerCase());
    AgentInstructionLexer lexer = new AgentInstructionLexer(inputStream);
    lexer.removeErrorListeners();
    lexer.addErrorListener(this);
    errors.clear();

    try {
      CommonTokenStream tokens = new CommonTokenStream(lexer);

      AgentInstructionParser parser = new AgentInstructionParser(tokens);
      parser.removeErrorListeners();
      parser.addErrorListener(this);

      ParseTree parseTree = parser.foundBehaviours();

      ASTListener listener = new ASTListener();
      ParseTreeWalker walker = new ParseTreeWalker();
      walker.walk(listener, parseTree);

      this.ast = listener.getAST();

    } catch (RecognitionException e) {
      this.ast = new AST();
      errors.add(e.getMessage());

    } catch (ParseCancellationException e) {
      this.ast = new AST();
      errors.add("Syntax error");
    }

    parsed = errors.isEmpty();
    checked = transformed = false;
  }

  private void checkAST() {
    if (ast == null || !errors.isEmpty()) {
      checked = false;
      return;
    }

    this.checker.check(this.ast);

    List<SemanticError> checkerErrors = this.ast.getErrors();
    if (!checkerErrors.isEmpty()) {
      for (SemanticError e : checkerErrors) {
        this.errors.add(e.toString());
      }
    }

    checked = checkerErrors.isEmpty();
    transformed = false;
  }

  private void transformAST() {
    if (ast == null || !errors.isEmpty()) {
      transformed = false;
      return;
    }

    this.evaluator.apply(this.ast);

    transformed = errors.isEmpty();
  }

  private Optional<String> generateAST() {
    if (ast == null || !errors.isEmpty()) {
      generated = false;
      return Optional.empty();
    }

    generated = true;

    return this.generator.generate(this.ast).describeConstable();
  }

  public String getGeneratedSourceCode() {
    if (parsed && checked && transformed && generated) {
      return generatedSourceCode;
    } else {
      return "Something went wrong compiling your input.";
    }
  }

  public Agent getAgent() {
    return agent;
  }

  public void setAgent(Agent agent) {
    this.agent = agent;
  }

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s,
      RecognitionException e) {
    /*
     * Method is empty, needs to be implemented because of implementation of ANTLRErrorListener interface.
     * Can be implemented in the future to handle syntax errors in AST
     */
  }

  @Override
  public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet,
      ATNConfigSet atnConfigSet) {
    /*
     * Method is empty, needs to be implemented because of implementation of ANTLRErrorListener interface.
     * Can be implemented in the future to report AST ambiguities
     */
  }

  @Override
  public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet,
      ATNConfigSet atnConfigSet) {
    /*
     * Method is empty, needs to be implemented because of implementation of ANTLRErrorListener interface.
     * Can be implemented in the future to report full context of the AST
     */
  }

  @Override
  public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2,
      ATNConfigSet atnConfigSet) {
    /*
     * Method is empty, needs to be implemented because of implementation of ANTLRErrorListener interface.
     * Can be implemented in the future to report sensitivities in the context of the AST
     */
  }
}
