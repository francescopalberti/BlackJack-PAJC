package unibs.pajc.game;

import java.util.ArrayList;

/**
 * Player objects represent a standard player
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */


public class Player {
	private boolean bust=false;
	private int blackJackValue=0;
	private ArrayList<Card> handCards;
	
	/**
     * Constructor for Player object.
     *
     * @param card1 first hand card
     * @param card second hand card
     */

	public Player(Card card1, Card card2) {
		handCards = new ArrayList<Card>();
		handCards.add(card1);
		handCards.add(card2);
		blackJackValue();
	}//end Constructor


	public int GetCardTotal() {
		return blackJackValue;
	}

	public void CardHit(Card ca){
		handCards.add(ca);
		blackJackValue();
		CheckBust();
	}

	/**
	 * Returns the value of the hand with aces counting as
	 * either 1 or 11 depending on whether or not the hand
	 * is soft.
	 *
	 * @return the value of the hand
	 */

	public int handValue() {
		int value = 0;  // value of the hand in Blackjack
		for (Card card : handCards) {
			value += card.getValue();
		}
    return value;
	}
	
	/**
	 * Returns whether or not the hand contains an ace.
	 *
	 * @return true if the hand contains an ace, false if does not
	 */

	private boolean hasAce() {
		for (Card card : handCards) {
			if (card.getValue() == 1) {
				return true;
			}
		}
    return false;
	}
	
	/**
	 * Returns whether or not the hand is soft.
	 *
	 * @return true if the hand is soft, false if not
	 */

	public boolean isSoft() {
		return hasAce() && handValue() < 12;
	}

	/**
	 * Calculate the real value of the hand. 
	 * Considering if it is an hard or soft hand
	 *
	 */
	
	public void blackJackValue() {
		blackJackValue = handValue();
		if (isSoft()) {
			blackJackValue += 10;
		}
	}
	
	/**
	 * Returns whether or not the player bust
	 *
	 * @return true if the player bust
	 */

	public boolean CheckBust(){
		if(blackJackValue > 21){
			bust = true;
		}
		else
		{
			bust = false;
		}
		return bust;
	}
}
