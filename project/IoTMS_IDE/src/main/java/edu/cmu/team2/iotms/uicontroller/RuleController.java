package edu.cmu.team2.iotms.uicontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.cmu.team2.iotms.application.RuleService;
import edu.cmu.team2.iotms.domain.RuleInfo;

@Controller
public class RuleController {
	
	@Autowired
	private RuleService ruleService;

	@RequestMapping("/rulelist")
	public ModelAndView getRulesetList(ModelAndView model) throws IOException{
		List<RuleInfo> rulelist = ruleService.getRuleset();
		model.addObject("rulelist", rulelist);
		model.setViewName("rulelist");
		return model;
	}
//	@RequestMapping("/rulesearch")
//	public String getRulesetsearch() {
//		ruleService.searchRuleset();
//
//		return "rulelist";
//	}
	@RequestMapping(value = "/rule/create", method = RequestMethod.POST)
	public String createRuleset(
			@RequestParam(value="ruleSet", required=false) String ruleSet) {
		RuleInfo ruleInfo = new RuleInfo();
		ruleInfo.setRuleSet(ruleSet);
		
		ruleService.createRuleSet(ruleInfo);

		return "redirect:/rulelist";
	}
	@RequestMapping(value = "/rule/remove", method = RequestMethod.POST)
	public String removeRuleset(
			@RequestParam(value="ruleSet_ID", required=false) String ruleId,
			@RequestParam(value="ruleSet", required=false) String ruleset) {
		RuleInfo ruleInfo = new RuleInfo();
		ruleInfo.setRuleId(ruleId);
		ruleInfo.setRuleSet(ruleset);

		ruleService.removeRuleSet(ruleInfo);

		return "redirect:/rulelist";
	}
	@RequestMapping(value = "/rule/update", method = RequestMethod.POST)
	public String updateRuleset(
			@RequestParam(value="ruleSet", required=false) String ruleSet) {
		RuleInfo ruleInfo = new RuleInfo();
		ruleInfo.setRuleSet(ruleSet);

		ruleService.updateRuleSet(ruleInfo);

		return "redirect:/rulelist";
	}
	
	@RequestMapping(value = "/rule/confirm", method = RequestMethod.POST)
	public String confirmMessage(@RequestParam(value="yesno", required=false) String yesno) {
		ruleService.confirm(yesno);

		return "";
	}
}
