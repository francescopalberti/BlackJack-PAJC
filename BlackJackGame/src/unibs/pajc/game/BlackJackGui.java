package unibs.pajc.game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Class that manage the GUI of the game
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */

public class BlackJackGui extends JFrame {
	private BlackjackGuiController controller; // client GUI controller
	private JButton Hit;
	private JButton Stay;
	private JPanel buttons;
	private JTextArea displayArea; 
	
	/**
     * Constructor for GUI object. Set up GUI
     *
     * @param aController. Client controller
     */
	
    public BlackJackGui(BlackjackGuiController aController) {
    	super( "Player" );
    	this.controller = aController;
    	buttons = new JPanel();
    	buttons.setLayout(new GridLayout(1,2));
    	Hit = new JButton("Hit");
    	Stay = new JButton("Stay");
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		Hit.addActionListener(
				new ActionListener() { 
					// send message to server
					public void actionPerformed( ActionEvent event )
					{
						controller.sendData( "hit" );
					} // end method actionPerformed
				} // end anonymous inner class
			); // end call to addActionListener
	
		Stay.addActionListener(
				new ActionListener() {
				// send message to server
					public void actionPerformed( ActionEvent event )
					{
						controller.sendData( "stay" );
					} // end method actionPerformed
				} // end anonymous inner class
			); // end call to addActionListener

		buttons.add(Hit, BorderLayout.SOUTH);
		buttons.add(Stay, BorderLayout.SOUTH);
		buttons.setVisible(true);
		add(buttons,BorderLayout.SOUTH);
		displayArea = new JTextArea(); // create displayArea
		add( new JScrollPane( displayArea ), BorderLayout.CENTER );

		setSize( 300, 300 ); // set size of window
		setVisible( true ); // show window
    }	 // end Client constructor
    
    /**
	 * Manipulates displayArea in the event-dispatch thread
	 *
	 * @param message Message to display
	 */	
  
    public void displayMessage( final String message)
    {
    	SwingUtilities.invokeLater(
    			new Runnable()
    			{
    				public void run() // updates displayArea
    				{
    					displayArea.append( message);
    				} // end method run
    			}  // end anonymous inner class
    			); // end call to SwingUtilities.invokeLater
    } // end method displayMessage

    /**
	 * disable the buttons
	 */	
	public void disableButtons() {
		buttons.setVisible(false);
	}
}
