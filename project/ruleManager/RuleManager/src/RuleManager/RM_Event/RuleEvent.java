package RuleManager.RM_Event;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import EventBus.IoTMSEventBus;
import RuleManager.RM_Core.Rule;
import RuleManager.RM_Core.RuleSet;

public class RuleEvent {

	private static RuleEvent ruleEvent = new RuleEvent();

	private RuleEvent() {
		
	}

	public static RuleEvent getInstance ()
	{
		return ruleEvent;
	}
	
	@SuppressWarnings("unchecked")
	public void execute(JSONObject JSONMsg)
	{
		System.out.println("[Process] Handle RuleEvent : " + JSONMsg);
		RuleSet ruleset = RuleSet.getInstance();
		String type = (String) JSONMsg.get("Type");
		
		if (type.equalsIgnoreCase("Add") || type.equalsIgnoreCase("Delete"))
		{
			String rule = (String) JSONMsg.get("Rule");
			if (type.equalsIgnoreCase("Add"))
			{
				ruleset.addRule(rule);
			}
			else if (type.equalsIgnoreCase("Delete"))
			{
				ruleset.deleteRule(rule);
			}			
		}
		else if (type.equalsIgnoreCase("Search"))
		{
			JSONObject	JSONOutMsg = new JSONObject();
				
			JSONArray 	targets = new JSONArray();
			targets.add("UIControler");
		
			JSONArray 	rules = new JSONArray();
			
			Iterator<Rule> iterator = ruleset.getWholeRule().iterator();
			while (iterator.hasNext())
			{
				Rule rule  = iterator.next();
				System.out.println("[Process] Rules [" + rule.isActive() + "] - " + rule.getStatement());
				rules.add(rule.getStatement());
				//rules.add(iterator.next().getStatement());
			}
			
			JSONOutMsg.put("Targets", targets);
			JSONOutMsg.put("Job", "RuleSearch");			
			JSONOutMsg.put("Rules", rules);
					
			System.out.println("[Process] SearchRule : " + JSONOutMsg);
			
			// post event.
			IoTMSEventBus.getInstance().postEvent(JSONOutMsg);
		}
		else if (type.equalsIgnoreCase("SearchNode"))
		{
			String nodeID = (String) JSONMsg.get("Value");
			
			JSONObject	JSONOutMsg = new JSONObject();
			
			JSONArray 	targets = new JSONArray();
			targets.add("UIControler");				
			
			JSONArray 	rules = new JSONArray();
			Iterator<Rule> iterator = ruleset.searchRules(nodeID).iterator();
			while (iterator.hasNext())
			{
				Rule rule  = iterator.next();
				System.out.println("[Process] Rules [" + rule.isActive() + "] - " + rule.getStatement());
				rules.add(rule.getStatement());
				//rules.add(iterator.next().getStatement());
			}
			
			JSONOutMsg.put("Targets", targets);
			JSONOutMsg.put("Job", "RuleSearch");			
			JSONOutMsg.put("Rules", rules);
			
			System.out.println("[Process] SearchRule : " + JSONOutMsg);
					
			// post event.
			IoTMSEventBus.getInstance().postEvent(JSONOutMsg);
		}
	}
}
