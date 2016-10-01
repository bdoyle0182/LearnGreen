package game;


/*
 * This class is the thread that moderates when the game logic should be updated.
 */
public class GameLogic implements Runnable 
{
	//the main game
	Game game;
	//true while the game is played
	boolean playing = true;

	/*
	 * Constructor for the game class
	 */
	public GameLogic(Game game)
	{
		this.game = game;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{

		int counter = 0;
		
		while(playing)
		{
			if (!game.getGamePaused()) 
			{
				//Update the game
				game.update(counter);
				
				//adjust counter to track the number of times the game has updated
				counter++;
				
				try 
				{
					Thread.currentThread().sleep(50);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}