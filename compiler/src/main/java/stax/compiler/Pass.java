package stax.compiler;

import java.util.ArrayList;

public interface Pass {
    public ArrayList<Instruction> run(ArrayList<Instruction> instructions);
}
