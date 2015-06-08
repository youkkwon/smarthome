import java.util.ArrayList;
import com.google.common.eventbus.Subscribe;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


public class TestEventBusIoTMS {
	
	public ArrayList<EventBusSubscriberIoTMS> mSubArrayList;	
	private EventBusIoTMS eb = EventBusIoTMS.getInstance();
	
	//thread publishing event message 
	class ThreadTestEventBus extends Thread {
		JSONObject mMsgJSON;		
		ThreadTestEventBus(JSONObject msgJSON)
		{
			mMsgJSON = msgJSON;
		}
	    public void run() {
	    	for(int i = 0 ; i < 30 ; i++ )
	    	{
	    		mMsgJSON.put("count",i);
	    		eb.publish(mMsgJSON);
	    	   	try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    } 
	} 	
	
 	public void test() {
 		//allocate & register EventBus's Subscriber
 		mSubArrayList = new ArrayList<EventBusSubscriberIoTMS>();
		mSubArrayList.add(new EventBusSubscriberIoTMS("NodeManager"));
		mSubArrayList.add(new EventBusSubscriberIoTMS("RuleManager"));
		mSubArrayList.add(new EventBusSubscriberIoTMS("UserInterface"));
		mSubArrayList.add(new EventBusSubscriberIoTMS("EmergencyMessage"));
 		mSubArrayList.add(new EventBusSubscriberIoTMS("CommManager"));
 	
 		
 		//event message from rule manager
 		JSONArray jarrayRuleManager = new JSONArray();
 		JSONObject msgRuleManager = new JSONObject(); 		
 		jarrayRuleManager.add("NodeManager");
 		jarrayRuleManager.add("UserInterface");	
 		msgRuleManager.put("target",jarrayRuleManager);
 		msgRuleManager.put("name","RuleManager");
 		msgRuleManager.put("parameter","close");
 		msgRuleManager.put("action","door");
 		msgRuleManager.put("type","ControlDoor");
 		msgRuleManager.put("return","door close");
 		
 		
 		//event message from node manager
 		JSONArray jarrayNodeManager = new JSONArray();
 		JSONObject msgNodeManager = new JSONObject();	
 		jarrayNodeManager.add("RuleManager");
 		jarrayNodeManager.add("UserInterface");	
 		msgNodeManager.put("target",jarrayNodeManager);		 		
 		msgNodeManager.put("name","NodeManager");
 		msgNodeManager.put("parameter","open");
 		msgNodeManager.put("action","FeedbackDoorStatus");
 		msgNodeManager.put("type","string");
 		msgNodeManager.put("return","doorOpen");
		
 		//event message from UI
 		JSONArray jarrayUserInterface = new JSONArray();
 		JSONObject msgUserInterface = new JSONObject();	
 		jarrayUserInterface.add("RuleManager");
 		jarrayUserInterface.add("NodeManager");	
 		msgUserInterface.put("target",jarrayUserInterface);		 		
 		msgUserInterface.put("name","UserInterface");
 		msgUserInterface.put("parameter","temperature");
 		msgUserInterface.put("action","UIdirectGetSensorValue");
 		msgUserInterface.put("type","string");
 		msgUserInterface.put("return","degree");
		
 		ThreadTestEventBus ThreadRuleManager = new ThreadTestEventBus(msgRuleManager);
 		ThreadTestEventBus ThreadNodeManager = new ThreadTestEventBus(msgNodeManager);
 		ThreadTestEventBus ThreadUserInterface = new ThreadTestEventBus(msgUserInterface);
 		
 		ThreadRuleManager.start();
 		ThreadNodeManager.start();
 		ThreadUserInterface.start();
 	//	eb.publish(msgRuleManager);
 	//	eb.publish(msgNodeManager);
 	//	eb.publish(msgUserInterface);
 		
 	}
}
