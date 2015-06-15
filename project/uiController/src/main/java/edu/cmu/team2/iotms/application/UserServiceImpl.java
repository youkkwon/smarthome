package edu.cmu.team2.iotms.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import edu.cmu.team2.iotms.domain.UserInfo;

public class UserServiceImpl implements UserService {
	private JdbcTemplate jdbcTemplate;
	
	public UserServiceImpl(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	@Override
	public List<UserInfo> getUsers() {
		String sql = "SELECT user_id, user_name, user_mail, user_twitter, user_passwd FROM user_info";
		System.out.println("getUsers sql : "+sql);
		List<UserInfo> users = jdbcTemplate.query(sql, new RowMapper<UserInfo>() {
	        @Override
	        public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	UserInfo aUser = new UserInfo();
	        	
	        	aUser.setUserId(rs.getString("user_id"));
	        	aUser.setUserName(rs.getString("user_name"));
	        	aUser.setUserMail(rs.getString("user_mail"));
	        	aUser.setUserTwitter(rs.getString("user_twitter"));
	        	aUser.setUserPasswd(rs.getString("user_passwd"));
	        	
	        	return aUser;
	        }
	    });
	 
	    return users;

	}

	@Override
	public void createUser(UserInfo user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUser(UserInfo user) {
		String sql = "delete from user_info where user_id='"+user.getUserId()+"' ";
		System.out.println("removeUser sql : "+sql);
		jdbcTemplate.execute(sql);
	}

	@Override
	public void updateUser(UserInfo user) {
		String sql = "update user_info set user_name='"+user.getUserName()+"' "
				+",user_mail='"+user.getUserMail()+"' "
				+",user_twitter='"+user.getUserTwitter()+"' "
				+",user_passwd='"+user.getUserPasswd()+"' "
				+"where user_id='"+user.getUserId()+"' ";
		System.out.println("updateUser sql : "+sql);
		jdbcTemplate.execute(sql);
	}
	
}
