package comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

public class Link implements Runnable {

	private Thread t;
	private Socket clientSocket = null;	
	private LinkEventListener listener;
	private String mac = "00:00:00:00:00:00";
	
	public Link(String name, Socket socket, LinkEventListener l)
	{
		System.out.println("Link Start...");
		clientSocket = socket;
		listener = l;
		t = new Thread(this, name);
		t.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Message to display from serverMsg[]
		String inputLine;
		
		/*****************************************************************************
	 	* At this point we are all connected and we need to create the streams so
	 	* we can read and write.
	 	*****************************************************************************/

		System.out.println ("Connection successful (" + clientSocket.toString() + ")");
		System.out.println ("Waiting for input.....");

		//listener.onLinkEvent(new CommEvent("Status", "Connected", ""));
			
		try 
    	{	
    		BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));

			/*****************************************************************************
    		 * Now we can read and write to and from the client. Our protocol is to wait
    		 * for the client to send us strings which we read until we get a "Bye." string
    	 	 * from the client.
		 	 *****************************************************************************/

 	    	try
 	    	{
 	    		while ((inputLine = in.readLine()) != null)
    			{
 	    			//------------------------------------------------------------
 	    			// need to post message
 	    			//------------------------------------------------------------
      				System.out.println ("Client Message: " + inputLine);
      				
      				mac = CommUtil.parseMACAddress(inputLine);
      				listener.onData(new LinkEvent("Data", "Data", inputLine, this));
      				//------------------------------------------------------------
	   				if (inputLine.equals("Bye."))
    	    		 	break;
   				} // while

			} catch (Exception e) {
				System.err.println("readLine failed::" + e.toString());
				e.printStackTrace();
  				listener.onStatus(new LinkEvent("Status", "Disconnected", e.toString()));
			}
    		
			/*****************************************************************************
    		 * Close up the I/O ports then close up the sockets
		 	 *****************************************************************************/
	    	in.close();
		
   		 	clientSocket.close();
    	} 
		catch (Exception e) {
			System.err.println("exception");
    		//System.exit(1);
		}
	}

	public void send(String message)
	{
		try
		{
			System.out.println ("Send Message (Link) : " + message);
				
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
	
			/*****************************************************************************
			* The protocol is simple: The client sends a message, then waits for two
			* messages from the server and thats it.
			*****************************************************************************/
			/* Here we write a message...												*/
	
			out.write( message, 0, message.length() );
			out.newLine();
			out.flush();
		}
		catch(IOException e)
		{
			
		}
	}
	
	public void disconnect()
	{
		System.out.println ("Disconnect the node (Link)");
		try
		{
			clientSocket.close();
		}
		catch(IOException e)
		{
			
		}
	}

	public String getMACAddress()
	{
		return mac;
	}
	
	public void addListener(LinkEventListener l) {
		// TODO Auto-generated method stub
		System.out.println("Start to add link using addListener.");
		listener = l;
		System.out.println("End to add link using addListener.");
	}
}
