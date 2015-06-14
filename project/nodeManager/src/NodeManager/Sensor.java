package NodeManager;

import org.json.simple.JSONObject;

public class Sensor extends Thing {

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(Type type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject getValue(JSONObject JSONMsg) {
		// TODO Auto-generated method stub
		JSONMsg.put("Value", "Unknown");
		
		// add thing id
		JSONMsg.put("ThingID", this.mName);
		
		return JSONMsg;
	}

	/*
	 * @see NodeManager.Thing#setValue(int)
	 * true : 값의 변경이 생김
	 * fasle : 값의 변경이 없음
	 */
	@Override
	public boolean setValue(int value) {
		// TODO Auto-generated method stub
		if (value != mValue) {
			mValue = value;
			return true;
		}
		
		return false;
	}

	@Override
	public JSONObject doCommand(JSONObject JSONMsg) {
		// TODO Auto-generated method stub
		// sensor는 doCommand가 없음
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return mName;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.mName = name;
	}

}
