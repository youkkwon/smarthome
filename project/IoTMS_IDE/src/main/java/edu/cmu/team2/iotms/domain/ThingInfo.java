package edu.cmu.team2.iotms.domain;

public class ThingInfo {
	private String node_id;
	private String thing_id;
	private String thing_name;
	private String type;
	private String stype;
	private String vtype;
	private String vmin;
	private String vmax;
	private String value;
	
	public String getNode_id() {
		return node_id;
	}
	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}
	public String getThing_id() {
		return thing_id;
	}
	public void setThing_id(String thing_id) {
		this.thing_id = thing_id;
	}
	public String getThing_name() {
		return thing_name;
	}
	public void setThing_name(String thing_name) {
		this.thing_name = thing_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStype() {
		return stype;
	}
	public void setStype(String stype) {
		this.stype = stype;
	}
	public String getVtype() {
		return vtype;
	}
	public void setVtype(String vtype) {
		this.vtype = vtype;
	}
	public String getVmin() {
		return vmin;
	}
	public void setVmin(String vmin) {
		this.vmin = vmin;
	}
	public String getVmax() {
		return vmax;
	}
	public void setVmax(String vmax) {
		this.vmax = vmax;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
