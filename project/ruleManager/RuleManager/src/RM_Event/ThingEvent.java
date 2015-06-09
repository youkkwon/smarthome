package RM_Event;

import java.util.Iterator;
import java.util.LinkedList;

import org.json.simple.JSONObject;

import RM_Core.Action;
import RM_Core.Condition;

public class ThingEvent extends Event {

	public ThingEvent(JSONObject msg) {
		super(msg);
	}

	public void run()
	{
		System.out.println("[Process] Handle ThingEvent : " + JSONMsg);
		
		String nodeID = (String) JSONMsg.get("NodeID");
		String thingID = (String) JSONMsg.get("ThingID");
		String type = (String) JSONMsg.get("Type");
		String value = (String) JSONMsg.get("Value");
		
		Condition condition = new Condition (nodeID, thingID, value, type);
	
		LinkedList<Action> actions = ruleset.getActions(condition.getStatement());
		Iterator<Action> iterator = actions.iterator();
		while (iterator.hasNext())
			iterator.next().execute();		
	}
}
