package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.team2.iotms.model.nodeManager.DefaultThing;
import edu.cmu.team2.iotms.model.nodeManager.Thing;

public class NodeDao {
	private static NodeDao singletondao = null;
	private static Connection conn= null;
	
	private NodeDao() {
		conn = IoTMSDao.getInstance().getConnection();
	}
	
	public static NodeDao getInstance() {
		if (singletondao == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (NodeDao.class) {
				if (singletondao == null) {
					singletondao = new NodeDao();
				}
			}
		}
		return singletondao;
	}
	
	public List<String> getAllNodes() {
		List<String> nodes = new ArrayList<String>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("select node_id from node_info");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					nodes.add(rs.getString("node_id"));					
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
		
		return nodes;
	}

	public boolean insertNode(String node_id) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "insert into node_info(node_id) value('"+node_id+"')";
		System.out.println("NodeDao(insertNode) sql: "+query);

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
	
	public boolean deleteAllNodes() {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "delete from node_info";
		System.out.println("NodeDao(deleteAllNodes) sql: "+query);

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
	
	public boolean deleteNode(String node_id) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "delete from node_info where node_id='"+node_id+"'";
		System.out.println("NodeDao(deleteNode) sql: "+query);

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
	
	public List<Thing> getAllThingsInNode(String node_id) {
		List<Thing> things = new ArrayList<Thing>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("select node_id,thing_id,type,stype,vtype,vmin,vmax "
					+"from thing_info "
					+"where node_id='"+node_id+"'");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					Thing thing = new DefaultThing();
					thing.setId(rs.getString("node_id")+rs.getString("thing_id"));
					thing.setType(rs.getString("node_id"));
					thing.setSensorType(rs.getString("node_id"));
					thing.setValue(rs.getString("node_id"));
					
					things.add(thing);
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
		
		return things;
	}
	
	public String getJsonOfNode(String node_id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result=null;
		
		try {
			pstmt = conn.prepareStatement("select json "
					+"from node_info "
					+"where node_id='"+node_id+"'");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					result = rs.getString("json");
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
		return result;
	}
	public boolean setJsonOfNode(String node_id, String json) {
		boolean ret = false;
		boolean update = false;
		PreparedStatement pstmt = null;

		try {
			String query = "insert into node_info(node_id, json, registered) value('"+node_id+"','"+json+"',0)";
			System.out.println("NodeDao(setJsonOfNode-insert) sql: "+query);
			pstmt = conn.prepareStatement(query);
			ret = pstmt.execute();
		} catch (SQLException e) {
			update = true;
		}
		
		if(update) {
			try {
				String query = "update node_info set json='"+json+"', registered=0 where node_id='"+node_id+"'";
				System.out.println("NodeDao(setJsonOfNode-update) sql: "+query);
				pstmt = conn.prepareStatement(query);
				ret = pstmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if(pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public boolean insertThing(String node_id, String thing_id, String thing_name
			, String type, String stype, String vtype, String vmin, String vmax) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "insert into thing_info(node_id, thing_id, thing_name, type, stype, vtype, vmin, vmax) "
				+ "value('"+node_id+"','"+thing_id+"','"+thing_name+"','"+type+"','"+stype+"','"+vtype+"','"+vmin+"','"+vmax+"')";
		
		System.out.println("NodeDao(insertThing) sql: "+query);

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
	
	public boolean deleteThingsInNode(String node_id) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "delete from thing_info where node_id = '"+node_id+"'";
		System.out.println("NodeDao(deleteThingsInNode) sql: "+query);

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
	
	public boolean deleteThing(String node_id, String thing_id) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "delete from thing_info "
					+"where node_id='"+node_id+"' and thing_id='"+thing_id+"'";
		System.out.println("NodeDao(deleteThing) sql: "+query);

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
	
	public boolean updateThing(String nodeid, String thingid, String thingValue) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "update thing_info set value='" + thingValue
				+ "' where node_id='" + nodeid + "' and thing_id='" + thingid + "' ";
		
		//System.out.println("LoggerDao(updateEvent) sql: "+query);

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

	public boolean authorizedNode(String node_id) {
		boolean ret = false;
		PreparedStatement pstmt = null;
		String query = "update node_info set registered=1 where node_id='"+node_id+"'";
		
		//System.out.println("LoggerDao(updateEvent) sql: "+query);

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
