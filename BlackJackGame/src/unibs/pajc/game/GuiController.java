package unibs.pajc.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * GuiController.java
 *
 * The GuiController class for the Blackjack Game.
 * Contains GuiController
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */

public class GuiController {
	private String serverAddress;       // server address
	private int serverPort;  			//server port
	private Socket socket; // socket to communicate with server
	private BufferedReader in;                                                  // in to server
    private PrintWriter out;
    private GuiModel gm;
    private GuiView gui;
    
    public GuiController(String serverAddress, int serverPort) {
        System.out.println("Starting Blackjack client\nServer address: " + serverAddress + "\nServer port: " + serverPort);
        this.serverAddress=serverAddress;
        this.serverPort=serverPort;
        gm = new GuiModel();
    }
    
    // connect to server and process messages from server
  	public void runClient() throws IOException {
  		try // connect to server, get streams, process connection
  		{
  			connectToServer(); // create a Socket to make connection
  			getStreams(); // get the input and output streams
  			processConnection();
  		} finally {
 			closeConnection();
  	    }
  	} // end method runClient
  	
  	// connect to server
  	private void connectToServer() throws IOException
  		{   
  		System.out.println( "Attempting connection\n" );   
  		try {
  			socket = new Socket(serverAddress, serverPort);
  			System.out.print( "Connected to: " + 
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

  	

  	/**
  	 *  quit the game and closes the socket.
  	 */

  	private void closeConnection () throws IOException 
  	{
  		System.out.println("CLIENT -> CHIUSURA CONNESSIONE SOCKET");
		if(out != null && in != null && socket != null) {
			out.close();
			in.close();
			socket.close();
		}
  	}	 // end method closeConnection
  	
  	
  	/**
  	* Sends a message to the server.
  	*
  	* @param clientMessage Message to send to server
  	*/

  	public void sendClientMessageToServer(String clientMessage) {
  		out.println(clientMessage);
  	} // end method sendData

  	/**
	 * Process connection with server
	 *
	 */	
	private void processConnection() {
		this.gui = new GuiView(this);
		String messageFromServer;
		do // process messages sent from server
			{  
			messageFromServer=getServerMessage();
			System.out.print(messageFromServer); //for diagnostic use
			changeView(messageFromServer); // display graphic message
			if (messageFromServer.contains("Bust!") || messageFromServer.contains("Please Wait")){
				gui.disableButtons();				
			}
		} while(!messageFromServer.equals( "SERVER>>>TERMINATE" ));
	} // end method processConnection
	
	
	/**
     * Gets a message sent by the server.
     *
     * @return message sent by the server
     */

    public String getServerMessage() {
        String serverMessage = null;
        while (true) {
            try {
                serverMessage = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
         return serverMessage;
        }
    }

	/**
   * Changes the client view based on which message was received from the server.
   *
   * @param serverMessage Message received from server
   */
	public void changeView (String messageToDisplay) {
		String[] serverMessageComponents = messageToDisplay.split("--");   // array containing the components of the server message
        switch (serverMessageComponents[0]) {
            case "CONNECT-OK":
                gui.showWelcomeGui();
                break;
            case "GETBET":
                gui.showBetGui();
                break;
            case "ALLBETPLACED":
                gui.allBetPlaced();
                break;
            case "NEWPLAYERCARD":
            	gm.addCardToPlayer(serverMessageComponents[1], serverMessageComponents[2]); // Add card from top of deck to dealer's cards
            	gui.updateCardPanels(gm.getDealerCards(), gm.getPlayerCards()); 
            	break;
            case "NEWDEALERCARD":
            	gm.addCardToDealer(serverMessageComponents[1], serverMessageComponents[2]); // Add card from top of deck to dealer's cards
            	gui.updateCardPanels(gm.getDealerCards(), gm.getPlayerCards());                
                break;
            case "TURN":
            	gui.isYourTurn();                
                break;
            case "BUST":
            	gui.playerBusted();                
                break;
            case "STAND":
            	gui.playerStand();                
                break;
	}
  
}
}
