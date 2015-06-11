package RM_Core;

import java.util.LinkedList;
import java.util.ListIterator;

import RM_Utils.InvalidRuleException;

// Singleton
public class RuleSet {

	private LinkedList<Rule> 	rules;
	private String				mode;			// TODO - class 로 뺄까? 사실 한개 뿐인데 observer 로 할 필요는 없을 듯 하다.
	
	private static RuleSet ruleset = new RuleSet();
	
	private RuleSet ()
	{
		rules = new LinkedList<Rule>();
	}
	
	public static RuleSet getInstance()
	{
		return ruleset;		
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
	}
	
	public boolean deleteRule (String[] condStr, String[] actStr)
	{
		ListIterator<Rule>	iterator = rules.listIterator();
		
		try {
		while (iterator.hasNext()) 
		{
			Rule rule = iterator.next();
			if (rule.isSameRule(condStr, actStr)) 
			{
				rules.remove(rule);
				return true;
			}
			else if (rule.isSameCondition(condStr))
			{
				rule.deleteActions(actStr);
				return true;
			}
		}
		
		} catch (InvalidRuleException e) {
			System.out.println(e.getExceptionMsg());
		}
		
		System.out.println ("There is no such a rule");
		return false;
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
		this.mode = "*@8==" + mode.trim() + "#Alarm";
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
