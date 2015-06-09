package Main;

import EventBus.*;
import RM_Core.Scheduler;

public class Main {
	
	public static void testRuleManager()
	{
		RM_Tester tester = new RM_Tester();
		
		tester.test();
	}
	
	public static void main(String[] args) {
				
		Scheduler 					scheduler 	= Scheduler.getInstance();
		
		RM_EventBusReceiver 		rm 		= new RM_EventBusReceiver();
		NM_EventBusReceiver 		nm 		= new NM_EventBusReceiver();
		CM_EventBusReceiver 		cm 		= new CM_EventBusReceiver();		
		
		Logger_EventBusReceiver 	logger 	= new Logger_EventBusReceiver();		
		Message_EventBusReceiver 	message = new Message_EventBusReceiver();
		
		scheduler.start();
		
		testRuleManager();
	}
}
