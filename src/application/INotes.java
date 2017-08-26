package application;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public interface INotes 
{
	public final String ADD = "add";
	public final String REMOVE = "remove";
	public final String LOAD = "load";
	public final String UPDATE = "update";
	public final String SAVE = "save";
	public final String SHOW_REMIND = "show_remind";
	
	public final String ITEM = "item";
	public final String TYPE = "type";
	public final String STATUS = "status";
	public final String CREATED = "created";
	public final String EXPIRE = "expire";
	public final String CONTENT = "content";
	public final String TITLE = "title";
	public final String NAME = "name";
	public final String TEXT = "text";
	public final String X = "X";
	public final String Y = "Y";
	public final String WIDHT = "WIDTH";
	public final String HEIGHT = "HEIGHT";
	public final String SHOWN = "SHOWN";
	public final String SUBTASKS = "subtasks";
	public final String CHILDRENS = "childrens";
	public final String REMIND = "remind";
	public final String IMPORTATN = "importatn";
	public final String NUMBER = "number";
	
	public final String NOTE = "note";
	public final String TASK = "task";
	public final String STICKER = "sticker";
	public final String REMINDER = "reminder";
	public final String FOLDER = "folder";
	
	public final String ACTIVE = "active";
	public final String CANSELED = "canseled";
	public final String COMPLETED = "completed";
	
	public final String NON = "non";
	
	public final String RESIZE_LT = "resizeLT";
	public final String RESIZE_RT = "resizeRT";
	public final String RESIZE_LB = "resizeLB";
	public final String RESIZE_RB = "resizeRB";
	public final String RESIZE_T = "resizeT";
	public final String RESIZE_B = "resizeB";
	public final String RESIZE_L = "resizeL";
	public final String RESIZE_R = "resizeR";
	public final String MOVE = "move";
	
	public final String IMG_ADD_FOLDER = "addFolder.png";
	public final String IMG_FOLDER = "folder.png";
	public final String IMG_OPENT_FOLDER = "openFolder.png";
	
	public final String NOT_ACTION = "notAction";

	public Element getXML(Document doc);
	public VBox getTabItem(Settings settings);
	public String getType();
	public String getID();
	public void show(Settings settings);
	public void hide();
	public boolean isShown();
	public void setShown(Boolean b);	
	public NotePosition getPosition();
	public Stage getStage();
	public ToolBar getMovedArea();
	public INotes getParent();
	public void setParent(INotes n);
	public NotesContainer getContainer();
	public void updateSubNotes(String type, TreeItem<VBox> rootTree, INotes note, Settings settings);
	public TreeItem<VBox> getSubTasksTree();
	public void closeActiveWindow();
	public void setStatus(String status);
	public String getStatus();
	public HBox addControls(INotes note, Settings settings);
	public boolean isImportant();
	public void setImportant(Boolean b);
	public LinkedList<INotes> getChildrens();
	public void addChildren(INotes n, Integer position);
	public INotes getRootItem();
	public void killChild(INotes child);
	public int getNumber();
	public void setNumber(int number);
}
