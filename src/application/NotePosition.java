package application;

public class NotePosition 
{
	private double x;
	private double y;
	
	private double width;
	private double height;
	
	public static final double DEFAULT_X = 100;
	public static final double DEFAULT_Y = 100;
	
	public static final double DEFAULT_WIDHT = 300;
	public static final double DEFAULT_HEIGHT = 300;
	
	
	NotePosition()
	{
		x = DEFAULT_X;
		y = DEFAULT_Y;
		
		width = DEFAULT_WIDHT;
		height = DEFAULT_HEIGHT;
	}
	
	NotePosition(double x, double y)
	{
		this.x = x;
		this.y = y;
		
		this.width = DEFAULT_WIDHT;
		this.height = DEFAULT_HEIGHT;
	}
	
	NotePosition(double x, double y, double widht, double height)
	{
		this.x = x;
		this.y = y;
		
		this.width = widht;
		this.height = height;
	}
	
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setSize(double widht, double height)
	{
		this.width = widht;
		this.height = height;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getWidth()
	{
		return width;
	}
	
	public double getHeight()
	{
		return height;
	}
	
	public boolean isIntersects(NotePosition np)
	{
		return ( this.y < np.y + np.height || this.y + this.height > np.y || this.x + this.width < np.x || this.x > np.x + np.width );
	}
}
