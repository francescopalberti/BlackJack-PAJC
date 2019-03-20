package unibs.pajc.game;

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

/**
 * Class that manage the game
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */
public class ServerBJApp{
	private static final int numberOfPlayers = 2;
	private CountDownLatch placedBetsLatch;                                 // latch to wait for all players to place their bets
	private CountDownLatch playerContinueLatch;                                 // latch to wait for all players to place their bets
	private Deck gameDeck;
	private ExecutorService executor; // will run players
	private ServerSocket server; // server socket
	private SockServer[] sockServer; // Array of objects to be threaded
	private int counter = 1; // counter of number of connections
	private Card dcard1,dcard2;
	private ArrayList<Player> players;
	private Player dealer;

	// set up GUI
	public ServerBJApp() {
		
		players = new ArrayList<Player>();
		sockServer = new SockServer[ 100 ]; // allocate array for up to 10 server threads
		executor = Executors.newFixedThreadPool(100); // create thread pool
		
		
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
					executor.execute(sockServer[counter]);
					// then, continue to create another object and wait (loop)
					
				} catch (EOFException e) {
					System.out.println( "Server terminated connection" ); //diagnostic use
				} finally {
					++counter;
				}
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		playBlackjack();
	} 

	
	private void playBlackjack() {
		gameDeck = new Deck();
		placedBetsLatch = new CountDownLatch(numberOfPlayers); //serve per aspettare che tutti scommettano
		playerContinueLatch = new CountDownLatch(numberOfPlayers);
		
		sendBetMsg();
		
		/*attendo che tutti piazzino la scommessa*/
		try {
            placedBetsLatch.await(); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		allBetPlaced();
		
		/*Piccola pausa di 0.5s*/
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dealCards();
        menageTurn();
        dealerTurn();
		getResults();
		
		/*Attendo che tutti proseguano il gioco*/
		try {
            playerContinueLatch.await(); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		playBlackjack();
	}

	private void sendBetMsg() {
		for (int i = 1; i <= numberOfPlayers; i++) {
			sockServer[i].sendDataToClient("GETBET");
			System.out.println("SERVER-> Bet P" +i+" Sent"); //diagnostic use
		}
	}

	private void allBetPlaced(){
			for (int i=1;i<= numberOfPlayers;i++) {
				sockServer[i].sendDataToClient("ALLBETPLACED");
				System.out.println("SERVER-> ALLBETPLACED P" +i+" Sent"); //diagnostic use
			}
		
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
				players.get(i-1).newHand();
				players.get(i-1).CardHit(c1);
				players.get(i-1).CardHit(c2);
				sockServer[i].sendDataToClient("NEWDEALERCARD--" + dcard1 + "--1DCARD");
				sockServer[i].sendDataToClient("NEWDEALERCARD--" + dcard2);
				sockServer[i].sendDataToClient("NEWPLAYERCARD--" + c1);
				sockServer[i].sendDataToClient("NEWPLAYERCARD--" + c2);
				/* metodo futuro per visualizzare le carte dell' avversario
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
				sockServer[i].sendDataToClient("TURN");
				System.out.println("SERVER P"+i+" TURN");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!sockServer[i].getEndTurn()) {
					sockServer[i].sendDataToClient("ENDTURN");
					System.out.println("SERVER-> P"+i+"ENDTURN");
				}
				
		}
	}
	
	private void dealerTurn() {	
		for (int j = 1; j <= numberOfPlayers; j++) {
			sockServer[j].sendDataToClient("DEALERTURN");
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (dealer.getCardTotal() < 16){
			while(dealer.getCardTotal() < 16){
				Card card1 = gameDeck.dealCard();
				dealer.CardHit(card1);
				for (int j = 1; j <= numberOfPlayers; j++) {
					sockServer[j].sendDataToClient("NEWDEALERCARD--" + card1);
				}
			}
		}
	}
	
	private void getResults() {
		try{
			for (int i=1;i <= numberOfPlayers;i++) {
				if (players.get(i-1).hasABJ()) {
					players.get(i-1).refreshBalance("BJ");
					sockServer[i].sendDataToClient("NEWBALANCE--"+ players.get(i-1).getBalance());
					sockServer[i].sendDataToClient("RESULT--BLACKJACK");
					
				
				} else {
					if( (dealer.getCardTotal() <= 21) && (players.get(i-1).getCardTotal() <= 21 ) ){

						if (dealer.getCardTotal() > players.get(i-1).getCardTotal()) {
							playerLose(i);
							}

						if (dealer.getCardTotal() < players.get(i-1).getCardTotal()) {
							playerWon(i);
						}

						if (dealer.getCardTotal() == players.get(i-1).getCardTotal()) {
							playerTie(i);
						}				

					}//end if statement when dealer and player are under 21

					if(dealer.CheckBust()){
						
						if(players.get(i-1).CheckBust()){
							playerTie(i);
						}
						if(players.get(i-1).getCardTotal() <= 21){
							playerWon(i);
						}
						
					}//end if statement when dealer busted

					if(players.get(i-1).CheckBust() && dealer.getCardTotal() <= 21){
						playerLose(i);
					}//end if statement when player busted
				}
			}	
				
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
	}
	
	private void playerLose(int i) {
		players.get(i-1).refreshBalance("LOSE");
		sockServer[i].sendDataToClient("NEWBALANCE--"+ players.get(i-1).getBalance());
		sockServer[i].sendDataToClient("RESULT--LOSE");
	}
	
	private void playerWon(int i) {
		players.get(i-1).refreshBalance("WIN");
		sockServer[i].sendDataToClient("NEWBALANCE--"+ players.get(i-1).getBalance());
		sockServer[i].sendDataToClient("RESULT--WIN");
	}
	
	private void playerTie(int i) {
		sockServer[i].sendDataToClient("NEWBALANCE--"+ players.get(i-1).getBalance());
		sockServer[i].sendDataToClient("RESULT--TIE");
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
	
	/* This new Inner Class implements Runnable and objects instantiated from this
	 * class will become server threads each serving a different client
	 */
	
	private class SockServer implements Runnable {
		private PrintWriter output; // output stream to client
		private BufferedReader input; // input stream from client
		private Socket connection; // connection to client
		private int myConID;
		private Boolean endTurn=false;
		public SockServer(int counterIn) throws IOException {
			myConID = counterIn;
			Player p = new Player();
			players.add(p);
		}

		// wait for connection to arrive, then display connection info
		private void waitForConnection() throws IOException {
			System.out.println( "SERVER -> Waiting for connection" + myConID);
			connection = server.accept(); // allow server to accept connection            
			System.out.println( "SERVER -> Connection " + myConID + " received from: " +
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
					closeConnection(); //  close connection
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
			System.out.println( "SERVER -> STREAM OK" );
		} // end method getStreams

		// process connection with client
		private void processConnection() throws IOException
		{
			String client_msg = "";
			sendDataToClient("CONNECTOK--" + myConID); // send connection successful message
			do // process messages sent from client
			{ 
				client_msg=input.readLine(); // read new message
				System.out.println( "CLIENT -> "+ client_msg);
				if(client_msg.contains("BET")){	
					String[] clientMessageComponents = client_msg.split("--");
					placeBet(Integer.parseInt(clientMessageComponents[1]));
				}
				if(client_msg.contains("HIT")){				
					cardHit();
				}
				if(client_msg.contains("STAY")){
					playerStay();
				}
				if(client_msg.contains("CONTINUE")){
					playerContinue();
				}
			} while(!client_msg.equals("CLIENT>>>TERMINATE"));
		} // end method processConnection

		private void playerContinue() {
			endTurn=false;
			playerContinueLatch.countDown();
		}

		private void placeBet(int betValue) {
			players.get(this.myConID -1).setBet(betValue);
			placedBetsLatch.countDown();
		}

		/**
	     * Sets the player up for a new round of Blackjack.
	     */

		private void cardHit() {
			Card nextc = gameDeck.dealCard();
			sendDataToClient("NEWPLAYERCARD--" +nextc.toString());
			players.get(this.myConID -1).CardHit(nextc);
			//refreshOtherPlayersCards(myConID,nextc);
			
			if(players.get(this.myConID -1).CheckBust()) {			//if player busted
				sendDataToClient("BUST--");
				endTurn=true;
			}
		}
		
		private void playerStay() {
			sendDataToClient("STAND--");
			endTurn=true;
		}

		
		/**
		 * @return the endTurn
		 */
		public Boolean getEndTurn() {
			return endTurn;
		}

		// close streams and socket
		private void closeConnection() {
			System.out.println( "SERVER -> TERMINATING SOCKET " + myConID + "\n" );
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