package minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui extends Application
{
	public Gui()
	{

	}

	@Override
	public void start(Stage primaryStage)
	{

		Parent root = null;
		try
		{
			root = FXMLLoader.load(getClass().getResource("main.fxml"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		Scene scene = new Scene(root);

		primaryStage.setTitle("Minesweeper");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(Gui.class.getResource("master.css").toExternalForm());
		primaryStage.show();
	}
}
