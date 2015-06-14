package Tester;

import org.json.simple.JSONObject;

import NodeManager.NodeManager;

public class TestServer {

	public void test() throws InterruptedException {
		// TODO Auto-generated method stub
		
		JSONObject JSONMsg = new JSONObject();
		// TODO
		
		// discover node
		System.out.println("\n# 1. Create Node Manager.");
		NodeManager nm = new NodeManager();
		System.out.println("\n# 2. Discover Node.");
		nm.discoverNode(JSONMsg);

		Thread.sleep(3000);
		
		// register node
		System.out.println("\n# 3. Register the Node.");
		String msg = "{\"job\":\"Register\",\"NodeID\":\"12:23:34:45:56:67\",\"ip\":\"127.0.0.1\",\"port\":\"5503\"}";
		nm.registerNode(JSONMsg);
		

		Thread.sleep(15000);
		System.out.println("\n# 4. Send data to the Node.");
		msg = "{\"job\":\"Request\",\"NodeID\":\"12:23:34:45:56:67\",\"Door\":\"OpenDoor\"}";
		//nm.send("12:23:34:45:56:67", msg);

		Thread.sleep(5000);
		System.out.println("\n# 5. Disconnect the Node.");
		msg = "{\"job\":\"Request\",\"NodeID\":\"12:23:34:45:56:67\",\"Door\":\"OpenDoor\"}";
		//nm.disconnect("12:23:34:45:56:67");
	}

}
