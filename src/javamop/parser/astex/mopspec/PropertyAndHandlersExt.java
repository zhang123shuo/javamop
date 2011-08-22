package javamop.parser.astex.mopspec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javamop.logicpluginshells.LogicPluginShellResult;
import javamop.parser.ast.mopspec.MOPParameters;
import javamop.parser.ast.stmt.BlockStmt;
import javamop.parser.ast.visitor.CollectMOPVarVisitor;
import javamop.parser.astex.ExtNode;
import javamop.parser.astex.visitor.GenericVisitor;
import javamop.parser.astex.visitor.VoidVisitor;

public class PropertyAndHandlersExt extends ExtNode {
	
	PropertyExt property;
	HashMap<String, BlockStmt> handlers;
	HashMap<String, MOPParameters> usedParameters = new HashMap<String, MOPParameters>();

	List<HandlerExt> handlerList= new ArrayList<HandlerExt>();

	//things that should be defined afterward
	int propertyId; //will be defined in JavaMOPSpec
	Properties logicResult; //will be defined by MOPProcessor
	
	HashMap<String, String> eventMonitoringCodes = new HashMap<String, String>();
	HashMap<String, String> aftereventMonitoringCodes = new HashMap<String, String>();
	
	boolean versionedStack = false;

	public PropertyAndHandlersExt(int line, int column, PropertyExt property, HashMap<String, BlockStmt> handlers, List<HandlerExt> handlerList) {
		super(line, column);
		this.property = property;
		this.handlers = handlers;
		this.handlerList = handlerList;
	}

	public boolean hasHandler() {
		return (handlers != null);
	}

	public boolean hasGenericHandlers() {
		return (handlers != null);
	}

	public List<HandlerExt> getHandlerList(){
		return handlerList;
	}
	
	public PropertyExt getProperty() {
		return property;
	}

	public HashMap<String, BlockStmt> getHandlers() {
		return handlers;
	}
	
	public MOPParameters getUsedParametersIn(String category, MOPParameters specParam){
		MOPParameters ret;
		
		//if cached, return it.
		if((ret = usedParameters.get(category)) != null)
			return ret;
		
		BlockStmt handler = handlers.get(category);
		
		ret = handler.accept(new CollectMOPVarVisitor(), specParam);
		
		usedParameters.put(category, ret);
		
		return ret;
	}
	
	public int getPropertyId(){
		return propertyId;
	}
	
	public void setLogicShellOutput(LogicPluginShellResult logicShellOutput) {
		this.logicResult = logicShellOutput.properties;
		
		if(this.logicResult == null)
			return;
		
		parseMonitoredEvent(eventMonitoringCodes, this.logicResult.getProperty("monitored events"));
		parseMonitoredEvent(aftereventMonitoringCodes, this.logicResult.getProperty("after monitored events"));
	}
	
	public void parseMonitoredEvent(HashMap<String, String> codes, String eventStr){
		if(eventStr == null)
			return;
			
		Pattern p = Pattern.compile("(\\!)?(\\#)?\\s*(\\w+):\\{\\n");
		Matcher matcher = p.matcher(eventStr);

		String eventName;
		String eventMonitoringCode;

		while (matcher.find()) {
			eventName = matcher.group();
			eventName = eventName.replaceAll("(\\!)?(\\#)?\\s*(\\w+):\\{\\n", "$1$2$3");

			int k = javamop.util.Tool.findBlockEnd(eventStr, matcher.end());

			String eventActionTemp = eventStr.substring(matcher.end(), k);
			String[] eventActionTemp2 = eventActionTemp.split("\n");

			eventMonitoringCode = "";
			for (String eventActionTemp3 : eventActionTemp2) {
				if (eventActionTemp3 != null && eventActionTemp3.length() != 0)
					eventMonitoringCode += eventActionTemp3 + "\n";
			}
			codes.put(eventName, eventMonitoringCode);
		}
	}

	public String getLogicProperty(String input){
		if(logicResult == null)
			return null;
		return logicResult.getProperty(input);
	}
	
	public String getEventMonitoringCode(String eventName){
		return eventMonitoringCodes.get(eventName);
	}

	public String getAfterEventMonitoringCode(String eventName){
		return aftereventMonitoringCodes.get(eventName);
	}
	
	public void setVersionedStack(){
		versionedStack = true;
	}

	public boolean getVersionedStack(){
		return versionedStack;
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
