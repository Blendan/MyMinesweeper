package minesweeper;

import javafx.application.Platform;

import java.util.ArrayList;

public class LittleSolver extends LittleHelper implements Runnable
{
	private boolean running = true;
	private Control control;
	private int round = 0;

	public LittleSolver(ArrayList<Feld> feld, int width, int height, Control control)
	{
		super(feld, width, height);
		this.control = control;
	}

	private void nextSolve()
	{
		for (Feld value: feld)
		{
			pruefeUmliegend(value);
		}

		for (Feld value: feld)
		{
			pruefeUmliegend(value);
		}

		boolean gotOne = false;

		for (Feld value: feld)
		{
			if(value.isGoastMarkirt()&&!value.getMakirt())
			{
				Platform.runLater(()->
				{
					control.setBombengefunden(value.makiren()+control.getBombengefunden());
				});

				try
				{
					Thread.sleep(40);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				gotOne = true;
			}
			else if(value.getProzent()==-100)
			{
				Platform.runLater(()->value.zeigen(false));

				if(value.getSpeicherText().equals("0"))
				{
					Platform.runLater(()->control.zeigeumligend(value.getX(),value.getY()));
				}

				if(value.getBombe())
				{
					running = false;
					System.out.println("ups");
				}

				try
				{
					Thread.sleep(40);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				gotOne = true;

			}

		}

		if(!gotOne)
		{
			round ++;
			starteRaten();
		}
		else
		{
			for (Feld value: feld)
			{
				Platform.runLater(()->value.showProzent());
			}
		}

		if(round == 2)
		{
			round = 0;
			running = false;
		}


		Platform.runLater(()->control.gewinnPruefung());


	}

	private void starteRaten()
	{
		//TODO es sol jhe nach nch benötigter bomben anzahl das feld mit den höchsten prozent geflagt werden
	}

	@Override
	public void run()
	{
		round = 0;
		running = true;
		while(running)
		{
			nextSolve();


		}
	}

	public void setRunning(boolean running)
	{
		this.running = running;
	}
}
