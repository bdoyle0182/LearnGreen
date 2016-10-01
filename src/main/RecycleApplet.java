//
// Project 4
// Name: Brendan Doyle, Kevin Murphy, Kristen Skillmen, Sean Golden
// E-mail: bjd54, ksm79, ks336, srg41
// Instructor: Singh
// COSC 150
//
// In accordance with the class policies and Georgetown's Honor Code,
// I certify that, with the exceptions of the lecture notes and those
// items noted below, I have neither given nor received any assistance
// on this project.
//
//

package main;
import game.Game;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JApplet;

import panels.MasterPanel;
import panels.OpeningScreenPanel;

/*
 * 
 */
public class RecycleApplet extends JApplet {
	
	private static final long serialVersionUID = 1L;
		
	Game gamePanel;
	OpeningScreenPanel openingPanel;
	public static Dimension screenDimension = new Dimension(800,450);

	/*
	 * Applet is initialized on the main thread. The GUI is created
	 * on the event dispatch thread in the event dispatch thread.
	 */
	public void init()
	{
		try
		{
			EventQueue.invokeLater( new Runnable()
			{
				@Override
				public void run() 
				{
					createGUI();
				}
			} );
		} 
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		
		Frame applet = (Frame)this.getParent().getParent();
		applet.setTitle("Learn Green");
	}
		
	/*
	 * Method creates the GUI by creating
	 * a MasterPanel that handles all of our
	 * panels.
	 */
	private void createGUI() 
	{
		MasterPanel master = new MasterPanel();
		this.getContentPane().add(master);
		this.setSize(screenDimension);
		this.setVisible(true);
	}	
}
