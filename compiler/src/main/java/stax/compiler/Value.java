package stax.compiler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import stax.compiler.Type.BaseType;

public class Value {
  
  public Type type;
  public Token token;

  public boolean boolval;
  public String stringval;
  public BigDecimal numval;
  public Type value;
  public ArrayList<Value> values;
  public ArrayList<Instruction> instructions;

  public Value(BaseType t, Token to) {
    type = new Type(t);
    token = to;
  }

  public Value(Value orig) {
    type = new Type(orig.type);
    token = orig.token == null ? null : new Token(orig.token);
    boolval = orig.boolval;
    stringval = orig.stringval;
    numval = orig.numval == null ? null : new BigDecimal(orig.numval.toString());
    value = orig.value == null ? null : new Type(orig.value);
    values = orig.values == null ? null : new ArrayList<>(orig.values);
    instructions = orig.instructions == null ? null : new ArrayList<>(orig.instructions);
  }

  public String toString()
  {
    String s = "(" + type.toString();
    if (value != null) {
      s += " : " + value.toString();
    } else if (type.baseType == BaseType.BOOL) {
      s += " : " + (token == null ? boolval : token.lexeme);
    } else if (type.baseType == BaseType.ID) {
      s += " : " + token.lexeme;
    } else if (type.baseType == BaseType.NUMBER) { 
      s += " : " + (token == null ? numval.toString() : token.lexeme);
    } else if (values != null && !values.isEmpty()) {
      s += " - [";
      for (int i = 0; i < values.size(); i++) {
        s += " " + values.get(i) + (i < values.size() - 1 ? ", " : " ");
      }
      s += "]";
    } else if (instructions != null && !instructions.isEmpty()) {
      s += " {\n";
      for (int i = 0; i < instructions.size(); i++) {
        s += "\t" + instructions.get(i) + "\n";
      }
      s += "}\n";
    }
    s += ")";
    return s;
  }

  public static Value CreateBool(boolean val)
  {
    Value v = new Value(BaseType.BOOL, null);
    v.boolval = val;
    return v;
  }

  public static Value CreateString(String val)
  {
    Value v = new Value(BaseType.STRING, null);
    v.stringval = val;
    return v;
  }

  public static Value CreateNumber(BigDecimal val)
  {
    Value v = new Value(BaseType.NUMBER, null);
    v.numval = val;
    return v;
  }

  public static Value CreateType(Type val)
  {
    Value v = new Value(BaseType.TYPE, null);
    v.value = val;
    return v;
  }

  public static Value CreateList(ArrayList<Value> vals)
  {
    Value val = new Value(BaseType.LIST, null);
    val.values = vals;
    return val;
  }

  public static Value CreateExpr(ArrayList<Instruction> insts)
  {
    Value val = new Value(BaseType.EXPR, null);
    val.instructions = insts;
    return val;
  }

  public BigDecimal GetNumberValue(HashMap<String, Value> context) {
    if (type.baseType == BaseType.ID && context.containsKey(token.lexeme)) {
      return context.get(token.lexeme).GetNumberValue(context);
    }
    return token == null ? numval : new BigDecimal(token.lexeme);
  }

  public String GetStringValue(HashMap<String, Value> context, boolean wrapped) {
    if (type.baseType == BaseType.ID && context.containsKey(token.lexeme)) {
      return context.get(token.lexeme).GetStringValue(context, wrapped);
    }
    return (wrapped ? "'" : "") + (token == null ? stringval : token.lexeme) + (wrapped ? "'" : "");
  }

  public boolean GetBooleanValue(HashMap<String, Value> context) {
    if (type.baseType == BaseType.ID && context.containsKey(token.lexeme)) {
      return context.get(token.lexeme).GetBooleanValue(context);
    }
    return token == null ? boolval : token.lexeme.equals("true");
  }

  public ArrayList<Value> GetListValue(HashMap<String, Value> context) {
    if (type.baseType == BaseType.ID && context.containsKey(token.lexeme)) {
      return context.get(token.lexeme).GetListValue(context);
    }
    return values;
  }

  public ArrayList<Instruction> GetExpression(HashMap<String, Value> context) {
    if (type.baseType == BaseType.ID && context.containsKey(token.lexeme)) {
      return context.get(token.lexeme).GetExpression(context);
    }
    return instructions;
  }

  private String instructionsString() {
    String s = "( ";
    for (int i = 0; i < instructions.size(); i++) {
      s += instructions.get(i).GetPrintString(true);
      s += " ";
    }
    s += ")";
    return s;
  }

  private String listString() {
    String s = "[ ";
    for (int i = 0; i < values.size(); i++) {
      s += instructions.get(i).GetPrintString(true);
      s += i != values.size() - 1 ? ", " : " ";
    }
    s += "]";
    return s;
  }

  public String GetPrintString(HashMap<String, Value> context) {
    return GetPrintString(context, false);
  }

  public String GetPrintString(HashMap<String, Value> context, boolean stringWrapped) {
    switch (type.baseType) {
      case BOOL: return GetBooleanValue(context) ? "true" : "false";
      case NUMBER: return GetNumberValue(context).toString();
      case STRING: return GetStringValue(context, stringWrapped);
      case EXPR: return instructionsString();
      case LIST: return listString();
      case ID: return context.containsKey(token.lexeme) ? context.get(token.lexeme).GetPrintString(context, stringWrapped) : token.lexeme;
      default: return "";
    }
  }

  public Value GetResolvedValue(HashMap<String, Value> context) {
    if (type.baseType == BaseType.ID && context.containsKey(token.lexeme)) {
      return context.get(token.lexeme).GetResolvedValue(context);
    }
    return this;
  }
}
