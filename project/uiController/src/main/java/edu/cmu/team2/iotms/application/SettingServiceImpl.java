package edu.cmu.team2.iotms.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import EventBus.IoTMSEventBus;
import edu.cmu.team2.iotms.domain.Setting;

public class SettingServiceImpl implements SettingService {
	private JdbcTemplate jdbcTemplate;
	
	public SettingServiceImpl(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	@Override
	public Setting  getSettings() {
		String sql = "SELECT security_settime, lightoff_settime, logging_duration FROM setting";
		System.out.println("getSettings sql : "+sql);
		List<Setting> settings = jdbcTemplate.query(sql, new RowMapper<Setting>() {
	        @Override
	        public Setting mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	Setting aSetting = new Setting();
	        	
	        	aSetting.setSecuritySettime(rs.getString("security_settime"));
	        	aSetting.setLightoffSettime(rs.getString("lightoff_settime"));
	        	aSetting.setLoggingDuration(rs.getString("logging_duration"));
	        	
	            return aSetting;
	        }
	    });
	 
	    return settings.get(0);
	}

	@Override
	public void setSettings(Setting settings) {
		String sql = "update setting set security_settime="+settings.getSecuritySettime()
					+",lightoff_settime="+settings.getLightoffSettime()
					+",logging_duration="+settings.getLoggingDuration();
		System.out.println("setSettings sql : "+sql);

		ConfigEvent("Alarm", settings.getSecuritySettime());
		ConfigEvent("Light", settings.getLightoffSettime());
				
		jdbcTemplate.execute(sql);
	}
	
	@SuppressWarnings("unchecked")
	public void ConfigEvent(String type, String value)
	{
		JSONObject	JSONMsg = new JSONObject();
		JSONArray 	targets = new JSONArray();
		
		targets.add("RuleManager");
		JSONMsg.put("Targets", targets);
		JSONMsg.put("Job", "ConfigCtrl");
		JSONMsg.put("Type",  type);
		JSONMsg.put("Value", value);

		IoTMSEventBus.getInstance().postEvent(JSONMsg);
	}

}
