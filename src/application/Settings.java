package application;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Settings 
{
	private double opacityActive;
	private double opacityInactive;
	
	private int widthMain;
	private int heightMain;
	
	private int widthAddItems;
	private int heightAddItems;
	
	private int widthRemindWidnow;
	private int heightRemindWindow;

	private String PATH_TO_JAR;
	private String NOTE_SAVE_FOLDER;
	private String ATTACHMENTS_SAVE_FOLDER;
	public static final String NOTES = "notes.xml";
	
	private long firstRemind;
	private long secondRemind;
	private long thirdRemind;

	Settings() 
	{
		this.opacityActive = 0.9;
		this.opacityInactive = 0.5;
		
		this.widthMain = 400;
		this.heightMain = 400;
		
		this.widthAddItems = 250;
		this.heightAddItems = 200;
		
		this.widthRemindWidnow = 400;
		this.heightRemindWindow = 300;
		
		this.firstRemind = 60000 * (24 * 60) ;	// one day
		this.secondRemind = 60000 * 60;		// one hour
		this.thirdRemind = 60000 * 10; 	// 10 min
		
		/*this.firstRemind = 60000 * 5 ;	// 5 min
		this.secondRemind = 60000 * 3;		// 3 min
		this.thirdRemind = 60000 * 1; 	// 1 min*/
		
		if(System.getProperty("os.name").toLowerCase().contains("win"))
		{
			PATH_TO_JAR = "";
			NOTE_SAVE_FOLDER = "DATA";
			ATTACHMENTS_SAVE_FOLDER = NOTE_SAVE_FOLDER + File.separator + "ATTACHMENTS";
		}
		else
		{
			PATH_TO_JAR = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			PATH_TO_JAR = PATH_TO_JAR.substring(0, PATH_TO_JAR.lastIndexOf(File.separator));
			NOTE_SAVE_FOLDER = "DATA";
			ATTACHMENTS_SAVE_FOLDER = NOTE_SAVE_FOLDER + File.separator + "ATTACHMENTS";
			
			try
			{
				PATH_TO_JAR = URLDecoder.decode(PATH_TO_JAR, "UTF-8");
			}
			catch(UnsupportedEncodingException e)
			{
				Messages.showError("Settings class, Constructor : " + e.getMessage());
			}
		}
	}
	
	public String saveString()
	{
		if(this.PATH_TO_JAR.equals(""))
		{
			return this.NOTE_SAVE_FOLDER;
		}
		else
		{
			return this.PATH_TO_JAR + File.separator + this.NOTE_SAVE_FOLDER;
		}
	}
	
	public String attachmentSaveString()
	{
		if(this.PATH_TO_JAR.equals(""))
		{
			return this.ATTACHMENTS_SAVE_FOLDER;
		}
		else
		{
			return this.PATH_TO_JAR + File.separator + this.ATTACHMENTS_SAVE_FOLDER;
		}
	}
	
	public long getFirstRemind()
	{
		return this.firstRemind;
	}
	
	public long getSecondRemind()
	{
		return this.secondRemind;
	}
	
	public long getThirdRemind()
	{
		return this.thirdRemind;
	}
	
	public double getOpacityActive()
	{
		return this.opacityActive;
	}
	
	public double getOpacityInactive()
	{
		return this.opacityInactive;
	}
	
	public int getWidthMain()
	{
		return this.widthMain;
	}
	
	public int getHeightMain()
	{
		return this.heightMain;
	}
	
	public int getWidthAddItems()
	{
		return this.widthAddItems;
	}
	
	public int getHeightAddItems()
	{
		return this.heightAddItems;
	}
	
	public int getWidthRemindWindow()
	{
		return this.widthRemindWidnow;
	}
	
	public int getHeightRemindWindow()
	{
		return this.heightRemindWindow;
	}
}
