package logicrepository.plugins.ltl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.HashMap;

//class representing an Next node in an LTL formula
public class Next extends LTLFormula {

  Next(LTLFormula child){
    children = new ArrayList<LTLFormula>(1);
    children.add(child);
  }

  public LTLType getLTLType(){ 
     return LTLType.X;
  }

  protected LTLFormula normalize(boolean b) {
    if(b) {
      return new DualNext(
        new Negation(children.get(0)).normalize(false));
    }
    else{
      children.set(0,children.get(0).normalize(false));
      return this;
    }
  }

  public LTLFormula copy(){
    return new Next(children.get(0).copy());
  }

  public ATransition d(HashMap<LTLFormula, ATransition> D){
    LinkedHashSet<ATuple> retTuples 
   = new LinkedHashSet<ATuple>(1);
    LinkedHashSet<LTLFormula> empty  
   = new LinkedHashSet<LTLFormula>(0);
    LinkedHashSet<LinkedHashSet<LTLFormula>> nextSet
   = children.get(0).toSetForm();

    for(LinkedHashSet<LTLFormula> next : nextSet){
      retTuples.add(new ATuple(empty, sigma, next));
    }
    return new ATransition(retTuples);
  }

}
