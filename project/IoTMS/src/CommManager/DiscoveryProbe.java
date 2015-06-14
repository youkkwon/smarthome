package CommManager;

import java.net.*;
import java.io.*;

public class DiscoveryProbe extends Thread
{
	String ip;	// IP address and port to try to connect to.
	int port;
	int index;
	Socket sock;
	String mac;
	//BufferedWriter out;
	
	private AdapterEventListener listener;

	// Here we set up the thread and local parameters

	public DiscoveryProbe ( String ip, int port, int index, AdapterEventListener l )
	{
		this.ip = ip;
		this.port = port;
		this.index = index;
		listener = l;
		setDaemon(false);

	} // constructor

	// Here is the main executable of the thread

	public void registerNode(String msg)
	{
		try
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			System.out.println(msg);
			out.write( msg, 0, msg.length() );
			out.newLine();
			out.flush();
		}
		catch(Exception e)
		{
     		e.printStackTrace();
		}
	}

	public void disconnectNode()
	{
		try
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			String msg = "Disconnect";
			System.out.println(msg);
			out.write( msg, 0, msg.length() );
			out.newLine();
			out.flush();
		}
		catch(Exception e)
		{
     		e.printStackTrace();
		}
	}
	
	public String getMACAddress()
	{
		return mac;
	}
	
	public void run()
	{
		System.out.println("Start to probe...");
		try
   		{
			/*****************************************************************************
			* First we try to connect. We set the timeout to 3 1/4 seconds. This value is
			* critcal. If its too short, its not enough time to connect, but the longer it
			* is, the longer it take to scan the addresses.
			*****************************************************************************/

			// original
			/*
			SocketAddress sockaddr = new InetSocketAddress(ip, port);
			Socket sock = new Socket();
			sock.connect(sockaddr, CommUtil.getDiscoveryPort());
			*/
			// new
			sock = new Socket(ip, CommUtil.getDiscoveryPort());
			System.out.println( "SERVER FOUND AT:: " + ip + "!!!" );
			
			/*****************************************************************************
			* If we get here, we are connected. Now we determine if is an Arduino server
			* running the CommandServer application. So wecreate the input and output
			* streams. The "out" variable is for writing to the server, and "in" is for
			* reading from the server.
			*****************************************************************************/

			BufferedReader in = new BufferedReader( new InputStreamReader(sock.getInputStream()));
			//out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			
			/*****************************************************************************
			* The protocol is simple: The client sends a message, then waits for two
			* messages from the server and thats it. We send a query command the client
			* which is a "Q\n" string. If the write timesout, we will drop down to the
			* exception which means that while we have found a server, its not an Arduino
			* running the CommandServer application.
			*****************************************************************************/

			String inputLine = null;
		   	boolean done = false;

			/*****************************************************************************
			* If we get to here, then we found a CommandServer, so we read the MAC address
			* of the Wifi Shield on the Arduino.
			*****************************************************************************/

		   	while (!done)
			{
				if ((inputLine = in.readLine()) != null)
				{
					if(inputLine.length() > 1)
					{
						System.out.println("discoveryProb: " + inputLine);
						
						mac = CommUtil.parseMACAddress(inputLine);
						String job = CommUtil.parseJob(inputLine);
						
						if(job.equals("Discovered"))
						{
							listener.onDiscovered(new AdapterEvent("Discovered","found",inputLine));
						}
						else if(job.equals("Registered"))
						{
							listener.onRegistered(new AdapterEvent("Registered","found",inputLine));
							//System.out.println("@@ Request to disconnect...");
							//disconnectNode();
							break;
						}
					}

				} else {

					done = true;
				}
			} // while

			/*****************************************************************************
			* That's it! Close the streams and close the socket.
			*****************************************************************************/

			in.close();
			//out.close();
	  		sock.close();
	  		
			System.out.println("discoveryProb: Closed");

     	} catch (IOException e) {
     		e.printStackTrace();
    	}

	} // main
} // class