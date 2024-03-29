package logicrepository.plugins.fsm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import logicrepository.plugins.fsm.parser.ast.*;

public class FSMEnables {
	private static State fail;

	static {
		fail = State.get("fail");
	}

	private State startState;
	private ArrayList<Symbol> events;
	private ArrayList<State> states;
	private ArrayList<State> categories;
	private HashMap<State, HashSet<State>> aliases;
	private HashMap<State, Transition> stateMap;
	private HashMap<State, HashSet<State>> reachability;
	private String creationEvents = "";
	private HashMap<State, HashMap<Symbol, HashSet<HashSet<Symbol>>>> enables;

	FSMEnables(State startState, ArrayList<Symbol> events, ArrayList<State> states, ArrayList<State> categories, HashMap<State, HashSet<State>> aliases,
			HashMap<State, Transition> stateMap) {
		this.startState = startState;
		this.events = events;
		this.states = states;
		this.categories = categories;
		this.aliases = aliases;
		this.stateMap = stateMap;

		reachability = new HashMap();
		enables = new HashMap();

		computeReachability();
		computeEnables();
	}

	private void computeEnables() {
		for (State category : categories) {
			HashMap<Symbol, HashSet<HashSet<Symbol>>> categoryEnable = new HashMap<Symbol, HashSet<HashSet<Symbol>>>();
			enables.put(category, categoryEnable);
			for (Symbol event : events) {
				categoryEnable.put(event, new HashSet<HashSet<Symbol>>());
			}
		}
		HashMap<State, HashSet<HashSet<Symbol>>> eventsSeen = new HashMap();
		for (State state : states) {
			eventsSeen.put(state, new HashSet<HashSet<Symbol>>());
		}
		computeEnables(startState, new HashSet<Symbol>(), eventsSeen);
		HashSet<Symbol> nil = new HashSet<Symbol>();
		HashSet<Symbol> creationEventsSet = new HashSet<Symbol>();
		out: for (State category : categories) {
			for (Symbol event : events) {
				if (enables.get(category).get(event).contains(nil)) {
					creationEventsSet.add(event);
					if (creationEventsSet.size() == events.size())
						break out;
				}
			}
		}
		for (Symbol event : creationEventsSet) {
			creationEvents += " " + event;
		}
	}

	private void computeEnables(State state, HashSet<Symbol> eventPath, HashMap<State, HashSet<HashSet<Symbol>>> eventsSeen) {
		// add this path to the mapping of seen event paths
		// for this state. This avoids infinite recursion
		// because we check to see if a state has seen a given
		// path before we take the recursive step
		eventsSeen.get(state).add(eventPath);
		boolean nContainsDefault = true;
		State defaultDestination = null;
		HashSet<Symbol> definedSymbols = new HashSet();
		Transition t = stateMap.get(state);
		for (Symbol event : t.keySet()) {
			State destination = t.get(event);
			if (event == null) {
				nContainsDefault = false;
				defaultDestination = destination;
				continue;
			}
			definedSymbols.add(event);
			for (State category : categories) {
				if (reachability.get(category).contains(destination)) {
					enables.get(category).get(event).add(eventPath);
				}
			}
			HashSet<Symbol> newPath = (HashSet<Symbol>) eventPath.clone();
			newPath.add(event);
			if (!eventsSeen.get(destination).contains(newPath))
				computeEnables(destination, newPath, eventsSeen);
		}
		if (nContainsDefault) {
			// We need to add the current event path to the fail enable for all
			// events that do not have transitions out of the current state
			// (or a category that is an alias which contains fail)
			// because there is no default transition
			handleImmediateFailures(definedSymbols, eventPath);
		} else {
			// handle the default transition, which should compute
			// for all events not explicitly listed
			handleDefaultTransition(definedSymbols, eventPath, defaultDestination, eventsSeen);
		}
	}

	private void computeReachability() {
		for (State category : categories) {
			reachability.put(category, new HashSet<State>());
		}
		HashMap<State, HashSet<HashSet<State>>> seen = new HashMap();
		for (State state : states) {
			seen.put(state, new HashSet<HashSet<State>>());
		}
		computeReachability(startState, new HashSet<State>(), seen);
	}

	private void computeReachability(State state, HashSet<State> path, HashMap<State, HashSet<HashSet<State>>> seen) {
		// add this path to the mapping of seen paths
		// for this state. This avoids infinite recursion
		// because we check to see if a state has seen a given
		// path before we take the recursive step
		seen.get(state).add(path);
		HashSet<State> newPath = (HashSet<State>) path.clone();
		newPath.add(state);
		for (State category : categories) {
			// If this is a state that is in one of our categories
			// put all the states seen on the path to this state in
			// the reachability for said category

			// First check to see if the category is an alias
			if (aliases.containsKey(category)) {
				HashSet<State> aliasedStates = aliases.get(category);
				if (aliasedStates.contains(state)) {
					addPath(category, newPath);
				}
				// the alias contains fail, we only need to check this
				// if the alias does not already contain this state
				// bcause adding the path to the category a second
				// time accomplishes nothing
				else if (aliasedStates.contains(fail) && !nContainsDefault(state) && (stateMap.get(state).size() < events.size())) {
					addPath(category, newPath);
				}
			}
			// otherwise it must be a state or "fail", outright.
			// This is essentially a copy of the above, only the conditions
			// differ
			else {
				if (category.equals(state)) {
					addPath(category, newPath);
				} else if (category.equals(fail) && !nContainsDefault(state) && (stateMap.get(state).size() < events.size())) {
					addPath(fail, newPath);
				}
			}
		}
		// This is the recursive call. We call it for every
		// path out of the current state, after appending this state to
		// the path. Note that we must clone the path, because all
		// Objects are references in Java, we do not want one path
		// for every recursive call! I implemented my own copy
		// because I don't like the unsafe operation warning
		// clone gives
		Transition t = stateMap.get(state);
		for (Symbol event : t.keySet()) {
			State destination = t.get(event);
			if (!seen.get(destination).contains(newPath))
				computeReachability(destination, newPath, seen);
		}
	}

	private void addPath(State category, HashSet<State> path) {
		for (State pathState : path) {
			reachability.get(category).add(pathState);
		}
	}

	private boolean nContainsDefault(State state) {
		return stateMap.get(state).containsSymbol(null);
	}

	private void handleImmediateFailures(HashSet<Symbol> definedSymbols, HashSet<Symbol> eventPath) {
		for (State category : categories) {
			if (aliases.containsKey(category) && aliases.get(category).contains(fail) || category.equals(fail)) {
				for (Symbol event : events) {
					if (!definedSymbols.contains(event)) {
						enables.get(category).get(event).add(eventPath);
					}
				}
			}
		}
	}

	private void handleDefaultTransition(HashSet<Symbol> definedSymbols, HashSet<Symbol> eventPath, State defaultDestination,
			HashMap<State, HashSet<HashSet<Symbol>>> eventsSeen) {
		for (State category : categories) {
			if (!reachability.get(category).contains(defaultDestination))
				continue;
			HashMap<Symbol, HashSet<HashSet<Symbol>>> categoryEnables = enables.get(category);
			for (Symbol event : events) {
				if (!definedSymbols.contains(event)) {
					categoryEnables.get(event).add(eventPath);
					HashSet<Symbol> newPath = (HashSet<Symbol>) eventPath.clone();
					newPath.add(event);
					if (!eventsSeen.get(defaultDestination).contains(newPath))
						computeEnables(defaultDestination, newPath, eventsSeen);
				}
			}
		}
	}

	public HashMap<State, HashMap<Symbol, HashSet<HashSet<Symbol>>>> getEnables() {
		return enables;
	}

	public String toString() {
		String output = "";
		for (State category : categories) {
			output += "// " + category + " Enables\n";
			output += enables.get(category).toString() + "\n";
		}
		return output;
	}

	public String FSMString() {
		String output = startState.toString();
		output += stringOfTransitions(startState);
		for (State key : stateMap.keySet()) {
			if (key == startState)
				continue;
			output += key;
			output += stringOfTransitions(key);
		}
		for (State key : aliases.keySet()) {
			output += stringOfAlias(key);
		}
		return output;
	}

	public String creationEvents() {
		return creationEvents;
	}

	private String stringOfTransitions(State state) {
		return "[\n" + stateMap.get(state) + "\n]\n";
	}

	private String stringOfAlias(State alias) {
		String aliasStr = aliases.get(alias).toString();
		return "alias " + alias + " = " + aliasStr.substring(1, aliasStr.length() - 1) + "\n";
	}
}
