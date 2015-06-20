

#ifndef MyEeprom_h
#define MyEeprom_h

#if ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif

#include <EEPROM.h>

#define MyEeprom_LIB_VERSION "0.1.00"

#define EEP_IOTMS_IP_START_ADDR			1
#define EEP_IPADDR_SIZE						15
#define EEP_IP_END_ADDR					(EEP_IOTMS_IP_START_ADDR + EEP_IPADDR_SIZE - 1)
#define EEP_IOTMS_PORT_START_ADDR		16
#define EEP_PORTNUM_SIZE					5
#define EEP_PORTNUM_END_ADDR			(EEP_IOTMS_PORT_START_ADDR + EEP_PORTNUM_SIZE - 1)

#define EEP_INIT_MARK_ADDR				1022

class MyEeprom
{
	
public :
	int CheckIoTMSRegistrationStatus(void);
	String GetIoTMSIpAddressFromEeprom(void);
	int GetIoTMSPortNumberFromEeprom(void);
	void SaverIoTMSInformationToEeprom(String, int, int);
	void ResetEeprom(int);
	void InitilaizeEeprom(void);
	void SaveIoTMSRegistrationStatus(char);
	
private :
//	unsigned long BaseTime;
	
};

#endif

