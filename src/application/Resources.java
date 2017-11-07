package application;

import java.io.File;

public class Resources 
{
	private static final String PATH = "res";
	
	// images
	
	public static final String IMG_ADD_FILE = "addFile.png";
	public static final String IMG_ADD_FOLDER = "addFolder.png";
	public static final String IMG_ADD_LINK = "addLink.png";
	public static final String IMG_ADD_TASK = "addTask.png";
	public static final String IMG_CANSEL = "cansel.png";
	public static final String IMG_CANSEL_SMAL = "canselSmal.png";
	public static final String IMG_CLOSE = "close.png";
	public static final String IMG_COGWHEEL = "cogwheel.png";
	public static final String IMG_CONFIRMATION = "confirmation.png";
	public static final String IMG_COPY_LINK = "copyLink.png";
	public static final String IMG_DONE = "done.png";
	public static final String IMG_FILE = "file.png";
	public static final String IMG_FIRST_REMIND = "firstRmind.png";
	public static final String IMG_FOLDER = "folder.png";
	public static final String IMG_ICON = "icon.png";
	public static final String IMG_IMPORTANT = "important.png";
	public static final String IMG_IMPORTANT_NO = "importantNo.png";
	public static final String IMG_IMPORTANT_YES = "importantYes.png";
	public static final String IMG_LINK = "link.png";
	public static final String IMG_LOAD = "load.png";
	public static final String IMG_NOTE_ICON = "noteIcon.png";
	public static final String IMG_OK = "ok.png";
	public static final String IMG_OPEN_FOLDER = "openFolder.png";
	public static final String IMG_REMINDE = "reminde.png";
	public static final String IMG_REMINDER_ICON = "reminderIcon.png";
	public static final String IMG_RESIZE = "resize.png";
	public static final String IMG_RESTORE = "restore.png";
	public static final String IMG_SAVE = "save.png";
	public static final String IMG_SECOND_REMIND = "secondRemind.png";
	public static final String IMG_SHOW_NO = "showNo.png";
	public static final String IMG_SHOW_YES = "showYes.png";
	public static final String IMG_STICKER_ICON = "stickerIcon.png";
	public static final String IMG_TASK_ICON = "taskIcon.png";
	public static final String IMG_THIRD_REMIND = "thirdRemind.png";
	public static final String IMG_TO_ATTACHMENTS = "toAttachments.png";
	public static final String IMG_TO_TEXT = "toText.png";
	public static final String IMG_TRASH = "trash.png";

	// Audio files
	
	public static final String AUD_ALARM = "alarm.waw";
	
	public static String getResource(String id)
	{
		return PATH + File.separator + id;
	}
}
