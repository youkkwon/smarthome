
#include "MyTimer.h"

void MyTimer::SetBaseTimer(void)
{
	BaseTime = millis();
}

/*
* Confirms that the elapsed time from the reference time by a specific time.
* Argument : 
*  - passTime : pass time from base time
*  - updateOption : 0 - Update basetime by currenttime
*  -                          1 - Update basetime by basetim+passTime
*/
int MyTimer::CheckPassTime(unsigned long passTime, char updateOption)
{
	unsigned long currenttime = millis();
	int result = 0;
		
	if(BaseTime + passTime <= currenttime)
	{
		result = 1;

		if(updateOption == 0)
		{	BaseTime = currenttime;
		}
		else
		{	BaseTime += passTime;
		}
	}

	return result;
}

