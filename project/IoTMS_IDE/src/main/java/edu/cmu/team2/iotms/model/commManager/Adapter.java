package edu.cmu.team2.iotms.model.commManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Adapter implements Runnable, LinkEventListener {

	private Thread t;
	private AdapterEventListener adapterListener;
	private Discovery wifiDiscovery;
	private ArrayList<Link> linkList;
	
	public Adapter()
	{
		System.out.println("[CM - Process] WiFiAdapter Start...");
		linkList = new ArrayList<Link>();
		t = new Thread(this, "wifi");
		t.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		System.out.println("[CM - Process] Server Socket Thread Start...");
		
    	ServerSocket serverSocket = null;							// Server socket object
		Socket clientSocket = null;									// Client socket object
    	int	portNum = CommUtil.getServerPort();											// Port number for server socket
    	
    	/*****************************************************************************
	 	* First we instantiate the server socket. The ServerSocket class also does
	 	* the listen()on the specified port.
	 	*****************************************************************************/
		try
		{
    		serverSocket = new ServerSocket(portNum);
    		System.out.println ( "\n\n[CM - Process] Waiting for connection on port " + portNum + "." );
    	}
		catch (IOException e)
    	{
    		System.err.println( "\n\n[CM - Process] Could not instantiate socket on port: " + portNum + " " + e);
    		System.exit(1);
    	}
		
		while(true)
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

	    	//linkList.add(new Link("Link", clientSocket, this));
	    	linkList.add(new Link("Link", clientSocket, this));
	    	
	    	//adapterListener.onAdapterEvent(new CommEvent("Add_node", "Connect", (new Integer(clientSocketList.indexOf(clientSocket))).toString()));
			
    	} // while loop
		
    	//serverSocket.close();

	}
	
	public void addListener(AdapterEventListener l)
	{
		adapterListener = l;
	}
	
	public void discoverNode(int duration)
	{
		wifiDiscovery = new Discovery("WiFiDiscovery");
		wifiDiscovery.addListener(adapterListener);
	}

	public void registerNode(String msg)
	{
		wifiDiscovery.registerNode(msg);
	}
	
	public void disconnectNode(String mac)
	{
		for(Link link : linkList)
		{
			if(link.getMACAddress().equals(mac))
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				link.disconnect();
				linkList.remove(link);
			}
		}
	}

	@Override
	public void onData(LinkEvent event) {
		// TODO Auto-generated method stub
		adapterListener.onAddNode(new AdapterEvent("Add_node", "Connect", event.getMessage(), event.getLink()));
	}

	@Override
	public void onStatus(LinkEvent event) {}

}
