package stax.compiler;

import java.util.ArrayList;

public class Type {
  
  public enum BaseType {
    NUMBER, STRING, LIST, BOOL, CHAR, ID, EXPR, TYPE,
    UNKNOWN, //Single type to be type resolved
    UNKNOWNNUM, //many types to be type resolved
    ANY,     //any single type
    ANYNUM   //any number of types
  }

  public BaseType baseType;
  public String name; // this is useful for named generic variables
  public ArrayList<Type> inputs;
  public ArrayList<Type> outputs;

  public Type() {
    inputs = new ArrayList<>();
    outputs = new ArrayList<>();
  }

  public Type(BaseType t) {
    baseType = t;
    inputs = new ArrayList<>();
    outputs = new ArrayList<>();
  }

  public Type(BaseType t, BaseType inputType)
  {
    baseType = t;
    inputs = new ArrayList<>();
    inputs.add(new Type(inputType));
    outputs = new ArrayList<>();
  }

  public Type(BaseType t, BaseType inputType, BaseType outputType)
  {
    baseType = t;
    inputs = new ArrayList<>();
    inputs.add(new Type(inputType));
    outputs = new ArrayList<>();
    outputs.add(new Type(outputType));
  }

  public Type(BaseType t, String n) {
    baseType = t;
    name = n;
    inputs = new ArrayList<>();
    outputs = new ArrayList<>();
  }

  public Type(BaseType t, String n, BaseType inputType)
  {
    baseType = t;
    name = n;
    inputs = new ArrayList<>();
    inputs.add(new Type(inputType));
    outputs = new ArrayList<>();
  }

  public Type(BaseType t, String n, BaseType inputType, BaseType outputType)
  {
    baseType = t;
    name = n;
    inputs = new ArrayList<>();
    inputs.add(new Type(inputType));
    outputs = new ArrayList<>();
    outputs.add(new Type(outputType));
  }

  public String toString()
  {
    String s = baseType.toString();
    if (!inputs.isEmpty() || !outputs.isEmpty()) {
      s += "(";
      for (int i = 0; i < inputs.size(); i++)
      {
        s += " " + inputs.get(i).toString() + (i != inputs.size() - 1 ? " ," : " ");
      }
      s += ":";
      for (int i = 0; i < outputs.size(); i++)
      {
        s += " " + outputs.get(i).toString() + (i != outputs.size() - 1 ? " ," : " ");
      }
      s += ")";
    }
    return s;
  }

  public static Type FromToken(Token token) {
    switch (token.lexeme) {
      case "num": return new Type(BaseType.NUMBER);
      case "str": return new Type(BaseType.STRING);
      case "list": return new Type(BaseType.LIST);
      case "bool": return new Type(BaseType.BOOL);
      case "char": return new Type(BaseType.CHAR);
      case "id": return new Type(BaseType.ID);
      case "expr": return new Type(BaseType.EXPR);
      default: return null;
    }
  }
}
