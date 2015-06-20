package edu.cmu.team2.iotms.uicontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.cmu.team2.iotms.application.NodeService;
import edu.cmu.team2.iotms.domain.NodeInfo;

@Controller
public class NodeController {
	
	@Autowired
	private NodeService nodeService;
	private static final String REGISTERED = "1";
	private static final String UNREGISTERED = "0";

	@RequestMapping("/nodelist")
	public ModelAndView getNodeList(ModelAndView model) throws IOException{
		List<NodeInfo> nodes = nodeService.getNodeList(REGISTERED);
		
		for(NodeInfo node:nodes) {
			node.setThings(nodeService.getThingList(node.getNode_id()));
		}
		
		model.addObject("nodelist", nodes);
		model.setViewName("nodelist");
		return model;
	}
	
	@RequestMapping("/newnode")
	public ModelAndView newNode(ModelAndView model) throws IOException{
		List<NodeInfo> nodes = nodeService.getNewNodes();
		
		model.addObject("newnode", nodes);
		model.setViewName("newnode");
		
		return model;
	}
	
	@RequestMapping("/node/discover")
	public String discoverNodes(ModelAndView model) throws IOException{
		nodeService.discoverNodes();
		return "newnode";
	}
	
	@RequestMapping("/node/register")
	public String registerNode(@RequestParam("nodeid") String nodeid
			,@RequestParam("serial") String serial
			,ModelAndView model) throws IOException{
		nodeService.registerNode(nodeid, serial);
		return "newnode";
	}
	
	@RequestMapping("/node/testnode")
	public String testNode(@RequestParam("nodeid") String nodeid
			,@RequestParam("thingid") String thingid
			,@RequestParam("type") String type
			,@RequestParam("value") String value
			,ModelAndView model) throws IOException{
		nodeService.testNode(nodeid, thingid, type, value);
		return "";
	}

	@RequestMapping("/thing/control")
	public String controlThing(@RequestParam("nodeid") String nodeid
			,@RequestParam("thingid") String thingid
			,@RequestParam("type") String type
			,@RequestParam("value") String value) {
		
		nodeService.controlThing(nodeid, thingid, type, value);
		
		return "redirect:/nodelist";
	}
}
