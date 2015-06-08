import com.google.common.eventbus.EventBus;
import org.json.simple.JSONObject;

public class EventBusIoTMS extends EventBus{
	private static EventBusIoTMS uniqueInstance = new EventBusIoTMS();
	
	public static EventBusIoTMS getInstance(){
		return uniqueInstance;
	}
	
	private EventBusIoTMS(){}
	
	private void saveLog(JSONObject msgJSON){
		System.out.println("[Logger]save log: " + msgJSON);
	}
	
	
	public void publish(JSONObject msgJSON){
		post(msgJSON);
		saveLog(msgJSON);
	}
}

