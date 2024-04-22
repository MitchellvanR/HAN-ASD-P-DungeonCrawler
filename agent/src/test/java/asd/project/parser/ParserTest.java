package asd.project.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import asd.project.ast.AST;
import asd.project.compiler.AgentInstructionLexer;
import asd.project.compiler.AgentInstructionParser;
import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParserTest {
  AST parseTestFile(String resource) throws IOException {
    ClassLoader classLoader = this.getClass().getClassLoader();
      InputStream inputStream = classLoader.getResourceAsStream(resource);
    assert inputStream != null;
    CharStream charStream = CharStreams.fromStream(inputStream);
      AgentInstructionLexer lexer = new AgentInstructionLexer(charStream);

      CommonTokenStream tokens = new CommonTokenStream(lexer);

      AgentInstructionParser parser = new AgentInstructionParser(tokens);
      parser.setErrorHandler(new BailErrorStrategy());

      BaseErrorListener errorListener = new BaseErrorListener() {
        private String message;
        public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
          message = msg;
        }
        public String toString() {
          return message;
        }
      };
      parser.removeErrorListeners();
      parser.addErrorListener(errorListener);

      ASTListener listener = new ASTListener();
      try {
        ParseTree parseTree = parser.foundBehaviours();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, parseTree);
      } catch(ParseCancellationException e) {
        fail(errorListener.toString());
      }

    return listener.getAST();

  }

  @Test
  @DisplayName("parseTestFile1_agentinstruction0")
  void parseTestFile1() throws IOException {
    //Arrange
    AST sut = parseTestFile("agentinstruction0.ai");

    //Act
    AST exp = CompilerFixtures.uncheckedLevel0();

    //Assert
    assertEquals(exp, sut);
  }

  @Test
  @DisplayName("parseTestFile2_agentinstruction1")
  void parseTestFile2() throws IOException {
    //Arrange
    AST sut = parseTestFile("agentinstruction1.ai");

    //Act
    AST exp = CompilerFixtures.uncheckedLevel1();

    //Assert
    assertEquals(exp, sut);
  }

  @Test
  @DisplayName("parseTestFile3_agentinstruction2")
  void parseTestFile3() throws IOException {
    //Arrange
    AST sut = parseTestFile("agentinstruction2.ai");

    //Act
    AST exp = CompilerFixtures.uncheckedLevel2();

    //Assert
    assertEquals(exp, sut);
  }
  @Test
  @DisplayName("parseTestFile4_agentinstruction3")
  void parseTestFile4() throws IOException {
    //Arrange
    AST sut = parseTestFile("agentinstruction3.ai");

    //Act
    AST exp = CompilerFixtures.uncheckedLevel3();

    //Assert
    assertEquals(exp, sut);
  }

}
