package Main;

import EventBus.*;
import RM_Core.Scheduler;
import RM_Event.RuleManager;

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
				
		// Event bus for IoTMS
		RM_EventBusReceiver 		rm 		= new RM_EventBusReceiver();
		NM_EventBusReceiver 		nm 		= new NM_EventBusReceiver();
		CM_EventBusReceiver 		cm 		= new CM_EventBusReceiver();				
		Logger_EventBusReceiver 	logger 	= new Logger_EventBusReceiver();		
		Message_EventBusReceiver 	message = new Message_EventBusReceiver();
		
		// RuleManager
		RuleManager 				rulemanager	= RuleManager.getInstance();		
		Scheduler 					rm_scheduler= Scheduler.getInstance();
		
		rulemanager.start();							
		rm_scheduler.start();
	
		testRuleManager();
	}
}
