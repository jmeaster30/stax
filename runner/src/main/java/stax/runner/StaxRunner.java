package stax.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stax.compiler.Compiler;
import stax.compiler.Instruction;
import stax.compiler.Value;

public class StaxRunner {

  private int depth;
  private boolean messages;
  private Stack<Value> memoryStack;
  private HashMap<String, Value> context;
  private ArrayList<Instruction> instructions;

  public StaxRunner() {
    depth = 0;
    instructions = new ArrayList<>();
    memoryStack = new Stack<>();
    context = new HashMap<>();
  }

  public StaxRunner(boolean messages) {
    this.messages = messages;
    depth = 0;
    instructions = new ArrayList<>();
    memoryStack = new Stack<>();
    context = new HashMap<>();
  }

  public StaxRunner(ArrayList<Instruction> insts, Stack<Value> mem, HashMap<String, Value> ctxt, int dep)
  {
    instructions = insts;
    context = ctxt;
    memoryStack = mem;
    depth = dep;
  }

  public void CompileFile(String sourcePath) throws FileNotFoundException
  {
    FileReader fr = new FileReader(new File(sourcePath));
    Compiler comp = new Compiler(fr);
    comp.compile();
    instructions = comp.instructions;
    memoryStack = new Stack<>();
    context = new HashMap<>();
  }

  public void CompileSource(String source)
  {
    Compiler comp = new Compiler(source);
    comp.compile();
    instructions = comp.instructions;
    memoryStack = new Stack<>();
    context = new HashMap<>();
  }

  public Stack<Value> Run(boolean interactiveDebug)
  {
    Scanner scanner = new Scanner(System.in);

    for (int i = 0; i < instructions.size(); i++)
    {
      Instruction inst = instructions.get(i);

      switch (inst.instructionType) {
        case PUSHVAL:
          memoryStack.push(inst.value);
          break;
        case PUSHLIST: {
          int amount = inst.type.inputs.size();
          ArrayList<Value> vals = new ArrayList<>();
          for (int j = 0; j < amount; j++) vals.add(0, memoryStack.pop());
          memoryStack.push(Value.CreateList(vals));
          break;
        }
        case PUSHEXPR:
          memoryStack.push(inst.value);
          break;
        case DEFINE:
          break;
        case SET: {
          Value val = memoryStack.pop().GetResolvedValue(context);
          Value id = memoryStack.pop();
          context.put(id.token.lexeme, new Value(val));
          break;
        }
        case DUP:
          Value top = memoryStack.pop();
          memoryStack.push(top);
          memoryStack.push(top);
          break;
        case DUPN: {
          ArrayList<Value> vals = new ArrayList<>();
          BigDecimal amount = memoryStack.pop().GetNumberValue(context);
          for (int j = 0; BigDecimal.valueOf(j).compareTo(amount) < 0; j++) vals.add(0, memoryStack.pop());
          for (int j = 0; BigDecimal.valueOf(j).compareTo(amount) < 0; j++) memoryStack.push(vals.get(j));
          for (int j = 0; BigDecimal.valueOf(j).compareTo(amount) < 0; j++) memoryStack.push(vals.get(j));
          break;
        }
        case POP:
          memoryStack.pop();
          break;
        case POPN: {
          BigDecimal amount = memoryStack.pop().GetNumberValue(context);
          for (int j = 0; BigDecimal.valueOf(j).compareTo(amount) < 0; j++) memoryStack.pop();
          break;
        }
        case SWAP: {
          Value topval = memoryStack.pop();
          Value next = memoryStack.pop();
          memoryStack.push(topval);
          memoryStack.push(next);
          break;
        }
        case BUBBN: {
          ArrayList<Value> vals = new ArrayList<>();
          BigDecimal amount = memoryStack.pop().GetNumberValue(context);
          for (int j = 0; BigDecimal.valueOf(j).compareTo(amount) < 0; j++) vals.add(0, memoryStack.pop());
          Value topval = memoryStack.pop();
          for (int j = 0; BigDecimal.valueOf(j).compareTo(amount) < 0; j++) memoryStack.push(vals.get(j));
          memoryStack.push(topval);
          break;
        }
        case SINKN: {
          ArrayList<Value> vals = new ArrayList<>();
          BigDecimal amount = memoryStack.pop().GetNumberValue(context);
          Value topval = memoryStack.pop();
          for (int j = 0; BigDecimal.valueOf(j).compareTo(amount) < 0; j++) vals.add(0, memoryStack.pop());
          memoryStack.push(topval);
          for (int j = 0; BigDecimal.valueOf(j).compareTo(amount) < 0; j++) memoryStack.push(vals.get(j));
          break;
        }
        case EVAL: {
          ArrayList<Instruction> expression = memoryStack.pop().GetExpression(context);
          StaxRunner runner = new StaxRunner(expression, memoryStack, context, depth + 1);
          memoryStack = runner.Run(interactiveDebug);
          break;
        }
        case EVALIF: {
          ArrayList<Instruction> expression = memoryStack.pop().GetExpression(context);
          boolean boolval = memoryStack.pop().GetBooleanValue(context);
          if (boolval) {
            StaxRunner runner = new StaxRunner(expression, memoryStack, context, depth + 1);
            memoryStack = runner.Run(interactiveDebug);
          }
          memoryStack.push(Value.CreateBool(boolval));
          break;
        }
        case TYPEOF: {
          Value val = memoryStack.pop();
          memoryStack.push(Value.CreateString(val.type.toString()));
          break;
        }
        case NAMEOF:
          System.err.println("ERROR :: Nameof unimplemented :(");
          break;
        case HERE:
          System.err.println("ERROR :: Here unimplemented :(");
          break;
        case FIRST: {
          ArrayList<Value> list = memoryStack.pop().GetListValue(context);
          if (list.isEmpty()) {
            System.err.println("ERROR :: first on empty lists unimplemented :(");
          }
          memoryStack.push(list.get(0));
          break;
        }
        case REST: {
          ArrayList<Value> list = memoryStack.pop().GetListValue(context);
          if (!list.isEmpty()) {
            list.remove(0);
          }
          memoryStack.push(Value.CreateList(list));
          break;
        }
        case NOT: {
          boolean val = memoryStack.pop().GetBooleanValue(context);
          memoryStack.push(Value.CreateBool(!val));
          break;
        }
        case AND: {
          boolean b = memoryStack.pop().GetBooleanValue(context);
          boolean a = memoryStack.pop().GetBooleanValue(context);
          memoryStack.push(Value.CreateBool(a && b));
          break;
        }
        case OR: {
          boolean b = memoryStack.pop().GetBooleanValue(context);
          boolean a = memoryStack.pop().GetBooleanValue(context);
          memoryStack.push(Value.CreateBool(a || b));
          break;
        }
        case ADD: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          memoryStack.push(Value.CreateNumber(a.add(b)));
          break;
        }
        case SUB: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          memoryStack.push(Value.CreateNumber(a.subtract(b)));
          break;
        }
        case MULT: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          memoryStack.push(Value.CreateNumber(a.multiply(b)));
          break;
        }
        case DIV: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          memoryStack.push(Value.CreateNumber(a.divide(b)));
          break;
        }
        case EQ: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          memoryStack.push(Value.CreateBool(a.compareTo(b) == 0));
          break;
        }
        case NEQ: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          memoryStack.push(Value.CreateBool(a.compareTo(b) != 0));
          break;
        }
        case LT: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          memoryStack.push(Value.CreateBool(a.compareTo(b) < 0));
          break;
        }
        case GT: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          memoryStack.push(Value.CreateBool(a.compareTo(b) > 0));
          break;
        }
        case LTE: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          memoryStack.push(Value.CreateBool(a.compareTo(b) <= 0));
          break;
        }
        case GTE: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          memoryStack.push(Value.CreateBool(a.compareTo(b) >= 0));
          break;
        }
        case DIVMOD: {
          BigDecimal b = memoryStack.pop().GetNumberValue(context);
          BigDecimal a = memoryStack.pop().GetNumberValue(context);
          BigDecimal[] results = a.divideAndRemainder(b);
          memoryStack.push(Value.CreateNumber(results[1]));
          memoryStack.push(Value.CreateNumber(results[0]));
          break;
        }
        case PRINT: {
          System.out.print(memoryStack.pop().GetPrintString(context));
          break;
        }
        case PRINTLN: {
          System.out.println(memoryStack.pop().GetPrintString(context));
          break;
        }
        case INPUT: {
          System.err.println("ERROR :: Input unimplemented :(");
          break;
        }
        default:
          System.err.println("ERROR :: Unknown instruction " + inst.instructionType.toString() + " :(");
      }

      if (interactiveDebug) {
        System.out.println("[" + depth + "] :: " + inst.GetPrintString(false));
        System.out.print("\t");
        for (int x = memoryStack.size() - 1; x >= 0 && x >= memoryStack.size() - 11; x--) {
          System.out.print(memoryStack.get(x).toString() + " | ");
        }
        System.out.println("");
        for (Map.Entry<String, Value> variable : context.entrySet()) {
          System.out.println("\t" + variable.getKey() + " = " + variable.getValue().GetPrintString(context));
        }
        scanner.nextLine();
      }

    }

    if (depth == 0 && messages) {
      System.out.println("LEFTOVER STACK ....");
      for (int i = 0; i < memoryStack.size(); i++)
      {
        Value val = memoryStack.get(i);
        System.out.println(val.toString());
      }
    }

    scanner.close();

    return memoryStack;
  }
}
