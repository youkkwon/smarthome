/**************************************************************
* File: ClientDemo
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2013 Anthony J. Lattanze
* Versions:
* 1.0 May 2013.
* 1.5 April 2014 - added #define for port id
*
* Description:
*
* This program runs on an Arduino processor with a WIFI shield. 
* This program is a client demo. This runs in a loop communicating
* with a server process that also runs in a loop. The protocol is
* that after we connect to a server, this process sends three '\n'
* terminated strings. The final string is "bye\n" which is a 
* signal to the server that we are done sending data. Then
* this process waits for the server to send a single string back. This illustrates basic client
* server connection and two-way communication.
*
* Compilation and Execution Instructions: Only Compile using 
* Arduino IDE VERSION 1.0.4
*
* Parameters: None
*
* Internal Methods: void printConnectionStatus()
*
************************************************************************************************/

#include <SPI.h>
#include <WiFi.h>
#include <ArduinoJson.h>


#define PORTID  550               // IP socket port ID
#define MAX_TARGET_NUM 5
#define MAX_WIFI_STRING_LENGTH 50
//char ssid[] = "LGTeam2";             // The network SSID 
char ssid[] = "jwhyun";             // The network SSID 
char pass[] = "10293847";             // The network SSID 
//char ssid[] = "Shadyside Inn";             // The network SSID 
//char pass[] = "hotel5405";             // The network SSID 

char c;                           // Character read from server
int status = WL_IDLE_STATUS;      // Network connection status
// IPAddress server(192,168,1,124);  // The server's IP address
//IPAddress server(192,168,1,139);  // The server's IP address
IPAddress server(192,168,10,107);  // The server's IP address
//IPAddress server(10,253,225,74);  // The server's IP address
//IPAddress server(192,168,10,109);  // The server's IP address
WiFiClient client;                // The client (our) socket
IPAddress ip;                     // The IP address of the shield
IPAddress subnet;                 // The IP address of the shield
long rssi;                        // Wifi shield signal strength
byte mac[6];                      // Wifi shield MAC address
char stringbuf[256]={0};




 void setup()  
 {
  // Initialize a serial terminal for debug messages.
  Serial.begin(115200);
  
  Serial.println("Attempting to connect to network...");
  Serial.print("SSID: ");
  Serial.println(ssid);
  

  
  // Attempt to connect to Wifi network.
  while ( status != WL_CONNECTED) 
  { 
     Serial.print("Attempting to connect to SSID: ");
     Serial.println(ssid);
     status = WiFi.begin(ssid,pass);
  }  
   
  Serial.println( "Connected to network:" );
  Serial.println( "\n----------------------------------------" );

  // Print the basic connection and network information.
  printConnectionStatus();

  Serial.println( "\n----------------------------------------\n" );



}

//NodeID
char gNodeID[] = "78:C4:E:2:5C:A3";
//JSON thing list
struct sThingsList
{
  char *id;
  char *Type;
  char *SType;
  char *VType;
  char *VMin;
  char *VMax;
};

const struct sThingsList gThings1 = {"0001","Door"       ,"Actuator","String","Open"  ,"Close"};
const struct sThingsList gThings2 = {"0002","Light"      ,"Actuator","String","On"    ,"Off"  };
const struct sThingsList gThings3 = {"0003","Presence"   ,"Sensor"  ,"String","AtHome","Away" };
const struct sThingsList gThings4 = {"0004","Temperature","Sensor"  ,"Number","-50"   ,"50"   };
const struct sThingsList gThings5 = {"0005","Humidity"   ,"Sensor"  ,"Number","0"     ,"100"  };
const struct sThingsList gThings6 = {"0006","DoorSensor" ,"Sensor"  ,"String","Open"  ,"Close"};
const struct sThingsList gThings7 = {"0007","MailBox"    ,"Sensor"  ,"String","Empty" ,"Mail" };
const struct sThingsList gThings8 = {"0008","Alarm"      ,"Actuator","String","Set"   ,"Unset"};  


//JSON event
struct sThingEvent
{
  char *id;
  char *Type;
  char Value[10];
};
struct sThingEvent gThingEvent[] = {
  {"0003","Presence"   , "AtHome"},
  {"0004","Temperature", "-999"  },
  {"0005","Humidity"   , "100"   },
  {"0006","DoorSensor" , "Close" },
  {"0007","MailBox"    , "Mail"  }
};

void SendJSONobject(char *key, char *value,bool bEnd)
{
   client.write('"');
     client.print(key);
   client.write('"');   
   client.write(':');   
   client.write('"');
     client.print(value);
   client.write('"');   
   if(!bEnd) client.write(',');  
}

void SendJSONthings(const struct sThingsList *thing, bool bEnd)
{
  
  char id[5];
  char Type[15];
  char SType[20];
  char VType[10];
  char VMin[10];
  char VMax[10];
  
  client.write('{');
    SendJSONobject("Id",thing->id,false);
    SendJSONobject("Type",thing->Type,false);
    SendJSONobject("SType",thing->SType,false);
    SendJSONobject("VType",thing->VType,false);
    SendJSONobject("VMin",thing->VMin,false);
    SendJSONobject("VMax",thing->VMax,true);    
  client.write('}');  
  if(!bEnd) client.write(',');  
}

void SendJSONdiscoverRegister(bool bDiscoverRegister)
{
  struct sThingsList thing; 
  client.write('{');    
    if(bDiscoverRegister)
    {
      SendJSONobject("Job","Discovered",false);
      SendJSONobject("NodeID",gNodeID,false); 
    }
    else
    {
      SendJSONobject("Job","Registered",false);
      SendJSONobject("NodeID",gNodeID,false); 
      SendJSONobject("Result","Authorized",false); 
    }
    client.print("\"ThingList\":[");   
      
      SendJSONthings(&gThings1,false);
      SendJSONthings(&gThings2,false);
      SendJSONthings(&gThings3,false);
      SendJSONthings(&gThings4,false);
      SendJSONthings(&gThings5,false);
      SendJSONthings(&gThings6,false);
      SendJSONthings(&gThings7,false);
      SendJSONthings(&gThings8,true);    

    client.write(']');
  client.write('}');
}



void SendJSONthingStatus(struct sThingEvent& thing, bool bEnd)
{
  client.write('{');
    SendJSONobject("Id",thing.id,false);
    SendJSONobject("Type",thing.Type,false);
    SendJSONobject("Value",thing.Value,true);
  client.write('}');  
  if(!bEnd) client.write(',');  
}

void SendJSONstatusEvent(void)
{
  client.write('{');    
    SendJSONobject("Job","Event",false);
    SendJSONobject("NodeID",gNodeID,false); 
    client.print("\"Status\":[");    
      SendJSONthingStatus(gThingEvent[0],false);
      SendJSONthingStatus(gThingEvent[1],false);
      SendJSONthingStatus(gThingEvent[2],false);
      SendJSONthingStatus(gThingEvent[3],false);
      SendJSONthingStatus(gThingEvent[4],true);
    client.write(']');
  client.write('}');
}

void loop() 
{
  // Here we attempt connect to the server on the port specified above
  
 
  int stringbufIndex = 0;
  Serial.print("\nAttempting to connect to server...");
  
  if (client.connect(server, PORTID)) 
  {
      Serial.println("connected");

      // We write a couple of messages to the server
      Serial.print("Server Message: ");
/*
      char c = ' ';      
      stringbufIndex = 0;
      while (1)
      {
        if (client.available()) 
        {
          c = client.read();
          if( c == '\n' )
          {
            stringbuf[stringbufIndex] = 0;
            break;
          }
          stringbuf[stringbufIndex++] = c;
        }
      }       
*/

     while(1)
     {
       SendJSONdiscoverRegister(true);
       client.println();  
       SendJSONdiscoverRegister(false);
       client.println();  
       SendJSONstatusEvent();
       client.println(); 
       delay(3000);      
     }
 
      // That's it. We wait a second, then do it all again.
      client.stop();
      Serial.println();
      Serial.println( "Send JSON message Done...");
      delay(1000);
      
  } // if

} //  LOOP

/************************************************************************************************
* The following method prints out the connection information
************************************************************************************************/

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
