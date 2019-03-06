package unibs.pajc.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Class that manage the game
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */
public class ServerBJApp extends JFrame {
	private static final int numberOfPlayers = 2;
	private CountDownLatch placedBetsLatch;                                 // latch to wait for all players to place their bets
	private CountDownLatch playerTurnLatch;                                 // latch to wait for all players to place their bets
	private Deck gameDeck;
	private JTextArea displayArea; // display information to user
	private ExecutorService executor; // will run players
	private ServerSocket server; // server socket
	private SockServer[] sockServer; // Array of objects to be threaded
	private int counter = 1; // counter of number of connections
	private Card dcard1,dcard2;
	private ArrayList<Player> players;
	private Player dplayer;
	private int playersleft;
	private boolean roundover = true;
	private Player dealer;

	// set up GUI
	public ServerBJApp() {
		
		super( "DIAGNOSTIC SERVER" );
		players = new ArrayList<Player>();
		sockServer = new SockServer[ 100 ]; // allocate array for up to 10 server threads
		executor = Executors.newFixedThreadPool(100); // create thread pool

		displayArea = new JTextArea(); // create displayArea
		displayArea.setEditable(false);
		add( new JScrollPane( displayArea ), BorderLayout.CENTER );
		
		gameDeck = new Deck();

		setSize( 300, 300 ); // set size of window
		setVisible( true ); // show window
		
		
	} // end Server constructor

	// set up and run server 
	public void startGameConnection() {
		try {
			server = new ServerSocket( 44444, 100 );
			while (counter<=numberOfPlayers) {
				try {
					//create a new runnable object to serve the next client to call in
					sockServer[counter] = new SockServer(counter);
					// make that new object wait for a connection on that new server object
					sockServer[counter].waitForConnection();
					// launch that server object into its own new thread
					executor.execute(sockServer[ counter ]);
					// then, continue to create another object and wait (loop)

				} catch (EOFException e) {
					displayMessage( "\nServer terminated connection" ); //diagnostic use
				} finally {
					++counter;
				} // end finally
			} // end while
		} catch (IOException e) {
			e.printStackTrace();
		} // end catch
		
		placedBetsLatch = new CountDownLatch(numberOfPlayers); //serve per aspettare che tutti scommettano
		for (int i = 1; i <= numberOfPlayers; i++) {
			sockServer[i].sendDataToClient("GETBET--");
			displayMessage("\nSERVER-> Bet P" +i+" Sent"); //diagnostic use
		}
		
		try {
            placedBetsLatch.await(); //attendo che tutti piazzino la scommessa
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		allBetPlaced();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        dealCards();
		menageTurn();
	} 

	
	private void allBetPlaced(){
		try{
			for (int i=1;i<= numberOfPlayers;i++) {
				sockServer[i].sendDataToClient("ALLBETPLACED--");
				displayMessage("\nSERVER-> ALLBETPLACED P" +i+" Sent"); //diagnostic use
			}
		}
		catch(NullPointerException n){}
	}

	private void dealCards(){
		try{
			gameDeck.shuffle();   
			dcard1 = gameDeck.dealCard(); //dealer cards
			dcard2 = gameDeck.dealCard();
			dealer = new Player(dcard1,dcard2);
			for (int i=1;i<= numberOfPlayers;i++) {
				Card c1,c2;
				c1 = gameDeck.dealCard();  //player cards
				c2 = gameDeck.dealCard();
				Player p = new Player(c1,c2);
				players.add(p);
				sockServer[i].sendDataToClient("NEWDEALERCARD--" + dcard1);
				sockServer[i].sendDataToClient("NEWDEALERCARD--" + dcard2);
				sockServer[i].sendDataToClient("NEWPLAYERCARD--" + c1);
				sockServer[i].sendDataToClient("NEWPLAYERCARD--" + c2);
				/*displayMessage("\nNEWDEALERCARD--" + dcard1);	 //DIAGNOSTIC
				displayMessage("\nNEWDEALERCARD--" + dcard2);   //DIAGNOSTIC
				displayMessage("\nNEWPLAYERCARD--" + c1);	//DIAGNOSTIC
				displayMessage("\nNEWPLAYERCARD--" + c2);    //DIAGNOSTIC
				for (int j = 1; j < counter; j++) {
					if(j!=i) {
						sockServer[j].sendData("P"+i + " " + c1 + " " + c2);
						sockServer[j].sendData("P"+i + " Card Total " +  p.GetCardTotal());
					}
				}*/
			}
		}
		catch(NullPointerException n){}
	}
	
	private void menageTurn() {
		for (int i = 1; i <= numberOfPlayers; i++)  {
				sockServer[i].sendDataToClient("TURN--");
				displayMessage("\nSERVER P"+i+" TURN");
				
				
		}
	}
	

	 /*
	private void refreshOtherPlayersCards(int i, Card aCard) {
		for (int j = 1; j <= numberOfPlayers; j++) {
			if(j!=i) {
				sockServer[j].sendData("P"+i + " " + aCard);
			}
		}
	}
	 */
	private void getResults() {

		try{
			for (int i=1;i<= counter;i++) {
				sockServer[i].sendDataToClient("Dealer has " + dplayer.GetCardTotal());

				if( (dplayer.GetCardTotal() <= 21) && (players.get(i-1).GetCardTotal() <= 21 ) ){

					if (dplayer.GetCardTotal() > players.get(i-1).GetCardTotal()) {
						sockServer[i].sendDataToClient("\n You Lose!");
					}

					if (dplayer.GetCardTotal() < players.get(i-1).GetCardTotal()) {
						sockServer[i].sendDataToClient("\n You Win!");
					}

					if (dplayer.GetCardTotal() == players.get(i-1).GetCardTotal()) {
						sockServer[i].sendDataToClient("\n Tie!");
					}				

				}//end if statement when dealer and player are under 21

				if(dplayer.CheckBust()){
					
					if(players.get(i-1).CheckBust()){
						sockServer[i].sendDataToClient("\n Tie!");
					}
					if(players.get(i-1).GetCardTotal() <= 21){
						sockServer[i].sendDataToClient("\n You Won!");
					}
				}

				if(players.get(i-1).CheckBust() && dplayer.GetCardTotal() <= 21){
					sockServer[i].sendDataToClient("\n You Lose!");
				}
			}//end for loop
			


		}//end try block
		catch(NullPointerException n){}
	}
	
	// manipulates displayArea for diagnostic usage
		private void displayMessage( final String messageToDisplay )
		{
			SwingUtilities.invokeLater(
					new Runnable() 
					{
						public void run() // updates displayArea
						{
							displayArea.append( messageToDisplay ); // append message
						} // end method run
					} // end anonymous inner class
					); // end call to SwingUtilities.invokeLater
		} // end method displayMessage

	/* This new Inner Class implements Runnable and objects instantiated from this
	 * class will become server threads each serving a different client
	 */
	
	private class SockServer implements Runnable {
		private PrintWriter output; // output stream to client
		private BufferedReader input; // input stream from client
		private Socket connection; // connection to client
		private int myConID;
		public SockServer(int counterIn) {
			myConID = counterIn;
		}

		// wait for connection to arrive, then display connection info
		private void waitForConnection() throws IOException {
			displayMessage( "SERVER -> Waiting for connection" + myConID + "\n" );
			connection = server.accept(); // allow server to accept connection            
			displayMessage( "SERVER -> Connection " + myConID + " received from: " +
			connection.getInetAddress().getHostName() ); //DIAGNOSTICA
		} // end method waitForConnection
				
		public void run() {
			try {
				try {
					getStreams(); // get input & output streams
					processConnection(); // process connection
					} finally {
						closeConnection(); //  close connection
					}// end catch
				} catch (IOException e) {
					e.printStackTrace();
				}
		} // end method	

		
		private void getStreams() throws IOException
		{
			try {
	            InputStreamReader isr = new InputStreamReader(connection.getInputStream());     // input stream reader from socket
	            input = new BufferedReader(isr);
	            output = new PrintWriter(connection.getOutputStream(), true);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			displayMessage( "\nSERVER -> STREAM OK" );
		} // end method getStreams

		// process connection with client
		private void processConnection() throws IOException
		{
			String client_msg = "";
			sendDataToClient("CONNECT-OK--" + myConID + " successful"); // send connection successful message
			do // process messages sent from client
			{ 
				client_msg=input.readLine(); // read new message
				displayMessage( "\nCLIENT -> "+ client_msg);
				if(client_msg.contains("BET")){				
					placeBet();
				}
				if(client_msg.contains("HIT")){				
					cardHit();
				}
				if(client_msg.contains("STAY")){
					playerStay();
				}
			} while(!client_msg.equals("CLIENT>>>TERMINATE"));
		} // end method processConnection

		private void placeBet() {
			placedBetsLatch.countDown();
		}

		/**
	     * Sets the player up for a new round of Blackjack.
	     */

		private void dealerTurn() {		
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (dealer.GetCardTotal() < 16){
				while(dealer.GetCardTotal() < 16){
					Card card1 = gameDeck.dealCard();
					dealer.CardHit(card1);
					displayMessage("Dealer hits..." + card1 +  "\n" + "Total:" + dealer.GetCardTotal() + "\n");				
				}
			}
			if(dealer.CheckBust()){
				displayMessage("Dealer Busts!");
			}
			else{
				displayMessage("Dealer has" + " " + dealer.GetCardTotal());
			}

			getResults();
		}

		private void cardHit() {
			Card nextc = gameDeck.dealCard();
			sendDataToClient("NEWPLAYERCARD--" +nextc.toString());
			players.get(this.myConID -1).CardHit(nextc);
			//refreshOtherPlayersCards(myConID,nextc);
			
			if(players.get(this.myConID -1).CheckBust()) {			//if player busted
				sendDataToClient("BUST--");

			}
		}
		
		private void playerStay() {
			sendDataToClient("STAND--");
		}


		// close streams and socket
		private void closeConnection() {
			displayMessage( "\nSERVER -> TERMINATING SOCKET " + myConID + "\n" );
			try {
				output.close(); // close output stream
				input.close(); // close input stream
				connection.close(); // close socket
			} catch ( IOException e) {
				e.printStackTrace();
			} // end catch
		} // end method closeConnection

		private void sendDataToClient(String message) {
			output.println(message);
			output.flush(); // flush output to client
		} // end method sendData


	} // end class SockServer


} // end class Server