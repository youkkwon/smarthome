package RM_Event;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import EventBus.IoTMSEventBus;
import RM_Core.Rule;
import RM_Exception.InvalidRuleException;

public class RuleEvent extends Event {

	private static final String[][] String = null;

	public RuleEvent(JSONObject msg) {
		super(msg);
	}
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		System.out.println("[Process] Handle RuleEvent : " + JSONMsg);
		
		String type = (String) JSONMsg.get("Type");
		
		if (type.equalsIgnoreCase("Add") || type.equalsIgnoreCase("Delete"))
		{
			JSONArray condArray = (JSONArray) JSONMsg.get("Conditions");
			JSONArray actArray = (JSONArray) JSONMsg.get("Actions");
			if (condArray == null || actArray == null)
			{
				return;
			}
				
			String[] conditions = String[condArray.size()];
			for (int i=0; i < condArray.size(); i++)
			{
				conditions[i] = (java.lang.String) condArray.get(i);
			}
					
			String[] actions = String[actArray.size()];
			for (int i=0; i < actArray.size(); i++)
			{
				actions[i] = (java.lang.String) actArray.get(i);
			}
			
			if (type.equalsIgnoreCase("Add"))
			{
				try {
					ruleset.addRule(conditions, actions);
				} catch (InvalidRuleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (type.equalsIgnoreCase("Delete"))
				try {
					ruleset.deleteRule(conditions, actions);
				} catch (InvalidRuleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else if (type.equalsIgnoreCase("Search"))
		{
			JSONObject	JSONMsg = new JSONObject();
		
			JSONArray 	targets = new JSONArray();
			targets.add("UIControler");
			
			JSONArray 	rules = new JSONArray();
			Iterator<Rule> iterator = ruleset.getWholeRule().iterator();
			while (iterator.hasNext())
			{
				rules.add(iterator.next().getStatement());
			}
			
			JSONMsg.put("Targets", targets);
			JSONMsg.put("Job", "RuleSearch");			
			JSONMsg.put("Rules", rules);
					
			// post event.
			IoTMSEventBus.getInstance().postEvent(JSONMsg);
		}
	}
}
