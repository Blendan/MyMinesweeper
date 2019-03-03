package minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Gui extends Application
{
	private Control control;
	public Gui()
	{

	}

	@Override
	public void stop()
	{
		if(control.getLittleSolver()!=null)
		{
			control.getLittleSolver().setRunning(false);
		}
	}

	@Override
	public void start(Stage primaryStage)
	{


		Parent root = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
		try
		{
			root = (Parent)loader.load();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		Scene scene = new Scene(root);

		primaryStage.setTitle("Minesweeper");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(Gui.class.getResource("master.css").toExternalForm());
		primaryStage.show();

		control = (Control)loader.getController();
	}
}
