package unibs.pajc.game;

import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.*;

public class GameFrame extends JFrame{
		GameFrame() {
			setTitle("Blackjack");
			setSize(1280, 720);
			setLocationRelativeTo(null);
			setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
			setLayout(null);
			setResizable(false);
			this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			int delete = JOptionPane.showConfirmDialog(null, "Vuoi chiudere il frame ? ","Attenzione !",JOptionPane.YES_NO_OPTION);	
			if(delete==0){
				System.exit(0);
			}
			}
			}); 
	               ImagePanel bgImagePanel = new ImagePanel("resources\\game_bkg.png");
			bgImagePanel.setBounds(0, 0, this.getWidth(), this.getHeight());
			setContentPane(bgImagePanel);
		}
}




