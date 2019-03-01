package minesweeper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Control implements Initializable
{
	@FXML
	private Button btnStart;

	@FXML
	private TextField textAreaHeight;

	@FXML
	private TextField textAreaWidth;

	@FXML
	private TextField textAreaCount;

	@FXML
	private BorderPane mainPane;

	//-------------------------------------

	private ArrayList<Feld> feld;
	private int height;
	private int width;
	private int bombengefunden;
	private int felderAufgedeckt;
	private int bombenZaehler;
	private int anzahlBombenGesamt;
	private GridPane gridBoxMinen;


	public Control()
	{
		feld = new ArrayList<>();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		btnStart.setOnAction((event)->{startGame();});
	}

	private void startGame()
	{

		boolean weiter = true;
		anzahlBombenGesamt = 0;
		try
		{
			height = Integer.parseInt(textAreaHeight.getText());
			width = Integer.parseInt(textAreaWidth.getText());
			anzahlBombenGesamt = Integer.parseInt(textAreaCount.getText());

		}
		catch (Exception e)
		{
			weiter = false;
			e.printStackTrace();
		}

		if(height>0&&width>0 &&anzahlBombenGesamt>0 && anzahlBombenGesamt<height*width && weiter)
		{
			mainPane.setBottom(null);

			felderAufgedeckt = 0;
			bombengefunden = 0;
			gridBoxMinen = new GridPane();
			feld = new ArrayList<>();

			gridBoxMinen.heightProperty().addListener((e)->{scaleFeld();});
			gridBoxMinen.widthProperty().addListener((e)->{scaleFeld();});

			RowConstraints rc;
			ColumnConstraints cc;

			for (int i = 0; i < height; i ++)
			{
				rc = new RowConstraints();
				rc.setValignment(VPos.BASELINE);

				gridBoxMinen.getRowConstraints().add(rc);
				for (int j = 0; j < width; j++)
				{
					cc = new ColumnConstraints();

					gridBoxMinen.getColumnConstraints().add(cc);
				}
			}

			int index = 0;
			int size = getFeldSize();

			for (int i = 0; i < height; i ++)
			{
				for (int j = 0; j < width; j ++)
				{
					feld.add(new Feld(i,j));

					feld.get(index).setOnMouseClicked((e)->{
						Feld temp = (Feld)e.getSource();

						if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 1)
						{
							bombengefunden += temp.makiren();
						}
						else if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1)
						{
							if(!temp.getMakirt())
							{
								if(temp.getBombe())
								{
									verloren();
								}
								else
								{
									temp.zeigen();
									if(temp.getSpeicherText().equals("0"))
									{
										zeigeumligend(temp.getX(),temp.getY());
									}
									felderAufgedeckt ++;
								}
							}

						}

						gewinnPruefung();
					});

					feld.get(index).setPrefHeight(size);
					feld.get(index).setPrefWidth(size);

					gridBoxMinen.add(feld.get(index),j,i);

					//System.out.println(i+" "+j+" "+index); // DEBUG

					index ++;
				}
			}

			int zufall;

			for(int i = 0; i < anzahlBombenGesamt;i++)
			{
				zufall = (int)(Math.random() * feld.size());

				if(feld.get(zufall).getBombe())
				{
					i --;
				}
				else
				{
					feld.get(zufall).setBombe(true);
				}
			}

			for(int i = 0;i<feld.size();i++)
			{
				bombenZaehler = 0;

				if (!feld.get(i).getBombe())
				{
					int tempX = feld.get(i).getX();
					int tempY = feld.get(i).getY();

					for (int j = 0; j < feld.size(); j++)
					{
						for (int k = -1; k < 2; k++)
						{
							for (int l = -1; l < 2; l++)
							{
								if (feld.get(j).IstFeld(tempX + k, tempY + l))
								{
									if (feld.get(j).getBombe())
									{
										bombenZaehler++;
									}
								}
							}
						}
					}

					feld.get(i).setSpeicherText(Integer.toString(bombenZaehler));
				}
				else
				{
					feld.get(i).setSpeicherText("X");
				}


				gridBoxMinen.setVisible(true);
				gridBoxMinen.setAlignment(Pos.CENTER);
				mainPane.setCenter(gridBoxMinen);
			}
		}
	}

	private int getFeldSize()
	{
		if(height==width)
		{
			if(gridBoxMinen.getHeight()>gridBoxMinen.getWidth())
			{
				return (int)gridBoxMinen.getWidth() / width;
			}
			else
			{
				return (int)gridBoxMinen.getHeight() / width;
			}
		}
		else  if (height > width*(gridBoxMinen.getHeight()/gridBoxMinen.getWidth()))
		{
			return (int)gridBoxMinen.getHeight()-70 /height;
		}
		else
		{
			return (int)gridBoxMinen.getWidth() / width;
		}
	}

	private void scaleFeld()
	{
		int size = getFeldSize();

		for (Feld value: feld)
		{
			value.setPrefSize(size,size);
		}
	}


	private void gewinnPruefung()
	{
		if(bombengefunden == anzahlBombenGesamt)
		{
			aufdeken();
			Label temp = new Label("Gewonnen");
			temp.setFont(new Font(80));
			temp.setAlignment(Pos.CENTER);
			temp.setTextAlignment(TextAlignment.CENTER);
			temp.setPrefWidth(10000);
			temp.setTextFill(Color.BLACK);
			mainPane.setBottom(temp);
			mainPane.getBottom().setStyle("-fx-background-color: green");
		}
	}

	private void verloren()
	{
		aufdeken();
		Label temp = new Label("Verloren");
		temp.setFont(new Font(80));
		temp.setTextAlignment(TextAlignment.CENTER);
		temp.setPrefWidth(10000);
		temp.setTextFill(Color.BLACK);
		mainPane.setBottom(temp);
		temp.setAlignment(Pos.CENTER);
		mainPane.getBottom().setStyle("-fx-background-color: red");
	}

	private void aufdeken()
	{
		for(int i = 0; i < feld.size(); i ++)
		{
			feld.get(i).zeigen();
		}
	}

	// last es blos nicht frei!
	/*
	private void zeigeumligend(int x, int y)
	{

		for(int i = 0; i < feld.size(); i++)
		{
			for(int j = -1; j < 2; j+=2)
			{
				if(feld.get(i).IstFeld(x+j,y))
				{

					if(feld.get(i).getSpeicherText().equals("0")&&feld.get(i).getText().equals("")&&!feld.get(i).getMakirt())
					{
						feld.get(i).zeigen();
						zeigeumligend(feld.get(i).getX(),feld.get(i).getY());
						felderAufgedeckt ++;
					}
					else if(!feld.get(i).getSpeicherText().equals("X")&&feld.get(i).getText().equals("")&&!feld.get(i).getMakirt())
					{
						feld.get(i).zeigen();
						felderAufgedeckt ++;
					}
				}

				if(feld.get(i).IstFeld(x,y+j))
				{

					if(feld.get(i).getSpeicherText().equals("0")&&feld.get(i).getText().equals("")&&!feld.get(i).getMakirt())
					{
						feld.get(i).zeigen();
						zeigeumligend(feld.get(i).getX(),feld.get(i).getY());
						felderAufgedeckt ++;
					}
					else if(!feld.get(i).getSpeicherText().equals("X")&&feld.get(i).getText().equals("")&&!feld.get(i).getMakirt())
					{
						feld.get(i).zeigen();
						felderAufgedeckt ++;
					}
				}


				if(i!=feld.size()-1)
				{
					if (feld.get(i + 1).IstFeld(x + 1, y + j))
					{
						if (!feld.get(i + 1).getSpeicherText().equals("X") && !feld.get(i + 1).getSpeicherText().equals("0") && feld.get(i + 1).getText().equals("") && !feld.get(i + 1).getMakirt())
						{
							feld.get(i + 1).zeigen();
							felderAufgedeckt++;
						}
						else if (!feld.get(i + 1).getSpeicherText().equals("X") && feld.get(i + 1).getSpeicherText().equals("0") && feld.get(i + 1).getText().equals("") && !feld.get(i + 1).getMakirt())
						{
							feld.get(i + 1).zeigen();
							zeigeumligend(feld.get(i + 1).getX(),feld.get(i).getY());
							felderAufgedeckt++;
						}
					}
				}

				if(i!=0)
				{
					if (feld.get(i - 1).IstFeld(x - 1, y + j))
					{
						if (!feld.get(i - 1).getSpeicherText().equals("X") && !feld.get(i - 1).getSpeicherText().equals("0") && feld.get(i - 1).getText().equals("") && !feld.get(i - 1).getMakirt())
						{
							feld.get(i - 1).zeigen();
							felderAufgedeckt++;
						}
						else if (!feld.get(i - 1).getSpeicherText().equals("X") && feld.get(i + 1).getSpeicherText().equals("0") && feld.get(i - 1).getText().equals("") && !feld.get(i - 1).getMakirt())
						{
							feld.get(i - 1).zeigen();
							zeigeumligend(feld.get(i- 1).getX(),feld.get(i).getY());
							felderAufgedeckt++;
						}
					}
				}
			}
		}
	}
	*/

	//mehr performance whniger bugs
	private void zeigeumligend(int x, int y)
	{
		int feldID = width*x + y;
		//System.out.println(feldID +"|"+x+"|"+y); //DEBUG

		for (int i = -1; i < 2; i++)
		{
			for (int j = -1*width; j <= width; j += width)
			{
				if (feldID + i+j >= 0 && feldID + i+j < height * width && !(feldID%width == 0 && i == -1) && !(feldID%width == width-1 && i == 1))
				{
					if (!feld.get(feldID + i+j).getMakirt() && !feld.get(feldID + i+j).isAufgedekt())
					{
						if (feld.get(feldID + i+j).getSpeicherText().equals("0"))
						{
							feld.get(feldID + i+j).zeigen();
							zeigeumligend(feld.get(feldID + i+j).getX(), feld.get(feldID + i+j).getY());
							felderAufgedeckt++;
						}
						else if (!feld.get(feldID + i+j).getSpeicherText().equals("X"))
						{
							feld.get(feldID + i+j).zeigen();
							felderAufgedeckt++;
						}

					}
				}
			}
		}
	}
}
