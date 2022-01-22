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

  public String toString()
  {
    return "[" + type.toString() + " - " + error.toString() + ": '" + lexeme + "' (" + line + ", " + columnStart +  ")]";
  }
}
