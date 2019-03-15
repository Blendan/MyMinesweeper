package minesweeper;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Feld extends Button
{
	private int x, y;
	private String speicherText = "";


	private String styleBombe = "feld-mine";
	private String styleBombeNotFound = "feld-mine-notFound";
	private String styleNormal = "feld-blank";
	private String styleNumber = "feld-green";
	private String styleMarked = "feld-flag";
	private String styleWrong = "feld-wrong";

	private int prozent = 0;
	private int bombenOffen = 0;
	private int felderOffen = 0;

	private boolean goastMarkirt = false;


	private boolean bombe = false;
	private boolean makirt = false;
	private boolean aufgedekt = false;
	private boolean maybeBomb = false;
	private boolean save = false;

	Feld(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.setText("");
		this.setStyle(styleNormal);
		this.setTextFill(Color.BLACK);

		this.setAlignment(Pos.CENTER);

		setClass(styleNormal);

	}

	void showProzent()
	{
		if (!makirt && !aufgedekt)
		{
			if (goastMarkirt)
			{
				this.setText("X");
			}
			else if (prozent == -100)
			{
				this.setText("0");
			}
			else if (prozent == 0)
			{
				this.setText("?");
			}
			else
			{
				this.setText(prozent + "");
			}
		}
		prozent = 0;
		goastMarkirt = false;
	}

	void addProzent(int plus)
	{
		if (goastMarkirt)
		{
			prozent = 100;
		}
		else if (prozent != -100)
		{
			if (plus == -100)
			{
				prozent = -100;
			}
			else
			{
				prozent += plus;
			}
		}
	}

	private void setClass(String style)
	{
		this.getStyleClass().clear();
		this.getStyleClass().add("feld");
		this.getStyleClass().add(style);
	}

	void setBackgroundSize(int size)
	{
		this.setStyle("-fx-background-size: " + size + " " + size);
	}

	boolean IstFeld(int x, int y)
	{
		return this.x == x && this.y == y;
	}

	//--------------------------------

	int getX()
	{
		return x;
	}

	int getY()
	{
		return y;
	}

	String getSpeicherText()
	{
		return speicherText;
	}

	boolean getBombe()
	{
		return bombe;
	}

	boolean getMakirt()
	{
		return makirt;
	}

	//--------------------------------

	void setBombe(boolean bombe)
	{
		this.bombe = bombe;
	}

	void setSpeicherText(String speicherText)
	{
		this.speicherText = speicherText;
		if (!speicherText.equals("X"))
		{
			styleNumber = "feld-" + speicherText;
		}
	}

	void zeigen(boolean isFinal, boolean isWinn)
	{
		this.setDisable(true);
		this.setText("");
		aufgedekt = true;
		if (bombe && isFinal)
		{
			if (!makirt && !isWinn)
			{
				this.setClass(styleBombeNotFound);
			}
			else
			{
				this.setClass(styleBombe);
			}
		}
		else if (isFinal)
		{
			if (makirt && !isWinn)
			{
				this.setClass(styleWrong);
			}
			else
			{
				this.setClass(styleNumber);
			}
		}
		else
		{
			this.setClass(styleNumber);
		}
	}

	int makiren()
	{
		if (!aufgedekt)
		{
			if (makirt)
			{
				makirt = false;
				this.setClass(styleNormal);

				if (bombe)
				{
					return -1;
				}
				else
				{
					return 1;
				}
			}
			else
			{
				this.setText("");
				makirt = true;
				this.setClass(styleMarked);


				if (bombe)
				{
					return 1;
				}
				else
				{
					return -1;
				}
			}
		}

		return 0;
	}

	boolean isAufgedekt()
	{
		return aufgedekt;
	}


	void hideProzent()
	{
		this.setText("");
	}

	boolean isGoastMarkirt()
	{
		return goastMarkirt;
	}

	void setGoastMarkirt(boolean goastMarkirt)
	{
		this.goastMarkirt = goastMarkirt;
	}

	int getProzent()
	{
		return prozent;
	}

	public void setBombenOffen(int bombenOffen)
	{
		this.bombenOffen = bombenOffen;
	}

	public int getBombenOffen()
	{
		return bombenOffen;
	}

	public int getFelderOffen()
	{
		return felderOffen;
	}

	public void setFelderOffen(int felderOffen)
	{
		this.felderOffen = felderOffen;
	}

	public boolean isMaybeBomb()
	{
		return maybeBomb;
	}

	public void setMaybeBomb(boolean maybeBomb)
	{
		this.maybeBomb = maybeBomb;
	}

	public void setSave(boolean save)
	{
		this.save = save;
	}

	public boolean getSave()
	{
		return save;
	}
}
