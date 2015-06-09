package RM_Core;

import java.util.LinkedList;
import java.util.ListIterator;

//Singleton
public class Scheduler extends Thread {

	private int period;
	private LinkedList<DelayAction> actions;
	private static Scheduler scheduler = new Scheduler(); 
	
	private Scheduler()
	{
		actions =  new LinkedList<DelayAction>();
		period = 1;//5; 			// default 5 seconds;
	}
	
	public static Scheduler getInstance()
	{
		return scheduler;		
	}
	
	public int getPeriod ()
	{
		return period;
	}
	
	public void setPeriod (int period)
	{
		this.period = period;
	}
	
	public void addAction (DelayAction action)
	{
		System.out.println("Scheduled Action is arrived.");
		deleteAction(action);	//	delete if there is already actions (same or confilct)
		actions.add(action);
	}
	
	public void deleteAction (Action cancle_action)
	{
		ListIterator<DelayAction>	iterator = actions.listIterator();
		while (iterator.hasNext())
		{
			Action action = iterator.next();
			if (cancle_action.isConflict(action))
			{
				System.out.println("delete action : " + action.getStatement());
				actions.remove(action);
			}
		}
	}
	
	// 가장 손쉽게는 thread 로 돌려서 period 마다 꺠서 확인하기. ㅎㅎㅎ  나중에 폼나게 바꿉시다요. 
	// TODO expire 처리. 
	public void run()
	{
		System.out.println ("Scheduler is activated : " + actions.size());
		while (true) 
		{
			try {
				Thread.sleep(1000 * period);		
			} catch(Exception e) {
				e.printStackTrace();
			}
			updateScheduler();
		}
	}
	
	public void updateScheduler()
	{
		//System.out.println ("updateScheduler : " + actions.size());
		
		ListIterator<DelayAction>	iterator = actions.listIterator();
		while (iterator.hasNext())
		{
			DelayAction action = iterator.next();
			action.decreaseTime(period);
			System.out.println ("Action : " + action.getStatement() + " time left : " + action.getTimeLeft());
			if (action.isExpired())
			{
				action.postEvent();
				actions.remove(action);
			}
		}
	}	
}
