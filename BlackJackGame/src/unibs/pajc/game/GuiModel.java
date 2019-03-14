package unibs.pajc.game;
import java.util.ArrayList;

import unibs.pajc.game.Card.Rank;
import unibs.pajc.game.Card.Suit;

/**
 * GuiModel.java
 *
 * The GuiModel class for the Blackjack Game.
 * Contains GuiModel
 *
 * @author Francesco Palberti, Enrico Zaninelli
 */

public class GuiModel {
    private CardGroup dealerCards;
    private CardGroup playerCards;
    private int betAmount;
    private boolean multiplayer=false;

	// initialize chatServer and set up GUI
    public GuiModel(){
		playerCards=new CardGroup();
		dealerCards=new CardGroup();
	}
	
    
    public void setBetAmount(int betAmount){
        this.betAmount = betAmount;
    }
    
    public int getBetAmount(){
        return this.betAmount;
    }
   
	public void setMultiplayer() {
		multiplayer=true;
	}

	public boolean isMultiplayerUser() {
		return multiplayer;
	}
	
	public void addCardToDealer(String rank, String suit) {
		Rank aRank = Rank.valueOf(rank.toUpperCase());
		Suit aSuit = Suit.valueOf(suit.toUpperCase());
		dealerCards.add(new Card(aRank, aSuit));
	}
	
	public void addCardToPlayer(String rank, String suit) {
		Rank aRank = Rank.valueOf(rank.toUpperCase());
		Suit aSuit = Suit.valueOf(suit.toUpperCase());
		playerCards.add(new Card(aRank, aSuit));
	}
	
	public CardGroup getDealerCards() {
		return dealerCards;
	}
	
	public void uncoverDealerCard() {
		dealerCards.get(0).unCoverCard();
	}

	public CardGroup getPlayerCards() {
		return playerCards;
	}

	public void firstDealerCard(String rank, String suit) {
		Rank aRank = Rank.valueOf(rank.toUpperCase());
		Suit aSuit = Suit.valueOf(suit.toUpperCase());
		Card dealerCard = new Card(aRank, aSuit);
		dealerCard.coverCard();
		dealerCards.add(dealerCard);
	}

}
