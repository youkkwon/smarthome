package edu.cmu.team2.iotms.application;

import java.util.List;

import edu.cmu.team2.iotms.domain.Message;

public interface MessageService {
	public List<Message> list(String fromdate, String todate);
}
