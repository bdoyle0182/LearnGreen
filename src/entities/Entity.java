package entities;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
 * This class represents an entity on the screen
 */
public class Entity extends JLabel
{
	//Default var for serialization
	private static final long serialVersionUID = 1L;
	
	//x and y positions of the entity
	protected float xPos, yPos;
	
	//image associated with the game
	protected Image image;
	
	//the x and y velocity at which the object should be moving
	protected float xVel = 0,
					yVel = 0;
	
	//The filename of the image associated with this object
	protected String imageName;
	
	/*
	 * Constructor used for some simple entities, such as the hearts.
	 */
	public Entity (float xPos, float yPos, float xVel, float yVel, Image image)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.xVel = xVel;
		this.yVel = yVel;
		
		this.image = image;
		this.setIcon(new ImageIcon(image));
		
		this.setLocation((int)xPos, (int)yPos);
		this.setSize(image.getWidth(null), image.getHeight(null));
	}
	
	/*
	 * Constructor for factory-created tossed objects
	 * Takes string for image name rather than image
	 */
	public Entity(float xPos, float yPos, float xVel, float yVel, String imageName)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.xVel = xVel;
		this.yVel = yVel;
		
		this.imageName = imageName;
		this.image = readImage(imageName);
		
		this.setIcon(new ImageIcon(image));
		
		this.setLocation((int)xPos, (int)yPos);
		this.setSize(image.getWidth(null), image.getHeight(null));
		
	}
	
	/*
	 * setImage()
	 * 
	 * Set the image associated with the entity, and thereby dynamically change the size of the
	 * entity component
	 */
	public void setImage(Image image)
	{
		this.image = image;
		this.setIcon(new ImageIcon(image));
		this.setSize(image.getWidth(null), image.getHeight(null));

	}
	
	/*
	 * getXPos()
	 * 
	 * Get the x position of the entity
	 */
	public float getXPos() 
	{
		return xPos;
	}
	
	/*
	 * getYPos
	 * 
	 * Get the y position of the entity
	 */
	public float getYPos() 
	{
		return yPos;
	}

	/*
	 * readImage()
	 * 
	 * Reads an image (given the filename) from a file, and returns the corresponding
	 * image object
	 * 
	 */
	public Image readImage(String imageName)
	{
		Image newImage = null;
		try 
		{
			newImage = ImageIO.read(new File(imageName));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return newImage;
	}
}
