package application;

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
	
	public static final String ROOT_SETTINGS_PATH = "DATA";
	//public static final String SETTINGS = "settings.xml";
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
