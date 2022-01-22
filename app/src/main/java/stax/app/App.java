package stax.app;

import java.io.FileNotFoundException;

import stax.compiler.Compiler;

public class App {
  public static void main(String[] args) {
    Options opts = parseArgs(args);

    if (opts.help) {
      System.out.println("#=============================================#");
      System.out.println("# stax - JVM Stack Based Programming Language #");
      System.out.println("#=============================================#");
      System.out.println("  stax help             - this");
      System.out.println("  stax run <source>     - execute source file");
      System.out.println("  stax compile <source> - compile source file");
      return;
    }
        
    if (opts.interpret || opts.compile) {
      try {
        Compiler comp = new Compiler(opts.source);
        comp.compile();
      } catch (FileNotFoundException e) {
        System.err.println("ERROR :: File Not Found '" + opts.source + "'");
      }
    }
  }

  private static class Options {
    public boolean interpret  = false;
    public boolean compile    = false;
    public boolean help       = false;
    public String  source     = "";

    public Options(boolean interp, boolean comp, boolean helpme, String sourceFile) {
      interpret = interp;
      compile = comp;
      help = helpme;
      source = sourceFile;
    }
  }

  private static Options parseArgs(String[] args)
  {
    boolean interp = false;
    boolean comp = false;
    boolean help = false;
    String src = "";
    
    if (args.length == 0) {
      help = true;
    } else {
      switch (args[0]) {
        case "compile":
          comp = true;
          break;
        case "run":
          interp = true;
          break;
        case "help":
          help = true;
          break;
        default:
          help = true;
          break;
      }

      if ((interp || comp) && args.length > 1) {
        src = args[1];
      } else {
        comp = false;
        interp = false;
        help = true;
      }
    }

    return new Options(interp, comp, help, src);
  }
}
