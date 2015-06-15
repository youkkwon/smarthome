package edu.cmu.team2.iotms.uicontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.cmu.team2.iotms.application.EventHistoryService;
import edu.cmu.team2.iotms.domain.EventHistory;

@Controller
public class EventHistoryController {
	
	@Autowired
	private EventHistoryService eventHistoryService;

	@RequestMapping("/eventhistory")
	public ModelAndView getEventHistory(@RequestParam("fromdate") String fromdate,@RequestParam("todate") String todate,ModelAndView model) throws IOException{
		List<EventHistory> eventhistory = eventHistoryService.list(fromdate, todate);
		model.addObject("eventhistory", eventhistory);
		model.setViewName("eventhistory");
		return model;
	}
}
