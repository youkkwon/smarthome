
/**************************************************************************************
* File: HomeNodeController
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2015 Anthony J. Lattanze
* Versions:
* 1.0 June 2015 - Initial version
*
* Description:
*
* This program demonstrates how to use the Arduino SA Node(CMU IoT Project). 
* Arduino SA node brief description :
* router - standard Wi-Fi router connected to the CMU network as described above
*	- 1 SA node that includes the following sensors/actuators:
*	- 1 each indoor and outdoor light (LEDs)
*	- 1 temp and humidity sensor
*	- 1 door open-close actuator
*	- 1 door open-close sensor 
*	- 1 alarm (LED: lit=secured, unlit=unsecured)
*	- 1 presence/proximity sensor
*
* This lists the Arduino Pin numbers and the sensors that they are connected to.
* Note that the WIFI Shield uses pins 4, 7, 10, 11, 12, 13 - these can never be used by any SA
* nodes using the Arduino Uno-WIFI Shield combination.
*    - Door open/close [sensor] : pin 2
*    - Door alarm light [actuators]: pin 3
*    - Indoor light [actuators]: pin 5
*    - Proximity [sensor] : pin 6
*    - Temperature/humidity [sensor] : pin 8
*    - Door servo [actuators] : pin 9.
*
* Compilation and Execution Instructions: Compiled using Arduino IDE VERSION 1.6.4
*
*
* Each of these methods are explained in detail where they appear below.
**************************************************************************************/
#include <SPI.h>
#include <WiFi.h>
#include <Servo.h>
#include <HomeNodeDDI.h>           // Note that the DHT file must be in your Arduino installation folder, in the library foler.
#include <MyTimer.h>
#include <ArduinoJson.h>

//#define	CHECK_REGISTER_IOTMS

// main loop state(MLS) define
enum {
	MLS_SET_UP = 0,
	MLS_WIFI_CONNECTTION,
	MLS_CHECK_REGISTED_SERVER,
	MLS_STANDBY_DISCOVERY,
	MLS_REGISTER_NODE_TO_IOTMS,
	MLS_CONNECTING_SERVER,
	MLS_NORMAL_CONTROL,
};

// Actuator command
enum {
	COMMAND_DOOR_OPEN = 1,
	COMMAND_DOOR_CLOSE,
	COMMAND_LIGHT_ON,
	COMMAND_LIGHT_OFF,
	COMMAND_ALARM_SET,
	COMMAND_ALARM_UNSET,
};


#define SERVER_PORT			550
#define CLIENT_PORT			3250
#define SCOMMAND_SIZE		64
#define SSID		"LGTeam2"				
// Node sensor/actuator control variable
HomeNodeDDI HomeNode;
int MainLoopState = 0;
MyTimer SensingTimer;
MyTimer SendStateTimer;

#ifdef CHECK_REGISTER_IOTMS
String SerialCommand;
char Scommand[SCOMMAND_SIZE];
#endif



// Server connection variable
//char ssid[] = "LGTeam2";	  // The network SSID (name) 
int WiFistatus = WL_IDLE_STATUS;		// The status of the network connections
WiFiServer server(SERVER_PORT);	// The WIFI status,.. we are using port 500
WiFiClient ServerClient;
WiFiClient Client;
IPAddress IoTMSIp(192, 168, 1, 143);           // 
IPAddress ip;                 // The IP address of the shield
//IPAddress subnet;            // The subnet we are connected to
//long rssi;                   // The WIFI shield signal strength
byte mac[6];                   // MAC address of the WIFI shield
char inChar;                   // This is a character sent from the client
boolean IoTMSMagReadComplete;                  // Loop flag
String IoTMSCommand;

// Jason message variable
StaticJsonBuffer<128> jsonBuffer;  
String MacAddressString;

int SendDiscoveredMessage(void);

void ConnectToIoTMS(void);

#ifdef CHECK_REGISTER_IOTMS
void CheckRegisterServerForTest(void);
#endif

void RegisterNodeToIoTMS(void);
int RegisterNodeCommandCtl(void);
void StandbyIoTMSConnection(void);
void CheckRegisterServer(void);
void WiFiConnectionState(void);
void NormalControlState(void);
void SendSAnodeStateCtl(void);
int IoTMSCommandCtl(void);
int IoTMSCommandParsing(void);
void ActuaroControl(int);
void SensingNode(void);
void printConnectionStatus(void);

// Jason message send function
void SendJSONobject(WiFiClient, char *, char *, bool);
void SendJSONdiscoverRegister(WiFiClient, bool);
void SendJSONstatusEvent(WiFiClient);
void SendJSONnotAuthorizedEvent(WiFiClient);

void setup() {
	Serial.begin(9600);		// Set up a debug window
	HomeNode.DoorServoAttach();	
	SensingTimer.SetBaseTimer();
	MainLoopState = MLS_WIFI_CONNECTTION;
	//MainLoopState = MLS_NORMAL_CONTROL;
      //TestTimer.SetBaseTimer();
}

void loop() {	
	SensingNode();

	switch(MainLoopState)
	{
		case MLS_SET_UP :
			// not working
			break;

		case MLS_WIFI_CONNECTTION :
			WiFiConnectionState();
			break;

		case MLS_CHECK_REGISTED_SERVER :
			CheckRegisterServer();
			break;

		case MLS_STANDBY_DISCOVERY : 
			StandbyIoTMSConnection();
			break;
                        
		case MLS_REGISTER_NODE_TO_IOTMS :
			RegisterNodeToIoTMS();
			break;
                        
		case MLS_CONNECTING_SERVER :
			ConnectToIoTMS();
			break;

		case MLS_NORMAL_CONTROL :
			NormalControlState();
			break;
	}
}

/*
* MLS_WIFI_CONNECTTION
*
* WiFi connection 
*/
void WiFiConnectionState(void)
{
	// Attempt to connect to WIfI network indicated by SSID.

	//Serial.print("Attempting to connect to SSID: ");
	//Serial.println(ssid);
	WiFistatus = WiFi.begin(SSID);

	if(WiFistatus == WL_CONNECTED)
	{	
		MainLoopState = MLS_CHECK_REGISTED_SERVER;
		printConnectionStatus();
		Serial.print("Connection success ot : ");
		Serial.print(SSID);
		Serial.println(" WiFi network");
		Serial.println("For demo input connection type Server or Client");
	}
}

/*
* MLS_CHECK_REGISTED_SERVER
*
* check Registered server and set next state
* check registered server : confirm eeprom save data 
*/
void CheckRegisterServer(void)
{    
	#ifndef CHECK_REGISTER_IOTMS
	MainLoopState = MLS_STANDBY_DISCOVERY;
	#else
	CheckRegisterServerForTest();
	#endif
}

#ifdef CHECK_REGISTER_IOTMS
void CheckRegisterServerForTest(void)
{
	int i;

	// Read eeprom about IoTMS Register information
	// if IoTMS Register informaton is valid, SA Node connect to IoTMS as client
	// but invalid information, SA Node Stand by connection from IoTMS as server.
	if(Serial.available())
	{		
		//Serial.readBytesUntil('\n', SerialCommand, 32); 
		Serial.readBytesUntil('\n', Scommand, 32);
		SerialCommand += Scommand;
		if(SerialCommand.equals("Server"))
		{
			// servier connection
			Serial.print("User input valid : ");
			Serial.println(SerialCommand);
			Serial.println("Go to the Server connectio state for standby IoTMS Discovery connection");
			MainLoopState = MLS_STANDBY_DISCOVERY;
			server.begin();
		}
		else if(SerialCommand.equals("Client"))
		{		 
			// client connecton
			Serial.print("User input valid : ");
			Serial.println(SerialCommand);
			Serial.println("Go to the Clinet connectio state");
			MainLoopState = MLS_CONNECTING_SERVER;
		}
		else
		{
			Serial.print("User input not valid : ");
			Serial.println(SerialCommand);
			SerialCommand.remove(0);
			for(i = 0 ; i < SCOMMAND_SIZE ; i++)
			{
				Scommand[i] = NULL;
			}
		}
	}
}
#endif

/*
* MLS_STANDBY_DISCOVERY
*
* Wait connection from IoTMS, if connected with IoTMS
* Send Discovered message to IoTMS
*/
void StandbyIoTMSConnection(void)
{
	//delay(1000);
	//Serial.println("=== StandbyToTMSConnection function ===");
	//server.begin();
	ServerClient = server.available();
	if(ServerClient)
	{
		if(ServerClient.connected())
		{
			Serial.println("Connected to client");
			// Send message to IoTMS
			// message : SA Node information
			SendJSONdiscoverRegister(ServerClient, true);    // Send Discovery message 
			MainLoopState = MLS_REGISTER_NODE_TO_IOTMS;
			Serial.println("SA Node wait IoTMS Register message...");
		}
	}
}

/*
* MLS_REGISTER_NODE_TO_IOTMS
*
* After send Discovered message, wait Register message from IoTMS
* if receive Register message, check Message and Serial key
* send Registered or unvalid message or unvalid Serial key message.
*/
void RegisterNodeToIoTMS(void)
{
	if(ServerClient)
	{
		IoTMSMagReadComplete = false;            
		IoTMSCommand = "";
		while(ServerClient.available())
		{
			inChar = ServerClient.read();
			Serial.print(inChar);		// for debugging
			if(inChar == '\n')
			{
				ServerClient.flush();
				IoTMSMagReadComplete = true;
			}
			else
			{	
				IoTMSCommand += inChar;
			}
		}

		if(IoTMSMagReadComplete == true)
		{
			if(RegisterNodeCommandCtl() == -1)
			{
				SendJSONnotAuthorizedEvent(ServerClient);		// send Not Authorized message
			}
			else
			{	
				SendJSONdiscoverRegister(ServerClient, false);	// Send Discovery message 
				ServerClient.stop();
				MainLoopState = MLS_CONNECTING_SERVER;
				Serial.println("=== ConnectToIoTMS function ===");
			}
		}
	}
}

int RegisterNodeCommandCtl(void)
{
	Serial.print("IoTMS Message : ");
	Serial.println(IoTMSCommand);		// for debugging

	// Json parsing
	// if parsing result is Register SA node, Send Registered message
	// and Connect client connection after Close server connection close.
	JsonObject& iotmsMsg = jsonBuffer.parseObject((char *)IoTMSCommand.c_str());

	if (!iotmsMsg.success()) 
	{
		Serial.println("parseObject() failed");
		return -1;
	}
	else
	{
		String PasingMsg;
		PasingMsg = iotmsMsg["Jop"];
		if(!PasingMsg.equals("Register"))
		{
			// invalid Jop, send NotAuthorized message
			return -1;
		}

		PasingMsg = iotmsMsg["NodeID"];
		if(!PasingMsg.equals(MacAddressString))
		{
			// invalid Node ID, send NotAuthorized message
			return -1;
		}

		PasingMsg = iotmsMsg["URL"];
		// IoTMS IP Address 저장 처리한다. - 이부분은 나중에 구현하고 현재는 코드상에서 입력하여 진행 한다.  
		
		PasingMsg = iotmsMsg["Port"];
		// IoTMS와 연결할 Port number 저장 처리한다. - 이부분은 나중에 구현하고 현재는 코드상에서 입력하여 진행 한다.  
		
		PasingMsg = iotmsMsg["SerialNumber"];
		if(!PasingMsg.equals("12345678"))
		{
			// invalid Serial Number, send NotAuthorized message
			return -1;
		}

		return 1;
	}
}

void ConnectToIoTMS(void)
{
	if(Client.connect(IoTMSIp, CLIENT_PORT))
	{
		Serial.println();
		Serial.println("Connect success with IoTMS");

		MainLoopState = MLS_NORMAL_CONTROL;
		SendStateTimer.SetBaseTimer();
	}
}

/*
* 1. Sense / Actuator control
* 2. IoTMS Communication control (Send / Recieve)
*/
void NormalControlState(void)
{
	int ActuatorCommand = 0;

	SendSAnodeStateCtl();
	ActuatorCommand = IoTMSCommandCtl();
	ActuaroControl(ActuatorCommand);
}

/*
* Every 3 second, Send SA node Status to IoTMS
* State : Presence, Temperature, Huidity, Door Sensing Result
*/
void SendSAnodeStateCtl(void)
{
	if(SendStateTimer.CheckPassTime(3000, 0) == 1)
	{
		SendJSONstatusEvent(Client);
	}
}

/*
enum {
	DOOR_OPEN = 0,
	DOOR_CLOSE,
	LIGHT_ON,
	LIGHT_OFF,
	ALARM_SET,
	ALARM_UNSET,
};
*/
int IoTMSCommandCtl(void)
{
	int result = 0;

	if(Client.connected())
	{
		IoTMSMagReadComplete = false;
		IoTMSCommand = "";
		while(Client.available())
		{
			inChar = Client.read();
			Serial.print(inChar);		// for debugging
			if(inChar == '\n')
			{
				Client.flush();
				IoTMSMagReadComplete = true;
			}
			else
			{	
				IoTMSCommand += inChar;
			}
		}

		if(IoTMSMagReadComplete == true)
		{	
			result = IoTMSCommandParsing();
		}
	}
	else
	{
		;	// ToDo : Disconnected Process 
	}
	
	return result;
}

/*
*	Command jason format
* 	"Job":"ActionCtrl",
*	"NodeID":"12:12:12:12:12:12",
*	"Id":"0001",
*	"Type":"Door",
*	"Value":"Open",
*/
int IoTMSCommandParsing(void)
{
	Serial.print("IoTMS Command : ");
	Serial.println(IoTMSCommand);		// for debugging

	// Json parsing
	// if parsing result is Register SA node, Send Registered message
	// and Connect client connection after Close server connection close.
	JsonObject& iotmsCommand = jsonBuffer.parseObject((char *)IoTMSCommand.c_str());

	if (!iotmsCommand.success()) 
	{
		Serial.println("Command message parseObject() failed");
		return 0;
	}
	else
	{
		String PasingMsg;
		PasingMsg = iotmsCommand["Jop"];
		if(!PasingMsg.equals("ActionCtrl"))
		{
			// invalid Jop
			return 0;
		}

		PasingMsg = iotmsCommand["NodeID"];
		if(!PasingMsg.equals(MacAddressString))
		{
			// invalid Node ID
			return 0;
		}

		PasingMsg = iotmsCommand["Type"];
		if(PasingMsg.equals("Door"))
		{
			PasingMsg = iotmsCommand["Value"];
			if(PasingMsg.equals("Open"))
			{	
				return COMMAND_DOOR_OPEN;
			}
			else if(PasingMsg.equals("Close"))
			{
				return COMMAND_DOOR_CLOSE;
			}
			else
			{	return 0;
			}
		}
		else if(PasingMsg.equals("Light"))
		{
			PasingMsg = iotmsCommand["Value"];
			if(PasingMsg.equals("On"))
			{	
				return COMMAND_LIGHT_ON;
			}
			else if(PasingMsg.equals("Off"))
			{
				return COMMAND_LIGHT_OFF;
			}
			else
			{	return 0;
			}
		}
		else if(PasingMsg.equals("Alarm"))
		{
			PasingMsg = iotmsCommand["Value"];
			if(PasingMsg.equals("Set"))
			{	
				return COMMAND_ALARM_SET;
			}
			else if(PasingMsg.equals("Unset"))
			{
				return COMMAND_ALARM_UNSET;
			}
			else
			{	return 0;
			}
		}
		else
		{	return 0;
		}
	}
}

/*
* Acturator control functio According to Command
*/
void ActuaroControl(int Command)
{
	switch(Command)
	{
		case COMMAND_DOOR_OPEN :
			HomeNode.DoorControl(DOOR_OPEN);
			break;

		case COMMAND_DOOR_CLOSE :
			HomeNode.DoorControl(DOOR_CLOSE);
			break;

		case COMMAND_LIGHT_ON :
			HomeNode.IndoorLightControl(ON);
			break;

		case COMMAND_LIGHT_OFF :
			HomeNode.IndoorLightControl(OFF);
			break;

		case COMMAND_ALARM_SET :
			HomeNode.SecureAlarmLightControl(ON);
			break;

		case COMMAND_ALARM_UNSET :
			HomeNode.SecureAlarmLightControl(OFF);
			break;

		default :
			break;
		
	}
}

/*
* Sensing temperature, humidity, Door, proximity Every 1second.
*/
void SensingNode(void)
{
        //Serial.print("Sensing base time : ");
        //Serial.println(SensingTimer.BaseTime);
	if(SensingTimer.CheckPassTime(1000, 0) == 1)
	{
		// ToDo : check every 1 second
		HomeNode.readDoorState();
		HomeNode.readProximityVal();
		HomeNode.read1byte_tempNhumidity();

		/*
		Serial.println("===========================");
		Serial.print("Door State : ");
		Serial.println(HomeNode.doorstate);
		Serial.print("Proximity Value : ");
		Serial.println(HomeNode.proximity);
		Serial.print("Temperature Value : ");
		Serial.println(HomeNode.temperature);
		Serial.print("Humidity Value : ");
		Serial.println(HomeNode.humidity);
		Serial.println("===========================");
		*/
	}
	
}

void printConnectionStatus() 
{
	// Print the basic connection and network information: Network, IP, and Subnet mask
	ip = WiFi.localIP();
	Serial.print("Connected to ");
	Serial.print(SSID);
	Serial.print(" IP Address:: ");
	Serial.println(ip);
	//subnet = WiFi.subnetMask();
	//Serial.print("Netmask: ");
	//Serial.println(subnet);

	// Print our MAC address.
	WiFi.macAddress(mac);
	Serial.print("WiFi Shield MAC address ");
	int i;
	for(i = 5 ; i > -1 ; i--)
	{
		MacAddressString += String(mac[i], HEX);
		if(i != 0) MacAddressString += ":";
	}
	Serial.print("String object mac address : ");                    
	Serial.println(MacAddressString);

	// Print the wireless signal strength:
	//rssi = WiFi.RSSI();
	//Serial.print("Signal strength (RSSI): ");
	//Serial.print(rssi);
	//Serial.println(" dBm");
} 
 // printConnectionStatus
 
/*****************************************************************
*
* Jason message create function
*
******************************************************************/

#include <avr/pgmspace.h>
//JSON thing list
const char gJSONthings1[] PROGMEM ="{\"Id\":\"0001\",\"Type\":\"Door\" 	  ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"Open\"  ,\"VMax\" : \"Close\"},";
const char gJSONthings2[] PROGMEM ="{\"Id\":\"0002\",\"Type\":\"Light\"	  ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"On\"	 ,\"VMax\" : \"Off\"  },";
const char gJSONthings3[] PROGMEM ="{\"Id\":\"0003\",\"Type\":\"Presence\"   ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"AtHome\",\"VMax\" : \"Away\" },";
const char gJSONthings4[] PROGMEM ="{\"Id\":\"0004\",\"Type\":\"Temperature\",\"SType\":\"Sensor\"  ,\"VType\": \"Number\",\"VMin\" : \"-50\"	 ,\"VMax\" : \"50\"   },";
const char gJSONthings5[] PROGMEM ="{\"Id\":\"0005\",\"Type\":\"Humidity\"   ,\"SType\":\"Sensor\"  ,\"VType\": \"Number\",\"VMin\" : \"0\"	 ,\"VMax\" : \"100\"\ },";
const char gJSONthings6[] PROGMEM ="{\"Id\":\"0006\",\"Type\":\"DoorSensor\" ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"Open\"  ,\"VMax\" : \"Close\"},";
const char gJSONthings7[] PROGMEM ="{\"Id\":\"0007\",\"Type\":\"MailBox\"	  ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"Empty\" ,\"VMax\" : \"Mail\" },";
const char gJSONthings8[] PROGMEM ="{\"Id\":\"0008\",\"Type\":\"Alarm\"	  ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"Set\"	 ,\"VMax\" : \"Unset\"}";

const char JSONstatus1[] PROGMEM ="{\"Id\":\"0003\",\"Type\":\"Presence\"	 ,\"Value\":\"";
const char JSONstatus2[] PROGMEM ="{\"Id\":\"0004\",\"Type\":\"Temperature\",\"Value\":\"";
const char JSONstatus3[] PROGMEM ="{\"Id\":\"0005\",\"Type\":\"Humidity\"	 ,\"Value\":\"";
const char JSONstatus4[] PROGMEM ="{\"Id\":\"0006\",\"Type\":\"DoorSensor\" ,\"Value\":\"";
const char JSONstatus5[] PROGMEM ="{\"Id\":\"0007\",\"Type\":\"MailBox\"	 ,\"Value\":\"";


void SendJSONobject(WiFiClient localclient, char *key, char *value, bool bEnd)
{
	localclient.write('"');
		localclient.print(key);
	localclient.write('"');   
	localclient.write(':');   
	localclient.write('"');
		localclient.print(value);
	localclient.write('"');   

	if(!bEnd) localclient.write(',');  
}

void SendJSONdiscoverRegister(WiFiClient localclient , bool bDiscoverRegister)
{
	//struct sThingsList thing; 
	localclient.write('{');	  
		if(bDiscoverRegister)
		{
			SendJSONobject(localclient, "Job", "Discovered", false);
			SendJSONobject(localclient, "NodeID", (char *)MacAddressString.c_str(), false); 
		}
		else
		{
			SendJSONobject(localclient, "Job", "Registered", false);
			SendJSONobject(localclient, "NodeID",(char *)MacAddressString.c_str(), false); 
			SendJSONobject(localclient, "Result", "Authorized", false); 
		}

		localclient.print("\"ThingList\":[");	 
		{
			int i;
			char ch=0;
			for(i = 0 ; i < strlen(gJSONthings1) ; i++) localclient.write(pgm_read_byte_near(gJSONthings1 + i));
			for(i = 0 ; i < strlen(gJSONthings2) ; i++) localclient.write(pgm_read_byte_near(gJSONthings2 + i));
			for(i = 0 ; i < strlen(gJSONthings3) ; i++) localclient.write(pgm_read_byte_near(gJSONthings3 + i));
			for(i = 0 ; i < strlen(gJSONthings4) ; i++) localclient.write(pgm_read_byte_near(gJSONthings4 + i));
			for(i = 0 ; i < strlen(gJSONthings5) ; i++) localclient.write(pgm_read_byte_near(gJSONthings5 + i));
			for(i = 0 ; i < strlen(gJSONthings6) ; i++) localclient.write(pgm_read_byte_near(gJSONthings6 + i));
			for(i = 0 ; i < strlen(gJSONthings7) ; i++) localclient.write(pgm_read_byte_near(gJSONthings7 + i));
			for(i = 0 ; i < strlen(gJSONthings8) ; i++) localclient.write(pgm_read_byte_near(gJSONthings8 + i));
		}
		localclient.write(']');
	localclient.write('}');
	localclient.write('\n');
}


void SendJSONstatusEvent(WiFiClient localclient)
{
	localclient.write('{');	  
		SendJSONobject(localclient, "Job", "Event", false);
		SendJSONobject(localclient, "NodeID", (char *)MacAddressString.c_str(), false); 
		localclient.print("\"Status\":[");    
		{
			int i;
			char ch=0;

			// ToDo :  Thing status value 값은 감지된 값으로 변경 필요.
			// Presence
			for(i = 0 ; i < strlen(JSONstatus1) ; i++) localclient.write(pgm_read_byte_near(JSONstatus1 + i));		
			if(HomeNode.proximity <= 50)		// door open
			{	localclient.print("AtHome");
			}
			else
			{	localclient.print("Away");
			}
			localclient.print("\"}," );

			// Temperature
			for(i = 0 ; i < strlen(JSONstatus2) ; i++) localclient.write(pgm_read_byte_near(JSONstatus2 + i)); 
			localclient.print(HomeNode.temperature);
			localclient.print("\"}," );

			// Humidity
			for(i = 0 ; i < strlen(JSONstatus3) ; i++) localclient.write(pgm_read_byte_near(JSONstatus3 + i)); 
			localclient.print(HomeNode.humidity);
			localclient.print("\"}," );

			// DoorSensor
			for(i = 0 ; i < strlen(JSONstatus4) ; i++) localclient.write(pgm_read_byte_near(JSONstatus4 + i)); 
			if(HomeNode.doorstate == 0)		// door open
			{	localclient.print("Open");
			}
			else
			{	localclient.print("Close");
			}
			localclient.print("\"}," );
		}
		localclient.write(']');
	localclient.write('}');	
	localclient.write('\n');
}

void SendJSONnotAuthorizedEvent(WiFiClient localclient)
{
	localclient.write('{');	  
		SendJSONobject(localclient, "Job", "Event", false);
		SendJSONobject(localclient, "NodeID", (char *)MacAddressString.c_str(), false); 
		SendJSONobject(localclient, "Result", "NotAuthorized", false);
	localclient.write('}');
	localclient.write('\n');
}




