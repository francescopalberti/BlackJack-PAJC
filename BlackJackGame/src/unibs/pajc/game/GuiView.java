package unibs.pajc.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


/**
 * Class that manage the GUI of the game
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */

public class GuiView extends JFrame {
	private GuiController controller; // client GUI controller
	private JButton Hit;
	private JButton Stay;
	private JPanel buttons;
	private JTextArea displayArea; 
	private JFrame frame = new MainFrame(); // Creating an instance of the MainFrame class.

	private CardGroupPanel dealerCardPanel = null, playerCardPanel = null; // The deck of cards, the dealer's cards, the player's cards, the panels for the player's and dealer's cards
	private Card dealerHiddenCard; //  and the hidden card of the dealer.

	private double balance = 100.0; // Setting the initial amounts for the Balance,
	private int betAmount = 0, roundCount = 0; // the amount the player bets and the number of rounds.

	// Creating the GUI elements in the window builder
	private JTextField tfBalance;
	private JLabel lblInitialBalance;
	private JButton btnNewGame;
	private JButton btnEndGame;
	private JTextField tfBetAmount;
	private JLabel lblEnterBet;
	private JButton btnBet;
	private JLabel lblCurrentBalance;
	private JLabel lblBalanceAmount;
	private JLabel lblDealer;
	private JLabel lblPlayer;
	private JButton btnHit;
	private JButton btnStand;
	private JLabel lblBetAmount;
	private JLabel lblBetAmountDesc;
	private JLabel lblInfo;
	private JButton btnContinue;
	private JLabel lblShuffleInfo = null;
	
	/**
     * Constructor for GUI object. Set up GUI
     *
     * @param aController. Client controller
     */
	
    public GuiView(GuiController guiController) {
    	frame.setVisible(true);
    	this.controller = guiController;
    	
    }	 // end Client constructor
    
    // This function runs when the program starts or when the game ends. It displays the initial GUI objects to enter an initial balance and start/stop a game
 	public void initGuiObjects() {
 		
 	}
 
 	public void showWelcomeGui() {
 		lblInfo = new JLabel("Waiting other players"); // Deal info label
		lblInfo.setBackground(Color.ORANGE);
		lblInfo.setOpaque(false);
		lblInfo.setForeground(Color.ORANGE);
		lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfo.setBounds(290, 482, 320, 28);
		frame.getContentPane().add(lblInfo);
 	}
 	
 	public void showBetGui() { // This runs when a new game is started. It initializes and displays the current balance label, deal amount and deal button
 		btnEndGame = new JButton("End Game"); // End game button, this removes all GUI objects and starts from scratch
 		btnEndGame.setEnabled(false);
 		btnEndGame.setBounds(121, 610, 99, 50);
 		btnEndGame.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				frame.getContentPane().removeAll(); // Remove all objects from screen
 				frame.repaint(); // Repaint to show update
 				initGuiObjects(); // Restart the game logic and display the New Game menu
 			}
 		});
 		frame.getContentPane().add(btnEndGame);

 		tfBalance = new JTextField(); // Text field to store initial balance
 		tfBalance.setText("100");
 		tfBalance.setBounds(131, 580, 89, 28);
 		frame.getContentPane().add(tfBalance);
 		tfBalance.setColumns(10);
 		lblInitialBalance = new JLabel("Initial Balance:"); // Initial balance label
 		lblInitialBalance.setFont(new Font("Arial", Font.BOLD, 13));
 		lblInitialBalance.setForeground(Color.WHITE);
 		lblInitialBalance.setBounds(30, 586, 100, 16);
 		tfBalance.setEnabled(false);
 		frame.getContentPane().add(lblInitialBalance);
 		
		lblCurrentBalance = new JLabel("Current Balance:"); // Current balance label
		lblCurrentBalance.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentBalance.setFont(new Font("Arial", Font.BOLD, 16));
		lblCurrentBalance.setForeground(Color.WHITE);
		lblCurrentBalance.setBounds(315, 578, 272, 22);
		frame.getContentPane().add(lblCurrentBalance);

		lblBalanceAmount = new JLabel(); // Balance label, shows current balance
		lblBalanceAmount.setText(String.format("$%.2f", balance));
		lblBalanceAmount.setForeground(Color.ORANGE);
		lblBalanceAmount.setFont(new Font("Arial", Font.BOLD, 40));
		lblBalanceAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblBalanceAmount.setBounds(315, 600, 272, 50);
		frame.getContentPane().add(lblBalanceAmount);

		
		lblInfo.setText("Please enter a bet and click Deal");

		tfBetAmount = new JTextField(); // Bet amount text field
		tfBetAmount.setText("10");
		tfBetAmount.setBounds(790, 580, 89, 28);
		frame.getContentPane().add(tfBetAmount);

		lblEnterBet = new JLabel("Enter Bet:"); // Bet amount info label
		lblEnterBet.setFont(new Font("Arial", Font.BOLD, 14));
		lblEnterBet.setForeground(Color.WHITE);
		lblEnterBet.setBounds(689, 586, 100, 16);
		frame.getContentPane().add(lblEnterBet);

		btnBet = new JButton("Bet"); // Deal button
		btnBet.setBounds(679, 610, 200, 50);
		btnBet.setEnabled(true);
 		btnBet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBet();
			}
		});
		frame.getContentPane().add(btnBet);
		frame.repaint();
 	}
 	
 	
 	
 	public void getBet() {
 		if (isValidAmount(tfBetAmount.getText()) == true) { // Parse bet amount given
			betAmount = Integer.parseInt(tfBetAmount.getText());
		} else {
			lblInfo.setText("Error: Bet must be a natural number!"); // Give an error
			tfBetAmount.requestFocus();
			return;
		}

		if (betAmount > balance) { // If bet is higher than balance
			lblInfo.setText("Error: Bet higher than balance!"); // Give an error
			tfBetAmount.requestFocus();
			return;
		}
		controller.sendClientMessageToServer( "BET" ); // When pressed, hit
		
		balance -= betAmount; // Subtract bet from balance
		
		lblBalanceAmount.setText(String.format("$%.2f", balance));
		
		tfBetAmount.setEnabled(false);
		btnBet.setEnabled(false);

		lblInfo.setText("Waiting others player bet"); // Next instruction
		frame.repaint(); // Redraw frame to show changes

	}
 	
 	public void allBetPlaced() {
 		lblDealer = new JLabel("Dealer"); // Dealer label
		lblDealer.setForeground(Color.WHITE);
		lblDealer.setFont(new Font("Arial Black", Font.BOLD, 20));
		lblDealer.setBounds(415, 158, 82, 28);
		frame.getContentPane().add(lblDealer);

		lblPlayer = new JLabel("Player"); // Player label
		lblPlayer.setForeground(Color.WHITE);
		lblPlayer.setFont(new Font("Arial Black", Font.BOLD, 20));
		lblPlayer.setBounds(415, 266, 82, 28);
		frame.getContentPane().add(lblPlayer);

		btnHit = new JButton("Hit"); // Hit button
		btnHit.setBounds(290, 515, 140, 35);
		btnHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.sendClientMessageToServer( "HIT" ); // When pressed, hit
			}
		});
		btnHit.setEnabled(false);
		frame.getContentPane().add(btnHit);
		btnStand = new JButton("Stand"); // Stand button
		btnStand.setBounds(470, 515, 140, 35);
		btnStand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.sendClientMessageToServer( "STAY" ); // When pressed, stand
			}
		});
		btnStand.setEnabled(false);
		frame.getContentPane().add(btnStand);

		btnContinue = new JButton("Continue"); // When the final outcome is reached, press this to accept and continue the game
		btnContinue.setEnabled(false);
		btnContinue.setVisible(false);
		btnContinue.setBounds(290, 444, 320, 35);
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				acceptOutcome(); // Accept outcome
			}
		});
		
		frame.getContentPane().add(btnContinue);
		lblInfo.setText("Cards dealt"); // Next instruction
		frame.repaint(); // Redraw frame to show changes
		
	}

 	public void updateCardPanels(CardGroup dealerCards, CardGroup playerCards) { // Displays dealer and player cards as images
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
			frame.getContentPane().add(playerCardPanel);
		}
		
		frame.repaint();
	}
    
 	public void isYourTurn() { 
 		btnHit.setEnabled(true);
 		btnStand.setEnabled(true);
		frame.repaint();
	}
 	
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
 	
	public  void outcomeHappened() { //If something's happened, this round is over. Show the results of round and Continue button

		btnHit.setEnabled(false);
		btnStand.setEnabled(false);

		// Fancy effects, highlight info label orange and delay the display of Continue button by 500ms
		lblInfo.setOpaque(true);
		lblInfo.setForeground(Color.RED);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
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

		frame.getContentPane().remove(lblDealer);
		frame.getContentPane().remove(lblPlayer);
		frame.getContentPane().remove(btnHit);
		frame.getContentPane().remove(btnStand);
		frame.getContentPane().remove(lblBetAmount);
		frame.getContentPane().remove(lblBetAmountDesc);
		frame.getContentPane().remove(btnContinue);
		frame.getContentPane().remove(dealerCardPanel);
		frame.getContentPane().remove(playerCardPanel);
		lblInfo.setText("Please enter a bet and click Bet");
		tfBetAmount.setEnabled(true);
		btnBet.setEnabled(true);
		btnBet.requestFocus();
		frame.repaint();

		if (balance <= 0) { // If out of funds, either top up or end game
			int choice = JOptionPane.showOptionDialog(null, "You have run out of funds. Press Yes to add $100, or No to end the current game.", "Out of funds", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

			if (choice == JOptionPane.YES_OPTION) {
				balance += 100;
				lblBalanceAmount.setText(String.format("$%.2f", balance));
			} else {
				frame.getContentPane().removeAll();
				frame.repaint();
				initGuiObjects();
				return;
			}
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
