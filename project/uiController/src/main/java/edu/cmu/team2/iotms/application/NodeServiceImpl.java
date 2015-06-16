package edu.cmu.team2.iotms.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import EventBus.IoTMSEventBus;
import edu.cmu.team2.iotms.domain.NodeInfo;
import edu.cmu.team2.iotms.domain.ThingInfo;

public class NodeServiceImpl implements NodeService {
	private JdbcTemplate jdbcTemplate;

	public NodeServiceImpl(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);	
	}

	@Override
	public List<NodeInfo> getNodeList() {
		String sql = "select node_id,node_name,serialnumber from node_info ";
		System.out.println("getNodeList sql : "+sql);
		List<NodeInfo> nodelist = jdbcTemplate.query(sql, new RowMapper<NodeInfo>() {
	        @Override
	        public NodeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	NodeInfo aNode = new NodeInfo();
	        	
	        	aNode.setNode_id(rs.getString("node_id"));
	        	aNode.setNode_name(rs.getString("node_name"));
	        	aNode.setSerialnumber(rs.getString("serialnumber"));
	        		        	
	        	return aNode;
	        }
	    });
	 
	    return nodelist;
	}

	@Override
	public List<ThingInfo> getThingList(String node_id) {
		String sql = "select node_id,thing_id,thing_name,type,stype,vtype,vmin,vmax,value from thing_info "
				+"where node_id = '"+node_id+"'";
		System.out.println("getThingList sql : "+sql);
		List<ThingInfo> thinglist = jdbcTemplate.query(sql, new RowMapper<ThingInfo>() {
	        @Override
	        public ThingInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	ThingInfo aThing = new ThingInfo();
	        	aThing.setNode_id(rs.getString("node_id"));
	        	aThing.setThing_id(rs.getString("thing_id"));
	        	aThing.setThing_name(rs.getString("thing_name"));
	        	aThing.setType(rs.getString("type"));
	        	aThing.setStype(rs.getString("stype"));
	        	aThing.setVtype(rs.getString("vtype"));
	        	aThing.setVmin(rs.getString("vmin"));
	        	aThing.setVmax(rs.getString("vmax"));
	        	aThing.setValue(rs.getString("value"));
	        	        		        	
	        	return aThing;
	        }
	    });
	 
	    return thinglist;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void controlThing(String nodeid, String thingid, String type,
			String value) {
		
		JSONObject msgJSON = new JSONObject();
		JSONArray target = new JSONArray();
		target.add("RuleManager");
		msgJSON.put("Targets",target);
		msgJSON.put("Job", "ActionCtrl");
		msgJSON.put("NodeID", nodeid);
		msgJSON.put("ThingID", thingid);
		msgJSON.put("Type", type);
		msgJSON.put("Value", value);
		
		IoTMSEventBus.getInstance().postEvent(msgJSON);
		
		if(type.compareToIgnoreCase("alarm")==0) {
			msgJSON = new JSONObject();
			target = new JSONArray();
			target.add("RuleManager");
			msgJSON.put("Targets",target);
			msgJSON.put("Job", "Alarm");
			msgJSON.put("Value", value);
			
			IoTMSEventBus.getInstance().postEvent(msgJSON);
		}
		
	}
}
