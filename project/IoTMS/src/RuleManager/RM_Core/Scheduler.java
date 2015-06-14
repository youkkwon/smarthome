package RuleManager.RM_Core;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

//Singleton
public class Scheduler extends Thread {

	private int period;
	private LinkedBlockingQueue<DelayAction> actions;
	
	private static Scheduler scheduler = new Scheduler(); 

	private Scheduler()
	{
		actions =  new LinkedBlockingQueue<DelayAction>();
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
	
	public synchronized void addAction (DelayAction action)
	{
		System.out.println("Scheduled Action is arrived." + action.getStatement());
		deleteAction(action);	//	delete if there is already actions (same or confilct)
		actions.add(action);
	}
	
	public void deleteAction (Action cancle_action)
	{
		Iterator<DelayAction>	iterator = actions.iterator();
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
	
	public void cancelStateAction ()
	{
		Iterator<DelayAction>	iterator = actions.iterator();
		while (iterator.hasNext())
		{
			Action action = iterator.next();
			if (action.isActionOn("*@8"))
			{
				System.out.println("cancel action due to state change: " + action.getStatement());
				actions.remove(action);
			}
		}
	}
	
	
	// 가장 손쉽게는 thread 로 돌려서 period 마다 꺠서 확인하기. ㅎㅎㅎ  나중에 폼나게 바꿉시다요. 
	// TODO expire 처리. 
	public void run()
	{
		while (true) 
		{
			try {
				Thread.sleep(1000 * period);		
			} catch(Exception e) {
				e.printStackTrace();
			}
			if (!actions.isEmpty())
				updateScheduler();
		}
	}
	
	public synchronized void updateScheduler()
	{
		Iterator<DelayAction>	iterator = actions.iterator();
		while (iterator.hasNext())
		{
			DelayAction action = iterator.next();
			action.decreaseTime(period);
			//System.out.println ("Action : " + action.getStatement() + " time left : " + action.getTimeLeft());
			if (action.isExpired())
			{
				System.out.println ("Scheduled action is executed." + action.getStatement());
				action.postEvent();
				actions.remove(action);
			}
		}
	}
}
