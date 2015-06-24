package edu.cmu.team2.iotms.model.ruleManager.RM_Event;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;
import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Rule;
import edu.cmu.team2.iotms.model.ruleManager.RM_Core.RuleSet;
import edu.cmu.team2.iotms.model.utility.InvalidRuleException;

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
		System.out.println("[RM - Process] Handle RuleEvent : " + JSONMsg);
		RuleSet ruleset = RuleSet.getInstance();
		String type = (String) JSONMsg.get("Type");
		
		if (type.equalsIgnoreCase("Add") || type.equalsIgnoreCase("Delete"))
		{
			JSONObject	JSONOutMsg = new JSONObject();
			
			JSONArray 	targets = new JSONArray();
			targets.add("UIControler");
			
			JSONOutMsg.put("Targets", targets);
			JSONOutMsg.put("Job", "RuleCtrl");			
				
			String rule = (String) JSONMsg.get("Rule");
			if (type.equalsIgnoreCase("Add"))
			{
				try {
					ruleset.addRule(rule);
					JSONOutMsg.put("Result", "Success");
					JSONOutMsg.put("Desc", "N/A");
				} catch (InvalidRuleException e) {
					System.out.println(e.getExceptionMsg());
					JSONOutMsg.put("Result", "Fail");
					JSONOutMsg.put("Desc", e.getExceptionMsg());
				}
			}
			else if (type.equalsIgnoreCase("Delete"))
			{
				try {
					boolean result = ruleset.deleteRule(rule);
					if (result)
					{
						JSONOutMsg.put("Result", "Success");
						JSONOutMsg.put("Desc", "N/A");
					}
					else
					{
						JSONOutMsg.put("Result", "Fail");
						JSONOutMsg.put("Desc", "There is no rule");
					}
				} catch (InvalidRuleException e) {
					JSONOutMsg.put("Result", "Fail");
					JSONOutMsg.put("Desc", e.getExceptionMsg());
				}
			}			
			//System.out.println("[RM - Process] Add/Delete Rule : " + JSONOutMsg);
			
			// post event.
			IoTMSEventBus.getInstance().postEvent(JSONOutMsg);
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
				rules.add(rule.getStatement());
			}
			
			JSONOutMsg.put("Targets", targets);
			JSONOutMsg.put("Job", "RuleSearch");			
			JSONOutMsg.put("Rules", rules);
					
			//System.out.println("[RM - Process] SearchRule : " + JSONOutMsg);
			
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
				rules.add(rule.getStatement());
			}
			
			JSONOutMsg.put("Targets", targets);
			JSONOutMsg.put("Job", "RuleSearch");			
			JSONOutMsg.put("Rules", rules);
			
			//System.out.println("[RM - Process] SearchRule : " + JSONOutMsg);
					
			// post event.
			IoTMSEventBus.getInstance().postEvent(JSONOutMsg);
		}
	}
}
