
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
#include <ArduinoJson.h>
#include <EEPROM.h>
#include "HomeNodeDDI.h"           // Note that the DHT file must be in your Arduino installation folder, in the library foler.
#include "MyTimer.h"
#include "MyEeprom.h"
#include "EncodeNSendMessage.h"


#define	CHECK_REGISTER_IOTMS
#define	MANUAL_SET_SENSOR

// main loop state(MLS) define
enum {
	MLS_SET_UP = 0,
	MLS_WIFI_CONNECTTION,
	MLS_CHECK_REGISTED_SERVER,
	MLS_STANDBY_DISCOVERY,
	MLS_REGISTER_NODE_TO_IOTMS,
	MLS_CONNECTING_SERVER,
	MLS_NORMAL_CONTROL,
	MLS_REMOVE_NODE,
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
#define SSID					"LGTeam2"	
#define SERIAL_NUMBER		"12345678"

/*
* Node sensor/actuator control variable
*/
HomeNodeDDI HomeNode;
int MainLoopState;
MyTimer SensingTimer;
MyTimer SendStateTimer;

#ifdef CHECK_REGISTER_IOTMS
String SerialCommand;
char Scommand[SCOMMAND_SIZE];
#endif

#ifdef MANUAL_SET_SENSOR
boolean ManualSetSensor;
#endif


/*
* Server connection variable
*/
//char ssid[] = "LGTeam2";	  // The network SSID (name) 
int WiFistatus = WL_IDLE_STATUS;		// The status of the network connections
WiFiServer server(SERVER_PORT);	// The WIFI status,.. we are using port 500
WiFiClient ServerClient;
WiFiClient Client;
IPAddress ip;                 // The IP address of the shield
//IPAddress subnet;            // The subnet we are connected to
//long rssi;                   // The WIFI shield signal strength
byte mac[6];                   // MAC address of the WIFI shield
String MacAddressString;

char inChar;                   // This is a character from the client
boolean IoTMSMagReadComplete;                  // Loop flag
String IoTMSCommand;

MyEeprom EEpCtl;
EncodeNSendMessage SendToIoTMS;

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
void ActuatorControl(int);
void SensingNode(void);
void RemoveNode(void);

void SetWiFiConnectionStatus(void);
void printWiFiConnectionStatus(void);
int CheckWiFiNetWork(void);

void CheckManualSensingInput(void);


// Eeprom control function
/**
int CheckIoTMSRegistrationStatus(void);
String GetIoTMSIpAddressFromEeprom(void);
int GetIoTMSPortNumberFromEeprom(void);
void SaverIoTMSInformationToEeprom(String, int);
void ResetEeprom(int);
void InitilaizeEeprom(void);
**/

// Jason message send function
/*
void SendJSONobject(WiFiClient, char *, char *, bool);
void SendJSONdiscoverRegister(WiFiClient, bool);
void SendJSONstatusEvent(WiFiClient);
void SendJSONnotAuthorizedEvent(WiFiClient);
*/

void setup() {
	Serial.begin(115200);		// Set up a debug window
	HomeNode.DoorServoAttach();	
	SensingTimer.SetBaseTimer();
	EEpCtl.InitilaizeEeprom();
	//EEpCtl.ResetEeprom(1023);
	MainLoopState = MLS_WIFI_CONNECTTION;
	//MainLoopState = MLS_NORMAL_CONTROL;
      //TestTimer.SetBaseTimer();

#ifdef MANUAL_SET_SENSOR
	ManualSetSensor = false;
#endif
}

void loop() {	

	
#ifdef MANUAL_SET_SENSOR
	CheckManualSensingInput();
#endif
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

		case MLS_REMOVE_NODE :
			RemoveNode();
			break;

		default :
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

	if(WiFi.status() == WL_NO_SHIELD) 
	{
		Serial.println("WiFi shield not present");
		delay(1000);
		return;
	}
	
	//Serial.print("Attempting to connect to SSID: ");
	//Serial.println(ssid);
	WiFistatus = WiFi.begin(SSID);

	if(WiFistatus == WL_CONNECTED)
	{	
		MainLoopState = MLS_CHECK_REGISTED_SERVER;
		SetWiFiConnectionStatus();
		printWiFiConnectionStatus();

#ifdef	CHECK_REGISTER_IOTMS
		Serial.println("For demo input connection type Server or Client :");
#endif
	}
	else
	{	
		Serial.println("WiFi not connect");
		delay(1000);
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
	if(CheckWiFiNetWork() == -1)
	{	return;
	}
	
	#ifndef CHECK_REGISTER_IOTMS
	if(EEpCtl.CheckIoTMSRegistrationStatus())
	{	MainLoopState = MLS_CONNECTING_SERVER;
	}
	else
	{			
		server.begin();
		Serial.println("Stand by IoTMS");
		MainLoopState = MLS_STANDBY_DISCOVERY;
	}
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
			Serial.print("input valid : ");
			Serial.println(SerialCommand);
			//Serial.println("Go to the Server connectio state for standby IoTMS Discovery connection");
			MainLoopState = MLS_STANDBY_DISCOVERY;
			server.begin();
		}
		else if(SerialCommand.equals("Client"))
		{		 
			// client connecton
			Serial.print("input valid : ");
			Serial.println(SerialCommand);
			//Serial.println("Go to the Clinet connectio state");
			MainLoopState = MLS_CONNECTING_SERVER;
		}
		else
		{
			//Serial.print("input not valid : ");
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

#ifdef MANUAL_SET_SENSOR
void CheckManualSensingInput(void)
{
	int Spaceindex;
	
	if(MainLoopState != MLS_CHECK_REGISTED_SERVER)
	{
		if(Serial.available())
		{		
			int i;
			for(i = 0 ; i < SCOMMAND_SIZE ; i++)
			{
				Scommand[i] = '\0';
			}
			//Serial.readBytesUntil('\n', SerialCommand, 32); 
			Serial.readBytesUntil('\n', Scommand, 32);
			SerialCommand = Scommand;
			Serial.println(SerialCommand);
			if(SerialCommand.equals("manual"))
			{
				if(ManualSetSensor == false)
				{	
					ManualSetSensor = true;
				}
				else
				{
					ManualSetSensor = false;
				}
			}
			else
			{
				// temperature, humidity option
				Spaceindex = SerialCommand.indexOf(' ');
				String Stemp;
				Stemp = SerialCommand.substring(0, Spaceindex);
				//Serial.println(Stemp);
				//if(SerialCommand.substring(0, Spaceindex - 1) == "temperature")
				if(Stemp.equals("temperature"))
				{
					//String Stemp;
					Stemp = SerialCommand.substring(Spaceindex + 1);
					//Serial.println(Stemp);
					HomeNode.temperature = Stemp.toInt();
					Serial.println(HomeNode.temperature);
				}
				//else if(SerialCommand.substring(0, Spaceindex - 1) == "humidity")
				if(Stemp.equals("humidity"))
				{
					//String Stemp;
					Stemp = SerialCommand.substring(Spaceindex + 1);
					//Serial.println(Stemp);
					HomeNode.humidity = Stemp.toInt();
					Serial.println(HomeNode.humidity);
				}
				else
				{
					Serial.println(SerialCommand);
				}
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
	if(CheckWiFiNetWork() == -1)
	{	return;
	}
	
	//delay(1000);
	//Serial.println("=== StandbyToTMSConnection function ===");
	//server.begin();
	ServerClient = server.available();
	if(ServerClient)
	{
		if(ServerClient.connected())
		{
			Serial.println("Connected with IoTMS");
			// Send message to IoTMS
			// message : SA Node information
			SendToIoTMS.SendJSONdiscoverRegister(ServerClient, true, MacAddressString);    // Send Discovery message 
			MainLoopState = MLS_REGISTER_NODE_TO_IOTMS;
			//Serial.println("Wait IoTMS Register msg");
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
	if(CheckWiFiNetWork() == -1)
	{	return;
	}
	
	if(ServerClient.connected())
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
				SendToIoTMS.SendJSONnotAuthorizedEvent(ServerClient, MacAddressString);		// send Not Authorized message
			}
			else
			{	
				Serial.println("Receive & Send regist msg");
				//SendJSONdiscoverRegister(ServerClient, false);	// Send Discovery message 
				SendToIoTMS.SendJSONdiscoverRegister(ServerClient, false, MacAddressString);
				//ServerClient.stop();
				MainLoopState = MLS_CONNECTING_SERVER;
			}
		}
	}
	else
	{	MainLoopState = MLS_STANDBY_DISCOVERY;
		ServerClient.stop();
	}
}

int RegisterNodeCommandCtl(void)
{
	StaticJsonBuffer<160> jsonBuffer;  
	
	Serial.print("IoTMS Msg : ");
	Serial.println(IoTMSCommand);		// for debugging

	// Json parsing
	// if parsing result is Register SA node, Send Registered message
	// and Connect client connection after Close server connection close.
	JsonObject& iotmsMsg = jsonBuffer.parseObject((char *)IoTMSCommand.c_str());

	if (!iotmsMsg.success()) 
	{
		Serial.println("parse  failed");
		return -1;
		//return 1;
	}
	else
	{
		String PasingMsg;
		PasingMsg = iotmsMsg["Job"];
		if(!PasingMsg.equals("Register"))
		{
			// invalid Jop, send NotAuthorized message
			Serial.println(PasingMsg);		// for debugging
			return -1;
		}

		PasingMsg = iotmsMsg["NodeID"];
		if(!PasingMsg.equals(MacAddressString))
		{
			// invalid Node ID, send NotAuthorized message
			Serial.println(PasingMsg);		// for debugging
			return -1;
		}

		PasingMsg = iotmsMsg["URL"];
		// Save IoTMS IP Address to Eeprom. 
		EEpCtl.SaverIoTMSInformationToEeprom(PasingMsg, EEP_IOTMS_IP_START_ADDR, EEP_IP_END_ADDR);
		
		PasingMsg = iotmsMsg["Port"];
		// Save IoTMS Port number to Eeprom.
		EEpCtl.SaverIoTMSInformationToEeprom(PasingMsg, EEP_IOTMS_PORT_START_ADDR, EEP_PORTNUM_END_ADDR);

		char ch = '1';
		EEpCtl.SaveIoTMSRegistrationStatus(ch);
		
		PasingMsg = iotmsMsg["SerialNumber"];
		if(!PasingMsg.equals(SERIAL_NUMBER))
		{
			// invalid Serial Number, send NotAuthorized message
			Serial.println(PasingMsg);		// for debugging
			return -1;
		}

		return 1;
	}
}

/*
* MLS_CONNECTING_SERVER
*
*/ 
void ConnectToIoTMS(void)
{
	int portnumber;
	String IpAddr;

	if(CheckWiFiNetWork() == -1)
	{	return;
	}

	portnumber = EEpCtl.GetIoTMSPortNumberFromEeprom();
	IpAddr = EEpCtl.GetIoTMSIpAddressFromEeprom();

	Serial.println("Try Connect to IoTMS");
	Serial.print("IoTMS Ip addr and port : ");
	Serial.print(IpAddr);
	Serial.print(" and ");
	Serial.println(portnumber);

	Client.stop();
	if(Client.connect((char *)IpAddr.c_str(), portnumber))
	{
		Serial.println();
		Serial.println("Connect success with IoTMS");

		MainLoopState = MLS_NORMAL_CONTROL;
		SendStateTimer.SetBaseTimer();
	}
	else
	{
		delay(1000);
		int status = WiFi.status();
		Serial.println(status);
		
	}
}

/*
* MLS_NORMAL_CONTROL
*
* 1. Sense / Actuator control
* 2. IoTMS Communication control (Send / Recieve)
*/
void NormalControlState(void)
{
	int ActuatorCommand = 0;

	SendSAnodeStateCtl();
	ActuatorCommand = IoTMSCommandCtl();
	ActuatorControl(ActuatorCommand);
}

/*
* Every 3 second, Send SA node Status to IoTMS
* State : Presence, Temperature, Huidity, Door Sensing Result
*/
void SendSAnodeStateCtl(void)
{
	if(SendStateTimer.CheckPassTime(3000, 0) == 1)
	{
		if(Client.connected())
		{
			Serial.println("=== Send sensing data to IoTMS");
			SendToIoTMS.SendJSONstatusEvent(Client, MacAddressString, HomeNode);
		}
		else
		{
			Serial.println("=== Soket disconnected");

			// For retry connecting to IoTMS, set MLS_CONNECTING_SERVER step
			Client.stop();		// Stop current connection before retry connection 
			MainLoopState = MLS_CONNECTING_SERVER;
		}
	}
}

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
	StaticJsonBuffer<160> jsonBuffer;  
	
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
		PasingMsg = iotmsCommand["Job"];
		if(!PasingMsg.equals("ActionCtrl"))
		{
			// invalid Jop
			Serial.print("Invalid jop : ");
			Serial.println(PasingMsg);
			return 0;
		}

		PasingMsg = iotmsCommand["NodeID"];
		if(!PasingMsg.equals(MacAddressString))
		{
			// invalid Node ID
			Serial.print("Invalid Mac address (System and Recieve mac): ");
			Serial.println(MacAddressString);
			Serial.print(" and ");
			Serial.println(PasingMsg);
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
			{	
				Serial.print("Door command error : ");
				Serial.println(PasingMsg);
				return 0;
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
			{	
				Serial.print("Light command error : ");
				Serial.println(PasingMsg);
				return 0;
			}
		}
		else if(PasingMsg.equals("AlarmLamp"))
		{
			PasingMsg = iotmsCommand["Value"];
			if(PasingMsg.equals("On"))
			{	
				return COMMAND_ALARM_SET;
			}
			else if(PasingMsg.equals("Off"))
			{
				return COMMAND_ALARM_UNSET;
			}
			else
			{	
				Serial.print("Alarm command error : ");
				Serial.println(PasingMsg);
				return 0;
			}
		}
		else if(PasingMsg.equals("Remove"))
		{
			MainLoopState = MLS_REMOVE_NODE;
			return 0;
		}
		else
		{	
			Serial.print("No thing : ");
			Serial.println(PasingMsg);
			return 0;
		}
	}
}

/*
* Acturator control functio According to Command
*/
void ActuatorControl(int Command)
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
			//message error, send error message to IoTMS
			SendToIoTMS.SendJSONMsgErrEvent(ServerClient, MacAddressString);
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

#ifdef MANUAL_SET_SENSOR
		if(ManualSetSensor == true)
		{	
			Serial.print(HomeNode.temperature);
			Serial.print(" : ");
			Serial.println(HomeNode.humidity);
			return;
		}
#endif

		// ToDo : check every 1 second
		HomeNode.readDoorState();
		HomeNode.readProximityVal();
		HomeNode.read1byte_tempNhumidity();

		Serial.print(HomeNode.doorstate);
		Serial.print(" : ");
		Serial.print(HomeNode.proximity);
		Serial.print(" : ");
		Serial.print(HomeNode.temperature);
		Serial.print(" : ");
		Serial.println(HomeNode.humidity);
		/*
		Serial.print("Door State : ");
		Serial.println(HomeNode.doorstate);
		Serial.print("Proximity Value : ");
		Serial.println(HomeNode.proximity);
		Serial.print("Temperature Value : ");
		Serial.println(HomeNode.temperature);
		Serial.print("Humidity Value : ");
		Serial.println(HomeNode.humidity);
		*/
	}
	
}

void RemoveNode(void)
{
	// soket disconnect
	//server.stop();
	ServerClient.stop();
	Client.stop();

	// Erase eeprom IoTMS information.
	// Not erase ip address and port number.
	EEpCtl.SaveIoTMSRegistrationStatus('0');

	MainLoopState = MLS_CHECK_REGISTED_SERVER;
}


/*
* After connected to WiFi network and SA node working
* check wifi network, if disconnected, set  
*/
int CheckWiFiNetWork(void)
{
	int status = WiFi.status();

	if(status == WL_CONNECT_FAILED ||  status == WL_CONNECTION_LOST || status == WL_DISCONNECTED)
	{
		Serial.println("WiFi connection error");
		MainLoopState = MLS_WIFI_CONNECTTION;
		ServerClient.stop();
		Client.stop();
		WiFi.disconnect();
		return -1;
	}

	return 1;
}

/*
* Set WliFi Connection information
*/
void SetWiFiConnectionStatus(void)
{
	ip = WiFi.localIP();
	//subnet = WiFi.subnetMask();
	WiFi.macAddress(mac);
	int i;
	for(i = 5 ; i > -1 ; i--)
	{
		MacAddressString += String(mac[i], HEX);
		if(i != 0) MacAddressString += ":";
	}
	//rssi = WiFi.RSSI();
}

/*
* Print to Serial monitor about WiFi connection information
*/
void printWiFiConnectionStatus(void) 
{
	// Print the basic connection and network information: Network, IP, and Subnet mask
	Serial.print("Connected to ");
	Serial.println(SSID);
	Serial.print("IP Address:: ");
	Serial.println(ip);
	//Serial.print("Netmask: ");
	//Serial.println(subnet);

	// Print our MAC address.
	Serial.print("WiFi Shield MAC : ");                    
	Serial.println(MacAddressString);

	// Print the wireless signal strength:
	//Serial.print("Signal strength (RSSI): ");
	//Serial.print(rssi);
	//Serial.println(" dBm");
} 

