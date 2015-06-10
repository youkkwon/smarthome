package NodeManager.Things;

import org.json.simple.JSONObject;

import NodeManager.Thing;
import NodeManager.Type;

public class Alarm extends Thing {

	public Alarm(Type type, int id) {
		mType = type;
		mId = id;
	}
	
	@Override
	public int GetID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void SetID(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Type GetType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetType(Type type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject GetValue(JSONObject JSONMsg) {
		if (mValue == 0)
			JSONMsg.put("Value", "Unset");
		else
			JSONMsg.put("Value", "Set");
		
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
