package edu.cmu.team2.iotms.domain;

import java.util.List;

public class NodeInfo {
	private String node_id;
	private String node_name;
	private String serialnumber;
	private List<ThingInfo> things;
	private String json;
	
	public String getNode_id() {
		return node_id;
	}
	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}
	public String getNode_name() {
		return node_name;
	}
	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}
	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	public List<ThingInfo> getThings() {
		return things;
	}
	public void setThings(List<ThingInfo> things) {
		this.things = things;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
}
