package Tester;

public class Tester {
	
	private String nodeID = "78:c4:e:1:7f:f9";
	
	public  void test()
	{
		/*
		NM_Tester nm_tester = new NM_Tester(nodeID);
		nm_tester.test();	
		*/
				
		///*
	 	RM_Tester rm_tester = new RM_Tester(nodeID);
		try {
			rm_tester.test();				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		//*/
		
		/*
		Integrity_Tester it_tester = new Integrity_Tester(nodeID);
		try {
			it_tester.test();				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		*/
		
		/*
		TestDevice 	device_tester 	= new TestDevice(nodeID);
		device_tester.test(3);
		*/
			
		/*
		TestJSON	json_tester		= new TestJSON(nodeID);
		json_tester.test();
		*/	
		
		/*
		TestServer	server_tester	= new TestServer(nodeID);	
		server_tester.test();		
		*/		
	}
}
