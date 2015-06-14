package Tester;

public class Tester {
	
	public  void test()
	{
		RM_Tester rm_tester = new RM_Tester();
		NM_Tester nm_tester = new NM_Tester();
		
		TestDevice 	device_tester 	= new TestDevice();		
		TestJSON	json_tester		= new TestJSON();
		TestServer	server_tester	= new TestServer();
		
		try {
			rm_tester.test();				
	//		nm_tester.test();
			
			/*
			device_tester.test(3);
			json_tester.test();
			server_tester.test();		
			*/	
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
