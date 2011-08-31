package logicrepository.plugins.ptcaret.visitor;

import logicrepository.plugins.ptcaret.ast.PTCARET_BinaryFormula;
import logicrepository.plugins.ptcaret.ast.PTCARET_False;
import logicrepository.plugins.ptcaret.ast.PTCARET_Formula;
import logicrepository.plugins.ptcaret.ast.PTCARET_Id;
import logicrepository.plugins.ptcaret.ast.PTCARET_True;
import logicrepository.plugins.ptcaret.ast.PTCARET_UnaryFormula;

public interface VoidVisitor<A> {

	public void visit(PTCARET_True n, A arg);

	public void visit(PTCARET_False n, A arg);

	public void visit(PTCARET_Id n, A arg);

	public void visit(PTCARET_UnaryFormula n, A arg);

	public void visit(PTCARET_BinaryFormula n, A arg);

	public void visit(PTCARET_Formula n, A arg);
}