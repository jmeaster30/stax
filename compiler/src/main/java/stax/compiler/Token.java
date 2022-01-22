package stax.compiler;

public class Token {

  public enum TokenType
  {
    IDENTIFIER, KEYWORD, TYPE, NUMBER, STRING,
    LPAREN, RPAREN, LSQUARE, RSQUARE, LCURLY, RCURLY, 
    COMMA, COLON, UNDERSCORE, ERROR, END
  }

  public enum TokenError
  {
    NONE, FILE_ERROR, MALFORMED_NUMBER, UNCLOSED_STRING
  }

  public TokenType type;
  public TokenError error;
  public String lexeme;
  public int line;
  public int columnStart;
  public int columnEnd;

  public Token(TokenType t, TokenError e, String lex, int ln, int colStart, int colEnd)
  {
    type = t;
    error = e;
    lexeme = lex;
    line = ln;
    columnStart = colStart;
    columnEnd = colEnd;
  }

  public Token(Token orig)
  {
    type = orig.type;
    error = orig.error;
    lexeme = orig.lexeme;
    line = orig.line;
    columnStart = orig.columnStart;
    columnEnd = orig.columnEnd;
  }

  public String toString()
  {
    return "[" + type.toString() + " - " + error.toString() + ": '" + lexeme + "' (" + line + ", " + columnStart +  ")]";
  }
}
