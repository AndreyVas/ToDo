package application;

import java.util.LinkedList;
import java.util.Observable;

import javafx.stage.Stage;

public class NotesContainer extends Observable
{
	private LinkedList<INotes> noteList;
	private INotes lastUpdatedObject;
	private LinkedList<IReminds> toRemind;
	private Stage primaryStage;
	
	private int noteCount;
	private int stickerCount;
	private int reminderCount;
	private int taskCount;
	
	public Stage getPrimaryStage()
	{
		return this.primaryStage;
	}
	
	public NotesContainer(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
		noteList = new LinkedList<INotes>();
		
		this.noteCount = 0;
		this.stickerCount = 0;
		this.reminderCount = 0;
		this.taskCount = 0;
	}
	
	private void addItemCount(String type, INotes n)
	{
		switch(type)
		{
			case INotes.NOTE:
				if(n != null) n.setNumber(noteCount);
				noteCount++;
				break;
				
			case INotes.FOLDER:
				if(n != null) n.setNumber(noteCount);
				noteCount++;
				break;
			
			case INotes.STICKER:
				if(n != null) n.setNumber(stickerCount);
				stickerCount++;
				break;
				
			case INotes.REMINDER:
				if(n != null) n.setNumber(reminderCount);
				reminderCount++;
				break;
				
			case INotes.TASK:
				if(n != null) n.setNumber(taskCount);
				taskCount++;
				break;
		}
	}
	
	private void reduceItemCount(String type)
	{
		switch(type)
		{
			case INotes.NOTE:
				noteCount--;
				break;
				
			case INotes.FOLDER:
				noteCount--;
				break;
			
			case INotes.STICKER:
				stickerCount--;
				break;
				
			case INotes.REMINDER:
				reminderCount--;
				break;
				
			case INotes.TASK:
				taskCount--;
				break;
		}
	}
	
	public INotes getChanged()
	{
		return this.lastUpdatedObject;
	}
	
	public int getItemsCount(String type)
	{
		switch(type)
		{
			case INotes.NOTE:
				return noteCount;

			case INotes.FOLDER:
				return noteCount;
				
			case INotes.STICKER:
				return stickerCount;
				
			case INotes.REMINDER:
				return reminderCount;
				
			case INotes.TASK:
				return taskCount;
		}
		
		return 0;
	}
	
	public void addNew(INotes n)
	{
		if(n.getParent() == null)
		{
			noteList.add(n);
			
			
			addItemCount(n.getType(), n);
			
			lastUpdatedObject = n;
			
			setChanged();
			notifyObservers(INotes.ADD);
		}
		else
		{
			INotes parent = n.getParent();
			n.setNumber(parent.getChildrens().size());
			
			parent.addChildren(n, null);
			
			lastUpdatedObject = parent;
			
			setChanged();
			notifyObservers(INotes.UPDATE);
		}
	}
	
	public void add(INotes n)
	{
		noteList.add(n);
		
		addItemCount(n.getType(), null);
		
		lastUpdatedObject = n;
		
		setChanged();
		notifyObservers(INotes.LOAD);
	}
	
	public void update(INotes n)
	{
		if(n.getParent() == null)
		{
			lastUpdatedObject = n;
		}
		else
		{
			lastUpdatedObject = n.getRootItem();
		}
		
		setChanged();
		notifyObservers(INotes.UPDATE);
	}
	
	public void moveNote(String itemID, String toID, String moveType)
	{
		// remove note from current position
		
		INotes note = findNote(itemID, null);	

		INotes parent = note.getParent();
		
		if(parent == null)
		{
			// remove item from main container
			
			int num = note.getNumber();
			String type = note.getType().equals(INotes.FOLDER) ? INotes.NOTE : note.getType();
			this.noteList.remove(note);
			
			// update numbers for other notes
			
			for(INotes n : this.noteList)
			{				
				if(type.equals(n.getType().equals(INotes.FOLDER) ? INotes.NOTE : n.getType()) && n.getNumber() > num)
				{
					 n.setNumber(n.getNumber() -1 );
		
				}
			}
		}
		else
		{
			// remove item from notes container
			
			parent.killChild(note);
		}
		
		// set note to new position
		
		INotes targetNote = findNote(toID, null);
		INotes targetParent = targetNote.getParent();
		String targetNoteType = targetNote.getType().equals(INotes.FOLDER) ? INotes.NOTE : targetNote.getType();
		
		if(targetParent == null)
		{
			int position = 0;
			
			
			switch(moveType)
			{
				case NoteManager.MOVE_AFTER:
					position = targetNote.getNumber() + 1;
				break;
				
				case NoteManager.MOVE_BEFORE:
					position = targetNote.getNumber();
					break;
					
				case NoteManager.MOVE_IN:
					targetNote.addChildren(note, null);
					break;
			}

			if(!moveType.equals(NoteManager.MOVE_IN))
			{
				for(INotes n : this.noteList)
				{
					if(targetNoteType.equals(n.getType().equals(INotes.FOLDER) ? INotes.NOTE : n.getType()) && n.getNumber() >= position)
					{
						n.setNumber(n.getNumber() + 1);
					}
				}

				note.setParent(null);
				note.setNumber(position);
				this.noteList.add(note);
			}	
		}
		else
		{
			switch(moveType)
			{
				case NoteManager.MOVE_AFTER:
					targetParent.addChildren(note, targetNote.getNumber() + 1);
				break;
				
				case NoteManager.MOVE_BEFORE:
					targetParent.addChildren(note, targetNote.getNumber());
					break;
					
				case NoteManager.MOVE_IN:
					targetNote.addChildren(note, null);
					break;
			}
		}
		
		save();
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
	
	public void delete(INotes note)
	{		
		if(note.getParent() == null)
		{
			lastUpdatedObject = note;
			lastUpdatedObject.closeActiveWindow();
			
			noteList.remove(note);
			reduceItemCount(note.getType());
			
			String type = note.getType().equals(INotes.FOLDER) ? INotes.NOTE : note.getType();

			for(INotes n : this.noteList)
			{
				String t = n.getType().equals(INotes.FOLDER) ? INotes.NOTE : n.getType();
				
				if(t.equals(type) && n.getNumber() > note.getNumber())
				{
					n.setNumber(n.getNumber() - 1);
				}
					
			}
			
			setChanged();
			notifyObservers(INotes.REMOVE);
		}
		else
		{
			INotes parent = note.getParent();
			note.closeActiveWindow();
			
			lastUpdatedObject = note.getRootItem();
			
			parent.killChild(note);
			
			setChanged();
			notifyObservers(INotes.UPDATE);
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

	public INotes findNote(String id, LinkedList<INotes> list)
	{
		list = (list == null) ? this.noteList : list;
		
		for(INotes n : list)
		{
			if(n.getChildrens() != null && n.getChildrens().size() > 0)
			{
				INotes note = findNote(id, n.getChildrens());
				
				if(note != null)
					return note;
			}
			
			if(n.getID().equals(id))
				return n;
		}
		
		return null;
	}

}
