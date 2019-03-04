package minesweeper;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Feld extends Button
{
	private int x,y;
	private String speicherText = "";


	private String styleBombe = "feld-mine";
	private String styleBombeNotFound = "feld-mine-notFound";
	private String styleNormal = "feld-blank";
	private String styleNumber = "feld-green";
	private String styleMarked = "feld-flag";
	private String styleWrong = "feld-wrong";

	private int prozent = 0;

	private boolean goastMarkirt = false;


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

		this.setAlignment(Pos.CENTER);

		setClass(styleNormal);

	}

	public void showProzent()
	{
		if(!makirt&&!aufgedekt)
		{
			if(goastMarkirt)
			{
				this.setText("100");
			}
			else if(prozent==-100)
			{
				this.setText("0");
			}
			else if(prozent==0)
			{
				this.setText("?");
			}
			else
			{
				this.setText(prozent+"");
			}
		}
		prozent = 0;
		goastMarkirt = false;
	}

	public void addProzent(int plus)
	{
		if(goastMarkirt)
		{
			prozent = 100;
		}
		else if(prozent != -100)
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
			styleNumber = "feld-"+speicherText;
		}
	}

	public void zeigen(boolean isFinal)
	{
		this.setDisable(true);
		this.setText("");
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
		if(!aufgedekt)
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

	public boolean isAufgedekt()
	{
		return aufgedekt;
	}


	public void hideProzent()
	{
		this.setText("");
	}

	public boolean isGoastMarkirt()
	{
		return goastMarkirt;
	}

	public void setGoastMarkirt(boolean goastMarkirt)
	{
		this.goastMarkirt = goastMarkirt;
	}

	public int getProzent()
	{
		return prozent;
	}
}
