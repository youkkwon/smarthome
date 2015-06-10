package RM_Event;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import org.json.simple.JSONObject;

//public class RMEventHandler implements Observer {
public class RMEventHandler {
	
	private LinkedList<JSONObject> events;
	private static RMEventHandler event = new RMEventHandler(); 
	
	private RMEventHandler()
	{
		events =  new LinkedList<JSONObject>();
	}
	
	public static RMEventHandler getInstance()
	{
		return event;		
	}
	
	public void update(Observable o, Object arg)
	{
		if (!events.isEmpty())
			processEvent();	
	}
	
	public void pushEvent (JSONObject JSONMsg)
	{
		events.add(JSONMsg);
	}
	
	public void processEventMsg (JSONObject JSONMsg)
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
	
	private void processEvent ()
	{	
		Iterator<JSONObject> iterator = events.iterator();
		while (iterator.hasNext())
		{
			JSONObject JSONMsg = (JSONObject) iterator.next();
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
			else
			{
				System.out.println ("Not valid event, ignore it.");
				return;
			}
			events.remove(JSONMsg);
		}	
	}	
}
