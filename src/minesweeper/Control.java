package minesweeper;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
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
	private Button btnLittleHelper;

	@FXML
	private Button btnLittleSolver;

	@FXML
	private TextField textAreaHeight;

	@FXML
	private TextField textAreaWidth;

	@FXML
	private TextField textAreaCount;

	@FXML
	private BorderPane mainPane;

	@FXML
	private Label lblTimer;

	//-------------------------------------

	private ArrayList<Feld> feld;
	private int height;
	private int width;
	private int bombengefunden;
	private int anzahlBombenGesamt;
	private GridPane gridBoxMinen;

	private LittleHelper littleHelper;
	private LittleSolver littleSolver;

	private boolean isAktiveLittleSolver = false;
	private boolean isAktiveLittleHelper = false;

	private  boolean fertit = false;

	private boolean timerIsRuning = false;

	private Timer timer;

	public Control()
	{
		feld = new ArrayList<>();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		timer = new Timer(lblTimer);

		btnStart.setOnAction((event)->startGame());
		btnLittleHelper.setOnAction((event -> tootleLittleHelper()));
		btnLittleSolver.setOnAction((event -> tootleLittleSolver()));
	}

	private void stardTimer()
	{
		if(!timerIsRuning)
		{
			timer.setRunning(true);
			new Thread(timer).start();
			timerIsRuning = true;
		}
	}

	void stopTimer()
	{
		timer.setRunning(false);
		timerIsRuning = false;
	}

	private void tootleLittleSolver()
	{
		if(isAktiveLittleSolver)
		{
			isAktiveLittleSolver = false;

			if(littleSolver!=null)
			{
				littleSolver.setRunning(false);
			}

			if(feld!=null)
			{
				for (Feld value: feld)
				{
					value.hideProzent();
				}
			}
		}
		else
		{
			if(littleHelper!=null)
			{
				littleHelper.startHelp();
			}

			isAktiveLittleSolver = true;
		}
	}

	private void tootleLittleHelper()
	{
		if(isAktiveLittleHelper)
		{
			isAktiveLittleHelper = false;
			if(feld!=null)
			{
				for (Feld value: feld)
				{
					value.hideProzent();
				}
			}
		}
		else
		{
			if(littleHelper!=null)
			{
				littleHelper.startHelp();
			}

			isAktiveLittleHelper = true;
		}
	}

	private void startGame()
	{
		boolean weiter = true;
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
			fertit = false;
			stopTimer();

			mainPane.setBottom(null);

			bombengefunden = 0;
			gridBoxMinen = new GridPane();
			feld = new ArrayList<>();

			if(littleSolver!=null)
			{
				littleSolver.setRunning(false);
			}

			littleHelper = new LittleHelper(feld,width,height);
			littleSolver = new LittleSolver(feld,width,height,this);

			gridBoxMinen.heightProperty().addListener((e)-> new Thread(()-> Platform.runLater(()->scaleFeld())).start());
			gridBoxMinen.widthProperty().addListener((e)-> new Thread(()-> Platform.runLater(()->scaleFeld())).start());

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

			for (int i = 0; i < height; i ++)
			{
				for (int j = 0; j < width; j ++)
				{
					feld.add(new Feld(i,j));

					feld.get(index).setOnMouseClicked((e) ->
					{
						Feld temp = (Feld) e.getSource();


						if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 1)
						{
							bombengefunden += temp.makiren();
						}
						else if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1)
						{
							if (!temp.getMakirt())
							{
								if (temp.getBombe())
								{
									verloren();
								}
								else
								{
									temp.zeigen(false);
									if (temp.getSpeicherText().equals("0"))
									{
										zeigeumligend(temp.getX(), temp.getY());
									}

									if (isAktiveLittleSolver)
									{

										littleSolver.setRunning(false);
										new Thread(littleSolver).start();
									}

								}

							}

						}

						if (isAktiveLittleHelper)
						{
							littleHelper.startHelp();
						}

						gewinnPruefung();
					});

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
				int bombenZaehler = 0;

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

			}

			gridBoxMinen.setVisible(true);
			gridBoxMinen.setAlignment(Pos.CENTER);
			scaleFeld();
			mainPane.setCenter(gridBoxMinen);

			stardTimer();


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
		else  if (gridBoxMinen.getHeight() > gridBoxMinen.getWidth()*((double) height/(double)width))
		{

			return (int)gridBoxMinen.getWidth() /width;
		}
		else
		{

			return (int)gridBoxMinen.getHeight() / height;
		}

	}

	private void scaleFeld()
	{
		int size = getFeldSize();

		for (Feld value: feld)
		{
			value.setPrefSize(size,size);
			value.setBackgroundSize(size);
		}
	}


	void gewinnPruefung()
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

	void verloren()
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
		fertit = true;
		stopTimer();
		for (Feld feld1 : feld)
		{
			feld1.zeigen(true);
		}
	}

	//mehr performance whniger bugs
	void zeigeumligend(int x, int y)
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
							feld.get(feldID + i+j).zeigen(false);
							zeigeumligend(feld.get(feldID + i+j).getX(), feld.get(feldID + i+j).getY());
						}
						else if (!feld.get(feldID + i+j).getSpeicherText().equals("X"))
						{
							feld.get(feldID + i+j).zeigen(false);
						}

					}
				}
			}
		}
	}

	LittleSolver getLittleSolver()
	{
		return littleSolver;
	}

	int getBombengefunden()
	{
		return bombengefunden;
	}

	public int getAnzahlBombenGesamt()
	{
		return anzahlBombenGesamt;
	}

	void setBombengefunden(int bombengefunden)
	{
		this.bombengefunden = bombengefunden;
	}

	public boolean isFertit()
	{
		return fertit;
	}
}
