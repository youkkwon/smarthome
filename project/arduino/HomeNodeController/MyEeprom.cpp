
#include "MyEeprom.h"

// Get IoTMS registraton status from eeprom
int MyEeprom::CheckIoTMSRegistrationStatus(void)
{
	unsigned char ch;

	ch  = EEPROM[0];

	if(ch == '1')
	{
		return 1;
	}
	else
	{
		return 0;
	}
}


String MyEeprom::GetIoTMSIpAddressFromEeprom(void)
{
	String Ipaddress;
	char ch;
	int i;

	for(i = EEP_IOTMS_IP_START_ADDR ; i < (EEP_IOTMS_IP_START_ADDR + EEP_IPADDR_SIZE) ; i++)
	{
		ch  = EEPROM[i];
		if(ch == '\0')
		{
			break;
		}
		else
		{
			Ipaddress += ch;
		}
	}

	return Ipaddress;
}

int MyEeprom::GetIoTMSPortNumberFromEeprom(void)
{
	String portnumber;
	char ch;
	int i;

	for(i = EEP_IOTMS_PORT_START_ADDR ; i < (EEP_IOTMS_PORT_START_ADDR + EEP_PORTNUM_SIZE) ; i++)
	{
		ch = EEPROM[i];
		if(ch == '\0')
		{
			break;
		}
		else
		{
			portnumber += ch;
		}
	}
	return (int)(portnumber.toInt());
}

void MyEeprom::SaverIoTMSInformationToEeprom(String str, int startaddr, int endaddr)
{
	char *temp;
	int i, j;
	
	temp = (char *)str.c_str();

	for(i = 0, j = startaddr ; temp[i] != '\0' ; i++, j++)
	{
		EEPROM[j] = temp[i];
	}

	for(j++ ; j <= endaddr ; j++)
	{
		EEPROM[j] = '\0';
	}
}

void MyEeprom::SaveIoTMSRegistrationStatus(char ch)
{
	EEPROM[0] = ch;
}

void MyEeprom::ResetEeprom(int endaddr)
{
	int i;
	for(i = 0 ; i <= endaddr ; i++)
	{
		EEPROM[i] = '\0';
	}
}

void MyEeprom::InitilaizeEeprom(void)
{
	unsigned char ch1, ch2;

	ch1 = EEPROM[EEP_INIT_MARK_ADDR];
	ch2 = EEPROM[EEP_INIT_MARK_ADDR + 1];

	if(ch1 == 'A' & ch2 == 'A')
	{
		// Already Eeprom initilaizeed
	}
	else
	{
		ResetEeprom(EEP_INIT_MARK_ADDR - 1);
		EEPROM[EEP_INIT_MARK_ADDR] = 'A';
		EEPROM[EEP_INIT_MARK_ADDR + 1] = 'A';
	}
}

