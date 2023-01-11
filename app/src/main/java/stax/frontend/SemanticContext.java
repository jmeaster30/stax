package stax.frontend;

import java.util.*;

public class SemanticContext {
  
  public int stackSize;
  public HashMap<String, Type> variables;

  public SemanticContext()
  {
    variables = new HashMap<>();
  }

}
