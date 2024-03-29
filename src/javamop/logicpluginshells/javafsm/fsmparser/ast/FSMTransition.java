package javamop.logicpluginshells.javafsm.fsmparser.ast;

import java.util.*;

public class FSMTransition extends Node {

	boolean defaultFlag = false;
	String eventName;
	String stateName;
	
	public FSMTransition (int line, int column, boolean defaultFlag, String eventName, String stateName){
		super(line, column);
		this.defaultFlag = defaultFlag;
		this.eventName = eventName;
		this.stateName = stateName;
	}
	
	public boolean isDefaultFlag() { return defaultFlag; }
	
	public String getEventName() { return eventName; }
	
	public String getStateName() { return stateName; }

	@Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
	
}
