package stax.compiler;

import java.util.ArrayList;

import stax.compiler.Type.BaseType;

public class Value {
  
  public Type type;
  public Token token;

  public Type value;
  public ArrayList<Value> values;
  public ArrayList<Instruction> instructions;

  Value(BaseType t, Token to) {
    type = new Type(t);
    token = to;
  }

  public String toString()
  {
    String s = "< " + type.toString();
    if (value != null) {
      s += " - " + value.toString();
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
    s += " >";
    return s;
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
}
