package edu.cmu.team2.iotms.model.ruleManager.RM_Core;

// {Condition} := {NodeID}@{Thing ID}=={Value}{Type}
public class Condition {
	
	private String			nodeID;
	private String			value;
	private String			type;
	protected String 		statement;			// ex) If alarm mode (Mode == 1), If Light one (Light ID == 1)
	private boolean 		modeOnly;			// Mode condition
	private boolean			alarmMode;			// AlarmMode condition
	private boolean			minMaxCond;			// Min, Max condition such as Humidity and Temperature
	
	public Condition (String nodeID, String thingID, String value, String type)
	{
		this.nodeID		= nodeID;
		this.value		= value;
		this.type		= type;
		statement 		= nodeID + "@" + thingID + "==" + value + "#" + type;
		modeOnly 		= type.equalsIgnoreCase("Alarm");	// Condition Type is alarm mode.
		alarmMode		= value.equalsIgnoreCase("Set");
		minMaxCond 		= (type.equalsIgnoreCase("Humidity") ||  type.equalsIgnoreCase("Temperature")) ? true : false;
	}
	
	public String getStatement()
	{
		return statement;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public String getType()
	{
		return type;
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
		boolean match = false;
	
		if (minMaxCond)
		{
			int startIndex, endIndex;
			
			startIndex = cond.indexOf("==") + 2;
			endIndex = cond.indexOf("#");
			String type = cond.substring(endIndex+1);
			if (type.equalsIgnoreCase("Humidity") || type.equalsIgnoreCase("Temperature")) 
			{
				String condStr = cond.substring(startIndex, endIndex);			
				
				float realValue = 0;
				if (condStr.indexOf(".") != -1)
				{
					realValue = Float.parseFloat(condStr);	//	cond.substring(startIndex, endIndex));
				}
				else
					realValue = Integer.parseInt(condStr);	//	cond.substring(startIndex, endIndex));
					
				startIndex = value.indexOf("Over");
				if (startIndex != -1)
				{
					int max = Integer.parseInt(value.substring(startIndex+4));
					if (realValue > max)
						match = true;
				}
				startIndex = value.indexOf("Under");
				if (startIndex != -1)
				{
					int min = Integer.parseInt(value.substring(startIndex+5));
					if (realValue < min)
						match = true;
				}
			}
			else
				match = false;
		}
		else 
			match = statement.equalsIgnoreCase(cond);	
		
		return match;	
	}

	public boolean isModeCond()
	{
		return modeOnly;
	}
	
	public boolean isAlarmModeCond()
	{
		return (modeOnly && alarmMode);
	}
	
	public boolean isMinMaxCond()
	{
		return minMaxCond;
	}
}
