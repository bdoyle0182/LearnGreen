package panels;
import game.Game;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.RecycleApplet;

/*
 * The OpeningScreenPanel class creates and formats
 * our opening screen panel. You can move to help or 
 * high scores panels from this panel as well as play 
 * the game using the help, high scores, and play
 * buttons.
 */
public class OpeningScreenPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	BufferedImage logo;
	JLabel titleHeader;
	JButton playGameButton;
	JButton helpButton;
	JTextField playerNameField;
	
	private Color backgroundColor = new Color(0xC3FDB8);

	private Dimension logoDim = new Dimension(175, 175);
	private MasterPanel cardHandler;
	private String playerName = "";
	
	/*
	 * Constructor passes the cards from the MasterPanel
	 * into the cardHandler and initializes the GUI.
	 */
	public OpeningScreenPanel(MasterPanel cards) 
	{
		this.cardHandler = cards;
		initGUI();
	}
	
	/*
	 * initGui method sets the formatting of the OpeningScreenPanel,
	 * adds the game logo, and creates/adds all of the buttons to the
	 * panel.
	 */
	public void initGUI() 
	{
	  	this.setBackground(backgroundColor);
	  	this.setLayout(null);
	  	
	  	//Reads Logo file
        try 
        {
			logo = ImageIO.read(new File("logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        //Button Generics
        int buttonRowCoord = (int) RecycleApplet.screenDimension.getHeight() / 6 * 5;
        int buttonLength = (int) RecycleApplet.screenDimension.getWidth() / 5;
        int buttonHeight = 20;
        int buttonBorderBuffer = 20;
        
        //Play button
        JButton playButton = new JButton("Play");
        int playButtonXCoord = (int) RecycleApplet.screenDimension.getWidth() - buttonLength - buttonBorderBuffer;
        playButton.setSize(buttonLength, buttonHeight);
        playButton.setLocation(playButtonXCoord, buttonRowCoord);
        playButton.addActionListener(new PlayGameButtonListener());
        this.add(playButton);
        
        //Text Field
        playerNameField = new JTextField("Guest");
        playerNameField.setSize(buttonLength, buttonHeight);
        playerNameField.setLocation(playButtonXCoord, (buttonRowCoord-30));
        this.add(playerNameField);
        
        //Help button        
        JButton helpButton = new JButton("Help");   
        helpButton.setLocation(buttonBorderBuffer, buttonRowCoord);
        helpButton.setSize(buttonLength, buttonHeight);
        helpButton.addActionListener(new HelpButtonListener());
        this.add(helpButton);
        
        //Highscore button
        JButton hiscoresButton = new JButton("Highscores");
        int hiscoresButtonXCoord = (int) RecycleApplet.screenDimension.getWidth() / 2 - buttonLength / 2;
        hiscoresButton.setLocation(hiscoresButtonXCoord, buttonRowCoord);
        hiscoresButton.setSize(buttonLength, buttonHeight);
        hiscoresButton.addActionListener(new HighScoreButtonListener());
        this.add(hiscoresButton);
        
		this.setVisible(true);
	}
	
	/*
	 * PlayGameButtonListener performs handles the events
	 * that occur when the Play Button is pressed. The Game
	 * Panel is created and added to the master panel. The
	 * player's name is also set.
	 */
	class PlayGameButtonListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			playerName = playerNameField.getText();
			cardHandler.setCurrentPlayerName(playerName);
			
			Game gPanel = new Game(cardHandler);
			gPanel.setGamePaused(false);
			cardHandler.setGame(gPanel);
			
            CardLayout cl = (CardLayout)(cardHandler.getLayout());
            cardHandler.add(gPanel, "game");
            cl.show(cardHandler, "game");
		}
	}
	
	/*
	 * HelpButtonListener handles the events when the Help
	 * Button is pressed. The help button takes you to 
	 * the help panel from the card layout.
	 */
	class HelpButtonListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			CardLayout cl = (CardLayout)(cardHandler.getLayout());
            cl.show(cardHandler, "help");			
		}
	}
	
	/*
	 * HelpButtonListener handles the events when the Help
	 * Button is pressed. The help button takes you to 
	 * the help panel from the card layout.
	 */
	class HighScoreButtonListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			CardLayout cl = (CardLayout)(cardHandler.getLayout());
            cl.show(cardHandler, "highscores");			
		}
	}
	
	/*
	 * Method draws the panel with the same dimensions as the
	 * applet.
	 */
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		int xLoc = (int) ((RecycleApplet.screenDimension.getWidth() / 2) - (logoDim.getWidth() / 2));
		int yLoc = 20;
		g.drawImage(logo, xLoc, yLoc, (int) logoDim.getWidth(), (int) logoDim.getHeight(), null);
	}
};