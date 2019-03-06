package unibs.pajc.game;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 * This class extends JPanel, and will create a panel which displays the images of a number of cards, 
 * stored in an instance of Deck, next to each other.
 *  Additionally the card total is displayed using standard Ace subtraction rule.*
 * @author Francesco Palberti, Enrico Zaninelli
 */


public class CardGroupPanel extends JPanel{
	CardGroupPanel(CardGroup cardGroup, int left, int top, int width, int height, int gap) {

	int numCards = cardGroup.getCount();

			setBounds(left, top, 35 + numCards * (width + gap), height);
			setLayout(null);
			setOpaque(false); // for transparent background

			int total = cardGroup.getTotalValue();

			JLabel playerScoreLbl = new JLabel((total == 21 ? "BJ" : total) + "");
			playerScoreLbl.setForeground(Color.WHITE);
			playerScoreLbl.setFont(new Font("Lucida Grande", Font.BOLD, 20));
			playerScoreLbl.setVerticalAlignment(SwingConstants.CENTER);
			playerScoreLbl.setHorizontalAlignment(SwingConstants.RIGHT);
			playerScoreLbl.setBounds(0, 0, 30, height);
			add(playerScoreLbl);
			
			for (int i = 0; i < numCards; i++) {
				ImagePanel cardImagePanel = new ImagePanel(cardGroup.get(i).getFileName());
				cardImagePanel.setBounds(35 + i * (width + gap), 0, width, height);
				add(cardImagePanel);
			}
		}
}
