package stax.frontend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import stax.frontend.Token.TokenError;
import stax.frontend.Token.TokenType;

public class Lexer {
  
  private boolean end;
  private BufferedReader input;
  private Token currentToken;
  // private int lineNum;
  // private int colNum;
  // private int offset;

  public static Lexer fromFile(FileReader source)
  {
    return new Lexer(source);
  }

  public static Lexer fromSource(String source)
  {
    return new Lexer(source);
  }

  private Lexer(FileReader inputFile)
  {
    end = false;
    input = new BufferedReader(inputFile);
  }

  private Lexer(String source)
  {
    end = false;
    input = new BufferedReader(new StringReader(source));
  }

  public ArrayList<Token> getAll()
  {
    ArrayList<Token> tokens = new ArrayList<>();

    peek();
    while (currentToken.type != TokenType.END) {
      tokens.add(currentToken);
      consume();
    }

    return tokens;
  }

  public Token peek()
  {
    if (currentToken == null) {
      getNextToken();
    }
    return currentToken;
  }

  public Token consume()
  {
    Token current = currentToken;
    getNextToken();
    return current;
  }

  private String lexString(BufferedReader br, char end) throws IOException
  {
    StringBuilder str = new StringBuilder();
    int s = 0;
    boolean done = false;
    while((s = br.read()) != -1) {
      str.append((char)s);
      if (s == end && ! done) {
        done = true;
      } else if (s == end) {
        break;
      }
    }
    return str.toString();
  }

  private String[] keywords = {
    "def", "dup", "dupn", "pop", "popn", "swap", "bubbn", "sinkn",
    "true", "false", "eval", "evalif", "typeof",
    "first", "rest", "len", "concat", "not", "or", "and",
    "add", "sub", "mult", "div", "divmod",
    "eq", "neq", "lt", "gt", "lte", "gte",
    "print", "println", "input", "set"
  };

  private String[] types = {
    "num", "str", "list", "bool", "id", "expr"
  };

  private String escapeCharacters(String str)
  {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      char result = c;
      if (c == '\\' && i < str.length() - 1) {
        i += 1;
        char v = str.charAt(i);
        switch (v) {
          case 'n':  result = (char)10; break;
          case 't':  result = (char)9; break;
          default:   result = v; break;
        }
      }
      sb.append(result);
    }

    return sb.toString();
  }

  private Token buildToken(String lexeme)
  {
    TokenError error = TokenError.NONE;
    TokenType type = TokenType.IDENTIFIER;
    if (lexeme.chars().filter(ch -> !((ch >= '0' && ch <= '9') || ch == '.' || ch == '-')).count() == 0)
    {
      type = TokenType.NUMBER;
      if (lexeme.chars().filter(ch -> ch == '.').count() > 1
        || lexeme.lastIndexOf("-") > 0)
      {
        type = TokenType.ERROR;
        error = TokenError.MALFORMED_NUMBER;
      }
    }
    else if (lexeme.charAt(0) == '\"' || lexeme.charAt(0) == '\'')
    {
      type = TokenType.STRING;
      if (lexeme.charAt(lexeme.length() - 1) != lexeme.charAt(0)) {
        type = TokenType.ERROR;
        error = TokenError.UNCLOSED_STRING;
      } else {
        lexeme = escapeCharacters(lexeme.substring(1, lexeme.length() - 1));
      }
    }
    else if (lexeme.charAt(0) == '(' && lexeme.length() == 1)  { type = TokenType.LPAREN; }
    else if (lexeme.charAt(0) == ')' && lexeme.length() == 1)  { type = TokenType.RPAREN; }
    else if (lexeme.charAt(0) == '[' && lexeme.length() == 1)  { type = TokenType.LSQUARE; }
    else if (lexeme.charAt(0) == ']' && lexeme.length() == 1)  { type = TokenType.RSQUARE; }
    else if (lexeme.charAt(0) == '{' && lexeme.length() == 1)  { type = TokenType.LCURLY; }
    else if (lexeme.charAt(0) == '}' && lexeme.length() == 1)  { type = TokenType.RCURLY; }
    else if (lexeme.charAt(0) == ',' && lexeme.length() == 1)  { type = TokenType.COMMA; }
    else if (lexeme.charAt(0) == ':' && lexeme.length() == 1)  { type = TokenType.COLON; }
    else if (lexeme.charAt(0) == '_' && lexeme.length() == 1)  { type = TokenType.UNDERSCORE; }
    else if (Arrays.stream(keywords).anyMatch(lexeme::equals)) { type = TokenType.KEYWORD; }
    else if (Arrays.stream(types).anyMatch(lexeme::equals))    { type = TokenType.TYPE; }

    return new Token(type, error, lexeme, 0, 0, 0);
  }

  private void getNextToken()
  {
    if (end) {
      currentToken = new Token(TokenType.END, TokenError.NONE, "", 0, 0, 0);
      return;
    }

    StringBuilder lex = new StringBuilder();    
    try {
      int c = 0;
      while((c = input.read()) != -1) {
        if ((char)c == '\"') {
          input.reset();
          lex.append(lexString(input, '\"'));
          break;
        } else if ((char)c == '\'') {
          input.reset();
          lex.append(lexString(input, '\''));
          break;
        } else if ((char)c == ' ' || (char)c == '\n' || (char)c == '\t' || (char)c == '\r' || (char)c == '\f') {
          if (lex.length() != 0) {
            break;
          }
        } else if ((char)c == '(' || (char)c == ')'
        || (char)c == '[' || (char)c == ']' 
        || (char)c == '{' || (char)c == '}'
        || (char)c == ',' || (char)c == '_' || (char)c == ':')
        {
          if (lex.length() == 0) {
            lex.append((char)c);
          } else {
            input.reset();
          }
          break;
        } else if ((char)c == '#') {
          if (lex.length() == 0) {
            while((char)c != '\n' && c != -1) {
              c = input.read();
            }
          } else {
            input.reset();
          }
        } else {
          lex.append((char)c);
        }
        input.mark(0);
      }
      if (c == -1) {
        end = true;
        if (lex.length() == 0) {
          currentToken = new Token(TokenType.END, TokenError.NONE, "", 0, 0, 0);
          return;
        }
      }
    } catch (Exception e) {
      currentToken = new Token(TokenType.ERROR, TokenError.FILE_ERROR, "An error occured while reading the source file: " + lex, 0, 0, 0);
    }
    currentToken = buildToken(lex.toString().trim());
  }

}
