package edu.cmu.team2.iotms.uicontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.cmu.team2.iotms.application.UserServiceImpl;
import edu.cmu.team2.iotms.domain.UserInfo;

@Controller
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@RequestMapping("/userlist")
	public ModelAndView getUserList(ModelAndView model) throws IOException{
		List<UserInfo> userlist = userService.getUsers();
		model.addObject("userlist", userlist);
		model.setViewName("userlist");
		return model;
	}
	@RequestMapping(value = "/user/create", method = RequestMethod.POST)
	public String createUser(
			@RequestParam(value="userId", required=true) String userId, 
			@RequestParam(value="userName", required=false) String userName,
			@RequestParam(value="userMail", required=false) String userMail,
			@RequestParam(value="userTwitter", required=false) String userTwitter,
			@RequestParam(value="userPasswd", required=false) String userPasswd) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setUserName(userName);
		userInfo.setUserMail(userMail);
		userInfo.setUserTwitter(userTwitter);
		userInfo.setUserPasswd(userPasswd);
		
		userService.createUser(userInfo);
		
		return "userlist";
	}
	@RequestMapping(value = "/user/remove", method = RequestMethod.POST)
	public String removeUser(
			@RequestParam(value="userId", required=true) String userId, 
			@RequestParam(value="userName", required=false) String userName,
			@RequestParam(value="userMail", required=false) String userMail,
			@RequestParam(value="userTwitter", required=false) String userTwitter,
			@RequestParam(value="userPasswd", required=false) String userPasswd) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setUserName(userName);
		userInfo.setUserMail(userMail);
		userInfo.setUserTwitter(userTwitter);
		userInfo.setUserPasswd(userPasswd);
		
		userService.removeUser(userInfo);
		
		return "redirect:/userlist";
	}
	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public String updateUser(
			@RequestParam(value="userId", required=true) String userId, 
			@RequestParam(value="userName", required=false) String userName,
			@RequestParam(value="userMail", required=false) String userMail,
			@RequestParam(value="userTwitter", required=false) String userTwitter,
			@RequestParam(value="userPasswd", required=false) String userPasswd) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setUserName(userName);
		userInfo.setUserMail(userMail);
		userInfo.setUserTwitter(userTwitter);
		userInfo.setUserPasswd(userPasswd);
		
		userService.updateUser(userInfo);
		
		return "redirect:/userlist";
	}
}
