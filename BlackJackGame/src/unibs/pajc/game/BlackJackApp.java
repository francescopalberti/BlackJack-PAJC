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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Class that manage the game
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */
public class BlackJackApp extends JFrame {
    private JButton Deal;
	private Deck newdeck;
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

	// set up GUI
	public BlackJackApp() {

		super( "Dealer" );


		players = new ArrayList<Player>();
		sockServer = new SockServer[ 100 ]; // allocate array for up to 10 server threads
		executor = Executors.newFixedThreadPool(100); // create thread pool


		Deal = new JButton("Deal Cards");

		Deal.addActionListener(
				new ActionListener() 
				{
					// send message to client
					public void actionPerformed( ActionEvent event )
					{
						Deal.setEnabled(false);
						newdeck = new Deck();
						roundover=false;
						DealCards();
						displayMessage("\n\nCARDS DEALT\n\n");

					} // end method actionPerformed
				} // end anonymous inner class
				); // end call to addActionListener


		add(Deal,BorderLayout.SOUTH);

		displayArea = new JTextArea(); // create displayArea
		displayArea.setEditable(false);
		add( new JScrollPane( displayArea ), BorderLayout.CENTER );

		setSize( 300, 300 ); // set size of window
		setVisible( true ); // show window
	} // end Server constructor

	// set up and run server 
	public void runDeal()
	{
		try // set up server to receive connections; process connections
		{
			server = new ServerSocket( 44444, 100 ); // create ServerSocket

			while ( true ) 
			{
				try 
				{
					//create a new runnable object to serve the next client to call in
					sockServer[counter] = new SockServer(counter);
					// make that new object wait for a connection on that new server object
					sockServer[counter].waitForConnection();
					// launch that server object into its own new thread
					executor.execute(sockServer[ counter ]);
					// then, continue to create another object and wait (loop)

				} // end try
				catch ( EOFException eofException ) 
				{
					displayMessage( "\nServer terminated connection" );
				} // end catch
				finally 
				{
					++counter;
				} // end finally
			} // end while
		} // end try
		catch ( IOException ioException ) 
		{} // end catch
	} // end method runServer

	// manipulates displayArea in the event-dispatch thread
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

	private void DealCards(){

		try{
			playersleft = counter-1;
			newdeck.shuffle();
			dcard1 = newdeck.dealCard();
			dcard2 = newdeck.dealCard();
			displayMessage("\n\n" +dcard1.toString() + " " +dcard2.toString());

			for (int i=1;i<= counter;i++) {
				Card c1,c2;
				c1 = newdeck.dealCard();
				c2 = newdeck.dealCard();
				Player p = new Player(c1,c2);
				players.add(p);
				sockServer[i].sendData("You were Dealt:\n" + c1 + " " + c2);
				sockServer[i].sendData("Your Total: " +  p.GetCardTotal());

			}
		}
		catch(NullPointerException n){}
	}

	private void Results() {

		try{
			for (int i=1;i<= counter;i++) {
				sockServer[i].sendData("Dealer has " + dplayer.GetCardTotal());

				if( (dplayer.GetCardTotal() <= 21) && (players.get(i-1).GetCardTotal() <= 21 ) ){

					if (dplayer.GetCardTotal() > players.get(i-1).GetCardTotal()) {
						sockServer[i].sendData("\n You Lose!");
					}

					if (dplayer.GetCardTotal() < players.get(i-1).GetCardTotal()) {
						sockServer[i].sendData("\n You Win!");
					}

					if (dplayer.GetCardTotal() == players.get(i-1).GetCardTotal()) {
						sockServer[i].sendData("\n Tie!");
					}				

				}//end if statement when dealer and player are under 21

				if(dplayer.CheckBust()){
					
					if(players.get(i-1).CheckBust()){
						sockServer[i].sendData("\n Tie!");
					}
					if(players.get(i-1).GetCardTotal() <= 21){
						sockServer[i].sendData("\n You Won!");
					}
				}

				if(players.get(i-1).CheckBust() && dplayer.GetCardTotal() <= 21){
					sockServer[i].sendData("\n You Lose!");
				}
			}//end for loop
			


		}//end try block
		catch(NullPointerException n){}
	}

	/* This new Inner Class implements Runnable and objects instantiated from this
	 * class will become server threads each serving a different client
	 */
	private class SockServer implements Runnable
	{
		private PrintWriter output; // output stream to client
		private BufferedReader input; // input stream from client
		private Socket connection; // connection to client
		private int myConID;

		public SockServer(int counterIn)
		{
			myConID = counterIn;
		}

		public void run() {
			try {
				try {
					getStreams(); // get input & output streams
					processConnection(); // process connection

				} // end try
				catch ( EOFException eofException ) 
				{
					displayMessage( "\nServer" + myConID + " terminated connection" );
				}
				finally
				{
					closeConnection(); //  close connection
				}// end catch
			} // end try
			catch ( IOException ioException ) 
			{} // end catch
		} // end try

		// wait for connection to arrive, then display connection info
		private void waitForConnection() throws IOException
		{

			displayMessage( "Waiting for connection" + myConID + "\n" );
			connection = server.accept(); // allow server to accept connection            
			displayMessage( "Connection " + myConID + " received from: " +
					connection.getInetAddress().getHostName() );
		} // end method waitForConnection

		private void getStreams() throws IOException
		{
			try {
	            InputStreamReader isr = new InputStreamReader(connection.getInputStream());     // input stream reader from socket
	            input = new BufferedReader(isr);
	            output = new PrintWriter(connection.getOutputStream(), true);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			displayMessage( "\nGot I/O streams\n" );
		} // end method getStreams

		// process connection with client
		private void processConnection() throws IOException
		{
			String message = "Connection " + myConID + " successful";
			sendData( message ); // send connection successful message


			do // process messages sent from client
			{ 
				if(message.contains("hit")){				
					cardhit();
				}

				if(message.contains("stay")){
					this.sendData("Please Wait");
					playersleft--;
					CheckDone();
				}


				message = input.readLine(); // read new message

			} while ( !message.equals( "CLIENT>>> TERMINATE" ) );
		} // end method processConnection


		private void DealerGo() {		
			dplayer = new Player(dcard1,dcard2);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (dplayer.GetCardTotal() < 16){
				while(dplayer.GetCardTotal() < 16){
					Card card1 = newdeck.dealCard();
					dplayer.CardHit(card1);
					displayMessage("Dealer hits..." + card1 +  "\n" + "Total:" + dplayer.GetCardTotal() + "\n");				
				}
			}
			if(dplayer.CheckBust()){
				displayMessage("Dealer Busts!");
			}
			else{
				displayMessage("Dealer has" + " " + dplayer.GetCardTotal());
			}

			Results();
		}

		private void cardhit() {

			Card nextc = newdeck.dealCard();
			sendData(nextc.toString());
			players.get(this.myConID -1).CardHit(nextc);
			sendData("Your Total: " +  players.get(this.myConID -1).GetCardTotal());
			if(players.get(this.myConID -1).CheckBust()) {			//if player busted
				sendData("Bust!\n");		
				playersleft--;
				if(playersleft==0){
					DealerGo();
				}
			}


		}


		private void CheckDone() {

			if(playersleft==0){

				DealerGo();
			}
		}

		// close streams and socket
		private void closeConnection() 
		{
			displayMessage( "\nTerminating connection " + myConID + "\n" );

			try 
			{
				output.close(); // close output stream
				input.close(); // close input stream
				connection.close(); // close socket
			} // end try
			catch ( IOException ioException ) 
			{} // end catch
		} // end method closeConnection

		private void sendData( String message )
		{
			output.println( message );
			output.flush(); // flush output to client
		} // end method sendData


	} // end class SockServer


} // end class Server