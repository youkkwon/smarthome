package edu.cmu.team2.iotms.model.ruleManager.RM_Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Rule;

public class RuleManagerFileStorage extends RuleManagerStroage {

	private int alarm_config = 300;			// 5m by default
	private int light_config = 600;			// 10m by default
	
	private String				rule_filename;
	private String				config_filename;
	private static RuleManagerFileStorage storage = new RuleManagerFileStorage(); 
	
	private RuleManagerFileStorage()
	{
		rule_filename = "RM_resource/IoTMS.rule";
		config_filename = "RM_resource/IoTMS.config";
	}

	public static RuleManagerFileStorage getInstance()
	{
		return storage;		
	}
		
	@SuppressWarnings("unused")
	private void loadConfig () 
	{
		int 	idx = 0;
		String 	input = new String();
				
		try {
			BufferedReader br = new BufferedReader(new FileReader(config_filename));		
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
	
	public void storeConfig() 
	{
		try {
			BufferedWriter output =  new BufferedWriter(new FileWriter(config_filename));			
			output.write("Alarm:" + Integer.toString(alarm_config) + "\n");
			output.write("Light:" + Integer.toString(light_config) + "\n");
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	@Override
	public ListIterator<String> loadRuleSet() {
	
		String 	input = new String();
		LinkedList<String> raw_rules = new LinkedList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(rule_filename));		
			while ( (input=br.readLine()) != null )
			{				
				if (input.length() != 0)
				raw_rules.add(input);										
			} 
			br.close();
		} catch (FileNotFoundException e) {
			// Predefined Rule.
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return raw_rules.listIterator();
	}

	@Override
	public boolean storeRuleSet(LinkedList<Rule> rules) {
		
		try {
			BufferedWriter bw =  new BufferedWriter(new FileWriter(rule_filename));	
			ListIterator<Rule>	iterator = rules.listIterator();
			
			while (iterator.hasNext()) 
			{
				bw.write(iterator.next().getStatement());
				bw.newLine();
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean insertRule(Rule rule) {
		
		return false;
	}

	@Override
	public boolean deleteRule(Rule rule) {
		return false;
	}
}
