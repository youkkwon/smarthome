
#ifndef MyTimer_h
#define MyTimer_h

#if ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif

#define MyTimer_LIB_VERSION "0.1.00"

class MyTimer
{
	
public :
	unsigned long BaseTime;
	
	void SetBaseTimer(void);
	int CheckPassTime(unsigned long passTime, char updateOption);
	
private :
//	unsigned long BaseTime;
	
};

#endif


