package RuleManager.RM_Core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import EventBus.IoTMSEventBus;

// {Action} := (!)? {NodeID}@{Thing ID}={Value}(in{value})?{Type}
public abstract class Action 
{
	private String nodeID;
	private String thingID;
	protected String type;
	private String value;
	
	private String 			actionID;			// nodeID:thingID
	protected String 		statement;
	protected JSONObject 	JSONMsg;
	
	public Action (String nodeID, String thingID, String value, String type)
	{
		JSONMsg			= new JSONObject();
		
		this.nodeID 	= nodeID;
		this.thingID 	= thingID; 
		this.value		= value;
		this.type		= type;
		actionID		= this.nodeID + "@" + this.thingID;
		statement 		= this.nodeID + "@" + this.thingID + "=" + this.value + "#" + this.type; 
	}

	public String getStatement()
	{
		return statement;
	}
	
	// for cancel action
	public boolean equals (Action action) 
	{
		return action.isActionMatch(statement);
	}
	
	public boolean isActionMatch (String action)
	{
		return statement.equalsIgnoreCase(action);
	}
		
	// indicate action is about which.
	public boolean isActionOn (String actionID)
	{
		return this.actionID.equalsIgnoreCase(actionID);
	}
	
	public boolean isConflict (Action action)
	{
		return action.isActionOn(actionID);
	}
	
	public boolean isActionOnType (String type)
	{
		return this.type.equalsIgnoreCase(type);
	}
	
	@SuppressWarnings("unchecked")
	protected void generateJSONMsg() 
	{
		JSONArray 	targets = new JSONArray();
		
		targets.add("Logger");
		if (type.equalsIgnoreCase("Message"))
		{
			targets.add("Message");
			JSONMsg.put("Targets", targets);
			JSONMsg.put("Job", "ReceiveMsg");
			JSONMsg.put("Type",  type);
			JSONMsg.put("Value", value);	
		}
		else
		{	
			targets.add("NodeManager");
			JSONMsg.put("Targets", targets);
			JSONMsg.put("Job", "ActionCtrl");
			JSONMsg.put("NodeID",  nodeID);
			JSONMsg.put("ThingID", thingID);
			JSONMsg.put("Type",  type);
			JSONMsg.put("Value", value);			
		}			
	}
	
	protected void postEvent() {				
		// post event.
	//	IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
		
	public abstract void execute();
	public abstract boolean isDelayAction();
}
