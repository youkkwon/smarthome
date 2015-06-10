package Main;

import EventBus.*;
import RM_Core.Scheduler;
import RM_Event.RMEventHandler;

public class Main {
	
	public static void testRuleManager()
	{
		RM_Tester tester = new RM_Tester();
		
		try {
			tester.test();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
				
		Scheduler 					rm_scheduler= Scheduler.getInstance();
		RMEventHandler 				rm_event 	= RMEventHandler.getInstance();
		
		RM_EventBusReceiver 		rm 		= new RM_EventBusReceiver();
		NM_EventBusReceiver 		nm 		= new NM_EventBusReceiver();
		CM_EventBusReceiver 		cm 		= new CM_EventBusReceiver();				
		Logger_EventBusReceiver 	logger 	= new Logger_EventBusReceiver();		
		Message_EventBusReceiver 	message = new Message_EventBusReceiver();
		
		rm_scheduler.start();
	//	rm.addObserver(rm_event);
	//	rm_event.start();
		
		testRuleManager();
	}
}
