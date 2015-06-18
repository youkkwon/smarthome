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
//#define PORTID  5503               // IP socket port ID

#define MAX_TARGET_NUM 5

char ssid[] = "LGTeam2";             // The network SSID 
//char ssid[] = "jwhyun";             // The network SSID 
//char pass[] = "10293847";             // The network SSID 
//char ssid[] = "Shadyside Inn";             // The network SSID 
//char pass[] = "hotel5405";             // The network SSID 

char c;                           // Character read from server
int status = WL_IDLE_STATUS;      // Network connection status
// IPAddress server(192,168,1,124);  // The server's IP address
IPAddress server(192,168,1,139);  // The server's IP address
//IPAddress server(192,168,10,107);  // The server's IP address
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
     status = WiFi.begin(ssid);
  }  
   
  Serial.println( "Connected to network:" );
  Serial.println( "\n----------------------------------------" );

  // Print the basic connection and network information.
  printConnectionStatus();

  Serial.println( "\n----------------------------------------\n" );



}

/************************************************************************************************/
/************************************************************************************************/
/************************************************************************************************/
/************************************************************************************************/
/************************************************************************************************/
//
//  JSON message
//
/************************************************************************************************/
/************************************************************************************************/
/************************************************************************************************/
/************************************************************************************************/

//NodeID

#include <Servo.h>
struct HomeNodeDDI
{
  double humidity;
  double temperature;
  long proximity;
  int BeIndoors;
  int doorstate;

} HomeNode =
{
 98,12,34,0,1
};

String MacAddressString= "12:23:34:45:56:67";
#include <avr/pgmspace.h>
//JSON thing list
const char gJSONthings1[] PROGMEM ="{\"Id\":\"0001\",\"Type\":\"Door\"       ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"Open\"  ,\"VMax\" : \"Close\"},";
const char gJSONthings2[] PROGMEM ="{\"Id\":\"0002\",\"Type\":\"Light\"      ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"On\"    ,\"VMax\" : \"Off\"  },";
const char gJSONthings3[] PROGMEM ="{\"Id\":\"0003\",\"Type\":\"Presence\"   ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"AtHome\",\"VMax\" : \"Away\" },";
const char gJSONthings4[] PROGMEM ="{\"Id\":\"0004\",\"Type\":\"Temperature\",\"SType\":\"Sensor\"  ,\"VType\": \"Number\",\"VMin\" : \"-50\"   ,\"VMax\" : \"50\"   },";
const char gJSONthings5[] PROGMEM ="{\"Id\":\"0005\",\"Type\":\"Humidity\"   ,\"SType\":\"Sensor\"  ,\"VType\": \"Number\",\"VMin\" : \"0\"     ,\"VMax\" : \"100\"\ },";
const char gJSONthings6[] PROGMEM ="{\"Id\":\"0006\",\"Type\":\"DoorSensor\" ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"Open\"  ,\"VMax\" : \"Close\"},";
const char gJSONthings7[] PROGMEM ="{\"Id\":\"0007\",\"Type\":\"MailBox\"    ,\"SType\":\"Sensor\"  ,\"VType\": \"String\",\"VMin\" : \"Empty\" ,\"VMax\" : \"Mail\" },";
const char gJSONthings8[] PROGMEM ="{\"Id\":\"0008\",\"Type\":\"Alarm\"      ,\"SType\":\"Actuator\",\"VType\": \"String\",\"VMin\" : \"Set\"   ,\"VMax\" : \"Unset\"}";
//JSON event
const char JSONstatus1[] PROGMEM ="{\"Id\":\"0003\",\"Type\":\"Presence\"   ,\"Value\":\"";
const char JSONstatus2[] PROGMEM ="{\"Id\":\"0004\",\"Type\":\"Temperature\",\"Value\":\"";
const char JSONstatus3[] PROGMEM ="{\"Id\":\"0005\",\"Type\":\"Humidity\"   ,\"Value\":\"";
const char JSONstatus4[] PROGMEM ="{\"Id\":\"0006\",\"Type\":\"DoorSensor\" ,\"Value\":\"";
const char JSONstatus5[] PROGMEM ="{\"Id\":\"0007\",\"Type\":\"MailBox\"    ,\"Value\":\"";

#define MAX_WIFI_STRING_LENGTH 70
String gsBufferWiFi;
void SendJSONobject(WiFiClient localclient, char *key, char *value, bool bEnd)
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

void SendJSONdiscoverRegister(WiFiClient localclient , bool bDiscoverRegister)
{
  gsBufferWiFi="";
  gsBufferWiFi+='{';
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
  gsBufferWiFi+="\"ThingList\":[";
  localclient.print(gsBufferWiFi.c_str());
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


void SendJSONstatusEvent(WiFiClient localclient)
{
  gsBufferWiFi="";
  gsBufferWiFi+='{';  
  SendJSONobject(localclient, "Job", "Event", false);
  SendJSONobject(localclient, "NodeID", (char *)MacAddressString.c_str(), false);  
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
    if(HomeNode.proximity <= 50)    // door open
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
    gsBufferWiFi+=HomeNode.temperature;
    gsBufferWiFi+="\"},";
    localclient.print(gsBufferWiFi.c_str());
    
//JSONstatus3 humidity
    gsBufferWiFi="";
    for(i = 0 ; i < strlen(JSONstatus3) ; i++) 
   {
      gsBufferWiFi+=(char)pgm_read_byte_near(JSONstatus3 + i);
    }
    gsBufferWiFi+=HomeNode.humidity;
    gsBufferWiFi+="\"},";
    localclient.print(gsBufferWiFi.c_str());
    
//JSONstatus4  DoorSensor
    gsBufferWiFi="";
    for(i = 0 ; i < strlen(JSONstatus4) ; i++) 
    {
      gsBufferWiFi+=(char)pgm_read_byte_near(JSONstatus4 + i);
    }
    if(HomeNode.doorstate == 0)   // door open
    { 
      gsBufferWiFi+="Open";
    }
    else
    { 
      gsBufferWiFi+="Close";
    }
    gsBufferWiFi+="\"}";
    localclient.print(gsBufferWiFi.c_str());
  }
  localclient.print("]}\n");
}

void SendJSONnotAuthorizedEvent(WiFiClient localclient)
{
  gsBufferWiFi="";
  //localclient.write('{');   
  gsBufferWiFi+='{';
  SendJSONobject(localclient, "Job", "Event", false);
  SendJSONobject(localclient, "NodeID", (char *)MacAddressString.c_str(), false); 
  SendJSONobject(localclient, "Result", "NotAuthorized", false);
  gsBufferWiFi+="}\n";
  localclient.print(gsBufferWiFi.c_str());
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
      {
        Serial.println(stringbuf);
        Serial.println();
        StaticJsonBuffer<200> jsonBuffer;  
        JsonObject& msg = jsonBuffer.parseObject(stringbuf);
        msg.prettyPrintTo(Serial);   
        Serial.println();
      }
*/
    Serial.println("start Message: "); 
     for(int i = 0 ; i <= 100 ; i++)
     {
       SendJSONdiscoverRegister(client, true);
       SendJSONdiscoverRegister(client,false);
       SendJSONstatusEvent(client);
       SendJSONnotAuthorizedEvent(client);
       Serial.print( "Send JSON message Done...");
       Serial.println(i);
      // delay(3000);      
     }
    Serial.println("end Message: "); 
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
