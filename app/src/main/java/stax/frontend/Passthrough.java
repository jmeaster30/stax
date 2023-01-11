package stax.frontend;

import java.util.ArrayList;

public class Passthrough implements Pass {

  public ArrayList<Instruction> run(ArrayList<Instruction> instructions) {
    return instructions;
  }

  public Passthrough() { /* */ }
  
}
