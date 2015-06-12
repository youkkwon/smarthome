package EventBus;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import RM_Event.RuleManager;

import com.google.common.eventbus.Subscribe;

// EventBus 와 이벤트를 주고 받는 것을 담당. 
public class RM_EventBusReceiver { //extends Observable implements Runnable {
	
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
		targets = (JSONArray) JSONMsg.get("Targets");
		for (int i=0; i < targets.size(); i++)
		{
			if (targets.get(i).equals(ID))
			{
				RuleManager.getInstance().pushEvent(JSONMsg);				
			}
		}
	}	
}
