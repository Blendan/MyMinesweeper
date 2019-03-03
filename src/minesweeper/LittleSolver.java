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

	public void nextSolve()
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
					Thread.sleep(10);
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
					Thread.sleep(20);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				gotOne = true;

			}

			Platform.runLater(()->value.showProzent());
		}

		if(!gotOne)
		{
			round ++;
		}

		if(round == 2)
		{
			round = 0;
			running = false;
		}


		Platform.runLater(()->control.gewinnPruefung());


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
