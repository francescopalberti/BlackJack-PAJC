package unibs.pajc.game;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WelcomeGui {

    JPanel outerPanel;

    JFrame welcomeFrame;
    JPanel welcomePanel;
    JLabel welcomeLabel;

    public JButton onePlayerButton;
    public JButton multiPlayerButton;

    public void display(){
        welcomeFrame = new JFrame();
        welcomePanel = new JPanel(new GridLayout(4, 0, 5, 0));
        welcomeLabel = new JLabel();

        onePlayerButton = new JButton("Solo Game");
        multiPlayerButton = new JButton("MultiPlayer");


        welcomeLabel.setText("Welcome to Blackjack");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        welcomePanel.add(welcomeLabel);

        welcomePanel.add(onePlayerButton);
        welcomePanel.add(multiPlayerButton);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // create the outer panel to center the widgets
        outerPanel = new JPanel();

        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.PAGE_AXIS));
        outerPanel.add(Box.createHorizontalGlue());
        outerPanel.add(welcomePanel);
        outerPanel.add(Box.createHorizontalGlue());

        welcomeFrame.add(welcomePanel);
        welcomeFrame.setSize(200,175);
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setVisible(true);
        welcomeFrame.pack();
        welcomeFrame.setLocationRelativeTo(null);
    }
}
