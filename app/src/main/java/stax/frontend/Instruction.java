package stax.frontend;

import stax.frontend.Type.BaseType;

public class Instruction {
  
  public enum InstructionType
  {
    DEFINE, DUP, DUPN, POP, POPN, SWAP, BUBBN, SINKN,
    EVAL, EVALIF, TYPEOF, FIRST, REST, LEN, CONCAT,
    NOT, OR, AND, ADD, SUB, MULT, DIV, DIVMOD,
    EQ, NEQ, LT, GT, LTE, GTE,
    PRINT, PRINTLN, INPUT, SET,

    PUSHVAL, PUSHLIST, PUSHEXPR
  }

  public Type type;
  public InstructionType instructionType;
  public Token token;
  public Value value;

  public Instruction(Instruction orig)
  {
    type = new Type(orig.type);
    instructionType = orig.instructionType;
    token = new Token(orig.token);
    value = new Value(orig.value);
  }
  
  public Instruction(Type t, InstructionType it, Token to)
  {
    type = t;
    instructionType = it;
    token = to;
  }

  public Instruction(Type t, InstructionType it, Value val)
  {
    type = t;
    instructionType = it;
    value = val;
  }

  public String toString()
  {
    return instructionType.toString() + " - " + type.toString() + (value != null ? " >> " + value.toString() : "");
  }

  public String GetPrintString(boolean stringWrapped)
  {
    return instructionType.toString();
  }

  public static Instruction CreateSet(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ID));
    defineType.inputs.add(new Type(BaseType.ANY));
    return new Instruction(defineType, InstructionType.SET, token);
  }

  public static Instruction CreateDefine(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ID));
    defineType.inputs.add(new Type(BaseType.LIST));
    defineType.inputs.add(new Type(BaseType.LIST));
    defineType.inputs.add(new Type(BaseType.EXPR, BaseType.ANYNUM, BaseType.ANYNUM));
    return new Instruction(defineType, InstructionType.DEFINE, token);
  }

  public static Instruction CreateDup(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY, "T"));
    defineType.outputs.add(new Type(BaseType.ANY, "T"));
    defineType.outputs.add(new Type(BaseType.ANY, "T"));
    return new Instruction(defineType, InstructionType.DUP, token);
  }

  public static Instruction CreateDupN(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANYNUM));
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.outputs.add(new Type(BaseType.ANYNUM));
    return new Instruction(defineType, InstructionType.DUPN, token);
    // when type checking we need to override these "***n" methods to show how they change the stack
  }

  public static Instruction CreatePop(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY));
    return new Instruction(defineType, InstructionType.POP, token);
  }

  public static Instruction CreatePopN(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANYNUM));
    defineType.inputs.add(new Type(BaseType.NUMBER));
    return new Instruction(defineType, InstructionType.POPN, token);
  }

  public static Instruction CreateSwap(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY, "T"));
    defineType.inputs.add(new Type(BaseType.ANY, "U"));
    defineType.outputs.add(new Type(BaseType.ANY, "U"));
    defineType.outputs.add(new Type(BaseType.ANY, "T"));
    return new Instruction(defineType, InstructionType.SWAP, token);
  }

  public static Instruction CreateBubbN(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY, "T"));
    defineType.inputs.add(new Type(BaseType.ANYNUM, "U"));
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.outputs.add(new Type(BaseType.ANYNUM, "U"));
    defineType.outputs.add(new Type(BaseType.ANY, "T"));
    return new Instruction(defineType, InstructionType.BUBBN, token);
  }

  public static Instruction CreateSinkN(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANYNUM, "T"));
    defineType.inputs.add(new Type(BaseType.ANY, "U"));
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.outputs.add(new Type(BaseType.ANY, "U"));
    defineType.outputs.add(new Type(BaseType.ANYNUM, "T"));
    return new Instruction(defineType, InstructionType.SINKN, token);
  }

  public static Instruction CreateEval(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANYNUM));
    defineType.inputs.add(new Type(BaseType.EXPR, BaseType.ANYNUM, BaseType.ANYNUM));
    defineType.outputs.add(new Type(BaseType.ANYNUM));
    return new Instruction(defineType, InstructionType.EVAL, token);
  }

  public static Instruction CreateEvalIf(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANYNUM));
    defineType.inputs.add(new Type(BaseType.BOOL));
    defineType.inputs.add(new Type(BaseType.EXPR, BaseType.ANYNUM, BaseType.ANYNUM));
    defineType.outputs.add(new Type(BaseType.ANYNUM));
    defineType.outputs.add(new Type(BaseType.BOOL));
    return new Instruction(defineType, InstructionType.EVALIF, token);
  }

  public static Instruction CreateTypeOf(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.outputs.add(new Type(BaseType.STRING));
    return new Instruction(defineType, InstructionType.TYPEOF, token);
  }

  public static Instruction CreateFirst(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.LIST));
    defineType.outputs.add(new Type(BaseType.ANY));
    return new Instruction(defineType, InstructionType.FIRST, token);
  }

  public static Instruction CreateRest(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.LIST));
    defineType.outputs.add(new Type(BaseType.LIST));
    return new Instruction(defineType, InstructionType.REST, token);
  }

  public static Instruction CreateNot(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.BOOL));
    defineType.outputs.add(new Type(BaseType.BOOL));
    return new Instruction(defineType, InstructionType.NOT, token);
  }

  public static Instruction CreateOr(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.BOOL));
    defineType.inputs.add(new Type(BaseType.BOOL));
    defineType.outputs.add(new Type(BaseType.BOOL));
    return new Instruction(defineType, InstructionType.OR, token);
  }

  public static Instruction CreateAnd(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.BOOL));
    defineType.inputs.add(new Type(BaseType.BOOL));
    defineType.outputs.add(new Type(BaseType.BOOL));
    return new Instruction(defineType, InstructionType.AND, token);
  }

  public static Instruction CreateAdd(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.outputs.add(new Type(BaseType.NUMBER));
    return new Instruction(defineType, InstructionType.ADD, token);
  }

  public static Instruction CreateSub(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.outputs.add(new Type(BaseType.NUMBER));
    return new Instruction(defineType, InstructionType.SUB, token);
  }

  public static Instruction CreateMult(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.outputs.add(new Type(BaseType.NUMBER));
    return new Instruction(defineType, InstructionType.MULT, token);
  }

  public static Instruction CreateDiv(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.outputs.add(new Type(BaseType.NUMBER));
    return new Instruction(defineType, InstructionType.DIV, token);
  }

  public static Instruction CreateDivMod(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.inputs.add(new Type(BaseType.NUMBER));
    defineType.outputs.add(new Type(BaseType.NUMBER));
    defineType.outputs.add(new Type(BaseType.NUMBER));
    return new Instruction(defineType, InstructionType.DIVMOD, token);
  }

  public static Instruction CreateEq(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.outputs.add(new Type(BaseType.BOOL));
    return new Instruction(defineType, InstructionType.EQ, token);
  }

  public static Instruction CreateNeq(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.outputs.add(new Type(BaseType.BOOL));
    return new Instruction(defineType, InstructionType.NEQ, token);
  }

  public static Instruction CreateLt(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.outputs.add(new Type(BaseType.BOOL));
    return new Instruction(defineType, InstructionType.LT, token);
  }

  public static Instruction CreateGt(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.outputs.add(new Type(BaseType.BOOL));
    return new Instruction(defineType, InstructionType.GT, token);
  }

  public static Instruction CreateLte(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.outputs.add(new Type(BaseType.BOOL));
    return new Instruction(defineType, InstructionType.LTE, token);
  }

  public static Instruction CreateGte(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.inputs.add(new Type(BaseType.ANY));
    defineType.outputs.add(new Type(BaseType.BOOL));
    return new Instruction(defineType, InstructionType.GTE, token);
  }

  public static Instruction CreatePrint(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY));
    return new Instruction(defineType, InstructionType.PRINT, token);
  }

  public static Instruction CreatePrintln(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.ANY));
    return new Instruction(defineType, InstructionType.PRINTLN, token);
  }

  public static Instruction CreateInput(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.outputs.add(new Type(BaseType.STRING));
    return new Instruction(defineType, InstructionType.INPUT, token);
  }

  public static Instruction CreatePushVal(Value val) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.outputs.add(new Type(BaseType.ANY));
    return new Instruction(defineType, InstructionType.PUSHVAL, val);
  }

  public static Instruction CreatePushList(int amount) {
    Type defineType = new Type(BaseType.EXPR);
    for (int i = 0; i < amount; i++) {
      defineType.inputs.add(new Type(BaseType.ANY));
    }
    defineType.outputs.add(new Type(BaseType.LIST));
    return new Instruction(defineType, InstructionType.PUSHLIST, (Token)null);
  }

	public static Instruction CreateLen(Token token) {
		Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.LIST));
    defineType.outputs.add(new Type(BaseType.NUMBER));
    return new Instruction(defineType, InstructionType.LEN, token);
	}

  public static Instruction CreateConcat(Token token) {
    Type defineType = new Type(BaseType.EXPR);
    defineType.inputs.add(new Type(BaseType.STRING));
    defineType.inputs.add(new Type(BaseType.STRING));
    defineType.outputs.add(new Type(BaseType.STRING));
    return new Instruction(defineType, InstructionType.CONCAT, token);
  }
}
