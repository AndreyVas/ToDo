package application;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Reminder extends Note implements INotes, IReminds
{
	Calendar remindDate;
	protected int remindCount;
	
	protected LocalDateTime expire;

	Reminder(Resize resizeObject, NotesContainer lincToCont)
	{
		super(resizeObject, lincToCont);
		
		this.remindCount = IReminds.NO_REMIND;
		this.title = "";
	}

	Reminder(String body, String status, Boolean important, LocalDateTime expire, Resize resizeObject, NotesContainer lincToCont, int remindCount)
	{
		super(resizeObject, lincToCont);
		
		this.type = INotes.REMINDER;
		this.body = body;
		this.status = status;
		this.expire = expire;
		this.important = important;
		this.title = "";
		this.remindCount = remindCount;
	}

	protected static HBox createDatePicker(DatePicker date, ComboBox<String> hours, ComboBox<String> minutes, LocalDateTime expire)
	{
		HBox fullDateCont = new HBox();
		fullDateCont.getStyleClass().add("datePicker");
    	
    	VBox dateCont = new VBox();
    	
    	Label lDate = new Label ("Date");   	

    	dateCont.getChildren().addAll(lDate, date);
    	
    	VBox hoursCont = new VBox();
    	
    	Label lHours = new Label("Hours");
 
    	hours.getItems().addAll(
    		    "00",
    		    "01",
    		    "02",
    		    "03",
    		    "04",
    		    "05",
    		    "06",
    		    "07",
    		    "08",
    		    "09",
    		    "10",
    		    "11",
    		    "12",
    		    "13",
    		    "14",
    		    "15",
    		    "16",
    		    "17",
    		    "18",
    		    "19",
    		    "20",
    		    "21",
    		    "22",
    		    "23"
    		);
    	
    	
    	
    	hoursCont.getChildren().addAll(lHours, hours);
    	
    	VBox minCont = new VBox();
    	
    	Label lMinutes = new Label("Minutes");

    	minutes.getItems().addAll(
    		    "00",
    		    "01",
    		    "02",
    		    "03",
    		    "04",
    		    "05",
    		    "06",
    		    "07",
    		    "08",
    		    "09",
    		    "10",
    		    "11",
    		    "12",
    		    "13",
    		    "14",
    		    "15",
    		    "16",
    		    "17",
    		    "18",
    		    "19",
    		    "20",
    		    "21",
    		    "22",
    		    "23",
    		    "24",
    		    "25",
    		    "26",
    		    "27",
    		    "28",
    		    "29",
    		    "30",
    		    "31",
    		    "32",
    		    "33",
    		    "34",
    		    "35",
    		    "36",
    		    "37",
    		    "38",
    		    "39",
    		    "40",
    		    "41",
    		    "42",
    		    "43",
    		    "44",
    		    "45",
    		    "46",
    		    "47",
    		    "48",
    		    "49",
    		    "50",
    		    "51",
    		    "52",
    		    "53",
    		    "54",
    		    "55",
    		    "56",
    		    "57",
    		    "58",
    		    "59"
    		);

    	if(expire != null)
    	{
    		minutes.setValue(expire.getMinute() >= 10 ? String.valueOf(expire.getMinute()) :  "0" + String.valueOf(expire.getMinute()));
    		hours.setValue(expire.getHour() >= 10 ? String.valueOf(expire.getHour()) :  "0" + String.valueOf(expire.getHour()));
    		date.setValue(expire.toLocalDate());
    	}
    	else
    	{
    		minutes.setValue("00");
    		hours.setValue("00");
    		date.setValue(LocalDate.now());
    	}
    	
    	minCont.getChildren().addAll(lMinutes, minutes);
    	
    	fullDateCont.getChildren().addAll(dateCont, hoursCont, minCont);
    	
    	return fullDateCont;
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
    	
    	String textInvite = "Enter a reminder text...";
    	
    	HBox tbButtonsLeft = new HBox();
		HBox tbButtonsRight = new HBox();
    	
    	//---------------------------------------------------------
    	
    	VBox content = new VBox();
    	
    	DatePicker date = new DatePicker();
    	ComboBox hours = new ComboBox();
    	ComboBox minutes = new ComboBox();

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

    	content.getChildren().addAll(createDatePicker(date, hours, minutes, null), bodyE);
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
			
				LocalDateTime expireTime = date.getValue().atTime(Integer.valueOf(hours.getValue().toString()), Integer.valueOf(minutes.getValue().toString()));
				
				INotes n = new Reminder(bodyE.getText(), INotes.ACTIVE, false, expireTime, resizeObject, notes, IReminds.NO_REMIND);
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
		//tb.getStyleClass().add("topMenu");
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
    	stage.getIcons().add(new Image("reminderIcon.png"));
    	stage.show();
    	
    	resizeObject.setResized(stage, tb, resized);
	}

	public Element getXML(Document doc) 
	{
		Element item = doc.createElement("item"); 
		
		Element type = doc.createElement("type"); 
		type.setTextContent(INotes.REMINDER);

		Element content = doc.createElement("content"); 

		Element text = doc.createElement("text"); 
		text.setTextContent(this.body);
		Element status = doc.createElement("status"); 
		status.setTextContent(this.getStatus());
		Element expire = doc.createElement("expire"); 
		expire.setTextContent(this.expire.getYear() +":" + this.expire.getMonthValue() + ":" + this.expire.getDayOfMonth() + ":"
				+ this.expire.getHour() + ":" + this.expire.getMinute());
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
		Element remindCount = doc.createElement(IReminds.REMIND_COUNT);
		remindCount.setTextContent(String.valueOf(this.remindCount));
		
		content.appendChild(status);
		content.appendChild(expire);
		content.appendChild(text);
		content.appendChild(x);
		content.appendChild(y);
		content.appendChild(width);
		content.appendChild(height);
		content.appendChild(important);
		content.appendChild(remindCount);
		
		item.appendChild(type);
		item.appendChild(content);

		return item;
	}
	
	public static INotes createItem(NodeList noteItems, Resize resizeObject, NotesContainer notes)
	{
		if(noteItems != null)
		{
			String body = "";
			String status = "";
			LocalDateTime expire = null;
			double x = NotePosition.DEFAULT_X;
			double y = NotePosition.DEFAULT_Y;
			double width = NotePosition.DEFAULT_WIDHT;
			double height = NotePosition.DEFAULT_HEIGHT;
			Boolean important = false;
			int remindCount = IReminds.NO_REMIND;
			
			for(int i = 0; i < noteItems.getLength(); i++)
		    {
				switch(noteItems.item(i).getNodeName())
				{
					case INotes.TEXT:
						body = noteItems.item(i).getTextContent();
						break;
						
					case INotes.STATUS:
						status = noteItems.item(i).getTextContent();
						break;
						
					case INotes.EXPIRE:
						expire = parseExpireString(noteItems.item(i).getTextContent());
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
						important = Boolean.valueOf(noteItems.item(i).getTextContent());
						break;
						
					case IReminds.REMIND_COUNT:
						remindCount = Integer.valueOf(noteItems.item(i).getTextContent());
						break;
				}
		    }
			
			Reminder ret = new Reminder(body, status, important,  expire, resizeObject, notes, remindCount);
			ret.setSizeAndPosition(x, y, width, height);
	
			return ret; 
		}
		else
		{
			return null;
		}

	}
	
	protected static LocalDateTime parseExpireString(String expireString)
	{
		int year = Integer.valueOf(expireString.substring(0, expireString.indexOf(":")));
		expireString = expireString.substring(expireString.indexOf(":") + 1, expireString.length());
		
		int month = Integer.valueOf(expireString.substring(0, expireString.indexOf(":")));
		expireString = expireString.substring(expireString.indexOf(":") + 1, expireString.length());
		
		int day = Integer.valueOf(expireString.substring(0, expireString.indexOf(":")));
		expireString = expireString.substring(expireString.indexOf(":") + 1, expireString.length());
		
		int hour = Integer.valueOf(expireString.substring(0, expireString.indexOf(":")));
		expireString = expireString.substring(expireString.indexOf(":") + 1, expireString.length());
		
		int min = Integer.valueOf(expireString);
		expireString = expireString.substring(expireString.indexOf(":") + 1, expireString.length());
		
		LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, min);
		
		return ldt;
	}

	public StackPane getTabItem(Settings settings)
	{
		//Label text = new Label((this.body.substring(0, this.body.length() > 20 ? 20 : this.body.length()) + " ...").replace("\n", " "));
		
		Label text = new Label(this.body);
		
		if(isImportant())
		{
			ImageView importantImg = new ImageView("important.png");
			text.setGraphic(importantImg);
		}
	
		
		Pane p = new Pane();
		
		VBox infoCont = new VBox();
		infoCont.getChildren().addAll(text);
		
		HBox reminde = new HBox();

		ImageView remindeImg = new ImageView("reminde.png");
		remindeImg.setFitWidth(12);
		remindeImg.setFitHeight(12);
		
		Label remaindeLabel = new Label(this.expire.getDayOfMonth() + "/" + this.expire.getMonthValue() + "/" + this.expire.getYear() + " " 
				+ ((this.expire.getHour() >= 10) ? String.valueOf(this.expire.getHour()) : ("0"  + String.valueOf(this.expire.getHour()))) 
				+ ":" + ((this.expire.getMinute() >= 10) ? String.valueOf(this.expire.getMinute()) : ("0"  + String.valueOf(this.expire.getMinute()))));
		
		reminde.getChildren().addAll(remindeImg, remaindeLabel);
	

		StackPane cont = new StackPane();
		cont.getChildren().addAll(p, text, reminde);
		cont.setId(String.valueOf(this.id));
		
		text.getStyleClass().add("tabPaneItemText");
		cont.getStyleClass().add("tabPaneItem");
		reminde.getStyleClass().add("reminder");
		
		//-----------------------------------
	
		HBox controls = addControls(this, settings);
		
		controls.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				text.setOpacity(0.4);
				reminde.setOpacity(0.4);
			}
		});
		
		controls.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				text.setOpacity(1);
				reminde.setOpacity(1);
			}
		});
		
		cont.getChildren().add(controls);
		
		//-----------------------------------

		return cont;
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
		    	// add main info 
				
				VBox contentCont = new VBox();
	
		    	TextArea bodyE = new TextArea(this.body);
		    	bodyE.getStyleClass().add("noteText");
		    	bodyE.setWrapText(true);
		    	
		    	bodyE.textProperty().addListener(new ChangeListener<String>()
    			{
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
					{
						tbButtonsLeft.setVisible(true);
					}
    		
    			});

		    	HBox expireDate = new HBox();
		    	expireDate.getStyleClass().add("datePicker");
		    
		    	DatePicker date = new DatePicker();
		    	ComboBox<String> hours = new ComboBox<String>();
		    	ComboBox<String> minutes = new ComboBox<String>();
		    	
		    	hours.valueProperty().addListener(new ChangeListener<String>() 
		    	{
		            @Override public void changed(ObservableValue<? extends String> ov, String t, String t1) 
		            {
		            	tbButtonsLeft.setVisible(true);
		            }    
		        });
		    	
		    	minutes.valueProperty().addListener(new ChangeListener<String>() 
		    	{
		            @Override public void changed(ObservableValue<? extends String> ov, String t, String t1) 
		            {
		            	tbButtonsLeft.setVisible(true);
		            }    
		        });
		    	
		    	date.valueProperty().addListener(new ChangeListener<Object>()
		    	{
					@Override
					public void changed(ObservableValue<?> arg0, Object arg1, Object arg2)
					{
						tbButtonsLeft.setVisible(true);
					}
		    	});

		    	
		    	expireDate.getChildren().addAll(createDatePicker(date, hours, minutes, this.expire));
		
		    	contentCont.getChildren().addAll(bodyE, expireDate);
		    	VBox.setVgrow(bodyE, Priority.ALWAYS);
		   
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

						body = bodyE.getText();
						
						LocalDateTime ldt = date.getValue().atTime(Integer.valueOf(hours.getValue().toString()), Integer.valueOf(minutes.getValue().toString()));

						if(!ldt.equals(expire))
						{
							remindCount = IReminds.NO_REMIND;
							expire = ldt;
						}

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
				//tbButtonsCon.prefWidthProperty().bind(tb.widthProperty().add(-20));   // !!!!!!!!! need correct !
				
				//------------------------------------------
		    	
				HBox resizedCont = new HBox();
				resizedCont.setAlignment(Pos.CENTER_RIGHT);
				ImageView resizeImg = new ImageView("resize.png");
				Label resized = new Label();
				resized.setGraphic(resizeImg);
				resizedCont.getChildren().add(resized);
				
		    	//------------------------------------------
		    	
		    	pane.setTop(tb);
		    	tb.getStyleClass().add("buttonsMainCont");
		    	pane.setCenter(contentCont);
		    	pane.setBottom(resizedCont);
		    	
		    	showNote.setScene(scene);
		    	showNote.getIcons().add(new Image("reminderIcon.png"));
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

	public LocalDateTime getExpire()
	{
		return this.expire;
	}
	
	public boolean isRemind()
	{
		return true;
	}

	public int getRemindCount()
	{
		return this.remindCount;
	}
	
	public void setRemindCount(int remindCount)
	{
		this.remindCount = remindCount;
	}

	@Override
	public StackPane getRemindItem(Settings settings) 
	{
		Label text;
		
		if(this.title.equals(""))
			text = new Label(this.body);
			//text = new Label((this.body.substring(0, this.body.length() > 20 ? 20 : this.body.length()) + " ...").replace("\n", " "));
		else
			//text = new Label((this.title.substring(0, this.title.length() > 20 ? 20 : this.title.length()) + " ...").replace("\n", " "));
			text = new Label(this.body);
		
		if(isImportant())
		{
			ImageView importantImg = new ImageView("important.png");
			text.setGraphic(importantImg);
		}

		VBox infoCont = new VBox();
		infoCont.getChildren().addAll(text);
		
		HBox reminde = new HBox();

		ImageView remindeImg = new ImageView("reminde.png");
		remindeImg.setFitWidth(12);
		remindeImg.setFitHeight(12);
		
		Label remaindeLabel = new Label(this.expire.getDayOfMonth() + "/" + this.expire.getMonthValue() + "/" + this.expire.getYear() + " " 
				+ ((this.expire.getHour() >= 10) ? String.valueOf(this.expire.getHour()) : ("0"  + String.valueOf(this.expire.getHour()))) 
						+ ":" + ((this.expire.getMinute() >= 10) ? String.valueOf(this.expire.getMinute()) : ("0"  + String.valueOf(this.expire.getMinute()))));
	
		reminde.getChildren().addAll(remindeImg, remaindeLabel);
	
		//------------------------------------------------------
		
		HBox remindCountIMG = new HBox();
		remindCountIMG.getStyleClass().add("remidCount");
		
		ImageView rcIMG = new ImageView();
		
		switch(this.getRemindCount())
		{
			case IReminds.FIRST_REMIND:
				rcIMG = new ImageView("firstRemind.png");
				break;
				
			case IReminds.SECOND_REMIND:
				rcIMG = new ImageView("secondRemind.png");
				break;
				
			case IReminds.THIRD_REMIND:
				rcIMG = new ImageView("thirdRemind.png");
				break;
		}
		
		Label rcLabel = new Label();
		rcLabel.setGraphic(rcIMG);
		
		remindCountIMG.getChildren().add(rcLabel);
		
		//------------------------------------------------------

		StackPane cont = new StackPane();
		cont.getChildren().addAll(/*p,*/ text, reminde, remindCountIMG);
		cont.setId(String.valueOf(this.id));
		
		text.getStyleClass().add("tabPaneItemText");
		cont.getStyleClass().add("tabPaneItem");
		reminde.getStyleClass().add("reminder");
		
		//----------------------------------------------------------------
		
		cont.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				text.setOpacity(0.4);
			}
		});
		
		cont.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) 
			{
				text.setOpacity(1);
			}
		});
		
		//----------------------------------------------------------------

		return cont;
	}
	
	public HBox addControls(INotes note, Settings settings)
	{
		HBox controls = new HBox();
		controls.getStyleClass().add("tabPaneItemButtonsCont");
		
		//-------------------------------------------
		
		ImageView delImg = new ImageView("trash.png");
		Button delete = new Button();
		delete.setGraphic(delImg);
		delete.setTooltip(new Tooltip(NoteManager.DELETE));
		delete.getStyleClass().add("tabPaneItemButtons");
	
		delete.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> note.getContainer().delete(note.getID()));
		delete.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> delete.setOpacity(0.4));
		delete.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> delete.setOpacity(1));
		
		//----------------------------------------------

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
		
		controls.getChildren().addAll(importantBut, delete);
		
		return controls;
	}

	@Override
	public int getRemindCoutn() 
	{
		return this.remindCount;
	}
}
