package edu.cmu.team2.iotms.application;

import java.util.List;

import edu.cmu.team2.iotms.domain.RuleInfo;

public interface RuleService {
	public List<RuleInfo> getRuleset();
	public void searchRuleset();
	public void createRuleSet(RuleInfo ruleInfo);
	public void removeRuleSet(RuleInfo ruleInfo);
	public void updateRuleSet(RuleInfo ruleInfo);
	public void confirm(String yesno);
}
