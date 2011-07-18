/* Generated By:JavaCC: Do not edit this line. CFGParser.java */
   package logicrepository.plugins.cfg.cfgutil;

   import java.util.ArrayList;
   import java.util.HashMap;
   import java.util.HashSet;
   import java.io.*;

   public class CFGParser implements CFGParserConstants {
      public static CFGParser parse(InputStream input) {
         CFGParser cfgParser = new CFGParser(input);

         try{
            cfgParser.Start();
         }
         catch(Exception e){
            System.err.println(e.getMessage());
            System.exit(1);
         }
         return cfgParser;
      }

      public static void main(String[] args)
      throws ParseException, TokenMgrError {
         CFGParser parser = new CFGParser(System.in);
         parser.Start();
         CFG g = parser.getCFG();
         g.simplify();
         System.out.println(g.toString());
      }

      private CFG cfg;
      private int namenum = 0;
      private HashMap<ArrayList<Symbol>,NonTerminal> starmap = new HashMap<ArrayList<Symbol>,NonTerminal>();

      private NonTerminal freshNT(){ return new NonTerminal(Integer.toString(namenum++)); }

      private NonTerminal addstar(ArrayList<Symbol> body){
         if (starmap.containsKey(body)) return starmap.get(body);
         ArrayList<Symbol> e = new ArrayList<Symbol>();
         NonTerminal nt = freshNT();
         starmap.put((ArrayList<Symbol>)body.clone(),nt);
         e.add(new Epsilon());
         body.add(nt);
         cfg.add(new Production(nt,body));
         cfg.add(new Production(nt,e));
         return nt;
      }

      public CFG getCFG(){ return cfg; }

      private void specializeSymbols(){
         HashSet<NonTerminal> nts = cfg.nonTerminals();
         ArrayList<Symbol> rhs;
         Symbol s;
         for (Production p : cfg.prods) {
            rhs = p.rhs;
            for (int j = 0; j < rhs.size(); j++) {
               s = rhs.get(j);
               if (s instanceof Epsilon){
                  rhs.set(j,new Epsilon());
               }
               else{
                  if (nts.contains(s))
                     rhs.set(j,new NonTerminal(s));
                  else {rhs.set(j,new Terminal(s)); }}
            }
         }
      }

  final public void Start() throws ParseException {
   cfg = new CFG();
    Cfg();
    jj_consume_token(0);
    specializeSymbols();
  }

  final public void Cfg() throws ParseException {
   ArrayList<Production> ps;
    ps = Production();
     cfg.add(ps); cfg.start = ps.get(0).lhs;
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      jj_consume_token(COMMA);
      ps = Production();
      cfg.add(ps);
    }
  }

  final public ArrayList<Production> Production() throws ParseException {
   Token t;
   ArrayList<Production> ps = new ArrayList<Production>();
   NonTerminal nt;
   ArrayList<Symbol> alt = new ArrayList<Symbol>();
    t = jj_consume_token(SYMBOL);
    jj_consume_token(ARROW);
     nt = new NonTerminal(t.image);
    alt = Alternative();
     ps.add(new Production(nt,alt));
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ALT:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
      jj_consume_token(ALT);
      alt = Alternative();
      ps.add(new Production(nt,alt));
    }
     {if (true) return ps;}
    throw new Error("Missing return statement in function");
  }

  final public ArrayList<Symbol> Alternative() throws ParseException {
   ArrayList<Symbol> e;
   ArrayList<Symbol> ss = new ArrayList<Symbol>();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EPSILON:
      jj_consume_token(EPSILON);
    ss.add(new Epsilon()); {if (true) return ss;}
      break;
    case SYMBOL:
    case LPAREN:
      e = Expression();
                       {if (true) return e;}
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public ArrayList<Symbol> Expression() throws ParseException {
   Token t;
   ArrayList<Symbol> s = new ArrayList<Symbol>();
   ArrayList<Symbol> e = new ArrayList<Symbol>();
   ArrayList<Symbol> r = new ArrayList<Symbol>();
   ArrayList<Symbol> body = new ArrayList<Symbol>();
   boolean isPlus;
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SYMBOL:
        t = jj_consume_token(SYMBOL);
    body.add(new Symbol(t.image));
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case PLUS:
        case STAR:
          isPlus = Mods();
        if (isPlus) r.addAll(body); r.add(addstar(body));
          break;
        default:
          jj_la1[3] = jj_gen;
          ;
        }
    if (r.isEmpty()) r.addAll(body); e.addAll(r); r.clear(); body.clear();
        break;
      case LPAREN:
        jj_consume_token(LPAREN);
        body = Expression();
        jj_consume_token(RPAREN);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case PLUS:
        case STAR:
          isPlus = Mods();
        if (isPlus) r.addAll(body); r.add(addstar(body));
          break;
        default:
          jj_la1[4] = jj_gen;
          ;
        }
    if (r.isEmpty()) r.addAll(body); e.addAll(r); r.clear(); body.clear();
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SYMBOL:
      case LPAREN:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_3;
      }
    }
    {if (true) return e;}
    throw new Error("Missing return statement in function");
  }

  final public boolean Mods() throws ParseException {
   boolean isPlus = true;
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        jj_consume_token(PLUS);
            isPlus = true && isPlus;
        break;
      case STAR:
        jj_consume_token(STAR);
              isPlus = false;
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case STAR:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_4;
      }
    }
    {if (true) return isPlus;}
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public CFGParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[9];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x10,0x4,0x828,0x600,0x600,0x820,0x820,0x600,0x600,};
   }

  /** Constructor with InputStream. */
  public CFGParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public CFGParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new CFGParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
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
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public CFGParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new CFGParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public CFGParser(CFGParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(CFGParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
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
    boolean[] la1tokens = new boolean[13];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 9; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 13; i++) {
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
