package unibs.pajc.game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MainForm {

	public static void main(String[] args) {
        new MainForm();
    }

    public MainForm() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("PAJC BlackJack");
                frame.add(new MenuPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class MenuPane extends JPanel {

        public MenuPane() {
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTH;

            add(new JLabel("<html><h1><strong><i>PAJC GAME</i></strong></h1><hr></html>"), gbc);

            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel buttons = new JPanel(new GridBagLayout());
            
            JButton btnSoloGame = new JButton("Solo Game");      		
     		btnSoloGame.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				
    			}
    		});
     		
     		JButton btnMultiGame = new JButton("Multiplayer Game");      		
      		btnMultiGame.addActionListener(new ActionListener() {
     			public void actionPerformed(ActionEvent e) {
     				new MainClient();
     				System.exit(0); //calling the method is a must
     			}
     		});
     		
            buttons.add(btnSoloGame, gbc);
            buttons.add(btnMultiGame, gbc);

            gbc.weighty = 1;
            add(buttons, gbc);
        }

    }

}
