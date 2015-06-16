package edu.cmu.team2.iotms.model.eventBus;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;

import edu.cmu.team2.iotms.model.nodeManager.NodeManager;

// EventBus 와 이벤트를 주고 받는 것을 담당. 
public class NM_EventBusReceiver extends Thread {

	protected JSONObject 	JSONMsg;
	private	JSONArray		targets;
	private String			ID;
	
	public NM_EventBusReceiver() 
	{
		JSONMsg = new JSONObject();
		ID = "NodeManager";
		IoTMSEventBus.getInstance().register(this);
	}

	@Subscribe
	public void ProcessEvent(JSONObject JSONMsg)
	{
		targets = (JSONArray) JSONMsg.get("Targets");
		for (int i=0; i < targets.size(); i++)
		{
			if (targets.get(i).equals(ID))
				ProcessNodeManagerEvent(JSONMsg);			
		}
	}
	
	private void ProcessNodeManagerEvent (JSONObject JSONMsg)
	{	
		System.out.println ("[EventBus] Process NodeManager Event : " + JSONMsg);
		
		// Receive Event
		String job = (String) JSONMsg.get("Job");
		
		if (job.equalsIgnoreCase("ThingMonitor"))
			NodeManager.getInstance().showThingInfo(JSONMsg);
		else if (job.equalsIgnoreCase("NodeMonitor"))
			NodeManager.getInstance().showNodeInfo(JSONMsg);
		else if (job.equalsIgnoreCase("ActionCtrl"))
			NodeManager.getInstance().doCommand(JSONMsg);
		else if (job.equalsIgnoreCase("Discover"))
			NodeManager.getInstance().discoverNode(JSONMsg);
		else if (job.equalsIgnoreCase("Register"))
			NodeManager.getInstance().registerNode(JSONMsg);
		else
		{
			System.out.println ("Not valid event, ignore it.");
			return;
		}
	}	
}
