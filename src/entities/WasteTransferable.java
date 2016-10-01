package entities;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;


public class WasteTransferable implements Serializable, Transferable {

	private static final long serialVersionUID = 1L;
	private WasteType wasteType;
	private int[] returnVals;
	

	public WasteTransferable(WasteType type, int objIdx) 
	{
		returnVals = new int[2];
		
		switch (type) 
		{
			case TRASH: 
				returnVals[0] = 0;
				break;
			case RECYCLE: 
				returnVals[0] = 1;
				break;
			case COMPOST: 
				returnVals[0] = 2;
				break;
		}
		
		returnVals[1] = objIdx;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException 
	{
		return returnVals;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() 
	{
		return new DataFlavor[]{new DataFlavor(WasteTransferable.class, "WasteTransferable")};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) 
	{
		return true;
	}

	public WasteType getWasteType() 
	{		
		return wasteType;
	}
}
