/* Generated By:JavaCC: Do not edit this line. PTCARET_ParserConstants.java */
package logicrepository.plugins.ptcaret.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface PTCARET_ParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int OR = 5;
  /** RegularExpression Id. */
  int AND = 6;
  /** RegularExpression Id. */
  int XOR = 7;
  /** RegularExpression Id. */
  int NOT = 8;
  /** RegularExpression Id. */
  int IMPLIES = 9;
  /** RegularExpression Id. */
  int IFF = 10;
  /** RegularExpression Id. */
  int ALWAYS = 11;
  /** RegularExpression Id. */
  int EVENTUALLY = 12;
  /** RegularExpression Id. */
  int PREVIOUSLY = 13;
  /** RegularExpression Id. */
  int SINCE = 14;
  /** RegularExpression Id. */
  int AB_ALWAYS = 15;
  /** RegularExpression Id. */
  int AB_EVENTUALLY = 16;
  /** RegularExpression Id. */
  int AB_PREVIOUSLY = 17;
  /** RegularExpression Id. */
  int AB_SINCE = 18;
  /** RegularExpression Id. */
  int ATBEGIN = 19;
  /** RegularExpression Id. */
  int ATCALL = 20;
  /** RegularExpression Id. */
  int ALWAYS_STACK_ATBEGIN = 21;
  /** RegularExpression Id. */
  int EVENTUALLY_STACK_ATBEGIN = 22;
  /** RegularExpression Id. */
  int SINCE_STACK_ATBEGIN = 23;
  /** RegularExpression Id. */
  int ALWAYS_STACK_ATCALL = 24;
  /** RegularExpression Id. */
  int EVENTUALLY_STACK_ATCALL = 25;
  /** RegularExpression Id. */
  int SINCE_STACK_ATCALL = 26;
  /** RegularExpression Id. */
  int ALWAYS_STACK = 27;
  /** RegularExpression Id. */
  int EVENTUALLY_STACK = 28;
  /** RegularExpression Id. */
  int SINCE_STACK = 29;
  /** RegularExpression Id. */
  int CONSTANT = 30;
  /** RegularExpression Id. */
  int TRUE = 31;
  /** RegularExpression Id. */
  int FALSE = 32;
  /** RegularExpression Id. */
  int ID = 33;
  /** RegularExpression Id. */
  int DIGIT = 34;
  /** RegularExpression Id. */
  int LETTER = 35;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\r\"",
    "\"\\t\"",
    "\"\\n\"",
    "<OR>",
    "<AND>",
    "<XOR>",
    "<NOT>",
    "<IMPLIES>",
    "\"<->\"",
    "\"[*]\"",
    "\"<*>\"",
    "\"(*)\"",
    "\"S\"",
    "\"[*a]\"",
    "\"<*a>\"",
    "\"(*a)\"",
    "\"Sa\"",
    "\"@b\"",
    "\"@c\"",
    "\"[*s@b]\"",
    "\"<*s@b>\"",
    "\"Ss@b\"",
    "\"[*s@c]\"",
    "\"<*s@c>\"",
    "\"Ss@c\"",
    "\"[*s@bc]\"",
    "\"<*s@bc>\"",
    "\"Ss@bc\"",
    "<CONSTANT>",
    "\"true\"",
    "\"false\"",
    "<ID>",
    "<DIGIT>",
    "<LETTER>",
    "\"(\"",
    "\")\"",
  };

}
