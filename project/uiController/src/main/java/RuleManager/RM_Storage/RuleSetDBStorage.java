package RuleManager.RM_Storage;

import java.util.LinkedList;
import java.util.ListIterator;

import Database.RuleSetDao;
import RuleManager.RM_Core.Rule;

public class RuleSetDBStorage extends RuleSetStroage {
	private static RuleSetDBStorage storage = new RuleSetDBStorage(); 
	
	private RuleSetDBStorage() {
	}

	public static RuleSetDBStorage getInstance() {
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
}
