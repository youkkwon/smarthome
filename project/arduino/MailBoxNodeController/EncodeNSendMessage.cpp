
#include <SPI.h>
#include "EncodeNSendMessage.h"
#include <avr/pgmspace.h>

//JSON thing list
//const char gJSONthings1[] PROGMEM ="{\"Id\":\"0001\",\"Type\":\"Door\" 	  ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"Open\"  ,\"VMax\" : \"Close\"},";
//const char gJSONthings2[] PROGMEM ="{\"Id\":\"0002\",\"Type\":\"Light\"	  ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"On\"	 ,\"VMax\" : \"Off\"  },";
//const char gJSONthings3[] PROGMEM ="{\"Id\":\"0003\",\"Type\":\"Presence\"   ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"AtHome\",\"VMax\" : \"Away\" },";
//const char gJSONthings4[] PROGMEM ="{\"Id\":\"0004\",\"Type\":\"Temperature\",\"SType\":\"Sensor\"  ,\"VType\": \"Number\",\"VMin\" : \"-50\"	 ,\"VMax\" : \"50\"   },";
//const char gJSONthings5[] PROGMEM ="{\"Id\":\"0005\",\"Type\":\"Humidity\"   ,\"SType\":\"Sensor\"  ,\"VType\": \"Number\",\"VMin\" : \"0\"	 ,\"VMax\" : \"100\"\ },";
//const char gJSONthings6[] PROGMEM ="{\"Id\":\"0006\",\"Type\":\"DoorSensor\" ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"Open\"  ,\"VMax\" : \"Close\"},";
const char gJSONthings1[] PROGMEM ="{\"Id\":\"0007\",\"Type\":\"MailBox\"	  ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"Empty\" ,\"VMax\" : \"Mail\" }";
//const char gJSONthings8[] PROGMEM ="{\"Id\":\"0008\",\"Type\":\"AlarmLamp\"	  ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"On\"	 ,\"VMax\" : \"Off\"}";

//const char JSONstatus1[] PROGMEM ="{\"Id\":\"0003\",\"Type\":\"Presence\"	 ,\"Value\":\"";
//const char JSONstatus2[] PROGMEM ="{\"Id\":\"0004\",\"Type\":\"Temperature\",\"Value\":\"";
//const char JSONstatus3[] PROGMEM ="{\"Id\":\"0005\",\"Type\":\"Humidity\"	 ,\"Value\":\"";
//const char JSONstatus4[] PROGMEM ="{\"Id\":\"0006\",\"Type\":\"DoorSensor\" ,\"Value\":\"";
const char JSONstatus1[] PROGMEM ="{\"Id\":\"0007\",\"Type\":\"MailBox\"	 ,\"Value\":\"";

#define MAX_WIFI_STRING_LENGTH 70
String gsBufferWiFi;

void EncodeNSendMessage::SendJSONobject(char *key, char *value, bool bEnd)
{
	gsBufferWiFi+='"';
		gsBufferWiFi+=key;
	gsBufferWiFi+='"';   
	gsBufferWiFi+=':';   
	gsBufferWiFi+='"';
		gsBufferWiFi+=value;
	gsBufferWiFi+='"';   

	if(!bEnd) gsBufferWiFi+=',';	  
}

void EncodeNSendMessage::SendJSONdiscoverRegister(WiFiClient localclient , bool bDiscoverRegister, String MacAddr)
{
	gsBufferWiFi="";
	gsBufferWiFi+='{';
	if(bDiscoverRegister)
	{
		SendJSONobject("Job", "Discovered", false);
		SendJSONobject("NodeID", (char *)MacAddr.c_str(), false); 
		Serial.println(gsBufferWiFi);		// for debug
	}
	else
	{
		SendJSONobject("Job", "Registered", false);
		SendJSONobject("NodeID",(char *)MacAddr.c_str(), false); 
		SendJSONobject("Result", "Authorized", false); 
	}
	gsBufferWiFi+="\"ThingList\":[";
	localclient.print(gsBufferWiFi.c_str());

	// nested thing message
	{
		int i;
		//thing1
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(gJSONthings1) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(gJSONthings1 + i);
			if(i>MAX_WIFI_STRING_LENGTH)
			{
				localclient.print(gsBufferWiFi.c_str());
				gsBufferWiFi="";
			}
		}
		localclient.print(gsBufferWiFi.c_str());
		
	
	}
	
	localclient.print("]}\n");
}


void EncodeNSendMessage::SendJSONstatusEvent(WiFiClient localclient, String MacAddr, HomeNodeDDI Hnode)
{
	gsBufferWiFi="";
	gsBufferWiFi+='{';  
	SendJSONobject("Job", "Event", false);
	SendJSONobject("NodeID", (char *)MacAddr.c_str(), false);  
	gsBufferWiFi+="\"Status\":[";
	localclient.print(gsBufferWiFi.c_str());	  

	{
		int i;
		//JSONstatus1  // Presence
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(JSONstatus1) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(JSONstatus1 + i);
		}
		if(Hnode.proximity <= 50)	// door open
		{ 
			gsBufferWiFi+="Mail";
		}
		else
		{ 
			gsBufferWiFi+="Empty"; 	 
		}

		gsBufferWiFi+="\"}";
		localclient.print(gsBufferWiFi.c_str());
	}
	localclient.print("]}\n");
}

void EncodeNSendMessage::SendJSONnotAuthorizedEvent(WiFiClient localclient, String MacAddr)
{
	gsBufferWiFi="";
	gsBufferWiFi+='{';
	SendJSONobject("Job", "Registered", false);
	SendJSONobject("NodeID", (char *)MacAddr.c_str(), false); 
	SendJSONobject("Result", "NotAuthorized", false);
	gsBufferWiFi+="}\n";
	localclient.print(gsBufferWiFi.c_str());
}

void EncodeNSendMessage::SendJSONMsgErrEvent(WiFiClient localclient, String MacAddr)
{
	gsBufferWiFi="";
	gsBufferWiFi+='{';
	SendJSONobject("Job", "ActionCtrl", false);
	SendJSONobject("NodeID", (char *)MacAddr.c_str(), false); 
	SendJSONobject("Result", "ActionCtrlMsgError", false);
	gsBufferWiFi+="}\n";
	localclient.print(gsBufferWiFi.c_str());
}




