package javamop.output.combinedaspect.event;

import java.util.ArrayList;

import javamop.parser.ast.aspectj.ArgsPointCut;
import javamop.parser.ast.aspectj.CFlowPointCut;
import javamop.parser.ast.aspectj.CombinedPointCut;
import javamop.parser.ast.aspectj.ConditionPointCut;
import javamop.parser.ast.aspectj.EndObjectPointCut;
import javamop.parser.ast.aspectj.EndProgramPointCut;
import javamop.parser.ast.aspectj.EndThreadPointCut;
import javamop.parser.ast.aspectj.FieldPointCut;
import javamop.parser.ast.aspectj.IDPointCut;
import javamop.parser.ast.aspectj.IFPointCut;
import javamop.parser.ast.aspectj.MethodPointCut;
import javamop.parser.ast.aspectj.NotPointCut;
import javamop.parser.ast.aspectj.PointCut;
import javamop.parser.ast.aspectj.StartThreadPointCut;
import javamop.parser.ast.aspectj.TargetPointCut;
import javamop.parser.ast.aspectj.ThisPointCut;
import javamop.parser.ast.aspectj.ThreadNamePointCut;
import javamop.parser.ast.aspectj.ThreadPointCut;
import javamop.parser.ast.aspectj.WithinPointCut;

public class PointcutComparator {

	public boolean compare(PointCut p1, PointCut p2){
		return p1.toString().equals(p2.toString());
	}
	
	public boolean compare(ArgsPointCut p1, ArgsPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(CombinedPointCut p1, CombinedPointCut p2){
		ArrayList<PointCut> list2 = new ArrayList<PointCut>();
		list2.addAll(p2.getPointcuts());

		for(PointCut p3 : p1.getPointcuts()){
			boolean found = false;
			for(PointCut p4 : p2.getPointcuts()){
				if(compare(p3, p4)){
					found = true;
					list2.remove(p4);
					break;
				}
			}
			
			if(!found)
				return false;
		}
		
		for(PointCut p3 : list2){
			boolean found = false;
			for(PointCut p4 : p1.getPointcuts()){
				if(compare(p3, p4)){
					found = true;
					break;
				}
			}
			
			if(!found)
				return false;
		}
		
		return true;
	}

	public boolean compare(NotPointCut p1, NotPointCut p2){
		return compare(p1.getPointCut(), p2.getPointCut());
	}
	
	public boolean compare(ConditionPointCut p1, ConditionPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(FieldPointCut p1, FieldPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(MethodPointCut p1, MethodPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(TargetPointCut p1, TargetPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(ThisPointCut p1, ThisPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(CFlowPointCut p1, CFlowPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(IFPointCut p1, IFPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(IDPointCut p1, IDPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(WithinPointCut p1, WithinPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(ThreadNamePointCut p1, ThreadNamePointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(ThreadPointCut p1, ThreadPointCut p2){
		return p1.toString().equals(p2.toString());
	}
	
	public boolean compare(EndProgramPointCut p1, EndProgramPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(EndThreadPointCut p1, EndThreadPointCut p2){
		return p1.toString().equals(p2.toString());
	}
	
	public boolean compare(EndObjectPointCut p1, EndObjectPointCut p2){
		return p1.toString().equals(p2.toString());
	}

	public boolean compare(StartThreadPointCut p1, StartThreadPointCut p2){
		return p1.toString().equals(p2.toString());
	}
	
}
