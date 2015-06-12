package RM_Event;

import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;

public class RuleManager extends Thread {
	
	private static RuleManager event = new RuleManager(); 
	private LinkedBlockingQueue<JSONObject> events;
	
	private RuleManager()
	{
		events =  new LinkedBlockingQueue<JSONObject>();
	}
	
	public static RuleManager getInstance()
	{
		return event;		
	}
	
	public void pushEvent (JSONObject JSONMsg)
	{
		events.add(JSONMsg);
	}
	
	private void processEventMsg (JSONObject JSONMsg)
	{	
		String job = (String) JSONMsg.get("Job");
				
		if (job.equalsIgnoreCase("Alarm") || job.equalsIgnoreCase("MessageCtrl"))
			StateEvent.getInstance().execute(JSONMsg);
		else if (job.equalsIgnoreCase("RuleCtrl"))
			RuleEvent.getInstance().execute(JSONMsg);
		else if (job.equalsIgnoreCase("NodeCtrl"))
			NodeEvent.getInstance().execute(JSONMsg);
		else if (job.equalsIgnoreCase("ActionCtrl"))
			ActionEvent.getInstance().execute(JSONMsg);
		else if (job.equalsIgnoreCase("ThingCtrl"))
			ThingEvent.getInstance().execute(JSONMsg);
		else if (job.equalsIgnoreCase("ConfigCtrl"))
			ConfigEvent.getInstance().execute(JSONMsg);
		else
			System.out.println ("Not valid event, ignore it.");
		
		return;
	}

	public void run()
	{
		while (true)
		{
			JSONObject JSONMsg;
			try {
				JSONMsg = events.take();
				processEventMsg(JSONMsg);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}		
		}
	
	}	
}
