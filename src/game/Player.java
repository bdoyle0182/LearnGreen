package game;
/*
 * Player class relates a player's name and
 * score into one object. Player is used to
 * set High Scores.
 */
public class Player {
	
	private String name;
	private int score;
	
	/*
	 * Constructor set's the player's name
	 * from the text field on opening screen 
	 * panel and the player's score.
	 */
	public Player(String nameIn, int scoreIn) 
	{
		name = nameIn;
		score = scoreIn;
	}
	
	//Method sets player's name
	public void setName(String nameIn) 
	{
		name = nameIn;
	}
	
	//Method sets player's score
	public void setScore(int scoreIn) 
	{
		score = scoreIn;
	}
	
	//Method returns player's name
	public String getName() 
	{
		return name;
	}
	
	//Method returns player's score
	public int getScore() 
	{
		return score;
	}
};

