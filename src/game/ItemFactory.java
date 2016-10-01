package game;
import java.util.ArrayList;
import java.util.Random;

import entities.TossedItem;
import entities.WasteType;

public class ItemFactory {

	private static ArrayList<String> trashImages;
	private static ArrayList<String> compostImages;
	private static ArrayList<String> recycableImages;
	float xPos;
	float yPos;
	float xVel;
	float yVel;
	Game game;

	public ItemFactory(float xpos, float ypos, float xvel, float yvel, Game g)
	{
		trashImages = new ArrayList<String>();
		compostImages = new ArrayList<String>();
		recycableImages = new ArrayList<String>();

		game = g;

		createTrashImages();
		createCompostImages();
		createRecycableImages();

		xPos = xpos;
		yPos = ypos;
		xVel = xvel;
		yVel = yvel;
	}

	public TossedItem createTossedItem(WasteType criteria)
	{
		int panelBaseline = game.getTrashPanel().getBounds().height;
		
		TossedItem newTossedItem = null;
		if ( criteria == WasteType.TRASH ) {
			newTossedItem = new TossedItem(xPos, yPos, xVel, yVel, getRandTrashImage(),
					WasteType.TRASH, game);

		}
		else if ( criteria == WasteType.COMPOST ) {
			newTossedItem = new TossedItem(xPos, yPos, xVel, yVel, getRandCompostImage(),
					WasteType.COMPOST, game);
		}
		else if ( criteria == WasteType.RECYCLE ) {
			newTossedItem = new TossedItem(xPos, yPos, xVel, yVel, getRandRecycableImage(),
					WasteType.RECYCLE, game);
		}

		if (newTossedItem != null) {
			newTossedItem.setYPos(panelBaseline - newTossedItem.getPreferredSize().height);
			newTossedItem.setXPos(0 - newTossedItem.getPreferredSize().width);
		}
		
		return newTossedItem;
	}

	public void createTrashImages()
	{
			trashImages.add("styrofoam.png");
	}

	public void createCompostImages()
	{
			compostImages.add("apple.png");
	}

	public void createRecycableImages()
	{
			recycableImages.add("waterbottle.png");
			recycableImages.add("newspaper.png");
	}

	public void updateTrashImages(int level)
	{
		if (level == 2)
			trashImages.add("juice.png");
		if (level == 3)
			trashImages.add("plasticbag.png");
		if (level == 5)
			trashImages.add("chips.png");
	}
	
	public void updateCompostImages(int level)
	{
		if (level == 2)
			compostImages.add("orange.png");
		if (level == 3)
			compostImages.add("banana.png");
		if (level == 4)
			compostImages.add("tea.png");
	}
	
	public void updateRecycableImages(int level)
	{
		if (level == 2)
			recycableImages.add("can.png");
		if (level == 3)
			recycableImages.add("bottle.png");
		if (level == 6)
			recycableImages.add("cardboardbox.png");
	}
	
	public String getRandTrashImage()
	{
		Random randomizer = new Random();
		int randIdx = randomizer.nextInt(trashImages.size());
		return trashImages.get(randIdx);
	}

	public String getRandCompostImage()
	{
		Random randomizer = new Random();
		int randIdx = randomizer.nextInt(compostImages.size());
		return compostImages.get(randIdx);
	}

	public String getRandRecycableImage()
	{
		Random randomizer = new Random();
		int randIdx = randomizer.nextInt(recycableImages.size());
		return recycableImages.get(randIdx);
	}

	public void setXPos(float xpos) {
		xPos = xpos;
	}

	public void setYPos(float ypos) {
		yPos = ypos;
	}

	public void setXVel(float xvel) {
		xVel = xvel;
	}

	public void setYVel(float yvel) {
		yVel = yvel;
	}


}
