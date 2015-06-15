package EventBus;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;

// EventBus �� �̺�Ʈ�� �ְ� �޴� ���� ���. 
public class Message_EventBusReceiver extends Thread {

	protected JSONObject 	JSONMsg;
	private	JSONArray		targets;
	private String			ID;
	
	public Message_EventBusReceiver() 
	{
		JSONMsg = new JSONObject();
		ID = "Message";
		IoTMSEventBus.getInstance().register(this);
	}

	@Subscribe
	public void ProcessEvent(JSONObject JSONMsg)
	{		
		targets = (JSONArray) JSONMsg.get("Targets");
		for (int i=0; i < targets.size(); i++)
		{
			if (targets.get(i).equals(ID))
				ProcessCommManagerEvent(JSONMsg);			
		}
	}
	
	private void ProcessCommManagerEvent (JSONObject JSONMsg)
	{	
		System.out.println ("[EventBus] Process Message Event : " + JSONMsg);	
	}	
}
