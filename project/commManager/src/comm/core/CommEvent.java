package comm.core;

public class CommEvent {
	
	private String type; // status, data 
	private String status; // Connected, Disconnected, Data, Discovery
	private String message;
	private Object link;
	
	public CommEvent(String t, String s, String m)
	{
		type = t;
		status = s;
		message = m;
	}

	public CommEvent(String t, String s, String m, Object l)
	{
		type = t;
		status = s;
		message = m;
		link = l;
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public Object getLink()
	{
		return link;
	}
}
