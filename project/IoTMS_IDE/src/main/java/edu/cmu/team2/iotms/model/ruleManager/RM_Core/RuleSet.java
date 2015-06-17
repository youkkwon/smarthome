package edu.cmu.team2.iotms.model.ruleManager.RM_Core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import edu.cmu.team2.iotms.model.ruleManager.RM_Storage.RuleManagerDBStorage;
import edu.cmu.team2.iotms.model.utility.InvalidRuleException;

// Singleton
public class RuleSet {

	private LinkedList<Rule> 	rules;
	private String				mode;			
	
	private String[] 			conditions	= null;
	private String[] 			actions 	= null;
	
	private static RuleSet ruleset = new RuleSet();
	
	private RuleSet ()
	{
		rules = new LinkedList<Rule>();
		loadRuleSet();
	}
	
	private int countChar (String string, char ch)
	{
		int		count = 0;
		char[] array = string.toCharArray();
		
		for (int i=0; i < array.length; i++)
			if (array[i] == ch) count++;
		
		return count;
	}
	
	private boolean loadRuleSet()
	{
		ListIterator<String> raw_rules = RuleManagerDBStorage.getInstance().loadRuleSet();
		
		while (raw_rules.hasNext())
		{
			String input = raw_rules.next();
			 
			parse_statement(input);
			
			try {
				Rule rule = new Rule(conditions, actions);
				rules.add(rule);
			} catch (InvalidRuleException e) {
				System.out.println(e.getExceptionMsg());
			}
		}
		return false;
	}
	
	public static RuleSet getInstance()
	{
		return ruleset;		
	}
		
	public void parse_statement (String input)
	{
		int idx, beginIndex, endIndex;
		 
		idx = input.indexOf("then");
		String condStr = input.substring(2, idx).trim();
		String actStr = input.substring(idx+4).trim();

		conditions = new String[countChar(condStr, ',')+1];
		actions = new String[countChar(actStr, ',')+1];
		
		beginIndex=0;
		for (int i=0; i < conditions.length; i++)
		{
			endIndex = condStr.indexOf(',', beginIndex);
			if (endIndex == -1)
				endIndex = condStr.length();
			conditions[i] = condStr.substring(beginIndex, endIndex).trim();
			beginIndex = endIndex+1;
		}
			
		beginIndex=0; 
		for (int i=0; i < actions.length; i++)
		{
			endIndex = actStr.indexOf(',', beginIndex);
			if (endIndex == -1)
				endIndex = actStr.length();
			actions[i] = actStr.substring(beginIndex, endIndex).trim();
			beginIndex = endIndex+1;
		}
	}
		
	public void addRule (String input)
	{
		parse_statement(input);
		addRule (conditions, actions);
	}
	
	public void deleteRule (String statement)
	{
		parse_statement(statement);		
		deleteRule (conditions, actions);
	}
	
	private  void addRule (String[] condStr, String[] actStr) 
	{
		boolean 			exist = false; 
		ListIterator<Rule>	iterator = rules.listIterator();
		try {
			while (iterator.hasNext()) 
			{
				Rule rule = iterator.next();
				if (rule.isSameCondition(condStr))
				{			
					rule.addActions(actStr);				
					exist = true;
					break;
				}
			}
			if (!exist)
			{
				Rule rule = new Rule(condStr, actStr);
				rules.add(rule);
			
				RuleManagerDBStorage.getInstance().insertRule(rule);
			}
		} catch (InvalidRuleException e) {
			System.out.println(e.getExceptionMsg());
		}	
	}
	
	private boolean deleteRule (String[] condStr, String[] actStr)
	{
		boolean				delete = false;
		ListIterator<Rule>	iterator = rules.listIterator();
		
		try {
			while (iterator.hasNext()) 
			{
				Rule rule = iterator.next();
				if (rule.isSameRule(condStr, actStr)) 
				{
					rules.remove(rule);
					delete = true;
					//System.out.println ("[RM - Process] deleteRule - SameRule " + rules.size());
					break;
				}
				else if (rule.isSameCondition(condStr))
				{
					rule.deleteActions(actStr);
					delete = true;
					//System.out.println ("[RM - Process] deleteRule - SameCondition " + rules.size());
					break;
				}
			}
		} catch (InvalidRuleException e) {
			System.out.println(e.getExceptionMsg());
		}
		
		if (delete) 
			RuleManagerDBStorage.getInstance().storeRuleSet(rules);
		else
			System.out.println ("[RM - Process] There is no such a rule");
		
		return delete;
	}
	
	// return rules on specific node.
	public LinkedList<Rule> searchRules (String node)
	{
		LinkedList<Rule>	searchRule = new LinkedList<Rule>();
		ListIterator<Rule>	iterator = rules.listIterator();
		
		while (iterator.hasNext()) 
		{
			Rule rule = iterator.next();
			if (rule.isRuleOn(node))
				searchRule.add(rule);			
		}
		
		return searchRule;
	}
	
	public LinkedList<Rule> getWholeRule ()
	{
		return rules;
	}
	
	public void setMode (String value) throws InvalidRuleException
	{
		// TODO - hard card value
		mode = "*@0010==" + value.trim() + "#Alarm";
		activeRulesBasedOnMode(mode);
				
		// cancel all mode related action. 
		Scheduler.getInstance().cancelStateAction("*@0010");
		if (value.trim().equalsIgnoreCase("Set"))
		{
			Scheduler.getInstance().cancelMalFuncAction("*@0011=Malfunction#Message");
		}
		
		// run actions on mode setting.
		LinkedList<Action> actions = RuleSet.getInstance().getActions(mode);
		if (actions == null || actions.isEmpty())
		{
			System.out.println("[RM - Process] No rule for this condition." + mode);
			return;
		}
		Iterator<Action> iterator = actions.iterator();
		while (iterator.hasNext())
		{
			Action action = iterator.next();
			action.execute();
		}		
	}
	
	public String getMode ()
	{
		return mode;
	}
	
	public LinkedList<Action> getActions (String condition)
	{
		ListIterator<Rule>	iterator = rules.listIterator();
		
		while (iterator.hasNext()) 
		{
			LinkedList<Action> actions = iterator.next().getActions(condition);
			// find actions on condition.
			if (actions != null && !actions.isEmpty())
				return actions;
		}
		return null;
	}
	
	public void activeRules (String condition)
	{
		ListIterator<Rule>	iterator = rules.listIterator();
		
		while (iterator.hasNext()) 
		{
			iterator.next().activateRule(condition);;
		}
	}
	
	public void deactivateRules (String condition)
	{
		ListIterator<Rule>	iterator = rules.listIterator();
		
		while (iterator.hasNext()) 
		{
			iterator.next().deactiveRule(condition);;
		}
	}
	
	private void activeRulesBasedOnMode (String mode) throws InvalidRuleException
	{
		ListIterator<Rule>	iterator = rules.listIterator();
		
		while (iterator.hasNext()) 
		{
			Rule rule = iterator.next();
			rule.activeRuleBasedOnMode(mode);
	//		System.out.println("[RM - Process] active[" + rule.isActive() + "] rule - " + rule.getStatement());
		}
	}
	
	public boolean isAllow (Action action) throws InvalidRuleException
	{
		ListIterator<Rule>	iterator = rules.listIterator();
		
		while (iterator.hasNext()) 
		{
			LinkedList<Action> actions = iterator.next().getNoActions(mode);
			// find actions on condition.
			if (actions != null && !actions.isEmpty())
			{
				ListIterator<Action>	act_iterator = actions.listIterator();
				while (act_iterator.hasNext())
				{
					if (act_iterator.next().equals(action))
						return false;
				}
			}
		}
		return true;
	}	
}
