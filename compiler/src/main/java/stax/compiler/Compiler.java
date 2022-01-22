package stax.compiler;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import stax.compiler.Token.TokenType;
import stax.compiler.Type.BaseType;

public class Compiler {
    
  public ArrayList<Instruction> instructions;

  private Lexer lexer;
  private ArrayList<Pass> semanticPasses;

  public Compiler(String sourcePath) throws FileNotFoundException
  {
    instructions = new ArrayList<>();
    lexer = Lexer.fromFilepath(sourcePath);
    semanticPasses = new ArrayList<>();
    semanticPasses.add(new Passthrough());
  }

  public void compile()
  {
    System.out.println("Start Compiling...");

    ArrayList<Token> tokens = lexer.getAll();
    instructions = parse(tokens);

    for (int i = 0; i < semanticPasses.size(); i++) {
      instructions = semanticPasses.get(i).run(instructions);
    }

    for (int i = 0; i < instructions.size(); i++)
    {
      Instruction in = instructions.get(i);
      System.out.println(in.toString());
    }
  }

  private int parseType(int startIndex, ArrayList<Token> tokens, ArrayList<Instruction> insts)
  {
    int i = startIndex;
    Token token = tokens.get(i);
    Value type = new Value(BaseType.ANY, token);
    if (token.lexeme.equals("expr"))
    {
      type.type = new Type(BaseType.EXPR);
      i++;
      Token t = tokens.get(i);
      if (t.type == TokenType.LCURLY) {
        i++;
        t = tokens.get(i);
        boolean afterColon = false;
        while (t.type != TokenType.RCURLY) {
          if (t.type == TokenType.UNDERSCORE) {
            if (afterColon) {
              type.type.outputs.add(new Type(BaseType.ANY));
            } else {
              type.type.inputs.add(new Type(BaseType.ANY));
            }
          }
          else if (t.type == TokenType.TYPE) {
            if (afterColon) {
              type.type.outputs.add(Type.FromToken(t));
            } else {
              type.type.inputs.add(Type.FromToken(t));
            }
          }
          else if (t.type == TokenType.COLON) {
            afterColon = true;
          }
          else {
            // TODO ERRORS :(
          }
          i++;
          t = tokens.get(i);
        }
      } else {
        i--; // whoops go back
      }
    }
    else if (token.lexeme.equals("num"))
    {
      type.type = new Type(BaseType.NUMBER);
    }
    else if (token.lexeme.equals("str"))
    {
      type.type = new Type(BaseType.STRING);
    }
    else if (token.lexeme.equals("list"))
    {
      type.type = new Type(BaseType.LIST);
    }
    else if (token.lexeme.equals("bool"))
    {
      type.type = new Type(BaseType.BOOL);
    }
    else if (token.lexeme.equals("char"))
    {
      type.type = new Type(BaseType.CHAR);
    }
    else if (token.lexeme.equals("id"))
    {
      type.type = new Type(BaseType.ID);
    }
    insts.add(Instruction.CreatePushVal(type));
    return i;
  }

  private int parseList(int index, ArrayList<Token> tokens, ArrayList<Instruction> insts)
  {
    int result = index;
    result++;

    int elementCount = 0;
    Token token = tokens.get(result);
    while (token.type != TokenType.RSQUARE)
    {
      elementCount++;
      while (token.type != TokenType.COMMA && token.type != TokenType.RSQUARE)
      {
        result = parseBasic(result, tokens, insts);
        result++;
        token = tokens.get(result);
      }

      if (token.type == TokenType.RSQUARE)
      {
        break;
      }

      result++;
      token = tokens.get(result);
    }

    insts.add(Instruction.CreatePushList(elementCount));

    return result;
  }

  private int parseExpr(int index, ArrayList<Token> tokens, ArrayList<Instruction> insts) {
    int result = index;
    result++;

    ArrayList<Instruction> exprInsts = new ArrayList<>();

    Token token = tokens.get(result);
    while (token.type != TokenType.RPAREN)
    {
      result = parseBasic(result, tokens, exprInsts);
      result++;
      token = tokens.get(result);
    }

    if (!exprInsts.isEmpty())
    {
      insts.add(Instruction.CreatePushVal(Value.CreateExpr(exprInsts)));
    }

    return result;
  }

  private int parseBasic(int index, ArrayList<Token> tokens, ArrayList<Instruction> insts) {
    int result = index;
    Token token = tokens.get(result);
    if (token.type == TokenType.NUMBER) {
      insts.add(Instruction.CreatePushVal(new Value(BaseType.NUMBER, token)));
    }
    else if (token.type == TokenType.STRING) {
      insts.add(Instruction.CreatePushVal(new Value(BaseType.STRING, token)));
    }
    else if (token.type == TokenType.IDENTIFIER) {
      insts.add(Instruction.CreatePushVal(new Value(BaseType.ID, token)));
    }
    else if (token.type == TokenType.KEYWORD) {
      switch (token.lexeme) {
        case "set":    insts.add(Instruction.CreateSet(token)); break;
        case "def":    insts.add(Instruction.CreateDefine(token)); break;
        case "dup":    insts.add(Instruction.CreateDup(token)); break;
        case "dupn":   insts.add(Instruction.CreateDupN(token)); break;
        case "pop":    insts.add(Instruction.CreatePop(token)); break;
        case "popn":   insts.add(Instruction.CreatePopN(token)); break;
        case "swap":   insts.add(Instruction.CreateSwap(token)); break;
        case "bubbn":  insts.add(Instruction.CreateBubbN(token)); break;
        case "sinkn":  insts.add(Instruction.CreateSinkN(token)); break;
        case "true":   insts.add(Instruction.CreatePushVal(new Value(BaseType.BOOL, token))); break;
        case "false":  insts.add(Instruction.CreatePushVal(new Value(BaseType.BOOL, token))); break;
        case "eval":   insts.add(Instruction.CreateEval(token)); break;
        case "evalif": insts.add(Instruction.CreateEvalIf(token)); break;
        case "typeof": insts.add(Instruction.CreateTypeOf(token)); break;
        case "nameof": insts.add(Instruction.CreateNameOf(token)); break;
        case "here":   insts.add(Instruction.CreateHere(token)); break;
        case "first":  insts.add(Instruction.CreateFirst(token)); break;
        case "rest":   insts.add(Instruction.CreateRest(token)); break;
        case "not":    insts.add(Instruction.CreateNot(token)); break;
        case "or":     insts.add(Instruction.CreateOr(token)); break;
        case "and":    insts.add(Instruction.CreateAnd(token)); break;
        case "add":    insts.add(Instruction.CreateAdd(token)); break;
        case "sub":    insts.add(Instruction.CreateSub(token)); break;
        case "mult":   insts.add(Instruction.CreateMult(token)); break;
        case "divmod": insts.add(Instruction.CreateDivMod(token)); break;
        case "print":  insts.add(Instruction.CreatePrint(token)); break;
        case "input":  insts.add(Instruction.CreateInput(token)); break;
        default: break;
      }
    }
    else if (token.type == TokenType.TYPE) {
      result = parseType(result, tokens, insts);
    }
    else if (token.type == TokenType.LSQUARE) {
      result = parseList(result, tokens, insts);
    }
    else if (token.type == TokenType.LPAREN) {
      result = parseExpr(result, tokens, insts);
    }
    return result;
  }

  private ArrayList<Instruction> parse(ArrayList<Token> tokens) {
    ArrayList<Instruction> insts = new ArrayList<>();

    for (int i = 0; i < tokens.size(); i++)
    {
      i = parseBasic(i, tokens, insts);
    }

    return insts;
  }
}
