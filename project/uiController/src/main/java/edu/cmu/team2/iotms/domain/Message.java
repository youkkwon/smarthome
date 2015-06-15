package edu.cmu.team2.iotms.domain;

public class Message {
	private String messagedate;
	private String messagecontent;
	
	public Message() {
		this.messagedate = "";
		this.messagecontent = "";
	}
	public Message(String messagedate, String messagecontent) {
		this.messagedate = messagedate;
		this.messagecontent = messagecontent;
	}
	
	public String getMessageDate() {
		return messagedate;
	}
	public void setMessageDate(String messagedate) {
		this.messagedate = messagedate;
	}

	public String getMessageContent() {
		return messagecontent;
	}
	public void setMessageContent(String messagecontent) {
		this.messagecontent = messagecontent;
	}
}
