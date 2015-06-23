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
		String  value = (String) msgJSON.get("Value");
		String  desc = "";
		try{
			desc = msgJSON.get("Desc").toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
		switch(value.toUpperCase()) {
		case CONFIRM :
			sendConfirmMessage(desc);
			break;
		case EMERGENCY :
			sendEmergencyMessage(desc);
			break;
		case MALFUNCTION :
			sendMalFunctionMessage(desc);
			break;
		case POST :
			sendPostMessage(desc);
			break;
		default:
			break;
		}	 			
 	}

	/*
	 * private void ProcessMessage(JSONObject msgJSON) {
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

	 */
	protected abstract void sendConfirmMessage(String desc);
	protected abstract void sendEmergencyMessage(String desc);
	protected abstract void sendMalFunctionMessage(String desc);
	protected abstract void sendPostMessage(String desc);
}
