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
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Sticker extends Note implements INotes
{
	Sticker(String body, String status, Calendar created, Resize resizeObject, NotesContainer lincToCont)
	{
		super(resizeObject, lincToCont);

		this.body = body;
		this.type = INotes.STICKER;
		this.status = status;
		this.shown = true;
		this.wasShown = false;

		showNote.setX(NotePosition.DEFAULT_X);
		showNote.setY(NotePosition.DEFAULT_Y);
		showNote.setWidth(NotePosition.DEFAULT_WIDHT);
		showNote.setHeight(NotePosition.DEFAULT_HEIGHT);
	}
	
	Sticker(String body, String status, Boolean important, Calendar created, double x, double y, double widht, double height, boolean shown, Resize resizeObject, NotesContainer lincToCont)
	{
		super(resizeObject, lincToCont);
		
		this.body = body;
		this.type = INotes.STICKER;
		this.status = status;
		this.shown = shown;
		this.wasShown = false;
		this.important = important;

		showNote.setX(x);
		showNote.setY(y);
		showNote.setWidth(widht);
		showNote.setHeight(height);
	}

	public static INotes createItem(NodeList noteItems, Resize resizeObject, NotesContainer lincToCont)
	{
		if(noteItems != null)
		{
			String body = "";
			String status = "";
			Calendar created = Calendar.getInstance();
			double x = NotePosition.DEFAULT_X;
			double y = NotePosition.DEFAULT_Y;
			double width = NotePosition.DEFAULT_WIDHT;
			double height = NotePosition.DEFAULT_HEIGHT;
			boolean shown = true;
			boolean important = false;
			
			for(int i = 0; i < noteItems.getLength(); i++)
		    {
				switch(noteItems.item(i).getNodeName())
				{
					case INotes.CREATED:
						created.setTimeInMillis(Long.valueOf(noteItems.item(i).getTextContent()));
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
						
					case INotes.SHOWN:
						shown = Boolean.valueOf(noteItems.item(i).getTextContent());
						break;
						
					case INotes.IMPORTATN:
						important = Boolean.valueOf(noteItems.item(i).getTextContent());
						break;
				}
		    }
			
			return new Sticker(body, status, important, created, x, y, width, height, shown, resizeObject, lincToCont);
		}
		else
		{
			return null;
		}
	}
	
	public static void createItemWindow(Settings settings, NotesContainer notes, Resize resizeObject)
	{
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
    	
    	String textInvite = "Enter a sticker text...";
    	
    	TextArea text = new TextArea(textInvite);
    	text.getStyleClass().add("noteText");
    	text.setWrapText(true);
    	
    	text.focusedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if(newValue)
					{
						if(text.getText().equals(textInvite))
							text.setText("");
					}
					else
					{
						if(text.getText().equals(""))
							text.setText(textInvite);
					}
				}
			});
    	
    	pane.setCenter(text);
    	VBox.setVgrow(text, Priority.ALWAYS);
    	
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
				INotes n = new Sticker(text.getText(), INotes.ACTIVE, Calendar.getInstance(), resizeObject, notes);
				notes.addNew(n);
				n.show(settings);
	
				stage.close();
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

		tb.getItems().addAll(tbButtonsCon);
		tb.setBackground(Background.EMPTY);

		//---------------------------------------------------------
		// set sticker position
		
		NotePosition defaultPosition = new NotePosition();
		
		for(INotes in : notes.getNoteList())
		{
			if(in.getType().equals(INotes.STICKER))
			{
				if(in.getPosition().isIntersects(defaultPosition))
				{
					if(defaultPosition.getX() + defaultPosition.getWidth() + in.getPosition().getWidth()/2 < Screen.getPrimary().getVisualBounds().getWidth()
							&& defaultPosition.getY() + defaultPosition.getHeight() + in.getPosition().getHeight()/2 < Screen.getPrimary().getVisualBounds().getHeight())
					{
						
						defaultPosition.setPosition(in.getPosition().getX() + in.getPosition().getWidth()/2, 
							in.getPosition().getY() + in.getPosition().getHeight()/2);
					}
				}
			}
		}
		
		//------------------------------------------
    	
		HBox resizedCont = new HBox();
		resizedCont.setAlignment(Pos.CENTER_RIGHT);
		ImageView resizeImg = new ImageView("resize.png");
		Label resized = new Label();
		resized.setGraphic(resizeImg);
		resizedCont.getChildren().add(resized);
		
    	//------------------------------------------
		
    	pane.setTop(tb);
    	pane.setBottom(resizedCont);
    	
    	stage.setScene(scene);
    	stage.getIcons().add(new Image("stickerIcon.png"));
    	stage.show();
    	
    	resizeObject.setResized(stage, tb, resized);
	}
	
	public void show(Settings settings)
	{
		if(wasShown)
		{
			showNote.show();
			showNote.toFront();
		}
		else if(!showNote.isShowing())
		{
			if(showNote.getScene() == null)
			{
				shown = true;
				
				HBox tbButtonsLeft = new HBox();
				HBox tbButtonsRight = new HBox();

				//---------------------------------------------------
				
				BorderPane pane = new BorderPane();
				pane.setOpacity(settings.getOpacityActive());
				
		    	Scene scene = new Scene(pane, showNote.getWidth(), showNote.getHeight(), Color.TRANSPARENT);
		    	scene.getStylesheets().add("application/note.css");
		    	scene.getStylesheets().add("application/application.css");

		    	//---------------------------------------------------------

		    	TextArea showBody = new TextArea();
		    	
		    	showBody.setText(this.body);
		    	showBody.getStyleClass().add("stickerText");
		    	showBody.setWrapText(true);
		    	
		    	showBody.textProperty().addListener(new ChangeListener<String>()
    			{
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
					{
						tbButtonsLeft.setVisible(true);
					}
    			});
		    	
		    	VBox infoCont = new VBox();
		    	infoCont.getChildren().addAll(showBody);
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
				
				//---------------------------------------------------------
		    	
		    	pane.setCenter(infoCont);
		    	pane.setTop(tb);
		    	pane.setBottom(resizedCont);
		    	
		    	showNote.setScene(scene);
		    	showNote.getIcons().add(new Image("stickerIcon.png"));
		    	showNote.show();
		    	
		    	resizeObject.setResized(this.showNote, tb, resized);
		    	
		    	this.wasShown = true;
			}
			else
			{
				showNote.show();
				shown = true;
			}
		}
		else
		{
			showNote.toFront();
		}
		
	}
	
	public void hide()
	{
		showNote.close();
	}
	
	public StackPane getTabItem(Settings settings)
	{
		Label text = new Label(this.body);
		
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
	
	public Element getXML(Document doc)
	{
		Element item = doc.createElement("item"); 
		
		Element type = doc.createElement("type"); 
		type.setTextContent(INotes.STICKER);

		Element content = doc.createElement("content"); 
		
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
		Element shown = doc.createElement(INotes.SHOWN); 
		shown.setTextContent(String.valueOf(this.shown));
		Element important = doc.createElement(INotes.IMPORTATN);
		important.setTextContent(String.valueOf(this.important));
		
		content.appendChild(status);
		content.appendChild(created);
		content.appendChild(expire);
		content.appendChild(text);
		content.appendChild(x);
		content.appendChild(y);
		content.appendChild(width);
		content.appendChild(height);
		content.appendChild(shown);
		content.appendChild(important);
		
		item.appendChild(type);
		item.appendChild(content);

		return item;
	}

	public boolean isShown()
	{
		return this.shown;
	}
	
	public void setShown(Boolean b)
	{
		this.shown = b;
	}
	
	public HBox addControls(INotes note, Settings settings)
	{
		HBox controls = new HBox();
		controls.getStyleClass().add("tabPaneItemButtonsCont");
		
		//-------------------------------------------
		
		Button show = new Button();
		
		ImageView showImgYes;
		ImageView showImgNo;
		
		if(note.isShown())
		{
			showImgYes = new ImageView("showYes.png");
			show.setGraphic(showImgYes);
		}
		else
		{
			showImgNo = new ImageView("showNo.png");
			show.setGraphic(showImgNo);
		}
		
		show.getStyleClass().add("tabPaneItemButtons");
		
		show.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				if(note.isShown())
				{
					ImageView showImgNo;
					showImgNo = new ImageView("showNo.png");
					show.setGraphic(showImgNo);
					
					note.setShown(false);
					note.hide();
				}
				else
				{
					ImageView showImgYes;
					showImgYes = new ImageView("showYes.png");
					show.setGraphic(showImgYes);
					
					note.show(settings);
					note.setShown(true);
				}
				
				note.getContainer().save();
			}
		});
		
		show.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> show.setOpacity(0.4));
		show.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> show.setOpacity(1));
		
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
		
		delete.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				note.getContainer().delete(note.getID());
			}
		});
		
		delete.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> delete.setOpacity(0.4));
		delete.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> delete.setOpacity(1));
		
		//-------------------------------------------
		
		controls.getChildren().addAll(importantBut, show, delete);
		
		return controls;
	}
}
