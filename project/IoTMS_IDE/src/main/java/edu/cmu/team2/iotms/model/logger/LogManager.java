package edu.cmu.team2.iotms.model.logger;

import java.util.concurrent.LinkedBlockingQueue;

import edu.cmu.team2.iotms.model.eventBus.IoTMSEventBus;
import Database.LoggerDao;

public class LogManager extends Thread {
	private LinkedBlockingQueue<String> logQueue;
	private static LogManager logmanager = new LogManager();
	
	private LogManager()
	{
		logQueue =  new LinkedBlockingQueue<String>();
		IoTMSEventBus.getInstance().register(new LogListener());
	}
	
	public static LogManager getInstance()
	{
		return logmanager;
	}
	
	public void pushLog(String log) {
		logQueue.add(log);
	}
	
	private void processLog(String log)
	{	
		LoggerDao.getInstance().addEventHistory(log);
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			String log;
			try {
				log = logQueue.take();
				processLog(log);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}
	
	}
}
