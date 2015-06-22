package edu.cmu.team2.iotms.model.ruleManager.RM_Core;

public class DelayAction extends Action {

	private int remainTime = 0;
	
	public DelayAction(String nodeID, String thingID, String value, String type) {
		super(nodeID, thingID, value, type);
	}

	public String getStatement()
	{
		String timeStr = statement; 
		
		if (type.equalsIgnoreCase("Alarm"))
		{
			if (value.equalsIgnoreCase("UnSet"))
				timeStr = timeStr + " in " + Config.getInstance().getMalFuncConfig() + " seconds";
			else
				timeStr = timeStr + " in " + Config.getInstance().getAlarmConfig() + " seconds";
		}
        else if (type.equalsIgnoreCase("Message"))
        {
            timeStr = timeStr + " in " + Config.getInstance().getMalFuncConfig() + " seconds";
        }
        else if (type.equalsIgnoreCase("DoorSensor"))
        {
            timeStr = timeStr + " in " + Config.getInstance().getDoorSensorConfig() + " seconds";
        }
		else 
		{
			timeStr = timeStr + " in " + Config.getInstance().getLightConfig() + " seconds";
		}
		
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
		{
			if (value.equalsIgnoreCase("UnSet"))
				remainTime = Integer.parseInt(Config.getInstance().getMalFuncConfig());
			else
				remainTime = Integer.parseInt(Config.getInstance().getAlarmConfig());
		}
        else if (type.equalsIgnoreCase("Message"))
        {
			remainTime = Integer.parseInt(Config.getInstance().getMalFuncConfig());
        }
        else if (type.equalsIgnoreCase("DoorSensor"))
        {
        	remainTime = Integer.parseInt(Config.getInstance().getDoorSensorConfig());
        }
		else 
		{
			remainTime = Integer.parseInt(Config.getInstance().getLightConfig());
		}
		
		generateJSONMsg();
		Scheduler.getInstance().addAction(this);
	}
}
