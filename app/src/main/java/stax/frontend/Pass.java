package stax.frontend;

import java.util.ArrayList;

public interface Pass {
    public ArrayList<Instruction> run(ArrayList<Instruction> instructions);
}
