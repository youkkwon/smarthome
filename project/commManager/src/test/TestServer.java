package test;

import node.NodeManager;

public class TestServer {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		// discover node
		System.out.println("\n# 1. Create Node Manager.");
		NodeManager nm = new NodeManager();
		
		System.out.println("\n# 2. Discover Node.");
		nm.discoverNode(10);

		Thread.sleep(3000);
		
		// register node
		System.out.println("\n# 3. Register the Node.");
		String msg = "{\"job\":\"Register\",\"NodeID\":\"12:23:34:45:56:67\",\"ip\":\"127.0.0.1\",\"port\":\"5503\"}";
		nm.registerNode(msg);
		

		Thread.sleep(15000);
		System.out.println("\n# 4. Send data to the Node.");
		msg = "{\"job\":\"Request\",\"NodeID\":\"12:23:34:45:56:67\",\"Door\":\"OpenDoor\"}";
		nm.send("12:23:34:45:56:67", msg);

		Thread.sleep(5000);
		System.out.println("\n# 5. Disconnect the Node.");
		msg = "{\"job\":\"Request\",\"NodeID\":\"12:23:34:45:56:67\",\"Door\":\"OpenDoor\"}";
		nm.disconnect("12:23:34:45:56:67");
	}

}
