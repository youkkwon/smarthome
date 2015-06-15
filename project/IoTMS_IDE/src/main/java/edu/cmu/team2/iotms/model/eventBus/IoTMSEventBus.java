package edu.cmu.team2.iotms.model.eventBus;

import org.json.simple.JSONObject;

import com.google.common.eventbus.EventBus;

public class IoTMSEventBus extends EventBus {

	private static IoTMSEventBus eh = new IoTMSEventBus();
	
	private IoTMSEventBus()
	{
	}
	
	public static IoTMSEventBus getInstance()
	{
		return eh;
	}
	
	public void postEvent(JSONObject msg) {				
		// post event.
		//System.out.println ("[EventBus] postEvent " + msg);
		post(msg);
	}
}
