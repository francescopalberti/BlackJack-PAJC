package unibs.pajc.game;

/**
 * The Client class for the Blackjack Game.
 * Contains main
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */

public class BlackjackClient {                                      
    private static final String DEFAULT_SERVER_ADDRESS = "localhost";   // default server address
    private static final int DEFAULT_SERVER_PORT = 44444;               // default server port

    /**
     * Main method of the client that creates objects and executes other methods.
     *
     * @param args String array of arguments passed to the client
     */
	public static void main( String[] args )
	   {
        GuiController appController;
        // if no command line args
        if ( args.length == 0 )
        	appController = new GuiController(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT); // connect to localhost
        else
        	appController = new GuiController( args[ 0 ], DEFAULT_SERVER_PORT ); // use args to connect
        appController.run();	   
	   } // end main
	
}
