package edu.cmu.team2.iotms.model.ruleManager.RM_Core;

import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;

//Singleton
public class Scalability extends Thread {

	private String nodeID = "";
	private static Scalability scheduler = new Scalability(); 

	private Calendar calendar = Calendar.getInstance( ); 
	
	private Scalability()
	{
		
	}
	
	public static Scalability getInstance()
	{
		return scheduler;		
	}
	
	// TODO expire 처리. 
	public void run()
	{
		int number = 0;
		
		while (true) 
		{			
			try {
				sendEvent(number++);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void execute(JSONObject JSONMsg)
	{
		System.out.println("Pong : " + JSONMsg.get("Value") + " Time : " + System.currentTimeMillis());
	}

	@SuppressWarnings("unchecked")
	private void sendEvent(int number)
	{
		JSONObject 	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("NodeManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "Ping");
		JSONMsg.put("NodeID",  nodeID);
		JSONMsg.put("Value", Integer.toString(number));	
		
		System.out.println("Ping : " + number + " Time : " + System.currentTimeMillis());
		
		// post event.
		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}
}
