package edu.cmu.team2.iotms.application;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.common.eventbus.Subscribe;

import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;

public class RuleWebSocketHandler extends TextWebSocketHandler {

	private Map<String, WebSocketSession> users;

	public RuleWebSocketHandler() {
		super();
		
		users = new ConcurrentHashMap<>();
		
		IoTMSEventBus.getInstance().register(this);
	}

	@Override
	public void afterConnectionEstablished(
			WebSocketSession session) throws Exception {
		log(session.getId() + " 연결 됨");
		users.put(session.getId(), session);
	}

	@Override
	public void afterConnectionClosed(
			WebSocketSession session, CloseStatus status) throws Exception {
		log(session.getId() + " 연결 종료됨");
		users.remove(session.getId());
	}

	@Override
	protected void handleTextMessage(
			WebSocketSession session, TextMessage message) throws Exception {
		log(session.getId() + "로부터 메시지 수신: " + message.getPayload());
		for (WebSocketSession s : users.values()) {
			s.sendMessage(message);
			log(s.getId() + "에 메시지 발송: " + message.getPayload());
		}
	}

	@Override
	public void handleTransportError(
			WebSocketSession session, Throwable exception) throws Exception {
		log(session.getId() + " 익셉션 발생: " + exception.getMessage());
	}

	private void log(String logmsg) {
		System.out.println(new Date() + " : " + logmsg);
	}
	
	@Subscribe
	public void SubscribeEvent(JSONObject JSONMsg) {
		JSONArray targets = (JSONArray) JSONMsg.get("Targets");
		for (int i=0; i < targets.size(); i++) {
			if (targets.get(i).equals("UIControler")) {
				if(JSONMsg.get("Job").toString().compareTo("RuleSearch") == 0)
					processSearch(JSONMsg);
			}
		}
	}
	
//	private void processSearch(JSONObject JSONMsg) {
//		JSONArray rules = (JSONArray) JSONMsg.get("Rules");
//		
//		ruleSet.clear();
//		for (int i=0; i < rules.size(); i++) {
//			RuleInfo ruleinfo = new RuleInfo();
//			ruleinfo.setRuleId(String.format("%d", i));
//			ruleinfo.setRuleSet(rules.get(i).toString());
//			
//			System.out.println("ruleset : "+ruleinfo.getRuleSet());
//			ruleSet.add(ruleinfo);
//		}
//	}
	
	private void processSearch(JSONObject JSONMsg) {
		TextMessage message = new TextMessage(JSONMsg.toJSONString());
		
		for (WebSocketSession s : users.values()) {
			try {
				s.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			log(s.getId() + "에 Rule 메시지 발송: " + message.getPayload());
		}

	}

}
