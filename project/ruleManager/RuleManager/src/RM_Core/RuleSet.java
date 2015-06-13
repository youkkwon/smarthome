package RM_Core;

import java.util.LinkedList;
import java.util.ListIterator;

import RM_Storage.RuleSetFileStorage;
import RM_Utils.InvalidRuleException;

// Singleton
public class RuleSet {

	private LinkedList<Rule> 	rules;
	private String				mode;			// TODO - class 로 뺄까? 사실 한개 뿐인데 observer 로 할 필요는 없을 듯 하다.
	
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
		int		idx = 0, beginIndex = 0, endIndex = 0;
		
		ListIterator<String> raw_rules = RuleSetFileStorage.getInstance().loadRuleSet();
		
		while (raw_rules.hasNext())
		{
			String input = raw_rules.next();
			 
			idx = input.indexOf("then");
			String condStr = input.substring(2, idx).trim();
			String actStr = input.substring(idx+4).trim();

			String[] conditions = new String[countChar(condStr, ',')+1];
			String[] actions = new String[countChar(actStr, ',')+1];
			
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
			try {
				createRule (conditions, actions);
			} catch (InvalidRuleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static RuleSet getInstance()
	{
		return ruleset;		
	}
	
	private void createRule (String[] condStr, String[] actStr) throws InvalidRuleException
	{
		Rule rule = new Rule(condStr, actStr);
		rules.add(rule);
	}
	
	public void addRule (String[] condStr, String[] actStr) 
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
		}
		} catch (InvalidRuleException e) {
			System.out.println(e.getExceptionMsg());
		}
		
		RuleSetFileStorage.getInstance().storeRuleSet(rules);
	}
	
	public boolean deleteRule (String[] condStr, String[] actStr)
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
					break;
				}
				else if (rule.isSameCondition(condStr))
				{
					rule.deleteActions(actStr);
					delete = true;
					break;
				}
			}
		} catch (InvalidRuleException e) {
			System.out.println(e.getExceptionMsg());
		}
		
		if (delete) {
			RuleSetFileStorage.getInstance().storeRuleSet(rules);			
		}
		else
			System.out.println ("There is no such a rule");
		
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
	
	public void setMode (String mode) throws InvalidRuleException
	{
		// TODO - hard card value
		this.mode = "*@10==" + mode.trim() + "#Alarm";
		activeRulesBasedOnMode(this.mode);
		
		if (mode.equalsIgnoreCase("UnSet"))
			Scheduler.getInstance().cancelStateAction();
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
			iterator.next().activeRuleBasedOnMode(mode);
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
