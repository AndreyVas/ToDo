package application;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;

import javafx.stage.Stage;

public class NotesContainer extends Observable
{
	private LinkedList<INotes> noteList;
	private INotes lastUpdatedObject;
	private LinkedList<IReminds> toRemind;
	private Stage primaryStage;
	
	public Stage getPrimaryStage()
	{
		return this.primaryStage;
	}
	
	public NotesContainer(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
		noteList = new LinkedList<INotes>();
	}
	
	public INotes getChanged()
	{
		return this.lastUpdatedObject;
	}
	
	public void addNew(INotes n)
	{
		if(n.getParent() == null)
			noteList.add(n);
		
		lastUpdatedObject = n;
		
		setChanged();
		notifyObservers(INotes.ADD);
	}
	
	public void add(INotes n)
	{
		noteList.add(n);
		lastUpdatedObject = n;
		
		setChanged();
		notifyObservers(INotes.LOAD);
	}
	
	public void update(INotes n)
	{
		
		lastUpdatedObject = n;

		setChanged();
		notifyObservers(INotes.UPDATE);
	}
	
	public void save()
	{
		setChanged();
		notifyObservers(INotes.SAVE);
	}

	public void remind(LinkedList<IReminds> toRemind)
	{
		if(toRemind.size() != 0)
		{
			this.toRemind = new LinkedList<IReminds>();
			
			for(IReminds r : toRemind)
			{
				this.toRemind.add(r);
			}
			
			setChanged();
			notifyObservers(INotes.SHOW_REMIND);
		}
	}

	public LinkedList<INotes> getNoteList()
	{
		return new LinkedList<INotes>(noteList);
	}

	public void delete(String id)
	{
		Iterator<INotes> iter = noteList.iterator();
		
		while(iter.hasNext())
		{
			INotes in = iter.next();
			
			if(in.getID().equals(id))
			{
				lastUpdatedObject = in;
				lastUpdatedObject.hide();
				
				iter.remove();
				
				setChanged();
				notifyObservers(INotes.REMOVE);
			}
		}
	}

	synchronized public LinkedList<IReminds> getRemindObjects()
	{
		LinkedList<IReminds> remindsObjects = new LinkedList<IReminds>();
		
		for(INotes n : noteList)
		{
			if(n.getType().equals(INotes.REMINDER) || n.getType().equals(INotes.TASK))
			{
				remindsObjects.add((IReminds) n);
			}
		}
		return remindsObjects;
	}
	
	public LinkedList<IReminds> getToRemind()
	{
		return this.toRemind;
	}
}
