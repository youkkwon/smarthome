package edu.cmu.team2.iotms.model.ruleManager.RM_Core;

import edu.cmu.team2.iotms.model.ruleManager.RM_Storage.RuleManagerDBStorage;

public class Config {

	private int alarm_config = 300;			// 5m by default
	private int light_config = 600;			// 10m by default
    private int malfunc_config = 10;        // 10s by default
			
	private static Config config = new Config(); 

	private Config()
	{
		loadConfig();
	}
	
	public static Config getInstance()
	{
		return config;		
	}

	public void setConfig (String type, int time)
	{
		if (type.equalsIgnoreCase("Alarm"))
			alarm_config = time;
		else if (type.equalsIgnoreCase("Light"))
			light_config = time;
        else if (type.equalsIgnoreCase("MalFunc"))
            malfunc_config = time;
		
		storeConfig();
	}
	
	public String getAlarmConfig()
	{
		return Integer.toString(alarm_config); 
	}
	
	public String getLightConfig()
	{
		return Integer.toString(light_config); 
	}

   	public String getMalFuncConfig()
	{
		return Integer.toString(malfunc_config); 
	}

	private void loadConfig () {
		alarm_config = RuleManagerDBStorage.getInstance().loadRuleAlarmConfig();
		light_config = RuleManagerDBStorage.getInstance().loadRuleLightOffConfig();
		malfunc_config = RuleManagerDBStorage.getInstance().loadRuleMalFuncConfig();
	}
	
	
	public void storeConfig() {
		RuleManagerDBStorage.getInstance().storeRuleAlarmConfig(alarm_config);
		RuleManagerDBStorage.getInstance().storeRuleLightOffConfig(light_config);
		RuleManagerDBStorage.getInstance().storeRuleMalFuncConfig(malfunc_config);
	}

}
