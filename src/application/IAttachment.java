package application;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.scene.layout.VBox;

public interface IAttachment 
{
	public static final String FILE = "file";
	public static final String LINK = "link";
	
	public static final String NAME = "name";
	public static final String ADDRESS = "address";
	public static final String PATH = "path";
	
	//public static final String IMG_LINK = "link.png";
	//public static final String IMG_FILE = "file.png";
	
	public Element getXML(Document doc);
	public VBox getGUIItem();
	public String getType();
	public String getID();
	public String getName();
	public String getPath();
	public void open();
}
