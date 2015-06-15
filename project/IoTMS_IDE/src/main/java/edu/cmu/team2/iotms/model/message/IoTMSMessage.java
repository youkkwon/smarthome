package edu.cmu.team2.iotms.model.message;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;

public abstract class IoTMSMessage {
	protected final static String mID="MESSAGE";
	protected final static String CONFIRM = "CONFIRM";
	protected final static String EMERGENCY = "EMERGENCY";
	protected final static String MALFUNCTION = "MALFUNCTION";
	protected final static String POST = "POST";
	
	@Subscribe
	public void SubscribeMessage(final JSONObject msgJSON) {
		JSONArray target= (JSONArray) msgJSON.get("Targets");
		for(int i = 0; i < target.size(); i++) {				
 			if( mID.equals( target.get(i).toString().toUpperCase() ) ){
 				new Thread(new Runnable(){
 				     public void run(){
 				    	ProcessMessage(msgJSON);
 				     }
 				}).start();
 			}	 			
 		}				
	}
	
	private void ProcessMessage(JSONObject msgJSON) {
		JSONArray  value= (JSONArray) msgJSON.get("value");
		for(int i = 0; i < value.size(); i++) {
 			switch(value.get(i).toString().toUpperCase()) {
 			case CONFIRM :
 				sendConfirmMessage();
 				break;
 			case EMERGENCY :
 				sendEmergencyMessage();
 				break;
 			case MALFUNCTION :
 				sendMalFunctionMessage();
 				break;
 			case POST :
 				sendPostMessage();
 				break;
 			default:
 				break;
 			}	 			
 		}
	}

	protected abstract void sendConfirmMessage();
	protected abstract void sendEmergencyMessage();
	protected abstract void sendMalFunctionMessage();
	protected abstract void sendPostMessage();
}
