package edu.cmu.team2.iotms.model.ruleManager.RM_Storage;

import java.util.LinkedList;
import java.util.ListIterator;

import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Rule;

public abstract class RuleManagerStroage extends Thread {
	
	public abstract ListIterator<String> loadRuleSet();
	public abstract boolean storeRuleSet(LinkedList<Rule> rules);
	public abstract boolean insertRule (Rule rule);
	public abstract boolean deleteRule (Rule rule);
}
