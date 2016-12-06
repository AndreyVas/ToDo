package application;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Resize 
{
	private double pressedX;
	private double pressedY;
	
	private double pressedMAX;
	private double pressedMAY;
	
	NoteManager noteManager;

	Resize(NoteManager nm)
	{
		this.noteManager = nm;
	}
	
	public void setResized(Stage st, ToolBar moved)
	{
		Stage stage = st;
		Scene scene = st.getScene();
	
		moved.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				scene.setCursor(Cursor.OPEN_HAND);
			}
		});
		
		moved.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				scene.setCursor(Cursor.OPEN_HAND);
			}
		});
		
		moved.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				scene.setCursor(Cursor.DEFAULT);
			}
		});
		
		moved.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				scene.setCursor(Cursor.CLOSED_HAND);
				
				pressedMAX = arg0.getScreenX();
				pressedMAY = arg0.getScreenY();
			}
		});
		
		moved.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				if(arg0.getScreenX() < pressedMAX)
				{
					stage.setX(stage.getX() - (pressedMAX - arg0.getScreenX()));
				}
				else if(arg0.getScreenX() > pressedMAX)
				{
					stage.setX(stage.getX() + (arg0.getScreenX() - pressedMAX));
				}
				
				if(arg0.getScreenY() < pressedMAY)
				{
					stage.setY(stage.getY() + (arg0.getScreenY() - pressedMAY));
				}
				else if(arg0.getScreenY() > pressedMAY)
				{
					stage.setY(stage.getY() - (pressedMAY - arg0.getScreenY()));
				}
				
				pressedMAX = arg0.getScreenX();
				pressedMAY = arg0.getScreenY();
			}
		});
		
		moved.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				noteManager.save();
			}
		});
	}
	
	public void setResized(Stage st, ToolBar moved, Label resized)
	{
		Stage stage = st;
		Scene scene = st.getScene();
		
		resized.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				scene.setCursor(Cursor.MOVE);
			}
		});
		
		resized.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				scene.setCursor(Cursor.DEFAULT);
			}
		});
		
		resized.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				pressedX = arg0.getScreenX();
				pressedY = arg0.getScreenY();
			}
		});

		resized.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				stage.setWidth(stage.getWidth() + (arg0.getScreenX() - pressedX));
				
				stage.setHeight(stage.getHeight() + (arg0.getScreenY() - pressedY));
				
				pressedX = arg0.getScreenX();
				pressedY = arg0.getScreenY();
				
				
			}
		});
		
		resized.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				
				noteManager.save();
			}
		});

		moved.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				scene.setCursor(Cursor.OPEN_HAND);
			}
		});
		
		moved.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				scene.setCursor(Cursor.OPEN_HAND);
			}
		});
		
		moved.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				scene.setCursor(Cursor.DEFAULT);
			}
		});
		
		moved.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				scene.setCursor(Cursor.CLOSED_HAND);
				
				pressedMAX = arg0.getScreenX();
				pressedMAY = arg0.getScreenY();
			}
		});
		
		moved.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				if(arg0.getScreenX() < pressedMAX)
				{
					stage.setX(stage.getX() - (pressedMAX - arg0.getScreenX()));
				}
				else if(arg0.getScreenX() > pressedMAX)
				{
					stage.setX(stage.getX() + (arg0.getScreenX() - pressedMAX));
				}
				
				if(arg0.getScreenY() < pressedMAY)
				{
					stage.setY(stage.getY() + (arg0.getScreenY() - pressedMAY));
				}
				else if(arg0.getScreenY() > pressedMAY)
				{
					stage.setY(stage.getY() - (pressedMAY - arg0.getScreenY()));
				}
				
				pressedMAX = arg0.getScreenX();
				pressedMAY = arg0.getScreenY();
			}
		});
		
		moved.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0) 
			{
				noteManager.save();
			}
		});
	}
}
