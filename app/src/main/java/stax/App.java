package stax;

import java.io.FileNotFoundException;

import stax.runner.StaxRunner;

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
        
    if (opts.interpret) {
      StaxRunner runner = new StaxRunner();
      try {
        runner.CompileFile(opts.source);
        runner.Run(opts.debug);
      } catch (FileNotFoundException e) {
        System.err.println("ERROR :: File Not Found '" + opts.source + "'");
      }
    }
    
    if (opts.compile) {
      /*StaxGenerator gen = new StaxGenerator();
      try {
        gen.CompileFile(opts.source);
        gen.Generate();
      } catch (FileNotFoundException e) {
        System.err.println("ERROR :: File Not Found '" + opts.source + "'");
      }*/
    }
  }

  private static class Options {
    public boolean interpret  = false;
    public boolean compile    = false;
    public boolean help       = false;
    public boolean debug      = false;
    public String  source     = "";

    public Options(boolean interp, boolean comp, boolean deb, boolean helpme, String sourceFile) {
      interpret = interp;
      compile = comp;
      help = helpme;
      debug = deb;
      source = sourceFile;
    }
  }

  private static Options parseArgs(String[] args)
  {
    boolean interp = false;
    boolean comp = false;
    boolean help = false;
    boolean debug = false;
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
        case "debug":
          interp = true;
          debug = true;
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

    return new Options(interp, comp, debug, help, src);
  }
}
