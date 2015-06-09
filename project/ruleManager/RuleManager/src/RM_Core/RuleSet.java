package RM_Core;

import java.util.LinkedList;
import java.util.ListIterator;

import RM_Exception.InvalidRuleException;

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
	
	public void addRule (String[] condStr, String[] actStr) throws InvalidRuleException 
	{
		boolean 			exist = false; 
		ListIterator<Rule>	iterator = rules.listIterator();
		
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
			rules.add(new Rule(condStr, actStr));
	}
	
	public boolean deleteRule (String[] condStr, String[] actStr) throws InvalidRuleException
	{
		ListIterator<Rule>	iterator = rules.listIterator();
		
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
		
		InvalidRuleException exception = new InvalidRuleException ("There is no such a rule");
		throw exception;
		
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
		this.mode = mode.trim();
		activeRulesBasedOnMode(mode);
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
