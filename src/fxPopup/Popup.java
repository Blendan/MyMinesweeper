package fxPopup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Popup
{
	private String buttonText;
	private String dialogMessage;
	private String dialogTitel;

	private String dialogStyle = "";
	private String dialogExitButtonStyle = "";

	private int dialogWidth = 200, dialogHeight = 70;

	public Popup()
	{
		this.buttonText = "OK";
	}

	public Popup(String dialogMessage, String dialogTitel)
	{
		this.buttonText = "OK";
		this.dialogMessage = dialogMessage;
		this.dialogTitel = dialogTitel;
	}

	public Popup(String dialogMessage, String dialogTitel, int dialogWidth, int dialogHeight)
	{
		this.buttonText = "OK";
		this.dialogMessage = dialogMessage;
		this.dialogTitel = dialogTitel;
		this.dialogWidth = dialogWidth;
		this.dialogHeight = dialogHeight;
	}

	public Popup(String buttonText, String dialogMessage, String dialogTitel)
	{
		this.buttonText = buttonText;
		this.dialogMessage = dialogMessage;
		this.dialogTitel = dialogTitel;
	}

	public Popup(String buttonText, String dialogMessage, String dialogTitel, String dialogStyle)
	{
		this.buttonText = buttonText;
		this.dialogMessage = dialogMessage;
		this.dialogTitel = dialogTitel;
		this.dialogStyle = dialogStyle;
	}

	public void show()
	{
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initStyle(StageStyle.UNDECORATED);

		VBox dialogMesage = new VBox(20);
		VBox dialogButton = new VBox(20);
		dialogMesage.setPadding(new Insets(10, 10, 10, 10));
		dialogButton.setPadding(new Insets(0, 10, 10, 10));
		dialogButton.setAlignment(Pos.CENTER);


		BorderPane layout = new BorderPane();
		dialogMesage.getChildren().add(new Text(dialogMessage));

		Button exitButton = new Button(buttonText);
		exitButton.setOnAction((event) -> dialog.close());
		exitButton.setStyle(dialogExitButtonStyle);
		dialogButton.getChildren().add(exitButton);

		layout.setCenter(dialogMesage);
		layout.setBottom(dialogButton);

		layout.setStyle(dialogStyle);

		Scene dialogScene = new Scene(layout, dialogWidth, dialogHeight);
		dialog.setScene(dialogScene);

		dialog.setTitle(dialogTitel);

		dialog.show();

		exitButton.requestFocus();
	}

	public String getButtonText()
	{
		return buttonText;
	}

	public void setButtonText(String buttonText)
	{
		this.buttonText = buttonText;
	}

	public String getDialogMessage()
	{
		return dialogMessage;
	}

	public void setDialogMessage(String dialogMessage)
	{
		this.dialogMessage = dialogMessage;
	}

	public String getDialogTitel()
	{
		return dialogTitel;
	}

	public void setDialogTitel(String dialogTitel)
	{
		this.dialogTitel = dialogTitel;
	}

	public String getDialogStyle()
	{
		return dialogStyle;
	}

	public void setDialogStyle(String dialogStyle)
	{
		this.dialogStyle = dialogStyle;
	}

	public int getDialogWidth()
	{
		return dialogWidth;
	}

	public void setDialogWidth(int dialogWidth)
	{
		this.dialogWidth = dialogWidth;
	}

	public int getDialogHeight()
	{
		return dialogHeight;
	}

	public void setDialogHeight(int dialogHeight)
	{
		this.dialogHeight = dialogHeight;
	}

	public String getDialogExitButtonStyle()
	{
		return dialogExitButtonStyle;
	}

	public void setDialogExitButtonStyle(String dialogExitButtonStyle)
	{
		this.dialogExitButtonStyle = dialogExitButtonStyle;
	}
}
