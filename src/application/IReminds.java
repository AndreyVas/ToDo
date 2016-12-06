package application;

import java.time.LocalDateTime;

import javafx.scene.layout.StackPane;

public interface IReminds 
{
	public static int NO_REMIND = 0;
	public static int FIRST_REMIND = 1;
	public static int SECOND_REMIND = 2;
	public static int THIRD_REMIND = 3;
	
	public static String REMIND_COUNT = "remind_count";
	
	public LocalDateTime getExpire();
	public boolean isRemind();
	public int getRemindCount();
	public void setRemindCount(int remindCount);
	
	public StackPane getRemindItem(Settings settings);
}
