package RM_Core;

// {Condition} := {NodeID}@{Thing ID}=={Value}{Type}
public class Condition {
	
	protected String 		statement;			// ex) If alarm mode (Mode == 1), If Light one (Light ID == 1)
	private boolean 		modeOnly;			// Mode condition
	
	public Condition (String nodeID, String thingID, String value, String type)
	{
		statement 		= nodeID + ":" + thingID + "==" + value;
		modeOnly 		= type.equalsIgnoreCase("Alarm");	// Condition Type is alarm mode.
	}
	
	public String getStatement()
	{
		return statement;
	}
	
	public boolean isConditionOn (String cond)
	{
		return (statement.startsWith(cond));
	}
	
	// for cancel action
	public boolean equals (Condition condition) 
	{
		return condition.isConditionMatch(statement);
	}
		
	public boolean isConditionMatch (String cond)
	{
		return statement.equalsIgnoreCase(cond);		
	}

	public boolean isModeCond()
	{
		return modeOnly;
	}
	
}
