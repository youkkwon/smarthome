package edu.cmu.team2.iotms.application;

import java.util.List;

import edu.cmu.team2.iotms.domain.NodeInfo;
import edu.cmu.team2.iotms.domain.ThingInfo;

public interface NodeService {
	public List<NodeInfo> getNodeList(String registered);
	public List<ThingInfo> getThingList(String node_id);
	public void removeNode(String nodeid);
	public void controlThing(String nodeid, String thingid, String type, String value);
	//public List<NodeInfo> getNewNodes();
	public void discoverNodes();
	public void registerNode(String nodeid, String serial);
	public void testNode(String nodeid, String thingid, String type,
			String value);
	public void testNodeDiscover(String nodeid);
	void testAuthorized(String nodeid);
}
