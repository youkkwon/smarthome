package edu.cmu.team2.iotms.model.commManager;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CommUtil {

	private static int discoveryPort = 3251;
	private static int serverPort = 5503;
	
	public static String parseMACAddress(String str)
	{
		JSONObject jsonObj;
		jsonObj = (JSONObject) JSONValue.parse(str);
		
		System.out.println("[CM - Process] json to be parsed: " + str);
		
		String tmp;
		tmp = (String) jsonObj.get("NodeID");
		System.out.println("[CM - Process] MAC Address : " + tmp);
	
		return tmp;
	}
	

	public static String parseJob(String str)
	{
		JSONObject jsonObj;
		jsonObj = (JSONObject) JSONValue.parse(str);
		
		System.out.println("[CM - Process] json to be parsed: " + str);
		
		String tmp;
		tmp = (String) jsonObj.get("Job");
		System.out.println("[CM - Process] Job : " + tmp);
	
		return tmp;
	}
	
	public static int getDiscoveryPort()
	{
		return discoveryPort;
	}
	

	public static int getServerPort()
	{
		return serverPort;
	}
}
