package minesweeper;

import java.util.ArrayList;

public class LittleHelper
{
	protected ArrayList<Feld> feld;
	protected int width;
	protected int height;

	public LittleHelper(ArrayList<Feld> feld, int width, int height)
	{
		this.feld = feld;
		this.width = width;
		this.height = height;
	}

	void startHelp()
	{
		for (Feld value: feld)
		{
			pruefeUmliegend(value);
		}

		for (Feld value: feld)
		{
			value.showProzent();
		}

	}

	protected boolean pruefeUmliegend(Feld value)
	{
		boolean gotOne = false;
		int felderOffen = 9;
		int felderMakirt = 0;

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
							felderOffen --;
						}

						if(feld.get(feldID + i + j).getMakirt() || feld.get(feldID + i + j).isGoastMarkirt())
						{
							felderMakirt ++;
						}
					}
					else
					{
						felderOffen --;
					}
				}
			}

			//System.out.println(felderMakirt+" | "+bombenToFind+" | "+felderOffen); //DEBUG

			int prozent = 0;

			if(felderMakirt<bombenToFind)
			{
				prozent = (int)(((double) (bombenToFind-felderMakirt)/(double)felderOffen)*100);

			}
			else if(felderMakirt>=bombenToFind)
			{
				prozent = -100;
				gotOne = true;
			}

			for (int i = -1; i < 2; i++)
			{
				for (int j = -1 * width; j <= width; j += width)
				{
					if (feldID + i + j >= 0 && feldID + i + j < height * width && !(feldID % width == 0 && i == -1) && !(feldID % width == width - 1 && i == 1))
					{
						if (!feld.get(feldID + i + j).getMakirt() && !feld.get(feldID + i + j).isAufgedekt() && !feld.get(feldID + i + j).isGoastMarkirt())
						{
							feld.get(feldID + i + j).addProzent(prozent);

							if(prozent==100)
							{
								feld.get(feldID + i + j).setGoastMarkirt(true);
								gotOne = true;
							}
						}

					}
				}
			}

		}
		return gotOne;
	}
}
