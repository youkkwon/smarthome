package EventBus;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;

// EventBus �� �̺�Ʈ�� �ְ� �޴� ���� ���. 
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
	}	
}
