package panels;
import game.Player;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.RecycleApplet;

/*
 * This class is responsible for displaying the high scores for the game
 */
public class HighScorePanel extends JPanel 
{
    private static final long serialVersionUID = 1L;
    
    //Data
    private Vector<Player> highScoreTable;

    BufferedImage logo; 
    
    public JLabel headerLabel;
    private String highScoreText = "";
    private MasterPanel cardHandler;
    public JButton backButton;
    public JTextArea highScoreList;
    
    /*
     * This constructor initializes the panel
     */
    public HighScorePanel(MasterPanel cards) 
    {
        this.cardHandler = cards;
        highScoreTable = new Vector<Player>();
        initGUI();
    }
    
    /*
     * This function is responsible for configuring all of the display elements on the screen
     */
    public void initGUI()
    {

    	// set background and layout
    	this.setBackground(MasterPanel.BACKGROUND_COLOR);
        this.setLayout(null);
          
        //logo
        try 
        {
            logo = ImageIO.read(new File("logo.png"));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }    
        
        //Button Generics
        int buttonRowCoord = MasterPanel.SCREEN_HEIGHT / 6 * 5;
        int buttonLength = MasterPanel.SCREEN_WIDTH / 5;
        int buttonHeight = 20;
        
        //Back button
        JButton backButton = new JButton("Back");
        int backButtonXCoord = MasterPanel.SCREEN_WIDTH / 2 - buttonLength / 2;
        backButton.setLocation(backButtonXCoord, buttonRowCoord);
        backButton.setSize(buttonLength, buttonHeight);
        backButton.addActionListener(new backButtonListener());
        this.add(backButton);
     
        //Header Label
        headerLabel = new JLabel("~ High-Scores ~");
        headerLabel.setFont(new Font("Times New Roman", Font.PLAIN, 36));
        headerLabel.setForeground(Color.BLACK);
        int headerXCoord = MasterPanel.SCREEN_WIDTH/2 - 125;
        int headerYCoord = 80;
        headerLabel.setSize(300, 80);
        headerLabel.setLocation(headerXCoord, headerYCoord);
        this.add(headerLabel);
        
        //Text Field
        highScoreList = new JTextArea("test");
        highScoreList.setEditable(false);
        highScoreList.setBackground(MasterPanel.BACKGROUND_COLOR);
        highScoreList.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        int textSizeX = 300;
        int textSizeY = 200;
        highScoreList.setSize(textSizeX, textSizeY);
        highScoreList.setLocation(headerXCoord, headerYCoord + 70);
        this.add(highScoreList);
        
        //font
        try 
        {
             Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("typogarden.ttf")).deriveFont(60f);
             GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
             ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("RESURREC.TTF")));
             headerLabel.setFont(customFont);
        }
        catch (IOException e) 
        {
             e.printStackTrace();
        }
        catch (FontFormatException e)
        {
             e.printStackTrace();
        }
        
        //load data from file
        populateListFromFile();
        
        //display text
        updateHighScoreText();
        
        this.setVisible(true);
    }
    
    /*
     * This function insert the player into the highscores list if the player's score is greater than
     * one of the players in the list, also dynamically reordering them as appropriate.
     */
    public void updateListWithNewEntry(Player testPlayer) 
    {
        boolean updateFileRequired = false;
        
        for (int index = 0; index < highScoreTable.size(); index++) 
        {
            Player tempPlayer = testPlayer;
            
            if ( tempPlayer.getScore() > highScoreTable.get(index).getScore() 
            		|| updateFileRequired )
            {
                tempPlayer = highScoreTable.get(index); //save old player in var
                highScoreTable.set(index, testPlayer);  //insert player in the given position
                testPlayer = tempPlayer; 
                updateFileRequired = true; 
            }
        }
        
        if (updateFileRequired) 
        {
            updateFileFromList();
        }
    }
    
    /*
     * Responsible for getting the names and scores of the player and placing them
     * in the text that is displayed.
     */
    public void updateHighScoreText() 
    {
        String finalText = "    Name \tScore";
        
        for (int i = 0; i < highScoreTable.size(); i++) 
        {
            finalText = finalText + "\n" + (i+1) + ")  " + highScoreTable.get(i).getName() 
                    + "\t    " + highScoreTable.get(i).getScore();
        }
        
        highScoreText = finalText;
        highScoreList.setText(highScoreText);
    }
    
    /*
     * This function rewrites the file containing highscore data from the updated
     * arraylist after entries are updated
     */
    public void updateFileFromList()
    {
        BufferedWriter bufferedWriter = null;
        String line = "";
        try 
        {
            bufferedWriter = new BufferedWriter(new FileWriter("test.txt"));
            
            //write each line
            for (int i = 0; i < highScoreTable.size(); i++) 
            {
                line = highScoreTable.get(i).getName() + " " + highScoreTable.get(i).getScore();
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            
            //clear and close buffered writer
            bufferedWriter.flush();
            bufferedWriter.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
    }
    
    /*
     * This function is responsible for populating the highscore list with the values stored in a text file
     */
    public void populateListFromFile()
    {
        BufferedReader bufferedReader = null;
        try 
        {
            bufferedReader = new BufferedReader(new FileReader("test.txt"));
            String line = "";
            highScoreTable.clear();
            while ((line = bufferedReader.readLine()) != null) 
            {
                String delims = "[ ]+";
                String[] population = line.split(delims);
                Player tempPlayer = new Player(population[0], Integer.parseInt(population[1]));
                highScoreTable.add(tempPlayer);
            } 
        } 
        catch (IOException e) 
        {
            
            e.printStackTrace();
        }
    }
    
    /*
     * This class is responsible for printing out the list of players for debugging purposes.
     */
    public void printList() 
    {
        for (Player currentPlayer : highScoreTable) 
        {
            System.out.println(currentPlayer.getName() + ": " + currentPlayer.getScore());
        }
    }
    
    /*
     * Repaints the logo on the screen
     * (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        int xLoc = (int) ((MasterPanel.SCREEN_WIDTH / 2) - (MasterPanel.SMALL_LOGO_DIMENSIONS.getWidth() / 2));
        int yLoc = 20;
        g.drawImage(logo, xLoc, yLoc,
        			(int) MasterPanel.SMALL_LOGO_DIMENSIONS.getWidth(),
        			(int) MasterPanel.SMALL_LOGO_DIMENSIONS.getHeight(), null);
    }
    
    /*
     * This class is responsible for bringing the player back to the main menu
     */
    class backButtonListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent arg0) 
        {
            populateListFromFile();
            updateHighScoreText();
            CardLayout cl = (CardLayout)(cardHandler.getLayout());
            cl.show(cardHandler, "main");            
        }
    }
    
}