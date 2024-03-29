/* Generated By:JavaCC: Do not edit this line. LTLParser.java */
package logicrepository.plugins.ltl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.io.*;

public class LTLParser implements LTLParserConstants {
  public static LTLParser parse(String input) {
     Reader reader = new StringReader(input);
     LTLParser ltlParser = new LTLParser(reader);

     try{
      ltlParser.Start();
     }
     catch(Exception e){
      System.err.println(e.getMessage());
      System.exit(1);
     }
     return ltlParser;
  }

  private LTLFormula formula;

  public LTLFormula getFormula(){
    return formula;
  }

  final public void Start() throws ParseException {
    formula = Formula();
    jj_consume_token(0);
  }

  final public LTLFormula Formula() throws ParseException {
  LTLFormula subFormula1 = null;
  LTLFormula subFormula2 = null;
    //why did I bother to make actual
      //node types for things that will just be lowered into
      //other node types.  The parser should do the lowering
      subFormula1 = OrFormula();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case U:
    case DU:
    case R:
    case S:
    case DS:
    case 25:
    case 26:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case U:
        jj_consume_token(U);
        subFormula2 = OrFormula();
     {if (true) return new Until(subFormula1,subFormula2);}
        break;
      case DU:
        jj_consume_token(DU);
        subFormula2 = OrFormula();
     {if (true) return new DualUntil(subFormula1,subFormula2);}
        break;
      case R:
        jj_consume_token(R);
        subFormula2 = OrFormula();
     {if (true) return new DualUntil(subFormula1,subFormula2);}
        break;
      case S:
        jj_consume_token(S);
        subFormula2 = OrFormula();
     {if (true) return new Since(subFormula1,subFormula2);}
        break;
      case DS:
        jj_consume_token(DS);
        subFormula2 = OrFormula();
     {if (true) return new DualSince(subFormula1,subFormula2);}
        break;
      case 25:
        jj_consume_token(25);
        subFormula2 = OrFormula();
     {if (true) return new Implication(subFormula1,subFormula2);}
        break;
      case 26:
        jj_consume_token(26);
        subFormula2 = OrFormula();
     {if (true) return new IFF(subFormula1,subFormula2);}
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[1] = jj_gen;
      ;
    }
  {if (true) return subFormula1;}
    throw new Error("Missing return statement in function");
  }

  final public LTLFormula OrFormula() throws ParseException {
  LTLFormula subFormula1;
  LTLFormula subFormula2;
  ArrayList<LTLFormula> children;
     children = new ArrayList<LTLFormula>();
    subFormula1 = XOrFormula();
                               children.add(subFormula1);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_1;
      }
      jj_consume_token(OR);
      subFormula2 = XOrFormula();
        children.add(subFormula2);
    }
     if(children.size() == 1) {if (true) return subFormula1;}
     {if (true) return new Or(children);}
    throw new Error("Missing return statement in function");
  }

  final public LTLFormula XOrFormula() throws ParseException {
  LTLFormula subFormula1;
  LTLFormula subFormula2;
  ArrayList<LTLFormula> children;
     children = new ArrayList<LTLFormula>();
    subFormula1 = AndFormula();
                                children.add(subFormula1);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case XOR:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_2;
      }
      jj_consume_token(XOR);
      subFormula2 = AndFormula();
        children.add(subFormula2);
    }
     if(children.size() == 1) {if (true) return subFormula1;}
     {if (true) return new XOr(children);}
    throw new Error("Missing return statement in function");
  }

  final public LTLFormula AndFormula() throws ParseException {
  LTLFormula subFormula1;
  LTLFormula subFormula2;
  ArrayList<LTLFormula> children;
     children = new ArrayList<LTLFormula>();
    subFormula1 = UnaryFormula();
                                  children.add(subFormula1);
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_3;
      }
      jj_consume_token(AND);
      subFormula2 = UnaryFormula();
        children.add(subFormula2);
    }
     if(children.size() == 1) {if (true) return subFormula1;}
     {if (true) return new And(children);}
    throw new Error("Missing return statement in function");
  }

  final public LTLFormula UnaryFormula() throws ParseException {
  LTLFormula subFormula;
  Token name;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAREN:
      jj_consume_token(LPAREN);
      subFormula = Formula();
      jj_consume_token(RPAREN);
                                  {if (true) return subFormula;}
      break;
    case NEG:
      jj_consume_token(NEG);
      subFormula = UnaryFormula();
                                       {if (true) return new Negation(subFormula);}
      break;
    case X:
      jj_consume_token(X);
      subFormula = UnaryFormula();
                                       {if (true) return new Next(subFormula);}
      break;
    case DX:
      jj_consume_token(DX);
      subFormula = UnaryFormula();
                                       {if (true) return new DualNext(subFormula);}
      break;
    case Y:
      jj_consume_token(Y);
      subFormula = UnaryFormula();
                                       {if (true) return new Previously(subFormula);}
      break;
    case DY:
      jj_consume_token(DY);
      subFormula = UnaryFormula();
                                       {if (true) return new DualPreviously(subFormula);}
      break;
    case ALWAYS:
      jj_consume_token(ALWAYS);
      subFormula = UnaryFormula();
                                       {if (true) return new DualUntil(False.get(), subFormula);}
      break;
    case EVENTUALLY:
      jj_consume_token(EVENTUALLY);
      subFormula = UnaryFormula();
                                       {if (true) return new Until(True.get(), subFormula);}
      break;
    case EVENTUALLY_PAST:
      jj_consume_token(EVENTUALLY_PAST);
      subFormula = UnaryFormula();
                                       {if (true) return new Since(True.get(), subFormula);}
      break;
    case TRUE:
      jj_consume_token(TRUE);
            {if (true) return True.get();}
      break;
    case FALSE:
      jj_consume_token(FALSE);
             {if (true) return False.get();}
      break;
    case ATOM:
      name = jj_consume_token(ATOM);
                   {if (true) return Atom.get(name.image);}
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public LTLParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[6];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x6001f00,0x6001f00,0x40,0x80,0x20,0x7fe014,};
   }

  /** Constructor with InputStream. */
  public LTLParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public LTLParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new LTLParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public LTLParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new LTLParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public LTLParser(LTLParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(LTLParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[27];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 6; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 27; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
