package edu.cmu.team2.iotms.application;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import Database.NodeDao;

import com.google.common.eventbus.Subscribe;

import edu.cmu.team2.iotms.domain.NodeInfo;
import edu.cmu.team2.iotms.domain.RuleInfo;
import edu.cmu.team2.iotms.domain.ThingInfo;
import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;

public class NodeServiceImpl implements NodeService {
	private JdbcTemplate jdbcTemplate;
	private List<NodeInfo> discoverNodes = new ArrayList<NodeInfo>();

	public NodeServiceImpl(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
		
		IoTMSEventBus.getInstance().register(this);
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

	@Override
	public List<NodeInfo> getNewNodes() {
		return discoverNodes;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void discoverNodes() {
		JSONObject msgJSON = new JSONObject();
		JSONArray target = new JSONArray();
		target.add("NodeManager");
		msgJSON.put("Targets",target);
		msgJSON.put("Job", "Discover");
		msgJSON.put("Duration", "30000");
		
		discoverNodes.clear();
		IoTMSEventBus.getInstance().postEvent(msgJSON);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void registerNode(String nodeid, String serial) {
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
	
	@Subscribe
	public void SubscribeEvent(JSONObject JSONMsg) {
		JSONArray targets = (JSONArray) JSONMsg.get("Targets");
		for (int i=0; i < targets.size(); i++) {
			if (targets.get(i).equals("UIControler")) {
				if(JSONMsg.get("Job").toString().compareTo("Discovered") == 0)
					processSearch(JSONMsg);
			}
		}
	}

	private void processSearch(JSONObject JSONMsg) {
		JSONObject node = (JSONObject) JSONMsg.get("NodeID");
		
		NodeDao.getInstance().setJsonOfNode(node.toString(), JSONMsg.toString());
		
		NodeInfo newNode = new NodeInfo();
		newNode.setNode_id(node.toString());
		
		JSONArray thinglist = (JSONArray) JSONMsg.get("ThingList");
		List<ThingInfo> things = new ArrayList<ThingInfo>();
		for (int i=0; i < thinglist.size(); i++) {
			ThingInfo thing = new ThingInfo();
			JSONObject thingobj = (JSONObject) thinglist.get(i);
			
			thing.setNode_id(node.get(i).toString());
			thing.setThing_id(thingobj.get("Id").toString());
			thing.setType(thingobj.get("Type").toString());
			thing.setStype(thingobj.get("SType").toString());
			thing.setVtype(thingobj.get("VType").toString());
			thing.setVmin(thingobj.get("VMin").toString());
			thing.setVmax(thingobj.get("VMax").toString());
			
			things.add(thing);
		}
		
		newNode.setThings(things);
		
		System.out.println("discoverNodes : "+newNode);
		discoverNodes.add(newNode);
	}
}
