package unibs.pajc.game;
/**
 * BlackjackController class update the view and get notify from the model.
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */


public class BlackJackController {
	private BlackJackModel model;  
	private BlackJackGUI view;
	private String serverAddress;       // server address
	private int serverPort;  			//server port

	/**
     * Constructor for controller. Create a new view and a new model. process connection with server
     *
     * @param serverAddress server address
     * @param serverPort server port
     */
	public BlackJackController(String serverAddress, int serverPort) {
		System.out.println("Starting Blackjack client\nServer address: " + serverAddress + "\nServer port: " + serverPort);
        this.serverAddress=serverAddress;
        this.serverPort=serverPort;
		view = new BlackJackGUI(this);
    	model = new BlackJackModel(this.serverAddress, this.serverPort);
    	processConnection();
	}
	
	
	/**
	 * Process connection with server
	 *
	 */	
	private void processConnection() {
		String messageFromServer;
		do // process messages sent from server
			{  
			messageFromServer=getServerMessage();
			System.out.println("messaggio"+messageFromServer); //for diagnostic use
			changeView( "\n" + messageFromServer ); // display graphic message
			if (messageFromServer.contains("Bust!") || messageFromServer.contains("Please Wait")){
				view.disableButtons();				
			}
		} while(!messageFromServer.equals( "SERVER>>> TERMINATE" ));
	} // end method processConnection
	
		
	/**
	 * Send data msg to server.
	 *
	 * @param serverMessage Message to server
	 */	
	public void sendData( String message )
    {
		model.sendClientMessage(message);
    }
	
	/**
	 * get msg from server.
	 *
	 */	
	public String getServerMessage()
    {
		return model.getServerMessage();
    }

	
	/**
     * Changes the client view based on which message was received from the server.
     *
     * @param serverMessage Message received from server
     */
	public void changeView (String messageToDisplay) {
		view.displayMessage(messageToDisplay);
	}

	


}
