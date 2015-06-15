package Tester;

import EventBus.*;
import NodeManager.NodeManager;
import RuleManager.RM_Event.RuleManager;
import RuleManager.RM_Core.Scheduler;

import Tester.Tester;

public class Main {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
				
		// Event bus for IoTMS
		RM_EventBusReceiver 		rm 		= new RM_EventBusReceiver();
		NM_EventBusReceiver 		nm 		= new NM_EventBusReceiver();
		Logger_EventBusReceiver 	logger 	= new Logger_EventBusReceiver();		
		Message_EventBusReceiver 	message = new Message_EventBusReceiver();
		
		// RuleManager
		RuleManager 				rulemanager	= RuleManager.getInstance();		
		Scheduler 					rm_scheduler= Scheduler.getInstance();
		
		rulemanager.start();							
		rm_scheduler.start();
	
		// NodeManager
		NodeManager 				nodemanager	= NodeManager.getInstance();	
		
		Tester tester	= new Tester();
		tester.test();
	}
}
