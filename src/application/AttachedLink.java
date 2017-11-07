package application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.w3c.dom.DOMException;
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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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

public class AttachedLink implements IAttachment
{
	private String name;
	private URL address;
	private String id;
	
	AttachedLink(String name, URL address)
	{
		this.name = name;
		this.address = address;
		
		this.id = String.valueOf(this.hashCode());
	}
	
	public static void createItemWindow(Stage primaryStage, Settings settings, Resize resizeObject, INotes parent)
	{
		String name = "Enter a link name...";
		String link = "Enter a link...";
		
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
  
    	TextField linkName = new TextField(name);
    	linkName.getStyleClass().add("noteTitle");
    	
    	linkName.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if(newValue)
				{
					if(linkName.getText().equals(name))
						linkName.setText("");
				}
				else
				{
					if(linkName.getText().equals(""))
						linkName.setText(name);
				}
			}
		});
    	
    	TextField linkText = new TextField(link);
    	linkText.getStyleClass().add("noteText");
    	
    	linkText.focusedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if(newValue)
					{
						if(linkText.getText().equals(link))
							linkText.setText("");
					}
					else
					{
						if(linkText.getText().equals(""))
							linkText.setText(link);
					}
				}
			});
   
    	content.getChildren().addAll(linkName, linkText);
    	
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
				if(linkName.getText().equals(name) || linkText.getText().equals(link))
				{
					System.out.println("not ready");
				}
				else
				{
					URL url = null;
					
					try 
					{
						url = new URL(linkText.getText());
					} 
					catch (MalformedURLException e) 
					{
						Messages.showError(e.getMessage());
					}
					
					if(url != null)
						parent.addAttachment(new AttachedLink(linkName.getText(), url));
					
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
	
	@Override
	public Element getXML(Document doc) 
	{
		Element item = doc.createElement(IAttachment.LINK);
		
		Element name = doc.createElement(IAttachment.NAME); 
		name.setTextContent(this.name);
		Element link = doc.createElement(IAttachment.ADDRESS); 
		link.setTextContent(this.address.toString());
		
		item.appendChild(name);
		item.appendChild(link);
		
		return item;
	}
	
	public static IAttachment createItem(NodeList items)
	{
		if(items != null)
		{
			String name = null;
			URL address = null;
			
			for(int i = 0; i < items.getLength(); i++)
			{
				switch(items.item(i).getNodeName())
				{
					case IAttachment.NAME:
						
						name = items.item(i).getTextContent();
						
					break;
					
					case IAttachment.ADDRESS:
						
						try 
						{
							address = new URL(items.item(i).getTextContent());
						} 
						catch (MalformedURLException e) 
						{
							e.printStackTrace();
						} 
						catch (DOMException e) 
						{

							e.printStackTrace();
						}
						
					break;
				}
			}
			
			return new AttachedLink(name, address);
		}
		else
		{
			return null;
		}
		
	}

	@Override
	public VBox getGUIItem() 
	{
		
		Label text = new Label(this.name);
		
		ImageView linkImg = new ImageView(Resources.getResource(Resources.IMG_LINK));
		text.setGraphic(linkImg);
	
		
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
		
		ImageView compLinkImg = new ImageView("copyLink.png");
		Button copyLink = new Button();
		copyLink.setGraphic(compLinkImg);
		copyLink.setTooltip(new Tooltip(NoteManager.COPY_LINK));
		copyLink.getStyleClass().add("tabPaneItemButtons");
		
		copyLink.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> 
		{
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			content.putString(this.address.toString());
			clipboard.setContent(content);
			
			copyLink.setOpacity(0);
		});
		
		copyLink.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> copyLink.setOpacity(0.4));
		copyLink.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> copyLink.setOpacity(1));
		
				
		controls.getChildren().addAll(copyLink, delete);
		
		return controls;
	}

	@Override
	public String getType() 
	{
		return IAttachment.LINK;
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
		return this.address.toString();
	}

	@Override
	public void open() 
	{
		try 
		 {
			 new ProcessBuilder("x-www-browser", this.address.toString()).start();
		 } 
		 catch (IOException e) 
		 {
			 e.printStackTrace();
		 }
		
	}

}
