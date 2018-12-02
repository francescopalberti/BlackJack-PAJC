package unibs.pajc.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeGuiController {

	  GuiModel gm;
	  WelcomeGui welcomeGui;

	  public WelcomeGuiController(GuiModel gm){
	    this.gm = gm;
	  }

	  public void run(){
	    welcomeGui = new WelcomeGui();
	    welcomeGui.display();
	    attachActionListeners();
	  }

	  public void attachActionListeners(){
	      welcomeGui.onePlayerButton.addActionListener(e -> gm.setNumPlayers(1));
	      welcomeGui.onePlayerButton.addActionListener(new closeAndIncrementStage());
	      welcomeGui.multiPlayerButton.addActionListener(e -> gm.setNumPlayers(2));
	      welcomeGui.multiPlayerButton.addActionListener(new closeAndIncrementStage());
	  }

	  public class closeAndIncrementStage implements ActionListener{
	      public void actionPerformed(ActionEvent event){
	          welcomeGui.welcomeFrame.setVisible(false);
	          gm.incrementStage();
	      }
	  }
}
