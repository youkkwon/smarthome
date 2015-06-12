package RM_Core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

	private int alarm_config = 300;			// 5m by default
	private int light_config = 600;			// 10m by default
	private String filename 	= "IoTMS.config";
			
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
	
	private void loadConfig () {
		
		int 	idx = 0;
		String 	input = new String();
				
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));		
			while ( (input=br.readLine()) != null )
			{
				idx = input.indexOf(":");
				if (input.startsWith("Alarm"))
					alarm_config = Integer.parseInt(input.substring(idx+1));
				else if (input.startsWith("Light"))
					light_config = Integer.parseInt(input.substring(idx+1));
					
			} 
			br.close();
		} catch (FileNotFoundException e) {
			storeConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void storeConfig() {
		
		try {
			BufferedWriter output =  new BufferedWriter(new FileWriter(filename));			
			output.write("Alarm:" + Integer.toString(alarm_config) + "\n");
			output.write("Light:" + Integer.toString(light_config) + "\n");
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
