package logicrepository.plugins.ltl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.HashMap;

//class representing an Until node in an LTL formula
public class Until extends LTLFormula {

  Until(LTLFormula leftChild, LTLFormula rightChild){
    children = new ArrayList<LTLFormula>(2);
    children.add(leftChild);
    children.add(rightChild);
  }

  public LTLType getLTLType(){ 
     return LTLType.U;
  }

  protected LTLFormula normalize(boolean b) {
    if(b) {
      return new DualUntil(
        new Negation(children.get(0)).normalize(false),
        new Negation(children.get(1)).normalize(false));
    }
    else{
      children.set(0,children.get(0).normalize(false));
      children.set(1,children.get(1).normalize(false));
      return this;
    }
  }

  public LTLFormula copy(){
    return new Until(children.get(0).copy(), children.get(1).copy());
  }

  public ATransition d(HashMap<LTLFormula, ATransition> D){
    LinkedHashSet<ATuple> retTuples 
   = new LinkedHashSet<ATuple>();
    LinkedHashSet<LTLFormula> empty  
   = new LinkedHashSet<LTLFormula>(0);
    LinkedHashSet<LTLFormula> next  
   = new LinkedHashSet<LTLFormula>(1);
    LinkedHashSet<ATuple> nextTuples 
   = new LinkedHashSet<ATuple>(1);

    next.add(this);
    nextTuples.add(new ATuple(empty, sigma, next));

    retTuples.addAll(D.get(children.get(1)).tuples);
    retTuples.addAll(D.get(children.get(0)).and(new ATransition(nextTuples)).tuples);
    return new ATransition(retTuples);
  }
}
