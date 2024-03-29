package javamoprt;

import java.io.PrintStream;

public class MOPLoggingAll extends MOPLogging {
  PrintStream p;
  
  MOPLoggingAll(PrintStream ps) {
    p = ps;
  }

  public void println(Level l) { p.println(); }
  public void print(Level l, boolean x){ p.println(x); }
  public void println(Level l, boolean x){ p.println(x); }
  public void print(Level l, char x){ p.println(x); }
  public void println(Level l, char x){ p.println(x); }
  public void print(Level l, char[] x){ p.println(x); }
  public void println(Level l, char[] x){ p.println(x); }
  public void print(Level l, double x){ p.println(x); }
  public void println(Level l, double x){ p.println(x); }
  public void print(Level l, float x){ p.println(x); }
  public void println(Level l, float x){ p.println(x); }
  public void print(Level l, int x){ p.println(x); }
  public void println(Level l, int x){ p.println(x); }
  public void print(Level l, long x){ p.println(x); }
  public void println(Level l, long x){ p.println(x); }
  public void print(Level l, Object x){ p.println(x); }
  public void println(Level l, Object x){ p.println(x); }
  public void print(Level l, String x){ p.println(x); }
  public void println(Level l, String x){ p.println(x); }
}

