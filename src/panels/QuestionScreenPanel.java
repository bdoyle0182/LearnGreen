package panels;

import game.Game;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import main.RecycleApplet;

/*
 * QuestionScreenPanel reads in a text file of 
 * Questions into a 2D ArrayList. The inner array
 * holds the questions and their respective answers.
 * The outer array holds those questions and answers.
 * Every time the question panel is opened to be viewed,
 * the question displayed is reset by the random function.
 * The player can receive an extra life if they answer
 * the question correctly.
 */
public class QuestionScreenPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	int screenWidth = (int) RecycleApplet.screenDimension.getWidth();
	int screenHeight = (int) RecycleApplet.screenDimension.getWidth();
	
	private int dialogWidth = (int) RecycleApplet.screenDimension.getWidth();
	private int dialogHeight = (int) RecycleApplet.screenDimension.getHeight();
	private int dialogX = 0;
	private int dialogY = 0;
	
	private final int BUTTON_HEIGHT = 20;
	private final int BUTTON_WIDTH = screenWidth / 2;
	
	//Swing components that hold questions and answers
	private JLabel question;
	private JButton answerOne;
	private JButton answerTwo;
	private JButton answerThree;
	private JButton answerFour;
	
	//String holds the correct answer
	private String correctAnswer;
	
	//Sets Background Color of panel
	private Color backgroundColor = new Color(0xC3FDB8);
	
	//2D Array Design
	private ArrayList<String> questionAndAnswers;
	private ArrayList<ArrayList<String>> allQuestions;
	
	private ArrayList<Integer> questionsUsed = new ArrayList<Integer>(); 
	private boolean questionUsed;
	
	GridBagConstraints layoutConstraints = new GridBagConstraints();
	
	MasterPanel masterPanel;
	
	private Game game;
	
	JButton resumeGame = new JButton();
	JLabel playerAnswer = new JLabel();
	JLabel correctLabel = new JLabel();
	
	/*
	 * Constructor passes in masterPanel so game can be
	 * accessed. It calls functions to read file and set
	 * the first question.
	 */
	public QuestionScreenPanel(MasterPanel masterPanel) 
	{
		
		
		this.setBackground(backgroundColor);
		this.masterPanel = masterPanel;
		this.game = masterPanel.getGame();
		
		populateListFromFile();
		setQuestion();
				
		this.setBounds(dialogX, dialogY, dialogWidth, dialogHeight);		
		this.setVisible(true);
	}
	
	/*
	 * Method reads in the file. File has a question line, four answer
	 * lines, a correct answer line, and an empty line between questions.
	 * The file adds to questionsAndAnswers until there's an empty line.
	 * questionsAndAnswers is then added to allQuestions and recreated
	 * for the next question.
	 */
	public void populateListFromFile() 
	{
		BufferedReader bufferedReader = null;
		allQuestions = new ArrayList<ArrayList<String>>();
		questionAndAnswers = new ArrayList<String>();

		try {
		    bufferedReader = new BufferedReader(new FileReader("questions.txt"));
		    String line = "";
		    
		    while ((line = bufferedReader.readLine()) != null) 
		    {			    	
		    	if(line.length() > 0)
		    		questionAndAnswers.add(line);
		    	else if (line.length() == 0)
		    	{
		    		allQuestions.add(questionAndAnswers);
		    		questionAndAnswers = new ArrayList<String>();
		    	}
		    } 
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	/*
	 * Method resets the question for recalls to the panel
	 * after the first question has been answered.
	 * The random function is used to pick a random question.
	 */
	public void resetQuestion() 
	{
		this.game = masterPanel.getGame();
		
		ArrayList<String> tempQuestion = new ArrayList<String>();
		tempQuestion = getRandQuestion();
		
		question.setText(tempQuestion.get(0));
		answerOne.setText(tempQuestion.get(1));
		answerTwo.setText(tempQuestion.get(2));
		answerThree.setText(tempQuestion.get(3));
		answerFour.setText(tempQuestion.get(4));
		correctAnswer = tempQuestion.get(5);
		
		layoutConstraints.gridy = 1;
		this.add(answerOne, layoutConstraints);
		
		layoutConstraints.gridy = 2;
		this.add(answerTwo, layoutConstraints);
		
		layoutConstraints.gridy = 3;
		this.add(answerThree, layoutConstraints);
		
		layoutConstraints.gridy = 4;
		this.add(answerFour, layoutConstraints);
		
		this.repaint();
	}
	
	/*
	 * Method sets the initial question and adds all the swing
	 * components the question uses to the panel using a 
	 * GridBagLayout. The method uses the random function
	 * to choose the first question.
	 */
	public void setQuestion()
	{
		this.game = masterPanel.getGame();
		
		this.setLayout(new GridBagLayout());
		
		ArrayList<String> tempQuestion = new ArrayList<String>();
		tempQuestion = getRandQuestion();
				
		question = new JLabel();
		question.setText(tempQuestion.get(0));
		question.setHorizontalAlignment(SwingConstants.CENTER);
		question.setPreferredSize(new Dimension(BUTTON_WIDTH + 200, BUTTON_HEIGHT));
		
		layoutConstraints.gridy = 0;
		layoutConstraints.insets = new Insets(10, 10, 10, 10);
		this.add(question, layoutConstraints);
		
		answerOne = new JButton();
		answerOne.setText(tempQuestion.get(1));
		answerOne.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		answerOne.addActionListener(new AnswerButtonListener());
		
		layoutConstraints.gridy = 1;
		this.add(answerOne, layoutConstraints);
		
		answerTwo = new JButton();
		answerTwo.setText(tempQuestion.get(2));
		answerTwo.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		answerTwo.addActionListener(new AnswerButtonListener());
		
		layoutConstraints.gridy = 2;
		this.add(answerTwo, layoutConstraints);
		
		answerThree = new JButton();
		answerThree.setText(tempQuestion.get(3));
		answerThree.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		answerThree.addActionListener(new AnswerButtonListener());
		
		layoutConstraints.gridy = 3;
		this.add(answerThree, layoutConstraints);
		
		answerFour = new JButton();
		answerFour.setText(tempQuestion.get(4));
		answerFour.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		answerFour.addActionListener(new AnswerButtonListener());
		
		layoutConstraints.gridy = 4;
		this.add(answerFour, layoutConstraints);
		
		correctAnswer = tempQuestion.get(5);	
	}
	
	public void addPlayerAnswer(JLabel playerAnswer)
	{
		playerAnswer.setHorizontalAlignment(SwingConstants.CENTER);
		playerAnswer.setPreferredSize(new Dimension(BUTTON_WIDTH + 200, BUTTON_HEIGHT));
		
		layoutConstraints.gridy = 1;
		this.add(playerAnswer, layoutConstraints);
	}
	
	public void addCorrectAnswer(JLabel correctLabel)
	{
		correctLabel.setHorizontalAlignment(SwingConstants.CENTER);
		correctLabel.setPreferredSize(new Dimension(BUTTON_WIDTH + 200, BUTTON_HEIGHT));
		
		layoutConstraints.gridy = 2;
		this.add(correctLabel, layoutConstraints);
		correctLabel.setVisible(true);
	}
	
	public void addResumeGame(JButton resumeGame)
	{
		resumeGame.setText("Resume Game");
		resumeGame.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		resumeGame.addActionListener(new ResumeGameButtonListener());
		
		layoutConstraints.gridy = 3;
		this.add(resumeGame, layoutConstraints);
		
		this.repaint();
	}
	
	public void removeQuestion()
	{
		this.remove(answerOne);
		this.remove(answerTwo);
		this.remove(answerThree);
		this.remove(answerFour);
	}
	
	public void removeAnswers()
	{
		this.remove(playerAnswer);
		this.remove(correctLabel);
		this.remove(resumeGame);
	}
	
	
	/*
	 * Method returns a random question from the allQuestions
	 * ArrayList.
	 */
	private ArrayList<String> getRandQuestion() 
	{	
		questionUsed = true;
		
		Random randomizer = new Random();
		int randIdx = 0;
		
		if(questionsUsed.isEmpty())
			randIdx = randomizer.nextInt(allQuestions.size());
		else
		{
			while(questionUsed)
			{
				randIdx = randomizer.nextInt(allQuestions.size());
				checkIfQuestionUsed(randIdx);
			}
		}
		
		questionsUsed.add(randIdx);
		
		return allQuestions.get(randIdx);
	}
	
	/*
	 * Method checks if random index generated has
	 * been used for a question by iterating through
	 * the usedQuestions integers.
	 */
	private void checkIfQuestionUsed(int randIdx)
	{
			for(Integer current : questionsUsed)
			{
				if (current == randIdx) {
					questionUsed = true;
					break;
				}
				else
					questionUsed = false;
			}
	}
	
	/*
	 * Method paints the panel.
	 */
	public void paintComponent(Graphics myGraphic) 
	{
		super.paintComponent(myGraphic);	
	}
	
	/*
	 * Method removes the panel from game.
	 */
	public void removePanel() 
	{
		game.remove(this);
	}
	
	public void callRepaint()
	{
		this.repaint();
	}
	
	/*
	 * AnswerButtonListener handles the events that occur
	 * when a Player presses an answer to a question. The 
	 * game is always resumed for any answer pressed. 
	 * However, the Player only get a life/heart back
	 * if the correct answer is pressed.
	 */
	class AnswerButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{		
			JButton button = (JButton) event.getSource();
						
			removeQuestion();
						
			if(button.getText().equals(correctAnswer))
			{
				game.addHeart();
				
				playerAnswer.setText("Correct: " + button.getText());
				addPlayerAnswer(playerAnswer);
			}
			else
			{
				playerAnswer.setText("Incorrect: " + button.getText());
				addPlayerAnswer(playerAnswer);
			}
			
			correctLabel.setText("Correct Answer: " + correctAnswer);
			addCorrectAnswer(correctLabel);
			
			addResumeGame(resumeGame);
			
			revalidate();
	
		}
	}
	
	/*
	 * Button listener for resume game button
	 * Reshows game card when 
	 */
	class ResumeGameButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			CardLayout cl = (CardLayout)(masterPanel.getLayout());
            cl.show(masterPanel, "game");
			game.setGamePaused(false);
			
			removeAnswers();
			revalidate();
		}
	}
}