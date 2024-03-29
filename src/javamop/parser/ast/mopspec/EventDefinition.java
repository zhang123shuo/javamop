package javamop.parser.ast.mopspec;

import java.util.*;
import java.io.*;
import javamop.parser.ast.*;
import javamop.parser.ast.aspectj.*;
import javamop.parser.ast.stmt.*;
import javamop.parser.ast.visitor.*;
import javamop.parser.ast.type.*;

public class EventDefinition extends Node {

	String id;
	Type retType;
	String pos;

	String pointCutStr;
	String purePointCutStr;
	PointCut pointCut;

	MOPParameters parameters;
	boolean hasReturning;
	MOPParameters retVal;
	boolean hasThrowing;
	MOPParameters throwVal;

	MOPParameters mopParameters;

	BlockStmt block;
	MOPParameters usedParameter = null;


	// will be modified by JavaMOPSpec when creation events are not specified
	boolean startEvent = false;
	
	// similar to startEvent, but won't be modified
	boolean creationEvent = false;

	String condition;
	String threadVar;
	String threadNameVar;
	ArrayList<String> threadBlockedVars;
	TypePattern endObjectType;
	String endObjectId;
	boolean endProgram = false;
	boolean endThread = false;
	boolean startThread = false;
	boolean endObject = false;
	String countCond;

	// things that should be defined afterward
	int idnum; // will be defined in JavaMOPSpec
	boolean duplicated = false; // will be defined in JavaMOPSpec
	String uniqueId = null; // will be defined in JavaMOPSpec
	MOPParameters mopParametersOnSpec; // will be defined in JavaMOPSpec

	public EventDefinition(int line, int column, String id, Type retType, String pos, List<MOPParameter> parameters, String pointCutStr, BlockStmt block,
			boolean hasReturning, List<MOPParameter> retVal, boolean hasThrowing, List<MOPParameter> throwVal, boolean startEvent, boolean creationEvent)
			throws javamop.parser.main_parser.ParseException {
		super(line, column);
		this.id = id;
		this.retType = retType;
		this.pos = pos;
		this.parameters = new MOPParameters(parameters);
		this.pointCutStr = pointCutStr;
		this.block = block;
		this.hasReturning = hasReturning;
		this.retVal = new MOPParameters(retVal);
		this.hasThrowing = hasThrowing;
		this.throwVal = new MOPParameters(throwVal);
		if(pointCutStr != null) 
			this.pointCut = parsePointCut(pointCutStr);
		this.startEvent = startEvent;
		this.creationEvent = creationEvent;
		this.mopParameters = new MOPParameters();
		this.mopParameters.addAll(this.parameters);
		this.mopParameters.addAll(this.retVal);
		this.mopParameters.addAll(this.throwVal);
	}

	private PointCut parsePointCut(String input) throws javamop.parser.main_parser.ParseException {
		// create a token for exceptions
		javamop.parser.main_parser.Token t = new javamop.parser.main_parser.Token();
		t.beginLine = super.getBeginLine();
		t.beginColumn = super.getBeginColumn();
		
		PointCut originalPointCut;
		PointCut resultPointCut;
		purePointCutStr = "";
		threadVar = "";
		condition = "";

		try {
			originalPointCut = javamop.parser.aspectj_parser.AspectJParser.parse(new ByteArrayInputStream(input.getBytes()));
		} catch (javamop.parser.aspectj_parser.ParseException e) {
			throw new javamop.parser.main_parser.ParseException("The following error encountered when parsing the pointcut in the event definition: "
					+ e.getMessage());
		}
		
		// thread pointcut
		threadVar = originalPointCut.accept(new ThreadVarVisitor(), null);
		if (threadVar == null)
			throw new javamop.parser.main_parser.ParseException("There are more than one thread() pointcut.");
		if (threadVar.length() != 0) {
			resultPointCut = originalPointCut.accept(new RemoveThreadVisitor(), new Integer(1));
		} else
			resultPointCut = originalPointCut;
		if (resultPointCut == null)
			throw new javamop.parser.main_parser.ParseException("thread() pointcut should appear at the root level in a conjuction form");
		
		// thread name pointcut
		threadNameVar = resultPointCut.accept(new ThreadNameVarVisitor(), null);
		if (threadNameVar == null)
			throw new javamop.parser.main_parser.ParseException("There are more than one threadName() pointcut.");
		if (threadNameVar.length() != 0) {
			resultPointCut = resultPointCut.accept(new RemoveThreadNameVisitor(), new Integer(1));
		} 
		if (resultPointCut == null)
			throw new javamop.parser.main_parser.ParseException("threadName() pointcut should appear at the root level in a conjuction form");
		
		// thread blocked pointcut
		String blockedThreads = resultPointCut.accept(new ThreadBlockedVarVisitor(), null);
		if (blockedThreads == null) {
			throw new javamop.parser.main_parser.ParseException("threadBlocked() should have one parameter.");
		} 
		if (blockedThreads.length() != 0) {
			resultPointCut = resultPointCut.accept(new RemoveThreadBlockedVisitor(), new Integer(1));
			threadBlockedVars = new ArrayList<String>();
			String vars[] = blockedThreads.split("@");
			for (String var : vars) {
				threadBlockedVars.add(var);
			}
		}
		if (resultPointCut == null)
			throw new javamop.parser.main_parser.ParseException("threadBlocked() pointcut should appear at the root level in a conjuction form");

		
		// condition pointcut
		condition = resultPointCut.accept(new ConditionVisitor(), null);
		if (condition == null)
			throw new javamop.parser.main_parser.ParseException("There are more than one condition() pointcut.");
		if (condition.length() != 0) {
			resultPointCut = resultPointCut.accept(new RemoveConditionVisitor(), new Integer(1));
		}
		// syntax de-sugar threadName pointcut into condition constraint
		if (threadNameVar != null && threadNameVar.length() != 0) {
			if (condition.length() != 0) {
				condition = ("Thread.currentThread().getName().equals(" + threadNameVar + ") && (" 
						+ condition + ")");
			} else {
				condition = "Thread.currentThread().getName().equals(" + threadNameVar + ")";
			}
		}
		if (resultPointCut == null)
			throw new javamop.parser.main_parser.ParseException("condition() pointcut should appear at the root level in a conjuction form");

		
		// Count condition pointcut
		countCond = resultPointCut.accept(new CountCondVisitor(), null);
		if (countCond == null)
			throw new javamop.parser.main_parser.ParseException("There are more than one countCond() pointcut.");
		if (countCond.length() != 0) {
			resultPointCut = resultPointCut.accept(new RemoveCountCondVisitor(), new Integer(1));
		}
		if (resultPointCut == null)
			throw new javamop.parser.main_parser.ParseException("countCond() pointcut should appear at the root level in a conjuction form");
		
		// endProgram pointcut
		String checkEndProgram = resultPointCut.accept(new EndProgramVisitor(), null);
		if (checkEndProgram == null)
			throw new javamop.parser.main_parser.ParseException("There are more than one endProgram() pointcut.");
		if (checkEndProgram.length() != 0) {
			endProgram = true;
			resultPointCut = resultPointCut.accept(new RemoveEndProgramVisitor(), new Integer(1));
		} else {
			endProgram = false;
		}
		if (resultPointCut == null)
			throw new javamop.parser.main_parser.ParseException("endProgram() pointcut should appear at the root level in a conjuction form");

		// endThread pointcut
		String checkEndThread = resultPointCut.accept(new EndThreadVisitor(), null);
		if (checkEndThread == null)
			throw new javamop.parser.main_parser.ParseException("There are more than one endThread() pointcut.");
		if (checkEndThread.length() != 0) {
			endThread = true;
			resultPointCut = resultPointCut.accept(new RemoveEndThreadVisitor(), new Integer(1));
		} else {
			endThread = false;
		}
		if (resultPointCut == null)
			throw new javamop.parser.main_parser.ParseException("endThread() pointcut should appear at the root level in a conjuction form");
		if (endProgram && endThread)
			throw new javamop.parser.main_parser.ParseException("endProgram() pointcut and endThread() pointcut cannot appear at the same time");

		// startThread pointcut
		String checkStartThread = resultPointCut.accept(new StartThreadVisitor(), null);
		if (checkStartThread == null)
			throw new javamop.parser.main_parser.ParseException("There are more than one startThread() pointcut.");
		if (checkStartThread.length() != 0) {
			startThread = true;
			resultPointCut = resultPointCut.accept(new RemovePointCutVisitor("startThread"), new Integer(1));
		} else {
			startThread = false;
		}
		if (resultPointCut == null)
			throw new javamop.parser.main_parser.ParseException("startThread() pointcut should appear at the root level in a conjuction form");
		if (endThread && startThread)
			throw new javamop.parser.main_parser.ParseException("startThread() pointcut and endThread() pointcut cannot appear at the same time");
		
		// endObject pointcut
		endObjectId = resultPointCut.accept(new EndObjectVisitor(), null);
		endObjectType = resultPointCut.accept(new EndObjectTypeVisitor(), null);
		if (endObjectId == null || (endObjectId.length() != 0 && endObjectType == null))
			throw new javamop.parser.main_parser.ParseException("There are more than one endObject() pointcut.");
		if (endObjectId.length() != 0) {
			endObject = true;
			resultPointCut = resultPointCut.accept(new RemoveEndObjectVisitor(), new Integer(1));
		} else {
			endObject = false;
		}
		if (resultPointCut == null)
			throw new javamop.parser.main_parser.ParseException("endObject() pointcut should appear at the root level in a conjuction form");
		if (endObject && (endProgram || endThread))
			throw new javamop.parser.main_parser.ParseException("endProgram() pointcut, endThread(), and endObject() pointcut cannot appear at the same time");

		purePointCutStr = resultPointCut.toString();

		return resultPointCut;
	}

	public String getId() {
		return id;
	}

	public int getIdNum() {
		return idnum;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public Type getRetType() {
		return retType;
	}

	public String getPos() {
		return pos;
	}

	public MOPParameters getParameters() {
		return parameters;
	}

	MOPParameters parametersWithoutThreadVar = null;

	public MOPParameters getParametersWithoutThreadVar() {
		if (parametersWithoutThreadVar != null)
			return parametersWithoutThreadVar;

		parametersWithoutThreadVar = new MOPParameters();
		for (MOPParameter param : parameters) {
			if (getThreadVar() != null && getThreadVar().length() != 0 && param.getName().equals(getThreadVar()))
				continue;
			parametersWithoutThreadVar.add(param);
		}

		return parametersWithoutThreadVar;
	}

	public MOPParameters getMOPParameters() {
		return mopParameters;
	}

	MOPParameters mopParametersWithoutThreadVar = null;
	public MOPParameters getMOPParametersWithoutThreadVar() {
		if(mopParametersWithoutThreadVar != null)
			return mopParametersWithoutThreadVar;
		
		mopParametersWithoutThreadVar = new MOPParameters();
		for(MOPParameter param : mopParameters){
			if (getThreadVar() != null && getThreadVar().length() != 0 && param.getName().equals(getThreadVar()))
				continue;
			mopParametersWithoutThreadVar.add(param);
		}
		return mopParametersWithoutThreadVar;
	}
	
	public MOPParameters getMOPParametersOnSpec() {
		return mopParametersOnSpec;
	}

	public PointCut getPointCut() {
		return pointCut;
	}

	public String getPointCutString() {
		return pointCutStr;
	}

	public BlockStmt getAction() {
		return block;
	}
	
	public MOPParameters getUsedParametersIn(MOPParameters specParam){
		//if cached, return it.
		if(usedParameter != null)
			return usedParameter;
		
		usedParameter = block.accept(new CollectMOPVarVisitor(), specParam);
		
		return usedParameter;
	}


	public boolean hasReturning() {
		return hasReturning;
	}

	public MOPParameters getRetVal() {
		return retVal;
	}

	public boolean hasThrowing() {
		return hasThrowing;
	}

	public MOPParameters getThrowVal() {
		return throwVal;
	}

	public String getThreadVar() {
		return threadVar;
	}
	
	public ArrayList<String> getThreadBlockedVar() {
		return threadBlockedVars;
	}

	public String getCondition() {
		return condition;
	}
	
	public String getCountCond() {
		return countCond;
	}

	public String getPurePointCutString() {
		return purePointCutStr;
	}

	public String getEndObjectVar() {
		if(this.endObject)
			return endObjectId;
		else
			return null;
	}
	
	public TypePattern getEndObjectType(){
		if(this.endObject)
			return endObjectType;
		else
			return null;
	}
	
	public boolean isStartEvent() {
		return this.startEvent;
	}
	
	public boolean isCreationEvent() {
		return this.creationEvent;
	}
	

	public boolean isEndProgram() {
		return this.endProgram;
	}

	public boolean isEndThread() {
		return this.endThread;
	}

	public boolean isEndObject() {
		return this.endObject;
	}

	public boolean isStartThread() {
		return this.startThread;
	}

	private Boolean cachedHas__SKIP = null;

	public boolean has__SKIP() {
		if (cachedHas__SKIP != null)
			return cachedHas__SKIP.booleanValue();

		if(this.getAction() != null){
			String eventAction = this.getAction().toString();
			if (eventAction.indexOf("__SKIP") != -1){
				cachedHas__SKIP = new Boolean(true);
				return true;
			}
		}
		cachedHas__SKIP = new Boolean(false);
		return false;
	}
	
	private Boolean cachedHas__LOC = null;

	public boolean has__LOC() {
		if (cachedHas__LOC != null)
			return cachedHas__LOC.booleanValue();

		if(this.getAction() != null){
			String eventAction = this.getAction().toString();
			if (eventAction.indexOf("__LOC") != -1
        || eventAction.indexOf("__DEFAULT_MESSAGE") != -1){
				cachedHas__LOC = new Boolean(true);
				return true;
			}
		}
		cachedHas__LOC = new Boolean(false);
		return false;
	}
	
	private Boolean cachedHas__STATICSIG = null;

	public boolean has__STATICSIG() {
		if (cachedHas__STATICSIG != null)
			return cachedHas__STATICSIG.booleanValue();

		if (this.getAction() != null) {
			String eventAction = this.getAction().toString();
			// Nice! (This was copied from the JavaMOPSpec class.)
			if (eventAction.indexOf("__STATICSIG") != -1){
				cachedHas__STATICSIG = new Boolean(true);
				return true;
			}
		}
		cachedHas__STATICSIG = new Boolean(false);
		return false;
	}

	@Override
	public <A> void accept(VoidVisitor<A> v, A arg) {
		v.visit(this, arg);
	}

	@Override
	public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
		return v.visit(this, arg);
	}

}
