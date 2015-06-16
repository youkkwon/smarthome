package Tester;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class TestJSON {

	private String nodeID;
	
	public TestJSON(String nodeID)
	{
		this.nodeID = nodeID;
	}
	
	public void test() {
		// TODO Auto-generated method stub
		//String disc_resp_msg = "{\"Job\":\"Discovered\",\"NodeID\":\"78:c4:e:1:7f:f9\",\"list\":[{\"id\":\"0001\",\"type\":\"Door\",\"stype\":\"Actuator\"},{\"id\":\"0002\",\"type\":\"DoorSensor\",\"stype\":\"Sensor\"}]}";
		String disc_resp_msg = "{\"Job\":\"Discovered\",\"NodeID\":\"78:c4:e:1:7f:f9\",\"list\":[{\"id\":\"0001\",\"type\":\"Door\",\"stype\":\"Actuator\"},{\"id\":\"0002\",\"type\":\"DoorSensor\",\"stype\":\"Sensor\"}]}";
 		//String disc_resp_msg = "{\"Job\":\"Discovered\",\"NodeID\":\"78:c4:e:1:7f:f9\"}\n";
 		
		disc_resp_msg.replaceAll("12:23:34:45:56:67", nodeID);
		
		JSONObject jsonObj;
		JSONArray jsonObj_array;
		jsonObj = (JSONObject) JSONValue.parse(disc_resp_msg);
		
		System.out.println("[Tester - JSON] json to be parsed: " + disc_resp_msg);
		
		String tmp;
		tmp = (String) jsonObj.get("NodeID");
		System.out.println("[Tester - JSON] MAC Address : " + tmp);
		jsonObj_array = (JSONArray) jsonObj.get("list");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = jsonObj_array.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().toString());
			JSONObject jsonObj1;
			jsonObj1 = (JSONObject) JSONValue.parse(iterator.next().toString());
			String tmp1;
			tmp1 = (String) jsonObj1.get("stype");
			System.out.println("[Tester - JSON] json to be parsed: " + tmp1);
		}
 
	}

}
