package edu.cmu.team2.iotms.model.ruleManager.RM_Storage;

import java.util.LinkedList;
import java.util.ListIterator;

import Database.RuleSetDao;
import edu.cmu.team2.iotms.model.ruleManager.RM_Core.Rule;

public class RuleManagerDBStorage extends RuleManagerStroage {
	private static RuleManagerDBStorage storage = new RuleManagerDBStorage(); 
	
	private RuleManagerDBStorage() {
	}

	public static RuleManagerDBStorage getInstance() {
		return storage;		
	}
	
	@Override
	public ListIterator<String> loadRuleSet() {
		return RuleSetDao.getInstance().loadRuleSet();
	}

	@Override
	public boolean storeRuleSet(LinkedList<Rule> rules) {
		RuleSetDao dao = RuleSetDao.getInstance();
		
		dao.deleteAllRules();
		ListIterator<Rule>	iterator = rules.listIterator();
		
		while (iterator.hasNext()) 
		{
			dao.insertRule(iterator.next().getStatement());
		}
		
		return false;
	}

	@Override
	public boolean insertRule(Rule rule) {
		return RuleSetDao.getInstance().insertRule(rule.getStatement());
	}

	@Override
	public boolean deleteRule(Rule rule) {
		return RuleSetDao.getInstance().deleteRule(rule.getStatement());
	}
	
	public int loadRuleAlarmConfig() {
		return RuleSetDao.getInstance().loadRuleAlarmConfig();
	}

	public int loadRuleLightOffConfig() {
		return RuleSetDao.getInstance().loadRuleLightOffConfig();
	}
	
	public boolean storeRuleAlarmConfig(int config) {
		return RuleSetDao.getInstance().storeRuleAlarmConfig(config);
	}

	public boolean storeRuleLightOffConfig(int config) {
		return RuleSetDao.getInstance().storeRuleLightOffConfig(config);
	}

    public int loadRuleMalFuncConfig() {
		return RuleSetDao.getInstance().loadRuleMalFuncCConfig();
	}

    public boolean storeRuleMalFuncConfig(int config) {
		return RuleSetDao.getInstance().storeRuleMalFuncConfig(config);
	}
    
    public int loadRuleDoorSensorConfig() {
		return RuleSetDao.getInstance().loadRuleDoorSensorCConfig();
	}

    public boolean storeRuleDoorSensorConfig(int config) {
		return RuleSetDao.getInstance().storeRuleDoorSensorCConfig(config);
	}
}
