
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

#define CONNECT_PORT	550
#define SCOMMAND_SIZE   64

// Node sensor/actuator control variable
HomeNodeDDI HomeNode;
int MainLoopState = 0;
MyTimer SensingTimer;
MyTimer TestTimer;
int testindex = 0;
String SerialCommand;
char Scommand[SCOMMAND_SIZE];

// Server connection variable
char ssid[] = "LGTeam2";	// The network SSID (name) 
int WiFistatus = WL_IDLE_STATUS;	// The status of the network connections
WiFiServer server(CONNECT_PORT);	// The WIFI status,.. we are using port 500
WiFiClient client;
IPAddress ServerIp;		// 
IPAddress ip;				// The IP address of the shield
IPAddress subnet;			// The subnet we are connected to
long rssi;                     // The WIFI shield signal strength
byte mac[6];                   // MAC address of the WIFI shield
char inChar;                   // This is a character sent from the client
char command;                  // This is the actual command character
boolean done;                  // Loop flag
 
void ConnectToIoTMS(void);
void RegisterNodeToIoTMS(void);
void StandbyIoTMSConnection(void);
void CheckRegisterServer(void);
void WiFiConnectionState(void);
void NormalControlState(void);
void SensingNode(void);
void printConnectionStatus(void);

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
        //Serial.print("Current arduino time : ");
        //Serial.println(millis());
	//delay(500);
}


void ConnectToIoTMS(void)
{
        delay(1000);
        Serial.println("=== ConnectToIoTMS function ===");
}


void StandbyIoTMSConnection(void)
{
        //delay(1000);
        //Serial.println("=== StandbyToTMSConnection function ===");
        //server.begin();
        client = server.available();
        if(client)
        {
                if(client.connected())
                {
                        Serial.println("Connected to client");
                        MainLoopState = MLS_REGISTER_NODE_TO_IOTMS;
                }
        }
}

void RegisterNodeToIoTMS(void)
{
        if(client)
        {
                Serial.println("Client Service Requested...");
                done = false;
                while(!done)
                {
                        if(client.available())
                        {
                                inChar = client.read();
                                if(inChar == '\n')
                                        done = true;
                                else
                                        command = inChar;
                        }
                }
                Serial.print( "command = " );      // We print the command to the terminal
                Serial.println( command );
                
                // The command interpreter starts here. Bascally, we act on the single character command.
                switch ( command )
                {
                        case 'Q': // This is a query command. Basically the protocol call is that after the client
                        case 'q': // connects we send our MAC address.
                                Serial.println( "Client Query,... Sending MAC" );
                                client.print(mac[5],HEX);
                                client.print(":");
                                client.print(mac[4],HEX);
                                client.print(":");
                                client.print(mac[3],HEX);
                                client.print(":");
                                client.print(mac[2],HEX);
                                client.print(":");
                                client.print(mac[1],HEX);
                                client.print(":");
                                client.println(mac[0],HEX);
                                break;
       
                        //More commands could be added here...
          
                        default: // If we don't know what the command is, we just say so and exit.
                                Serial.print( "Unrecognized Command:: " );
                                Serial.println( command );
    
                } // switch
     
                // To finish up the command processing we flush the input, stop the connection, 
                // and print a message to the terminal.
                client.flush();       
                client.stop();
                Serial.println( "Client Disconnected.\n" );
                Serial.println( "........................." );
        }
}

/*
* WiFi connection 
*/
void WiFiConnectionState(void)
{
	// Attempt to connect to WIfI network indicated by SSID.

	//Serial.print("Attempting to connect to SSID: ");
	//Serial.println(ssid);
	WiFistatus = WiFi.begin(ssid);

	if(WiFistatus == WL_CONNECTED)
	{	
		MainLoopState = MLS_CHECK_REGISTED_SERVER;
                printConnectionStatus();
                Serial.print("Connection success ot : ");
                Serial.print(ssid);
                Serial.println(" WiFi network");
                Serial.println("For demo input connection type Server or Client");
	}
}

/*
* check Registered server and set next state
* check registered server : confirm eeprom save data 
*/
void CheckRegisterServer(void)
{       int i;

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

/*
* 1. Sense / Actuator control
* 2. IoTMS Communication control (Send / Recieve)
*/
void NormalControlState(void)
{
        //Serial.print("Test base time : ");
        //Serial.println(TestTimer.BaseTime);
	if(TestTimer.CheckPassTime(2000, 0) == 0)
	{	
		return;
	}
	
	switch(testindex)
	{
		case 0 :
			HomeNode.DoorControl(DOOR_CLOSE);
			break;

		case 1 :
			HomeNode.IndoorLightControl(ON);
			break;

		case 2 :
			HomeNode.SecureAlarmLightControl(ON);
			break;
			
		case 3 :
			HomeNode.DoorControl(DOOR_OPEN);
			break;

		case 4 :
			HomeNode.IndoorLightControl(OFF);
			break;

		case 5 :
			HomeNode.SecureAlarmLightControl(OFF);
			break;

		default :
			break;
	}

	if(testindex >= 5)
	{	
		testindex = 0;
	}
	else
	{	
		testindex++;
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
     Serial.print(ssid);
     Serial.print(" IP Address:: ");
     Serial.println(ip);
     subnet = WiFi.subnetMask();
     Serial.print("Netmask: ");
     Serial.println(subnet);
   
     // Print our MAC address.
     WiFi.macAddress(mac);
     Serial.print("WiFi Shield MAC address: ");
     Serial.print(mac[5],HEX);
     Serial.print(":");
     Serial.print(mac[4],HEX);
     Serial.print(":");
     Serial.print(mac[3],HEX);
     Serial.print(":");
     Serial.print(mac[2],HEX);
     Serial.print(":");
     Serial.print(mac[1],HEX);
     Serial.print(":");
     Serial.println(mac[0],HEX);
   
     // Print the wireless signal strength:
     rssi = WiFi.RSSI();
     Serial.print("Signal strength (RSSI): ");
     Serial.print(rssi);
     Serial.println(" dBm");

 } // printConnectionStatus

