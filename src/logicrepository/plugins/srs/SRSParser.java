/* Generated By:JavaCC: Do not edit this line. SRSParser.java */
  package logicrepository.plugins.srs;

  import java.util.ArrayList;
  import java.util.HashMap;
  import java.util.HashSet;
  import java.io.*;

  public class SRSParser implements SRSParserConstants {
    public static SRSParser parse(InputStream input) {
      SRSParser srsParser = new SRSParser(input);
      try{
        srsParser.Start();
      }
      catch(Exception e){
        System.err.println(e.getMessage());
        System.exit(1);
      }
      return srsParser;
    }

    public static SRSParser parse(String input) {
      SRSParser srsParser = new SRSParser(new StringReader(input));
      try{
        srsParser.Start();
      }
      catch(Exception e){
        System.err.println(e.getMessage());
        System.exit(1);
      }
      return srsParser;
    }

    public static void main(String[] args) throws ParseException, TokenMgrError {
      SRSParser parser = new SRSParser(System.in);
      parser.Start();
      SRS srs = parser.getSRS();
      System.out.println(srs.toString());
      HashSet<Symbol> extraTerminals = new HashSet<Symbol>();
      for(String s : args){
        extraTerminals.add(Terminal.get(s));
      }
      PatternMatchAutomaton p = new PatternMatchAutomaton(srs, extraTerminals);
      System.out.println(p);
     // if(args.length == 0){
        System.out.println(p.toImplString());
     // }
      SpliceList<Symbol> l = new SpliceList<Symbol>();
      for(String s : args){
        l.add(Terminal.get(s));
      }
      p.rewrite(l);
    }

    private SRS srs;
    private PatternMatchAutomaton pma = null;
    public SRS getSRS() { return srs; }
    public PatternMatchAutomaton getPMA(Symbol[] extraTerminals){
      if(pma != null) return pma;
      pma = new PatternMatchAutomaton(srs, extraTerminals);
      return pma;
    }

  final public void Start() throws ParseException {
  srs = new SRS();
    Srs();
    jj_consume_token(0);
  }

  final public void Srs() throws ParseException {
  Rule r;
    r = Rule();
    srs.add(r);
    jj_consume_token(PERIOD);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BEGIN:
      case TERMINAL:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      r = Rule();
    srs.add(r);
      jj_consume_token(PERIOD);
    }
  }

  final public Rule Rule() throws ParseException {
  Sequence lhs;
  AbstractSequence rhs;
    lhs = LhsSequence();
    jj_consume_token(ARROW);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TERMINAL:
      rhs = Sequence();
      break;
    case EPSILON:
      rhs = Epsilon();
      break;
    case FAIL:
    case SUCCEED:
      rhs = SucceedOrFail();
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
     {if (true) return new Rule(lhs,rhs);}
    throw new Error("Missing return statement in function");
  }

  final public Sequence LhsSequence() throws ParseException {
  Sequence ret = new Sequence();
  Sequence seq;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BEGIN:
      jj_consume_token(BEGIN);
         ret.add(Symbol.get("^"));
      break;
    default:
      jj_la1[2] = jj_gen;
      ;
    }
    seq = Sequence();
                      ret.addAll(seq);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case END:
      jj_consume_token(END);
         ret.add(Symbol.get("$"));
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
    {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public Sequence Sequence() throws ParseException {
  Sequence ret = new Sequence();
  Symbol s;
  Token t;
    t = jj_consume_token(TERMINAL);
    ret.add(Symbol.get(t.image));
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TERMINAL:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_2;
      }
      t = jj_consume_token(TERMINAL);
      ret.add(Symbol.get(t.image));
    }
    {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public AbstractSequence SucceedOrFail() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SUCCEED:
      jj_consume_token(SUCCEED);
     {if (true) return Succeed.get();}
      break;
    case FAIL:
      jj_consume_token(FAIL);
     {if (true) return Fail.get();}
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public Sequence Epsilon() throws ParseException {
    jj_consume_token(EPSILON);
    {if (true) return new Sequence();}
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public SRSParserTokenManager token_source;
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
      jj_la1_0 = new int[] {0x220,0x21c,0x20,0x40,0x200,0x18,};
   }

  /** Constructor with InputStream. */
  public SRSParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public SRSParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new SRSParserTokenManager(jj_input_stream);
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
  public SRSParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new SRSParserTokenManager(jj_input_stream);
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
  public SRSParser(SRSParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(SRSParserTokenManager tm) {
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
    boolean[] la1tokens = new boolean[10];
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
    for (int i = 0; i < 10; i++) {
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
