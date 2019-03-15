package minesweeper;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Timer implements Runnable
{
	private int ms, s, min;
	private Label lblTimer;
	private boolean running;

	Timer(Label lblTimer)
	{
		this.lblTimer = lblTimer;
		lblTimer.setPrefWidth(10000);
		lblTimer.setAlignment(Pos.CENTER);
		lblTimer.setFont(new Font(25));
		lblTimer.setTextFill(Color.BLACK);
	}

	@Override
	public void run()
	{
		ms = 0;
		s = 0;
		min = 0;

		String msString = null, sString = null, minString = null;

		while (running)
		{
			ms++;
			if (ms == 100)
			{
				ms = 0;
				s++;
				if (s == 60)
				{
					s = 0;
					min++;
				}
			}

			if (ms < 10)
			{
				msString = "0" + ms;
			}
			else
			{
				msString = ms + "";
			}

			if (s < 10)
			{
				sString = "0" + s;
			}
			else
			{
				sString = s + "";
			}

			if (min < 10)
			{
				minString = "0" + min;
			}
			else
			{
				minString = min + "";
			}
			String finalSString = sString;
			String finalMsString = msString;
			String finalMinString = minString;

			if (min == 0)
			{

				Platform.runLater(() -> lblTimer.setText(finalSString + ":" + finalMsString));
			}
			else
			{

				Platform.runLater(() -> lblTimer.setText(finalMinString + ":" + finalSString + ":" + finalMsString));
			}
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		String finalSString = sString;
		String finalMsString = msString;
		String finalMinString = minString;

		Platform.runLater(() -> lblTimer.setText(finalMinString + ":" + finalSString + ":" + finalMsString));
	}

	@Override
	public String toString()
	{
		String msString, sString, minString;
		if (ms < 10)
		{
			msString = "0" + ms;
		}
		else
		{
			msString = ms + "";
		}

		if (s < 10)
		{
			sString = "0" + s;
		}
		else
		{
			sString = s + "";
		}

		if (min < 10)
		{
			minString = "0" + min;
		}
		else
		{
			minString = min + "";
		}

		return minString + ":" + sString + ":" + msString;
	}

	void setRunning(boolean running)
	{
		this.running = running;
	}
}
