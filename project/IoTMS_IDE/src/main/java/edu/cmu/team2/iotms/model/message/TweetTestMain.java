package edu.cmu.team2.iotms.model.message;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TweetTestMain {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		IoTMSMessage iotmsmsg = new TwitterMessage();
		
		JSONObject msgJSON = new JSONObject();
		JSONArray target = new JSONArray();
		target.add("MESSAGE");
		JSONArray value = new JSONArray();
		value.add("POST");
		msgJSON.put("target",target);
		msgJSON.put("value",value);
		iotmsmsg.SubscribeMessage(msgJSON);
		
		
		msgJSON = new JSONObject();
		target = new JSONArray();
		target.add("MESSAGE");
		value = new JSONArray();
		value.add("MALFUNCTION");
		msgJSON.put("target",target);
		msgJSON.put("value",value);
		iotmsmsg.SubscribeMessage(msgJSON);
		
		
		msgJSON = new JSONObject();
		target = new JSONArray();
		target.add("MESSAGE");
		value = new JSONArray();
		value.add("EMERGENCY");
		msgJSON.put("target",target);
		msgJSON.put("value",value);
		iotmsmsg.SubscribeMessage(msgJSON);
		
		
		msgJSON = new JSONObject();
		target = new JSONArray();
		target.add("MESSAGE");
		value = new JSONArray();
		value.add("CONFIRM");
		msgJSON.put("target",target);
		msgJSON.put("value",value);
		iotmsmsg.SubscribeMessage(msgJSON);
	}

}