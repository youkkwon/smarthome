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

#include "HomeNodeDDI.h"

#define TIMEOUT 10000

/////////////////////////////////////////////////////
//
// PUBLIC
//
// Pin Assignment
#define DOOR_SERVO_PIN		9	// Door open/close control
#define DOOR_SWITCH_PIN	2	// Door open/close sesne
#define INDOOR_LIGHT_PIN	5	// In door light On/Off control
#define ALARM_LIGHT_PIN	3	// Secure alarm light On/Off control
#define PROXIMITY_PIN		6	// Proximity sensor 
#define TEMP_HUMIDITY_PIN	8	// temperature & Humidity sensor


void HomeNodeDDI::DoorServoAttach(void)
{
	doorservo.attach(DOOR_SERVO_PIN);
}

/*
* Door control function
* Arguemnt : Order
*   - DOOR_OPEN(1) : Door open control
*   - DOOR_CLOSE(0) : Door close control
* Return : void
*/
void HomeNodeDDI::DoorControl(int order)
{
	if(order == DOOR_CLOSE)
	{
		doorservo.write(90);		// door close
	}
	else
	{
		doorservo.write(0);		// door open
	}
}

/*
* Secure alarm light control
* argument : order
*   - On : Light on
*   - Off : Light off
* return : void
*/
void HomeNodeDDI::SecureAlarmLightControl(int order)
{
	pinMode(ALARM_LIGHT_PIN, OUTPUT);	   // Set the specified pin to output mode.
	if(order == OFF)
	{
 		digitalWrite(ALARM_LIGHT_PIN, LOW);   // Set the pin to 5v.
	}
	else
	{
 		digitalWrite(ALARM_LIGHT_PIN, HIGH);   // Set the pin to 5v.
	}
}

/*
* In door light control
* argument : order
*   - On : Light on
*   - Off : Light off
* return : void
*/
void HomeNodeDDI::IndoorLightControl(int order)
{
	pinMode(INDOOR_LIGHT_PIN, OUTPUT);	   // Set the specified pin to output mode.
	if(order == OFF)
	{
 		digitalWrite(INDOOR_LIGHT_PIN, LOW);   // Set the pin to 5v.
	}
	else
	{
 		digitalWrite(INDOOR_LIGHT_PIN, HIGH);   // Set the pin to 5v.
	}
}

/*
* Sense door state
* return  : result of Sense door switch 
*/
void HomeNodeDDI::readDoorState(void)
{
	doorstate = digitalRead(DOOR_SWITCH_PIN);
}

void HomeNodeDDI::readProximityVal(void)
{
	long duration = 0;

	pinMode(PROXIMITY_PIN, OUTPUT);		// Sets pin as OUTPUT
	digitalWrite(PROXIMITY_PIN, HIGH);		// Pin HIGH
	pinMode(PROXIMITY_PIN, INPUT);		// Sets pin as INPUT
	digitalWrite(PROXIMITY_PIN, LOW);		// Pin LOW

	while(digitalRead(PROXIMITY_PIN) != 0)  // Count until the pin goes
	{
		duration++;                // LOW (cap discharges)
	}

	proximity = duration;

	// If a person is assumed that this proximity value is equal to or less than 50.
	if(proximity <= 50)
	{	BeIndoors = 1;
	}
	else
	{	BeIndoors = 0;
	}
}

// return values:
//  0 : OK
// -1 : checksum error
// -2 : timeout
int HomeNodeDDI::read1byte_tempNhumidity(void)
{
	// READ VALUES
	int rv = read_tempNhumidity();
	if (rv != 0) return rv;

	// CONVERT AND STORE
	humidity = bits[0];  // bit[1] == 0;
	temperature = bits[2];  // bits[3] == 0;

	// TEST CHECKSUM
	uint8_t sum = bits[0] + bits[2]; // bits[1] && bits[3] both 0
	if (bits[4] != sum) return -1;

	return 0;
}

/*
// return values:
//  0 : OK
// -1 : checksum error
// -2 : timeout
int HomeNodeDDI::read2byte_tempNhumidity(void)
{
	// READ VALUES
	int rv = read_tempNhumidity();
	if (rv != 0) return rv;

	// CONVERT AND STORE
	humidity = word(bits[0], bits[1]) * 0.1;

	int sign = 1;
	if (bits[2] & 0x80) // negative temperature
	{
		bits[2] = bits[2] & 0x7F;
		sign = -1;
	}
	temperature = sign * word(bits[2], bits[3]) * 0.1;


	// TEST CHECKSUM
	uint8_t sum = bits[0] + bits[1] + bits[2] + bits[3];
	if (bits[4] != sum) return -1;

	return 0;
}
*/

/////////////////////////////////////////////////////
//
// PRIVATE
//

// return values:
//  0 : OK
// -2 : timeout
int HomeNodeDDI::read_tempNhumidity(void)
{
	// INIT BUFFERVAR TO RECEIVE DATA
	uint8_t cnt = 7;
	uint8_t idx = 0;

	// EMPTY BUFFER
	for (int i=0; i< 5; i++) bits[i] = 0;

	// REQUEST SAMPLE
	pinMode(TEMP_HUMIDITY_PIN, OUTPUT);
	digitalWrite(TEMP_HUMIDITY_PIN, LOW);
	delay(20);
	digitalWrite(TEMP_HUMIDITY_PIN, HIGH);
	delayMicroseconds(40);
	pinMode(TEMP_HUMIDITY_PIN, INPUT);

	// GET ACKNOWLEDGE or TIMEOUT
	unsigned int loopCnt = TIMEOUT;
	while(digitalRead(TEMP_HUMIDITY_PIN) == LOW)
		if (loopCnt-- == 0) return -2;

	loopCnt = TIMEOUT;
	while(digitalRead(TEMP_HUMIDITY_PIN) == HIGH)
		if (loopCnt-- == 0) return -2;

	// READ THE OUTPUT - 40 BITS => 5 BYTES
	for (int i=0; i<40; i++)
	{
		loopCnt = TIMEOUT;
		while(digitalRead(TEMP_HUMIDITY_PIN) == LOW)
			if (loopCnt-- == 0) return -2;

		unsigned long t = micros();

		loopCnt = TIMEOUT;
		while(digitalRead(TEMP_HUMIDITY_PIN) == HIGH)
			if (loopCnt-- == 0) return -2;

		if ((micros() - t) > 40) bits[idx] |= (1 << cnt);
		if (cnt == 0)   // next byte?
		{
			cnt = 7;   
			idx++;      
		}
		else cnt--;
	}

	return 0;
}
//
// END OF FILE
//
