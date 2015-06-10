package NodeManager.Things;

import org.json.simple.JSONObject;

import NodeManager.Thing;
import NodeManager.Type;


public class Door extends Thing {

	public Door(Type type, int id) {
		mType = type;
		mId = id;
	}
	
	@Override
	public int GetID() {
		// TODO Auto-generated method stub
		return this.mId;
	}

	@Override
	public void SetID(int id) {
		// TODO Auto-generated method stub
		this.mId = id;
	}

	@Override
	public Type GetType() {
		// TODO Auto-generated method stub
		return this.mType;
	}

	@Override
	public void SetType(Type type) {
		// TODO Auto-generated method stub
		this.mType = type;
	}

	@Override
	public JSONObject GetValue(JSONObject JSONMsg) {
		JSONMsg.put("Value", "Unknown");
		
		// add thing id
		JSONMsg.put("ThingID", Integer.toString(mId));
		
		return JSONMsg;
	}

	@Override
	public void SetValue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doCommand() {
		// TODO Auto-generated method stub
		
	}
}
