package minesweeper;

import javafx.application.Platform;

import java.util.ArrayList;

public class LittleSolver extends LittleHelper implements Runnable
{
	private boolean running = true;
	private Control control;
	private int round = 0;
	private final int speed = 20;

	public LittleSolver(ArrayList<Feld> feld, int width, int height, Control control)
	{
		super(feld, width, height);
		this.control = control;
	}

	private void nextSolve()
	{
		boolean fucktUp = false;
		boolean gotOne = false;

		for (Feld value: feld)
		{
			gotOne = pruefeUmliegend(value);
		}

		if(round==2&&!gotOne)
		{

			for (Feld value: feld)
			{
				fucktUp = starteRaten(value);
			}
		}

		if(!fucktUp)
		{

			if (!waehleFelder())
			{
				round++;
			}
			else
			{
				round = 0;
			}

			if (round == 3)
			{
				round = 0;
				running = false;
			}
		}


		for (Feld value : feld)
		{
			Platform.runLater(value::showProzent);
		}


		Platform.runLater(()->control.gewinnPruefung());
	}

	private boolean waehleFelder()
	{
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
					Thread.sleep(speed);
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
					Platform.runLater(()->control.verloren());
				}

				try
				{
					Thread.sleep(speed);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				gotOne = true;

			}

		}

		return  gotOne;
	}

	private boolean starteRaten(Feld value)
	{
		int felderOffen = 9;
		int felderMakirt = 0;


		Feld feldMax = null;
		Feld feldMin = null;



		int feldID = width*value.getX()+value.getY();

		if(value.isAufgedekt() && !value.getSpeicherText().equals("X") && !value.getSpeicherText().equals("0"))
		{
			int bombenToFind = Integer.parseInt(value.getSpeicherText());

			for (int i = -1; i < 2; i++)
			{
				for (int j = -1 * width; j <= width; j += width)
				{
					if (feldID + i + j >= 0 && feldID + i + j < height * width && !(feldID % width == 0 && i == -1) && !(feldID % width == width - 1 && i == 1))
					{
						if (feld.get(feldID + i + j).getMakirt() || feld.get(feldID + i + j).isAufgedekt() || feld.get(feldID + i + j).isGoastMarkirt())
						{
							felderOffen--;
						}

						if (feld.get(feldID + i + j).getMakirt() || feld.get(feldID + i + j).isGoastMarkirt())
						{
							felderMakirt++;
						}

						if (!feld.get(feldID + i + j).getMakirt() && !feld.get(feldID + i + j).isAufgedekt() && !feld.get(feldID + i + j).isGoastMarkirt() && feld.get(feldID + i + j).getProzent()!=0 && feld.get(feldID + i + j).getProzent()!=-100)
						{
							if (feldMin == null)
							{
								feldMin = feld.get(feldID + i + j);
							}
							else
							{
								if (feld.get(feldID + i + j).getProzent() < feldMin.getProzent())
								{
									feldMin = feld.get(feldID + i + j);
								}
							}

							if (feldMax == null)
							{
								feldMax = feld.get(feldID + i + j);
							}
							else
							{
								if (feld.get(feldID + i + j).getProzent() > feldMax.getProzent())
								{
									feldMax = feld.get(feldID + i + j);
								}
							}
						}
					}
					else
					{
						felderOffen--;
					}

					if(felderOffen==1)
					{
						return true;
					}
				}
			}

			if(bombenToFind - felderMakirt == 1 && feldMax!=null && feldMin!=null)
			{
				if(feldMax.getProzent()>feldMin.getProzent())
				{
					feldMax.setGoastMarkirt(true);
				}
			}
		}
		return false;
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

	void setRunning(boolean running)
	{
		this.running = running;
	}
}
