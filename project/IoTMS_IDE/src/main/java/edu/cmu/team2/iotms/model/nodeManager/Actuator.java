package edu.cmu.team2.iotms.model.nodeManager;

import org.json.simple.JSONObject;

public class Actuator extends Thing {

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return this.mType;
	}

	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub
		this.mType = type;
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
		this.mValue = (String)JSONMsg.get("Value");
		
		return JSONMsg;
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

	@Override
	public String getSensorType() {
		// TODO Auto-generated method stub
		return mSensorType;
	}

	@Override
	public void setSensorType(String type) {
		// TODO Auto-generated method stub
		mSensorType = type;
	}

}
