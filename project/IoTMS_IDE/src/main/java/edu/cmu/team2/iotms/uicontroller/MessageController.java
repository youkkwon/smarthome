package edu.cmu.team2.iotms.uicontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.cmu.team2.iotms.application.MessageService;
import edu.cmu.team2.iotms.domain.Message;

@Controller
public class MessageController {
	
	@Autowired
	private MessageService messageService;

	@RequestMapping("/messagelist")
	public ModelAndView getEventHistory(@RequestParam("fromdate") String fromdate,@RequestParam("todate") String todate,ModelAndView model) throws IOException{
		List<Message> messages = messageService.list(fromdate, todate);
		model.addObject("messagelist", messages);
		model.setViewName("messagelist");
		return model;
	}
}
