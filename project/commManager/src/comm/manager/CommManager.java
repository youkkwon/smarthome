package comm.manager;

import java.util.ArrayList;

import comm.core.Adapter;
import comm.core.AdapterEventListener;
import comm.wifi.WiFiAdapter;
import comm.bluetooth.BluetoothAdapter;

public class CommManager {

	private static CommManager cm = null;
	private ArrayList<Adapter> adapterList = new ArrayList<Adapter>();
	
	private CommManager()
	{
		initialize();
	}
	
	public static CommManager getInstance()
	{
		if(cm == null)
		{
			cm = new CommManager();
		}
		return cm;
	}
	
	public void initialize()
	{
		adapterList.add(new WiFiAdapter());
		adapterList.add(new BluetoothAdapter());
	}

	public void addListener(AdapterEventListener l)
	{
		for(Adapter adapter : adapterList)
		{
			adapter.addListener(l);
		}	
	}
	
	public void discoverNode(int duration)
	{
		for(Adapter adapter : adapterList)
		{
			adapter.discoverNode(duration);
		}	
	}

	public void registerNode(String msg)
	{
		for(Adapter adapter : adapterList)
		{
			adapter.registerNode(msg);
		}	
	}

	public void rejectNode(String mac)
	{
		for(Adapter adapter : adapterList)
		{
			adapter.disconnectNode(mac);
		}	
	}

}
