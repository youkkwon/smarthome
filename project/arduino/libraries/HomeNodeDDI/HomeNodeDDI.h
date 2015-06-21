/******************************************************************
* FILE: HomeNodeDDI.cpp
* VERSION: 0.1.00
* PURPOSE: Node Sensor and Actuator control
*
* DATASHEET: 
*
* HISTORY:
* 0.1.0 by Jang Young Keun (06/13/2015)
* inspired by DHT library
*******************************************************************/

#ifndef HomeNodeDDI_h
#define HomeNodeDDI_h

#if ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif

//#include "../Servo/src/Servo.h"
#include <Servo.h>

#define HomeNodeDDI_LIB_VERSION "0.1.00"

#define DOOR_OPEN	1
#define DOOR_CLOSE	0
#define ON		1
#define OFF		0

// Pin Assignment
#define DOOR_SERVO_PIN		9	// Door open/close control
#define DOOR_SWITCH_PIN	2	// Door open/close sesne
#define INDOOR_LIGHT_PIN	5	// In door light On/Off control
#define ALARM_LIGHT_PIN	3	// Secure alarm light On/Off control
#define PROXIMITY_PIN		6	// Proximity sensor 
#define TEMP_HUMIDITY_PIN	8	// temperature & Humidity sensor

class HomeNodeDDI
{
public:
	int humidity;
	int temperature;
	long proximity;
	int BeIndoors;
	int doorstate;
	Servo doorservo;
	
	void DoorServoAttach(void);
	void DoorControl(int order);
	void SecureAlarmLightControl(int order);
	void IndoorLightControl(int order);
	
	void readDoorState();
	void readProximityVal(void);
	int read1byte_tempNhumidity(void);
	//int read2byte_tempNhumidity(void);
	
private:
	uint8_t bits[5];  // buffer to receive data
	int read_tempNhumidity(void);
};
#endif
//
// END OF FILE
//
