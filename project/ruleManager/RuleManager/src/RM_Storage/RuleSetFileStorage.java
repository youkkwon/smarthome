package RM_Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import RM_Core.Rule;
import RM_Core.RuleSet;

public class RuleSetFileStorage extends RuleSetStroage {

	String				filename;

	public RuleSetFileStorage()
	{
		filename = "IoTMS.rule";
	}
	
	private int countChar (String string, char ch)
	{
		int		count = 0;
		char[] array = string.toCharArray();
		
		for (int i=0; i < array.length; i++)
			if (array[i] == ch) count++;
		
		return count;
	}
	
	@Override
	public LinkedList<Rule> RoadRuleSet() {
		int		idx = 0, beginIndex = 0, endIndex = 0;
		String 	input = new String();
		RuleSet	ruleset = RuleSet.getInstance();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));		
			while ( (input=br.readLine()) != null )
			{
				idx = input.indexOf("then");
				String condStr = input.substring(2, idx).trim();
				String actStr = input.substring(idx+4).trim();
		
				String[] conditions = new String[countChar(condStr, ',')];
				String[] actions = new String[countChar(actStr, ',')];
				for (int i=0; i < conditions.length; i++)
				{
					endIndex = condStr.indexOf(',', beginIndex);
					conditions[i] = condStr.substring(beginIndex, endIndex).trim();
					beginIndex = endIndex+1;
				}
						
				for (int i=0; i < actions.length; i++)
				{
					endIndex = actStr.indexOf(',', beginIndex);
					actions[i] = actStr.substring(beginIndex, endIndex).trim();
					beginIndex = endIndex+1;
				}
				ruleset.addRule(conditions, actions);												
			} 
			br.close();
		} catch (FileNotFoundException e) {
			// Predefined Rule.
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean StoreRuleSet(LinkedList<Rule> rules) {
		try {
			BufferedWriter bw =  new BufferedWriter(new FileWriter(filename));	
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
	public boolean InsertRule(Rule rule) {
		return false;
	}

	@Override
	public boolean DeleteRule(Rule rule) {
		return false;
	}

}
