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
	private int numPlayers;
    private ArrayList<String> playerNames;
    private CardGroup dealerCards;
    private CardGroup playerCards;
    private int betAmount;
    private int stage;
    private boolean load, multiplayer=false;

	// initialize chatServer and set up GUI
    public GuiModel(){
		stage=1;
		playerCards=new CardGroup();
		dealerCards=new CardGroup();
	}
	public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }
    public int getNumPlayers(){
        return this.numPlayers;
    }
    public void setPlayerNames(ArrayList<String> playerNames){
        this.playerNames = playerNames;
    }
    public ArrayList<String> getPlayerNames(){
        return this.playerNames;
    }
    public void setBetAmount(int betAmount){
        this.betAmount = betAmount;
    }
    public int getBetAmount(){
        return this.betAmount;
    }
    public void incrementStage(){
        this.stage++;
    }
    public int getStage(){
        return this.stage;
    }
    public void setLoad(boolean load){
        this.load = load;
    }
    public boolean getLoad(){
        return this.load;
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
	public CardGroup getPlayerCards() {
		return playerCards;
	}

}
