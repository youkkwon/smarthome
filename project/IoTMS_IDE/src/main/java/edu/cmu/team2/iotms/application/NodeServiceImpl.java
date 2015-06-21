package edu.cmu.team2.iotms.application;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import edu.cmu.team2.iotms.domain.NodeInfo;
import edu.cmu.team2.iotms.domain.ThingInfo;
import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;

public class NodeServiceImpl implements NodeService {
	private JdbcTemplate jdbcTemplate;
	//private List<NodeInfo> discoverNodes = new ArrayList<NodeInfo>();

	public NodeServiceImpl(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public List<NodeInfo> getNodeList(String register) {
		String sql = "select node_id,node_name,serialnumber from node_info where registered="+register;
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

	@Override
	public void removeNode(String nodeid) {
		String sql = "update node_info set registered=0 where node_id='"+nodeid+"'";
		//String sql = "delete from node_info where node_id='"+nodeid+"'";
		System.out.println("removeNode sql : "+sql);
		jdbcTemplate.update(sql);
		
//		JSONObject msgJSON = new JSONObject();
//		JSONArray target = new JSONArray();
//		target.add("RuleManager");
//		target.add("NodeManager");
//		msgJSON.put("Targets",target);
//		msgJSON.put("Job", "RemoveNode");
//		msgJSON.put("NodeID", nodeid);
//		
//		IoTMSEventBus.getInstance().postEvent(msgJSON);
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

	//@Override
	//public List<NodeInfo> getNewNodes() {
	//	return discoverNodes;
	//}
	
	@SuppressWarnings("unchecked")
	@Override
	public void discoverNodes() {
		String sql = "delete from node_info where registered=0";
		System.out.println("before discoverNodes sql : "+sql);
		jdbcTemplate.update(sql);
		
		JSONObject msgJSON = new JSONObject();
		JSONArray target = new JSONArray();
		target.add("NodeManager");
		msgJSON.put("Targets",target);
		msgJSON.put("Job", "Discover");
		msgJSON.put("Duration", "30000");
		
		//discoverNodes.clear();
		IoTMSEventBus.getInstance().postEvent(msgJSON);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void registerNode(String nodeid, String serial) {
		String sql = "update node_info set serialnumber='"+serial+"', registered=1 where node_id='"+nodeid+"'";
		System.out.println("registerNode sql : "+sql);
		jdbcTemplate.update(sql);
		
		String localIP="";
		try {
			localIP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		System.out.println("localIP :"+localIP);
		
		JSONObject msgJSON = new JSONObject();
		JSONArray target = new JSONArray();
		target.add("NodeManager");
		msgJSON.put("Targets",target);
		msgJSON.put("Job", "Register");
		msgJSON.put("NodeID", nodeid);
		msgJSON.put("URL", localIP);
		msgJSON.put("Port", "550");
		msgJSON.put("SerialNumber", serial);
		
		IoTMSEventBus.getInstance().postEvent(msgJSON);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void testNode(String nodeid, String thingid, String type,
			String value) {
		JSONObject msgJSON = new JSONObject();
		JSONArray target = new JSONArray();
		
		target.add("RuleManager");
		msgJSON.put("Targets",target);
		msgJSON.put("Job", "ThingCtrl");
		msgJSON.put("NodeID", nodeid);
		msgJSON.put("ThingID", thingid);
		msgJSON.put("Type", type);
		msgJSON.put("Value", value);
		
		IoTMSEventBus.getInstance().postEvent(msgJSON);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void testNodeDiscover(String nodeid) {
		JSONObject msgJSON = new JSONObject();
		JSONArray target = new JSONArray();
		
		JSONArray thinglist = new JSONArray();
		JSONObject thing = new JSONObject();
		thing.put("Id", "0001");
		thing.put("Type", "Door");
		thing.put("SType", "Actuator");
		thing.put("VType", "String");
		thing.put("VMin", "Open");
		thing.put("VMax", "Close");
		thinglist.add(thing);
		
		thing = new JSONObject();
		thing.put("Id", "0002");
		thing.put("Type", "Light");
		thing.put("SType", "Actuator");
		thing.put("VType", "String");
		thing.put("VMin", "On");
		thing.put("VMax", "Off");
		thinglist.add(thing);
		
		thing = new JSONObject();
		thing.put("Id", "0003");
		thing.put("Type", "Presence");
		thing.put("SType", "Sensor");
		thing.put("VType", "String");
		thing.put("VMin", "AtHome");
		thing.put("VMax", "Away");
		thinglist.add(thing);
		
		thing = new JSONObject();
		thing.put("Id", "0004");
		thing.put("Type", "Temperature");
		thing.put("SType", "Sensor");
		thing.put("VType", "Number");
		thing.put("VMin", "-50");
		thing.put("VMax", "50");
		thinglist.add(thing);
		
		thing = new JSONObject();
		thing.put("Id", "0005");
		thing.put("Type", "Humidity");
		thing.put("SType", "Sensor");
		thing.put("VType", "Number");
		thing.put("VMin", "0");
		thing.put("VMax", "100");
		thinglist.add(thing);
		
		thing = new JSONObject();
		thing.put("Id", "0006");
		thing.put("Type", "DoorSensor");
		thing.put("SType", "Sensor");
		thing.put("VType", "String");
		thing.put("VMin", "Open");
		thing.put("VMax", "Close");
		thinglist.add(thing);
		
		thing = new JSONObject();
		thing.put("Id", "0008");
		thing.put("Type", "Alarm");
		thing.put("SType", "Actuator");
		thing.put("VType", "String");
		thing.put("VMin", "Set");
		thing.put("VMax", "Unset");
		thinglist.add(thing);
		
		target.add("UIControler");
		target.add("UI");
		msgJSON.put("Targets",target);
		msgJSON.put("NodeID", nodeid);
		msgJSON.put("Job", "Discovered");
		msgJSON.put("ThingList", thinglist);
		
		IoTMSEventBus.getInstance().postEvent(msgJSON);
	}
}
