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

import Database.NodeDao;

import com.google.common.eventbus.Subscribe;

import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;

public class NewNodeWebSocketHandler extends TextWebSocketHandler {

	private Map<String, WebSocketSession> users;

	public NewNodeWebSocketHandler() {
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
			if (targets.get(i).equals("UIControler") || targets.get(i).equals("UI")) {
				if(JSONMsg.get("Job").toString().compareTo("Discovered") == 0) {
					processDiscover(JSONMsg);
					sendTextMessage(JSONMsg);
				}
				if(JSONMsg.get("Job").toString().compareTo("Registered") == 0) {
					processRegistered(JSONMsg);
					sendTextMessage(JSONMsg);
				}
			}
		}
	}
	
	private void processRegistered(JSONObject JSONMsg) {

		String node_id = JSONMsg.get("NodeID").toString();
		String result = JSONMsg.get("Result").toString();
		
		if(result.toLowerCase().compareTo("authorized")==0) {
			NodeDao.getInstance().authorizedNode(node_id);
		}
		else {
			System.out.println("not authorized : "+JSONMsg.toString());
		}
	}

	private void processDiscover(JSONObject JSONMsg) {
		String node_id = JSONMsg.get("NodeID").toString();
		
		NodeDao.getInstance().setJsonOfNode(node_id, JSONMsg.toString());
		NodeDao.getInstance().deleteThingsInNode(node_id);

		JSONArray thinglist = (JSONArray) JSONMsg.get("ThingList");
		for (int i=0; i < thinglist.size(); i++) {
			JSONObject thingobj = (JSONObject) thinglist.get(i);
			
			NodeDao.getInstance().insertThing(node_id
					, thingobj.get("Id").toString() // thing_id
					,"", // thing_name
					thingobj.get("Type").toString() // type
					,thingobj.get("SType").toString() // stype
					,thingobj.get("VType").toString() // vtype
					,thingobj.get("VMin").toString() // vmin
					,thingobj.get("VMax").toString()); // vmax
		}
	}
	

	private void sendTextMessage(JSONObject JSONMsg) {
		TextMessage message = new TextMessage(JSONMsg.toJSONString());
		
		for (WebSocketSession s : users.values()) {
			try {
				s.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			log(s.getId() + "에 New Node메시지 발송: " + message.getPayload());
		}

	}

}
