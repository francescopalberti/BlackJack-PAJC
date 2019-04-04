package unibs.pajc.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
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
	private GuiController controller; 
	private JFrame frame; // Creating an instance of the MainFrame class.

	private CardGroupPanel dealerCardPanel = null;
	private CardGroupPanel playerCardPanel = null; 
	
	private int balance; // Setting the initial amounts for the Balance,
	private int betAmount;  // the amount the player bets.

	private JLabel lblInitBalanceValue;
	private JLabel lblInitialBalance;

	private JButton btnEndGame;
	private JLabel lblBetAmount;
	private JButton btnBet;
	private JLabel lblPlayerCrntBalnc;
	private JLabel lblPlyBalanceAmount;
	
	private JButton btnHit;
	private JButton btnStand;
	private JLabel lblInfo;
	private JButton btnContinue;
	
	private JButton btn1;
	private JButton btn5;
	private JButton btn10;
	private JButton btn25;
	private JButton btn100;
	
	/**
     * Constructor for GUI object. Set up GUI
     *
     * @param aController. Client controller
     */
	
    public GuiView(GuiController guiController) {
    	frame = new GameFrame();
    	frame.setVisible(true);
    	this.controller = guiController;
    	initGuiObjects();
    }	
    
    // This function runs when the program starts or when the game ends. It displays the initial GUI objects to enter an initial balance and start/stop a game
 	public void initGuiObjects() {
 		lblInitBalanceValue = new JLabel(String.format("$%d", Constraint.START_MONEY)); // Text field to store initial balance
 		lblInitialBalance = new JLabel("Initial Balance:"); // Initial balance label
 		lblPlayerCrntBalnc = new JLabel("Your Current Balance"); // Current balance label
 		lblPlyBalanceAmount = new JLabel(); // Balance label, shows current balance
 		balance=Constraint.START_MONEY;
 		btnBet = new JButton("Bet"); // Deal button
 		btnBet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getBet();
			}
		});
 		
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
 		btnEndGame.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				System.exit(0);
 				controller.sendClientMessageToServer( "END" ); 
 			}
 		});
 		
 		lblInfo = new JLabel("Welcome!"); // Deal info label
		lblPlyBalanceAmount.setText(String.format("$%d", Constraint.START_MONEY));
		lblBetAmount = new JLabel(String.format("$%d", 0)); 
		
 	}
 	
 	public void startupGuiObject() {
    	lblInitBalanceValue.setBounds(131, 280, 89, 28);
 		lblInitBalanceValue.setForeground(Color.WHITE);
 		frame.getContentPane().add(lblInitBalanceValue);
 		
 		lblInitialBalance.setFont(new Font("Arial", Font.BOLD, 13));
 		lblInitialBalance.setForeground(Color.GRAY);
 		lblInitialBalance.setBounds(30, 286, 100, 16);
 		frame.getContentPane().add(lblInitialBalance);
 		
 		btnBet.setIcon(new ImageIcon("resources\\buttons\\btn_bet.png"));
 		btnBet.setEnabled(true);
 		btnBet.setVisible(true);
 		
 		btnBet.setBounds(1100, 625, 150, 35);
 		frame.getContentPane().add(btnBet);
 		
 		btnHit.setIcon(new ImageIcon("resources\\buttons\\btn_hit.png"));
 		btnHit.setEnabled(false);
 		btnHit.setVisible(false);
 		
 		btnHit.setBounds(1050, 570, 160, 35);
 		frame.getContentPane().add(btnHit);
 		
 		btnStand.setIcon(new ImageIcon("resources\\buttons\\btn_stand.png"));
 		btnStand.setEnabled(false);
 		btnStand.setVisible(false);
 		
 		btnStand.setBounds(1050, 625, 160, 35);
 		frame.getContentPane().add(btnStand);
 		
 		btnContinue.setIcon(new ImageIcon("resources\\buttons\\btn_continua.png"));
 		btnContinue.setEnabled(false);
		btnContinue.setVisible(false);
		btnContinue.setBounds(480, 280, 300, 35);
		
 		frame.getContentPane().add(btnContinue);
 		
 		btnEndGame.setIcon(new ImageIcon("resources\\buttons\\btn_esci.png"));
 		btnEndGame.setEnabled(false);
 		btnEndGame.setVisible(false);
 		btnEndGame.setBounds(480, 320, 300, 35);
 		
 		frame.getContentPane().add(btnEndGame);
 		
 		lblInfo.setBackground(Color.ORANGE);
		lblInfo.setOpaque(false);
		lblInfo.setForeground(Color.ORANGE);
		lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfo.setBounds(50, 630, 320, 28);
		frame.getContentPane().add(lblInfo);
		
		lblPlyBalanceAmount.setForeground(Color.ORANGE);
		lblPlyBalanceAmount.setFont(new Font("Arial", Font.BOLD, 40));
		lblPlyBalanceAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlyBalanceAmount.setBounds(500, 632, 272, 50);
		frame.getContentPane().add(lblPlyBalanceAmount);
		
		lblBetAmount.setFont(new Font("Arial", Font.BOLD, 40));
		lblBetAmount.setForeground(Color.WHITE);
		lblBetAmount.setBounds(30, 320, 100, 60);
		frame.getContentPane().add(lblBetAmount);
		
		lblPlayerCrntBalnc.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerCrntBalnc.setFont(new Font("Arial", Font.BOLD, 16));
		lblPlayerCrntBalnc.setForeground(Color.WHITE);
		lblPlayerCrntBalnc.setBounds(500, 615, 272, 22);
		frame.getContentPane().add(lblPlayerCrntBalnc);
		
		
		initChip();
    }
 	
 	private void initChip() {
 		btn1 = new JButton("1"); // Stand button
 		btn1.setIcon(new ImageIcon("resources\\chip\\1_chip.png"));
 		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshCurrentBet(1);
			}
		});
 		btn1.setBounds(1150, 515, 75, 60);
 		btn1.setBorderPainted(false);
		btn1.setFocusPainted(false);
		btn1.setContentAreaFilled(false);
 		frame.getContentPane().add(btn1);
 		
 		btn5 = new JButton("5"); // Stand button
 		btn5.setIcon(new ImageIcon("resources\\chip\\5_chip.png"));
 		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshCurrentBet(5);
			}
		});
 		btn5.setBounds(1080, 545, 75, 60);
 		btn5.setBorderPainted(false);
		btn5.setFocusPainted(false);
		btn5.setContentAreaFilled(false);
 		frame.getContentPane().add(btn5);
 		
 		btn10 = new JButton("10"); // Stand button
 		btn10.setIcon(new ImageIcon("resources\\chip\\10_chip.png"));
 		btn10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshCurrentBet(10);
			}
		});
 		btn10.setBounds(1010, 575, 75, 60);
 		btn10.setBorderPainted(false);
		btn10.setFocusPainted(false);
		btn10.setContentAreaFilled(false);
 		frame.getContentPane().add(btn10);
 		
 		btn25 = new JButton("25"); // Stand button
 		btn25.setIcon(new ImageIcon("resources\\chip\\25_chip.png"));
 		btn25.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshCurrentBet(25);
			}
		});
 		btn25.setBounds(940, 595, 75, 60);
 		btn25.setBorderPainted(false);
 		btn25.setFocusPainted(false);
 		btn25.setContentAreaFilled(false);
 		frame.getContentPane().add(btn25);
 		
 		btn100 = new JButton("100"); // Stand button
 		btn100.setIcon(new ImageIcon("resources\\chip\\100_chip.png"));
 		btn100.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshCurrentBet(100);
			}
		});
 		btn100.setBounds(870, 610, 75, 60);
 		btn100.setBorderPainted(false);
 		btn100.setFocusPainted(false);
 		btn100.setContentAreaFilled(false);
 		frame.getContentPane().add(btn100);
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
 		startupGuiObject();
 		betAmount=0;
 		refreshCurrentBet(0);
 		
		lblInfo.setText("Please enter a bet and click Bet!");
		frame.repaint();
 	}
 	
 	/**
     * Gets the bet from the player
     *
     */
 	
 	/**
     * Gets the bet from the player
     *
     */
 	

 	
 	public void getBet() {
 		if(betAmount==0) {
 			lblInfo.setText("Error: Bet can't be 0!"); 
 			return; // Give an error
 		}
 		
		if (betAmount > balance) { // If bet is higher than balance
			lblInfo.setText("Error: Bet higher than balance!"); // Give an error
	 		betAmount=0;
	 		refreshCurrentBet(0);
			return;
		}
		
		controller.sendClientMessageToServer( "BET--"+ betAmount ); 
		
		balance -= betAmount; // Subtract bet from balance
		updateBalance(balance);

	}
 	
 	public void refreshCurrentBet(int chipImport) {
 		betAmount+=chipImport;
 		lblBetAmount.setText(String.format("$%d", betAmount)); 		
 		frame.repaint();
 	}
 	
 	public void allBetPlaced() {
 		btnBet.setEnabled(false);
 		btnBet.setVisible(false);
 		removeChip();

		lblInfo.setText("Cards dealt"); 
		frame.repaint(); 
		
	}
 	
 	private void removeChip() {
		btn1.setVisible(false);
		btn5.setVisible(false);
		btn10.setVisible(false);
		btn25.setVisible(false);
		btn100.setVisible(false);
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
		if(dealerCards!=null) {
			dealerCardPanel = new CardGroupPanel(dealerCards, 600 - (dealerCards.getCount() * 40), 50, 70, 104, 10);
			frame.getContentPane().add(dealerCardPanel);
		}
		if(playerCards!=null) {
			playerCardPanel = new CardGroupPanel(playerCards, 600 - (playerCards.getCount() * 40), 450, 70, 104, 10);
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
 		btnStand.setVisible(true);
		btnStand.setEnabled(true);
 		btnHit.setEnabled(true);
 		btnHit.setVisible(true);
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
 		lblInfo.setText("You BUSTED! Please Wait other players..."); 
 		frame.repaint();
	}
 	
 	public void playerStand() { 
 		btnHit.setEnabled(false);
 		btnStand.setEnabled(false);
 		lblInfo.setText("You Stand! Please Wait other players..."); 
		frame.repaint();
	}
 	
 	public void playerEndTurn() {
 		btnHit.setEnabled(false);
 		btnStand.setEnabled(false);
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
 	
	public void updateBalance(int balance) {
		lblPlyBalanceAmount.setText(String.format("$%d", balance));
	}
	
	public void outcomeHappened() { //If something's happened, this round is over. Show the results of round and Continue button
		
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

		controller.sendClientMessageToServer("CONTINUE"); // When pressed, hit
		frame.getContentPane().removeAll(); // Remove all objects from screen, restart the game

	}
	
	public void gameIsFinished() {
		
			int choice = JOptionPane.showOptionDialog(null, "You have run out of funds. Press Yes to add $100, or No to end the current game.", "Out of funds", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

			if (choice == JOptionPane.YES_OPTION) {
				balance += 100;
				updateBalance(balance);
			} else {
				frame.getContentPane().removeAll();
				frame.repaint();
				return;
			}
		
	}
	
}
