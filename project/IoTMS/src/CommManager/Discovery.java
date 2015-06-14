package CommManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Discovery implements Runnable {
	
	private Thread t;
	private AdapterEventListener listener;
	
	int searchSize = 2048;							// The number of IP address to scan at the same time.
   	DiscoveryProbe tcList[] = new DiscoveryProbe[searchSize];	// The list of threads that actually does the scan
	
	public Discovery(String name)
	{
		System.out.println("WiFiDiscovery Start...");
		//listener = l;
		t = new Thread(this, name);
		t.start();
	}
	
	@Override
	public void run()
    {
		//int searchSize = 2048;							// The number of IP address to scan at the same time.
		int	portNum = 550;								// Port number for server socket
		int i = 0;										// Loop index
	  	int oct1start, oct1finish;						// The first IP octet range (starting/ending IP addresses)
	  	int oct2start, oct2finish;						// The second IP octet range (starting/ending IP addresses)
	  	int oct3start, oct3finish;						// The third IP octet range (starting/ending IP addresses)
	  	int oct4start, oct4finish;						// The forth IP octet range (starting/ending IP addresses)
	  	int one, two, three, four;						// The four octets. These are used as loop indexes
	  	int octet;										// A single octet
	   	String myIpAddr = null;							// This application's IP address
	   	String mySubnetMask = null;						// The subnet mask this application is running on
	   	String searchIpAddr = null;						// The IP address to scan
	   	//Thread tcList[] = new DiscoveryProbe[searchSize];	// The list of threads that actually does the scan
	   	boolean FirstAddr = true;						// Flag indicating the first IP address
	   	Scanner keyboard = new Scanner(System.in);		// Keyboard scanner for "hit any key"

		/***********************************************************************************************************
		* We get our IP address and our Subnet mask and print it to the terminal
		***********************************************************************************************************/

	   	myIpAddr = GetMyIp();
	   	System.out.println( "\n\nMy IP Address:: " + myIpAddr );

	   	mySubnetMask = GetSubnetMask();
	   	mySubnetMask = "255.255.255.255";
	   	System.out.println( "Subnet Mask:: " + mySubnetMask +"\n\n" );

		/***********************************************************************************************************
		* Next we get the integer equivalent of each octet. We do this to determine the scan ranges.
		* This is the first Octet...
		***********************************************************************************************************/

	   	octet = GetOctet( 1, mySubnetMask);

	   	switch( octet )
	   	{
		   	case 255:									// No Addresses
		   		oct1start = GetOctet( 1, myIpAddr );
	  			oct1finish = oct1start + 1;
	  			break;

	  		case 0:										// All 255 Addresses
	  			oct1start = 0;
	  			oct1finish = 255;
	  			break;

	  		default:									// Partial mask
				oct1start = 0;
				oct1finish = octet;
		}

		/***********************************************************************************************************
		* This is the second Octet...
		***********************************************************************************************************/

	   	octet = GetOctet( 2, mySubnetMask);

	   	switch( octet )
	   	{
		   	case 255:									// No Addresses
		   		oct2start = GetOctet( 2, myIpAddr );
	  			oct2finish = oct2start + 1;
	  			break;

	  		case 0:										// All 255 Addresses
	  			oct2start = 0;
	  			oct2finish = 255;
	  			break;

	  		default:									// Partial mask
				oct2start = 0;
				oct2finish = octet;
		}

		/***********************************************************************************************************
		* This is the third Octet...
		***********************************************************************************************************/

		octet = GetOctet( 3, mySubnetMask);

	   	switch( octet )
	   	{
		   	case 255:									// No Addresses
		   		oct3start = GetOctet( 3, myIpAddr );
	  			oct3finish = oct3start + 1;
	  			break;

	  		case 0:										// All 255 Addresses
	  			oct3start = 0;
	  			oct3finish = 255;
	  			break;

	  		default:									// Partial mask
				oct3start = 0;
				oct3finish = octet;
		}

		/***********************************************************************************************************
		* This is the fourth Octet...
		***********************************************************************************************************/

		octet = GetOctet( 4, mySubnetMask);

	   	switch( octet )
	   	{
		   	case 255:									// No Addresses
		   		oct4start = GetOctet( 4, myIpAddr );
	  			oct4finish = oct4start + 1;
	  			break;

	  		case 0:										// All 255 Addresses
	  			oct4start = 0;
	  			oct4finish = 255;
	  			break;

	  		default:									// Partial mask
				oct4start = 0;
				oct4finish = octet;
		}

		/***********************************************************************************************************
		* Now we start the scan. This algorithm will scan about 2K addresses per minute.
		***********************************************************************************************************/

		System.out.print( "Scanning from:" + oct1start + "." + oct2start + "." + oct3start + "." + oct4start );
		System.out.println( " to:" + (oct1finish-1) + "." + (oct2finish-1) + "." + (oct3finish-1) + "." + (oct4finish-1) );

		for ( one = oct1start; one < oct1finish; one++ )						// First octet
		{
			for ( two = oct2start; two < oct2finish; two++ )					// Second octet
			{
	  			for ( three=oct3start; three < oct3finish; three++ )			// Third octet
	   			{
		   			for ( four=oct4start; four<oct4finish; four++ )				// Fourth octet
		   			{
			   			searchIpAddr = new String( String.valueOf(one));		// Here we build the IP string
			   			searchIpAddr += ".";
			   			searchIpAddr += String.valueOf(two);
			   			searchIpAddr += ".";
			   			searchIpAddr += String.valueOf(three);
			   			searchIpAddr += ".";
			   			searchIpAddr += String.valueOf(four);

		/***********************************************************************************************************
		* Here we instantiate a TryConnect thread for each IP address. The scans happen simultaniously. We can do
		* upto searchSize at a time.
		***********************************************************************************************************/


			   			tcList[i] = new DiscoveryProbe(searchIpAddr, portNum, i, listener);
			   			tcList[i].start();
			   			
			   			if(i > 0) System.exit(0);

		/***********************************************************************************************************
		* What we are checking for here is to see if the tcList thread pool is full. If so, we wait about 3 seconds
		* for the threads to finish. Then we start again. We report the scan ranges to the users so they can see
		* the progress of the scan.
		***********************************************************************************************************/

			   			if ( i == searchSize-1 )
			   			{
			   				FirstAddr = true;
			   				i = 0;
			   				try
			   				{
			   					Thread.sleep(3000); //give time for the first batch of connects to finish

		   					} catch ( Exception e ) {

			   					System.out.println( "Error in sleep " + e );
		   					}

		   				} else {
			   				if ( FirstAddr )
			   				{
				   				System.out.println( "Starting Scan..." );
				   				FirstAddr = false;
			   				}
			   				i++;
		   				}

					} // for
				} // for
			} // for
		} // for

		//System.out.println( "\nSearch is complete.\n");

 	} // Main


	/***********************************************************************************************************
	* This method gets the subnet mask of the application. Note this will only work on a Windows machine.
	* To find the subnet mask, this process will run an "ipconfig" command then redirect the output of the
	* ipconfig command to this application. The output is parsed for the subnet mask string.
	***********************************************************************************************************/

    static String GetSubnetMask()
    {
         try
         {
			// The following sets up the runtime to execute the ipconfig command
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("ipconfig");

            // The following redirects the output of the above process (pr) to input
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            // The following strings hold the line of input from the process and the subnet mask string
            String line = null;
            String Mask = null;

            // This is a loop flag
            boolean found = false;

			// We parse the input until we find the local ethernet adaptor information

            while( !found )
            {
	            line=input.readLine();

	            if (line != null)
	            {
					// This strings should work for both IPv4 and IPv6, but might vary. You should check
					// the output of your ipconfig command to check

		            if ( ( line.indexOf( "Ethernet adapter Local Area Connection:" ) >= 0 ) ||
		               ( line.indexOf( "Wireless LAN adapter Wireless Network Connection:" ) >= 0 )||
		               ( line.indexOf( "이더넷 어댑터 로컬 영역 연결:" ) >= 0 )||
		               ( line.indexOf( "무선 LAN 어댑터 무선 네트워크 연결:" ) >= 0 ) )
		            {
		              found = true;
					}

	            } else {

		            System.out.println( "Local adaptor information not found.");
	            }
            }

			// Now we parse the input until we find the subnet mask

            found = false;
            while( !found )
            {
	            line=input.readLine();

	            if (line != null)
	            {
		            if ( line.indexOf( "Subnet Mask" ) >= 0 || line.indexOf( "서브넷 마스크" ) >= 0)
		            {
		            	Mask = (line.substring(line.indexOf( ":" )+1 )).trim();
						found = true;
	            	}
	            } else {

		            System.out.println( "Subnet Mask not found.");
	            }
            }

            return ( Mask );

         } catch(Exception e) {

	        System.out.println(e.toString());
            e.printStackTrace();
            return( null );
         }

  	} // GetSubnetMask

	/***********************************************************************************************************
	* This method gets the IP address of the application. Note this will only work on a Windows machine.
	* To find the IP address, this process will run an "ipconfig" command then redirect the output of the
	* ipconfig command to this application. The output is parsed for the IP string.
	***********************************************************************************************************/

  	static String GetMyIp()
    {
         try
         {
			// The following sets up the runtime to execute the ipconfig command
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("ipconfig");

            // The following redirects the output of the above process (pr) to input
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            // The following strings hold the line of input from the process and the IP string
            String line = null;
            String ipAddr = null;

            // This is a loop flag
            boolean found = false;


			// We parse the input until we find the local ethernet adaptor information

            while( !found )
            {
	            line=input.readLine();

	            if (line != null)
	            {
		            if ( ( line.indexOf( "Ethernet adapter Local Area Connection:" ) >= 0 ) ||
		               ( line.indexOf( "Wireless LAN adapter Wireless Network Connection:" ) >= 0 )||
		               ( line.indexOf( "이더넷 어댑터 로컬 영역 연결:" ) >= 0 )||
		               ( line.indexOf( "무선 LAN 어댑터 무선 네트워크 연결:" ) >= 0 ) )
		            {
		              found = true;
					}
	            } else {

		            System.out.println( "Local adaptor information not found.");

	            }
            }

 			// Now we parse the input until we find the IP address

            found = false;
            while( !found )
            {
	            line=input.readLine();

	            if (line != null)
	            {
		            if (( line.indexOf( "IP Address" ) >= 0 ) || ( line.indexOf( "IPv4 Address" ) >= 0 ) ||
		            		( line.indexOf( "IP 주소" ) >= 0 ) || ( line.indexOf( "IPv4 주소" ) >= 0 ))
		            {
		            	ipAddr = (line.substring(line.indexOf( ":" )+1 )).trim();
						found = true;
	            	}

	            } else {

		            System.out.println( "IP Address not found.");

	            }
            }

            return ( ipAddr );

         } catch(Exception e) {

	        System.out.println(e.toString());
            e.printStackTrace();
            return( null );
         }

  	} // GetMyIP

	/***********************************************************************************************************
	* This method gets the specified Octet from an IP string. The octet is returned as an integer value.
	***********************************************************************************************************/

  	static int GetOctet( int OctetNum, String ipString )
 	{
	 	int Octet;				// The Octet
	 	String tempstring;		// Temporary string

		// Here we figure out which octet the caller wants, and then parse it from the ipString passed
		// into this method.

	 	switch( OctetNum )
	 	{
		 	case 1:
		 		Octet = Integer.valueOf(ipString.substring(0, ipString.indexOf(".")));
		 		break;

		 	case 2:
		 		tempstring = ipString.substring(ipString.indexOf(".")+1);
		 		Octet = Integer.valueOf(tempstring.substring(0, tempstring.indexOf(".")));
		 		break;

		 	case 3:
		 		tempstring = ipString.substring(ipString.indexOf(".")+1);
		 		tempstring = tempstring.substring(tempstring.indexOf(".")+1);
		 		Octet = Integer.valueOf(tempstring.substring(0, tempstring.indexOf(".")));
		 		break;

		 	case 4:
		 		tempstring = ipString.substring(ipString.indexOf(".")+1);
		 		tempstring = tempstring.substring(tempstring.indexOf(".")+1);
		 		tempstring = tempstring.substring(tempstring.indexOf(".")+1);
		 		Octet = Integer.valueOf(tempstring);
		 		break;

			default:
				Octet = -1;

		} // switch

		return( Octet );

	}// GetOctet


	public void addListener(AdapterEventListener l)
	{
		listener = l;
	}
	
	public void registerNode(String msg)
	{
		for(int i = 0; i < searchSize; i++)
		{
			if(tcList[i] != null)
				if(tcList[i].getMACAddress().equals(CommUtil.parseMACAddress(msg)))
				{
					tcList[i].registerNode(msg);
					return;
				}
		}
	}
}
