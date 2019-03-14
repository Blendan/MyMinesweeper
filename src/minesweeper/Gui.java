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


	/**
	 * beendet TImer und LittleHelper damit die nicht im hintergrund weiterlaufen
	 */
	@Override
	public void stop()
	{
		if(control.getLittleSolver()!=null)
		{
			control.getLittleSolver().forceColose();
			control.stopTimer();
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
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		assert root != null;
		Scene scene = new Scene(root);

		primaryStage.setTitle("Minesweeper");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(Gui.class.getResource("master.css").toExternalForm());
		primaryStage.show();

		control = (Control)loader.getController();
	}
}
