package minesweeper;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class LittleSolver extends LittleHelper implements Runnable
{
	private boolean running = true, forceClose = false;
	private Control control;
	private int round = 0;
	private final int speed = 20;
	private final boolean isDebug = false;

	LittleSolver(ArrayList<Feld> feld, int width, int height, Control control)
	{
		super(feld, width, height);
		this.control = control;
	}

	private boolean picRandom()
	{
		ArrayList<Feld> picabel = new ArrayList<>();

		for (Feld value : feld)
		{
			if (!value.isGoastMarkirt() && !value.isAufgedekt() && !value.getMakirt())
			{
				if (control.isFertig())
				{
					return false;
				}
				picabel.add(value);
			}
		}


		if (picabel.size() != 0)
		{
			int random = ThreadLocalRandom.current().nextInt(0, picabel.size());

			if (picabel.get(random).getBombe())
			{
				Platform.runLater(() -> control.verloren());
				return false;
			}
			else
			{
				Platform.runLater(() -> picabel.get(random).zeigen(false, false));

				if (picabel.get(random).getSpeicherText().equals("0"))
				{
					Platform.runLater(() -> control.zeigeumligend(picabel.get(random).getX(), picabel.get(random).getY()));
				}

				try
				{
					Thread.sleep(speed);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				return true;
			}
		}

		return false;
	}

	private void scannForFelder(Feld value)
	{
		if (value.isAufgedekt() && !value.getSpeicherText().equals("X"))
		{
			int felderOffen = 9;
			int felderMakirt = 0;

			int feldID = width * value.getX() + value.getY();
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
					}
					else
					{
						felderOffen--;
					}
				}
			}
			value.setBombenOffen(bombenToFind - felderMakirt);
			value.setFelderOffen(felderOffen);
		}
	}

	private void scannForBombs(Feld value)
	{
		if (!value.isAufgedekt() && !value.isGoastMarkirt())
		{
			int overlap = 0;
			int felderOffen = 8;

			int feldID = width * value.getX() + value.getY();

			for (int i = -1; i < 2; i++)
			{
				for (int j = -1 * width; j <= width; j += width)
				{
					if (feldID + i + j >= 0 && feldID + i + j < height * width && !(feldID % width == 0 && i == -1) && !(feldID % width == width - 1 && i == 1))
					{
						if (feld.get(feldID + i + j).isAufgedekt() && feld.get(feldID + i + j) != value)
						{
							if (feld.get(feldID + i + j).getBombenOffen() == 1)
							{
								overlap++;
							}
							felderOffen--;
						}

						if (feld.get(feldID + i + j).getMakirt() || feld.get(feldID + i + j).isGoastMarkirt())
						{
							break;
						}
					}
				}
			}
			if (isDebug)
			{
				System.out.println(felderOffen + " | " + overlap);
			}

			if (overlap >= 8 - felderOffen && felderOffen != 8)
			{
				value.setGoastMarkirt(true);
			}
		}
	}

	private void nextSolve()
	{
		boolean fucktUp = false;
		boolean gotOne = false;

		for (Feld value : feld)
		{
			if (control.isFertig() || forceClose)
			{
				break;
			}
			gotOne = pruefeUmliegend(value);
		}

		/*for (Feld value: feld)
		{
			if(control.isFertig()||forceClose)
			{
				break;
			}
			scannForFelder(value);
		}

		for (Feld value: feld)
		{
			if(control.isFertig()||forceClose)
			{
				break;
			}
			scannForBombs(value);
		}*/

		for (Feld value : feld)
		{
			if (control.isFertig() || forceClose)
			{
				break;
			}
			fucktUp = lookForError(value);
			if (fucktUp)
			{
				resetMarirung();
				break;
			}
		}


		if (round == 2 && !gotOne && !fucktUp)
		{
			for (Feld value : feld)
			{
				if (control.isFertig() || forceClose)
				{
					break;
				}
				fucktUp = starteRaten(value);
			}
		}

		if (gotOne)
		{
			round = 0;
		}

		if (!fucktUp)
		{
			if (!waehleFelder())
			{
				round++;
			}
			else
			{
				round = 0;
			}

			if (round == 4)
			{
				if (!picRandom())
				{
					running = false;
				}
				round = 0;
			}
		}

		if (control.isFertig())
		{
			running = false;
		}


		for (Feld value : feld)
		{
			if (control.isFertig())
			{
				break;
			}
			Platform.runLater(value::showProzent);
		}


	}

	private boolean waehleFelder()
	{
		boolean gotOne = false;

		for (Feld value : feld)
		{
			if (value.isGoastMarkirt() && !value.getMakirt())
			{
				Platform.runLater(() -> control.setBombengefunden(value.makiren() + control.getBombengefunden()));

				try
				{
					Thread.sleep(speed);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				gotOne = true;
			}
			else if (value.getProzent() == -100)
			{
				Platform.runLater(() -> value.zeigen(false, false));

				if (value.getSpeicherText().equals("0"))
				{
					Platform.runLater(() -> control.zeigeumligend(value.getX(), value.getY()));
				}

				if (value.getBombe())
				{
					running = false;
					control.verloren();
				}

				try
				{
					Thread.sleep(speed);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				gotOne = true;

			}

		}

		return gotOne;
	}

	private boolean starteRaten(Feld value)
	{
		int felderOffen = 9;
		int felderMakirt = 0;


		Feld feldMax = null;
		Feld feldMin = null;


		int feldID = width * value.getX() + value.getY();

		if (value.isAufgedekt() && !value.getSpeicherText().equals("X") && !value.getSpeicherText().equals("0"))
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

						if (!feld.get(feldID + i + j).getMakirt() && !feld.get(feldID + i + j).isAufgedekt() && !feld.get(feldID + i + j).isGoastMarkirt() && feld.get(feldID + i + j).getProzent() != 0 && feld.get(feldID + i + j).getProzent() != -100)
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

					if (felderOffen == 1)
					{
						return true;
					}
				}
			}

			if (bombenToFind - felderMakirt == 1 && feldMax != null && feldMin != null)
			{
				if (feldMax.getProzent() > feldMin.getProzent())
				{
					feldMax.setGoastMarkirt(true);
				}
			}
		}
		return false;
	}

	private boolean lookForError(Feld value)
	{
		int feldID = width * value.getX() + value.getY();
		int bombenGefunden = 0;
		int bombenToFind;

		if (value.isAufgedekt() && !value.getSpeicherText().equals("X") && !value.getSpeicherText().equals("0"))
		{
			bombenToFind = Integer.parseInt(value.getSpeicherText());

			for (int i = -1; i < 2; i++)
			{
				for (int j = -1 * width; j <= width; j += width)
				{
					if (feldID + i + j >= 0 && feldID + i + j < height * width && !(feldID % width == 0 && i == -1) && !(feldID % width == width - 1 && i == 1))
					{
						if (feld.get(feldID + i + j).getMakirt())
						{
							bombenGefunden++;
						}
					}
				}
			}

			return bombenGefunden > bombenToFind;
		}


		return false;
	}

	@Override
	public void run()
	{
		round = 0;
		running = true;
		forceClose = false;
		int counter = 0;
		int max = width * height * 2;

		while (running && !forceClose && counter <= max)
		{
			nextSolve();
			if (isDebug)
			{
				System.out.println("--" + counter);
			}
			counter++;
			control.gewinnPruefung();
			if (!forceClose && pruefeFelderIfNoneLeft() && control.getBombengefunden() != control.getAnzahlBombenGesamt() || control.getBombengefunden() < 0)
			{
				running = true;
				if (isDebug)
				{
					System.out.println("----------------------");
				}
				resetMarirung();
			}
			if (forceClose && isDebug)
			{
				System.out.println("closethatnow");
			}
			if (control.isFertig() && isDebug)
			{
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			}
		}
	}

	void forceColose()
	{
		forceClose = true;
	}

	private boolean pruefeFelderIfNoneLeft()
	{
		for (Feld value : feld)
		{
			if (!value.getMakirt() && !value.isAufgedekt() && !value.isGoastMarkirt())
			{
				return false;
			}
		}

		return true;
	}

	private void resetMarirung()
	{
		for (Feld value : feld)
		{
			if (value.getMakirt())
			{
				Platform.runLater(value::makiren);
			}
			value.setGoastMarkirt(false);
		}
		control.setBombengefunden(0);
	}

	void endRunning()
	{
		this.running = false;
	}
}
