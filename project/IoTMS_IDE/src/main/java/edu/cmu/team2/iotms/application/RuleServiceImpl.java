package edu.cmu.team2.iotms.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.google.common.eventbus.Subscribe;

import edu.cmu.team2.iotms.domain.RuleInfo;
import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;
import edu.cmu.team2.iotms.model.eventBus.NM_EventBusReceiver;
import edu.cmu.team2.iotms.model.eventBus.RM_EventBusReceiver;
import edu.cmu.team2.iotms.model.nodeManager.NodeManager;
import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Scheduler;
import edu.cmu.team2.iotms.model.ruleManager.RM_Event.RuleManager;

public class RuleServiceImpl implements RuleService {
	private JdbcTemplate jdbcTemplate;
	private List<RuleInfo> ruleSet = new ArrayList<RuleInfo>();

	public RuleServiceImpl(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
//*		
		RM_EventBusReceiver 		rm 		= new RM_EventBusReceiver();
		NM_EventBusReceiver 		nm 		= new NM_EventBusReceiver();
		
		// RuleManager
		RuleManager 				rulemanager	= RuleManager.getInstance();		
		Scheduler 					rm_scheduler= Scheduler.getInstance();
		
		rulemanager.start();							
		rm_scheduler.start();
		
		// NodeManager
		NodeManager 				nodemanager	= NodeManager.getInstance();	
		
		JSONObject msgJSON = new JSONObject();
		JSONArray target = new JSONArray();
		target.add("RuleManager");
		msgJSON.put("Targets",target);
		msgJSON.put("Job", "RuleCtrl");
		msgJSON.put("Type", "Search");
		
		IoTMSEventBus.getInstance().register(this);
		IoTMSEventBus.getInstance().postEvent(msgJSON);
//*/
	}

	@Override
	public List<RuleInfo> getRuleset() {
/*
		String sql = "SELECT ruleset_id, ruleset FROM ruleset_info";
		System.out.println("getRules sql : "+sql);
		List<RuleInfo> ruleSet = jdbcTemplate.query(sql, new RowMapper<RuleInfo>() {
	        @Override
	        public RuleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	RuleInfo aRuleSet = new RuleInfo();
	        	
	        	aRuleSet.setRuleId(rs.getString("ruleset_id"));
	        	aRuleSet.setRuleSet(rs.getString("ruleset"));
	        	
	        	return aRuleSet;
	        }
	    });
	 
	    return ruleSet;
//*/
		return ruleSet;
	}

	@Override
	public void createRuleSet(RuleInfo ruleInfo) {
//		String sql = "insert user_info(ruleset) values('"+ruleInfo.getRuleSet()+"') ";
//		System.out.println("createRuleset sql : "+sql);
//		jdbcTemplate.execute(sql);
		
		JSONObject msgJSON = new JSONObject();
		JSONArray target = new JSONArray();
		target.add("RuleManager");
		msgJSON.put("Targets",target);
		msgJSON.put("Job", "RuleCtrl");
		msgJSON.put("Type", "Add");
		msgJSON.put("Rule", ruleInfo.getRuleSet());
		
		IoTMSEventBus.getInstance().postEvent(msgJSON);
		
		msgJSON = new JSONObject();
		target = new JSONArray();
		target.add("RuleManager");
		msgJSON.put("Targets",target);
		msgJSON.put("Job", "RuleCtrl");
		msgJSON.put("Type", "Search");
		IoTMSEventBus.getInstance().postEvent(msgJSON);
	}

	@Override
	public void removeRuleSet(RuleInfo ruleInfo) {
//		String sql = "delete from ruleset_info where ruleset_id='"+ruleInfo.getRuleId()+"' ";
//		System.out.println("removeRuleSet sql : "+sql);
//		jdbcTemplate.execute(sql);
		
		JSONObject msgJSON = new JSONObject();
		JSONArray target = new JSONArray();
		target.add("RuleManager");
		msgJSON.put("Targets",target);
		msgJSON.put("Job", "RuleCtrl");
		msgJSON.put("Type", "Delete");
		msgJSON.put("Rule", ruleInfo.getRuleSet());
		
		IoTMSEventBus.getInstance().postEvent(msgJSON);
		
		msgJSON = new JSONObject();
		target = new JSONArray();
		target.add("RuleManager");
		msgJSON.put("Targets",target);
		msgJSON.put("Job", "RuleCtrl");
		msgJSON.put("Type", "Search");
		IoTMSEventBus.getInstance().postEvent(msgJSON);
	}

	@Override
	public void updateRuleSet(RuleInfo ruleInfo) {
		// TODO Auto-generated method stub
		
	}
	
	@Subscribe
	public void SubscribeEvent(JSONObject JSONMsg) {
		JSONArray targets = (JSONArray) JSONMsg.get("Targets");
		for (int i=0; i < targets.size(); i++) {
			if (targets.get(i).equals("UIControler")) {
				if(JSONMsg.get("Job").toString().compareTo("RuleSearch") == 0)
					processSearch(JSONMsg);
			}
		}
	}

	private void processSearch(JSONObject JSONMsg) {
		JSONArray rules = (JSONArray) JSONMsg.get("Rules");
		
		ruleSet.clear();
		for (int i=0; i < rules.size(); i++) {
			RuleInfo ruleinfo = new RuleInfo();
			ruleinfo.setRuleId(String.format("%d", i));
			ruleinfo.setRuleSet(rules.get(i).toString());
			
			System.out.println("ruleset : "+ruleinfo.getRuleSet());
			ruleSet.add(ruleinfo);
		}
	}

}
