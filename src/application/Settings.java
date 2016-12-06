package application;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Settings 
{
	private double opacityActive;
	private double opacityInactive;
	
	private enum SettingsEnum {BACKGROUND, FONT};
	
	private VBox mainSettingsPannel;
	private VBox backgroundPane;
	private VBox fontPane;
	
	private Color bcColor;
	private Color sColor;
	
	private int widthMain;
	private int heightMain;
	
	private int widthAddItems;
	private int heightAddItems;
	
	private int widthRemindWidnow;
	private int heightRemindWindow;
	
	public static final String ROOT_SETTINGS_PATH = "DATA\\";
	public static final String SETTINGS = "settings.xml";
	public static final String NOTES = "notes.xml";
	
	private long firstRemind;
	private long secondRemind;
	private long thirdRemind;
	
	Settings()
	{
		this.opacityActive = 0.9;
		this.opacityInactive = 0.5;
		
		this.bcColor = Color.CRIMSON;
		this.sColor = Color.AQUA;
		
		this.widthMain = 400;
		this.heightMain = 400;
		
		this.widthAddItems = 250;
		this.heightAddItems = 200;
		
		this.widthRemindWidnow = 400;
		this.heightRemindWindow = 300;
		
		this.firstRemind = 60000 * (24 * 60) ;	// one day
		this.secondRemind = 60000 * 60;		// one hour
		this.thirdRemind = 60000 * 10; 	// 10 min
		
		/*this.firstRemind = 60000 * 5 ;	// 5 min
		this.secondRemind = 60000 * 3;		// 3 min
		this.thirdRemind = 60000 * 1; 	// 1 min*/
	}
	
	public long getFirstRemind()
	{
		return this.firstRemind;
	}
	
	public long getSecondRemind()
	{
		return this.secondRemind;
	}
	
	public long getThirdRemind()
	{
		return this.thirdRemind;
	}
	
	public double getOpacityActive()
	{
		return this.opacityActive;
	}
	
	public double getOpacityInactive()
	{
		return this.opacityInactive;
	}
	
	public int getWidthMain()
	{
		return this.widthMain;
	}
	
	public int getHeightMain()
	{
		return this.heightMain;
	}
	
	public int getWidthAddItems()
	{
		return this.widthAddItems;
	}
	
	public int getHeightAddItems()
	{
		return this.heightAddItems;
	}
	
	public int getWidthRemindWindow()
	{
		return this.widthRemindWidnow;
	}
	
	public int getHeightRemindWindow()
	{
		return this.heightRemindWindow;
	}
	
	public void load()
	{
		
	}
	
	public void save()
	{
		
	}
	
	public void show()
	{
		BorderPane pane = new BorderPane();
		pane.setOpacity(this.opacityActive);
		Stage settingsWindow = new Stage();
		Scene scene = new Scene(pane, 300, 400, Color.TRANSPARENT);
		scene.getStylesheets().add(getClass().getResource("settings.css").toExternalForm());
		scene.getStylesheets().add("application/application.css");
		
		//------------------------------------------------------------------------

		ImageView ci = new ImageView("close.png");
		Button closeB = new Button();
		closeB.setGraphic(ci);
		closeB.getStyleClass().add("topMenuButtons");
		
		closeB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				settingsWindow.close();
			}
		});
		
		//------------------------------------------------------------------------
		
		ImageView coi = new ImageView("confirmation.png");
		Button confirmationB = new Button();
		confirmationB.setGraphic(coi);
		confirmationB.getStyleClass().add("topMenuButtons");
		
		confirmationB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				settingsWindow.close();
			}
		});
		
		//------------------------------------------------------------------------
		
		ToolBar tb = new ToolBar();
		tb.getStyleClass().add("topMenu");
		tb.getItems().addAll(confirmationB, closeB);
		tb.setBackground(Background.EMPTY);
		
		//------------------------------------------------------------------------
		
		
		VBox chooseCont = new VBox();
		chooseCont.getStyleClass().add("chooseCont");
		
		Button background = new Button("Фон");
		background.getStyleClass().add("chooseBut");
		
		background.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				choosePane(SettingsEnum.BACKGROUND);
			}
		});
		
		Button font = new Button("Текст");
		font.getStyleClass().add("chooseBut");
		
		font.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				choosePane(SettingsEnum.FONT);
			}
		});
		
		chooseCont.getChildren().addAll(font, background);
		
		//------------------------------------------------------------------------

		fontPane = createFontPane();
		backgroundPane = createBackgroundPane();

		mainSettingsPannel = new VBox();

		choosePane(SettingsEnum.FONT);
		
		//------------------------------------------------------------------------
		
		pane.setTop(tb);
		pane.setLeft(chooseCont);
		pane.setRight(mainSettingsPannel);
		
		
		settingsWindow.initStyle(StageStyle.TRANSPARENT);
		settingsWindow.setScene(scene);
		settingsWindow.show();
	}
	
	private VBox createBackgroundPane()
	{
		//---------background color----------
		
		VBox bcBox = new VBox();
		
		Label bcLabel = new Label("Цвет фона");
		
		final ColorPicker bcPicker = new ColorPicker();
		bcPicker.setValue(Color.CORAL);

		bcPicker.setOnAction(new EventHandler() 
        {
            public void handle(Event t) 
            {
                bcColor = bcPicker.getValue();
            }
        });
        
        bcBox.getChildren().addAll(bcLabel, bcPicker);
        
		//---------splitters color-----------
		
        VBox sBox = new VBox();
		
        Label sLabel = new Label("Цвет Разделителя");
		
		final ColorPicker sPicker = new ColorPicker();
		bcPicker.setValue(Color.CORAL);

		
		sPicker.setOnAction(new EventHandler() 
        {
            public void handle(Event t) 
            {
                System.out.println(sPicker.getValue());    
                
            }
        });
		
		sBox.getChildren().addAll(sLabel, sPicker);
        
        //---add panels to background panel---
        
		VBox cpPane = new VBox();
        cpPane.getChildren().addAll(bcBox, sBox);
		
		return cpPane;
	}

	private VBox createFontPane()
	{
		//----------choose font-----------
		
		VBox cfBox = new VBox();
		
		Label sfLabel = new Label("Выберите шрифт");

		
		
		
		cfBox.getChildren().addAll(sfLabel);
		
		//-----------font color-----------
		
		VBox fcBox = new VBox();
		
		Label fcLabel = new Label("Выберите цвет");
		
		fcBox.getChildren().addAll(fcLabel);
		
		//-----------font style-----------
		
		VBox fsBox = new VBox();
		
		Label fsLabel = new Label("Выберите стиль");
		
		fsBox.getChildren().addAll(fsLabel);
		
		//--------------------------------
		
		VBox fPane = new VBox();
		fPane.getChildren().addAll(cfBox, fcBox, fsBox);
		
		System.out.println("bla");
		
		return fPane;
	}
	
	private void choosePane(SettingsEnum se)
	{
		switch(se)
		{	
			case BACKGROUND:
				mainSettingsPannel.getChildren().remove(fontPane);
				mainSettingsPannel.getChildren().add(backgroundPane);
				break;
				
			case FONT:
				mainSettingsPannel.getChildren().remove(backgroundPane);
				mainSettingsPannel.getChildren().add(fontPane);
				break;
		}
	}

	
}
