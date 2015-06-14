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

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getValue(JSONObject JSONMsg) {
		// TODO Auto-generated method stub
		JSONMsg.put("Value", this.mValue);
		JSONMsg.put("ThingID", this.mId);
		JSONMsg.put("Type", this.mType);
		
		return JSONMsg;
	}

	/*
	 * @see NodeManager.Thing#setValue(int)
	 * true : 값의 변경이 생김
	 * fasle : 값의 변경이 없음
	 */
	@Override
	public boolean setValue(String value) {
		// TODO Auto-generated method stub
		if (!value.equalsIgnoreCase(mValue)) {
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
	public String getId() {
		// TODO Auto-generated method stub
		return mId;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		this.mId = id;
	}

}
