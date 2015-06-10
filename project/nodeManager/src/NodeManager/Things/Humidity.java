package NodeManager.Things;

import org.json.simple.JSONObject;

import NodeManager.Thing;
import NodeManager.Type;

public class Humidity extends Thing {

	public Humidity(Type type, int id) {
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
		// TODO Auto-generated method stub
		return null;
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
