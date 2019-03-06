package unibs.pajc.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CardGroup {
	public ArrayList<Card> cards;
	private boolean bust=false;
	private int blackJackValue=0;

	public CardGroup() {
		cards = new ArrayList<Card>();
	}
	public Card takeCard() { // Removes card from top of card group's ArrayList
								// and returns it
		if (this.cards.size() < 1) {
			System.out.println("Error: no more cards!");
			System.exit(0);
		}
		Card tempCard = this.cards.get(this.cards.size() - 1);
		this.cards.remove(this.cards.size() - 1);
		return tempCard;
	}

	public void shuffle() {
		long seed = System.nanoTime(); // Seed for random class
		Collections.shuffle(this.cards, new Random(seed)); // This implementation traverses the list backwards, from the last element up to the second, repeatedly swapping a randomly selected element into the "current position"
	}

	public int getTotalValue() {
		int totalValue = 0;  // value of the hand in Blackjack
		for (Card card : cards) {
			totalValue += card.getValue();
		}
    return totalValue;
	}

	public int getNumAces() {
		int numAces = 0;
		for (Card card : cards)
			if (card.getValue() == 1)
				numAces++;
		return numAces;
	}
	
	public void add(Card aCard) {
		cards.add(aCard);
	}
	
	public Card get(int i) {
		return cards.get(i);
	}

	public int getCount() {
		return this.cards.size();
	}
	
	/**
	 * Returns whether or not the hand is soft.
	 *
	 * @return true if the hand is soft, false if not
	 */

	public boolean isSoft() {
		return hasAce() && getTotalValue() < 12;
	}
	
	/**
	 * Returns whether or not the hand contains an ace.
	 *
	 * @return true if the hand contains an ace, false if does not
	 */

	public boolean hasAce() {
		for (Card card : cards) {
			if (card.getValue() == 1) {
				return true;
			}
		}
    return false;
	}
	
	/**
	 * Calculate the real value of the hand. 
	 * Considering if it is an hard or soft hand
	 *
	 */
	
	public void blackJackValue() {
		blackJackValue = getTotalValue();
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

	public void print() {
		for (Card card : cards) {
			card.toString();
		}
	}
}
