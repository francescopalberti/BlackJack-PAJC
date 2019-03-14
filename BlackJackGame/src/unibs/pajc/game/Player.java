package unibs.pajc.game;

/**
 * Player objects represent a standard player
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */


public class Player {
	private CardGroup handCards;
	private int balance=100;
	private int bet=0;
	
	/**
	 * @return the balance
	 */
	public int getBalance() {
		return balance;
	}

	/**
	 * @param string LOSE, WIN or BJ
	 */
	public void refreshBalance(String result) {
		switch (result) {
        case "LOSE":
            balance-=bet;
            break;
        case "WIN":
        	balance+=bet;
            break;
        case "BJ":
        	balance=(int) ((bet*1.5)+balance);
            break;
		}
	}

	/**
	 * @return the bet
	 */
	public int getBet() {
		return bet;
	}

	/**
	 * @param bet the bet to set
	 */
	public void setBet(int bet) {
		this.bet = bet;
	}

	/**
     * Constructor for Player object.
     *
     * @param null
     */

	public Player() {
		handCards = new CardGroup();
	}//end Constructor

	public Player(Card card1, Card card2) {
		handCards = new CardGroup();
		handCards.add(card1);
		handCards.add(card2);
	}

	public void newHand() {
		handCards = new CardGroup();
	}

	public int getCardTotal() {
		return handCards.blackJackValue();
	}

	public void CardHit(Card ca){
		handCards.add(ca);
	}

	
	/**
	 * Returns whether or not the hand contains an ace.
	 *
	 * @return true if the hand contains an ace, false if does not
	 */

	private boolean hasAce() {
		return handCards.hasAce();
	}

	
	/**
	 * Returns whether or not the player bust
	 *
	 * @return true if the player bust
	 */

	public boolean CheckBust(){
		return handCards.CheckBust();
	}
	
	/**
	 * Returns whether or not the hand is a blackJack.
	 *
	 * @return true if the hand contains an ace, false if does not
	 */

	public boolean hasABJ() {
		return handCards.hasABlackJack();
	}

	public String GetLoseBalance() {
		// TODO Auto-generated method stub
		return null;
	}
}
