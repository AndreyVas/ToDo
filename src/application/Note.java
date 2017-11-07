package application;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

public class Note implements INotes
{
	protected INotes parent;
	protected String title;
	protected String body;
	protected String status;
	protected Calendar created;
	protected int id;
	protected boolean shown;
	protected boolean wasShown;
	protected boolean important;
	protected String type;
	protected int number;
	protected String shownContentType;
	
	protected Resize resizeObject;

	protected NotesContainer lincToCont;
	protected Stage showNote;
	protected HBox updateNoteCont;  					// container for button save note
	protected Label changeContent;						// text/attachments panels switcher
	protected ToolBar tb;
	
	protected Map<String, IAttachment> attachments;
	
	INotes self;
	
	public Note()
	{
		
	}
	
	public NotesContainer getContainer()
	{
		return this.lincToCont;
	}
	
	public Note(Resize resizeObject, NotesContainer lincToCont, INotes parent)
	{
		this.parent = parent;
		this.self = this;
		this.resizeObject = resizeObject;
		this.lincToCont = lincToCont;
		
		this.id = this.hashCode();
		this.status = INotes.ACTIVE;
		this.created = Calendar.getInstance();
		this.important = false;
		
		showNote = new Stage();
		showNote.initOwner(lincToCont.getPrimaryStage());
		showNote.initStyle(StageStyle.TRANSPARENT);
		tb = new ToolBar();
		
		attachments = new HashMap<String, IAttachment>();
		
	}

	public Note(String title, String body, Resize resizeObject, NotesContainer lincToCont, INotes parent)
	{
		this.parent = parent;
		this.self = this;
		this.title = title;
		this.body = body;
		this.type = INotes.NOTE;
		
		this.id = this.hashCode();
		this.status = INotes.ACTIVE;
		this.created = Calendar.getInstance();
		this.shown = false;
		this.resizeObject = resizeObject;
		this.lincToCont = lincToCont;
		this.important = false;
		
		this.shownContentType = INotes.SHOW_TEXT;
	
		showNote = new Stage();
		showNote.initOwner(lincToCont.getPrimaryStage());
		showNote.initStyle(StageStyle.TRANSPARENT);
		tb = new ToolBar();
		
		attachments = new HashMap<String, IAttachment>();
	}
	
	public Note(String title, String body, Boolean important, String status, Integer number, Calendar created, Resize resizeObject, NotesContainer lincToCont, INotes parent)
	{
		this.parent = parent;
		this.self = this;
		this.title = title;
		this.body = body;
		this.type = INotes.NOTE;
		this.status = status;
		this.created = created;
		this.id = this.hashCode();
		this.shown = false;
		this.resizeObject = resizeObject;
		this.lincToCont = lincToCont;
		this.important = important;
		
		this.shownContentType = INotes.SHOW_TEXT;
		
		if(number != null)
		{
			this.number = number;
		}
		else
		{
			if(parent == null)
			{
				this.number = lincToCont.getItemsCount(this.type);
				
			}
			else
				this.number = parent.getChildrens().size();
		}
			
		showNote = new Stage();
		showNote.initOwner(lincToCont.getPrimaryStage());
		showNote.initStyle(StageStyle.TRANSPARENT);
		tb = new ToolBar();
		
		attachments = new HashMap<String, IAttachment>();
	}
	
	public void addAttachment(IAttachment a)
	{
		this.attachments.put(a.getID(), a);
		
		if(this.shown)
		{
			BorderPane rootPane = (BorderPane)this.showNote.getScene().rootProperty().get();
			
			shownContentType = INotes.SHOW_ATTACHMENTS;
			rootPane.setCenter(showAttachmentContent());
		}
		
		lincToCont.update(self);
	}
	
	public void addAttachments(Map<String, IAttachment> attachments)
	{
		this.attachments = attachments;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public boolean isImportant()
	{
		return this.important;
	}
	
	public void show(Settings settings)
	{
		if(!this.shown)
		{
			if(showNote.getScene() == null)
			{
				INotes currentNote = this;
				
				updateNoteCont = new HBox();					// container for update note button
				HBox tbButtonsRight = new HBox();				// container for buttons : close, add link, add file
				HBox resizedCont = new HBox();					// container for bottom resize icon
				HBox changeContentCont = new HBox();			// container for switch content type label
				
				this.shown = true;

				BorderPane pane = new BorderPane();
				pane.setOpacity(settings.getOpacityActive());
				
		    	Scene scene = new Scene(pane, 300,300, Color.TRANSPARENT);
		    	
		    	scene.getStylesheets().add("application/note.css");
		    	scene.getStylesheets().add("application/application.css");

		    	//------------------------------------------
		    	
		    	// close button for note
		    	ImageView ci = new ImageView(Resources.getResource(Resources.IMG_CLOSE));
				Button closeB = new Button();
				closeB.setGraphic(ci);
				closeB.getStyleClass().add("buttons");
				
				closeB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
				{
						shown = false;
						showNote.close();
				});
				
				//------------
				
				// add link to web page to note
				ImageView al = new ImageView(Resources.getResource(Resources.IMG_ADD_LINK));
				Button addLinkB = new Button();
				addLinkB.setGraphic(al);
				addLinkB.getStyleClass().add("buttons");
				
				addLinkB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
				{
						AttachedLink.createItemWindow(lincToCont.getPrimaryStage(), settings, resizeObject, currentNote);
				});
				
				//------------
				
				// load file to note
				ImageView af = new ImageView(Resources.getResource(Resources.IMG_ADD_FILE));
				Button addFileB = new Button();
				addFileB.setGraphic(af);
				addFileB.getStyleClass().add("buttons");
				
				addFileB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
					AttachedFile.createItemWindow(lincToCont.getPrimaryStage(), settings, resizeObject, currentNote));
				
				//------------

				updateNoteCont.getStyleClass().add("buttonsLeftCont");
				updateNoteCont.setVisible(false);
				

				tbButtonsRight.getStyleClass().add("buttonsRightCont");
				tbButtonsRight.setPickOnBounds(true);

				tbButtonsRight.getChildren().addAll(addLinkB, addFileB, closeB);
				
				StackPane tbButtonsCon = new StackPane();
				HBox.setHgrow(tbButtonsCon, Priority.ALWAYS);
				
				// 
				tbButtonsCon.addEventHandler(MouseEvent.MOUSE_MOVED, (e) ->
				{
						if(e.getX() > tbButtonsCon.getWidth()/2)
						{
							tbButtonsRight.setMouseTransparent(false);
						}
						else
						{
							tbButtonsRight.setMouseTransparent(true);
						}
				});
				
				tbButtonsCon.getChildren().addAll(updateNoteCont, tbButtonsRight);
			
				tb.getItems().add(tbButtonsCon);
				tb.getStyleClass().add("buttonsMainCont");

		    	//------------------------------------------
				
				/*
				 * container for bottom controls : 
				 * 		switch content - text\attachments
				 *		resize window - for windows. In Ubuntu work without this control
				 */
				
				StackPane buttomCont = new StackPane();
				
				//-------------
		    	
				// resize window control item
				resizedCont.setAlignment(Pos.CENTER_RIGHT);
				ImageView resizeImg = new ImageView(Resources.getResource(Resources.IMG_RESIZE));
				Label resized = new Label();
				resized.setGraphic(resizeImg);
				resizedCont.getChildren().add(resized);
				
				//-------------
				
				// switching between text and attachments content
				changeContentCont.setAlignment(Pos.CENTER_LEFT);

				changeContent = new Label();
				changeContent.getStyleClass().add("buttons");
				
				
				changeContent.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
				{
						if(shownContentType.equals(INotes.SHOW_TEXT))
						{
							shownContentType = INotes.SHOW_ATTACHMENTS;
							pane.setCenter(showAttachmentContent());
						}		
						else
						{
							shownContentType = INotes.SHOW_TEXT;
							pane.setCenter(showTextContent());
						}
				});
				
				changeContentCont.getChildren().add(changeContent);
				
				//-------------
				
				// if mouse in right side of the container set change content switcher to active status
				buttomCont.addEventHandler(MouseEvent.MOUSE_MOVED, (e) ->
				{
						if(e.getX() > buttomCont.getWidth()/2)
						{
							resizedCont.setMouseTransparent(false);
						}
						else
						{
							resizedCont.setMouseTransparent(true);
						}
				});
				
				buttomCont.getChildren().addAll(changeContentCont, resizedCont);
				
		    	//------------------------------------------
		    	
		    	pane.setTop(tb);
		    	
		    	if(this.shownContentType.equals(INotes.SHOW_TEXT))
		    	{
		    		pane.setCenter(showTextContent());
		    	}
		    	else
		    	{
		    		pane.setCenter(showAttachmentContent());
		    	}
		    	
		    	pane.setBottom(buttomCont);

		    	showNote.setScene(scene);
		    	showNote.getIcons().add(new Image(Resources.getResource(Resources.IMG_NOTE_ICON)));
		    	showNote.show();
		
		    	resizeObject.setResized(this.showNote, tb, resized);
			}
			else
			{
				showNote.show();
			}
		}
		else
		{
			showNote.toFront();
		}
	}
		
	private void saveItem(String title, String body)
	{
		updateNoteCont.setVisible(false);
		
		this.title = title;
		this.body = body;

		lincToCont.update(self);
	}
	
	private VBox showTextContent()
	{
		VBox infoCont = new VBox();
		
		TextField showTitle = new TextField();
		TextArea showBody = new TextArea();
		
		// create title area
		
    	showTitle.setText(this.title);
    	showTitle.getStyleClass().add("noteTitle");

    	showTitle.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				updateNoteCont.setVisible(true);
			}
		});
    	
    	showTitle.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
    		if(e.isControlDown())
    		{
    			if(e.getCode().equals(KeyCode.S) || e.getText().toLowerCase().equals("ы"))
    				saveItem(showTitle.getText(), showBody.getText());
    		}
    	});

    	// create area with main text content
    	
    	showBody.setText(this.body);
    	showBody.getStyleClass().add("noteText");
    	showBody.setWrapText(true);
    	
    	showBody.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				updateNoteCont.setVisible(true);
			}
		});
    	
    	showBody.addEventHandler(KeyEvent.KEY_PRESSED, (e) -> {
    		if(e.isControlDown())
    		{
    			if(e.getCode().equals(KeyCode.S) || e.getText().toLowerCase().equals("ы"))
    				saveItem(showTitle.getText(), showBody.getText());
    		}
    	});

    	infoCont.getChildren().addAll(showTitle, showBody);
    	VBox.setVgrow(showBody, Priority.ALWAYS);
    	
    	//--------------------------
    	
    	ImageView coi = new ImageView(Resources.getResource(Resources.IMG_CONFIRMATION));
		Button confirmationB = new Button();
		confirmationB.setGraphic(coi);
		confirmationB.getStyleClass().add("buttons");
		
		confirmationB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
		{
				saveItem(showTitle.getText(), showBody.getText());
		});
		
		updateNoteCont.getChildren().clear();
		updateNoteCont.getChildren().add(confirmationB);
		
		//--------------------------
		
		changeContent.setGraphic(new ImageView(Resources.getResource(Resources.IMG_TO_ATTACHMENTS)));
		
		return infoCont;
	}
	
	private VBox showAttachmentContent()
	{
		VBox infoCont = new VBox();
		
		if(this.attachments.size() > 0)
		{
			// show attachments

			for (Map.Entry<String, IAttachment> entry: attachments.entrySet())
			{
				VBox guiItem = entry.getValue().getGUIItem();
				
				guiItem.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> entry.getValue().open());
				
				infoCont.getChildren().add(guiItem);
			}
			    
		}
		else
		{
			Label attachmentsNotAddes = new Label("Attachments not added...");
			attachmentsNotAddes.getStyleClass().add("attachmentsNotAdded");
			infoCont.getChildren().add(attachmentsNotAddes);
		}
		
		changeContent.setGraphic(new ImageView(Resources.getResource(Resources.IMG_TO_TEXT)));
		
		return infoCont;
	}
	
	public void setParent(INotes n)
	{
		this.parent = n;
	}
	
	public VBox getTabItem(Settings settings)
	{
		Label text = new Label(this.title);
		
		if(isImportant())
		{
			ImageView importantImg = new ImageView(Resources.getResource(Resources.IMG_IMPORTANT));
			text.setGraphic(importantImg);
		}
		
		StackPane cont = new StackPane(text);
		//cont.setId(String.valueOf(this.id));
		
		text.getStyleClass().add("tabPaneItemText");
		cont.getStyleClass().add("tabPaneItem");

		//-----------------------------------
		
		HBox controls = addControls(this, settings);
		
		controls.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> text.setOpacity(0.4));
		controls.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> text.setOpacity(1));
		
		cont.getChildren().add(controls);
		
		//-----------------------------------
		
		VBox ret = new VBox(cont);
		ret.setId(String.valueOf(this.id));
		
		return ret;
	}

	public static INotes createItem(NodeList noteItems, Resize resizeObject, NotesContainer lincToCont, INotes parent)
	{
		if(noteItems != null)
		{
			String title = "";
			String body = "";
			String status = "";
			Calendar created = Calendar.getInstance();
			double x = NotePosition.DEFAULT_X;
			double y = NotePosition.DEFAULT_Y;
			double width = NotePosition.DEFAULT_WIDHT;
			double height = NotePosition.DEFAULT_HEIGHT;
			Boolean importatn = false;
			Integer number = null;
			Map<String, IAttachment> attachments = new HashMap<String, IAttachment>();;
			
			for(int i = 0; i < noteItems.getLength(); i++)
		    {
				switch(noteItems.item(i).getNodeName())
				{
					case INotes.CREATED:
						created.setTimeInMillis(Long.valueOf(noteItems.item(i).getTextContent()));
						break;
						
					case INotes.TITLE:
						title = noteItems.item(i).getTextContent();
						break;
						
					case INotes.TEXT:
						body = noteItems.item(i).getTextContent();
						break;
						
					case INotes.STATUS:
						status = noteItems.item(i).getTextContent();
						break;
						
					case INotes.EXPIRE:
						break;
						
					case INotes.X:
						x = Double.valueOf(noteItems.item(i).getTextContent());
						break;
						
					case INotes.Y:
						y = Double.valueOf(noteItems.item(i).getTextContent());
						break;
						
					case INotes.WIDHT:
						width = Double.valueOf(noteItems.item(i).getTextContent());
						break;
						
					case INotes.HEIGHT:
						height = Double.valueOf(noteItems.item(i).getTextContent());
						break;
						
					case INotes.IMPORTATN:
						importatn = Boolean.valueOf(noteItems.item(i).getTextContent());
						break;
						
					case INotes.NUMBER:
						number = Integer.valueOf(noteItems.item(i).getTextContent());
						break;
						
					case INotes.ATTACHMENTS:
						
						NodeList attachmetsList = noteItems.item(i).getChildNodes();
						
						for(int j = 0; j < attachmetsList.getLength(); j++)
						{
							switch(attachmetsList.item(j).getNodeName())
							{
								case IAttachment.LINK:
									
									IAttachment link = AttachedLink.createItem(attachmetsList.item(j).getChildNodes());
									attachments.put(link.getID(), link);
									
									break;
									
								case IAttachment.FILE:
									IAttachment file = AttachedFile.createItem(attachmetsList.item(j).getChildNodes());
									attachments.put(file.getID(), file);
									break;
							}
						}
						
						break;
				}
		    }
			
			Note ret = new Note(title, body, importatn, status, number, created, resizeObject, lincToCont, parent);
			ret.setSizeAndPosition(x,  y, width, height);
			ret.addAttachments(attachments);
			
			return ret; 
		}
		else
		{
			return null;
		}
	}
	
	public static void createItemWindow(Settings settings, NotesContainer notes, Resize resizeObject, INotes parent)
	{
		String titleInvite = "Enter a note name...";
		String textInvite = "Enter a note text...";
		
		Stage stage = new Stage();
		stage.initOwner(notes.getPrimaryStage());
		stage.initStyle(StageStyle.TRANSPARENT);
		
		BorderPane pane = new BorderPane();
		pane.setOpacity(settings.getOpacityActive());
		
    	Scene scene = new Scene(pane, 300,300, Color.TRANSPARENT);
    	scene.getStylesheets().add("application/note.css");
    	scene.getStylesheets().add("application/application.css");
    	
    	HBox tbButtonsLeft = new HBox();
		HBox tbButtonsRight = new HBox();
    	
    	//---------------------------------------------------------
 	
    	VBox content = new VBox();
  
    	TextField titleE = new TextField(titleInvite);
    	titleE.getStyleClass().add("noteTitle");
    	
    	titleE.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if(newValue)
				{
					if(titleE.getText().equals(titleInvite))
						titleE.setText("");
				}
				else
				{
					if(titleE.getText().equals(""))
						titleE.setText(titleInvite);
				}
			}
		});
    	
    	TextArea bodyE = new TextArea(textInvite);
    	bodyE.getStyleClass().add("noteText");
    	bodyE.setWrapText(true);
    	
    	bodyE.focusedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if(newValue)
					{
						if(bodyE.getText().equals(textInvite))
							bodyE.setText("");
					}
					else
					{
						if(bodyE.getText().equals(""))
							bodyE.setText(textInvite);
					}
				}
			});
   
    	content.getChildren().addAll(titleE, bodyE);
    	VBox.setVgrow(bodyE, Priority.ALWAYS);
    	
    	//---------------------------------------------------------
    	
    	ImageView ci = new ImageView(Resources.getResource(Resources.IMG_CLOSE));
		Button closeB = new Button();
		closeB.setGraphic(ci);
		closeB.getStyleClass().add("buttons");
		
		closeB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> stage.close());
		
		ImageView coi = new ImageView(Resources.getResource(Resources.IMG_CONFIRMATION));
		Button confirmationB = new Button();
		confirmationB.setGraphic(coi);
		confirmationB.getStyleClass().add("buttons");
		
		confirmationB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
		{
			if(titleE.getText().equals(titleInvite) || bodyE.getText().equals(textInvite))
			{
				System.out.println("not ready");
			}
			else
			{
				INotes n = new Note(titleE.getText(), bodyE.getText(), false,  INotes.ACTIVE, null, Calendar.getInstance(), resizeObject, notes, parent);
				notes.addNew(n);
				
				stage.close();
			}
		});

		tbButtonsLeft.getStyleClass().add("buttonsLeftCont");
		tbButtonsLeft.getChildren().add(confirmationB);

		tbButtonsRight.getStyleClass().add("buttonsRightCont");

		tbButtonsRight.getChildren().add(closeB);
		
		StackPane tbButtonsCon = new StackPane();
		HBox.setHgrow(tbButtonsCon, Priority.ALWAYS);
		
		tbButtonsCon.addEventHandler(MouseEvent.MOUSE_MOVED, (e) ->
		{
			if(e.getX() > tbButtonsCon.getWidth()/2)
			{
				tbButtonsRight.setMouseTransparent(false);
			}
			else
			{
				tbButtonsRight.setMouseTransparent(true);
			}
		});
		
		tbButtonsCon.getChildren().addAll(tbButtonsLeft, tbButtonsRight);
		
		ToolBar tb = new ToolBar();
		tb.getStyleClass().add("topMenu");
		tb.getItems().addAll(tbButtonsCon);
		tb.setBackground(Background.EMPTY);
	
		//------------------------------------------
    	
		HBox resizedCont = new HBox();
		resizedCont.setAlignment(Pos.CENTER_RIGHT);
		ImageView resizeImg = new ImageView(Resources.getResource(Resources.IMG_RESIZE));
		Label resized = new Label();
		resized.setGraphic(resizeImg);
		resizedCont.getChildren().add(resized);
		
    	//------------------------------------------
    	
    	pane.setTop(tb);
    	pane.setCenter(content);
    	pane.setBottom(resizedCont);
        
    	stage.setScene(scene);
    	stage.getIcons().add(new Image(Resources.getResource(Resources.IMG_NOTE_ICON)));
    	stage.show();
    	
    	resizeObject.setResized(stage, tb, resized);
	}
	
	public String getID()
	{
		return String.valueOf(this.id);
	}

	@Override
	public Element getXML(Document doc) 
	{
		Element item = doc.createElement(INotes.ITEM); 
		
		Element type = doc.createElement(INotes.TYPE); 
		type.setTextContent(INotes.NOTE);

		Element content = doc.createElement(INotes.CONTENT); 
		
		Element title = doc.createElement(INotes.TITLE); 
		title.setTextContent(this.title);
		Element text = doc.createElement(INotes.TEXT); 
		text.setTextContent(this.body);
		Element status = doc.createElement(INotes.STATUS); 
		status.setTextContent(this.getStatus());
		Element created = doc.createElement(INotes.CREATED); 
		created.setTextContent(Long.toString(Calendar.getInstance().getTimeInMillis()));
		Element expire = doc.createElement(INotes.EXPIRE); 
		expire.setTextContent("N/A");
		Element x = doc.createElement(INotes.X); 
		x.setTextContent(String.valueOf(this.showNote.getX()));
		Element y = doc.createElement(INotes.Y); 
		y.setTextContent(String.valueOf(this.showNote.getY()));
		Element width = doc.createElement(INotes.WIDHT); 
		width.setTextContent(String.valueOf(this.showNote.getWidth()));
		Element height = doc.createElement(INotes.HEIGHT); 
		height.setTextContent(String.valueOf(this.showNote.getHeight()));
		Element important = doc.createElement(INotes.IMPORTATN);
		important.setTextContent(String.valueOf(this.important));
		Element number = doc.createElement(INotes.NUMBER);
		number.setTextContent(String.valueOf(this.number));
		
		Element attachments = doc.createElement(INotes.ATTACHMENTS);
		
		for (Map.Entry<String, IAttachment> entry: this.attachments.entrySet())
		{
			attachments.appendChild(entry.getValue().getXML(doc));
		}
		
		content.appendChild(status);
		content.appendChild(created);
		content.appendChild(expire);
		content.appendChild(title);
		content.appendChild(text);
		content.appendChild(x);
		content.appendChild(y);
		content.appendChild(width);
		content.appendChild(height);
		content.appendChild(important);
		content.appendChild(number);
		content.appendChild(attachments);
		
		item.appendChild(type);
		item.appendChild(content);
		
		return item;
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
	public boolean isShown() 
	{
		return this.shown;
	}

	@Override
	public void setShown(Boolean b) {}
	
	public NotePosition getPosition()
	{
		return new NotePosition(showNote.getX(), showNote.getY(), showNote.getWidth(), showNote.getHeight());
	}
	
	public void hide(){}

	public Stage getStage() 
	{
		return showNote;
	}

	@Override
	public ToolBar getMovedArea() 
	{
		return tb;
	};

	public INotes getParent()
	{
		return this.parent;
	}
	
	protected void setSizeAndPosition(double x, double y, double widht, double height)
	{
		showNote.setX(x);
		showNote.setY(y);
		showNote.setWidth(widht);
		showNote.setHeight(height);
	}

	public void updateSubNotes(String type, TreeItem<VBox> rootTree, INotes note, Settings settings){}
	
	public TreeItem<VBox> getSubTasksTree()
	{
		return null;
	}
	
	public void closeActiveWindow()
	{
		if(this.showNote != null)
			this.showNote.close();
	}
	
	public void setStatus(String status)
	{
		this.status = status;
		this.getContainer().update(this);
	}
	
	public String getStatus()
	{
		return this.status;
	}
	
	public HBox addControls(INotes note, Settings settings)
	{
		HBox controls = new HBox();
		controls.getStyleClass().add("tabPaneItemButtonsCont");
		
		//-------------------------------------------
		
		Button importantBut = new Button();
		importantBut.getStyleClass().add("tabPaneItemButtons");
		
		ImageView importantImgYes;
		ImageView importantImgNo;
		
		if(note.isImportant())
		{
			importantImgYes = new ImageView(Resources.getResource(Resources.IMG_IMPORTANT_NO));
			importantBut.setGraphic(importantImgYes);
		}
		else
		{
			importantImgNo = new ImageView(Resources.getResource(Resources.IMG_IMPORTANT_YES));
			importantBut.setGraphic(importantImgNo);
		}
		
		importantBut.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> 
		{
			if(note.isImportant())
			{
				ImageView impImgNo;
				impImgNo = new ImageView(Resources.getResource(Resources.IMG_IMPORTANT_YES));
				importantBut.setGraphic(impImgNo);
				note.setImportant(false);
			}
			else
			{
				ImageView impImgYes;
				impImgYes = new ImageView(Resources.getResource(Resources.IMG_IMPORTANT_NO));
				importantBut.setGraphic(impImgYes);
				note.setImportant(true);
			}
			
			note.getContainer().update(note);
		});
		
		importantBut.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> importantBut.setOpacity(0.4));
		importantBut.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> importantBut.setOpacity(1));
		
		//-------------------------------------------
		
		ImageView delImg = new ImageView(Resources.getResource(Resources.IMG_TRASH));
		Button delete = new Button();
		delete.setGraphic(delImg);
		delete.setTooltip(new Tooltip(NoteManager.DELETE));
		delete.getStyleClass().add("tabPaneItemButtons");
		
		delete.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> note.getContainer().delete(note));
		delete.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> delete.setOpacity(0.4));
		delete.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> delete.setOpacity(1));
		
		//-------------------------------------------
		
		controls.getChildren().addAll(importantBut, delete);
		
		return controls;
	}
	
	public void setImportant(Boolean b)
	{
		this.important = b;
	}
	
	public LinkedList<INotes> getChildrens()
	{
		return null;
	}
	
	public void addChildren(INotes n, Integer position)
	{
		
	}
	
	public void killChild(INotes child) {}
	
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
}
