package RuleManager.RM_Core;

// {Condition} := {NodeID}@{Thing ID}=={Value}{Type}
public class Condition {
	
	private String			nodeID;
	protected String 		statement;			// ex) If alarm mode (Mode == 1), If Light one (Light ID == 1)
	private boolean 		modeOnly;			// Mode condition
	private boolean			alarmMode;			// AlarmMode condition
	
	public Condition (String nodeID, String thingID, String value, String type)
	{
		this.nodeID		= nodeID;
		statement 		= nodeID + "@" + thingID + "==" + value + "#" + type;
		modeOnly 		= type.equalsIgnoreCase("Alarm");	// Condition Type is alarm mode.
		alarmMode		= value.equalsIgnoreCase("Set");
	}
	
	public String getStatement()
	{
		return statement;
	}
	
	public boolean isConditionOn (String cond)
	{
		return nodeID.equalsIgnoreCase(cond);
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
	
	public boolean isAlarmModeCond()
	{
		return (modeOnly && alarmMode);
	}
}
