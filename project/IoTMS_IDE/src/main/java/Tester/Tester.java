package Tester;

public class Tester {
	
	private String nodeID = "12:23:34:45:56:67";
	
	public  void test()
	{
		/*
		RM_Tester rm_tester = new RM_Tester(nodeID);
		NM_Tester nm_tester = new NM_Tester(nodeID);
		try {
			rm_tester.test();				
			nm_tester.test();			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		*/
			
		
		TestDevice 	device_tester 	= new TestDevice(nodeID);
		device_tester.test(3);
			
		/*
		TestJSON	json_tester		= new TestJSON(nodeID);
		TestServer	server_tester	= new TestServer(nodeID);
		json_tester.test();
		server_tester.test();		
		*/	
		
	}
}
