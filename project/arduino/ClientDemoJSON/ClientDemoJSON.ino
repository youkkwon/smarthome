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
char c;                           // Character read from server
int status = WL_IDLE_STATUS;      // Network connection status
// IPAddress server(192,168,1,124);  // The server's IP address
//IPAddress server(192,168,1,139);  // The server's IP address
IPAddress server(192,168,10,102);  // The server's IP address
WiFiClient client;                // The client (our) socket
IPAddress ip;                     // The IP address of the shield
IPAddress subnet;                 // The IP address of the shield
long rssi;                        // Wifi shield signal strength
byte mac[6];                      // Wifi shield MAC address
char stringbuf[256]={0};
//String sendStringMsg; 


struct sMessage
{
  String Sendor;
  String Job;
  String NodeID;
  String ThingID;
  String Value;
  String Type;
  String Targets[MAX_TARGET_NUM];  
  int iTargetNum;
} gSendMsg,gReceiveMsg;
/*
void SendStringWiFiClient(void)
{
  sendStringMsg = String(stringbuf);
  client.println(sendStringMsg.substring(0,20));
  for(int i = 0 ; i < sendStringMsg.length() ; i+=MAX_WIFI_STRING_LENGTH)
  {
    client.println(sendStringMsg.substring(i,i+MAX_WIFI_STRING_LENGTH-1));      
  }
} */    

JsonObject& EncodingStrucMsgToJSONmsg(JsonBuffer& buf)
{
  JsonObject& msg = buf.createObject();
  msg["Sender"] = gSendMsg.Sendor.c_str();;
  msg["Job"] = gSendMsg.Job.c_str();
  msg["NodeID"] = gSendMsg.NodeID.c_str();
  msg["ThingID"] = gSendMsg.ThingID.c_str();
  msg["Value"] = gSendMsg.Value.c_str();
  msg["Type"] = gSendMsg.Type.c_str();
  
  JsonArray& targets = msg.createNestedArray("Targets");
  for(int i = 0 ; i < MAX_TARGET_NUM; i++) {
    if(gSendMsg.Targets[i].equals("")) {
      gSendMsg.iTargetNum = i;
      break;
    }
    targets.add(gSendMsg.Targets[i].c_str());
  }
  
  return msg;
}


bool DecodingJSONmsgToStructMsg(JsonBuffer& buf, char *stringMsgJSONformat)
{

  JsonObject& msg = buf.parseObject(stringMsgJSONformat);

  if (!msg.success()) {
    Serial.println("parseObject() failed");
    return false;
  }
 
  gReceiveMsg.Sendor = msg["Sender"];
  gReceiveMsg.Job = msg["Job"];
  gReceiveMsg.NodeID = msg["NodeID"];
  gReceiveMsg.ThingID = msg["ThingID"];
  gReceiveMsg.Value = msg["Value"];
  gReceiveMsg.Type = msg["Type"];
  
  for(int i = 0 ; i < msg["Targets"].size(); i++) {
    gReceiveMsg.Targets[i] = msg["Targets"][i];
  }
  gReceiveMsg.iTargetNum = msg["Targets"].size();
  return true;
}

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

void loop() 
{
  // Here we attempt connect to the server on the port specified above
  StaticJsonBuffer<200> jsonBuffer;  
  int stringbufIndex = 0;
  Serial.print("\nAttempting to connect to server...");
  
  if (client.connect(server, PORTID)) 
  {
      Serial.println("connected");

      // We write a couple of messages to the server
      Serial.print("Server Message: ");
      
      char c = ' ';      
      stringbufIndex = 0;
      while (1)
      {
        if (client.available()) 
        {
          c = client.read();
          if( c == ';' )
          {
            stringbuf[stringbufIndex] = 0;
            break;
          }
          stringbuf[stringbufIndex++] = c;
        }
      }       
      DecodingJSONmsgToStructMsg(jsonBuffer, stringbuf);
      gSendMsg = gReceiveMsg;
      JsonObject& messageEncoding = EncodingStrucMsgToJSONmsg(jsonBuffer);     
      messageEncoding.prettyPrintTo(Serial);   
      messageEncoding.printTo(stringbuf,sizeof(stringbuf)); 
      delay(1000);
      String sendStringMsg = stringbuf;


      for(int i = 0 ; i < sendStringMsg.length() ; i+=MAX_WIFI_STRING_LENGTH)
      {
        client.print(sendStringMsg.substring(i,i+MAX_WIFI_STRING_LENGTH));      
      } 
      client.println(); 
      client.println("Bye.");     
      client.println(); 
      delay(2000);
 
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
     
     /*
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
  */
 } // printConnectionStatus
