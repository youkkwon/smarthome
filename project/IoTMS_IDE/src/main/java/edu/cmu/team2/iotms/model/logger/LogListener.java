package edu.cmu.team2.iotms.model.logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;

public class LogListener {

	@Subscribe
	public void ListenEvent(JSONObject JSONMsg)
	{
		// ThingMonitor, ThingCtrl
		// Message
		String job = JSONMsg.get("Job").toString().toUpperCase();
				
		switch(job) {
		//case "THINGMONITOR":
		case "THINGCTRL":
			// Thing value, event History
			String nodeID = JSONMsg.get("NodeID").toString();
									
			JSONArray thingInfos = (JSONArray) JSONMsg.get("Status");
//			for (int i=0; i < thingInfos.size(); i++)
//			{
//				JSONObject thingObj = (JSONObject) thingInfos.get(i);
//				String thingID = (String)thingObj.get("Id");
//				String thingType = (String)thingObj.get("Type");
//				String thingValue = (String)thingObj.get("Value");
//			}
			LogManager.getInstance().pushLog("NodeID:"+nodeID+" Status:"+thingInfos.toString());
			break;
		case "REGISTER":
			// event History
			String nodeId = JSONMsg.get("NodeID").toString();
			String serial = JSONMsg.get("SerialNumber").toString();
			LogManager.getInstance().pushLog("Register node:"+nodeId+" serial:"+serial);
			break;
		default:
			break;	
		}
		
		
		
	}	
}
