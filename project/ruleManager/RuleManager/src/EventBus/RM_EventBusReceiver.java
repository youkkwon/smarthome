package EventBus;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import RM_Event.ActionEvent;
import RM_Event.Event;
import RM_Event.NodeEvent;
import RM_Event.RuleEvent;
import RM_Event.StateEvent;
import RM_Event.ThingEvent;

import com.google.common.eventbus.Subscribe;

// EventBus 와 이벤트를 주고 받는 것을 담당. 
public class RM_EventBusReceiver extends Thread {

	protected JSONObject 	JSONMsg;
	private	JSONArray		targets;
	private String			ID;
	
	public RM_EventBusReceiver() 
	{
		JSONMsg = new JSONObject();
		ID = "RuleManager";
		IoTMSEventBus.getInstance().register(this);
	}

	@Subscribe
	public void ProcessEvent(JSONObject JSONMsg)
	{
		System.out.println ("[EventBus] ProcessEvent : " + JSONMsg);
		
		targets = (JSONArray) JSONMsg.get("Targets");
		for (int i=0; i < targets.size(); i++)
		{
			if (targets.get(i).equals(ID))
				ProcessRuleManagerEvent(JSONMsg);			
		}
	}
	
	private void ProcessRuleManagerEvent (JSONObject JSONMsg)
	{	
		System.out.println ("[EventBus] Process RuleManager Event : " + JSONMsg);
		
		Event event = null;
		String job = (String) JSONMsg.get("Job");
		
		if (job.equalsIgnoreCase("Alarm") || job.equalsIgnoreCase("MessageCtrl"))
			event = new StateEvent(JSONMsg);
		else if (job.equalsIgnoreCase("RuleCtrl"))
			event = new RuleEvent(JSONMsg);
		else if (job.equalsIgnoreCase("NodeCtrl"))
			event = new NodeEvent(JSONMsg);
		else if (job.equalsIgnoreCase("ActionCtrl"))
			event = new ActionEvent(JSONMsg);
		else if (job.equalsIgnoreCase("ThingCtrl"))
			event = new ThingEvent(JSONMsg);
		else
		{
			System.out.println ("Not valid event, ignore it.");
			return;
		}
		event.start();		
	}	
}
