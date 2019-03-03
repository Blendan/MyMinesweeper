package minesweeper;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Feld extends Button
{
	private int x,y;
	String speicherText = "";
	private String styleBombe = "feld-mine";
	private String styleBombeNotFound = "feld-mine-notFound";
	private String styleNormal = "feld-blank";
	private String styleNumber = "feld-green";
	private String styleMarked = "feld-flag";
	private String styleWrong = "feld-wrong";
	private boolean bombe = false;
	private boolean makirt = false;
	private boolean aufgedekt = false;

	public Feld(int x,int y)
	{
		this.x = x;
		this.y = y;
		this.setText("");
		this.setStyle(styleNormal);
		this.setTextFill(Color.BLACK);

		setClass(styleNormal);

	}

	private void setClass(String style)
	{
		this.getStyleClass().clear();
		this.getStyleClass().add("feld");
		this.getStyleClass().add(style);
	}

	public void setBackgroundSize(int size)
	{
		this.setStyle("-fx-background-size: "+size+" "+size);
	}

	public boolean IstFeld(int x, int y)
	{
		if(this.x==x&&this.y==y)
		{
			return true;
		}
		return false;
	}

	//--------------------------------

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public String getSpeicherText()
	{
		return speicherText;
	}

	public boolean getBombe()
	{
		return bombe;
	}

	public boolean getMakirt()
	{
		return makirt;
	}

	//--------------------------------

	public void setBombe(boolean bombe)
	{
		this.bombe = bombe;
	}

	public void setSpeicherText(String speicherText)
	{
		this.speicherText = speicherText;
		if(!speicherText.equals("X"))
		{
			switch (speicherText)
			{
				case "0":
					styleNumber = "feld-0";
					break;
				case "1":
					styleNumber = "feld-1";
					break;
				case "2":
					styleNumber = "feld-2";
					break;
				case "3":
					styleNumber = "feld-3";
					break;
				case "4":
					styleNumber = "feld-4";
					break;
				case "5":
					styleNumber = "feld-5";
					break;
				case "6":
					styleNumber = "feld-6";
					break;
				case "7":
					styleNumber = "feld-7";
					break;
				case "8":
					styleNumber = "feld-8";
					break;

			}


		}
	}

	public void zeigen(boolean isFinal)
	{
		this.setDisable(true);
		aufgedekt = true;
		if (bombe)
		{
			if (isFinal&&!makirt)
			{
				this.setClass(styleBombeNotFound);
			}
			else
			{
				this.setClass(styleBombe);
			}
		}
		else
		{
			if(makirt)
			{
				this.setClass(styleWrong);
			}
			else
			{
				this.setClass(styleNumber);
			}
		}
	}

	public int makiren()
	{
		if(this.getText().equals(""))
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

	public boolean isAufgedekt()
	{
		return aufgedekt;
	}
}
