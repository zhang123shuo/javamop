package javamop.parser.ast.mopspec;

import javamop.parser.ast.*;
import javamop.parser.ast.visitor.GenericVisitor;
import javamop.parser.ast.visitor.VoidVisitor;

public abstract class Property extends Node {
	
	String type;
	
	public Property (int line, int column, String type){
		super(line, column);
		this.type = type;
	}
	
	public String getType() { return type; }
	
    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
	
}
