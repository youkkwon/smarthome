package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoggerDao {
	private static LoggerDao singletondao = null;
	private static Connection conn= null;
	
	private LoggerDao() {
		conn = IoTMSDao.getInstance().getConnection();
	}
	
	public static LoggerDao getInstance() {
		if (singletondao == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (LoggerDao.class) {
				if (singletondao == null) {
					singletondao = new LoggerDao();
				}
			}
		}
		return singletondao;
	}
	
	public boolean addEventHistory(String content) {
		boolean ret = false;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String eventdate = dateFormat.format(cal.getTime());
		
		PreparedStatement pstmt = null;
		String query = "insert into eventhistory(eventdate,eventcontent) "
				+ "value('"+eventdate+"','"+content+"')";
		
		//System.out.println("LoggerDao(addEventHistory) sql: "+query);

		try {
			pstmt = conn.prepareStatement(query);
			ret = pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}
	
	public boolean addMessageHistory(String content) {
		boolean ret = false;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String messagedate = dateFormat.format(cal.getTime());
		
		PreparedStatement pstmt = null;
		String query = "insert into messagehistory(messagedate,messagecontent) "
				+ "value('"+messagedate+"','"+content+"')";
		
		//System.out.println("LoggerDao(addMessageHistory) sql: "+query);

		try {
			pstmt = conn.prepareStatement(query);
			ret = pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

}
