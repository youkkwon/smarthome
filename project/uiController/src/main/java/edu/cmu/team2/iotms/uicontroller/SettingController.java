package edu.cmu.team2.iotms.uicontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.cmu.team2.iotms.application.SettingService;
import edu.cmu.team2.iotms.domain.Setting;

@Controller
public class SettingController {
	
	@Autowired
	private SettingService settingService;

	@RequestMapping("/setting")
	public ModelAndView getSetting(ModelAndView model) throws IOException{
		Setting setting = settingService.getSettings();
		model.addObject("setting", setting);
		model.setViewName("setting");
		return model;
	}
	@RequestMapping(value = "/setsetting", method = RequestMethod.POST)
	public String setSetting(@ModelAttribute("setting")
					Setting setting, BindingResult result) throws IOException{
		settingService.setSettings(setting);
		return "setting";
	}
}
