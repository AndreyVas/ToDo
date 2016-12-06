package application;

import java.time.ZoneId;
import java.util.LinkedList;
import javafx.application.Platform;

public class Remind implements Runnable
{
	String name;
	Thread thread;
	boolean suspendFlag;
	NotesContainer notes;
	Settings settings;
	
	Remind(NotesContainer notes, Settings settings)
	{
		this.notes = notes;
		name = "reminde";
		thread = new Thread(this, name);
		suspendFlag = true;
		thread.start();
		this.settings = settings;
	}
	
	@Override
	public void run() 
	{
		try
		{
			while(true)
			{
				long nowInMilliseconds =  System.currentTimeMillis();// LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
				long minRemindInMilliseconds = Long.MAX_VALUE;
				LinkedList<IReminds> forRemind = new LinkedList<IReminds>();
	
				boolean remind = false;
				
				for(IReminds r : notes.getRemindObjects())
				{
					if(r.isRemind())
					{
						ZoneId zoneId = ZoneId.systemDefault();
						long remindeInMilliseconds = r.getExpire().atZone(zoneId).toEpochSecond() * 1000;//r.getExpire().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();

						if(remindeInMilliseconds - nowInMilliseconds > settings.getFirstRemind())
						{
							if((remindeInMilliseconds - nowInMilliseconds) - settings.getFirstRemind() < minRemindInMilliseconds)
								minRemindInMilliseconds = (remindeInMilliseconds - nowInMilliseconds) - settings.getFirstRemind();
						}
						if(remindeInMilliseconds - nowInMilliseconds <= settings.getFirstRemind() && r.getRemindCount() == IReminds.NO_REMIND)
						{
							forRemind.add(r);
							remind = true;
							r.setRemindCount(IReminds.FIRST_REMIND);
							
							if((remindeInMilliseconds - nowInMilliseconds) - settings.getSecondRemind() < minRemindInMilliseconds)
								minRemindInMilliseconds = (remindeInMilliseconds - nowInMilliseconds) - settings.getSecondRemind();
						}
						else if(remindeInMilliseconds - nowInMilliseconds <= settings.getSecondRemind() && r.getRemindCount() == IReminds.FIRST_REMIND)
						{
							forRemind.add(r);
							remind = true;
							r.setRemindCount(IReminds.SECOND_REMIND);
							
							if((remindeInMilliseconds - nowInMilliseconds) - settings.getThirdRemind() < minRemindInMilliseconds)
								minRemindInMilliseconds = (remindeInMilliseconds - nowInMilliseconds) - settings.getThirdRemind();
						}
						else if(remindeInMilliseconds - nowInMilliseconds <= settings.getThirdRemind() && r.getRemindCount() == IReminds.SECOND_REMIND)
						{
							forRemind.add(r);
							remind = true;
							r.setRemindCount(IReminds.THIRD_REMIND);
						}
						else
						{
							if(r.getRemindCount() == IReminds.THIRD_REMIND)
							{
								forRemind.add(r);
							}
						}
					}
				}

				if(remind)
					Platform.runLater(new Runnable() 
					{
	                    @Override public void run() 
	                    {
	                    	notes.remind(forRemind);
	                    }
	                });
	
				this.suspendFlag = true;
				
				synchronized(this)
				{
					/*
						Calendar dating = Calendar.getInstance();
						dating.setTimeInMillis(minRemindInMilliseconds);
					    SimpleDateFormat formating = new SimpleDateFormat("YYYY/MM/dd | HH:mm:ss:SSS");
					    System.out.println("sleep to " +  minRemindInMilliseconds / 1000);
*/
						wait((minRemindInMilliseconds < 0) ? 0 : minRemindInMilliseconds);
				}
			}
		}
		catch(InterruptedException e)
		{
			//System.out.println(name + " finished");
		}
	}
	
	synchronized void suspend()
	{
		suspendFlag = true;
	}

	synchronized void resume()
	{
		suspendFlag = false;
		notify();
	}
}
