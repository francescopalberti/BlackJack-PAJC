package unibs.pajc.game;

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
        BlackJackController appController;
        // if no command line args
        if ( args.length == 0 )
        	appController = new BlackJackController(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT); // connect to localhost
        else
        	appController = new BlackJackController( args[ 0 ], DEFAULT_SERVER_PORT ); // use args to connect
       } // end main
	
}
