package entities;

import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import game.Game;

/*
 * TossedItem class
 * 
 * All of the trash objects (anything that needs to be thrown away) is represented
 * by a TossedItem object
 * 
 */
public class TossedItem extends Entity implements Movable, Transferable, DragSourceListener,
DragGestureListener, Serializable {

	private static final long serialVersionUID = 1L;

	private WasteType wasteType;	// what kind of trash this is
	private boolean isBeingDragged;	// whether the item is currently being dragged
	private Image image;			// image of object

	DragSource dragSource;	// drag source used to initiate drag event
	Game game;	// game the item is associated with

	/*
	 * TossedItem constructor
	 * 
	 * instantiates a tossed item with the given properties, and sets up the
	 * necessary components (drag source, transfer handler, and action listener)
	 * to allow for drag and drop functionality
	 * 
	 */
	public TossedItem(float xPos, float yPos, float xVel, float yVel, String imageName, WasteType t, Game g)
	{
		// call Entity constructor
		super(xPos, yPos, xVel, yVel, imageName);

		game = g;	// associate item with a game
		wasteType = t;	// set item's waste type
		
		this.image = readImage(imageName);
		
		// assign TransferHandler to handle the transfer of data with drag and drop
		this.setTransferHandler(new TransferHandler("property"));

		// instantiate drag source which will control the start of the drag item
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

		// add mouse listener to wait for a click to start the drag
		this.addMouseListener(new MouseAdapter() 
		{
			// on mouse press, start drag
			public void mousePressed(MouseEvent event) 
			{
				// get component that was pressed and the associated transferHandler
				JComponent component = (JComponent) event.getSource();
				TransferHandler th = component.getTransferHandler();

				// initate drag support for the object
				th.exportAsDrag(component, event, TransferHandler.COPY);
				isBeingDragged = true;
			}
		});
	}


	/*
	 * move()
	 * 
	 * Method moves the TossedItem to the right depending on the current
	 * velocity.
	 *  
	 */
	@Override
	public void move() 
	{
		// increase position variable appropriately
		this.xPos += this.xVel;

		// reset the location of the label on the screen
		setBounds((int) xPos, (int) yPos, this.getWidth(), this.getHeight());		
	}

	/*
	 * setYPos()
	 * 
	 * Sets the y-position of the TossedItem on its panel.
	 */
	public void setYPos(float yPos) 
	{
		this.yPos = yPos;
	}

	/*
	 * getWasteType()
	 * 
	 * Method returns the WasteType
	 */
	public WasteType getWasteType()
	{
		return wasteType;
	}

	/*
	 * getIsBeingDragged()
	 * 
	 * Method returns whether the item is currently being dragged.
	 */
	public boolean getIsBeingDragged()
	{
		return isBeingDragged;
	}

	/*
	 * dragGestureRecognized()
	 * 
	 * Method invoked when a drag-initiating gesture is detected
	 * Method starts a drag operation with the appropriate parameters
	 * 
	 * (non-Javadoc)
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {

		// during drag, mouse should be centered on the image
		Point grabLocation = new Point(-1 * (image.getWidth(null)/2), -1 * (image.getHeight(null)/2));

		// create new transferable to pass data of
		// a) waste type and b) index of item in array of tossed items
		Transferable transferable = new WasteTransferable(this.wasteType,
				game.getTossedItems().indexOf(this));

		// begin drag action
		dragSource.startDrag(dge, DragSource.DefaultCopyDrop, this.image,
				grabLocation, transferable, this);
	}

	/*
	 * getTransferData()
	 * 
	 * Returns an object which represents the data to be transferred.
	 * 
	 * (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException 
	{
		return this;
	}

	/*
	 * getTransferDataFlavors()
	 * 
	 * Returns an array of DataFlavor objects indicating the types the data can be provided in. 
	 * 
	 * (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	@Override
	public DataFlavor[] getTransferDataFlavors() 
	{
		return new DataFlavor[]{new DataFlavor(TossedItem.class, "TossedItem")};
	}

	/*
	 * isDataFlavorSupported()
	 * 
	 * Returns whether or not the specified data flavor is supported for this object.
	 * 
	 * (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) 
	{
		return true;
	}
	
	/*
	 * dragDropEnd()
	 * 
	 * When drag/drop action ends, sets isBeingDragged to false
	 * 
	 * (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {
		isBeingDragged = false;
	}

	/*
	 * setXPos()
	 * 
	 * sets the x-coordinate of the item
	 * 
	 */
	public void setXPos(int xcoord) {
		
		xPos = xcoord;

	}

	// Required drag event methods that do nothing in this application
	@Override
	public void dragEnter(DragSourceDragEvent dsde) {}

	@Override
	public void dragExit(DragSourceEvent dse) {}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {}
}
