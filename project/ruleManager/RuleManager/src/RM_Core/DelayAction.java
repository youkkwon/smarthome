package RM_Core;

public class DelayAction extends Action {

	private int time;
	private int remainTime;
	
	public DelayAction(String nodeID, String thingID, String value, String timeStr, String type) {
		super(nodeID, thingID, value, type);
		time			= Integer.parseInt(timeStr);
		remainTime		= time;
		statement 		= statement + " in " + timeStr;
	}

	public int getTimeLeft()
	{
		return remainTime;
	}
	
	public boolean isExpired()
	{
		return (remainTime <= 0) ? true : false;
	}
	
	public void decreaseTime (int time)
	{
		remainTime -= time;
	}
	
	@Override
	public void execute() {
		generateJSONMsg();
		Scheduler.getInstance().addAction(this);
	}	
}
