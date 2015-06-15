package NodeManager;

import org.json.simple.JSONObject;

public abstract class Thing {
	protected String mId = "";
	protected String mValue = "";
	protected String mSensorType = "Unknown"; // 0: unknown, 1: actuator, 2: sensor
	protected String mType = "Default";
    
    public abstract String getType();
    public abstract void setType(String type);
    
    public abstract String getSensorType();
    public abstract void setSensorType(String type);
    
    public abstract JSONObject getValue(JSONObject JSONMsg);

    /*
	 * @see NodeManager.Thing#setValue(int)
	 * true : 값의 변경이 생김
	 * fasle : 값의 변경이 없음
	 */
    public abstract boolean setValue(String value);
    
    public abstract String getId();
    public abstract void setId(String id);
    
    public abstract JSONObject doCommand(JSONObject JSONMsg);
}
