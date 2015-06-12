package RM_Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import RM_Core.Rule;

public class RuleSetFileStorage extends RuleSetStroage {

	String				filename;
	BufferedReader		input;
	BufferedWriter 		output;

	public RuleSetFileStorage()
	{
		filename = "RuleManager.txt";
	}
	
	@Override
	public LinkedList<Rule> RoadRuleSet() {
		
		try {
			input =  new BufferedReader(new FileReader(filename));
			
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean StoreRuleSet(LinkedList<Rule> ruleset) {
		
		try {
			output =  new BufferedWriter(new FileWriter(filename));
			
			output.close();
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
