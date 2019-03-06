package unibs.pajc.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	private Image img;

	public ImagePanel(String imgStr) { // Constructor, passed image string
		this.img = new ImageIcon(imgStr).getImage();
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null)); // If we don't use setBounds (after instance is created), this is a fallback to the actual dimensions of the image
		setSize(size);
		setLayout(null);
	}

	public void paintComponent(Graphics g) { // Draw the image to the JPanel
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
