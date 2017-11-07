package application;

import java.util.LinkedList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Folder implements INotes
{
	INotes parent;
	
	protected int id;
	protected boolean important;
	protected NotesContainer lincToCont;
	protected String type;
	protected boolean shown;
	protected Resize resizeObject;
	
	private LinkedList<INotes> childrens;
	
	private String name;
	private int number;
	
	Folder(String name, Integer number, NotesContainer lincToCont, Resize resizeObject, INotes parent)
	{
		this.resizeObject = resizeObject;
		this.lincToCont = lincToCont;
		this.id = this.hashCode();
		this.type = INotes.FOLDER;
		
		this.parent = parent;
		this.name = name;
		this.shown = false;
		
		this.childrens = new LinkedList<INotes>();
		

		if(number != null)
		{
			this.number = number;
		}
	}
	
	public static void createItemWindow(Settings settings, NotesContainer notes, Resize resizeObject, INotes parent)
	{
		String inviteText = "Enter a folder name...";
		
		Stage stage = new Stage();
		stage.initOwner(notes.getPrimaryStage());
		stage.initStyle(StageStyle.TRANSPARENT);
		
		BorderPane pane = new BorderPane();
		pane.setOpacity(settings.getOpacityActive());
		
    	Scene scene = new Scene(pane, 300,150, Color.TRANSPARENT);
    	scene.getStylesheets().add("application/note.css");
    	scene.getStylesheets().add("application/application.css");
    	
    	HBox tbButtonsLeft = new HBox();
		HBox tbButtonsRight = new HBox();
    	
    	//---------------------------------------------------------
		
		VBox content = new VBox();
		  
    	TextField enterName = new TextField(inviteText);
    	enterName.getStyleClass().add("noteTitle");
    	//enterName.getStyleClass().add("noteText");
    	
    	enterName.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if(newValue)
				{
					if(enterName.getText().equals(inviteText))
						enterName.setText("");
				}
				else
				{
					if(enterName.getText().equals(""))
						enterName.setText(inviteText);
				}
			}
		});
    	
    	content.getChildren().addAll(enterName);
    	
    	//---------------------------------------------------------
    	
    	ImageView ci = new ImageView(Resources.getResource(Resources.IMG_CLOSE));
		Button closeB = new Button();
		closeB.setGraphic(ci);
		closeB.getStyleClass().add("buttons");
		
		closeB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				stage.close();
			}
		});
		
		ImageView coi = new ImageView(Resources.getResource(Resources.IMG_CONFIRMATION));
		Button confirmationB = new Button();
		confirmationB.setGraphic(coi);
		confirmationB.getStyleClass().add("buttons");
		
		confirmationB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				if(!enterName.getText().equals(inviteText))
				{
					if(parent == null)
						notes.addNew(new Folder(enterName.getText(), null, notes, resizeObject, null));
					else
					{
						parent.addChildren(new Folder(enterName.getText(), null, notes, resizeObject, parent), null);
					}
					
					stage.close();
				}
			}
		});

		tbButtonsLeft.getStyleClass().add("buttonsLeftCont");
		tbButtonsLeft.getChildren().add(confirmationB);

		tbButtonsRight.getStyleClass().add("buttonsRightCont");

		tbButtonsRight.getChildren().add(closeB);
		
		StackPane tbButtonsCon = new StackPane();
		HBox.setHgrow(tbButtonsCon, Priority.ALWAYS);
		
		tbButtonsCon.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				if(arg0.getX() > tbButtonsCon.getWidth()/2)
				{
					tbButtonsRight.setMouseTransparent(false);
				}
				else
				{
					tbButtonsRight.setMouseTransparent(true);
				}
			}
		});
		
		tbButtonsCon.getChildren().addAll(tbButtonsLeft, tbButtonsRight);
		
		ToolBar tb = new ToolBar();
		tb.getStyleClass().add("topMenu");
		tb.getItems().addAll(tbButtonsCon);
		tb.setBackground(Background.EMPTY);
		
    	//---------------------------------------------------------
		
		HBox resizedCont = new HBox();
		resizedCont.setAlignment(Pos.CENTER_RIGHT);
		ImageView resizeImg = new ImageView(Resources.getResource(Resources.IMG_RESIZE));
		Label resized = new Label();
		resized.setGraphic(resizeImg);
		resizedCont.getChildren().add(resized);
		
		//---------------------------------------------------------
    	
		pane.setTop(tb);
    	pane.setCenter(content);
    	pane.setBottom(resizedCont);
        
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(Resources.getResource(Resources.IMG_NOTE_ICON)));
    	stage.show();
    	
    	//---------------------------------------------------------
    	
    	resizeObject.setResized(stage, tb, resized);
	}
	
	public static INotes createItem(NodeList folderItem, NotesContainer lincToCont, Resize resizeObject, INotes parent)
	{
		if(folderItem != null)
		{
			LinkedList<INotes> childrenCont = new LinkedList<INotes>();		
			Folder folder = new Folder("",  null, lincToCont, resizeObject, parent);
			Integer number = null;
			
			for(int i = 0; i < folderItem.getLength(); i++)
		    {
				switch(folderItem.item(i).getNodeName())
				{
					case INotes.NAME:
						folder.setName(folderItem.item(i).getTextContent());
						break;
						
					case INotes.SHOWN:
						folder.setShown(Boolean.valueOf(folderItem.item(i).getTextContent()));
						break;
						
					case INotes.NUMBER:
						number = Integer.valueOf(folderItem.item(i).getTextContent());
						break;
						
					case INotes.CHILDRENS:
						
						NodeList items = folderItem.item(i).getChildNodes();
						
						for(int j = 0; j < items.getLength(); j++)
						{
							if(items.item(j).getNodeName().equals("item"))
					    	{
								NodeList noteItems = items.item(j).getChildNodes();
					    		
					    		NodeList content = null;
					    		String type = "";
					    		
					    		for(int k = 0; k < noteItems.getLength(); k++)
					    		{
					    			
					    			switch(noteItems.item(k).getNodeName())
					    			{
						    			case INotes.TYPE:
						    				type = noteItems.item(k).getTextContent();
						    				
						    				break;
						    				
						    			case INotes.CONTENT:
						    				content = noteItems.item(k).getChildNodes();
						    				break;
					    			}
					    		}
					    		
					    		INotes tmpNote = null;

					    		switch(type)
					    		{
					    			case INotes.NOTE:
					    				tmpNote = Note.createItem(content, resizeObject, lincToCont, folder);
					    				break;
					    				
					    			case INotes.FOLDER:
					    				tmpNote = Folder.createItem(content, lincToCont, resizeObject, folder);
					    				break;
					    		}
					    		
					    		childrenCont.add(tmpNote);
					    	}
						}

						folder.setChildren(childrenCont);
						
						

					break;
				}
				
				
		    }
			
			//-------------------set number----------------------------
			
			if(number != null)
			{
				folder.number = number;
			}
			else
			{
				if(parent == null)
				{
					folder.number = lincToCont.getItemsCount(INotes.FOLDER);
				}
				else
					folder.number = parent.getChildrens().size();
			}
			
			//-----------------------------------------------------------
			
			return folder;
		}
		else
		{
			return null;
		}
	}
	
	private void setName(String name)
	{
		this.name = name;
	}
	
	private void setChildren(LinkedList<INotes> childrens)
	{
		this.childrens = childrens;
	}

	public INotes getRootItem()
	{
		if(this.parent != null)
		{
			INotes ret = this.parent;
			
			while(ret.getParent() != null)
			{
				ret = ret.getParent();
			}
			
			return ret;
		}
		else
		{
			return this;
		}
	}
	
	@Override
	public Element getXML(Document doc) 
	{
		Element item = doc.createElement(INotes.ITEM); 
		
		Element type = doc.createElement(INotes.TYPE); 
		type.setTextContent(INotes.FOLDER);
		
		Element content = doc.createElement(INotes.CONTENT);
		
		Element name = doc.createElement(INotes.NAME);
		name.setTextContent(this.name);
		
		Element shown = doc.createElement(INotes.SHOWN);
		shown.setTextContent(String.valueOf(this.shown));
		
		Element number = doc.createElement(INotes.NUMBER);
		number.setTextContent(String.valueOf(this.number));
		
		Element children = doc.createElement(INotes.CHILDRENS);
		
		// load notes and sub folders to content...
		for(INotes n : this.childrens)
		{
			children.appendChild(n.getXML(doc));
		}
		
		content.appendChild(name);
		content.appendChild(children);
		content.appendChild(number);
		content.appendChild(shown);
		
		item.appendChild(type);
		item.appendChild(content);
		
		return item;
	}

	@Override
	public VBox getTabItem(Settings settings) 
	{
		Label name = new Label(this.name);
		ImageView folderIcon;

		if(this.shown)
		{
			// if folder opened
			folderIcon = new ImageView(Resources.getResource(Resources.IMG_OPEN_FOLDER));
		}
		else
		{	
			// if folder not opened
			folderIcon = new ImageView(Resources.getResource(Resources.IMG_FOLDER));
		}
		
		name.setGraphic(folderIcon);
		
		StackPane cont = new StackPane(name);
		//cont.setId(String.valueOf(this.id));
		
		name.getStyleClass().add("tabPaneItemText");
		cont.getStyleClass().add("tabPaneItem");
		
		HBox controls = addControls(this, settings);
		
		controls.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				name.setOpacity(0.4);
			}
		});
		
		controls.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				name.setOpacity(1);
			}
		});
		
		cont.getChildren().add(controls);
		
		
		
		//-----------------------------------
		
		VBox ret = new VBox(cont);
		ret.setId(String.valueOf(this.id));
		
		//-----------------------------------
		// create children conteiner
		
		if(this.shown)
		{
			VBox childrenCont = new VBox();
			childrenCont.getStyleClass().add("tabPanSubItemCont");
			
			if(this.childrens.size() != 0)
			{
				for(INotes n : this.childrens)
				{
					VBox tabItem = n.getTabItem(settings);

					tabItem.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
					{
						@Override
						public void handle(MouseEvent event) 
						{
							n.show(settings);
							
							event.consume();
						}
					});
					
					int i;
					
					for(i = 0; i < childrenCont.getChildren().size(); i++)
					{
						if(n.getNumber() < this.lincToCont.findNote(childrenCont.getChildren().get(i).getId(), this.childrens).getNumber())
							break;
					}
					
					childrenCont.getChildren().add(i, tabItem);
				}
			}
			else
			{
				name.setText("(empty...) " + this.name);
			}
			
			ret.getChildren().add(childrenCont);
		}
		
		//-----------------------------------
		
		return ret;
	}
	
	public void setParent(INotes n)
	{
		this.parent = n;
	}
	
	public void addChildren(INotes n, Integer position)
	{
		if(position == null)
		{
			this.childrens.add(n);
		}
		else
		{
			for(INotes note : this.childrens)
			{
				if(note.getNumber() >= position)
				{
					note.setNumber(note.getNumber() + 1);
				}
			}
			
			n.setNumber(position);
			this.childrens.add(n);	
		}
		
		this.shown = true;
		n.setParent(this);
		
		this.lincToCont.update(this);
	}
	
	@Override
	public String getType() 
	{
		return this.type;
	}

	@Override
	public String getID() 
	{
		return String.valueOf(this.id);
	}

	@Override
	public void show(Settings settings) 
	{	
		// open and close folder
		this.shown = !this.shown;
		
		this.getContainer().update(this);
	}

	public int getNumber()
	{
		return this.number;
	}
	
	public void setNumber(int number)
	{
		this.number = number;
	}
	
	@Override
	public void hide() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isShown() 
	{
		// TODO Auto-generated method stub
		return this.shown;
	}

	@Override
	public void setShown(Boolean b) 
	{
		this.shown = b;
		
	}

	@Override
	public NotePosition getPosition() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stage getStage() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToolBar getMovedArea() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INotes getParent() 
	{
		return this.parent;
	}

	@Override
	public NotesContainer getContainer() 
	{
		return this.lincToCont;
	}

	@Override
	public void updateSubNotes(String type, TreeItem<VBox> rootTree, INotes note, Settings settings) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public TreeItem<VBox> getSubTasksTree() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeActiveWindow() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(String status) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getStatus() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HBox addControls(INotes folder, Settings settings) 
	{
		HBox controls = new HBox();
		controls.getStyleClass().add("tabPaneItemButtonsCont");
		
		//-------------------------------------------
		
		ImageView delImg = new ImageView(Resources.getResource(Resources.IMG_TRASH));
		Button delete = new Button();
		delete.setGraphic(delImg);
		delete.setTooltip(new Tooltip(NoteManager.DELETE));
		delete.getStyleClass().add("tabPaneItemButtons");
		
		delete.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> folder.getContainer().delete(folder));
		delete.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> delete.setOpacity(0.4));
		delete.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> delete.setOpacity(1));
		
		//-------------------------------------------
		
		ImageView addNoteImg = new ImageView(Resources.getResource(Resources.IMG_ADD_TASK));
		Button addNote = new Button();
		addNote.setGraphic(addNoteImg);
		addNote.setTooltip(new Tooltip(NoteManager.ADD_NOTE));
		addNote.getStyleClass().add("tabPaneItemButtons");
		
		addNote.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> addNote.setOpacity(0.4));
		addNote.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> addNote.setOpacity(1));
		addNote.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> Note.createItemWindow(settings, lincToCont, resizeObject, folder));

		//-------------------------------------------
		
		ImageView addFolderImg = new ImageView(Resources.getResource(Resources.IMG_ADD_FOLDER));
		Button addFolder = new Button();
		addFolder.setGraphic(addFolderImg);
		addFolder.setTooltip(new Tooltip(NoteManager.ADD_FOLDER));
		addFolder.getStyleClass().add("tabPaneItemButtons");
		
		addFolder.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> addFolder.setOpacity(0.4));
		addFolder.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> addFolder.setOpacity(1));
		
		addFolder.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> 
		{
			Folder.createItemWindow(settings, this.lincToCont, this.resizeObject, this);
		});
		
		//-------------------------------------------
		
		controls.getChildren().addAll(addNote, addFolder, delete);
				
		return controls;
	}

	public void killChild(INotes child)
	{
		this.childrens.remove(child);
		
		for(INotes n : this.childrens)
		{
			if(n.getNumber() > child.getNumber())
			{
				n.setNumber(n.getNumber() - 1);
			}
		}
	}
	
	@Override
	public boolean isImportant() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setImportant(Boolean b) 
	{
		// TODO Auto-generated method stub
		
	}

	public LinkedList<INotes> getChildrens()
	{
		return this.childrens;
	}
	
	public void addAttachment(IAttachment a)
	{
		
	}
}
