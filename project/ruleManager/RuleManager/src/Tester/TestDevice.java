package Tester;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import CommManager.CommUtil;

public class TestDevice {

	public void test(int mode)
 	{		
  		String inputLine;															// String from the server
		Socket clientSocket = null;													// The socket.
    	boolean done;																// Loop flag.
    	int msgCnt;																	// Number for message displayed
    	int	portNum = CommUtil.getServerPort();															// Port number for server socket

    	// args[0] == 0 // factory reset mode
    	// args[0] == 1 // registered mode
    	    	
    	if(mode == 0)
    	{
    	
		System.out.println("Server Socket Thread Start... (001)");
		
    	ServerSocket serverSocket = null;							// Client socket object
    	int	discPortNum = CommUtil.getDiscoveryPort();											// Port number for server socket
    	
    	/*****************************************************************************
	 	* First we instantiate the server socket. The ServerSocket class also does
	 	* the listen()on the specified port.
	 	*****************************************************************************/
		try
		{
    		serverSocket = new ServerSocket(discPortNum);
    		System.out.println ( "\n\nWaiting for connection on port " + discPortNum + "." );
    	}
		catch (IOException e)
    	{
    		System.err.println( "\n\nCould not instantiate socket on port: " + discPortNum + " " + e);
    		System.exit(1);
    	}
		
		//while(true)
		{
			/*****************************************************************************
    	 	* If we get to this point, a client has connected. Now we need to
    	 	* instantiate a client socket. Once its instantiated, then we accept the
    	 	* connection.
		 	*****************************************************************************/

	    	try
    		{
        		clientSocket = serverSocket.accept();
        	}
    		catch (Exception e)
        	{
        		System.err.println("Accept failed.");
        		System.exit(1);
        	}

			/*****************************************************************************
    	 	* At this point we are all connected and we need to create the streams so
    	 	* we can read and write.
		 	*****************************************************************************/

    		System.out.println ("Connection successful");
    		System.out.println ("Waiting for input.....");

 	    	try
 	    	{ 	   	
	    		BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
	    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

				/*****************************************************************************
	    		 * Now we can read and write to and from the client. Our protocol is to wait
	    		 * for the client to send us strings which we read until we get a "Bye." string
	    	 	 * from the client.
			 	 *****************************************************************************/
		    	{
	 	    		//String disc_resp_msg = "{\"Job\":\"Discovered\",\"NodeID\":\"12:23:34:45:56:67\",\"Sensor\":[\"DoorSensor\":[\"Open\",\"Close\"],\"Temperater\":\"Numeric\"]}\n";
	 	    		String disc_resp_msg = "{\"Job\":\"Discovered\",\"NodeID\":\"12:23:34:45:56:67\"}\n";
	 				System.out.println( "Sending message to client...." );
	   				out.write( disc_resp_msg, 0, disc_resp_msg.length() );
	   				out.newLine();
					out.flush();
		    	}	

				/*****************************************************************************
	    		 * Wait for register
			 	 *****************************************************************************/
	
		    	while (true)
		    	{
		    		if ((inputLine = in.readLine()) != null)
		    		{
	  	  				System.out.println("FROM SERVER: " + inputLine);
		   				
	  	  				/*if(inputLine.equals("Disconnect") == true)
		   				{
		   	 				System.out.println( "Disconnect request from server...." );
		   					break;
		   				}
		   				else*/
		   				{
		   	 	    		String reg_resp_msg = "{\"Job\":\"Registered\",\"NodeID\":\"12:23:34:45:56:67\"}\n";
		   	 				System.out.println( "Sending message to client.... " + reg_resp_msg );
		   	   				out.write( reg_resp_msg, 0, reg_resp_msg.length() );
		   	   				out.newLine();
		   					out.flush();
		   					break;
		   				}
		  			}
		  		}
 	    		
 	    	
				/*****************************************************************************
	    		 * Close up the I/O ports then close up the sockets
			 	 *****************************************************************************/

		    	in.close();
				out.close();

	   		 	clientSocket.close();
		    	serverSocket.close();
	
				System.out.println ( "\ndiscovery done...............\n" );
			

			} catch (Exception e) {
	     		e.printStackTrace();
			} 	    
			
    	} // while loop
    	}
		
   		while(true)
		{
  			/*****************************************************************************
    		* First we instantiate the clent socket. The user should enter the IP address
    		* of the server on the command line
			*****************************************************************************/

			done = false;
			while (!done)
			{
				try
    			{
      		  		/*if (argv.length == 0)
      		  		{
      		  			System.out.println ( "\nPlease specify an IP address on the command line.\n" );
       		 			System.exit(1);
   		 			} else*/ {
      		  			System.out.println ( "\n\nTrying to connect to " + "127.0.0.1" + " on port " + portNum + ".\n" );
      		  			//clientSocket = new Socket(argv[0], portNum);
      		  			clientSocket = new Socket("127.0.0.1", portNum);
       		 			done = true;
					}

   	        		Thread.sleep(1000);
   	        		
		  			/*****************************************************************************
		    		* If we get here, we are connected. Now we create the input and output streams.
		    		* The "out" variable is for writing to the server, and "in" is for reading
		    		* from the server.
					*****************************************************************************/
		
		   			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		    		BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
		
		  			/*****************************************************************************
		    		* The protocol is simple: The client sends a message, then waits for two
		    		* messages from the server and thats it.
					*****************************************************************************/
					/* Here we write a message...												*/
		    		
		    		for(int i = 0; i < 3; i++)
		    		{
		    			String json_string = "{\"Job\":\"event\",\"NodeID\":\"12:23:34:45:56:67\",\"type\":\"event\",\"Temperature\":\"28\",\"DoorSensor\":\"Open\"}\n";
			    		System.out.println(json_string);
						out.write( json_string, 0, json_string.length() );
						out.flush();
						Thread.sleep(3000);
		    		}
		
		    		/*****************************************************************************
		    		* Now we read two messages from the server...
					*****************************************************************************/
		
					msgCnt = 0;
			    	done = false;
		
			    	while (!done)
			    	{
			    		if ((inputLine = in.readLine()) != null)
			    		{
		  	  				System.out.println("FROM SERVER: " + inputLine);
		  	  				msgCnt++;
			  			}
						if (msgCnt > 1) done = true;
			  		}
		
			  		System.out.println("-------------------------------------------------------");
		
					/*****************************************************************************
		    		* That's it! Close the streams and close the socket.
					*****************************************************************************/
			  		in.close();
			  		out.close();
			  		clientSocket.close();
		
			  		Thread.sleep(3000);
    			}

    			catch (Exception e)
        		{
             		e.printStackTrace();
        			System.err.println( "Could not connect to " + mode + " on port: " + portNum + "\n");
        		}


    		}			  		

  		} // while
 	} // main

}
