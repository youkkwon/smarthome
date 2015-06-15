package Tester;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import NodeManager.NodeManager;

public class TestServer {
	
	private String nodeID;
	
	public TestServer (String nodeID)
	{
		this.nodeID = nodeID;
	}

	@SuppressWarnings("unchecked")
	public void test() throws InterruptedException {
		// TODO Auto-generated method stub
		

		// discover node
		System.out.println("\n[Tester - Server] # 1. Create Node Manager.");
		NodeManager nm = new NodeManager();
		System.out.println("\n[Tester - Server] # 2. Discover Node.");
		
		JSONObject JSONMsg = new JSONObject();
		JSONMsg.put("Duration", "10");
		nm.discoverNode(JSONMsg);

		Thread.sleep(3000);
		
		// register node
		System.out.println("\n[Tester - Server] # 3. Register the Node.");
		String msg = "{\"job\":\"Register\",\"NodeID\":\"12:23:34:45:56:67\",\"ip\":\"127.0.0.1\",\"port\":\"5503\"}";
		nm.registerNode((JSONObject) JSONValue.parse(msg));
		

		Thread.sleep(15000);
		System.out.println("\n[Tester - Server] # 4. Send data to the Node.");
		msg = "{\"job\":\"Request\",\"NodeID\":\"12:23:34:45:56:67\",\"Door\":\"OpenDoor\"}";
		//nm.send("12:23:34:45:56:67", msg);

		Thread.sleep(5000);
		System.out.println("\n[Tester - Server] # 5. Disconnect the Node.");
		msg = "{\"job\":\"Request\",\"NodeID\":\"12:23:34:45:56:67\",\"Door\":\"OpenDoor\"}";
		//nm.disconnect("12:23:34:45:56:67");
	}

}
