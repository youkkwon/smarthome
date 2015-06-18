package Tester;

public class Tester {
	
	private String nodeID = "78:c4:e:1:7f:f9";
	
	public  void test()
	{
		///*
	 	RM_Tester rm_tester = new RM_Tester(nodeID);
			try {
			rm_tester.test();				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		//*/
		
		/*
		NM_Tester nm_tester = new NM_Tester(nodeID);
		nm_tester.test();	
		 */
				
		/*
		TestDevice 	device_tester 	= new TestDevice(nodeID);
		device_tester.test(3);
		*/
			
		/*
		TestJSON	json_tester		= new TestJSON(nodeID);
		TestServer	server_tester	= new TestServer(nodeID);
		json_tester.test();
		server_tester.test();		
		*/	
		
	}
}
