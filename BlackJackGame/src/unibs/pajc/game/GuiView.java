package unibs.pajc.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


/**
 * Class that manage the GUI of the game
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */

public class GuiView extends JFrame {
	private GuiController controller; // client GUI controller
	private JPanel buttons;
	private JTextArea displayArea; 
	private JFrame frame = new MainFrame(); // Creating an instance of the MainFrame class.

	private CardGroupPanel dealerCardPanel = null, playerCardPanel = null; // The deck of cards, the dealer's cards, the player's cards, the panels for the player's and dealer's cards
	
	private int balance = 10; // Setting the initial amounts for the Balance,
	private int betAmount = 0;  // the amount the player bets.

	// Creating the GUI elements in the window builder
	private JLabel lblInitBalanceValue;
	private JLabel lblInitialBalance;

	private JButton btnEndGame;
	private JLabel lblEnterBet;
	private JButton btnBet;
	private JLabel lblCurrentBalance;
	private JLabel lblBalanceAmount;
	private JLabel lblDealer;
	private JLabel lblPlayer;
	private JButton btnHit;
	private JButton btnStand;
	private JLabel lblInfo;
	private JButton btnContinue;
	private JComboBox<String> chipBox;
	String[] chip = { "5", "10", "20", "50", "100" };
	
	/**
     * Constructor for GUI object. Set up GUI
     *
     * @param aController. Client controller
     */
	
    public GuiView(GuiController guiController) {
    	frame.setVisible(true);
    	this.controller = guiController;
    	initGuiObjects();
    }	 // end Client constructor
    
    // This function runs when the program starts or when the game ends. It displays the initial GUI objects to enter an initial balance and start/stop a game
 	public void initGuiObjects() {
 		

 		//Create the combo box, select item at index 4.
 		//Indices start at 0, so 4 specifies the pig.
 		chipBox = new JComboBox<String>(chip);
 		lblInitBalanceValue = new JLabel("100 $"); // Text field to store initial balance
 		lblInitialBalance = new JLabel("Initial Balance:"); // Initial balance label
 		lblCurrentBalance = new JLabel("Current Balance:"); // Current balance label
 		lblBalanceAmount = new JLabel(); // Balance label, shows current balance
 		lblEnterBet = new JLabel("Enter Bet:"); // Bet amount info label
 		btnBet = new JButton("Bet"); // Deal button
 		lblDealer = new JLabel("Dealer"); // Dealer label
 		lblPlayer = new JLabel("Player"); // Player label
 		
 		btnBet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBet();
			}
		});
 		btnBet.setBounds(679, 610, 200, 50);
 		
 		btnHit = new JButton("Hit"); // Hit button
 		btnHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.sendClientMessageToServer( "HIT" ); // When pressed, hit
			}
		});
 		btnStand = new JButton("Stand"); // Stand button
 		btnStand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.sendClientMessageToServer( "STAY" ); // When pressed, stand
			}
		});
 		btnContinue = new JButton("Continue"); // When the final outcome is reached, press this to accept and continue the game
 		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				acceptOutcome();
			}
		});	
 		
 		btnEndGame = new JButton("End Game"); // End game button, this removes all GUI objects and starts from scratch
 		btnEndGame.setEnabled(false);
 		btnEndGame.setVisible(false);
 		btnEndGame.setBounds(121, 610, 99, 50);
 		btnEndGame.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				frame.getContentPane().removeAll(); // Remove all objects from screen
 				frame.repaint(); // Repaint to show update
 				controller.sendClientMessageToServer( "END" ); 
 			}
 		});
 		frame.getContentPane().add(btnEndGame);
 		
 		lblInfo = new JLabel("Waiting other players"); // Deal info label
		lblInfo.setBackground(Color.ORANGE);
		lblInfo.setOpaque(false);
		lblInfo.setForeground(Color.ORANGE);
		lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfo.setBounds(290, 482, 320, 28);
		frame.getContentPane().add(lblInfo);
		
		chipBox.setBounds(790, 580, 89, 28);
		
 	}
 	
 	/**
     * Show the initial game view
     *
     */
 
 	public void showWelcomeGui() {
 		lblInfo.setText("Connnect OK! Waiting other players");
 		frame.repaint();
 	}
 	
 	/**
     * Show the the bet gui view after the getBet message
     *
     */
 	
 	public void showBetGui() { 
 				
 		lblInitBalanceValue.setBounds(131, 580, 89, 28);
 		frame.getContentPane().add(lblInitBalanceValue);
 		
 		
 		lblInitialBalance.setFont(new Font("Arial", Font.BOLD, 13));
 		lblInitialBalance.setForeground(Color.WHITE);
 		lblInitialBalance.setBounds(30, 586, 100, 16);
 		lblInitBalanceValue.setEnabled(false);
 		frame.getContentPane().add(lblInitialBalance);
 		
		
		lblCurrentBalance.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentBalance.setFont(new Font("Arial", Font.BOLD, 16));
		lblCurrentBalance.setForeground(Color.WHITE);
		lblCurrentBalance.setBounds(315, 578, 272, 22);
		frame.getContentPane().add(lblCurrentBalance);

		lblBalanceAmount.setText(String.format("$%d", balance));
		lblBalanceAmount.setForeground(Color.ORANGE);
		lblBalanceAmount.setFont(new Font("Arial", Font.BOLD, 40));
		lblBalanceAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblBalanceAmount.setBounds(315, 600, 272, 50);
		frame.getContentPane().add(lblBalanceAmount);

		lblInfo.setText("Please enter a bet and click Deal");
		frame.getContentPane().add(lblInfo);
		
		
		lblEnterBet.setFont(new Font("Arial", Font.BOLD, 14));
		lblEnterBet.setForeground(Color.WHITE);
		lblEnterBet.setBounds(689, 586, 100, 16);
		frame.getContentPane().add(lblEnterBet);

		btnBet.setEnabled(true);
		chipBox.setEnabled(true);
		frame.getContentPane().add(chipBox);
		frame.getContentPane().add(btnBet);
		
		frame.repaint();
 	}
 	
 	/**
     * Gets the bet from the player
     *
     */
 	
 	public void getBet() {
 		
 		if (isValidAmount((String) chipBox.getSelectedItem()) == true) { 
			betAmount = Integer.parseInt((String) chipBox.getSelectedItem());
		} else {
			lblInfo.setText("Error: Bet must be a natural number!"); // Give an error
			chipBox.requestFocus();
			return;
		}

		if (betAmount > balance) { // If bet is higher than balance
			lblInfo.setText("Error: Bet higher than balance!"); // Give an error
			chipBox.requestFocus();
			return;
		}
		
		controller.sendClientMessageToServer( "BET--"+ betAmount ); 
		
		balance -= betAmount; // Subtract bet from balance
		lblBalanceAmount.setText(String.format("$%d", balance));
	
		btnBet.setEnabled(false);
		chipBox.setEnabled(false);
		lblInfo.setText("Waiting others player bet"); 
		frame.repaint(); 

	}
 	
 	/**
     * After all the bet have been placed show the players cards
     *
     */
 	
 	public void allBetPlaced() {
 		
		lblDealer.setForeground(Color.WHITE);
		lblDealer.setFont(new Font("Arial Black", Font.BOLD, 20));
		lblDealer.setBounds(415, 158, 82, 28);
		frame.getContentPane().add(lblDealer);

		
		lblPlayer.setForeground(Color.WHITE);
		lblPlayer.setFont(new Font("Arial Black", Font.BOLD, 20));
		lblPlayer.setBounds(415, 266, 82, 28);
		frame.getContentPane().add(lblPlayer);

		
		btnHit.setBounds(290, 515, 140, 35);
		
		btnHit.setEnabled(false);
		frame.getContentPane().add(btnHit);
		
		
		btnStand.setBounds(470, 515, 140, 35);
		
		btnStand.setEnabled(false);
		frame.getContentPane().add(btnStand);
		
		lblInfo.setText("Cards dealt"); 
		frame.repaint(); 
		
	}
 	
 	/**
     * Displays dealer and player cards as images
     *
     */

 	public void updateCardPanels(CardGroup dealerCards, CardGroup playerCards) 
 	{
		if (dealerCardPanel != null) { // If they're already added, remove them
			frame.getContentPane().remove(dealerCardPanel);
			frame.getContentPane().remove(playerCardPanel);
		}
		// Create and display two panels
		if(dealerCards!=null) {
			dealerCardPanel = new CardGroupPanel(dealerCards, 420 - (dealerCards.getCount() * 40), 50, 70, 104, 10);
			frame.getContentPane().add(dealerCardPanel);
		}
		if(playerCards!=null) {
			playerCardPanel = new CardGroupPanel(playerCards, 420 - (playerCards.getCount() * 40), 300, 70, 104, 10);
			playerCardPanel.showScoreLbl();
			frame.getContentPane().add(playerCardPanel);
		}
		
		frame.repaint();
	}
 	
 	/**
     * 
     *
     */
    
 	public void isYourTurn() { 
 		btnHit.setEnabled(true);
 		btnStand.setEnabled(true);
 		lblInfo.setText("Is your turn!"); 
		frame.repaint();
	}
 	
 	/**
     * 
     *
     */
 	
 	public void playerBusted() { 
 		btnHit.setEnabled(false);
 		btnStand.setEnabled(false);
 		lblInfo.setBackground(Color.CYAN);
 		lblInfo.setText("You BUSTED! Please Wait other players..."); 
		frame.repaint();
	}
 	
 	public void playerStand() { 
 		btnHit.setEnabled(false);
 		btnStand.setEnabled(false);
 		lblInfo.setBackground(Color.CYAN);
 		lblInfo.setText("You Stand! Please Wait other players..."); 
		frame.repaint();
	}
 	
 	public void playerEndTurn() {
 		btnHit.setEnabled(false);
 		btnStand.setEnabled(false);
 		lblInfo.setBackground(Color.CYAN);
 		lblInfo.setText("Your time is finished! Please Wait other players..."); 
		frame.repaint();
	}

 	public void playerTie() {
 		dealerCardPanel.showScoreLbl();
 		lblInfo.setText("You Tie!"); 
		outcomeHappened();
 		frame.repaint();
	}

	public void playerWin() {
		dealerCardPanel.showScoreLbl();
		lblInfo.setText("You Won!"); 
		outcomeHappened();
		frame.repaint();
	}
	
	

	public void playerLose() {
		dealerCardPanel.showScoreLbl();
		lblInfo.setText("You Lose!"); 
		outcomeHappened();
		frame.repaint();
	}	
	
	public void playerBlackJack() {
		dealerCardPanel.showScoreLbl();
		lblInfo.setText("You Have a BlackJack!!"); 
		outcomeHappened();
		frame.repaint();
	}
 	
	public void updateBalance(int _balance) {
		this.balance=_balance;
		lblBalanceAmount.setText(String.format("$%d", balance));
	}
	public void outcomeHappened() { //If something's happened, this round is over. Show the results of round and Continue button
		btnContinue.setEnabled(false);
		btnContinue.setVisible(false);
		btnContinue.setBounds(290, 444, 320, 35);
		
		frame.getContentPane().add(btnContinue);
		
		btnHit.setEnabled(false);
		btnStand.setEnabled(false);

		// Fancy effects, highlight info label orange and delay the display of Continue button by 500ms
		lblInfo.setOpaque(true);
		lblInfo.setForeground(Color.RED);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				btnEndGame.setEnabled(true);
		 		btnEndGame.setVisible(true);
				btnContinue.setEnabled(true);
				btnContinue.setVisible(true);
				btnContinue.requestFocus();
			}
		}, 500);

	}

	public void acceptOutcome() { // When outcome is reached

		lblInfo.setOpaque(false);
		lblInfo.setForeground(Color.ORANGE);
		
		// Remove deal objects
		
		controller.sendClientMessageToServer("CONTINUE"); // When pressed, hit
		frame.getContentPane().removeAll(); // Remove all objects from screen, restart the game

	}
	
	public void gameIsFinished() {
		
			int choice = JOptionPane.showOptionDialog(null, "You have run out of funds. Press Yes to add $100, or No to end the current game.", "Out of funds", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

			if (choice == JOptionPane.YES_OPTION) {
				balance += 100;
				lblBalanceAmount.setText(String.format("$%d", balance));
			} else {
				frame.getContentPane().removeAll();
				frame.repaint();
				return;
			}
		
	}

	
    /**
	 * Manipulates displayArea in the event-dispatch thread
	 *
	 * @param message Message to display
	 */	
  
    public void displayMessage( final String message)
    {
    	SwingUtilities.invokeLater(
    			new Runnable()
    			{
    				public void run() // updates displayArea
    				{
    					displayArea.append( message);
    				} // end method run
    			}  // end anonymous inner class
    			); // end call to SwingUtilities.invokeLater
    } // end method displayMessage

    /**
	 * disable the buttons
	 */	
	public void disableButtons() {
		buttons.setVisible(false);
	}
	
	public static int heightFromWidth(int width) { // 500x726 original size, helper function to get height proportional to width
		return (int) (1f * width * (380f / 255f));
	}
	
	public static boolean isValidAmount(String s) { // This is to assure that the values entered for the initial balance and the player's bet are natural numbers.
		try {
			if (Integer.parseInt(s) > 0) // Ensure amount entered is > 0
				return true;
			else
				return false;
		} catch (NumberFormatException e) { // If not valid integer
			return false;
		}
	}

	

	

	

	
}
