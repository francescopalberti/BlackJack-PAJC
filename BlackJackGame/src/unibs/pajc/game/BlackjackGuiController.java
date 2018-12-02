package unibs.pajc.game;

/**
 * BlackjackController class update the view and get notify from the model.
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */


public class BlackjackGuiController {
	private BlackJackGui gui;
	private GuiModel gm;

	/**
     * Constructor for controller. Create a new view and a new model. process connection with server
     *
     * @param serverAddress server address
     * @param serverPort server port
     */
	
	public BlackjackGuiController(GuiModel gm) {
		this.gui = new BlackJackGui(this);
	    this.gm = gm;
	}
	
	public void run()
    {
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
				gui.disableButtons();				
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
		gm.sendClientMessage(message);
    }
	
	/**
	 * get msg from server.
	 *
	 */	
	public String getServerMessage()
    {
		return gm.getServerMessage();
    }

	
	/**
     * Changes the client view based on which message was received from the server.
     *
     * @param serverMessage Message received from server
     */
	public void changeView (String messageToDisplay) {
		gui.displayMessage(messageToDisplay);
	}

	


}
