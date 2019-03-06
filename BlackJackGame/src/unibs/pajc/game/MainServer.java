package unibs.pajc.game;

import javax.swing.JFrame;

/**
 * Main method of the server that creates objects and executes other methods.
 *
 * @param args String array of arguments passed to the server
 */
public class MainServer {
	public static void main( String[] args )
	   {
	      ServerBJApp application = new ServerBJApp(); 
	      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      application.startGameConnection(); // run server application
	   } // end main
}
