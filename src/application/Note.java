package application;

import java.util.Calendar;
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
import javafx.scene.control.TextArea;
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

public class Note implements INotes
{
	protected String title;
	protected String body;
	protected String status;
	protected Calendar created;
	protected int id;
	protected boolean shown;
	protected boolean wasShown;
	protected boolean important;
	protected String type;
	
	protected Resize resizeObject;

	protected NotesContainer lincToCont;
	protected Stage showNote;
	protected ToolBar tb;
	
	INotes self;
	
	public Note()
	{
		
	}
	
	public NotesContainer getContainer()
	{
		return this.lincToCont;
	}
	
	public Note(Resize resizeObject, NotesContainer lincToCont)
	{
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
	}

	public Note(String title, String body, Resize resizeObject, NotesContainer lincToCont)
	{
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
		
		showNote = new Stage();
		showNote.initOwner(lincToCont.getPrimaryStage());
		showNote.initStyle(StageStyle.TRANSPARENT);
		tb = new ToolBar();
		
	}
	
	public Note(String title, String body, Boolean important, String status, Calendar created, Resize resizeObject, NotesContainer lincToCont)
	{
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
		
		showNote = new Stage();
		showNote.initOwner(lincToCont.getPrimaryStage());
		showNote.initStyle(StageStyle.TRANSPARENT);
		tb = new ToolBar();
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
				HBox tbButtonsLeft = new HBox();
				HBox tbButtonsRight = new HBox();
				this.shown = true;

				BorderPane pane = new BorderPane();
				pane.setOpacity(settings.getOpacityActive());
				
		    	Scene scene = new Scene(pane, 300,300, Color.TRANSPARENT);
		    	
		    	scene.getStylesheets().add("application/note.css");
		    	scene.getStylesheets().add("application/application.css");
		    	
		    	//------------------------------------------
		    	// set main content 
		    	
		    	VBox infoCont = new VBox();
		    	
		    	TextField showTitle = new TextField();
		    	showTitle.setText(this.title);
		    	showTitle.getStyleClass().add("noteTitle");
		    	
		    	showTitle.textProperty().addListener(new ChangeListener<String>()
    			{
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
					{
						tbButtonsLeft.setVisible(true);
					}
    		
    			});

		    	TextArea showBody = new TextArea();
		    	showBody.setText(this.body);
		    	showBody.getStyleClass().add("noteText");
		    	showBody.setWrapText(true);
		    	
		    	showBody.textProperty().addListener(new ChangeListener<String>()
    			{
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
					{
						tbButtonsLeft.setVisible(true);
					}
    		
    			});

		    	infoCont.getChildren().addAll(showTitle, showBody);
		    	VBox.setVgrow(showBody, Priority.ALWAYS);

		    	//------------------------------------------
		    	// add save and update buttons
	
		    	ImageView ci = new ImageView("close.png");
				Button closeB = new Button();
				closeB.setGraphic(ci);
				closeB.getStyleClass().add("buttons");
				
				closeB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent arg0) 
					{
						shown = false;
						showNote.close();
					}
				});
				
				ImageView coi = new ImageView("confirmation.png");
				Button confirmationB = new Button();
				confirmationB.setGraphic(coi);
				confirmationB.getStyleClass().add("buttons");
				
				confirmationB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent arg0) 
					{
						tbButtonsLeft.setVisible(false);
						
						title = showTitle.getText();
						body = showBody.getText();
		
						lincToCont.update(self);
					}
				});

				tbButtonsLeft.getStyleClass().add("buttonsLeftCont");
				tbButtonsLeft.setVisible(false);
				tbButtonsLeft.getChildren().add(confirmationB);

				tbButtonsRight.getStyleClass().add("buttonsRightCont");
				tbButtonsRight.setPickOnBounds(true);

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
			
				tb.getItems().add(tbButtonsCon);
				tb.getStyleClass().add("buttonsMainCont");

		    	//------------------------------------------
		    	
				HBox resizedCont = new HBox();
				resizedCont.setAlignment(Pos.CENTER_RIGHT);
				ImageView resizeImg = new ImageView("resize.png");
				Label resized = new Label();
				resized.setGraphic(resizeImg);
				resizedCont.getChildren().add(resized);
				
				//------------------------------------------
				
		    	pane.setTop(tb);
		    	pane.setCenter(infoCont);
		    	pane.setBottom(resizedCont);

		    	showNote.setScene(scene);
		    	showNote.getIcons().add(new Image("noteIcon.png"));
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
	
	public StackPane getTabItem(Settings settings)
	{
		Label text = new Label(this.title);
		
		if(isImportant())
		{
			ImageView importantImg = new ImageView("important.png");
			text.setGraphic(importantImg);
		}
		
		StackPane cont = new StackPane(text);
		cont.setId(String.valueOf(this.id));
		
		text.getStyleClass().add("tabPaneItemText");
		cont.getStyleClass().add("tabPaneItem");

		//-----------------------------------
		
		HBox controls = addControls(this, settings);
		
		controls.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				text.setOpacity(0.4);
			}
		});
		
		controls.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				text.setOpacity(1);
			}
		});
		
		cont.getChildren().add(controls);
		
		//-----------------------------------
		
		return cont;
	}

	public static INotes createItem(NodeList noteItems, Resize resizeObject, NotesContainer lincToCont)
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
				}
		    }
			
			Note ret = new Note(title, body, importatn, status, created, resizeObject, lincToCont);
			ret.setSizeAndPosition(x,  y, width, height);
			
			return ret; 
		}
		else
		{
			return null;
		}

	}
	
	public static void createItemWindow(Settings settings, NotesContainer notes, Resize resizeObject)
	{
		String titleInvite = "¬ведите название заметки...";
		String textInvite = "¬ведите текст заметки...";
		
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
    	
    	ImageView ci = new ImageView("close.png");
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
		
		ImageView coi = new ImageView("confirmation.png");
		Button confirmationB = new Button();
		confirmationB.setGraphic(coi);
		confirmationB.getStyleClass().add("buttons");
		
		confirmationB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				if(titleE.getText().equals(titleInvite) || bodyE.getText().equals(textInvite))
				{
					System.out.println("not ready");
				}
				else
				{
					INotes n = new Note(titleE.getText(), bodyE.getText(), false,  INotes.ACTIVE, Calendar.getInstance(), resizeObject, notes);
					notes.addNew(n);
					
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
	
		//------------------------------------------
    	
		HBox resizedCont = new HBox();
		resizedCont.setAlignment(Pos.CENTER_RIGHT);
		ImageView resizeImg = new ImageView("resize.png");
		Label resized = new Label();
		resized.setGraphic(resizeImg);
		resizedCont.getChildren().add(resized);
		
    	//------------------------------------------
    	
    	pane.setTop(tb);
    	pane.setCenter(content);
    	pane.setBottom(resizedCont);
        
    	stage.setScene(scene);
    	stage.getIcons().add(new Image("noteIcon.png"));
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
		Element item = doc.createElement("item"); 
		
		Element type = doc.createElement("type"); 
		type.setTextContent(INotes.NOTE);

		Element content = doc.createElement("content"); 
		
		Element title = doc.createElement("title"); 
		title.setTextContent(this.title);
		Element text = doc.createElement("text"); 
		text.setTextContent(this.body);
		Element status = doc.createElement("status"); 
		status.setTextContent(this.getStatus());
		Element created = doc.createElement("created"); 
		created.setTextContent(Long.toString(Calendar.getInstance().getTimeInMillis()));
		Element expire = doc.createElement("expire"); 
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
		
		item.appendChild(type);
		item.appendChild(content);
		
		return item;
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
		return null;
	}
	
	protected void setSizeAndPosition(double x, double y, double widht, double height)
	{
		showNote.setX(x);
		showNote.setY(y);
		showNote.setWidth(widht);
		showNote.setHeight(height);
	}

	public void updateSubNotes(String type, TreeItem<StackPane> rootTree, INotes note, Settings settings){}
	
	public TreeItem<StackPane> getSubTasksTree()
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
			importantImgYes = new ImageView("importantNo.png");
			importantBut.setGraphic(importantImgYes);
		}
		else
		{
			importantImgNo = new ImageView("importantYes.png");
			importantBut.setGraphic(importantImgNo);
		}
		
		importantBut.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				if(note.isImportant())
				{
					ImageView importantImgNo;
					importantImgNo = new ImageView("importantYes.png");
					importantBut.setGraphic(importantImgNo);
					note.setImportant(false);
				}
				else
				{
					ImageView importantImgYes;
					importantImgYes = new ImageView("importantNo.png");
					importantBut.setGraphic(importantImgYes);
					note.setImportant(true);
				}
				
				note.getContainer().update(note);
			}
		});
		
		importantBut.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> importantBut.setOpacity(0.4));
		importantBut.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> importantBut.setOpacity(1));
		
		//-------------------------------------------
		
		ImageView delImg = new ImageView("trash.png");
		Button delete = new Button();
		delete.setGraphic(delImg);
		delete.setTooltip(new Tooltip(NoteManager.DELETE));
		delete.getStyleClass().add("tabPaneItemButtons");
		
		delete.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> note.getContainer().delete(note.getID()));
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
}
