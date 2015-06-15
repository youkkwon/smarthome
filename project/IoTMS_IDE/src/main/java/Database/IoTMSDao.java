package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IoTMSDao {
	private static IoTMSDao iotmsDao = null;
	private static Connection conn= null;
	private IoTMSDao() {
		conn = getConnection();
	}
	
	public static IoTMSDao getInstance() {
		if (iotmsDao == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (IoTMSDao.class) {
				if (iotmsDao == null) {
					iotmsDao = new IoTMSDao();
				}
			}
		}
		return iotmsDao;
	}

	public Connection getConnection() {
		if(conn == null) {
			conn = makeConnection();
		}
		return conn;
	}
	
	private static Connection makeConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/iotmsdb?" +
		        "user=iotms&password=iotms");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		};
		return conn;
	}
/*
	public void eventhistory() {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select eventdate, eventcontent from eventhistory");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					message.setEventDate(rs.getString("eventdate"));
					message.setEventContent(rs.getString("eventcontent"));
					
					eventhistory.add(message);
					
				} while(rs.next());
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

		
*/
}
