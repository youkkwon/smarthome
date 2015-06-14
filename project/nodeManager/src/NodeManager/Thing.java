package NodeManager;

import org.json.simple.JSONObject;

public abstract class Thing {
	protected String mName;
	protected int mValue = 0;
	protected int mSensorType = 0; // 0: unknown, 1: actuator, 2: sensor
	protected Type mType = Type.Unknown;
    
    public abstract Type getType();
    public abstract void setType(Type type);
    
    public abstract JSONObject getValue(JSONObject JSONMsg);

    /*
	 * @see NodeManager.Thing#setValue(int)
	 * true : 값의 변경이 생김
	 * fasle : 값의 변경이 없음
	 */
    public abstract boolean setValue(int value);
    
    public abstract String getName();
    public abstract void setName(String name);
    
    public abstract JSONObject doCommand(JSONObject JSONMsg);
}
