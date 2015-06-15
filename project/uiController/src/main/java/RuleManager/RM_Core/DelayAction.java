package RuleManager.RM_Core;

public class DelayAction extends Action {

	private int remainTime = 0;
	
	public DelayAction(String nodeID, String thingID, String value, String type) {
		super(nodeID, thingID, value, type);
	}

	public String getStatement()
	{
		String timeStr = statement; 
		
		if (type.equalsIgnoreCase("Alarm"))
			timeStr = timeStr + " in " + Config.getInstance().getAlarmConfig() + " seconds";
		else 
			timeStr = timeStr + " in " + Config.getInstance().getLightConfig() + " seconds";
		
		return timeStr;			
	}
	
	public boolean isDelayAction()
	{
		return true;
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
		
		if (type.equalsIgnoreCase("Alarm"))
			remainTime = Integer.parseInt(Config.getInstance().getAlarmConfig());
		else 
			remainTime = Integer.parseInt(Config.getInstance().getLightConfig());
		
		generateJSONMsg();
		Scheduler.getInstance().addAction(this);
	}
}
