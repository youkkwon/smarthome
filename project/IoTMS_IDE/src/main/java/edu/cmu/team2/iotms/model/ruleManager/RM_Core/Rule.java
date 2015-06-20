package edu.cmu.team2.iotms.model.ruleManager.RM_Core;

import java.util.LinkedList;
import java.util.ListIterator;

import edu.cmu.team2.iotms.model.utility.InvalidRuleException;

// {Rule} := if {Condition}+ then {Action}+
public class Rule {
	
	private boolean 	active;	
	private String		condStatement;
	private String 		actStatement;
	private String 		statement;
	
	private LinkedList <Condition> conditionList;
	private LinkedList <Action> noActionList;
	private LinkedList <Action> actionList;
	
	// statement 
	public Rule (String[] condStr, String[] actString)  throws InvalidRuleException 
	{
		active = true;
			
		conditionList	= new LinkedList<Condition>();
		actionList 		= new LinkedList<Action>();
		noActionList 	= new LinkedList<Action>();
			
		generateRule(condStr, actString);
	}

	public String getStatement()
	{
		updateStatement();
		return statement;
	}
	
	// for delete rule
	public boolean isSameRule(String[] condStr, String[] actString)
	{	
		String condStatement = condStr[0];		
		for (int i = 1; i < condStr.length; i++)
			condStatement = condStatement + ", " + condStr[i];
		
		String actStatement = actString[0];		
		for (int i = 1; i < actString.length; i++)
			actStatement = actStatement + ", " + actString[i];
		
		String statement = "if " + condStatement + " then " + actStatement;
				
		return this.statement.startsWith(statement);
	}
	
	// for update rule (add more actions on condition)
	public boolean isSameCondition (String[] condStr)
	{
		String condStatement = condStr[0];
		for (int i = 1; i < condStr.length; i++)
		{
			condStatement = condStatement + ", " + condStr[i];
		}
		
		return this.condStatement.equalsIgnoreCase(condStatement);
	}
	
	private void updateStatement()
	{
		ListIterator<Action>	act_iterator;
		ListIterator<Condition> cond_iterator = conditionList.listIterator();
		
		condStatement = null;
		actStatement = null;
		
		if (cond_iterator.hasNext())
			condStatement = cond_iterator.next().getStatement();
		while (cond_iterator.hasNext())
		{
			condStatement = condStatement + ", " + cond_iterator.next().getStatement();
		}
				
		act_iterator = actionList.listIterator();
		if (act_iterator.hasNext())
			actStatement = act_iterator.next().getStatement();
		while (act_iterator.hasNext())
		{
			actStatement = actStatement + ", " + act_iterator.next().getStatement();
		}
		
		act_iterator = noActionList.listIterator();
		if (act_iterator.hasNext()) 
		{
			if ( actStatement == null)
				actStatement = " !" + act_iterator.next().getStatement();
			else
				actStatement = actStatement + ", !" + act_iterator.next().getStatement();
		}
		while (act_iterator.hasNext())
		{
			actStatement = actStatement + ", !" + act_iterator.next().getStatement();
		}
		
		statement = "if " + condStatement + " then " + actStatement;
	}
	
	public boolean insertIfNotExist (Action action, boolean not_action) throws InvalidRuleException
	{
		ListIterator<Action>	iterator;
		
		if (not_action)
			iterator = noActionList.listIterator();
		else
			iterator = actionList.listIterator();
		
		while (iterator.hasNext())
		{
			Action existAction = iterator.next();	
			if (action.equals(existAction))
			{
				InvalidRuleException exception = new InvalidRuleException ("Caution : same action already exist.");
				// exception - state condition can't have two 
				throw exception;
			}
			if (action.isConflict(existAction))
			{
				InvalidRuleException exception = new InvalidRuleException ("Conflict : same condition but different action already exist.");
				// exception - state condition can't have two 
				throw exception;
			}
		}
		if (not_action)
			noActionList.add(action);
		else
			actionList.add(action);
		
		updateStatement();
		return true;
	}
	
	public boolean deleteIfExist (Action action, boolean not_action) throws InvalidRuleException
	{
		ListIterator<Action>	iterator;
		
		if (not_action)
			iterator = noActionList.listIterator();
		else
			iterator = actionList.listIterator();
		
		while (iterator.hasNext())
		{
			Action existAction = iterator.next();	
			if (action.equals(existAction))
			{
				if (not_action)			
					noActionList.remove(existAction);
				else
					actionList.remove(existAction);
				
				if (noActionList.isEmpty() && actionList.isEmpty())
				{
					InvalidRuleException exception = new InvalidRuleException ("Empty Rule, no Actions on this condition.");
					// exception - state condition can't have two 
					throw exception;
				}
				updateStatement();
				return true;
			}
		}
		return false;
	}
	
	public Action parseAction (String statement) throws InvalidRuleException
	{
		int 	startIndex, endIndex;
		String 	NodeID, ThingID, Value, Type;
		Action action;
		
		startIndex = (statement.startsWith("!")) ? 1 : 0;
		endIndex = statement.indexOf("@");
		if (startIndex == -1 || endIndex == -1)
		{
			InvalidRuleException exception = new InvalidRuleException ("Action statment is not valid");
			throw exception;
		}
		NodeID = statement.substring(startIndex, endIndex);
		
		startIndex = endIndex+1;
		endIndex = statement.indexOf("=");
		if (startIndex == -1 || endIndex == -1)
		{
			InvalidRuleException exception = new InvalidRuleException ("Action statment is not valid");
			throw exception;
		}
		ThingID = statement.substring(startIndex, endIndex);
		
		startIndex = endIndex+1;
		endIndex = statement.indexOf("#");
		if (startIndex == -1 || endIndex == -1)
		{
			InvalidRuleException exception = new InvalidRuleException ("Action statment is not valid");
			throw exception;
		}
		Value = statement.substring(startIndex, endIndex);

		startIndex = endIndex+1;
		endIndex = statement.indexOf("Delay");
		if (endIndex == -1)
			endIndex = statement.length();
		
		if (startIndex > statement.length())
		{
			InvalidRuleException exception = new InvalidRuleException ("Action statment is not valid");
			throw exception;
		}
		Type = statement.substring(startIndex, endIndex);		
		
		if ((statement.indexOf("Delay") != -1))
			action = new DelayAction(NodeID, ThingID, Value, Type);
		else 
			action = new InstantAction(NodeID, ThingID, Value, Type);
		
		return action;
	}
	
	public Condition parseCondition (String statement) throws InvalidRuleException
	{
		int 	startIndex, endIndex;
		String 	NodeID, ThingID, Value, Type;
	
		Condition condition;
		
		startIndex = 0;
		endIndex = statement.indexOf("@");
		if (startIndex == -1 || endIndex == -1)
		{
			InvalidRuleException exception = new InvalidRuleException ("Condition statment is not valid");
			throw exception;
		}
		NodeID = statement.substring(startIndex, endIndex);
		
		startIndex = endIndex+1;
		endIndex = statement.indexOf("==");
		if (startIndex == -1 || endIndex == -1)
		{
			InvalidRuleException exception = new InvalidRuleException ("Condition statment is not valid");
			throw exception;
		}
		ThingID = statement.substring(startIndex, endIndex);
		
		startIndex = endIndex+2;
		endIndex = statement.indexOf("#");
		if (startIndex == -1 || endIndex == -1)
		{
			InvalidRuleException exception = new InvalidRuleException ("Condition statment is not valid");
			throw exception;
		}
		Value = statement.substring(startIndex, endIndex);

		startIndex = endIndex+1;
		if (startIndex > statement.length())
		{
			InvalidRuleException exception = new InvalidRuleException ("Condition statment is not valid");
			throw exception;
		}
		Type = statement.substring(startIndex);		
		
		condition = new Condition(NodeID, ThingID, Value, Type);
		
		return condition;
	}
	
	public void addActions (String[] statement) throws InvalidRuleException
	{
		boolean not_action = false;
		
		for (int i=0; i < statement.length; i++)			
		{
			not_action = statement[i].startsWith("!") ? true : false;
			Action action = parseAction(statement[i]);
			insertIfNotExist (action, not_action);
		}
	}
	
	public void deleteActions (String[] statement) throws InvalidRuleException
	{
		boolean not_action = false;
		
		for (int i=0; i < statement.length; i++)			
		{
			not_action = statement[i].startsWith("!") ? true : false;
			Action action = parseAction(statement[i]);
			deleteIfExist (action, not_action);	
		}
	}
	
	// for search rule on certain node
	public boolean isRuleOn (String cond) 
	{
 		ListIterator<Condition>	iterator = conditionList.listIterator();
 		
		while (iterator.hasNext())
		{
			Condition condition = iterator.next();
			if (condition.isConditionOn(cond))
				return true;
		}
		return false;		
	}
	
	// Parse statement and conditions and actions. (JSON Parting)
	// Actions can be Instant and Delay one due to time condition presence.
	public void generateRule(String[] condStr, String[] actString) throws InvalidRuleException 
	{
		boolean alarmMode_condition = false; 
		boolean not_action = false;
		
		for (int i=0; i < condStr.length; i++)			
		{
			Condition condition = parseCondition(condStr[i]);
			if (condition.isAlarmModeCond())
				alarmMode_condition = true;
			conditionList.add(condition);
		}
		
		for (int i=0; i < actString.length; i++)			
		{
			not_action = actString[i].startsWith("!") ? true : false;
			Action action = parseAction(actString[i]);
			if (not_action)
				noActionList.add(action);
			else
				actionList.add(action);
		}
		if (alarmMode_condition)
			active = false;
		updateStatement();
	}

	// If a node become valid again (delete, broken), active all rules based on the node.
	// Notice be aware of mode info also.
	public void activateRule (String cond)
	{
		ListIterator<Condition>	iterator = conditionList.listIterator();
 		
		// condition check
		while (iterator.hasNext())
		{
			Condition condition = iterator.next();
			if (condition.isConditionOn(cond))
			{
				active = true;		
				//System.out.println("[RM - Process] Ruls is activated - " + statement);
				break;
			}
		}
	}
	
	// If a node become invalid (delete, broken), deactive all rules based on the node.
	public void deactiveRule (String cond)
	{
		ListIterator<Condition>	iterator = conditionList.listIterator();
 		
		// condition check
		while (iterator.hasNext())
		{
			if (iterator.next().isConditionOn(cond))
			{
				active = false;		
				//System.out.println("[RM - Process] Ruls is de-activated - " + statement);
				break;
			}
		}
	}
	
	public void activeRuleBasedOnMode (String mode) 
	{
		ListIterator<Condition>	iterator = conditionList.listIterator();
 		
		// condition check
		while (iterator.hasNext())
		{
			Condition condition = iterator.next();
			if (condition.isModeCond())
			{
				active = false;
				if (condition.isConditionMatch(mode))
				{
					active = true;
					break;
				}
			}			
		}
	}
	
	public boolean isActive ()
	{
		return active;
	}
	
	// return actions on condition(event) or no_action on condition(mode)
	public LinkedList<Action> getActions (String condition)
	{
		boolean match = false;
		ListIterator<Condition>	iterator = conditionList.listIterator();
		
		if (active == false) return null;
		
		// condition check
		while (iterator.hasNext())
		{
			if (iterator.next().isConditionMatch(condition) == true)
			{
				match = true;
				break;
			}
		}		
		
		return (match == true) ? actionList : null;
	}
	
	// Or condition. ex) Auto door control system blocked on several modes such as ourdoor, night, child-protect
	public  LinkedList<Action>getNoActions (String condition) throws InvalidRuleException
	{
		boolean match = false;
		ListIterator<Condition>	iterator = conditionList.listIterator();
		
		if (active == false) return null;
		
		// condition check
		while (iterator.hasNext())
		{
			if (iterator.next().isConditionMatch(condition) == true)
			{
				match = true;
				break;
			}
		}		
					
		return (match == true) ? noActionList : null;
	}	
}
