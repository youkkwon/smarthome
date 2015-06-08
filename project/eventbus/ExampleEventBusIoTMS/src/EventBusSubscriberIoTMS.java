import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;


public class EventBusSubscriberIoTMS {
	private String mID="sub";
	private EventBusIoTMS eb = EventBusIoTMS.getInstance();
	EventBusSubscriberIoTMS(String identifier){
		mID=identifier;
		eb.register(this);
	}
	private void ProcessMessage(JSONObject msgJSON){
		System.out.println(mID + " : " + msgJSON);
	}			
	
	@Subscribe
	public void SubscribeMessage(JSONObject msgJSON) {
		JSONArray   target= (JSONArray) msgJSON.get("target");
		for(int i = 0; i < target.size(); i++) {				
 			if( mID.equals( target.get(i) ) ){
 				ProcessMessage(msgJSON);
 			}	 			
 		}				
	}
	
	public String GetEventBusID(){
		return mID;
	}
}
