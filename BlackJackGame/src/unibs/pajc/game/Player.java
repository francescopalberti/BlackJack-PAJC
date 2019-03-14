package unibs.pajc.game;

/**
 * Player objects represent a standard player
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */


public class Player {
	private CardGroup handCards;
	
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

	public int GetCardTotal() {
		return handCards.blackJackValue();
	}

	public void CardHit(Card ca){
		handCards.add(ca);
	}

	/**
	 * Returns the value of the hand with aces counting as
	 * either 1 or 11 depending on whether or not the hand
	 * is soft.
	 *
	 * @return the value of the hand
	 */

	public int handValue() {
		return handCards.getTotalValue();
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
}
