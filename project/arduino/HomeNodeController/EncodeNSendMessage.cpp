
#include "EncodeNSendMessage.h"
#include <avr/pgmspace.h>

//JSON thing list
const char gJSONthings1[] PROGMEM ="{\"Id\":\"0001\",\"Type\":\"Door\" 	  ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"Open\"  ,\"VMax\" : \"Close\"},";
const char gJSONthings2[] PROGMEM ="{\"Id\":\"0002\",\"Type\":\"Light\"	  ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"On\"	 ,\"VMax\" : \"Off\"  },";
const char gJSONthings3[] PROGMEM ="{\"Id\":\"0003\",\"Type\":\"Presence\"   ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"AtHome\",\"VMax\" : \"Away\" },";
const char gJSONthings4[] PROGMEM ="{\"Id\":\"0004\",\"Type\":\"Temperature\",\"SType\":\"Sensor\"  ,\"VType\": \"Number\",\"VMin\" : \"-50\"	 ,\"VMax\" : \"50\"   },";
const char gJSONthings5[] PROGMEM ="{\"Id\":\"0005\",\"Type\":\"Humidity\"   ,\"SType\":\"Sensor\"  ,\"VType\": \"Number\",\"VMin\" : \"0\"	 ,\"VMax\" : \"100\"\ },";
const char gJSONthings6[] PROGMEM ="{\"Id\":\"0006\",\"Type\":\"DoorSensor\" ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"Open\"  ,\"VMax\" : \"Close\"},";
const char gJSONthings7[] PROGMEM ="{\"Id\":\"0007\",\"Type\":\"MailBox\"	  ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"Empty\" ,\"VMax\" : \"Mail\" },";
const char gJSONthings8[] PROGMEM ="{\"Id\":\"0008\",\"Type\":\"AlarmLamp\"	  ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"On\"	 ,\"VMax\" : \"Off\"}";

const char JSONstatus1[] PROGMEM ="{\"Id\":\"0003\",\"Type\":\"Presence\"	 ,\"Value\":\"";
const char JSONstatus2[] PROGMEM ="{\"Id\":\"0004\",\"Type\":\"Temperature\",\"Value\":\"";
const char JSONstatus3[] PROGMEM ="{\"Id\":\"0005\",\"Type\":\"Humidity\"	 ,\"Value\":\"";
const char JSONstatus4[] PROGMEM ="{\"Id\":\"0006\",\"Type\":\"DoorSensor\" ,\"Value\":\"";
const char JSONstatus5[] PROGMEM ="{\"Id\":\"0007\",\"Type\":\"MailBox\"	 ,\"Value\":\"";

#define MAX_WIFI_STRING_LENGTH 70
String gsBufferWiFi;

void EncodeNSendMessage::SendJSONobject(WiFiClient localclient, char *key, char *value, bool bEnd)
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
		SendJSONobject(localclient, "Job", "Discovered", false);
		SendJSONobject(localclient, "NodeID", (char *)MacAddr.c_str(), false); 
	}
	else
	{
		SendJSONobject(localclient, "Job", "Registered", false);
		SendJSONobject(localclient, "NodeID",(char *)MacAddr.c_str(), false); 
		SendJSONobject(localclient, "Result", "Authorized", false); 
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
		
		//thing2
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(gJSONthings2) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(gJSONthings2 + i);
			if(i>MAX_WIFI_STRING_LENGTH)
			{
				localclient.print(gsBufferWiFi.c_str());
				gsBufferWiFi="";
			}
		}
		localclient.print(gsBufferWiFi.c_str());
		
		//thing3
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(gJSONthings3) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(gJSONthings3 + i);
			if(i>MAX_WIFI_STRING_LENGTH)
			{
				localclient.print(gsBufferWiFi.c_str());
				gsBufferWiFi="";
			}
		}
		localclient.print(gsBufferWiFi.c_str());
		
		//thing4
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(gJSONthings4) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(gJSONthings4 + i);
			if(i>MAX_WIFI_STRING_LENGTH)
			{
				localclient.print(gsBufferWiFi.c_str());
				gsBufferWiFi="";
			}
		}
		localclient.print(gsBufferWiFi.c_str());
		
		//thing5
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(gJSONthings5) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(gJSONthings5 + i);
			if(i>MAX_WIFI_STRING_LENGTH)
			{
				localclient.print(gsBufferWiFi.c_str());
				gsBufferWiFi="";
			}
		}
		localclient.print(gsBufferWiFi.c_str());
		
		//thing6
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(gJSONthings6) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(gJSONthings6 + i);
			if(i>MAX_WIFI_STRING_LENGTH)
			{
				localclient.print(gsBufferWiFi.c_str());
				gsBufferWiFi="";
			}
		}
		localclient.print(gsBufferWiFi.c_str());
		
		//thing7
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(gJSONthings7) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(gJSONthings7 + i);
			if(i>MAX_WIFI_STRING_LENGTH)
			{
				localclient.print(gsBufferWiFi.c_str());
				gsBufferWiFi="";
			}
		}
		localclient.print(gsBufferWiFi.c_str());
		
		//thing8
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(gJSONthings8) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(gJSONthings8 + i);
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
	Serial.print(millis());
	
	gsBufferWiFi="";
	gsBufferWiFi+='{';  
	SendJSONobject(localclient, "Job", "Event", false);
	SendJSONobject(localclient, "NodeID", (char *)MacAddr.c_str(), false);  
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
			gsBufferWiFi+="AtHome";
		}
		else
		{ 
			gsBufferWiFi+="Away"; 	 
		}
		gsBufferWiFi+="\"},";
		localclient.print(gsBufferWiFi.c_str());
		
		//JSONstatus2 temperature
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(JSONstatus2) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(JSONstatus2 + i);
		}
		gsBufferWiFi+=Hnode.temperature;
		gsBufferWiFi+="\"},";
		localclient.print(gsBufferWiFi.c_str());

		//JSONstatus3 humidity
		gsBufferWiFi="";
		for(i = 0 ; i < strlen(JSONstatus3) ; i++) 
		{
			gsBufferWiFi+=(char)pgm_read_byte_near(JSONstatus3 + i);
		}
		gsBufferWiFi += Hnode.humidity;
		gsBufferWiFi += "\"},";
		localclient.print(gsBufferWiFi.c_str());

		//JSONstatus4  DoorSensor
		gsBufferWiFi = "";
		for(i = 0 ; i < strlen(JSONstatus4) ; i++) 
		{
			gsBufferWiFi += (char)pgm_read_byte_near(JSONstatus4 + i);
		}
		if(Hnode.doorstate == 0)   // door open
		{ 
			gsBufferWiFi += "Open";
		}
		else
		{ 
			gsBufferWiFi += "Close";
		}
		gsBufferWiFi+="\"}";
		localclient.print(gsBufferWiFi.c_str());
	}
	localclient.print("]}\n");

	Serial.print(" : ");
	Serial.println(millis());
}

void EncodeNSendMessage::SendJSONnotAuthorizedEvent(WiFiClient localclient, String MacAddr)
{
	gsBufferWiFi="";
	//localclient.write('{');	
	gsBufferWiFi+='{';
	SendJSONobject(localclient, "Job", "Event", false);
	SendJSONobject(localclient, "NodeID", (char *)MacAddr.c_str(), false); 
	SendJSONobject(localclient, "Result", "NotAuthorized", false);
	gsBufferWiFi+="}\n";
	localclient.print(gsBufferWiFi.c_str());
}




