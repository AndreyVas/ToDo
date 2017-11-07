package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.jws.soap.SOAPBinding.Style;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NoteManager implements Observer
{
	TabPane tp;
	private NotesContainer noteListLink;
	private File saveFile;
	private Settings settings;
	private Resize resize;
	private Remind remind;

	public final String NOTES = "Notes";
	public final String STICKERS = "Stickers";
	public final String REMINDERS = "Reminders";
	public final String TASKS = "Tasks";
	
	public static final String DONE = "Done";
	public static final String CANSEL = "Cansel";
	public static final String DELETE = "Delete";
	public static final String ADD_SUB = "Add subtask";
	public static final String ADD = "Add";
	public static final String RESTORE = "Restore";
	public static final String ADD_FOLDER = "Add folder";
	public static final String ADD_NOTE = "Add note";
	public static final String COPY_LINK = "Copy linc address";
	public static final String SAVE_FILE = "Save file";
	
	public static final String MOVE_BEFORE = "before";
	public static final String MOVE_AFTER = "after";
	public static final String MOVE_IN = "in";
	
	

	public VBox notesCont;
	public VBox stickersCont;
	public VBox remindersCont;
	public VBox tasksCont;
	
	private Label noteIcon;
	private Button addFolderButton;
	
	private Stage remindWindow;
	private Stage primaryStage;
	
	//----------------------------------------------------------------
	// Variables for drag items in tab
	
	javafx.scene.Node currentNode = null;
	javafx.scene.Node previousNode = null;
	
	javafx.scene.Node pickedNode = null;
	javafx.scene.Node toNode = null;
	
	private String moveTo;
	private boolean dragged = false;
	
	//----------------------------------------------------------------
	
	public Stage getPrimaryStage()
	{
		return this.primaryStage;
	}
	
	public NoteManager(NotesContainer noteListLink, Settings settings, Remind remind, Stage primaryStage)
	{
		this.noteIcon = new Label();
	
		this.primaryStage = primaryStage;
		this.settings = settings;
		this.resize = new Resize(this);
		this.remind = remind;
		
		tp = new TabPane();
		tp.setBackground(Background.EMPTY);
	
		tp.getStyleClass().add("tabPane");
		
		this.noteListLink = noteListLink;

		saveFile = new File(settings.saveString() + File.separator + Settings.NOTES);
		
		if(!saveFile.exists())
		{
			new File(settings.saveString()).mkdirs();
		}

		notesCont = new VBox();
		stickersCont = new VBox();
		remindersCont = new VBox();
		tasksCont = new VBox();
	}
	
	public TabPane show()
	{
		tp.getTabs().add(createTab(this.NOTES, notesCont));
		tp.getTabs().add(createTab(this.STICKERS, stickersCont));
		tp.getTabs().add(createTab(this.TASKS, tasksCont));
		tp.getTabs().add(createTab(this.REMINDERS, remindersCont));
		
		tp.getSelectionModel().selectedItemProperty().addListener(
		    new ChangeListener<Tab>() {
		        @Override
		        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) 
		        {
		            switch(t1.getId())
		            {
		            	case INotes.NOTE:
		            		noteIcon.setGraphic(new ImageView(Resources.getResource(Resources.IMG_NOTE_ICON)));
		            		addFolderButton.setVisible(true);
		            		break;
		            		
		            	case INotes.STICKER:
		            		noteIcon.setGraphic(new ImageView(Resources.getResource(Resources.IMG_STICKER_ICON)));
		            		addFolderButton.setVisible(false);
		            		break;
		            		
		            	case INotes.REMINDER:
		            		noteIcon.setGraphic(new ImageView(Resources.getResource(Resources.IMG_REMINDER_ICON)));
		            		addFolderButton.setVisible(false);
		            		break;
		            		
		            	case INotes.TASK:
		            		noteIcon.setGraphic(new ImageView(Resources.getResource(Resources.IMG_TASK_ICON)));
		            		addFolderButton.setVisible(false);
		            		break;
		            }
		        }
		    }
		);

		return tp;
	}
	
	public Label createTabIcon()
	{
		ImageView img = new ImageView(Resources.getResource(Resources.IMG_NOTE_ICON));
		this.noteIcon.setGraphic(img);
		this.noteIcon.getStyleClass().add("noteIcon");
		
		return this.noteIcon;
	}
	
	public Button createAddFolderButton()
	{
		ImageView afi = new ImageView(Resources.getResource(Resources.IMG_ADD_FOLDER));
		this.addFolderButton = new Button();
		this.addFolderButton.setGraphic(afi);
		this.addFolderButton.getStyleClass().add("buttons");

		this.addFolderButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> addFolder());
		this.addFolderButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> this.addFolderButton.setOpacity(0.4));
		this.addFolderButton.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> this.addFolderButton.setOpacity(1));
		
		return this.addFolderButton;
	}
	
	public INotes getNote(javafx.scene.Node node)
	{
		INotes note;
		
		if(node != null)
		{
			note = noteListLink.findNote(node.getId(), null);
			
			if(note != null)
			{
				return note;
			}
		}
		
		return null;
	}
	
	javafx.scene.Node getPickNode(MouseEvent event, VBox pickedArea)
	{
		javafx.scene.Node node = event.getPickResult().getIntersectedNode();
		

		if(node != null && !node.equals(pickedArea))
		{
			// get node under the mouse
			while(node != null && node.getId() == null)
			{
				node = node.getParent();
			}
		}
		
		return node;
	}
	
	public Tab createTab(String name, VBox content)
	{
		content.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				pickedNode = getPickNode(event, content);
				dragged = false;
			}
		});
		
		content.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				javafx.scene.Node node = event.getPickResult().getIntersectedNode();

				if(node != null && !node.equals(content) && content.isHover())
				{
					dragged = true;

					// get node under the mouse
					while(node != null && node.getId() == null)
					{
						node = node.getParent();
					}
					
					//INotes note = noteListLink.findNote(node.getId(), null);
		
					if(node != null && !node.equals(currentNode))
					{
						previousNode = currentNode;
						currentNode = node;
						
						if(previousNode != null && previousNode.getStyleClass().size() != 0)
							previousNode.getStyleClass().remove(previousNode.getStyleClass().size() - 1);
					}
					
					if(currentNode.getStyleClass().size() != 0)
						currentNode.getStyleClass().remove(currentNode.getStyleClass().size() -1);
					
					INotes n = noteListLink.findNote(node.getId(), null);
					
					if(event.getPickResult().getIntersectedPoint().getY() < node.getBoundsInLocal().getMaxY()/4)
					{
						if(!n.getType().equals(INotes.FOLDER) || (n.getType().equals(INotes.FOLDER) && !n.isShown()))
						{
							currentNode.getStyleClass().add("tabPaneItemInsertOnTop");	
							moveTo = MOVE_BEFORE;
						}
					}
					else if(event.getPickResult().getIntersectedPoint().getY() > (node.getBoundsInLocal().getMaxY() * 3/4))
					{
						if(!n.getType().equals(INotes.FOLDER) || (n.getType().equals(INotes.FOLDER) && !n.isShown()))
						{
							currentNode.getStyleClass().add("tabPaneItemInsertOnBottom");
							moveTo = MOVE_AFTER;
						}
					}
					else
					{			
						if(n.getType().equals(INotes.FOLDER) && !n.isShown())
						{
							currentNode.getStyleClass().add("tabPaneItemInsertOnCenter");
							moveTo = MOVE_IN;
						}
						else
						{
							if(event.getPickResult().getIntersectedPoint().getY() <= node.getBoundsInLocal().getMaxY()/2)
							{
								currentNode.getStyleClass().add("tabPaneItemInsertOnTop");
								moveTo = MOVE_BEFORE;
							}
							else
							{
								currentNode.getStyleClass().add("tabPaneItemInsertOnBottom");
								moveTo = MOVE_AFTER;
							}
						}
					}	
				}
				else
				{
					
				}
						
				event.consume();
			}
		});
		
		content.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				toNode = getPickNode(event, content);	
				
				if(currentNode != null && currentNode.getStyleClass().size() != 0)
					currentNode.getStyleClass().remove(currentNode.getStyleClass().size() - 1);
				
				previousNode = null;
				currentNode = null;
				
				if(dragged && content.isHover() && !toNode.equals(content))
				{
					//----------------------------------------------------
					
					if(pickedNode != null && toNode != null && pickedNode != toNode)
					{
						VBox parentFrom = (VBox)pickedNode.getParent();
							
						VBox parentTo = (VBox)toNode.getParent();
						
						int fromIndex = parentFrom.getChildren().indexOf(pickedNode);
						int toIndex = parentTo.getChildren().indexOf(toNode);
						
						parentFrom.getChildren().remove(pickedNode);
						
						switch(moveTo)
						{
							case MOVE_IN:
								
								//((VBox) toNode).getChildren().add(pickedNode);
								
								break;
								
							case MOVE_AFTER:
								
								if(parentTo.equals(parentFrom))
									parentTo.getChildren().add((fromIndex > toIndex) ? toIndex + 1 : toIndex, pickedNode);
								else
									parentTo.getChildren().add(toIndex + 1, pickedNode);
		
								break;
								
							case MOVE_BEFORE:
								
								if(parentTo.equals(parentFrom))
									parentTo.getChildren().add((toIndex > fromIndex) ? 
											((toIndex - 1 > 0) ? toIndex - 1 : 0) : toIndex, pickedNode);
								else
									parentTo.getChildren().add(toIndex, pickedNode);
								
								break;
						}	
						
						noteListLink.moveNote(pickedNode.getId(), toNode.getId(), moveTo);
					}
					//----------------------------------------------------
				}

			}
		});
		
		content.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> dragged = false);

		Tab tab = new Tab(name);
		
		ScrollPane sp = new ScrollPane();
		sp.setContent(content);
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.getStyleClass().add("tabScroll");
		
		tab.setContent(sp);
		
		switch(name)
		{
			case NOTES:
				tab.setId(INotes.NOTE);
				break;
				
			case STICKERS:
				tab.setId(INotes.STICKER);
				break;
				
			case TASKS:
				tab.setId(INotes.TASK);
				break;
				
			case REMINDERS:
				tab.setId(INotes.REMINDER);
				break;
		}
		
		tab.getStyleClass().add("tab");
		tab.setClosable(false);

		return tab;
	}
	
	@Override
	public void update(Observable list, Object action) 
	{
		NotesContainer n = (NotesContainer) list;
		
		switch(action.toString())
		{
			case INotes.ADD:
				
				if(n.getChanged().getParent() == null)
				{
					addItemToTab(n.getChanged());
				}
				else
				{
					INotes notesUpate = n.getChanged().getParent();
					
					while(notesUpate != null)
					{
						notesUpate.updateSubNotes(INotes.ADD, notesUpate.getSubTasksTree(), n.getChanged(), settings);
						
						notesUpate = notesUpate.getParent();
					}
				}
				
				save();
				
				if(n.getChanged().getType().equals(INotes.REMINDER) || n.getChanged().getType().equals(INotes.TASK))
					this.remind.resume();
				
				break;
				
			case INotes.REMOVE:
				
				if(n.getChanged().getParent() == null)
				{
					removeItemFromTab(n.getChanged());
				}
				
				n.getChanged().closeActiveWindow();
				
				save();
				
				if(n.getChanged().getType().equals(INotes.REMINDER) || n.getChanged().getType().equals(INotes.TASK))
					this.remind.resume();
				break;
				
			case INotes.LOAD:
				
				if(n.getChanged().getParent() == null)
				{
					addItemToTab(n.getChanged());
				}
				
				break;
				
			case INotes.UPDATE:
				
				INotes notesUpate = n.getChanged();
				
				if(notesUpate.getParent() == null)
				{
					updateItemInTab(n.getChanged());
				}
			
				while(notesUpate != null)
				{
					notesUpate.updateSubNotes(INotes.UPDATE, notesUpate.getSubTasksTree(), n.getChanged(), settings);
					
					notesUpate = notesUpate.getParent();
				}
				
				save();
				
				if(n.getChanged().getType().equals(INotes.REMINDER) || n.getChanged().getType().equals(INotes.TASK))
					this.remind.resume();
				
				break;
				
			case INotes.SAVE:
				save();
				break;
				
			case INotes.SHOW_REMIND:
				
				if(n.getToRemind().size() > 0)
				{
					showRemindWindow(n.getToRemind());
					save();
				}
				
				break;
		}
	}
	
	public void updateRemindCoutn(IReminds ir)
	{
		switch(ir.getRemindCoutn())
		{
			case IReminds.NO_REMIND:
				ir.setRemindCount(IReminds.FIRST_REMIND);
			break;
			
			case IReminds.FIRST_REMIND:
				ir.setRemindCount(IReminds.SECOND_REMIND);
				break;
				
			case IReminds.SECOND_REMIND:
				ir.setRemindCount(IReminds.THIRD_REMIND);
				break;
		}
	}
	
	public void showRemindWindow(LinkedList<IReminds> remindObjects)
	{
		if(remindWindow != null && remindWindow.isShowing())
		{
			remindWindow.close();
		}
		
		remindWindow = new Stage();
		remindWindow.initStyle(StageStyle.TRANSPARENT);
		
		BorderPane pane = new BorderPane();
    	pane.setOpacity(settings.getOpacityActive());
    	
    	Scene scene = new Scene(pane, settings.getWidthRemindWindow(), settings.getHeightRemindWindow(), Color.TRANSPARENT);
    	
    	scene.getStylesheets().add("application/application.css");
    	scene.getStylesheets().add("application/note.css");
    	scene.getStylesheets().add("application/noteManager.css");
    	
    	//---------------------------------------------------------------
    	
    	VBox content = new VBox();
    	
    	for(IReminds in : remindObjects)
    	{
    		updateRemindCoutn(in);
    		StackPane item = in.getRemindItem(settings);

    		item.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> 
    		{
				INotes n = (INotes)in;
				n.show(settings);
    		});
    		
    		content.getChildren().add(item);	
    	}
    	
    	//---------------------------------------------------------------
    	
    	ToolBar tb = new ToolBar();

    	Label remindTitle = new Label("Reminders");
    	remindTitle.getStyleClass().add("showRemindWindowLabel");
    	remindTitle.setGraphic(new ImageView(Resources.getResource(Resources.IMG_REMINDER_ICON)));

    	ImageView ci = new ImageView(Resources.getResource(Resources.IMG_CLOSE));
		Button closeB = new Button();
		closeB.setGraphic(ci);
		closeB.getStyleClass().add("buttons");
		
		closeB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
		{
			remindWindow.close();
			remind.resume();
		});
		
		HBox tbButtonsLeft = new HBox();
		tbButtonsLeft.getStyleClass().add("buttonsLeftCont");
		tbButtonsLeft.getChildren().add(remindTitle);

		HBox tbButtonsRight = new HBox();
		tbButtonsRight.getStyleClass().add("buttonsRightCont");
		tbButtonsRight.getChildren().add(closeB);
		
		StackPane tbButtonsCon = new StackPane();
		HBox.setHgrow(tbButtonsCon, Priority.ALWAYS);

		tbButtonsCon.getChildren().addAll(tbButtonsLeft, tbButtonsRight);
	
		tb.getItems().add(tbButtonsCon);
		tb.getStyleClass().add("buttonsMainCont");

		//---------------------------------------------------------------
		
		HBox resizedCont = new HBox();
		resizedCont.setAlignment(Pos.CENTER_RIGHT);
		ImageView resizeImg = new ImageView(Resources.getResource(Resources.IMG_RESIZE));
		Label resized = new Label();
		resized.setGraphic(resizeImg);
		resizedCont.getChildren().add(resized);
		
    	//---------------------------------------------------------------
    	
    	pane.setTop(tb);
    	pane.setCenter(content);
    	pane.setBottom(resizedCont);
        
    	//---------------------------------------------------------------
    	
    	remindWindow.setScene(scene);
    	remindWindow.getIcons().add(new Image(Resources.getResource(Resources.IMG_REMINDER_ICON)));
    	remindWindow.show();

    	Media sound = new Media(getClass().getResource(Resources.getResource(Resources.AUD_ALARM)).toString());
    	MediaPlayer mediaPlayer = new MediaPlayer(sound);
    	mediaPlayer.play();

    	this.resize.setResized(remindWindow, tb, resized);
	}

	public void save()
	{
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    Document doc = db.newDocument();
		    
		    //--------------------------------------------------------------------
		    
		    Element root = doc.createElement("items"); 

		    for(INotes in : noteListLink.getNoteList())
		    {
		    	root.appendChild(in.getXML(doc));
		    }
		    
		    doc.appendChild(root);
		    
		    //--------------------------------------------------------------------
		    
		    DOMSource source = new DOMSource(doc); 
		    StreamResult result;

			result = new StreamResult(new FileOutputStream(saveFile));
			
			TransformerFactory transFactory = TransformerFactory.newInstance(); 
		    Transformer transformer = transFactory.newTransformer(); 
		    transformer.transform(source, result); 
		    
		    result.getOutputStream().close();
		}
		catch (ParserConfigurationException e) 
	    {
			Messages.showError("NoteManager class, save function \\n" + e.getMessage());
		} 
	    catch (FileNotFoundException e) 
		{
	    	Messages.showError("NoteManager class, save function \\n" + e.getMessage());
		}
	    catch (TransformerException e) 
	    {
	    	Messages.showError("NoteManager class, save function \\n" + e.getMessage());
		} 
	    catch(IOException e)
	    {
	    	Messages.showError("NoteManager class, save function \\n" + e.getMessage());
	    }
		catch(Exception e)
		{
			Messages.showError("NoteManager class, save function \\n" + e.getMessage());
		}
	}
	
	public void load()
	{
		if(saveFile != null)
		{
			try 
		    {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
			    
			    DocumentBuilder db = dbf.newDocumentBuilder(); 
			    Document doc = db.parse(saveFile);

			    DOMSource source = new DOMSource(doc); 

			    DOMResult result = new DOMResult();
				
				TransformerFactory transFactory = TransformerFactory.newInstance(); 
			    Transformer transformer = transFactory.newTransformer(); 

			    transformer.transform(source, result); 
			    Node node = result.getNode().getFirstChild();
	
			    NodeList items = node.getChildNodes();

			    for(int i = 0; i < items.getLength(); i++)
			    {
			    	if(items.item(i).getNodeName().equals("item"))
			    	{
			    		NodeList noteItems = items.item(i).getChildNodes();
			    		
			    		NodeList content = null;
			    		String type = "";
			    		
			    		for(int j = 0; j < noteItems.getLength(); j++)
			    		{
			    			switch(noteItems.item(j).getNodeName())
			    			{
				    			case INotes.TYPE:
				    				type = noteItems.item(j).getTextContent();
				    				
				    				break;
				    				
				    			case INotes.CONTENT:
				    				content = noteItems.item(j).getChildNodes();
				    				break;
			    			}
			    		}

			    		INotes tmpNote = null;
			    		
			    		switch(type)
			    		{
			    			case INotes.NOTE:
			    				tmpNote = Note.createItem(content, resize, noteListLink, null);
			    				
			    				break;
			    				
			    			case INotes.FOLDER:
			    				tmpNote = Folder.createItem(content, noteListLink, resize, null);
			    				break;
			    				
			    			case INotes.TASK:
			    				tmpNote = Tasks.createItem(content, resize, noteListLink, null);
			    				break;
			    				
			    			case INotes.REMINDER:
			    				tmpNote = Reminder.createItem(content, resize, noteListLink);
			    				break;
			    				
			    			case INotes.STICKER:
			    				
			    				tmpNote = Sticker.createItem(content, resize, noteListLink);
			    				
			    				if(tmpNote.isShown())
			    				{
			    					tmpNote.show(settings);
			    				}

			    				break;
			    		}

			    		noteListLink.add(tmpNote);
			    	}	
			    }
			    
			    this.remind.resume();
		    }
			catch (ParserConfigurationException e) 
		    {
				System.out.println("Error " + e);
			} 
		    catch (FileNotFoundException e) 
			{
		    	System.out.println("Error : A file with saved jobs was not found, perhaps this is the first run");
			}
		    catch (TransformerException e) 
		    {
		    	System.out.println("Error " + e);
			}
			catch(IOException e)
			{
				System.out.println("Error " + e);
			}
			catch(Exception e)
			{
				System.out.println("Error " + e);
				
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("no path");
		}
	}
		
	public void removeItemFromTab(INotes item)
	{
		String id = item.getID();
		String type = (item.getType() == INotes.FOLDER) ? INotes.NOTE : item.getType();
		
		for(Tab t : tp.getTabs())
		{
			if(t.getId() == type)
			{
				VBox vb = (VBox)((ScrollPane)t.getContent()).getContent();

				Iterator<javafx.scene.Node> iter = vb.getChildren().iterator();
				
				while(iter.hasNext())
				{
					javafx.scene.Node tmpN = iter.next();
					
					if(tmpN.getId().equals(id))
					{
						iter.remove();
					}
				}
			}
		}
	}
	
	public void addItemToTab(INotes item)
	{
		VBox p = item.getTabItem(settings);

		p.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> 
		{
			if(!dragged)
				item.show(settings);
		});
		
		VBox cont = null;
		
		switch(item.getType())
		{
			case INotes.NOTE:
				//this.notesCont.getChildren().add(p);
				cont = this.notesCont;
				break;
				
			case INotes.STICKER:
				//this.stickersCont.getChildren().add(p);
				cont = this.stickersCont;
				break;
				
			case INotes.TASK:
				//this.tasksCont.getChildren().add(p);
				cont = this.tasksCont;
				break;
				
			case INotes.REMINDER:
				cont = this.remindersCont;
				//this.remindersCont.getChildren().add(p);
				break;
				
			case INotes.FOLDER:
				cont = this.notesCont;
				//this.notesCont.getChildren().add(p);
				
				break;
		}
		
		if(cont != null)
		{
			int i;
			
			for(i = 0; i < cont.getChildren().size(); i++)
			{
				if(item.getNumber() < getNote(cont.getChildren().get(i)).getNumber())
					break;
			}
			
			cont.getChildren().add(i, p);
		}
	}

	public void updateItemInTab(INotes item)
	{
		VBox cont = new VBox();

		// find required container
		switch(item.getType())
		{
			case INotes.NOTE:
				cont = this.notesCont;
				break;
				
			case INotes.STICKER:
				cont = this.stickersCont;
				break;
				
			case INotes.REMINDER:
				cont = this.remindersCont;
				break;
				
			case INotes.TASK:
				cont = this.tasksCont;
				break;
				
			case INotes.FOLDER:
				cont = this.notesCont;
								
				break;
		}

		//  find modifiable item in pane
		for(int i = 0; i < cont.getChildren().size(); i++)
		{
			if(cont.getChildren().get(i).getId().equals(item.getID()))
			{
				VBox sp = (VBox)cont.getChildren().get(i);
				sp.getChildren().removeAll(sp.getChildren());
				
				VBox tmpItemCont = item.getTabItem(settings);

				sp.getChildren().addAll(tmpItemCont.getChildren());
			}
		}
	}
	
	public void addFolder()
	{
		Folder.createItemWindow(settings, noteListLink, resize, null);
	}
	
	public void showAddWindow()
	{
		switch(this.tp.getSelectionModel().getSelectedItem().getId())
		{
			case INotes.NOTE:
				Note.createItemWindow(settings, noteListLink, resize, null);
				break;
			
			case INotes.STICKER:
				Sticker.createItemWindow(settings, noteListLink, resize);
				break;
				
			case INotes.REMINDER:
				Reminder.createItemWindow(settings, noteListLink, resize);
				break;
				
			case INotes.TASK:
				Tasks.createItemWindow(settings, null, resize, noteListLink);
				break;
		}
	}
}
