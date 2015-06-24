package Tester;

import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;
import edu.cmu.team2.iotms.model.eventBus.NM_EventBusReceiver;
import edu.cmu.team2.iotms.model.eventBus.RM_EventBusReceiver;

import edu.cmu.team2.iotms.model.message.MailMessage;
import edu.cmu.team2.iotms.model.message.TwitterMessage;

import edu.cmu.team2.iotms.model.nodeManager.NodeManager;
import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Scalability;
import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Scheduler;
import edu.cmu.team2.iotms.model.ruleManager.RM_Event.RuleManager;

public class Main {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
				
		// Event bus for IoTMS
		RM_EventBusReceiver 		rm 		= new RM_EventBusReceiver();
		NM_EventBusReceiver 		nm 		= new NM_EventBusReceiver();
		
		// RuleManager
		RuleManager 				rulemanager	= RuleManager.getInstance();		
		Scheduler 					rm_scheduler= Scheduler.getInstance();
		
		rulemanager.start();							
		rm_scheduler.start();
	
		Scalability					sc_tester = Scalability.getInstance();
		sc_tester.start();
		
		// NodeManager
		NodeManager 				nodemanager	= NodeManager.getInstance();	
		
		IoTMSEventBus.getInstance().register(new MailMessage());
		IoTMSEventBus.getInstance().register(new TwitterMessage());		
		
		Tester tester	= new Tester();
		tester.test();
		
		System.out.println ("Test is done");
		System.exit(1);
	}
}
