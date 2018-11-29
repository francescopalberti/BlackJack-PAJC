package unibs.pajc.game;

import javax.swing.JFrame;

/**
 * Main method of the server that creates objects and executes other methods.
 *
 * @param args String array of arguments passed to the server
 */
public class BlackJackServer {
	public static void main( String[] args )
	   {
	      BlackJackGame application = new BlackJackGame(); // create a blackjack hand
	      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      application.runDeal(); // run server application
	   } // end main
}
