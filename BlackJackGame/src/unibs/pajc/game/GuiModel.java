package unibs.pajc.game;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * GuiModel.java
 *
 * The GuiModel class for the Blackjack Game.
 * Contains GuiModel
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */

public class GuiModel {
	private int numPlayers;
    private ArrayList<String> playerNames;
    private int betAmount;
    private int stage;
    private boolean load;
    
    private String serverAddress; // host server for this application
	private int serverPort;
	                                                  
    private BufferedReader in;                                                  // in to server
    private PrintWriter out; 
	private String message = ""; // message from server
	private Socket socket; // socket to communicate with server

	// initialize chatServer and set up GUI
    public GuiModel(String host, int serverPort) {
    	this.serverAddress = host; // set server to which this client connects
		this.serverPort = serverPort;
		runClient();
		stage=1;
	}
	public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }
    public int getNumPlayers(){
        return this.numPlayers;
    }
    public void setPlayerNames(ArrayList<String> playerNames){
        this.playerNames = playerNames;
    }
    public ArrayList<String> getPlayerNames(){
        return this.playerNames;
    }
    public void setBetAmount(int betAmount){
        this.betAmount = betAmount;
    }
    public int getBetAmount(){
        return this.betAmount;
    }
    public void incrementStage(){
        this.stage++;
    }
    public int getStage(){
        return this.stage;
    }
    public void setLoad(boolean load){
        this.load = load;
    }
    public boolean getLoad(){
        return this.load;
    }
    
 // connect to server and process messages from server
 	public void runClient() {
 		try // connect to server, get streams, process connection
 		{
 			connectToServer(); // create a Socket to make connection
 			getStreams(); // get the input and output streams
 		} // end try
 		catch ( EOFException eofException ) 
 		{
 			System.err.println("Errore");
 	    } // end catch
 		catch ( IOException ioException ) 
 		{}
 	} // end method runClient

 // connect to server
 	private void connectToServer() throws IOException
 		{   
 		System.out.println( "Attempting connection\n" );   
 		try {
 			socket = new Socket(serverAddress, serverPort);
 			System.out.println( "Connected to: " + 
 			socket.getInetAddress().getHostName() );
 		}catch(IOException e) {
         System.err.println("No Blackjack server running on port " + serverPort + " at address " + serverAddress);
         System.exit(1);
     }
 	
 } // end method connectToServer

 // get streams to send and receive data
 	private void getStreams() throws IOException
 	{
 		try {
 			InputStreamReader isr = new InputStreamReader(socket.getInputStream());    // input stream reader from socket
 			in = new BufferedReader(isr);
 			out = new PrintWriter(socket.getOutputStream(), true);
 			System.out.println("\nGot I/O streams\n");
 		} catch (IOException e) {
 			e.printStackTrace();
     }
 	
     
 	} // end method getStreams

 	// process connection with server
 	public String getServerMessage(){

 		try {
             message = in.readLine();
             
         } catch (IOException e) {
             e.printStackTrace();
         }
 		return message;
 	
 	} // end method processConnection

 	/**
 	 *  quit the game and closes the socket.
 	 */

 	private void closeConnection() 
 	{
 		try {
 			socket.close();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 		System.exit(0);
 	}	 // end method closeConnection
 	
 	/**
 	* Sends a message to the server.
 	*
 	* @param clientMessage Message to send to server
 	*/

 	public void sendClientMessage(String clientMessage) {
 		out.println(clientMessage);
 	} // end method sendData

}
