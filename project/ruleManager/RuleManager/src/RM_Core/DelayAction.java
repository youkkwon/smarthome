package RM_Core;

public class DelayAction extends Action {

	private int time;
	private int remainTime;
	private String timeStr;
	
	public DelayAction(String nodeID, String thingID, String value, String timeStr, String type) {
		super(nodeID, thingID, value, type);
		this.timeStr	= timeStr;
		time			= Integer.parseInt(timeStr);
		remainTime		= time;
		statement 		= statement + " in " + timeStr;
	}

	public boolean isDelayAction()
	{
		return true;
	}
	
	public void changeConfigTime (String type, String timeStr)
	{
		// TOOD - Test
		if (this.type.equalsIgnoreCase(type))
		{
			time = Integer.parseInt(timeStr);
			statement.replaceAll(this.timeStr, timeStr);
			this.timeStr = timeStr;
		}		
	}
	
	public int getTime()
	{
		return time;
	}
	
	public void setTimeLeft(int left)
	{
		remainTime = left;
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
