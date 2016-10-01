package entities;
import game.Game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

/*
 * This class represents the waste receptacles where the trash will be dropped 
 */
public class Bin extends Entity 
{

	//The type of waste this bin is associated with 
	WasteType wasteType;
	
	//main game class
	Game game;
	
	//image for shadow of drag
	private Image shadowImage;
	
	//image of object for drag
	private Image plainImage;
	
	/*
	 * This class dictates the behavior that happens when the item is dropped on it.
	 */
	public class myDropListener extends DropTargetAdapter 
	{
		
		private DropTarget dropTarget;
		private JLabel label;
		
		public myDropListener(JLabel label) 
		{
			this.label = label;
			dropTarget = new DropTarget(label, DnDConstants.ACTION_COPY, this, true, null);
		}
		
		@Override
		public void dragEnter(DropTargetDragEvent arg0) 
		{
			setImage(shadowImage);
		}

		@Override
		public void dragExit(DropTargetEvent arg0) 
		{
			setImage(plainImage);
		}

		@Override
		public void drop(DropTargetDropEvent dtde) 
		{
			
			setImage(plainImage);
			
			//Used to transfer the data for the object that is dropped on the recycling bin
			Transferable tr = dtde.getTransferable();
			
			WasteType droppedWasteType = null;
							
			int[] myDroppedData;
			
			int droppedItemIdx = 0;
			
			try 
			{
				//data flavor represents a type of data that can be transferred by a transferable
				DataFlavor[] flavors = tr.getTransferDataFlavors();		
				
				//get the transfer data
				myDroppedData = (int[]) tr.getTransferData(flavors[0]);
				 
				droppedWasteType = WasteType.values()[myDroppedData[0]];
				droppedItemIdx = myDroppedData[1];
				
			} 
			catch (UnsupportedFlavorException e)
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			//check result of drop
			if(droppedWasteType == wasteType)
			{
				adjustScore();
			}
			else if(droppedWasteType != wasteType)
			{
				removeHeart();
			}
			
			//remove the item that was dragged from the screen regardless
			game.removeTossedItem(game.getTossedItems().get(droppedItemIdx));
			
			//Disallow "snapback" of the image to inital location of item when dragged.
			dtde.dropComplete(true);
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent arg0) {}

		@Override
		public void dragOver(DropTargetDragEvent arg0) {}
	}
	
	/*
	 * Bin constructor used by the factory to create bins.  
	 */
	public Bin(float xPos, float yPos, Image image, WasteType t, Game g)
	{
		super(xPos, yPos, 0, 0, image);
		this.wasteType = t; 
		game = g;
		plainImage = image;
		
		String shadowImageName = "";
		
		/*
		 * Set image for when the recycle bin is dragged over.
		 */
		switch (wasteType) 
		{
			case TRASH:
				shadowImageName = "trashBinShadow.png";
				break;
			case RECYCLE:
				shadowImageName = "recyclingBinShadow.png";
				break;
			case COMPOST: 
				shadowImageName = "compostBinShadow.png";
				break;
		}
		
		try 
		{
			//sets the image of the bin for when the bin is hovered over.
			shadowImage = ImageIO.read(new File(shadowImageName));		
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		new myDropListener(this);
	}
	
	/*
	 * Adjusts the score of the game
	 */
	public void adjustScore() 
	{
		game.adjustScore();
	}
	
	/*
	 * Removes a heart from the heartpanel and reduces lives
	 */
	public void removeHeart()
	{
		game.decrementLives();
	}
}
