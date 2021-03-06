package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Main extends Application 
{
	Settings settings;
	NoteManager noteManager;
	NotesContainer notes;
	Remind remind;
	Resize resize;

	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			primaryStage.getIcons().add(new Image(Resources.getResource(Resources.IMG_ICON)));
			
			settings = new Settings();
		
			notes = new NotesContainer(primaryStage);
			remind = new Remind(notes, settings);
			noteManager = new NoteManager(notes, settings, remind, primaryStage);
			resize = new Resize(noteManager);
			
			notes.addObserver(noteManager);

			//------------------------------------------------------------------------
			
			BorderPane mainPane = new BorderPane();
			Scene scene = new Scene(mainPane,settings.getWidthMain(), settings.getHeightMain(), Color.TRANSPARENT);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("noteManager.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("note.css").toExternalForm());
			
			mainPane.setOpacity(settings.getOpacityActive());
	
			//------------------------------------------------------------------------
			
			HBox tbButtonsLeft = new HBox();
			HBox tbButtonsRight = new HBox();
			
			//------------------------------------------------------------------------
			
			ImageView ati = new ImageView(Resources.getResource(Resources.IMG_ADD_TASK));
			Button addTaskB = new Button();
			addTaskB.setGraphic(ati);
			addTaskB.getStyleClass().add("buttons");
			
			addTaskB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent arg0) 
				{
					noteManager.showAddWindow();
				}
			});
			
			addTaskB.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> addTaskB.setOpacity(0.4));
			addTaskB.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> addTaskB.setOpacity(1));
			
			//------------------------------------------------------------------------
			
			/*
			ImageView afi = new ImageView(INotes.IMG_ADD_FOLDER);
			Button addFolderB = new Button();
			addFolderB.setGraphic(afi);
			addFolderB.getStyleClass().add("buttons");

			addFolderB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent arg0) 
				{
					noteManager.addFolder();
				}
			});*/
			
			Button addFolderB = noteManager.createAddFolderButton();
			
			//------------------------------------------------------------------------
			
			Label noteIcon = noteManager.createTabIcon();
			
			//------------------------------------------------------------------------
			
			ToolBar tb = new ToolBar();
			
			tb.setBackground(Background.EMPTY);
		
			tbButtonsLeft.getStyleClass().add("buttonsLeftCont");
			tbButtonsLeft.getChildren().addAll(addTaskB, addFolderB);

			tbButtonsRight.getStyleClass().add("buttonsRightCont");
			tbButtonsRight.setPickOnBounds(true);

			tbButtonsRight.getChildren().add(noteIcon);
			
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
			
			//------------------------------------------------------------------------
			
			HBox resizedCont = new HBox();
			resizedCont.setAlignment(Pos.CENTER_RIGHT);
			ImageView resizeImg = new ImageView(Resources.getResource(Resources.IMG_RESIZE));
			Label resized = new Label();
			resized.setGraphic(resizeImg);
			resizedCont.getChildren().add(resized);
			
			//------------------------------------------------------------------------
		
			mainPane.setTop(tb);
			mainPane.setCenter(noteManager.show());
			mainPane.setBottom(resizedCont);

			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setX(Screen.getPrimary().getBounds().getWidth() - (scene.getWidth() + scene.getWidth() / 10));
			primaryStage.setY(scene.getHeight() / 10);
  
			//primaryStage.getIcons().add(new Image("noteIcon.png"));
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			noteManager.load();
			resize.setResized(primaryStage, tb, resized);
			
			
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			
			Messages.showError(e.getMessage() + "\\n\\n" 
					+ e.getLocalizedMessage()  + "\\n\\n" + e.getStackTrace());
		}
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
