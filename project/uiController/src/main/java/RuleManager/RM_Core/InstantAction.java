package RuleManager.RM_Core;

public class InstantAction extends Action {

	public InstantAction (String nodeID, String thingID, String value, String type)
	{
		super(nodeID, thingID, value, type);
	}
	
	@Override
	public void execute() {
		generateJSONMsg();
		postEvent();
	}
	
	public boolean isDelayAction ()
	{
		return false;
	}
}

