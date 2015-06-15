package edu.cmu.team2.iotms.domain;

public class EventHistory {
	private String eventdate;
	private String eventcontent;
	
	public EventHistory() {
		this.eventdate = "";
		this.eventcontent = "";
	}
	public EventHistory(String eventdate, String eventcontent) {
		this.eventdate = eventdate;
		this.eventcontent = eventcontent;
	}
	
	public String getEventDate() {
		return eventdate;
	}
	public void setEventDate(String eventdate) {
		this.eventdate = eventdate;
	}

	public String getEventContent() {
		return eventcontent;
	}
	public void setEventContent(String eventcontent) {
		this.eventcontent = eventcontent;
	}
}
