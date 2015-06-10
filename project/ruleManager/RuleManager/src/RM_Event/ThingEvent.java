package RM_Event;

import java.util.Iterator;
import java.util.LinkedList;

import org.json.simple.JSONObject;

import RM_Core.Action;
import RM_Core.Condition;
import RM_Core.RuleSet;

public class ThingEvent {

	private static ThingEvent thingEvent = new ThingEvent();
	
	private ThingEvent() { 
	}

	public static ThingEvent getInstance ()
	{
		return thingEvent;
	}
	
	public void execute(JSONObject JSONMsg)
	{
		System.out.println("[Process] Handle ThingEvent : " + JSONMsg);
		
		String nodeID = (String) JSONMsg.get("NodeID");
		String thingID = (String) JSONMsg.get("ThingID");
		String type = (String) JSONMsg.get("Type");
		String value = (String) JSONMsg.get("Value");
		
		Condition condition = new Condition (nodeID, thingID, value, type);
	
		LinkedList<Action> actions = RuleSet.getInstance().getActions(condition.getStatement());
		if (actions == null || actions.isEmpty())
		{
			System.out.println("No rule for this condition.");
			return;
		}
		Iterator<Action> iterator = actions.iterator();
		while (iterator.hasNext())
			iterator.next().execute();		
	}
}
