package edu.cmu.team2.iotms.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;

import edu.cmu.team2.iotms.domain.EventHistory;
import edu.cmu.team2.iotms.model.logger.LogManager;

public class EventHistoryServiceImpl implements EventHistoryService {
	private JdbcTemplate jdbcTemplate;
	
	public EventHistoryServiceImpl(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
		LogManager.getInstance().start();
	}

	@Scheduled(fixedRate = 60000, initialDelay=10000)
	public void handle() {
		String sql = "delete from eventhistory "
				+ "where eventdate < (select DATE_ADD(now(),INTERVAL -logging_duration MINUTE) from setting)";
		jdbcTemplate.update(sql);
	}
	
	@Override
	public List<EventHistory> list(String fromdate, String todate) {
		String sql = "SELECT eventdate, eventcontent FROM EventHistory "
				+"where eventdate>='"+fromdate+"' "
				+"and DATE_ADD(eventdate,INTERVAL -1 DAY)<='"+todate+"' ";
		System.out.println("eventhistory sql : "+sql);
	    List<EventHistory> listEventHistory = jdbcTemplate.query(sql, new RowMapper<EventHistory>() {
	 
	        @Override
	        public EventHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	EventHistory aEventHistory = new EventHistory();
	 
	        	aEventHistory.setEventDate(rs.getString("eventdate"));
	        	aEventHistory.setEventContent(rs.getString("eventcontent"));
	 
	            return aEventHistory;
	        }
	 
	    });
	 
	    return listEventHistory;
	}
	
}
