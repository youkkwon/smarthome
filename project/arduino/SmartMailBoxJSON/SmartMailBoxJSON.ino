#include <ArduinoJson.h>

/**************************************************************************************
* File: SmartMailBox
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2015 Anthony J. Lattanze
* Versions:
* 1.0 June 2015 - Initial version
*
* Description:
*
* This program demonstrates how to use the smart mail box device. The mailbox has a 
* proximity inside that can be used to alert users of the presence of email.
*
* Compilation and Execution Instructions: Compiled using Arduino IDE VERSION 1.6.4
*
* Parameters: None
*
* Internal Methods: 
* long ProximityVal(int sensorIn) - Determines if the proximity sensor is covered 
* or not. If the sensor is covered, this method will return a value less than when 
* the sensor is not covered. Typical values will be 0 or 1 when covered, and 7 to 10
* when uncovered, buy the actual values you get will depend upon the ambient light.
*
* Each of these methods are explained in detail where they appear below.
**************************************************************************************/

int MailboxPin = 6;            // The pin with QTI/proximity sensor
#define MAX_TARGET_NUM 5

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


void setup() 
{ 
  Serial.begin(9600);                           // Set up a debug window


} 

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

void PrintStructMsg(struct sMessage msg)
{
  Serial.println("PrintStructMessage {");
  Serial.print("  msg.Sendor = ");
  Serial.println(msg.Sendor);
   
  Serial.print("  msg.Job = ");
  Serial.println(msg.Job);
   
  Serial.print("  msg.NodeID = ");
  Serial.println(msg.NodeID);
   
  Serial.print("  msg.ThingID = ") ;
  Serial.println(msg.ThingID);

  Serial.print("  msg.Value = ");
  Serial.println(msg.Value);
  
  Serial.print("  msg.Type = ");
  Serial.println(msg.Type);  
   
  Serial.print("  msg.Targets[");
  Serial.print(msg.iTargetNum);
  Serial.print("] = ");
  for(int i = 0 ; i < msg.iTargetNum ; i++)
    Serial.print(msg.Targets[i]+",");

  Serial.println("");
  Serial.println("}");
  Serial.println("");
}


void loop() 
{
  StaticJsonBuffer<300> jsonBuffer;  
  char stringbuf[256];
  
  gSendMsg.Sendor = "NodeManager";
  gSendMsg.Job = "monitor";
  gSendMsg.NodeID = "first";
  gSendMsg.ThingID = "temperature";
  gSendMsg.Value = "100degree";
  gSendMsg.Type = "pretty";
  gSendMsg.Targets[0] = "rule";    
  gSendMsg.Targets[1] = "UI";    
  gSendMsg.Targets[2] = "node";    
  gSendMsg.Targets[3] = "";    

  JsonObject& messageEncoding = EncodingStrucMsgToJSONmsg(jsonBuffer);     
  messageEncoding.prettyPrintTo(Serial);
  Serial.println();  
  messageEncoding.printTo(stringbuf,sizeof(stringbuf));
  Serial.print("string msg:");
  Serial.println(stringbuf);  
  Serial.println();  
  
  if( DecodingJSONmsgToStructMsg(jsonBuffer, stringbuf) )
    PrintStructMsg(gReceiveMsg);


 
  
  Serial.print("Mailbox Sensor Value = ");           // Determine the value of the QTI
  Serial.println( ProximityVal(MailboxPin) );        // proximity sensor and wait for 
  delay( 2500 );                                     // 2.5 seconds.
  
} 




/*********************************************************************
* long ProximityVal(int Pin)
* Parameters:            
* int pin - the pin on the Arduino where the QTI sensor is connected.
*
* Description:
* QTI schematics and specs: http://www.parallax.com/product/555-27401
* This method initalizes the QTI sensor pin as output and charges the
* capacitor on the QTI. The QTI emits IR light which is reflected off 
* of any surface in front of the sensor. The amount of IR light 
* reflected back is detected by the IR resistor on the QTI. This is 
* the resistor that the capacitor discharges through. The amount of 
* time it takes to discharge determines how much light, and therefore 
* the lightness or darkness of the material in front of the QTI sensor.
* Given the closeness of the object in this application you will get
* 0 if the sensor is covered. Any non-zero number means the sensor
* is not covered. Actual values vary and depend upon th ambient light.
***********************************************************************/
long ProximityVal(int Pin)
{
    long duration = 0;
    pinMode(Pin, OUTPUT);         // Sets pin as OUTPUT
    digitalWrite(Pin, HIGH);      // Pin HIGH
    pinMode(Pin, INPUT);          // Sets pin as INPUT
    digitalWrite(Pin, LOW);       // Pin LOW
    while(digitalRead(Pin) != 0)  // Count until the pin goes
       duration++;                // LOW (cap discharges)
       
    return duration;              // Returns the duration of the pulse
}
