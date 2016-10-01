package game;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import panels.MasterPanel;
import entities.Bin;
import entities.Entity;
import entities.TossedItem;
import entities.WasteType;

public class Game extends JPanel
{	
	private final int MAX_LIVES = 5;	// max number of lives user can have
	
	private static final long serialVersionUID = 1L;

	private MasterPanel masterPanel;	// master panel from which game was created
	
	// game status variables
	private int score;	//score
	private int livesRemaining;	//number of lives remaining
	private boolean gamePaused;	// whether game is paused
	
	private ArrayList<TossedItem> tossedItems;	// list of all tossed items in play
	private Bin[] bins;							// array of 3 recycling bins
	private ArrayList<Entity> hearts;			// list of hearts for each life user has
	
	// panels to be placed on screen
	private JPanel heartsPanel = null;
	private JPanel trashPanel = null;
	private JPanel conveyorPanel = null;
	private JPanel trashPilePanel = null;
	private JPanel scorePanel = null;
	private JPanel levelPanel = null;
	
	// labels for game status panel
	private JLabel scoreLabel = null;
	private JLabel levelLabel = null;
	private JLabel progressLabel = null;
	

	// variables for level features
	private int level = 1;
	private float itemSpeed;		// x velocity of items on conveyor belt
	private int minItemSpacing;		// min number of pixels between two items on belt
	private int maxItemSpacing;		// max number of pixels between two items on belt
	private int numItemsToSort;		// number of items that will be spawned on this level

	private int itemInterval; // number of pixels between current two objects

	ItemFactory trashFactory;	// factory to create tossed items
	private int animatedLineXVal = 600;	// x coordinate of line that moves along conveyor
										// to give appearance of movement

	/*
	 * Game constructor
	 * Sets up the game by initializing settings, setting up the user interface,
	 * and starting the game running on a new thread
	 * 
	 */
	public Game (MasterPanel cards)
	{
		// link game to controlling panel
		this.masterPanel = cards;
				
		// initialize game settings
		score = 0;
		livesRemaining = MAX_LIVES;
		gamePaused = false;
		
		// initialize UI
		initUI();	
		
		// initialize level settings
		updateLevelSettings(0);

		
		// Start game running on new thread (controlled by gameLogic runnable)
		GameLogic gameLogic = new GameLogic(this);
		Thread gameThread = new Thread(gameLogic);
		gameThread.start();
	}

	/*
	 * initUI()
	 * 
	 * Method to initialize the user interface
	 * Calls the appropriate methods to initialize all the panels that will show on the
	 * screen
	 * 
	 */
	public void initUI() 
	{
		// set layout to null to allow for absolute positioning
		this.setLayout(null);
		this.setBackground(MasterPanel.BACKGROUND_COLOR);	// set background color

		createHeartsPanel();

		createBinsPanel();

		createTrashPilePanel();

		createTrashPanel();

		createConveyorPanel();

		createScorePanel();
		
		createLevelPanel();

	}

	/*
	 * createBinsPanel()
	 * 
	 * creates a panel to display the three recycling bins
	 * 
	 */
	private void createBinsPanel() 
	{	
		// create array to store three bin objects
		bins = new Bin[3];
		
		// variables for panel location
		int binYLoc = 100;
		int binXLoc = 0;

		// create panel to display all bins
		JPanel binsPanel = new JPanel();
		binsPanel.setBackground(new Color(0,0,0,0));	// transparent background

		// Load images for all recycling bins
		Image trashBinImg = null;
		Image recyclingBinImg = null;
		Image compostBinImg = null;
		
		try 
		{
			trashBinImg = ImageIO.read(new File("trashBin.png"));

			recyclingBinImg = ImageIO.read(new File("recyclingBin.png"));

			compostBinImg = ImageIO.read(new File("compostBin.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		// create one bin object of each type
		// coordinates of bin don't matter since panel is flow layout
		bins[0] = new Bin(0, 0, trashBinImg, WasteType.TRASH, this);
		bins[1] = new Bin(0, 0, recyclingBinImg, WasteType.RECYCLE, this);
		bins[2] = new Bin(0, 0, compostBinImg, WasteType.COMPOST, this);

		// add each bin to the panel
		for(Bin bin : bins)
		{
			binsPanel.add(bin);
		}

		// set size and location of panel
		binsPanel.setSize(MasterPanel.SCREEN_WIDTH, trashBinImg.getHeight(null));
		binsPanel.setLocation(binXLoc, binYLoc);
		
		// add panel to screen
		this.add(binsPanel);
	}
	
	/*
	 * createTrashPilePanel()
	 * 
	 * Creates a panel in lower right corner to display an image of a pile of trash
	 * 
	 */
	private void createTrashPilePanel() {
		
		// instantiate panel
		trashPilePanel = new JPanel();
		trashPilePanel.setLayout(null);	// allow absolute positioning
		trashPilePanel.setBackground(new Color(0,0,0,0));	// transparent background

		// location values for trash pile
		int trashPileXLoc = MasterPanel.SCREEN_WIDTH - 250;
		int trashPileYLoc = MasterPanel.SCREEN_HEIGHT - 400;
		int trashPileDimension = 500;
		
		// load trash pile image
		Image trashPileImage = null;
		try 
		{
			trashPileImage = ImageIO.read(new File("trashpile.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		// set up image as JLabel and place in panel
		JLabel trashPileLabel = new JLabel();
		trashPileLabel.setIcon(new ImageIcon(trashPileImage));
		Dimension size = trashPileLabel.getPreferredSize();
		trashPileLabel.setBounds(0, 0, size.width, size.height);
		trashPileLabel.setVisible(true);
		trashPilePanel.add(trashPileLabel);

		// add panel to screen
		this.add(trashPilePanel);
		trashPilePanel.setBounds(trashPileXLoc, trashPileYLoc,
								 trashPileDimension, trashPileDimension);
	}

	/*
	 * createConveyorPanel()
	 * 
	 * Method creates a panel to hold the image of the conveyor belt
	 * 
	 */
	private void createConveyorPanel() {

		// variables for location values of belt
		int conveyorXLoc = 0;
		int conveyorYLoc = MasterPanel.SCREEN_HEIGHT - 150;
		int conveyorWidth = MasterPanel.SCREEN_WIDTH;
		int conveyorHeight = 200;

		// create conveyor panel
		conveyorPanel = new JPanel();
		conveyorPanel.setLayout(null);	// allow absolute positioning
		conveyorPanel.setBackground(new Color(0,0,0,0));	// transparent background

		// load image of conveyor belt
		Image conveyorImage = null;
		try 
		{
			conveyorImage = ImageIO.read(new File("conveyor.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		// place image on panel using JLabel
		JLabel conveyorLabel = new JLabel();
		conveyorLabel.setIcon(new ImageIcon(conveyorImage));
		Dimension size = conveyorLabel.getPreferredSize();
		conveyorLabel.setBounds(0, 0, size.width, size.height);
		conveyorLabel.setVisible(true);
		conveyorPanel.add(conveyorLabel);

		// add panel to screen
		this.add(conveyorPanel);
		conveyorPanel.setBounds(conveyorXLoc, conveyorYLoc, conveyorWidth, conveyorHeight);
	}
	
	/*
	 * createTrashPanel()
	 * 
	 * Method creates panel in the screen to which TossedItems will be dynamically
	 * added
	 * 
	 */
	private void createTrashPanel() {
		
		// variables for location/size of trash panel
		int trashXLoc = 0;
		int trashYLoc = MasterPanel.SCREEN_HEIGHT - 320;
		int trashPanelWidth = MasterPanel.SCREEN_WIDTH;
		int trashPanelHeight = 200;

		// instantiate trashPanel
		trashPanel = new JPanel();
		trashPanel.setLayout(null);	// allow absolute positioning of objects
		trashPanel.setBackground(new Color(0,0,0,0));	// transparent background

		// add trash panel to screen and set size
		this.add(trashPanel);
		trashPanel.setBounds(trashXLoc, trashYLoc, trashPanelWidth, trashPanelHeight);
		
		// instantiate arraylist to store dynamically created trash items
		tossedItems = new ArrayList<TossedItem>();
		
		// instantiate factory to generate trash items dynamically
		trashFactory = new ItemFactory(0.0f, 0.0f, itemSpeed, 0.0f, this);
		
	}
	
	/*
	 * createHeartsPanel()
	 * 
	 * Method creates a panel at top left of screen to display hearts representing how
	 * many lives the user has left
	 *
	 */
	public void createHeartsPanel()
	{	
		// instantiate array to store hearts on screen
		hearts = new ArrayList<Entity>();
		
		// instantiate panel for hearts to be displayed on
		heartsPanel = new JPanel();
		
		// load heart image
		Image heartImage = null;	
		
		try 
		{
			heartImage = ImageIO.read(new File("heart.png"));
		} catch (IOException e){
			e.printStackTrace();
		}
		
		// location values for heart panel
		int heartsPanelWidth = heartImage.getWidth(null) * (MAX_LIVES + 1);
		int heartsPanelHeight = heartImage.getHeight(null) + 10;
		int heartsPanelXLoc = 10;
		int heartsPanelYLoc = 10;
		
		heartsPanel.setLayout(null); // allow absolute positioning
		heartsPanel.setBackground(new Color(0,0,0,0));	// transparent background

		// create the appropriate number of hearts to correspond to lives and
		// position them on screen
		for(int j = 0; j < MAX_LIVES; j++)
		{
			Entity heart = new Entity(j * heartImage.getWidth(null), 0, 0, 0, heartImage);			
			hearts.add(heart);
			heartsPanel.add(heart);
		}
		
		// add heartsPanel to screen and set location
		this.add(heartsPanel);
		heartsPanel.setBounds(heartsPanelXLoc, heartsPanelYLoc,
							  heartsPanelWidth, heartsPanelHeight);
	}
	
	/*
	 * createScorePanel()
	 * 
	 * Creates a panel to display score in the top right corner of the screen.
	 * 
	 */
	public void createScorePanel()
	{		
		// dimensions and location variables for score panel
		int scorePanelWidth = 70;
		int scorePanelHeight = 30;
		int scoreXLoc = MasterPanel.SCREEN_WIDTH - scorePanelWidth - 10;
		int scoreYLoc = 10;

		// instantiate score panel and score label
		scorePanel = new JPanel();
		scoreLabel = new JLabel();
		
		scorePanel.setLayout(null);
		scorePanel.setBackground(new Color(0,0,0,0));

		// sets label text and size
		scoreLabel.setText("Score " + score);
		scoreLabel.setForeground(Color.BLACK);
		
		// add label to panel
		scorePanel.add(scoreLabel);
		scoreLabel.setBounds(0, 0, scorePanelWidth, scorePanelHeight);
		
		// add panel to screen and set bounds
		this.add(scorePanel);
		scorePanel.setBounds(scoreXLoc, scoreYLoc, scorePanelWidth, scorePanelHeight);

	}
	
	/*
	 * createLevelPanel()
	 * 
	 * Method creates a panel at the top of the screen that displays the current
	 * level and the items left to sort before the next level
	 * 
	 */
	public void createLevelPanel() {
		
		// instantiate panels and labels
		levelPanel = new JPanel();
		levelLabel = new JLabel();
		progressLabel = new JLabel();
		
		// dimensions and location variables for score panel & components
		int levelPanelWidth = 100;
		int levelPanelHeight = 100;
		int levelLabelHeight = 70;
		int progressLabelHeight = 30;
		// center panel on screen
		int levelXLoc = (MasterPanel.SCREEN_WIDTH / 2) - (levelPanelWidth / 2);
		int levelYLoc = 0;

		
		levelPanel.setLayout(null);	// allow absolute positioning of elements
		levelPanel.setBackground(new Color(0,0,0,0));	// transparent background

		// set text and format for level label
		levelLabel.setText("Level " + level);
		levelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		levelLabel.setForeground(Color.BLACK);

		// set text and format for progress label
		progressLabel.setText(numItemsToSort + " items to go");
		progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		progressLabel.setForeground(Color.BLACK);

		//Try block creates a new font for the score JLabel
		try {
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("typogarden.ttf")).deriveFont(60f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("RESURREC.TTF")));
			levelLabel.setFont(customFont);
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (FontFormatException e){
			e.printStackTrace();
		}
		
		// add labels to panel
		levelPanel.add(levelLabel);
		levelLabel.setBounds(0, 0, levelPanelWidth, levelLabelHeight);
		
		levelPanel.add(progressLabel);
		progressLabel.setBounds(0, 60, levelPanelWidth, progressLabelHeight);


		//Adds scoreBox to the game JFrame
		this.add(levelPanel);
		levelPanel.setBounds(levelXLoc, levelYLoc, levelPanelWidth, levelPanelHeight);
	}

	/*
	 * updateLevelSettings()
	 * 
	 * Method to change settings to those matching a given level
	 * 
	 */
	public void updateLevelSettings(int counter) {

		// switch statement to determine settings based on level
				// each statement changes the speed items move across the screen,
				// the range of space between any two items appearing on the conveyor belt
				// and the number of items the user must sort on the level
				switch (level) {
				case 1: 
					itemSpeed = 2.0f;
					minItemSpacing = 40;
					maxItemSpacing = 60;
					numItemsToSort = 5;
					break;
				case 2: 
					itemSpeed = 3.0f;
					minItemSpacing = 30;
					maxItemSpacing = 50;
					numItemsToSort = 20;
					break;
				case 3: 
					itemSpeed = 3.0f;
					minItemSpacing = 27;
					maxItemSpacing = 32;
					numItemsToSort = 25;
				break;
				case 4: 
					itemSpeed = 4.0f;
					minItemSpacing = 27;
					maxItemSpacing = 31;
					numItemsToSort = 30;
					break;
				case 5: 
					itemSpeed = 4.0f;
					minItemSpacing = 24;
					maxItemSpacing = 29;
					numItemsToSort = 35;
					break;
				case 6: 
					minItemSpacing = 24;
					maxItemSpacing = 28;
					numItemsToSort = 40;
					break;
				case 7: 
					itemSpeed = 5.0f;
					minItemSpacing = 23;
					maxItemSpacing = 28;
					numItemsToSort = 40;
					break;
				case 8:
					minItemSpacing = 20;
					maxItemSpacing = 25;
					numItemsToSort = 40;
				default:
					numItemsToSort = 42;
					
					break;
				}

		// update the different kinds of trash available based on the level
		if (level > 1) {
			trashFactory.updateTrashImages(level);
			trashFactory.updateCompostImages(level);
			trashFactory.updateRecycableImages(level);
		}
		
		// update trashFactory to create items with appropriate item speed
		trashFactory.setXVel(itemSpeed);

		// reset the interval of item appearance so item will appear when
		// level begins
		itemInterval = counter + 1;
	}
	
	/*
	 * addTossedItem()
	 * 
	 * Randomly generates a new tossed item and adds it to the game
	 * 
	 */
	public void addTossedItem()
	{
		// create new tossed item of random type
		TossedItem newTossedItem = trashFactory.createTossedItem(getRandTrashType());
		
		// add a new item to arraylist for storage
		tossedItems.add(newTossedItem);
		
		// add new item to panel, and set its dimensions
		trashPanel.add(newTossedItem);
		Dimension newTrashSize = newTossedItem.getPreferredSize();
		newTossedItem.setBounds((int) newTossedItem.getXPos(), (int) newTossedItem.getYPos(),
								newTrashSize.width, newTrashSize.height);
		trashPanel.repaint();
	}

	/*
	 * removeTossedItem()
	 * 
	 * removes a TossedItem from gameplay by taking it off the screen and
	 * removing it from the stored array of objects
	 * 
	 */
	public void removeTossedItem (TossedItem itemToRemove)
	{
		// if the item is in the list,
		// remove it from the list and from the panel
		if (tossedItems.contains(itemToRemove))
		{
			tossedItems.remove(itemToRemove);
			trashPanel.remove(itemToRemove);
			trashPanel.repaint();
			itemToRemove = null;
		}
	}

	/*
	 * adjustScore()
	 * 
	 * Method readjusts the score if 
	 * the user places trash in the correct
	 * bin and calls modifyScoreBox.
	 * 
	 */
	public void adjustScore()
	{
		score += 10;
		modifyScoreLabel();

	}

	/*
	 * decrementLives()
	 * 
	 * removes a life, and ends game if there are no lives left
	 * 
	 */
	public void decrementLives()
	{	
		// if user has lives left, remove one
		if (livesRemaining > 1) {
			
			removeHeart();
			livesRemaining -= 1;
			
		}
		
		// user has no lives remaining, game ends
		else
			gameOver();
	}
	
	/*
	 * addHeart()
	 * 
	 * Method to add a life to the game (adds another heart to the heart
	 * panel and increments the number of lives available to the user)
	 * 
	 */
	public void addHeart() {
		
		// load new heart image
		Image heartImage = null;
		
		try 
		{
			heartImage = ImageIO.read(new File("heart.png"));
		} catch (IOException e){
			e.printStackTrace();
		}

		// if user has fewer than the max number of lives allowed,
		// add a new life
		if (livesRemaining < MAX_LIVES) {
			
			livesRemaining++;
			
			// create heart object and add to objects array and screen
			Entity heart = new Entity((livesRemaining - 1) * heartImage.getWidth(null),
										0, 0, 0, heartImage);			
			hearts.add(heart);
			heartsPanel.add(heart);
		}
	}

	/*
	 * removeHeart()
	 * 
	 * Method modifies the Hearts Panel if a user incorrectly
	 * places trash in a bin. It removes the last heart in the
	 * array and repaints the frame without the last heart.
	 */
	public void removeHeart()
	{
		// if user still has lives remaining, remove one from hearts panel
		if(hearts.size() > 0)
		{
			heartsPanel.remove(hearts.get(hearts.size()-1));
			hearts.remove(hearts.size() - 1);
		}
		repaint();

	}

	/*
	 * modifyScoreLabel()
	 * 
	 * Method readjusts the ScoreLabel to the new
	 * score after the user places trash in the correct
	 * bin.
	 */
	public void modifyScoreLabel()
	{
		scoreLabel.setText("Score " + score);
	}


	/*
	 * getRandTrashType()
	 * 
	 * returns a random value from the enum of possible waste types
	 * 
	 */
	public WasteType getRandTrashType()
	{
		Random randomizer = new Random();
		int randIdx = randomizer.nextInt(WasteType.values().length);
		return WasteType.values()[randIdx];
	}

	/*
	 * update()
	 * 
	 * Method containing game logic for processing items on the conveyor belt:
	 * 1. adds items to screen at appropriate intervals
	 * 2. progress to next level if the level's items have all been sorted
	 * 3. check whether any item has gone off the screen 
	 * 4. move all items forward by x-velocity value
	 * 
	 */
	public void update(int counter)
	{

		// if it's time to add the next item to the screen, add it
		if (counter == itemInterval && numItemsToSort != 0) {
			
			// create a new trash item
			addTossedItem();

			// generate random interval for next item to appear at
			// interval will be between minItemSpacing and maxItemSpacing pixels wide
			Random randomizer = new Random();
			itemInterval = randomizer.nextInt(maxItemSpacing - minItemSpacing + 1) + minItemSpacing;
			itemInterval += counter;
			
			// decrease counter of items to go
			numItemsToSort--;
			progressLabel.setText(numItemsToSort + " items to go");
		}

		// progress to next level if all items for that level have been sorted
		if (numItemsToSort == 0 && tossedItems.size() == 0)
			startNextLevel(counter);


		// loop through all items on screen
		for (int i = 0; i < tossedItems.size(); i++)
		{
			// get a reference to the item
			TossedItem item = tossedItems.get(i);
			
			// check if item has gone off screen
			// remove it from play and remove a life if so
			if(this.isOffScreen(item))
			{
				// as long as item currently isn't being dragged, deletes it from screen
				if (!item.getIsBeingDragged()) {
					removeTossedItem(item);
					decrementLives();
				}
				
			}
			
			// otherwise move each item forward along belt
			else
				item.move();
		}
		
		// change x coordinate for conveyor belt's moving lines
		animatedLineXVal += itemSpeed;
	}
	
	/*
	 * Method to move game to next level (increment level variable,
	 * change to quiz card, call updateLevelSettings)
	 * 
	 */
	public void startNextLevel(int counter) {
		
		// pause game
		gamePaused = true;

		// increment level
		level++;
		levelLabel.setText("Level " + level);

		// change to question panel
		masterPanel.getQPanel().resetQuestion();
		CardLayout cl = (CardLayout)(masterPanel.getLayout());
		cl.show(masterPanel, "question");
		
		// update settings to those for next level
		updateLevelSettings(counter);
	}
	
	/*
	 * isOffScreen()
	 * 
	 * Method that returns whether a given item has rolled off the screen
	 * (progressed too far in positive x direction)
	 * 
	 */
	public boolean isOffScreen(TossedItem t)
	{
		// variable stores the furthest right point that counts as "on screen"
		int cutoffXCoord = MasterPanel.SCREEN_WIDTH - 180;
		
		// determine whether item is off the screen
		if(t.getX() > cutoffXCoord)
			return true;
		
		else
			return false;
	}
	


	/*
	 * gameOver()
	 * 
	 * method handles the end of a game by updating the high-score list as needed
	 * and returning user to the main screen
	 * 
	 */
	public void gameOver() 
	{
		// get player name and create new player obj with score
		String playerName = masterPanel.getCurrentPlayerName();
		Player currentPlayer = new Player(playerName, score);
		
		// update the high score panel
		masterPanel.getHsPanel().updateListWithNewEntry(currentPlayer);
		masterPanel.getHsPanel().populateListFromFile();
		masterPanel.getHsPanel().updateHighScoreText();

		// switch cards to main screen
		CardLayout cl = (CardLayout)(masterPanel.getLayout());
		cl.show(masterPanel, "main");
		
		// remove this instance of game from cards
		cl.removeLayoutComponent(this);
		gamePaused = true;
	}

	/*
	 * getTrashPanel()
	 * 
	 * returns trash panel
	 * 
	 */
	public JPanel getTrashPanel()
	{
		return this.trashPanel;
	}

	/*
	 * getGamePaused()
	 * 
	 * returns whether the game is paused
	 * 
	 */
	public boolean getGamePaused()
	{
		return this.gamePaused;
	}

	/*
	 * setGamePaused()
	 * 
	 * sets whether the game is paused to the given value
	 * 
	 */
	public void setGamePaused(boolean isPaused)
	{
		gamePaused = isPaused;
	}

	/*
	 * getTossedItems()
	 * 
	 * returns the list of tossed items currently in play
	 * 
	 */
	public ArrayList<TossedItem> getTossedItems() {
		return tossedItems;
	}


	/*
	 * paintComponent()
	 * 
	 * paints everything on screen
	 * 
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{
		
		super.paintComponent(g);
		this.drawAnimatedLines(g);
		this.repaint();

	}
	
	/*
	 * Method draws lines on the conveyor belt equal distance
	 * from each other using the modulus operator and are
	 * redrawn when the panel updates at a new location
	 * relative to the itemSpeed.
	 */
    private void drawAnimatedLines(Graphics g) 
    {
        Graphics2D animatedLine = (Graphics2D) g;
               		
        animatedLine.drawLine(animatedLineXVal % 640 , 335, animatedLineXVal % 640, 340); 
        animatedLine.drawLine((animatedLineXVal - 80) % 640, 335, (animatedLineXVal - 80) % 640, 340);
        animatedLine.drawLine((animatedLineXVal - 160) % 640, 335, (animatedLineXVal - 160) % 640, 340);
        animatedLine.drawLine((animatedLineXVal - 240) % 640, 335, (animatedLineXVal - 240) % 640, 340);
        animatedLine.drawLine((animatedLineXVal - 320) % 640, 335, (animatedLineXVal - 320) % 640, 340);
        animatedLine.drawLine((animatedLineXVal - 400) % 640, 335, (animatedLineXVal - 400) % 640, 340);
        animatedLine.drawLine((animatedLineXVal - 480) % 640, 335, (animatedLineXVal - 480) % 640, 340);
        animatedLine.drawLine((animatedLineXVal - 560) % 640, 335, (animatedLineXVal - 560) % 640, 340);
    }

	
}
