package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ListIterator;

public class RuleSetDao {
	private static RuleSetDao singletondao = null;
	private static Connection conn= null;
	
	private RuleSetDao() {
		conn = IoTMSDao.getInstance().getConnection();
	}
	
	public static RuleSetDao getInstance() {
		if (singletondao == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (RuleSetDao.class) {
				if (singletondao == null) {
					singletondao = new RuleSetDao();
				}
			}
		}
		return singletondao;
	}
	
	public ListIterator<String> loadRuleSet() {
		LinkedList<String> raw_rules = new LinkedList<String>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("select ruleset from ruleset_info");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					raw_rules.add(rs.getString("ruleset"));					
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
		
		return raw_rules.listIterator();
	}

	public boolean insertRule(String rulestring) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "insert into ruleset_info(ruleset) value('"+rulestring+"')";
		//System.out.println("RuleSetDao(insertRule) sql: "+query);

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
	
	public boolean deleteAllRules() {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "delete from ruleset_info";
		//System.out.println("RuleSetDao(deleteRule) sql: "+query);

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
	
	public boolean deleteRule(String rulestring) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "delete from ruleset_info where ruleset='"+rulestring+"'";
		//System.out.println("RuleSetDao(deleteRule) sql: "+query);

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
	
	public int loadRuleAlarmConfig() {
		int alarm = 10;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("select security_settime from setting");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					alarm = rs.getInt("security_settime");
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
		
		return alarm;
	}
	
	public int loadRuleLightOffConfig() {
		int lightoff = 10;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("select lightoff_settime from setting");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					lightoff = rs.getInt("lightoff_settime");
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
		
		return lightoff;
	}

	public boolean storeRuleAlarmConfig(int config) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "update setting set security_settime ="+config;
		//System.out.println("RuleSetDao(storeRuleAlarmConfig) sql: "+query);

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

	public boolean storeRuleLightOffConfig(int config) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "update setting set lightoff_settime ="+config;
		//System.out.println("RuleSetDao(storeRuleLightOffConfig) sql: "+query);

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

	public int loadRuleMalFuncCConfig() {
		int malfunc = 10;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("select malfunc_settime from setting");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					malfunc = rs.getInt("malfunc_settime");
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
		
		return malfunc;
	}

	public boolean storeRuleMalFuncConfig(int config) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "update setting set malfunc_settime ="+config;
		//System.out.println("RuleSetDao(storeRuleLightOffConfig) sql: "+query);

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

	public int loadRuleDoorSensorCConfig() {
		int doorsensor = 10;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("select doorsensor_settime from setting");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					doorsensor = rs.getInt("doorsensor_settime");
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
		
		return doorsensor;
	}

	public boolean storeRuleDoorSensorCConfig(int config) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "update setting set doorsensor_settime ="+config;
		//System.out.println("RuleSetDao(storeRuleLightOffConfig) sql: "+query);

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
