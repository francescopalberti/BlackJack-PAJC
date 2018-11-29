package unibs.pajc.game;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Deck objects represent a standard deck of playing cards.
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */

public class Deck {
    private ArrayList<Card> deck = new ArrayList<>();   // a deck is an arraylist of cards

    /**
     * Constructor for Deck object.
     */

    public Deck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Returns the last card in the deck. (dealer card)
     *
     * @return the last card in the deck
     */

    public Card dealCard() {
        Card card = deck.get(deck.size() - 1);    // last card in the deck
        deck.remove(card);
        return card;
    }

    /**
     * Returns the number of cards in the deck.
     *
     * @return the number of cards in the deck
     */

    public int size() {
        return deck.size();
    }
    
    /**
     * Shuffle the cards in the deck.
     *
     * @return void
     */
    
    public void shuffle() {
    	Collections.shuffle(deck);

    }
}