
/*******
*******************************************************************************
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
#include <HomeNodeDDI.h>           // Note that the DHT file must be in your Arduino installation folder, in the library foler.
#include <MyTimer.h>
#include <MyEeprom.h>

void setup() {
}

void loop() {
}

