package panels;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import main.RecycleApplet;

/*
 * This class is responsible for displaying the help menu
 */
public class HelpScreenPanel extends JPanel 
{
	//Default for serialization
    private static final long serialVersionUID = 1L;
    
    //images for the logo
    private BufferedImage logo;
    
    //card handler to switch between cards in the cardlayout
    private MasterPanel cardHandler;
    
    //buttons for the screen
    public JButton backButton;
    public JButton helpButton;
    public JButton factsButton;
    
    public JTextArea helpText;
    public JTextArea factsText;
    public JScrollPane helpScroll;
    public JScrollPane factsScroll;
      
    //Used for formatting images
    private boolean helpTextOn = false;
    private boolean factsTextOn = false;
    private final String spacing = "   ";
    
    private final String helpTextString = spacing + " - Types of Garbage: \n"
            + spacing + spacing + spacing + "1) Compost (food items): apple core, orange/banana peel, tea bag \n"
            + spacing + spacing + spacing + "2) Trash (day-to-day items): juice box, styrofoam, chips bag,"
            	  	  + "\n                                                        plastic bag \n"
            + spacing + spacing + spacing + "3) Recycle (paper and plastic): newspaper, water bottles, cans,"
            		  + "\n                                                        glass bottles, cardboard boxes \n \n "
    		+ spacing + " - Practice your recycling skills by dragging potential 'trash' items \n"
            + spacing + spacing + " from the conveyor belt and dropping them into their correct bins. \n\n"
            + spacing + " - Earn points by correctly placing the garbage into it's respective bin. \n\n"
            + spacing + " - Continue to place the items into the bins until the round is over or until \n"
            + spacing + spacing + "five lives have been lost. \n\n"
            + spacing + " - You lose lives whenever you incorrectly assign a piece of garbage or \n"
            + spacing + spacing + "when a piece of garbage reaches the Landfill \n\n"
            + spacing + " - After each round, answer the trivia question correclty for bonus lives!\n\n"
            + spacing + " - Have Fun!";
    
    private final String factsTextString = spacing + "1) Recycling an aluminum can saves enough energy to power your  \n"
    		+ spacing + spacing + "for three hours. That is equal to about half a gallon of gas! \n \n"
    		+ spacing + "2) To produce each week's Sunday paper, we cut down 500,000 trees! \n \n"
    		+ spacing + "3) Americans use 2,500,000 plastic water bottles every hour! Most of \n"
    		+ spacing + spacing + "these are thrown away and are not recycled. Many of the \n"
    		+ spacing + spacing + "non-recycled bottles account for the million sea creature deaths \n \n"
    		+ spacing + "4) The United States is the #1 trash producing country in the world, \n "
    		+ spacing + spacing + "creating about 1609 pounds per person per year! This means that 5%\n "
    		+ spacing + spacing + "of the world creates around 40% of the world's waste. Catastrophic!\n\n"
    		+ spacing + "5) Rain Forests are being cut down at a rate of 100 acres per minute!\n \n"
    		+ spacing + "6) On average it costs $30 to recycle a ton of garbage, where as it costs\n"
    		+ spacing + spacing + "$50 to send it to a landfill and $65 to incinerate it.\n\n"
    		+ spacing + "7) If all of our newspaper was recycled, we could save 250,000,000 trees \n"
    		+ spacing + spacing + "every single year solely due to newspaper!\n\n"
    		+ spacing + "8) Annually, United States paper recovery saves over 100,000,000\n"
    		+ spacing + spacing + "cubic yards of landfill space. That is over 1,800 football fields\n"
    		+ spacing + spacing + "covered in a foot trash!\n\n"
    		+ spacing + "9) Using digital learning applications (such as this one) instead \n"
    		+ spacing + spacing + "of handouts can help reduce the amount of paper being used annually. \n\n"
    		+ spacing + "10) You can make a difference in the World by recycling!";
    
    /*
     * This is the constructor for the panel
     */
    public HelpScreenPanel(MasterPanel cards) 
    {
        this.cardHandler = cards;
        initGUI();
    }
    
    /*
     * This function is responsible for configuring the GUI components
     */
    public void initGUI() 
    {
  	
        this.setBackground(MasterPanel.BACKGROUND_COLOR);	// set background color
        this.setLayout(null);	// set absolute positioning
            
        // load logo
        logo = null;
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
        
        //Back button
        JButton backButton = new JButton("Back");
        int backButtonXCoord = (int) RecycleApplet.screenDimension.getWidth() / 2 - buttonLength / 2;
        backButton.setLocation(backButtonXCoord, buttonRowCoord); 
        backButton.setSize(buttonLength, buttonHeight);
        backButton.addActionListener(new backButtonListener());
        this.add(backButton);
        
        //Help button
        helpButton = new JButton("How To Play");
        buttonRowCoord = (int) RecycleApplet.screenDimension.getHeight()/6;
        int buttonXCoord = (int) RecycleApplet.screenDimension.getWidth()/5;
        helpButton.setLocation(buttonXCoord, buttonRowCoord);
        helpButton.setSize(buttonLength, buttonHeight);
        helpButton.addActionListener(new helpButtonListener());
        this.add(helpButton);
        
        //Facts button
        factsButton = new JButton("Facts");
        buttonXCoord = (int) RecycleApplet.screenDimension.getWidth()/5 * 3; //
        factsButton.setLocation(buttonXCoord, buttonRowCoord);
        factsButton.setSize(buttonLength, buttonHeight);
        factsButton.addActionListener(new factsButtonListener());
        this.add(factsButton);
        
        //Text Generics
        int textBuffer = 30;
        int screenWidth = (int) RecycleApplet.screenDimension.getWidth();
        int screenHeight = (int) RecycleApplet.screenDimension.getHeight();
        int textYCoord = screenHeight/5 + textBuffer;
        int textXSize = (screenWidth/5 * 3);
        int textYSize = (screenHeight/5 * 3) - 20;
        
        //Help Text Area
        helpText = new JTextArea("<Help Text Goes Here>");
        helpText.setEditable(false);
        helpText.setBackground(MasterPanel.BACKGROUND_COLOR);
        helpText.setText(helpTextString);
        helpText.setCaretPosition(0);

        helpScroll = new JScrollPane(helpText);
        helpScroll.setBackground(MasterPanel.BACKGROUND_COLOR);
        helpScroll.setSize(textXSize + textBuffer, textYSize); 
        int scrollPanelXLoc = (int) (RecycleApplet.screenDimension.getWidth()/2) - (int) (helpScroll.getWidth()/2);
        helpScroll.setLocation(scrollPanelXLoc, textYCoord);
        helpScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        LineBorder helpLineBorder = new LineBorder(new Color(0, 100, 80), 5, true);
        TitledBorder roundedTitledBorder = new TitledBorder(helpLineBorder, "How To Play");
        roundedTitledBorder.setTitleJustification(TitledBorder.CENTER);
        helpScroll.setBorder(roundedTitledBorder);
        
        helpTextOn = true;
        this.add(helpScroll);
        
        //Facts Text Area
        factsText = new JTextArea("<Facts Text Goes Here>");
        factsText.setEditable(false);
        factsText.setBackground(MasterPanel.BACKGROUND_COLOR);
        factsText.setText(factsTextString);
        factsText.setCaretPosition(0);

        factsScroll = new JScrollPane(factsText);
        factsScroll.setBackground(MasterPanel.BACKGROUND_COLOR);
        factsScroll.setSize(textXSize + textBuffer, textYSize); 
        scrollPanelXLoc = (int) (RecycleApplet.screenDimension.getWidth()/2) - (int) (factsScroll.getWidth()/2);
        factsScroll.setLocation(scrollPanelXLoc, textYCoord);
        factsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        LineBorder factsLineBorder = new LineBorder(new Color(0, 100, 80), 5, true);
        TitledBorder factBorder = new TitledBorder(factsLineBorder, "Important Recycling Facts!");
        factBorder.setTitleJustification(TitledBorder.CENTER);
        factsScroll.setBorder(factBorder);
        
        this.add(factsScroll);
        factsScroll.setVisible(false);
//        
//      //Load fonts for headings
//        try 
//        {
//             Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("typogarden.ttf")).deriveFont(60f);
//             GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//             ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("RESURREC.TTF")));
//             headerLabel.setFont(customFont);
//        }
//        catch (IOException e) 
//        {
//             e.printStackTrace();
//        }
//        catch (FontFormatException e)
//        {
//             e.printStackTrace();
//        }
        
        this.setVisible(true);
    }
    
    /*
     * Repaints all images on the help screen
     * (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        int xLoc = (int) ((RecycleApplet.screenDimension.getWidth() / 2)
        					- (MasterPanel.SMALL_LOGO_DIMENSIONS.getWidth() / 2));
        int yLoc = 20;
        g.drawImage(logo, xLoc, yLoc, (int) MasterPanel.SMALL_LOGO_DIMENSIONS.getWidth(),
        			(int) MasterPanel.SMALL_LOGO_DIMENSIONS.getHeight(), null);
    }
    
    /*
     * This function sets the help text to be displayed
     */
    public void showHelp() 
    {
        if (!helpTextOn) 
        {
        	helpScroll.setVisible(true);
            helpTextOn = true;
        } 

        if (factsTextOn) 
        {
            factsScroll.setVisible(false);
            factsTextOn = false;
        } 
    }
    
    /*
     * This function displays the facts
     */
    public void showFacts() 
    {
        if (!factsTextOn) 
        {
        	factsScroll.setVisible(true);
            factsTextOn = true;
        } 
        
        if (helpTextOn) 
        {
            helpScroll.setVisible(false);
            helpTextOn = false;
        } 
    }
    
    /*
     * This class is responsible for taking the player back a menu when invoked.
     */
    class backButtonListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent arg0) 
        {

            CardLayout cl = (CardLayout)(cardHandler.getLayout());
            cl.show(cardHandler, "main");            
        }
    };
    
    /*
     * This class is responsible for displaying help when invoked.
     */
    class helpButtonListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent arg0) 
        {
            showHelp();
            repaint();
        }
    };
    
    /*
     * This class is responsible for displaying the facts when invoked.
     */
    class factsButtonListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent arg0) 
        {
            showFacts();
            repaint();
        }
    };
}