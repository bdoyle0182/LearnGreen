package panels;
import game.Game;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import main.RecycleApplet;

/*
 * MasterPanel handles all of our panels in a
 * CardLayout. Game is also a part of MasterPanel,
 * but is created and added in the Play button listener.
 */
public class MasterPanel extends JPanel {

	public static final Color BACKGROUND_COLOR = new Color(0xC3FDB8);
	
	public static final int SCREEN_WIDTH = (int) RecycleApplet.screenDimension.getWidth();
	public static final int SCREEN_HEIGHT = (int) RecycleApplet.screenDimension.getHeight();

	
	// logo size constants
	public static final Dimension SMALL_LOGO_DIMENSIONS = new Dimension(75, 75);
	
	private String currentPlayerName;
	
	private OpeningScreenPanel osPanel;
	private Game gPanel;
	private HighScorePanel hsPanel;
	private HelpScreenPanel hPanel;
	private QuestionScreenPanel qPanel;
	
	/*
	 * Master Panel constructor creates a CardLayout
	 * for all of our panels and initializes the GUI.
	 */
	public MasterPanel() 
	{
		this.setLayout(new CardLayout());
		initGUI();
	}
	
	/*
	 * iniGUI creates all of our panels except the Game
	 * Panel, which is creates when play is pressed in
	 * OpeningScreenPanel. The panels are also added to
	 * MasterPanel and its CardLayout.
	 */
	public void initGUI() 
	{
		osPanel = new OpeningScreenPanel(this); 
		hsPanel = new HighScorePanel(this);
		hPanel = new HelpScreenPanel(this);
		qPanel = new QuestionScreenPanel(this);
		
	    add(osPanel, "main");
	    add(hsPanel, "highscores");
	    add(hPanel, "help");
	    add(qPanel, "question");
	}
	
	//Returns current player's name
	public String getCurrentPlayerName() 
	{
		return currentPlayerName;
	}
	
	//Sets current player's name
	public void setCurrentPlayerName(String nameInput) 
	{
		currentPlayerName = nameInput;
	}
	
	//Returns current game being played
	public Game getGame() 
	{
		return gPanel;
	}
	
	//Sets the current game being played
	public void setGame(Game myGame) 
	{
		gPanel = myGame;
	}
	
	//Returns the question panel
	public QuestionScreenPanel getQPanel()
	{
		return this.qPanel;
	}

	// Returns the high score panel
	public HighScorePanel getHsPanel() {
		return hsPanel;
	}

	// Sets high score panel
	public void setHsPanel(HighScorePanel myHighScores) {
		hsPanel = myHighScores;
	}
};

