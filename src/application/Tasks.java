package application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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

public class Tasks extends Reminder implements INotes, IReminds
{
	private LinkedList<INotes> subTasks;
	private INotes parent;
	private boolean reminde;
	private TreeItem<VBox> subTuskTreeRoot;
	private Stage subTusksWindow;

	Tasks(Resize resizeObject, NotesContainer lincToCont)
	{
		super(resizeObject, lincToCont);
		
		this.type = INotes.TASK;
		subTasks = new LinkedList<INotes>();
		this.parent = null;
		this.expire = null;
		this.reminde = false;
		this.subTuskTreeRoot = null;
	}
	
	Tasks(INotes parent, Resize resizeObject, NotesContainer lincToCont)
	{
		super(resizeObject, lincToCont);
		
		this.type = INotes.TASK;
		subTasks = new LinkedList<INotes>();
		this.parent = parent;
		this.expire = null;
		this.reminde = false;
		this.subTuskTreeRoot = null;
	}
	
	public INotes getParent()
	{
		return this.parent;
	}
	
	public static INotes createItem(Resize resizeObject, NotesContainer lincToCont)
	{
		return new Tasks(resizeObject, lincToCont);
	}
	
	private LinkedList<INotes> getSubTusks()
	{
		return this.subTasks;
	}
	
	private void setInfo(String title, String body, String status)
	{
		this.title = title;
		this.body = body;
		this.status = status;
	}
	
	private void setExpireDate(boolean remind, LocalDateTime expire, int remindCount)
	{		
		if(expire != null)
			this.expire =  expire;

		this.reminde = remind;
		this.remindCount = remindCount;
		
	}
	
	private Tasks getMyself()
	{
		return this;
	}
	
	public static void createItemWindow(Settings settings, INotes parentNode, Resize resizeObject, NotesContainer lincToCont)
	{
		Stage stage = new Stage();
		stage.initOwner(lincToCont.getPrimaryStage());
		stage.initStyle(StageStyle.TRANSPARENT);
		
		BorderPane pane = new BorderPane();
		pane.setOpacity(settings.getOpacityActive());
		
    	Scene scene = new Scene(pane, 400,400, Color.TRANSPARENT);
    	scene.getStylesheets().add("application/note.css");
    	scene.getStylesheets().add("application/application.css");
    	
    	String titleInvite = "Enter a task name...";
		String textInvite = "Enter a task text...";
		String subTaskAdded = "Added subtasks - ";
		
		Tasks newTask = new Tasks(parentNode, resizeObject, lincToCont);
		
		HBox tbButtonsLeft = new HBox();
		HBox tbButtonsRight = new HBox();
    
    	//---------------------------------------------------------
    	// add main info 
		
		VBox contentCont = new VBox();
		
    	VBox infoCont = new VBox();
    	
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

    	HBox expireDate = new HBox();
    	expireDate.getStyleClass().add("datePicker");
    	
    	CheckBox enableExpire = new CheckBox();
    	DatePicker date = new DatePicker();
    	ComboBox<String> hours = new ComboBox<String>();
    	ComboBox<String> minutes = new ComboBox<String>();
    	
    	expireDate.getChildren().addAll(createDatePicker(enableExpire, date, hours, minutes, null));
    	
    	infoCont.getChildren().addAll(titleE, bodyE);
    	VBox.setVgrow(bodyE, Priority.ALWAYS);

    	contentCont.getChildren().addAll(infoCont, expireDate);
    	VBox.setVgrow(infoCont, Priority.ALWAYS);
    	
    	//---------------------------------------------------------
    	// add subtask 
    	
    	StackPane subItemsCont = new StackPane();
    	subItemsCont.getStyleClass().add("addSubTasks");
    	
    	HBox hb = new HBox();
    	Label subTasks = new Label(subTaskAdded + newTask.getSubTusks().size());
    	hb.getChildren().add(subTasks);

    	ImageView addST = new ImageView(Resources.getResource(Resources.IMG_ADD_TASK));
		Button addSubTask = new Button();
		addSubTask.setGraphic(addST);
		//addSubTask.getStyleClass().add("topMenuButtons");
		addSubTask.getStyleClass().add("buttons");
		
		subItemsCont.getChildren().addAll(hb, addSubTask);

		addSubTask.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
		{
			createItemWindow(settings, newTask, resizeObject, lincToCont);
			subTasks.setText(subTaskAdded + newTask.getSubTusks().size());
		});
    	
    	contentCont.getChildren().add(subItemsCont);
    	
    	//---------------------------------------------------------
    	// actions buttons : close and confirmation
    	
    	ImageView ci = new ImageView(Resources.getResource(Resources.IMG_CLOSE));
		Button closeB = new Button();
		closeB.setGraphic(ci);
		closeB.getStyleClass().add("buttons");
		
		closeB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> stage.close());
	
		ImageView coi = new ImageView(Resources.getResource(Resources.IMG_CONFIRMATION));
		Button confirmationB = new Button();
		confirmationB.setGraphic(coi);
		confirmationB.getStyleClass().add("topMenuButtons");
		confirmationB.getStyleClass().add("buttons");
		
		confirmationB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
		{
			if(titleE.getText().equals(titleInvite) || bodyE.getText().equals(textInvite))
			{
				System.out.println("not ready");
			}
			else
			{
				newTask.setInfo(titleE.getText(), bodyE.getText(), INotes.ACTIVE);
				
				if(enableExpire.isSelected())
				{
					newTask.setExpireDate(true, LocalDateTime.of(date.getValue(), LocalTime.of(Integer.valueOf(hours.getValue()), Integer.valueOf(minutes.getValue()))), IReminds.NO_REMIND);
				}
		
				if(parentNode != null)
				{
					Tasks parent = (Tasks)parentNode;
					parent.getSubTusks().add(newTask);
	
					newTask.lincToCont.addNew(newTask.getMyself());
				}
				else
				{
					newTask.lincToCont.addNew(newTask);
				}
				
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
    	// show window
		
    	pane.setTop(tb);
    	pane.setCenter(contentCont);
    	pane.setBottom(resizedCont);
    	stage.setScene(scene);
    	
    	resizeObject.setResized(stage, tb, resized);
    	
    	stage.getIcons().add(new Image(Resources.getResource(Resources.IMG_TASK_ICON)));
    	stage.showAndWait();
	}
	
	protected static HBox createDatePicker(CheckBox enable, DatePicker date, ComboBox<String> hours, ComboBox<String> minutes, LocalDateTime expire)
	{
		HBox fullDateCont = new HBox();

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
    	
    	if(enable.isSelected())
    	{
    		minutes.setDisable(false);
    		hours.setDisable(false);
    		date.setDisable(false);
    	}
    	else
    	{
    		minutes.setDisable(true);
    		hours.setDisable(true);
    		date.setDisable(true);
    	}
    	
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
    	
    	VBox enableExpireCont = new VBox();
		Label lEnable = new Label("");
		enable.selectedProperty().addListener(new ChangeListener<Boolean>() 
		{
	        public void changed(ObservableValue<? extends Boolean> ov,
	            Boolean old_val, Boolean new_val)
	        {
	        	if(new_val)
	        	{
	        		minutes.setDisable(false);
	        		hours.setDisable(false);
	        		date.setDisable(false);
	        	}
	        	else
	        	{
	        		minutes.setDisable(true);
	        		hours.setDisable(true);
	        		date.setDisable(true);
	        	}
	        }
	    });

		enableExpireCont.getChildren().addAll(lEnable, enable);
		
    	fullDateCont.getChildren().addAll(enableExpireCont, dateCont, hoursCont, minCont);
    	
    	return fullDateCont;
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
				
		    	Scene scene = new Scene(pane, 400,400, Color.TRANSPARENT);
		    	
		    	scene.getStylesheets().add("application/note.css");
		    	scene.getStylesheets().add("application/application.css");
		    	
		    	//------------------------------------------
		    	// add main info 
				
				VBox contentCont = new VBox();
				
		    	VBox infoCont = new VBox();
		    	
		    	TextField titleE = new TextField(this.title);
		    	titleE.getStyleClass().add("noteTitle");
		    	
		    	titleE.textProperty().addListener(new ChangeListener<String>()
	    			{
						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
						{
							tbButtonsLeft.setVisible(true);
						}
	    			});
		    	
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
		    	
		    	CheckBox enableExpire = new CheckBox();
		    	enableExpire.setSelected(reminde);
		    	enableExpire.selectedProperty().addListener(new ChangeListener<Boolean>() 
				{
			        public void changed(ObservableValue<? extends Boolean> ov,
			            Boolean old_val, Boolean new_val)
			        {
			        	tbButtonsLeft.setVisible(true);
			        }
			    });
		    	
		    	DatePicker date = new DatePicker();
		    	ComboBox<String> hours = new ComboBox<String>();
		    	ComboBox<String> minutes = new ComboBox<String>();
		    	
		    	expireDate.getChildren().addAll(createDatePicker(enableExpire, date, hours, minutes, this.expire));
		    	
		    	infoCont.getChildren().addAll(titleE, bodyE);
		    	VBox.setVgrow(bodyE, Priority.ALWAYS);

		    	contentCont.getChildren().addAll(infoCont, expireDate);
		    	VBox.setVgrow(infoCont, Priority.ALWAYS);
		    	
		    	//---------------------------------------------------------
		    	// add subtask 
		    	
		    	StackPane subItemsCont = new StackPane();
		    	subItemsCont.getStyleClass().add("addSubTasks");
		    	
		    	HBox hb = new HBox();
		    	Label subTasksCount = new Label("Added subtasks - " + this.getSubTusks().size());
		    	hb.getChildren().add(subTasksCount);

		    	ImageView addST = new ImageView(Resources.getResource(Resources.IMG_ADD_TASK));
				Button addSubTask = new Button();
				addSubTask.setGraphic(addST);
				addSubTask.getStyleClass().add("buttons");
				
				subItemsCont.getChildren().addAll(hb, addSubTask);
				
				subItemsCont.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
				{
					if(subTasks.size() != 0)
						subTaskChooseWindow(settings);
					
					subTasksCount.setText("Added subtasks - " + getMyself().getSubTusks().size());
				});

				addSubTask.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
				{
					createItemWindow(settings, getMyself(), resizeObject, getMyself().lincToCont);
					subTasksCount.setText("Added subtasks - " + getMyself().getSubTusks().size());
				});
		    	
		    	//------------------------------------------
		    	// add save and update buttons
	
		    	ImageView ci = new ImageView(Resources.getResource(Resources.IMG_CLOSE));
				Button closeB = new Button();
				closeB.setGraphic(ci);
				closeB.getStyleClass().add("buttons");
				
				closeB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
				{
					shown = false;
					showNote.close();
				});
				
				ImageView coi = new ImageView(Resources.getResource(Resources.IMG_CONFIRMATION));
				Button confirmationB = new Button();
				confirmationB.setGraphic(coi);
				confirmationB.getStyleClass().add("buttons");
				
				confirmationB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
				{
					tbButtonsLeft.setVisible(false);
					
					title = titleE.getText();
					body = bodyE.getText();

					if(enableExpire.isSelected())
					{
						LocalDateTime ldt = date.getValue().atTime(Integer.valueOf(hours.getValue().toString()), Integer.valueOf(minutes.getValue().toString()));

						if(!ldt.equals(expire))
						{
							remindCount = IReminds.NO_REMIND;
							expire = ldt;
						}
						
						reminde = true;
					}
					else
					{
						reminde = false;
					}
					
					lincToCont.update(self);
				});

				tbButtonsLeft.getStyleClass().add("buttonsLeftCont");
				tbButtonsLeft.setVisible(false);
				tbButtonsLeft.getChildren().add(confirmationB);

				tbButtonsRight.getStyleClass().add("buttonsRightCont");
				tbButtonsRight.setPickOnBounds(true);

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
			
				tb.getItems().add(tbButtonsCon);
				tb.getStyleClass().add("buttonsMainCont");

				//------------------------------------------
		    	
				HBox resizedCont = new HBox();
				resizedCont.setAlignment(Pos.CENTER_RIGHT);
				ImageView resizeImg = new ImageView(Resources.getResource(Resources.IMG_RESIZE));
				Label resized = new Label();
				resized.setGraphic(resizeImg);
				resizedCont.getChildren().add(resized);
				
		    	//------------------------------------------
		    	
				contentCont.getChildren().add(subItemsCont);
				
		    	pane.setTop(tb);
		    	pane.setCenter(contentCont);
		    	pane.setBottom(resizedCont);
		    	
		    	showNote.setScene(scene);
		    	showNote.getIcons().add(new Image(Resources.getResource(Resources.IMG_TASK_ICON)));
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

	public Element getXML(Document doc) 
	{
		Element item = doc.createElement(INotes.ITEM); 
		
		Element type = doc.createElement(INotes.TYPE); 
		type.setTextContent(INotes.TASK);

		Element content = doc.createElement(INotes.CONTENT); 

		Element title = doc.createElement(INotes.TITLE); 
		title.setTextContent(this.title);
		Element text = doc.createElement(INotes.TEXT); 
		text.setTextContent(this.body);
		Element status = doc.createElement(INotes.STATUS); 
		status.setTextContent(this.getStatus());
		Element expire = doc.createElement(INotes.EXPIRE); 
		Element remind = doc.createElement(INotes.REMIND);
		remind.setTextContent(String.valueOf(this.reminde));
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
		Element number = doc.createElement(INotes.NUMBER);
		number.setTextContent(String.valueOf(this.number));
		
		if(this.expire != null)
			expire.setTextContent(this.expire.getYear() +":" + this.expire.getMonthValue() + ":" + this.expire.getDayOfMonth() + ":"
				+ this.expire.getHour() + ":" + this.expire.getMinute());
		else
			expire.setTextContent(INotes.NON);
			
		
		Element subTasks = doc.createElement(INotes.SUBTASKS);
		
		if(this.getSubTusks().size() > 0)
		{
			for(INotes in : this.getSubTusks())
			{
				Tasks tmp = (Tasks)in;
				subTasks.appendChild(tmp.getXML(doc));
			}
		}

		content.appendChild(status);
		content.appendChild(expire);
		content.appendChild(remind);
		content.appendChild(title);
		content.appendChild(text);
		content.appendChild(subTasks);
		content.appendChild(x);
		content.appendChild(y);
		content.appendChild(width);
		content.appendChild(height);
		content.appendChild(important);
		content.appendChild(remindCount);
		content.appendChild(number);
		
		item.appendChild(type);
		item.appendChild(content);

		return item;
	}
	
	public static INotes createItem(NodeList noteItems, Resize resizeObject, NotesContainer lincToCont, Tasks parent)
	{
		if(noteItems != null)
		{
			Tasks newTask = new Tasks(parent, resizeObject, lincToCont);
			String title = "";
			String body = "";
			String status = "";
			LocalDateTime expire = null;
			boolean remind = false;
			double x = NotePosition.DEFAULT_X;
			double y = NotePosition.DEFAULT_Y;
			double width = NotePosition.DEFAULT_WIDHT;
			double height = NotePosition.DEFAULT_HEIGHT;
			Boolean important = false;
			int remindCount = IReminds.NO_REMIND;
			Integer number = null;
			
			for(int i = 0; i < noteItems.getLength(); i++)
		    {
				switch(noteItems.item(i).getNodeName())
				{
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
						if(!noteItems.item(i).getTextContent().equals(INotes.NON))
							expire = parseExpireString(noteItems.item(i).getTextContent());
						else
							expire = null;
						break;
						
					case INotes.REMIND:
						remind = Boolean.valueOf(noteItems.item(i).getTextContent());
						break;
						
					case INotes.SUBTASKS:
						
						NodeList subTasksList = noteItems.item(i).getChildNodes();
						
						for(int j = 0; j < subTasksList.getLength(); j++)
			    		{
							for(int k = 0; k < subTasksList.item(j).getChildNodes().getLength(); k++)
							{
								if(subTasksList.item(j).getChildNodes().item(k).getNodeName().equals(INotes.CONTENT))
								{
									newTask.getSubTusks().add(createItem(subTasksList.item(j).getChildNodes().item(k).getChildNodes(), 
											resizeObject, lincToCont, newTask));
								}
							}				
			    		}
						
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
						
					case INotes.NUMBER:
						number = Integer.valueOf(noteItems.item(i).getTextContent());
						break;
				}
		    }

			newTask.setInfo(title, body, status);
			newTask.setImportant(important);
			newTask.setExpireDate(remind, expire, remindCount);
			newTask.setSizeAndPosition(x, y, width, height);
			if(number != null) newTask.setNumber(number);
		
			return (INotes)newTask;
		} 
		else
		{
			return null;
		}
	}

	public VBox getTabItem(Settings settings)
	{
		Label text = new Label(this.title);
		
		if(this.status.equals(INotes.CANSELED))
		{
			ImageView importantImg = new ImageView(Resources.getResource(Resources.IMG_CANSEL_SMAL));
			text.setGraphic(importantImg);
			text.getStyleClass().add("tabPaneItemTextCansel");
		}
		else if(this.status.equals(INotes.COMPLETED))
		{
			ImageView importantImg = new ImageView(Resources.getResource(Resources.IMG_OK));
			text.setGraphic(importantImg);
			text.getStyleClass().add("tabPaneItemTextCompleted");
		}
		else if(isImportant())
		{
			ImageView importantImg = new ImageView(Resources.getResource(Resources.IMG_IMPORTANT));
			text.setGraphic(importantImg);
		}
		
		Pane p = new Pane();
		
		VBox infoCont = new VBox();
		infoCont.getChildren().addAll(text);
		
		HBox reminde = new HBox();

		if(this.reminde == true)
		{
			ImageView remindeImg = new ImageView(Resources.getResource(Resources.IMG_REMINDE));
			remindeImg.setFitWidth(12);
			remindeImg.setFitHeight(12);
			
			Label remaindeLabel = new Label(this.expire.getDayOfMonth() + "/" + this.expire.getMonthValue() + "/" + this.expire.getYear() + " " 
				+ ((this.expire.getHour() >= 10) ? String.valueOf(this.expire.getHour()) : ("0"  + String.valueOf(this.expire.getHour()))) 
					+ ":" + ((this.expire.getMinute() >= 10) ? String.valueOf(this.expire.getMinute()) : ("0"  + String.valueOf(this.expire.getMinute()))));
			
			reminde.getChildren().addAll(remindeImg, remaindeLabel);
		}

		StackPane cont = new StackPane();
		cont.getChildren().addAll(p, text, reminde);
		//cont.setId(String.valueOf(this.id));
		
		cont.getStyleClass().add("tabPaneItem");
		reminde.getStyleClass().add("reminder");
		
		//-----------------------------------

		HBox controls = addControls(this, settings);
		
		controls.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) ->
		{
			text.setOpacity(0.4);
			reminde.setOpacity(0.4);
		});
		
		controls.addEventHandler(MouseEvent.MOUSE_EXITED, (e) ->
		{
			text.setOpacity(1);
			reminde.setOpacity(1);
		});
		
		cont.getChildren().add(controls);
		
		//-----------------------------------
		
		VBox ret = new VBox(cont);
		ret.setId(String.valueOf(this.id));
		
		return ret;
	}
	
	private void subTaskChooseWindow(Settings settings)
	{
		subTusksWindow = new Stage();
		subTusksWindow.initOwner(this.showNote);
		subTusksWindow.initStyle(StageStyle.TRANSPARENT);
		
		BorderPane pane = new BorderPane();
		pane.setOpacity(settings.getOpacityActive());

    	Scene scene = new Scene(pane, 400,400, Color.TRANSPARENT);
    	scene.getStylesheets().add("application/note.css");
    	scene.getStylesheets().add("application/application.css");
    	
    	tb = new ToolBar();
    	
		HBox tbButtonsRight = new HBox();
    	
    	//------------------------------------------------------------------------

		VBox rootTreeValue = getTabItem(settings);
		//rootTreeValue.getChildren().add(addControls(this, settings));
		subTuskTreeRoot = new TreeItem<VBox> (); // TreeItem<String> rootItem = new TreeItem<String> ("Inbox", rootIcon);
		subTuskTreeRoot.setValue(rootTreeValue);
		subTuskTreeRoot.setExpanded(true);
 
        createTree(subTuskTreeRoot, this.getSubTusks(), settings);
        
        //------------------------------------------------------------------------
        // add save and update buttons
    	
    	ImageView ci = new ImageView(Resources.getResource(Resources.IMG_CLOSE));
		Button closeB = new Button();
		closeB.setGraphic(ci);
		closeB.getStyleClass().add("buttons");
		
		closeB.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
		{
			subTuskTreeRoot = null;
			subTusksWindow.close();
		});

		tbButtonsRight.getStyleClass().add("buttonsRightCont");
		tbButtonsRight.setPickOnBounds(true);

		tbButtonsRight.getChildren().add(closeB);
		
		StackPane tbButtonsCon = new StackPane();
		HBox.setHgrow(tbButtonsCon, Priority.ALWAYS);
		
		tbButtonsCon.getChildren().addAll(tbButtonsRight);
	
		tb.getItems().add(tbButtonsCon);
		tb.getStyleClass().add("buttonsMainCont");

    	//------------------------------------------
    	
		HBox resizedCont = new HBox();
		resizedCont.setAlignment(Pos.CENTER_RIGHT);
		ImageView resizeImg = new ImageView(Resources.getResource(Resources.IMG_RESIZE));
		Label resized = new Label();
		resized.setGraphic(resizeImg);
		resizedCont.getChildren().add(resized);
		
        //------------------------------------------------------------------------

        TreeView<VBox> tree = new TreeView<VBox> (subTuskTreeRoot); 
        tree.getStyleClass().add("subTaskTreeCont");
        StackPane root = new StackPane();
        root.getChildren().add(tree);
    
        pane.setTop(tb);
    	pane.setCenter(root);
    	pane.setBottom(resizedCont);
 
        subTusksWindow.setScene(scene);
        resizeObject.setResized(subTusksWindow, tb, resized);
        subTusksWindow.showAndWait(); 
	}
	
	public void updateSubNotes(String type, TreeItem<VBox> rootTree, INotes note, Settings settings)
	{
		if(type.equals(INotes.ADD))
		{
			if(rootTree != null)
			{
				if(rootTree.getValue().getId().equals(note.getParent().getID()))
				{
					Tasks t = (Tasks)note;
					
					TreeItem<VBox> addTreeItem = new TreeItem<VBox>();
					addTreeItem.setValue(createTreeItem(t, settings, rootTree));
					
					if(t.getSubTusks().size() > 0)
						t.createTree(addTreeItem, t.getSubTusks(), settings);
					
					rootTree.getChildren().add( (rootTree.getChildren().size() > 0) ? 
							rootTree.getChildren().size() - 1 :
								0, addTreeItem);
				}

				for(TreeItem<VBox> ti : rootTree.getChildren())
				{
					updateSubNotes(type, ti, note, settings);					
				}
			}
		}
		else if(type.equals(INotes.REMOVE))
		{
			if(rootTree != null)
			{
				for(TreeItem<VBox> ti : rootTree.getChildren())
				{
					if(ti.getValue().getId().equals(note.getID()))
					{
						rootTree.getChildren().remove(ti);
						break;
					}
					
					if(ti.getChildren().size() != 0)
					{
						updateSubNotes(type, ti, note, settings);
					}
				}
			}
		}
		else if(type.equals(INotes.UPDATE))
		{
			if(rootTree != null)
			{
				if(rootTree.getValue().getId().equals(note.getID()))
				{
					VBox sp = note.getTabItem(settings);
					//sp.getChildren().add(note.addControls(note, settings));
					rootTree.setValue(sp);
					
					sp.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
					{
						@Override
						public void handle(MouseEvent arg0) 
						{
							note.show(settings);
						}
					});
				}
				
				for(TreeItem<VBox> ti : rootTree.getChildren())
				{
					if(ti.getValue().getId().equals(note.getID()))
					{
						VBox sp = note.getTabItem(settings);
						//sp.getChildren().add(note.addControls(note, settings));
						ti.setValue(sp);
						
						sp.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> note.show(settings));
						
						break;
					}
					
					if(ti.getChildren().size() != 0)
					{
						updateSubNotes(type, ti, note, settings);
					}
				}
			}
		}
	}
	
	public TreeItem<VBox> getSubTasksTree()
	{
		return this.subTuskTreeRoot;
	}
	
	private void createTree(TreeItem<VBox> root, LinkedList<INotes> items, Settings settings)
	{
		for(INotes in : items)
		{
			Tasks tmp = (Tasks)in;
			TreeItem<VBox> treeItem = new TreeItem<VBox>();
			treeItem.setValue(createTreeItem(tmp, settings, treeItem));
			
			if(tmp.getSubTusks().size() != 0)
			{
				createTree(treeItem, tmp.getSubTusks(), settings);
			}
			
			root.getChildren().add(treeItem);
		}
	}
	
	private VBox createTreeItem(INotes t, Settings settings, TreeItem<VBox> treeCont)
	{
		VBox item = new VBox();
		item.setId(t.getID());

		//--------------------------------------------------------
		
		item = t.getTabItem(settings);
		//item.getChildren().addAll(t.addControls(t, settings));
		
		item.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> t.show(settings));
		
		return item;
	}
	
	public HBox addControls(INotes note, Settings settings)
	{
		HBox tabControls = new HBox();

		//----------------------------------------------
		
		ImageView ci = new ImageView(Resources.getResource(Resources.IMG_TRASH));
		Button delete = new Button();
		delete.setGraphic(ci);
		delete.getStyleClass().add("buttons");
		delete.getStyleClass().add("tabPaneItemButtons");
		
		delete.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> 
		{
			if(note.getParent() == null)
			{
				note.getContainer().delete(note);
			}
			else
			{
				Tasks pTask = (Tasks)note.getParent();
				pTask.getSubTusks().remove(note);
				
				{
					updateSubNotes(INotes.REMOVE, pTask.subTuskTreeRoot, note, settings);
					pTask = (Tasks)pTask.getParent();
				}
				
				note.closeActiveWindow();

				lincToCont.save();
			}
		});
		
		delete.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> delete.setOpacity(0.4));
		delete.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> delete.setOpacity(1));
		
		//----------------------------------------------
		
		ImageView doneImg = new ImageView(Resources.getResource(Resources.IMG_DONE));
		Button done = new Button();
		done.setGraphic(doneImg);
		done.getStyleClass().add("tabPaneItemButtons");
		done.setTooltip(new Tooltip(NoteManager.DONE));
	
		done.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> note.setStatus(INotes.COMPLETED));
		done.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> done.setOpacity(0.4));
		done.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> done.setOpacity(1));
		
		//----------------------------------------------
		
		ImageView canselImg = new ImageView(Resources.getResource(Resources.IMG_CANSEL));
		Button cansel = new Button();
		cansel.setGraphic(canselImg);
		cansel.getStyleClass().add("tabPaneItemButtons");
		cansel.setTooltip(new Tooltip(NoteManager.CANSEL));
		
		cansel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
		{
			note.setStatus(INotes.CANSELED);
			note.getContainer().update(note);
		});
		
		cansel.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> cansel.setOpacity(0.4));
		cansel.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> cansel.setOpacity(1));
		
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
		
		ImageView addImg = new ImageView(Resources.getResource(Resources.IMG_ADD_TASK));
		Button addSubTask = new Button();
		addSubTask.setGraphic(addImg);
		addSubTask.getStyleClass().add("tabPaneItemButtons");
		addSubTask.setTooltip(new Tooltip(NoteManager.ADD_SUB));
	
		addSubTask.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> createItemWindow(settings, note, resizeObject, note.getContainer()));
		addSubTask.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> addSubTask.setOpacity(0.4));
		addSubTask.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> addSubTask.setOpacity(1));

		//----------------------------------------------
		
		ImageView restoreImg = new ImageView(Resources.getResource(Resources.IMG_RESTORE));
		Button restore = new Button();
		restore.setGraphic(restoreImg);
		restore.getStyleClass().add("tabPaneItemButtons");
		restore.setTooltip(new Tooltip(NoteManager.RESTORE));
		
		restore.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> 
		{
			note.setStatus(INotes.ACTIVE);
			note.getContainer().update(note);
		});
		
		restore.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> restore.setOpacity(0.4));
		restore.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> restore.setOpacity(1));

		//----------------------------------------------
		
		if(note.getStatus().equals(INotes.ACTIVE))
		{
			tabControls.getStyleClass().add("tabPaneItemButtonsCont");
			tabControls.getChildren().addAll(done, importantBut, addSubTask, cansel, delete);
		}
		else if(note.getParent() == null || note.getParent().getStatus().equals(INotes.ACTIVE))
		{
			tabControls.getStyleClass().add("tabPaneItemButtonsCont");
			tabControls.getChildren().addAll(restore, delete);
		}

		return tabControls;
	}
	
	public boolean isRemind()
	{
		return this.reminde;
	}
	
	public void closeActiveWindow()
	{
		if(this.showNote != null)
			this.showNote.close();
		
		if(this.subTusksWindow != null)
			this.subTusksWindow.close();
	
		for(INotes n : this.subTasks)
		{
			n.closeActiveWindow();
		}
	}
	
	public LinkedList<INotes> getChildrens()
	{
		return this.subTasks;
	}

	public void setStatus(String status)
	{
		this.status = status;
		this.getContainer().update(this);
		
		if(this.subTasks.size() > 0)
		{
			for(INotes n : this.subTasks)
			{
				n.setStatus(status);
			}
		}
	}
}
