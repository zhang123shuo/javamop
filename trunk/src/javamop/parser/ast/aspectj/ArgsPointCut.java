package javamop.parser.ast.aspectj;

import java.util.*;
import javamop.parser.ast.visitor.GenericVisitor;
import javamop.parser.ast.visitor.VoidVisitor;

public class ArgsPointCut extends PointCut {
	
	List<TypePattern> args;
	public ArgsPointCut(int line, int column, String type, List<TypePattern> args){
		super(line, column, type);
		this.args = args;
	}
	
	public List<TypePattern> getArgs() { return args; }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
