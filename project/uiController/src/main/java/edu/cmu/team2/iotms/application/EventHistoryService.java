package edu.cmu.team2.iotms.application;

import java.util.List;

import edu.cmu.team2.iotms.domain.EventHistory;

public interface EventHistoryService {
	public List<EventHistory> list(String fromdate, String todate);
}
