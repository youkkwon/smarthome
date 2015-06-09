package RM_Storage;

import java.util.LinkedList;

import RM_Core.Rule;

public abstract class RuleSetStroage {
	
	public abstract LinkedList<Rule>  RoadRuleSet();
	public abstract boolean StoreRuleSet(LinkedList<Rule> ruleset);
	public abstract boolean InsertRule (Rule rule);
	public abstract boolean DeleteRule (Rule rule);
}
