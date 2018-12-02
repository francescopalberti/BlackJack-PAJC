package unibs.pajc.game;

/**
 * GuiController.java
 *
 * The GuiController class for the Blackjack Game.
 * Contains GuiController
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */

public class GuiController {
	public GuiModel gm;
	private String serverAddress;       // server address
	private int serverPort;  			//server port


    public GuiController(String serverAddress, int serverPort){
        System.out.println("Starting Blackjack client\nServer address: " + serverAddress + "\nServer port: " + serverPort);
        this.serverAddress=serverAddress;
        this.serverPort=serverPort;
		gm = new GuiModel(this.serverAddress, this.serverPort);
    }

  /** Launches and runs the Blackjack game
   */
  public void run(){
    try{

      WelcomeGuiController wc = new WelcomeGuiController(gm);
      wc.run();
      while(gm.getStage() == 1) { Thread.sleep(10); }

      BetGuiController bwc = new BetGuiController(gm);
      bwc.run();
      while(gm.getStage() == 2) { Thread.sleep(10); }

    }
    catch(InterruptedException ex)
    {
      Thread.currentThread().interrupt();
    }

    BlackjackGuiController bgc = new BlackjackGuiController(gm);
    bgc.run();
  }
}
