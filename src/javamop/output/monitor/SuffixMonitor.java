package javamop.output.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javamop.MOPException;
import javamop.output.MOPVariable;
import javamop.output.OptimizedCoenableSet;
import javamop.output.combinedaspect.GlobalLock;
import javamop.output.combinedaspect.indexingtree.reftree.RefTree;
import javamop.parser.ast.mopspec.EventDefinition;
import javamop.parser.ast.mopspec.JavaMOPSpec;
import javamop.parser.ast.mopspec.PropertyAndHandlers;
import javamop.parser.ast.stmt.BlockStmt;

public class SuffixMonitor extends Monitor {
	MOPVariable loc = new MOPVariable("MOP_loc");
	MOPVariable staticsig = new MOPVariable("MOP_staticsig");
	MOPVariable lastevent = new MOPVariable("MOP_lastevent");
	MOPVariable skipAroundAdvice = new MOPVariable("MOP_skipAroundAdvice");
	MOPVariable thisJoinPoint = new MOPVariable("thisJoinPoint");

	List<EventDefinition> events;

	Monitor innerMonitor = null;

	ArrayList<String> categories;
	MOPVariable monitorList = new MOPVariable("monitorList");
	boolean existSkip = false;
	String aspectName;
	
	public SuffixMonitor(String name, JavaMOPSpec mopSpec, OptimizedCoenableSet coenableSet, boolean isOutermost) throws MOPException {
		super(name, mopSpec, coenableSet, isOutermost);

		this.isDefined = mopSpec.isSuffixMatching();

		if (this.isDefined) {
			monitorName = new MOPVariable(mopSpec.getName() + "SuffixMonitor");

			if (isOutermost) {
				varInOutermostMonitor = new VarInOutermostMonitor(name, mopSpec, mopSpec.getEvents());
				monitorTermination = new MonitorTermination(name, mopSpec, mopSpec.getEvents(), coenableSet);
			}
			
			if (mopSpec.isEnforce())
			{
				// TODO Do we need raw monitor for enforcing properties?
				innerMonitor = new EnforceMonitor(name, mopSpec, coenableSet, false);
				for (PropertyAndHandlers p : mopSpec.getPropertiesAndHandlers()) {
					int totalHandlers = p.getHandlers().size();
					if (p.getHandlers().containsKey("deadlock"))
						totalHandlers--;
					// We only allow one handler (except deadlock handler) when enforcing a property
					if (totalHandlers > 1)
						throw new MOPException("Only one handler (except deadlock handler) is allowed when enforcing a property");
				}

			}
			else
			{
				if (mopSpec.getPropertiesAndHandlers().size() == 0)
					innerMonitor = new RawMonitor(name, mopSpec, coenableSet, false);
				else		
					innerMonitor = new BaseMonitor(name, mopSpec, coenableSet, false);
			}
			events = mopSpec.getEvents();
			
			for (PropertyAndHandlers prop : mopSpec.getPropertiesAndHandlers()) {
				if(!existSkip){
					for (BlockStmt handler : prop.getHandlers().values()) {
						if (handler.toString().indexOf("__SKIP") != -1){
							existSkip = true;
							break;
						}
					}
				}
			}

			for (EventDefinition event : events) {
				if (event.has__SKIP()){
					existSkip = true;
					break;
				}
			}
		} else {
			if (mopSpec.isEnforce())
			{
				// TODO Do we need raw monitor for enforcing properties?
				innerMonitor = new EnforceMonitor(name, mopSpec, coenableSet, isOutermost);
				for (PropertyAndHandlers p : mopSpec.getPropertiesAndHandlers()) {
					int totalHandlers = p.getHandlers().size();
					if (p.getHandlers().containsKey("deadlock"))
						totalHandlers--;
					// We only allow one handler (except deadlock handler) when enforcing a property
					if (totalHandlers > 1)
						throw new MOPException("Only one handler (except deadlock handler) is allowed when enforcing a property");
				}
			}
			else
			{
				if (mopSpec.getPropertiesAndHandlers().size() == 0)
					innerMonitor = new RawMonitor(name, mopSpec, coenableSet, isOutermost);
				else		
					innerMonitor = new BaseMonitor(name, mopSpec, coenableSet, isOutermost);
			}
		}

		if (this.isDefined && mopSpec.isGeneral()){
			if(mopSpec.isFullBinding() || mopSpec.isConnected())
				monitorInfo = new MonitorInfo(mopSpec);
		}
	}
	
	public void setRefTrees(HashMap<String, RefTree> refTrees){
		this.refTrees = refTrees;
		innerMonitor.setRefTrees(refTrees);
		
		if(monitorTermination != null)
			monitorTermination.setRefTrees(refTrees);
	}
	
	public void setAspectName(String name) {
		this.aspectName = name;
	}
	
	public String getAspectName() {
		return this.aspectName;
	}

	public MOPVariable getOutermostName() {
		if (isDefined)
			return monitorName;
		else
			return innerMonitor.getOutermostName();
	}

	public Set<String> getNames() {
		Set<String> ret = innerMonitor.getNames();
		if (isDefined)
			ret.add(monitorName.toString());
		return ret;
	}

	public Set<MOPVariable> getCategoryVars() {
		return innerMonitor.getCategoryVars();
	}

	public String doEvent(EventDefinition event) {
		String ret = "";

		String uniqueId = event.getUniqueId();
		int idnum = event.getIdNum();

		MOPVariable monitor = new MOPVariable("monitor");
		MOPVariable monitorSet = new MOPVariable("monitorSet");
		MOPVariable newMonitor = new MOPVariable("newMonitor");
		MOPVariable it = new MOPVariable("it");
		HashSet<MOPVariable> categoryVars = new HashSet<MOPVariable>();

		categoryVars.addAll(innerMonitor.getCategoryVars());

		ret += "public final void event_" + uniqueId + "(" + event.getMOPParameters().parameterDeclString() + ") {\n";

		if (isOutermost) {
			ret += lastevent + " = " + idnum + ";\n";
		}

		ret += "HashSet " + monitorSet + " = new HashSet();\n";

		if (event.isStartEvent()) {
			ret += innerMonitor.getOutermostName() + " " + newMonitor + " = new " + innerMonitor.getOutermostName() + "();\n";
			if (monitorInfo != null){
				ret += monitorInfo.copy(newMonitor);
			}
			ret += monitorList + ".add(" + newMonitor + ");\n";
		}

		ret += "Iterator " + it + " = " + monitorList + ".iterator();\n";
		ret += "while (" + it + ".hasNext()){\n";
		ret += innerMonitor.getOutermostName() + " " + monitor + " = (" + innerMonitor.getOutermostName() + ")" + it + ".next();\n";

		ret += innerMonitor.Monitoring(monitor, event, loc, staticsig, null, this.aspectName, false, false);

		ret += "if(" + monitorSet + ".contains(" + monitor + ")";
		for (MOPVariable categoryVar : categoryVars) {
			ret += " || " + monitor + "." + categoryVar;
		}
		ret += " ) {\n";
		ret += it + ".remove();\n";
		ret += "} else {\n";
		ret += monitorSet + ".add(" + monitor + ");\n";
		ret += "}\n";

		ret += "}\n";

		ret += "}\n";

		return ret;
	}

	public String Monitoring(MOPVariable monitorVar, EventDefinition event, MOPVariable loc, MOPVariable staticsig, GlobalLock l, String aspectName, boolean inMonitorSet, boolean isShutdownHook) {
		String ret = "";
		boolean checkSkip = event.getPos().equals("around");

		if (!isDefined)
			return innerMonitor.Monitoring(monitorVar, event, loc, staticsig, l, aspectName, inMonitorSet, isShutdownHook);

		if (has__LOC) {
			if(loc != null)
				ret += monitorVar + "." + this.loc + " = " + loc + ";\n";
			else
				if(isShutdownHook)
					ret += monitorVar + "." + this.loc + " = " + "\"[End of Program]\"" + ";\n";
				else
					ret += monitorVar + "." + this.loc + " = " + "thisJoinPoint.getSourceLocation().toString()" + ";\n";
		}
		if (has__STATICSIG) {
			if(staticsig != null)
				ret += monitorVar + "." + this.staticsig + " = " + staticsig + ";\n";
			else
				ret += monitorVar + "." + this.staticsig + " = " + "thisJoinPoint.getStaticPart().getSignature()" + ";\n";
		}

		if (this.hasThisJoinPoint){
			ret += monitorVar + "." + this.thisJoinPoint + " = " + this.thisJoinPoint + ";\n";
		}

		if (checkSkip && event.has__SKIP()) {
			ret += monitorVar + "." + skipAroundAdvice + " = false;\n";
		}

		ret += monitorVar + ".event_" + event.getUniqueId() + "(";
		ret += event.getMOPParameters().parameterString();
		ret += ");\n";

		if (this.hasThisJoinPoint){
			ret += monitorVar + "." + this.thisJoinPoint + " = null;\n";
		}

		if (checkSkip && event.has__SKIP()) {
			ret += skipAroundAdvice + " |= " + monitorVar + "." + skipAroundAdvice + ";\n";
		}

		return ret;
	}

	public MonitorInfo getMonitorInfo(){
		if (isDefined)
			return monitorInfo;
		else
			return innerMonitor.getMonitorInfo();
		
	}

	public String toString() {
		String ret = "";

		MOPVariable monitor = new MOPVariable("monitor");
		MOPVariable newMonitor = new MOPVariable("newMonitor");

		if (isDefined) {
			ret += "class " + monitorName;
			if (isOutermost)
				ret += " extends javamoprt.MOPMonitor";
			ret += " implements Cloneable, javamoprt.MOPObject {\n";

			if(varInOutermostMonitor != null)
				ret += varInOutermostMonitor;
			
			ret += "Vector<" + innerMonitor.getOutermostName() + "> " + monitorList + " = new Vector<" + innerMonitor.getOutermostName() + ">();\n";

			if (this.has__LOC)
				ret += "String " + loc + ";\n";
			if (this.has__STATICSIG)
				ret += "org.aspectj.lang.Signature " + staticsig + ";\n";
			if (this.hasThisJoinPoint)
				ret += "JoinPoint " + thisJoinPoint + " = null;\n";
			if (existSkip)
				ret += "boolean " + skipAroundAdvice + " = false;\n";

			// clone()
			ret += "public Object clone() {\n";
			ret += "try {\n";
			ret += monitorName + " ret = (" + monitorName + ") super.clone();\n";
			if (monitorInfo != null)
				ret += monitorInfo.copy("ret", "this");
			ret += "ret." + monitorList + " = new Vector<" + innerMonitor.getOutermostName() + ">();\n";
			ret += "for(" + innerMonitor.getOutermostName() + " " + monitor + " : this." + monitorList + "){\n";
			ret += innerMonitor.getOutermostName() + " " + newMonitor + " = ";
			ret += "(" + innerMonitor.getOutermostName() + ")" + monitor + ".clone()" + ";\n";
			if (monitorInfo != null)
				ret += monitorInfo.copy(newMonitor, monitor);
			ret += "ret." + monitorList + ".add(" + newMonitor + ");\n";
			ret += "}\n";
			ret += "return ret;\n";
			ret += "}\n";
			ret += "catch (CloneNotSupportedException e) {\n";
			ret += "throw new InternalError(e.toString());\n";
			ret += "}\n";
			ret += "}\n";
			ret += "\n";
			
			// events
			for (EventDefinition event : this.events) {
				ret += this.doEvent(event) + "\n";
			}

			// endObject and some declarations
			if (isOutermost && monitorTermination != null) {
				ret += monitorTermination;
			}

			if (monitorInfo != null){
				ret += monitorInfo.monitorDecl();
			}

			ret += "}\n";
			ret += "\n";
		}

		ret += this.innerMonitor;

		return ret;
	}
}
