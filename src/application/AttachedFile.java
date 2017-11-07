package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AttachedFile  implements IAttachment
{
	private String name;
	private Path path;
	private String id;
	
	AttachedFile(String name, Path path)
	{
		this.name = name;
		this.path = path;
		this.id = String.valueOf(this.hashCode());
	}
	
	public static void createItemWindow(Stage primaryStage, Settings settings, Resize resizeObject, INotes parent)
	{
		String name = "Enter a file name...";
		String path = "file not loaded";
		

		Stage stage = new Stage();
		stage.initOwner(primaryStage);
		stage.initStyle(StageStyle.TRANSPARENT);
		
		BorderPane pane = new BorderPane();
		pane.setOpacity(settings.getOpacityActive());
		
    	Scene scene = new Scene(pane, 350,150, Color.TRANSPARENT);
    	scene.getStylesheets().add("application/note.css");
    	scene.getStylesheets().add("application/application.css");
    	
    	HBox tbButtonsLeft = new HBox();
		HBox tbButtonsRight = new HBox();
    	
		//---------------------------------------------------------
		
		VBox content = new VBox();
		  
		// field with file name
    	TextField fileName = new TextField(name);
    	fileName.getStyleClass().add("noteTitle");
    	
    	fileName.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if(newValue)
				{
					if(fileName.getText().equals(name))
						fileName.setText("");
				}
				else
				{
					if(fileName.getText().equals(""))
						fileName.setText(name);
				}
			}
		});
    	
    	//----------------------------
		
    	HBox cooseFileCont = new HBox();
    	FileChooser fileChooser = new FileChooser();
    	Label filePath = new Label(path);
    	filePath.getStyleClass().add("filePath");

        Label openButton = new Label();
        openButton.setGraphic(new ImageView(Resources.getResource(Resources.IMG_LOAD)));
        openButton.getStyleClass().add("buttons");
 
        openButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent arg0) 
            {
            	File f = fileChooser.showOpenDialog(stage);
            	
            	if(f != null)
            		filePath.setText(f.toString());
            }
        });
		
        cooseFileCont.getChildren().addAll(openButton, filePath);
        
        content.getChildren().addAll(fileName, cooseFileCont);
        
		//------------------------------------------
        
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
				if(fileName.getText().equals(name) || filePath.equals(path))
				{
					System.out.println("not ready");
				}
				else
				{
					//------------------------------------------------------------
					
					Path sours = Paths.get(filePath.getText());
					Path deist = Paths.get(settings.attachmentSaveString() + File.separator 
							+ "_" + String.valueOf(sours.getFileName().hashCode()) + "_" + sours.getFileName());
					
					try 
					{
						if(!Files.exists(Paths.get(settings.attachmentSaveString())))
						{
							Files.createDirectory(Paths.get(settings.attachmentSaveString()));
						}
						
						Files.copy(sours, deist);
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
					//------------------------------------------------------------
					
					parent.addAttachment(new AttachedFile(fileName.getText(), deist));
					
					//------------------------------------------------------------

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

	public static IAttachment createItem(NodeList items)
	{
		if(items != null)
		{
			String name = null;
			Path path = null;
			
			for(int i = 0; i < items.getLength(); i++)
			{
				switch(items.item(i).getNodeName())
				{
					case IAttachment.NAME:
						
						name = items.item(i).getTextContent();
						
					break;
					
					case IAttachment.PATH:
						
						path = Paths.get(items.item(i).getTextContent());
						
					break;
				}
			}
			
			return new AttachedFile(name, path);
		}
		else
		{
			return null;
		}
	}
	

	@Override
	public Element getXML(Document doc) 
	{
		Element item = doc.createElement(IAttachment.FILE);
		
		Element name = doc.createElement(IAttachment.NAME); 
		name.setTextContent(this.name);
		Element path = doc.createElement(IAttachment.PATH); 
		path.setTextContent(this.path.toString());
		
		item.appendChild(name);
		item.appendChild(path);
		
		return item;
	}

	@Override
	public VBox getGUIItem() 
	{
		
		Label text = new Label(this.name);
		
		ImageView fileImg = new ImageView(Resources.getResource(Resources.IMG_FILE));
		text.setGraphic(fileImg);
	
		
		StackPane cont = new StackPane(text);
		//cont.setId(String.valueOf(this.id));
		
		text.getStyleClass().add("tabPaneItemText");
		cont.getStyleClass().add("tabPaneItem");

		//-----------------------------------
		
		HBox controls = addControls();
		
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
		
		VBox ret = new VBox(cont);
		ret.setId(String.valueOf(this.id));
		
		return ret;
	}
	
	private HBox addControls()
	{
		HBox controls = new HBox();
		controls.getStyleClass().add("tabPaneItemButtonsCont");
		
		ImageView delImg = new ImageView(Resources.getResource(Resources.IMG_TRASH));
		Button delete = new Button();
		delete.setGraphic(delImg);
		delete.setTooltip(new Tooltip(NoteManager.DELETE));
		delete.getStyleClass().add("tabPaneItemButtons");
		
		delete.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {});
		delete.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> delete.setOpacity(0.4));
		delete.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> delete.setOpacity(1));
		
		ImageView saveFileImg = new ImageView(Resources.getResource(Resources.IMG_SAVE));
		Button save = new Button();
		save.setGraphic(saveFileImg);
		save.setTooltip(new Tooltip(NoteManager.SAVE_FILE));
		save.getStyleClass().add("tabPaneItemButtons");
		
		save.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> 
		{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialFileName(this.path.getFileName().toString()
					.substring(this.path.getFileName().toString().lastIndexOf("_") + 1, 
							this.path.getFileName().toString().length()));
			
			File f = fileChooser.showSaveDialog(null);
			
			if(f != null)
			{
				try 
				{
					Files.copy(this.path, f.toPath(), StandardCopyOption.REPLACE_EXISTING); 

					// open folder with copied file
					
					if( Desktop.isDesktopSupported() )
					{
					    new Thread(() -> {
					    	  try 
					    	  {
					    		  Desktop.getDesktop().open(
					    				  new File(f.toString().substring(0, f.toString().lastIndexOf(File.separator))));
					    	  } 
					    	  catch (IOException ex) 
					    	  {
									System.out.println("err");
					    	  }
					     }).start();
					}
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		
		});
		
		save.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> save.setOpacity(0.4));
		save.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> save.setOpacity(1));
		
		controls.getChildren().addAll(save, delete);
		
		return controls;
	}

	@Override
	public String getType() 
	{
		return IAttachment.FILE;
	}

	@Override
	public String getID() 
	{
		return this.id;
	}

	@Override
	public String getName() 
	{
		return this.name;
	}

	@Override
	public String getPath() 
	{
		return this.path.toString();
	}

	public void open() 
	{
		if( Desktop.isDesktopSupported() )
		{
		    new Thread(() -> {
		    	  try 
		    	  {
		    		  Desktop.getDesktop().open(this.path.toFile());
		    	  } 
		    	  catch (IOException e) 
		    	  {
						e.printStackTrace();
		    	  }
		     }).start();
		}
	}

}
