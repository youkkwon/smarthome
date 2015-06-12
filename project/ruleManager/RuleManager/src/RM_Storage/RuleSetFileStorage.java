package RM_Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import RM_Core.Rule;

public class RuleSetFileStorage extends RuleSetStroage {

	String				filename;
	private static RuleSetFileStorage storage = new RuleSetFileStorage(); 
	
	private RuleSetFileStorage()
	{
		filename = "IoTMS.rule";
	}

	public static RuleSetFileStorage getInstance()
	{
		return storage;		
	}
	
	
	
	@Override
	public ListIterator<String> loadRuleSet() {
	
		String 	input = new String();
		LinkedList<String> raw_rules = new LinkedList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));		
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
	public boolean insertRule(Rule rule) {
		
		return false;
	}

	@Override
	public boolean deleteRule(Rule rule) {
		return false;
	}
}
