
#ifndef EncodNSendMessage_h
#define EncodNSendMessage_h

#if ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif

#include <SPI.h>
#include <WiFi.h>
#include "HomeNodeDDI.h"           // Note that the DHT file must be in your Arduino installation folder, in the library foler.

class EncodeNSendMessage
{
	public : 
		void SendJSONobject(WiFiClient, char *, char *, bool);
		void SendJSONdiscoverRegister(WiFiClient , bool, String);
		void SendJSONstatusEvent(WiFiClient, String, HomeNodeDDI);
		void SendJSONnotAuthorizedEvent(WiFiClient, String);
		void SendJSONMsgErrEvent(WiFiClient, String);
};

#endif

