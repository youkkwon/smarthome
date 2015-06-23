package edu.cmu.team2.iotms.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;

import edu.cmu.team2.iotms.domain.Message;
import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;
import edu.cmu.team2.iotms.model.message.MailMessage;
import edu.cmu.team2.iotms.model.message.TwitterMessage;

public class MessageServiceImpl implements MessageService {
	private JdbcTemplate jdbcTemplate;
	
	public MessageServiceImpl(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
		
		IoTMSEventBus.getInstance().register(new MailMessage());
		IoTMSEventBus.getInstance().register(new TwitterMessage());
	}
	
	@Scheduled(fixedRate = 60000, initialDelay=10000)
	public void handle() {
		String sql = "delete from MessageHistory "
				+ "where messagedate < (select DATE_ADD(now(),INTERVAL -logging_duration MINUTE) from setting)";
		jdbcTemplate.update(sql);
	}
	
	@Override
	public List<Message> list(String fromdate, String todate) {
		String sql = "SELECT messagedate, messagecontent FROM MessageHistory "
				+"where messagedate>='"+fromdate+"' "
				+"and DATE_ADD(messagedate,INTERVAL -1 DAY)<='"+todate+"' ";
		System.out.println("messagelist sql : "+sql);
	    List<Message> listMessage = jdbcTemplate.query(sql, new RowMapper<Message>() {
	 
	        @Override
	        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	Message aMessage = new Message();
	 
	        	aMessage.setMessageDate(rs.getString("messagedate"));
	        	aMessage.setMessageContent(rs.getString("messagecontent"));
	 
	            return aMessage;
	        }
	 
	    });
	     
	    return listMessage;
	}

}
