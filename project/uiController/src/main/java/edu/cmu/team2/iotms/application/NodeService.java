package edu.cmu.team2.iotms.application;

import java.util.List;

import edu.cmu.team2.iotms.domain.NodeInfo;
import edu.cmu.team2.iotms.domain.ThingInfo;

public interface NodeService {
	public List<NodeInfo> getNodeList();
	public List<ThingInfo> getThingList(String node_id);
	public void controlThing(String nodeid, String thingid, String type, String value);
	public void discoverNodes();
	public void registerNode(String nodeid, String serial);
}
