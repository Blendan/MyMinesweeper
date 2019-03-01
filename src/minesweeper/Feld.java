package minesweeper;

import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Feld extends Button
{
	private int x,y;
	String speicherText = "";
	private String styleBombe = "-fx-background-color: red";
	private String styleNormal = "-fx-background-color: gray";
	private String styleNumber = "-fx-background-color: green";
	private String styleMarked = "-fx-background-color: blue";
	private boolean bombe = false;
	private boolean makirt = false;

	public Feld(int x,int y)
	{
		this.x = x;
		this.y = y;
		this.setText("");
		this.setStyle(styleNormal);
		this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
					styleNumber = "-fx-background-color: green";
					break;
				case "1":
					styleNumber = "-fx-background-color: rgb(43, 203, 34)";
					break;
				case "2":
					styleNumber = "-fx-background-color: rgb(103, 203, 33)";
					break;
				case "3":
					styleNumber = "-fx-background-color: rgb(154, 203, 33)";
					break;
				case "4":
					styleNumber = "-fx-background-color: rgb(184, 203, 33)";
					break;
				case "5":
					styleNumber = "-fx-background-color: rgb(203, 201, 33)";
					break;
				case "6":
					styleNumber = "-fx-background-color: rgb(203, 181, 33)";
					break;
				case "7":
					styleNumber = "-fx-background-color: rgb(203, 150, 33)";
					break;
				case "8":
					styleNumber = "-fx-background-color: rgb(203, 99, 33)";
					break;

			}


		}
	}

	public void zeigen()
	{
		this.setText(speicherText);
		this.setDisable(false);
		if(bombe)
		{
			this.setStyle(styleBombe);
		}
		else
		{
			this.setStyle(styleNumber);
		}
	}

	public int makiren()
	{
		if(this.getText().equals(""))
		{
			if (makirt)
			{
				makirt = false;
				this.setStyle(styleNormal);

				if (bombe)
				{
					return -1;
				}
			}
			else
			{
				makirt = true;
				this.setStyle(styleMarked);


				if (bombe)
				{
					return 1;
				}
			}
		}

		return 0;
	}
}
